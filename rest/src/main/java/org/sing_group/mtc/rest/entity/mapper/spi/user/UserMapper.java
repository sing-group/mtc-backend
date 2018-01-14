/*-
 * #%L
 * REST
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

package org.sing_group.mtc.rest.entity.mapper.spi.user;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.user.AdministratorCreationData;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.AdministratorEditionData;
import org.sing_group.mtc.rest.entity.user.ManagerCreationData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.entity.user.PatientCreationData;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.PatientEditionData;
import org.sing_group.mtc.rest.entity.user.TherapistCreationData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.entity.user.TherapistEditionData;

public interface UserMapper {

  public AdministratorData toData(Administrator admin);
  
  public AdministratorCreationData toCreationData(Administrator admin, String password);

  public AdministratorEditionData toEditionData(Administrator admin, String password);

  public ManagerData toData(Manager manager, UriBuilder uriBuilder);
  
  public ManagerCreationData toCreationData(Manager manager, String password);

  public ManagerEditionData toEditionData(Manager manager, String password);

  public TherapistData toData(Therapist therapist, UriBuilder uriBuilder);
  
  public TherapistCreationData toCreationData(Therapist therapist, String password);

  public TherapistEditionData toEditionData(Therapist therapist, String password);

  public PatientData toData(Patient patient, UriBuilder uriBuilder);
  
  public PatientCreationData toCreationData(Patient patient, String password);

  public PatientEditionData toEditionData(Patient patient, String password);
  
  public Administrator toAdministrator(AdministratorCreationData data);
  
  public Administrator toAdministrator(String login, AdministratorEditionData data);
  
  public Manager toManager(ManagerCreationData data);
  
  public Manager toManager(String login, ManagerEditionData data);
  
  public Therapist toTherapist(TherapistCreationData data, Institution institution);
  
  public Therapist toTherapist(String login, TherapistEditionData data);
  
  public Patient toPatient(PatientCreationData data, Therapist therapist);
  
  public Patient toPatient(String login, PatientEditionData data);

}
