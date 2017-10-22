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
package org.sing_group.mtc.rest.entity.game.session;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.mtc.rest.entity.DateToTimestampAdapter;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "assigned-games-session-edition", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "assigned-games-session-edition", description = "Data to modify an assigned a games session.")
public class AssignedGamesSessionEditionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @XmlElement(name = "startDate", required = false)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date startDate;
  
  @XmlElement(name = "endDate", required = false)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date endDate;
  
  AssignedGamesSessionEditionData() {}
    
  public AssignedGamesSessionEditionData(
    Date startDate, Date endDate
  ) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
    AssignedGamesSessionEditionData other = (AssignedGamesSessionEditionData) obj;
    if (endDate == null) {
      if (other.endDate != null)
        return false;
    } else if (!endDate.equals(other.endDate))
      return false;
    if (startDate == null) {
      if (other.startDate != null)
        return false;
    } else if (!startDate.equals(other.startDate))
      return false;
    return true;
  }
  
}
