/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 Miguel Reboiro-Jato and Adolfo Piñón Blanco
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.mtc.service;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.sing_group.mtc.domain.entities.User.haveSameRole;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.resource.spi.SecurityException;

import org.sing_group.mtc.domain.entities.User;

/**
 * EJB for the RegularUsers. Only administrators have access to this class.
 * 
 * @author Miguel Reboiro Jato
 */
@Stateless
@RolesAllowed("ADMIN")
public class UserService {
  @PersistenceContext
  private EntityManager em;
  
  @Resource
  private SessionContext ctx;
  
  private boolean isAdminOrUser(String email) {
    return ctx.isCallerInRole("ADMIN") || ctx.getCallerPrincipal().getName().equalsIgnoreCase(email);
  }

  @RolesAllowed({ "ADMIN", "PATIENT" })
  public User get(String email) throws SecurityException {
    notBlank(email, "Email can't be null or empty");
    
    if (this.isAdminOrUser(email)) {
      return em.createQuery("SELECT u FROM User u WHERE upper(email) = upper(:email)", User.class)
        .setParameter("email", email)
      .getSingleResult();
    } else {
      throw new SecurityException("Illegal access");
    }
  }

  @RolesAllowed({ "ADMIN", "PATIENT" })
  public User get(long id) throws SecurityException {
    final User patient = em.find(User.class, id);

    if (this.isAdminOrUser(patient.getEmail())) {
      return patient;
    } else {
      throw new SecurityException("Illegal access");
    }
  }

  public Stream<User> list() {
    return em.createQuery("SELECT user FROM User user", User.class)
      .getResultList().stream();
  }

  public User create(User user) {
    if (user == null)
      throw new IllegalArgumentException("user can't be null");
    
    if (existsUserWithEmail(user.getEmail())) {
      throw new DuplicateEmailException("An user with the same email (" + user.getEmail() + ") already exists");
    }

    this.em.persist(user);

    return user;
  }

  private boolean existsUserWithEmail(String email) {
    try {
      this.get(email);
      return true;
    } catch (NoResultException nre) {
      return false;
    } catch (SecurityException se) {
      throw new RuntimeException("Unexpected security exception", se);
    }
  }

  public User update(User user) {
    if (user == null)
      throw new IllegalArgumentException("user can't be null");
    
    final User currentUser = this.em.find(User.class, user.getId());
    if (user.getPassword() == null) {
      user.setPassword(currentUser.getPassword());
    }
    
    if (!haveSameRole(user, currentUser))
      throw new IllegalArgumentException("User's role can't be changed");

    return em.merge(user);
  }

  public void remove(long id) {
    em.remove(this.em.find(User.class, id));
  }
}
