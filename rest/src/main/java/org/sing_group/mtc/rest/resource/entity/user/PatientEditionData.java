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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;

import io.swagger.annotations.ApiModel;

/**
 * Profile data of the patient entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "patient-edition-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "patient-edition-data", description = "Edition data of the patient entity.")
public class PatientEditionData extends UserEditionData {
  private static final long serialVersionUID = 1L;
  
  @XmlElement(name = "therapist", required = true)
  private String therapist;

  PatientEditionData() {
    super();
  }
  
  public PatientEditionData(String login, String password, String therapist) {
    super(login, password, RoleType.PATIENT);
    
    this.therapist = therapist;
  }
  
  public String getTherapist() {
    return therapist;
  }

  public void setTherapist(String therapist) {
    this.therapist = therapist;
  }

  @Override
  public String toString() {
    return "PatientEditionData [login=" + getLogin() + ", password=" + password + "]";
  }
  
}
