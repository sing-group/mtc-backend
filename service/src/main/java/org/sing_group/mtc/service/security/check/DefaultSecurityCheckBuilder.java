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
package org.sing_group.mtc.service.security.check;

import static java.util.Arrays.stream;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

import java.util.Arrays;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Default;

import org.sing_group.mtc.domain.entities.user.RoleType;

@Stateless
@Default
@PermitAll
@TransactionAttribute(SUPPORTS)
public class DefaultSecurityCheckBuilder implements SecurityCheckBuilder {
  @Resource
  private SessionContext context;

  @Override
  public SecurityCheck hasRole(RoleType role) {
    return () -> {
      if (this.context.isCallerInRole(role.name())) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user is not in role: " + role);
      }
    };
  }

  @Override
  public SecurityCheck hasAnyRoleOf(RoleType ... roles) {
    return () -> {
      if (stream(roles).map(RoleType::name).anyMatch(this.context::isCallerInRole)) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user is not in roles: " + Arrays.toString(roles));
      }
    };
  }
  
  @Override
  public SecurityCheck hasLogin(String login) {
    return this.hasLogin(() -> login);
  }
  
  @Override
  public SecurityCheck hasLogin(Supplier<String> loginSupplier) {
    return () -> {
      if (this.context.getCallerPrincipal().getName().equals(loginSupplier.get())) {
        return SecurityCheckResult.valid();
      } else {
        return SecurityCheckResult.invalid("user login is not the expected: " + loginSupplier.get());
      }
    };
  }
}
