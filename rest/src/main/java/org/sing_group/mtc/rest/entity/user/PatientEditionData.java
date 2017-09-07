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

import org.sing_group.mtc.domain.entities.user.RoleType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Profile data of the patient entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "patient-edition-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "patient-edition-data", description = "Edition data of the patient entity.")
public class PatientEditionData extends UserEditionData {
  private static final long serialVersionUID = 1L;
  
  private String therapist;

  PatientEditionData() {
    super();
  }
  
  public PatientEditionData(String login, String password, String therapist) {
    super(login, password, RoleType.PATIENT);
    
    this.therapist = therapist;
  }

  @XmlElement(name = "therapist", required = true)
  public String getTherapist() {
    return therapist;
  }

  public void setTherapist(String therapist) {
    this.therapist = therapist;
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
    return "PatientEditionData [getLogin()=" + getLogin() + ", getPassword()=" + getPassword() + "]";
  }
  
}
