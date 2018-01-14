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
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.mtc.rest.entity.DateToTimestampAdapter;
import org.sing_group.mtc.rest.entity.IdAndUri;
import org.sing_group.mtc.rest.entity.user.UserUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "assigned-games-session", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "assigned-games-session", description = "Games session assigned to a patient.")
public class AssignedGamesSessionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @XmlAttribute(name = "id", required = true)
  private long id;
  
  @XmlElement(name = "assignmentDate", required = true)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date assignmentDate;
  
  @XmlElement(name = "startDate", required = false)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date startDate;
  
  @XmlElement(name = "endDate", required = false)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date endDate;
  
  @XmlElement(name = "gamesSession", required = true)
  private IdAndUri gamesSession;
  
  @XmlElement(name = "patient", required = true)
  private UserUri patient;
  
  @XmlElementWrapper(name = "results", required = false)
  @XmlElement(name = "result", required = false)
  private IdAndUri[] gameResults;

  AssignedGamesSessionData() {}
    
  public AssignedGamesSessionData(
    long id,
    Date assignmentDate, Date startDate, Date endDate,
    IdAndUri gamesSession,
    UserUri patient,
    IdAndUri[] gameResults
  ) {
    this.id = id;
    this.assignmentDate = assignmentDate;
    this.startDate = startDate;
    this.endDate = endDate;
    this.gamesSession = gamesSession;
    this.patient = patient;
    this.gameResults = gameResults;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getAssignmentDate() {
    return assignmentDate;
  }

  public void setAssignmentDate(Date assignmentDate) {
    this.assignmentDate = assignmentDate;
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

  public IdAndUri getGamesSession() {
    return gamesSession;
  }

  public void setGamesSession(IdAndUri gamesSession) {
    this.gamesSession = gamesSession;
  }

  public UserUri getPatient() {
    return patient;
  }

  public void setPatient(UserUri patient) {
    this.patient = patient;
  }

  public IdAndUri[] getGameResults() {
    return gameResults;
  }

  public void setGameResults(IdAndUri[] gameResults) {
    this.gameResults = gameResults;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((assignmentDate == null) ? 0 : assignmentDate.hashCode());
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + Arrays.hashCode(gameResults);
    result = prime * result + ((gamesSession == null) ? 0 : gamesSession.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((patient == null) ? 0 : patient.hashCode());
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
    AssignedGamesSessionData other = (AssignedGamesSessionData) obj;
    if (assignmentDate == null) {
      if (other.assignmentDate != null)
        return false;
    } else if (!assignmentDate.equals(other.assignmentDate))
      return false;
    if (endDate == null) {
      if (other.endDate != null)
        return false;
    } else if (!endDate.equals(other.endDate))
      return false;
    if (!Arrays.equals(gameResults, other.gameResults))
      return false;
    if (gamesSession == null) {
      if (other.gamesSession != null)
        return false;
    } else if (!gamesSession.equals(other.gamesSession))
      return false;
    if (id != other.id)
      return false;
    if (patient == null) {
      if (other.patient != null)
        return false;
    } else if (!patient.equals(other.patient))
      return false;
    if (startDate == null) {
      if (other.startDate != null)
        return false;
    } else if (!startDate.equals(other.startDate))
      return false;
    return true;
  }
  
}
