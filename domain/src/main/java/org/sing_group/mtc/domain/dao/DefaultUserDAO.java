/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.dao;

import static java.util.Objects.requireNonNull;
import static org.sing_group.mtc.domain.entities.user.User.haveSameRole;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.mtc.domain.dao.spi.UserDAO;
import org.sing_group.mtc.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultUserDAO implements UserDAO {
  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, User> dh;

  DefaultUserDAO() {}

  public DefaultUserDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, User.class, this.em);
  }

  @Override
  public User get(int id) {
    return dh.get(id);
  }
  
  @Override
  public User getByEmail(String email) {
    return dh.getBy("email", email);
  }
  
  @Override
  public Stream<User> list() {
    return dh.list().stream();
  }

  @Override
  public User create(User user) {
    requireNonNull(user, "user can't be null");
    
    if (this.existsUserWithEmail(user.getEmail())) {
      throw new DuplicateEmailException("An user with the same email (" + user.getEmail() + ") already exists");
    }

    return this.dh.persist(user);
  }

  @Override
  public User update(User user) {
    requireNonNull(user, "user can't be null");
    
    final User currentUser = this.get(user.getId());

    if (!haveSameRole(user, currentUser))
      throw new IllegalArgumentException("User's role can't be changed");
    
    currentUser.setEmail(user.getEmail());
    currentUser.setName(user.getName());
    currentUser.setSurname(user.getSurname());
    if (user.getPassword() != null) {
      currentUser.setPassword(user.getPassword());
    }

    return currentUser;
  }

  @Override
  public void remove(int id) {
    this.dh.removeByKey(id);
  }

  @Override
  public boolean existsUserWithEmail(String email) {
    try {
      this.getByEmail(email);

      return true;
    } catch (NoResultException nre) {
      return false;
    }
  }
}
