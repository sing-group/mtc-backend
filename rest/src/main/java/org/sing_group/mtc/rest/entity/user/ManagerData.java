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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Profile data of the manager entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "manager-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "manager-data", description = "Profile data of the manager entity.")
public class ManagerData extends IdentifiedUserData {
  private static final long serialVersionUID = 1L;
  
  private IdAndUri[] institutions;
  
  ManagerData() {}

  public ManagerData(
    String login, String email, String name, String surname,
    long[] institutionIds, URI[] institutionUris
  ) {
    super(login, email, name, surname, RoleType.MANAGER);
    
    this.institutions = new IdAndUri[institutionIds.length];
    for (int i = 0; i < institutionIds.length; i++) {
      this.institutions[i] = new IdAndUri(institutionIds[i], institutionUris[i]);
    }
  }

  @XmlElementWrapper(name = "institutions", required = false)
  @XmlElement(name = "institutions")
  public IdAndUri[] getInstitutions() {
    return institutions;
  }

  public void setInstitutions(IdAndUri[] institutions) {
    this.institutions = institutions;
  }

  @ApiModelProperty(allowableValues = "MANAGER", required = true)
  @Override
  public RoleType getRole() {
    return super.getRole();
  }

  @Override
  public void setRole(RoleType role) {
    if (role != RoleType.MANAGER)
      throw new IllegalArgumentException("Invalid role. Only " + RoleType.MANAGER + " role is admitted");
  }

  @Override
  public String toString() {
    return "ManagerData [getLogin()=" + getLogin() + ", getEmail()=" + getEmail() + ", getName()=" + getName()
      + ", getSurname()=" + getSurname() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Arrays.hashCode(institutions);
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
    ManagerData other = (ManagerData) obj;
    if (!Arrays.equals(institutions, other.institutions))
      return false;
    return true;
  }
}
