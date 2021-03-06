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

import static org.sing_group.mtc.service.security.check.SecurityCheck.eval;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.GameResultDAO;
import org.sing_group.mtc.domain.dao.spi.game.session.AssignedGamesSessionDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
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
  private GameResultDAO resultsDao;
  
  @Inject
  private SecurityGuard securityManager;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public AssignedGamesSession get(long assignedId) {
    final AssignedGamesSession assignedGamesSession = this.sessionDao.get(assignedId);
    
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(assignedGamesSession.getPatientLogin()),
      checkThat.hasLogin(assignedGamesSession.getTherapistLogin())
    ).call(() -> assignedGamesSession);
  }

  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public Stream<AssignedGamesSession> list(ListingOptions options) {
    if (eval(checkThat.hasRole(RoleType.THERAPIST))) {
      final Therapist therapist = this.securityManager.getLoggedUser();
      
      return this.sessionDao.listByTherapist(therapist, options);
    } else if (eval(checkThat.hasRole(RoleType.PATIENT))) {
      final Patient patient = this.securityManager.getLoggedUser();
      
      return this.sessionDao.listByPatient(patient, options);
    } else {
      throw new SecurityException("Illegal access. Cause: user is not in THERAPIST or PATIENT role");
    }
  }

  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public long count() {
    if (eval(checkThat.hasRole(RoleType.THERAPIST))) {
      final Therapist therapist = this.securityManager.getLoggedUser();
      
      return therapist.getSessions().flatMap(GamesSession::getAssignedGamesSessions).count();
    } else if (eval(checkThat.hasRole(RoleType.PATIENT))) {
      final Patient patient = this.securityManager.getLoggedUser();
      
      return patient.getAssignedGameSessions().count();
    } else {
      throw new SecurityException("Illegal access. Cause: user is not in THERAPIST or PATIENT role");
    }
  }
  
  @Override
  public AssignedGamesSession modify(AssignedGamesSession assigned) {
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(this.get(assigned.getId()).getTherapistLogin())
    ).call(() -> this.sessionDao.modify(assigned));
  }

  @Override
  public void delete(int sessionId) {
    this.securityManager.ifAuthorized(
      checkThat.hasLogin(this.get(sessionId).getTherapistLogin())
    ).run(() -> this.sessionDao.delete(sessionId));
  }
  
  @Override
  public Stream<GameResult> listResultsOf(int assignedId, ListingOptions options) {
    final AssignedGamesSession assignedGamesSession = this.sessionDao.get(assignedId);
    
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(assignedGamesSession.getPatientLogin()),
      checkThat.hasLogin(assignedGamesSession.getTherapistLogin())
    ).call(() -> this.resultsDao.listByAssignedGamesSession(assignedGamesSession, options));
  }
  
  @Override
  public long countResultsOf(int assignedId) {
    final AssignedGamesSession assignedGamesSession = this.sessionDao.get(assignedId);
    
    return this.securityManager.ifAuthorized(
      checkThat.hasLogin(assignedGamesSession.getPatientLogin()),
      checkThat.hasLogin(assignedGamesSession.getTherapistLogin())
    ).call(() -> assignedGamesSession.getGameResults().count());
  }
}
