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
 * Creation data of the therapist entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "therapist-creation-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "therapist-creation-data", description = "Creation data of the therapist entity.")
public class TherapistCreationData extends TherapistEditionData {
  private static final long serialVersionUID = 1L;

  private String login;

  TherapistCreationData() {}

  public TherapistCreationData(
    String login, String password, String email, String name, String surname, Integer institution
  ) {
    super(password, email, name, surname, institution);
    
    this.login = login;
  }

  @XmlElement(name = "login", required = true)
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((login == null) ? 0 : login.hashCode());
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
    TherapistCreationData other = (TherapistCreationData) obj;
    if (login == null) {
      if (other.login != null)
        return false;
    } else if (!login.equals(other.login))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TherapistCreationData [getLogin()=" + getLogin() + ", getEmail()=" + getEmail() + ", getName()=" + getName()
      + ", getSurname()=" + getSurname() + ", getPassword()=" + getPassword() + ", getInstitution()=" + getInstitution()
      + "]";
  }
  
}
