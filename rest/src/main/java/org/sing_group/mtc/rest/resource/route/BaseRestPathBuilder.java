/*
 * #%L
 * REST
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
package org.sing_group.mtc.rest.resource.route;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;

public final class BaseRestPathBuilder implements RestPathBuilder {
  private final UriBuilder builder;
  
  public BaseRestPathBuilder(UriBuilder builder) {
    this.builder = builder;
  }
  
  public GamesSessionWithIdRestPathBuilder gamesSession(long id) {
    return new GamesSessionWithIdRestPathBuilder(this.builder, id);
  }
  
  public GamesSessionWithIdRestPathBuilder gamesSession(GamesSession session) {
    return gamesSession(session.getId());
  }
  
  public GamesSessionAssignedWithIdRestPathBuilder gamesSessionAssigned(long id) {
    return new GamesSessionAssignedWithIdRestPathBuilder(this.builder, id);
  }
  
  public GamesSessionAssignedWithIdRestPathBuilder gamesSessionAssigned(AssignedGamesSession assignedSession) {
    return gamesSessionAssigned(assignedSession.getId());
  }
  
  public AdminRestPathBuilder admin() {
    return new AdminRestPathBuilder(this.builder);
  }
  
  public AdminRestPathBuilder admin(String login) {
    return new AdminRestPathBuilder(this.builder, login);
  }
  
  public AdminRestPathBuilder admin(Administrator admin) {
    return admin(admin.getLogin());
  }
  
  public ManagerRestPathBuilder manager() {
    return new ManagerRestPathBuilder(builder);
  }
  
  public ManagerWithResourcesRestPathBuilder manager(String login) {
    return new ManagerWithResourcesRestPathBuilder(builder, login);
  }
  
  public ManagerWithResourcesRestPathBuilder manager(Manager manager) {
    return manager(manager.getLogin());
  }
  
  public TherapistRestPathBuilder therapist() {
    return new TherapistRestPathBuilder(builder);
  }
  
  public TherapistWithResourcesRestPathBuilder therapist(String login) {
    return new TherapistWithResourcesRestPathBuilder(builder, login);
  }
  
  public TherapistWithResourcesRestPathBuilder therapist(Therapist therapist) {
    return therapist(therapist.getLogin());
  }
  
  public PatientRestPathBuilder patient() {
    return new PatientRestPathBuilder(builder);
  }
  
  public PatientRestPathBuilder patient(String login) {
    return new PatientRestPathBuilder(builder, login);
  }
  
  public PatientRestPathBuilder patient(Patient patient) {
    return patient(patient.getLogin());
  }
  
  public InstitutionRestPathBuilder institution() {
    return new InstitutionRestPathBuilder(this.builder);
  }
  
  public InstitutionWithResourcesRestPathBuilder institution(long id) {
    return new InstitutionWithResourcesRestPathBuilder(this.builder, id);
  }

  public InstitutionWithResourcesRestPathBuilder institution(Institution institution) {
    return institution(institution.getId());
  }
  
  public UserRoleRestPathBuilder userRole() {
    return new UserRoleRestPathBuilder(this.builder);
  }
  
  public GameResultRestPathBuilder gameResult() {
    return new GameResultRestPathBuilder(this.builder);
  }
  
  public GameResultRestPathBuilder gameResult(long id) {
    return new GameResultRestPathBuilder(this.builder, id);
  }
  
  public GameResultRestPathBuilder gameResult(GameResult result) {
    return gameResult(result.getId());
  }

  public URI build() {
    return this.builder.build();
  }
  
}
