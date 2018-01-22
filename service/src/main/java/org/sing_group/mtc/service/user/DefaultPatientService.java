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

package org.sing_group.mtc.service.user;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;
import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.game.session.AssignedGamesSessionDAO;
import org.sing_group.mtc.domain.dao.spi.game.session.GamesSessionDAO;
import org.sing_group.mtc.domain.dao.spi.user.PatientDAO;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.security.check.SecurityCheck;
import org.sing_group.mtc.service.security.check.SecurityCheckBuilder;
import org.sing_group.mtc.service.spi.user.PatientService;

@Stateless
@RolesAllowed("THERAPIST")
public class DefaultPatientService implements PatientService {
  @Inject
  private PatientDAO dao;
  
  @Inject
  private GamesSessionDAO gamesSessionDao;
  
  @Inject
  private AssignedGamesSessionDAO assignedSessionsDao;

  @Inject
  private SecurityGuard securityGuard;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  private SecurityCheck checkThatIsTherapistOf(Patient therapist) {
    final String therapistLogin = therapist.getTherapist().getLogin();
    
    return checkThat.hasLoginAndRole(therapistLogin, RoleType.THERAPIST);
  }
  
  private SecurityCheck checkThatIsTherapistOf(String patientLogin) {
    return this.checkThatIsTherapistOf(this.dao.get(patientLogin));
  }

  private SecurityCheck checkThatIsPatientOfAssignedSession(long assignedSessionId) {
    return checkThat.hasLogin(this.assignedSessionsDao.get(assignedSessionId).getPatientLogin());
  }
  
  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public Patient get(String login) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    
    return this.securityGuard.ifAuthorized(
      checkThat.hasLoginAndRole(login, RoleType.PATIENT),
      checkThatIsTherapistOf(login)
    ).call(() -> dao.get(login));
  }

  @Override
  public Stream<Patient> list(ListingOptions listingOptions) {
    final Therapist therapist = this.securityGuard.getLoggedUser();
    
    return dao.listByTherapist(therapist, listingOptions);
  }

  @Override
  public long count() {
    final Therapist therapist = this.securityGuard.getLoggedUser();
    
    return therapist.getPatients().count();
  }
  
  @Override
  public Patient create(Patient patient) {
    return this.securityGuard.ifAuthorized(
      checkThatIsTherapistOf(patient)
    ).call(() -> dao.create(patient));
  }
  
  @Override
  public Patient update(Patient patient) {
    return this.securityGuard.ifAuthorized(
      checkThatIsTherapistOf(patient.getLogin())
    ).call(() -> dao.update(patient));
  }

  @Override
  public void delete(String login) {
    this.securityGuard.ifAuthorized(
      checkThatIsTherapistOf(login)
    ).run(() -> dao.delete(login));
  }

  @RolesAllowed({ "THERAPIST", "PATIENT" })
  @Override
  public Stream<AssignedGamesSession> listAssignedSessions(String login, ListingOptions options) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    requireNonNull(options, "'options' can't be null");
    
    return this.securityGuard.ifAuthorized(
      checkThat.hasLoginAndRole(login, RoleType.PATIENT),
      checkThat.hasLoginAndRole(() -> this.dao.get(login).getTherapist().getLogin(), RoleType.THERAPIST)
    ).call(() -> this.assignedSessionsDao.listByPatient(this.dao.get(login), options));
  }

  @Override
  public AssignedGamesSession assignSession(String login, long gamesSessionId, Date startDate, Date endDate) {
    requireStringSize(login, 1, 100, "'login' should have a length between 1 and 100");
    requireNonNull(startDate, "'startDate' can't be null");
    requireNonNull(endDate, "'startDate' can't be null");

    return this.securityGuard.ifFullyAuthorized(
      checkThatIsTherapistOf(login),
      checkThat.hasLogin(() -> this.gamesSessionDao.get(gamesSessionId).getTherapist().map(Therapist::getLogin).orElse(""))
    ).call(() -> this.assignedSessionsDao.assignSession(
      this.dao.get(login),
      this.gamesSessionDao.get(gamesSessionId),
      startDate, endDate
    ));
  }
  
  @RolesAllowed("PATIENT")
  @Override
  public GameResult addGameResult(
    long assignedSessionId, int gameIndex, Date startDate, Date endDate, Map<String, String> results
  ) {
    requireNonEmpty(results);
    
    return this.securityGuard.ifAuthorized(
      this.checkThatIsPatientOfAssignedSession(assignedSessionId)
    ).call(() -> 
      this.assignedSessionsDao.addGameResult(assignedSessionId, gameIndex, startDate, endDate, results)
    );
  }
}
