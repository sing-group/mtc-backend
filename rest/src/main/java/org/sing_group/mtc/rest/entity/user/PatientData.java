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
 * Profile data of the patient entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "patient-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "patient-data", description = "Profile data of the patient entity.")
public class PatientData extends UserData {
  private static final long serialVersionUID = 1L;
  
  protected UserUri therapist;
  
  protected IdAndUri[] assignedSession;
  
  PatientData() {}

  public PatientData(
    String login,
    String therapistLogin,
    URI therapistUri,
    long[] assignedSessionIds,
    URI[] assignedSessionUris
  ) {
    super(login, RoleType.PATIENT);
    
    this.therapist = new UserUri(therapistLogin, therapistUri);
    
    this.assignedSession = new IdAndUri[assignedSessionIds.length];
    for (int i = 0; i < assignedSessionIds.length; i++) {
      this.assignedSession[i] = new IdAndUri(assignedSessionIds[i], assignedSessionUris[i]);
    }
  }

  @XmlElement(name = "therapist", required = true)
  public UserUri getTherapist() {
    return therapist;
  }

  public void setTherapist(UserUri therapist) {
    this.therapist = therapist;
  }

  @XmlElementWrapper(name = "assignedSessions")
  @XmlElement(name = "assignedSession", required = false)
  public IdAndUri[] getAssignedSession() {
    return assignedSession;
  }

  public void setAssignedSession(IdAndUri[] assignedSession) {
    this.assignedSession = assignedSession;
  }

  @ApiModelProperty(allowableValues = "PATIENT", required = true)
  @Override
  public RoleType getRole() {
    return super.getRole();
  }

  @Override
  public void setRole(RoleType role) {
    if (role != RoleType.PATIENT)
      throw new IllegalArgumentException("Invalid role. Only " + RoleType.PATIENT + " role is admitted");
  }

  @Override
  public String toString() {
    return "PatientData [getLogin()=" + getLogin() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(assignedSession);
    result = prime * result + ((therapist == null) ? 0 : therapist.hashCode());
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
    PatientData other = (PatientData) obj;
    if (!Arrays.equals(assignedSession, other.assignedSession))
      return false;
    if (therapist == null) {
      if (other.therapist != null)
        return false;
    } else if (!therapist.equals(other.therapist))
      return false;
    return true;
  }
}
