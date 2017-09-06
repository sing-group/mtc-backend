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
package org.sing_group.mtc.rest.entity.user;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;

import io.swagger.annotations.ApiModel;

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
  
  @XmlElement(name = "institution", required = true)
  private URI institution;
  
  @XmlElementWrapper(name = "patients", required = false)
  @XmlElement(name = "patient")
  private URI[] patients;
  
  @XmlElementWrapper(name = "sessions", required = false)
  @XmlElement(name = "session")
  private URI[] sessions;
  
  TherapistData() {}

  public TherapistData(
    String login, String email, String name, String surname,
    URI institution, URI[] patients, URI[] sessions
  ) {
    super(login, email, name, surname, RoleType.THERAPIST);
    
    this.institution = institution;
    this.patients = patients;
    this.sessions = sessions;
  }

  public URI getInstitution() {
    return institution;
  }

  public void setInstitution(URI institution) {
    this.institution = institution;
  }

  public URI[] getPatients() {
    return patients;
  }

  public void setPatients(URI[] patients) {
    this.patients = patients;
  }

  public URI[] getSessions() {
    return sessions;
  }

  public void setSessions(URI[] sessions) {
    this.sessions = sessions;
  }

  @Override
  public String toString() {
    return "TherapistData [getLogin()=" + getLogin() + ", getEmail()=" + getEmail() + ", getName()=" + getName() + ", getSurname()=" + getSurname() + "]";
  }
}
