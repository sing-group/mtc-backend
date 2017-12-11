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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

/**
 * Creation data of the patient entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "patient-creation-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "patient-creation-data", description = "Creation data of the patient entity.")
public class PatientCreationData extends PatientEditionData {
  private static final long serialVersionUID = 1L;

  private String login;

  private String therapist;
  
  PatientCreationData() {}

  public PatientCreationData(String login, String password, String therapist) {
    super(password);
    
    this.login = login;
    this.therapist = therapist;
  }

  @XmlElement(name = "login", required = true)
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  @XmlElement(name = "therapist", required = true)
  public String getTherapist() {
    return therapist;
  }

  public void setTherapist(String therapist) {
    this.therapist = therapist;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((login == null) ? 0 : login.hashCode());
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
    PatientCreationData other = (PatientCreationData) obj;
    if (login == null) {
      if (other.login != null)
        return false;
    } else if (!login.equals(other.login))
      return false;
    if (therapist == null) {
      if (other.therapist != null)
        return false;
    } else if (!therapist.equals(other.therapist))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "PatientCreationData [getLogin()=" + getLogin() + ", getTherapist()=" + getTherapist() + ", getPassword()="
      + getPassword() + "]";
  }
  
}
