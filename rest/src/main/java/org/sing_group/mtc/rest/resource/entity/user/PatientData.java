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
package org.sing_group.mtc.rest.resource.entity.user;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;

@XmlRootElement(name = "manager-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientData extends UserData {
  private static final long serialVersionUID = 1L;
  
  @XmlElement(name = "therapist", required = true)
  private URI therapist;
  
  @XmlElementWrapper(name = "assignedSessions")
  @XmlElement(name = "assignedSession")
  private URI[] assignedSession;
  
  PatientData() {}

  public PatientData(String login, URI therapist, URI[] assignedSessions) {
    super(login, RoleType.MANAGER);
    
    this.therapist = therapist;
  }
  
  public URI getTherapist() {
    return therapist;
  }

  public void setTherapist(URI therapist) {
    this.therapist = therapist;
  }

  public URI[] getAssignedSession() {
    return assignedSession;
  }

  public void setAssignedSession(URI[] assignedSession) {
    this.assignedSession = assignedSession;
  }

  @Override
  public String toString() {
    return "PatientData [login=" + login + "]";
  }
}
