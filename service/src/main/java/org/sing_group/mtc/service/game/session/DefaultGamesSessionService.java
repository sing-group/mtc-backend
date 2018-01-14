/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Miguel Reboiro-Jato, Adolfo Piñón Blanco,
 *     Hugo López-Fernández, Rosalía Laza Fidalgo, Reyes Pavón Rial,
 *     Francisco Otero Lamas, Adrián Varela Pomar, Carlos Spuch Calvar,
 *     and Tania Rivera Baltanás
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

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.game.session.GamesSessionDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.security.check.SecurityCheck;
import org.sing_group.mtc.service.security.check.SecurityCheckBuilder;
import org.sing_group.mtc.service.spi.game.session.GamesSessionService;

@Stateless
@Default
@RolesAllowed("THERAPIST")
public class DefaultGamesSessionService implements GamesSessionService {
  @Inject
  private GamesSessionDAO sessionDao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  @Override
  public GamesSession create(GamesSession gameSession) {
    gameSession.setTherapist(this.securityManager.getLoggedUser());
    
    return this.sessionDao.persist(gameSession);
  }

@RolesAllowed({"THERAPIST", "PATIENT"})
  @Override
  public GamesSession get(long sessionId) {
    final GamesSession gamesSession = this.sessionDao.get(sessionId);
    
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(gamesSession.getTherapistLogin()),
      this.checkThatHasAssignedGamesSession(sessionId)
    ).call(() -> gamesSession);
  }

  private SecurityCheck checkThatHasAssignedGamesSession(long sessionId) {
    try {
      final GamesSession session = this.sessionDao.get(sessionId);
      
      final String[] patientLogins = session.getAssignedGamesSessions()
        .map(AssignedGamesSession::getPatient)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Patient::getLogin)
        .distinct()
      .toArray(String[]::new);
      
      return this.checkThat.hasAnyLoginOf(patientLogins);
    } catch (IllegalArgumentException iae) {
      return SecurityCheck.forbidden();
    }
  }
  
  @Override
  public GamesSession modify(GamesSession session) {
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(this.get(session.getId()).getTherapistLogin())
    ).call(() -> this.sessionDao.modify(session));
  }
  
  @Override
  public void delete(int sessionId) {
    this.securityManager.ifAuthorized(
      checkThat.hasLogin(this.get(sessionId).getTherapistLogin())
    ).run(() -> this.sessionDao.delete(sessionId));
  }
}
