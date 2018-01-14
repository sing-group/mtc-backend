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

package org.sing_group.mtc.rest.entity.user;

import java.net.URI;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.rest.entity.IdAndUri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Profile data of the therapist entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "therapist-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "therapist-data", description = "Profile data of the therapist entity.")
public class TherapistData extends IdentifiedUserData {
  private static final long serialVersionUID = 1L;
  
  private IdAndUri institution;
  
  private UserUri[] patients;
  
  private IdAndUri[] sessions;
  
  TherapistData() {}

  public TherapistData(
    String login, String email, String name, String surname,
    long institutionId,
    URI institutionUri,
    String[] patientLogins,
    URI[] patientUris,
    long[] sessionIds,
    URI[] sessionUris
  ) {
    super(login, email, name, surname, RoleType.THERAPIST);
    
    this.institution = new IdAndUri(institutionId, institutionUri);
    
    this.patients = new UserUri[patientLogins.length];
    for (int i = 0; i < patientLogins.length; i++) {
      this.patients[i] = new UserUri(patientLogins[i], patientUris[i]);
    }
    
    this.sessions = new IdAndUri[sessionIds.length];
    for (int i = 0; i < sessionIds.length; i++) {
      this.sessions[i] = new IdAndUri(sessionIds[i], sessionUris[i]);
    }
  }

  @XmlElement(name = "institution", required = true)
  public IdAndUri getInstitution() {
    return institution;
  }

  public void setInstitution(IdAndUri institution) {
    this.institution = institution;
  }

  @XmlElementWrapper(name = "patients", required = false)
  @XmlElement(name = "patient")
  public UserUri[] getPatients() {
    return patients;
  }

  public void setPatients(UserUri[] patients) {
    this.patients = patients;
  }

  @XmlElementWrapper(name = "sessions", required = false)
  @XmlElement(name = "session")
  public IdAndUri[] getSessions() {
    return sessions;
  }

  public void setSessions(IdAndUri[] sessions) {
    this.sessions = sessions;
  }

  @ApiModelProperty(allowableValues = "THERAPIST", required = true)
  @Override
  public RoleType getRole() {
    return super.getRole();
  }

  @Override
  public void setRole(RoleType role) {
    if (role != RoleType.THERAPIST)
      throw new IllegalArgumentException("Invalid role. Only " + RoleType.THERAPIST + " role is admitted");
  }
  
  @Override
  public String toString() {
    return "TherapistData [getLogin()=" + getLogin() + ", getEmail()=" + getEmail() + ", getName()=" + getName()
      + ", getSurname()=" + getSurname() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((institution == null) ? 0 : institution.hashCode());
    result = prime * result + Arrays.hashCode(patients);
    result = prime * result + Arrays.hashCode(sessions);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    TherapistData other = (TherapistData) obj;
    if (institution == null) {
      if (other.institution != null)
        return false;
    } else if (!institution.equals(other.institution))
      return false;
    if (!Arrays.equals(patients, other.patients))
      return false;
    if (!Arrays.equals(sessions, other.sessions))
      return false;
    return true;
  }
}
