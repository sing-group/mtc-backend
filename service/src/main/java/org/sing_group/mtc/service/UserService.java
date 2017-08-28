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

import static org.sing_group.fluent.checker.Checks.requireEmail;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.UserDAO;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.service.security.SecurityGuard;

/**
 * EJB for the RegularUsers. Only administrators have access to this class.
 * 
 * @author Miguel Reboiro-Jato
 */
@Stateless
@RolesAllowed("ADMIN")
public class UserService {
  @Inject
  private UserDAO dao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @PermitAll
  public User get(String email) {
    requireEmail(email, 100, "email should have and email format with a maximum length of 100 characters");
    
    return this.securityManager.ifAuthorized(new String[] { "ADMIN" }, () -> email, () -> dao.getByEmail(email));
  }

  @PermitAll
  public User get(int id) {
    return this.securityManager.ifAuthorized(new String[] { "ADMIN" }, () -> dao.get(id).getEmail(), () -> dao.get(id));
  }

  public Stream<User> list() {
    return this.dao.list();
  }

  public User create(User user) {
    return this.dao.create(user);
  }

  public User update(User user) {
    return this.dao.update(user);
  }

  public void remove(int id) {
    this.dao.remove(id);
  }
}
