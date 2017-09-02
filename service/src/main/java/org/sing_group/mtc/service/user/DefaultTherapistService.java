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
package org.sing_group.mtc.service.user;

import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.user.TherapistDAO;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.spi.user.TherapistService;

@Stateless
@RolesAllowed("MANAGER")
public class DefaultTherapistService implements TherapistService {
  @Inject
  private TherapistDAO dao;

  @Inject
  private SecurityGuard securityGuard;
  
  @Override
  @RolesAllowed({ "MANAGER", "THERAPIST" })
  public Therapist get(String login) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    
    return this.securityGuard.ifAuthorized(RoleType.MANAGER, () -> login, () -> dao.get(login));
  }

  @Override
  public Stream<Therapist> list() {
    return dao.list();
  }
  
  @Override
  public Therapist create(Therapist therapist) {
    return dao.create(therapist);
  }
  
  @Override
  public Therapist update(Therapist therapist) {
    return dao.update(therapist);
  }

  @Override
  public void delete(String login) {
    dao.delete(login);
  }
}
