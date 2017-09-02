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
package org.sing_group.mtc.service.security;

import static java.util.Arrays.stream;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.user.UserDAO;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.User;

@Stateless
@Default
@PermitAll
public class DefaultSecurityGuard implements SecurityGuard {
  @Inject
  private UserDAO userDao;
  
  @Resource
  private SessionContext context;

  @TransactionAttribute(SUPPORTS)
  @Override
  public void ifAuthorized(RoleType[] roles, Supplier<String> loginSupplier, Runnable action) {
    if (stream(roles).map(RoleType::name).anyMatch(this.context::isCallerInRole) ||
      this.context.getCallerPrincipal().getName().equals(loginSupplier.get())
    ) {
      action.run();
    } else {
      throw new SecurityException("Illegal access of user: " + this.context.getCallerPrincipal().getName());
    }
  }

  @TransactionAttribute(SUPPORTS)
  @Override
  public <T> T ifAuthorized(RoleType[] roles, Supplier<String> loginSupplier, Supplier<T> action) {
    if (stream(roles).map(RoleType::name).anyMatch(this.context::isCallerInRole) ||
      this.context.getCallerPrincipal().getName().equals(loginSupplier.get())
    ) {
      return action.get();
    } else {
      throw new SecurityException("Illegal access of user: " + this.context.getCallerPrincipal().getName());
    }
  }

  @TransactionAttribute(SUPPORTS)
  @Override
  public void ifAuthorized(RoleType role, Supplier<String> loginSupplier, Runnable action) {
    this.ifAuthorized(new RoleType[] { role }, loginSupplier, action);
  }

  @TransactionAttribute(SUPPORTS)
  @Override
  public <T> T ifAuthorized(RoleType role, Supplier<String> loginSupplier, Supplier<T> action) {
    return this.ifAuthorized(new RoleType[] { role }, loginSupplier, action);
  }

  @TransactionAttribute(REQUIRED)
  @Override
  @SuppressWarnings("unchecked")
  public <U extends User> U getLoggedUser() {
    return (U) this.userDao.get(this.context.getCallerPrincipal().getName());
  }

}
