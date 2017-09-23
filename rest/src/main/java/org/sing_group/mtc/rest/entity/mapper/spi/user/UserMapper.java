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
package org.sing_group.mtc.rest.entity.mapper.spi.user;

import java.net.URI;
import java.util.function.Function;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.AdministratorEditionData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.PatientEditionData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.entity.user.TherapistEditionData;

public interface UserMapper {

  public AdministratorData toData(Administrator admin);

  public AdministratorEditionData toEditionData(Administrator admin, String password);

  public ManagerData toData(Manager manager, Function<Institution, URI> institutionToURI);

  public ManagerEditionData toEditionData(Manager manager, String password);

  public TherapistData toData(
    Therapist therapist,
    Function<Institution, URI> institutionToURI,
    Function<Patient, URI> patientToURI,
    Function<GamesSession, URI> sessionToURI
  );

  public TherapistEditionData toEditionData(Therapist therapist, String password);

  public PatientData toData(
    Patient patient,
    Function<Therapist, URI> therapistToUri,
    Function<AssignedGamesSession, URI> assignedSessionToURI
  );

  public PatientEditionData toEditionData(Patient patient, String password);

  public Administrator toAdministrator(AdministratorEditionData data);

  public Manager toManager(ManagerEditionData data);

  public Therapist toTherapist(TherapistEditionData data, Institution institution);

  public Patient toPatient(PatientEditionData data, Therapist therapist);

}