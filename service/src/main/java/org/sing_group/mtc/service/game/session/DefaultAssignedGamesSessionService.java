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
package org.sing_group.mtc.service.game.session;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.game.session.AssignedGamesSessionDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.security.check.SecurityCheckBuilder;
import org.sing_group.mtc.service.spi.game.session.AssignedGamesSessionService;

@Stateless
@Default
@RolesAllowed("THERAPIST")
public class DefaultAssignedGamesSessionService implements AssignedGamesSessionService {
  @Inject
  private AssignedGamesSessionDAO sessionDao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public AssignedGamesSession get(int assignedId) {
    final AssignedGamesSession assignedGamesSession = this.sessionDao.get(assignedId);
    
    final String patient = assignedGamesSession.getPatient()
      .map(Patient::getLogin)
    .orElseThrow(IllegalStateException::new);
    
    final String therapist = assignedGamesSession.getSession()
      .flatMap(GamesSession::getTherapist)
      .map(Therapist::getLogin)
    .orElseThrow(IllegalStateException::new);
    
    return this.securityManager.ifAuthorized(
      checkThat.hasLoginAndRole(patient, RoleType.PATIENT),
      checkThat.hasLoginAndRole(therapist, RoleType.THERAPIST)
    ).call(() -> assignedGamesSession);
  }
}
