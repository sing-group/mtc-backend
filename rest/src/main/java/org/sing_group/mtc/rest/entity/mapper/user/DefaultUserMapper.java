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
package org.sing_group.mtc.rest.entity.mapper.user;

import java.net.URI;
import java.util.function.Function;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.AdministratorEditionData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.PatientEditionData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.entity.user.TherapistEditionData;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;

@Default
public class DefaultUserMapper implements UserMapper {
  @Override
  public AdministratorData toData(Administrator admin) {
    return new AdministratorData(
      admin.getLogin(),
      admin.getEmail(),
      admin.getName().orElse(null),
      admin.getSurname().orElse(null)
    );
  }
  
  @Override
  public AdministratorEditionData toEditionData(Administrator admin, String password) {
    return new AdministratorEditionData(
      admin.getLogin(),
      password,
      admin.getEmail(),
      admin.getName().orElse(null),
      admin.getSurname().orElse(null)
    );
  }
  
  @Override
  public ManagerData toData(Manager manager, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    final Function<Institution, URI> institutionToURI = institution -> pathBuilder.institution(institution).build();
    
    return new ManagerData(
      manager.getLogin(),
      manager.getEmail(),
      manager.getName().orElse(null),
      manager.getSurname().orElse(null),
      manager.getInstitutions()
        .map(institutionToURI)
      .toArray(URI[]::new)
    );
  }
  
  @Override
  public ManagerEditionData toEditionData(Manager manager, String password) {
    return new ManagerEditionData(
      manager.getLogin(),
      password,
      manager.getEmail(),
      manager.getName().orElse(null),
      manager.getSurname().orElse(null)
    );
  }
  
  @Override
  public TherapistData toData(Therapist therapist, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Function<Institution, URI> institutionToURI = institution -> pathBuilder.institution(institution).build();
    final Function<Patient, URI> patientToURI = patient -> pathBuilder.patient(patient).build();
    final Function<GamesSession, URI> sessionToURI = session -> pathBuilder.gamesSession(session).build();
    
    return new TherapistData(
      therapist.getLogin(),
      therapist.getEmail(),
      therapist.getName().orElse(null),
      therapist.getSurname().orElse(null),
      institutionToURI.apply(therapist.getInstitution()),
      therapist.getPatients()
        .map(patientToURI)
      .toArray(URI[]::new),
      therapist.getSessions()
        .map(sessionToURI)
      .toArray(URI[]::new)
    );
  }
  
  @Override
  public TherapistEditionData toEditionData(Therapist therapist, String password) {
    return new TherapistEditionData(
      therapist.getLogin(),
      password,
      therapist.getEmail(),
      therapist.getName().orElse(null),
      therapist.getSurname().orElse(null),
      therapist.getInstitution().getId()
    );
  }
  
  @Override
  public PatientData toData(Patient patient, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Function<Therapist, URI> therapistToUri =
      therapist -> pathBuilder.therapist(therapist).build();
    final Function<AssignedGamesSession, URI> assignedSessionToURI =
      assigned -> pathBuilder.gamesSessionAssigned(assigned).build();
    
    return new PatientData(
      patient.getLogin(),
      therapistToUri.apply(patient.getTherapist()),
      patient.getAssignedGameSessions()
        .map(assignedSessionToURI)
      .toArray(URI[]::new)
    );
  }
  
  @Override
  public PatientEditionData toEditionData(Patient patient, String password) {
    return new PatientEditionData(
      patient.getLogin(),
      password,
      patient.getTherapist().getLogin()
    );
  }

  @Override
  public Administrator toAdministrator(AdministratorEditionData data) {
    return new Administrator(data.getLogin(), data.getEmail(), data.getPassword(), data.getName(), data.getSurname());
  }

  @Override
  public Manager toManager(ManagerEditionData data) {
    return new Manager(data.getLogin(), data.getEmail(), data.getPassword(), data.getName(), data.getSurname(), null);
  }
  
  @Override
  public Therapist toTherapist(TherapistEditionData data, Institution institution) {
    return new Therapist(data.getLogin(), data.getEmail(), data.getPassword(), institution, data.getName(), data.getSurname(), null, null);
  }
  
  @Override
  public Patient toPatient(PatientEditionData data, Therapist therapist) {
    return new Patient(data.getLogin(), data.getPassword(), therapist);
  }
}
