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
package org.sing_group.mtc.rest.resource.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.User;

@XmlRootElement(name = "user-edition-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserEditionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static UserEditionData of(User user) {
    if (user instanceof Patient) {
      return new UserEditionData(user.getEmail(), user.getName(), user.getSurname(), user.getPassword(), User.getRole(user), ((Patient) user).getTherapist().getId());
    } else {
      return new UserEditionData(user.getEmail(), user.getName(), user.getSurname(), user.getPassword(), User.getRole(user));
    }
  }
  
  private String email;
  private String name;
  private String surname;
  private String password;
  private String role;
  
  @XmlElement(required = false)
  private Integer therapistId;

  UserEditionData() {}

  public UserEditionData(String email, String name, String surname, String password, String role) {
    this(email, name, surname, password, role, null);
  }

  public UserEditionData(String email, String name, String surname, String password, String role, Integer therapistId) {
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.password = password;
    this.role = role;
    this.therapistId = therapistId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }
  
  public Integer getTherapistId() {
    return therapistId;
  }

  public void setTherapistId(Integer therapistId) {
    this.therapistId = therapistId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((role == null) ? 0 : role.hashCode());
    result = prime * result + ((surname == null) ? 0 : surname.hashCode());
    result = prime * result + ((therapistId == null) ? 0 : therapistId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserEditionData other = (UserEditionData) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (role == null) {
      if (other.role != null)
        return false;
    } else if (!role.equals(other.role))
      return false;
    if (surname == null) {
      if (other.surname != null)
        return false;
    } else if (!surname.equals(other.surname))
      return false;
    if (therapistId == null) {
      if (other.therapistId != null)
        return false;
    } else if (!therapistId.equals(other.therapistId))
      return false;
    return true;
  }

}
