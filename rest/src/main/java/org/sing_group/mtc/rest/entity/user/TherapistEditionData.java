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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.domain.entities.user.RoleType;

import io.swagger.annotations.ApiModel;

/**
 * Edition data of the therapist entity.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "therapist-edition-data", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.PROPERTY)
@ApiModel(value = "therapist-edition-data", description = "Edition data of the therapist entity.")
public class TherapistEditionData extends IdentifiedUserEditionData {
  private static final long serialVersionUID = 1L;
  
  private Long institution;

  TherapistEditionData() {
    super();
  }
  
  public TherapistEditionData(String password, String email, String name, String surname, Long institution) {
    super(password, email, name, surname, RoleType.THERAPIST);
    
    this.institution = institution;
  }
  
  @XmlElement(name = "institution", required = true)
  public Long getInstitution() {
    return this.institution;
  }
  
  public void setInstitution(Long institution) {
    this.institution = institution;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((institution == null) ? 0 : institution.hashCode());
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
    TherapistEditionData other = (TherapistEditionData) obj;
    if (institution == null) {
      if (other.institution != null)
        return false;
    } else if (!institution.equals(other.institution))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TherapistEditionData [getEmail()=" + getEmail() + ", getName()=" + getName() + ", getSurname()="
      + getSurname() + ", getPassword()=" + getPassword() + ", getInstitution()=" + getInstitution() + "]";
  }
}
