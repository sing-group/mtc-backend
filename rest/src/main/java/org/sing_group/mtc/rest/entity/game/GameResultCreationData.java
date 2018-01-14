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

package org.sing_group.mtc.rest.entity.game;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.mtc.rest.entity.DateToTimestampAdapter;
import org.sing_group.mtc.rest.entity.MapStringStringAdapter;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "game-result-creation", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "game-result-creation", description = "Game result data for adding a result to an assigned games session.")
public class GameResultCreationData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "startDate", required = true)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date startDate;

  @XmlElement(name = "endDate", required = true)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date endDate;
  
  @XmlElement(name = "results", required = true)
  @XmlJavaTypeAdapter(MapStringStringAdapter.class)
  private Map<String, String> results;
  
  GameResultCreationData() {}
  
  public GameResultCreationData(Date startDate, Date endDate, Map<String, String> results) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.results = results;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startTime) {
    this.startDate = startTime;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endTime) {
    this.endDate = endTime;
  }
  
  public Map<String, String> getResults() {
    return results;
  }

  public void setResults(Map<String, String> results) {
    this.results = results;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + ((results == null) ? 0 : results.hashCode());
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
    GameResultCreationData other = (GameResultCreationData) obj;
    if (endDate == null) {
      if (other.endDate != null)
        return false;
    } else if (!endDate.equals(other.endDate))
      return false;
    if (results == null) {
      if (other.results != null)
        return false;
    } else if (!results.equals(other.results))
      return false;
    if (startDate == null) {
      if (other.startDate != null)
        return false;
    } else if (!startDate.equals(other.startDate))
      return false;
    return true;
  }
}
