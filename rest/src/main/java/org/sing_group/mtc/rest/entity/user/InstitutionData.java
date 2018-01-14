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

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "institution-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "institution-data", description = "Edition data of the administrator entity.")
public class InstitutionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private long id;
  
  @XmlElement(name = "name", required = true)
  private String name;

  @XmlElement(name = "description", required = false)
  private String description;

  @XmlElement(name = "address", required = false)
  private String address;

  @XmlElement(name = "manager", required = true)
  private UserUri manager;
  
  @XmlElementWrapper(name = "therapists", required = false)
  @XmlElement(name = "therapist")
  private UserUri[] therapists;
  
  InstitutionData() {}
  
  public InstitutionData(
    long id,
    String name,
    String description,
    String address,
    String managerLogin,
    URI managerUri,
    String[] therapistLogins,
    URI[] therapistUris
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.address = address;
    this.manager = new UserUri(managerLogin, managerUri);
    this.therapists = new UserUri[therapistLogins.length];
    
    for (int i = 0; i < therapistLogins.length; i++) {
      this.therapists[i] = new UserUri(therapistLogins[i], therapistUris[i]);
    }
  }
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
  
  public UserUri getManager() {
    return manager;
  }

  public void setManager(UserUri manager) {
    this.manager = manager;
  }

  public UserUri[] getTherapists() {
    return therapists;
  }

  public void setTherapists(UserUri[] therapists) {
    this.therapists = therapists;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((manager == null) ? 0 : manager.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + Arrays.hashCode(therapists);
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
    InstitutionData other = (InstitutionData) obj;
    if (address == null) {
      if (other.address != null)
        return false;
    } else if (!address.equals(other.address))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (id != other.id)
      return false;
    if (manager == null) {
      if (other.manager != null)
        return false;
    } else if (!manager.equals(other.manager))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (!Arrays.equals(therapists, other.therapists))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "InstitutionData [getId()=" + getId() + ", getName()=" + getName() + ", getDescription()=" + getDescription()
      + ", getAddress()=" + getAddress() + ", getManager()=" + getManager() + ", getTherapists()="
      + Arrays.toString(getTherapists()) + "]";
  }
  
}
