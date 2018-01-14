/*
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2018 Miguel Reboiro-Jato and Adolfo Piñón Blanco
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.mtc.rest.entity.DateToTimestampAdapter;
import org.sing_group.mtc.rest.entity.IdAndUri;
import org.sing_group.mtc.rest.entity.MapStringStringAdapter;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "game-result", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "game-result", description = "Game result data for adding a result to an assigned games session.")
public class GameResultData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlAttribute(name = "id", required = true)
  private long id;
  
  @XmlElement(name = "assignedGamesSession", required = true)
  private IdAndUri assignedGamesSession;
  
  @XmlElement(name = "game", required = true)
  private String game;

  @XmlElement(name = "gameIndex", required = true)
  private int gameIndex;
  
  @XmlElement(name = "attempt", required = true)
  private int attempt;
  
  @XmlElement(name = "startDate", required = true)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date startDate;

  @XmlElement(name = "endDate", required = true)
  @XmlJavaTypeAdapter(DateToTimestampAdapter.class)
  private Date endDate;
  
  @XmlElement(name = "results", required = true)
  @XmlJavaTypeAdapter(MapStringStringAdapter.class)
  private Map<String, String> results;
  
  GameResultData() {}

  public GameResultData(
    long id,
    IdAndUri assignedGamesSession,
    String game,
    int gameIndex,
    int attempt,
    Date startDate,
    Date endDate,
    Map<String, String> results
  ) {
    this.id = id;
    this.assignedGamesSession = assignedGamesSession;
    this.game = game;
    this.gameIndex = gameIndex;
    this.attempt = attempt;
    this.startDate = startDate;
    this.endDate = endDate;
    this.results = results;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public IdAndUri getAssignedGamesSession() {
    return assignedGamesSession;
  }

  public void setAssignedGamesSession(IdAndUri assignedGamesSession) {
    this.assignedGamesSession = assignedGamesSession;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public int getGameIndex() {
    return gameIndex;
  }

  public void setGameIndex(int gameIndex) {
    this.gameIndex = gameIndex;
  }
  
  public int getAttempt() {
    return attempt;
  }

  public void setAttempt(int attempt) {
    this.attempt = attempt;
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
    result = prime * result + ((assignedGamesSession == null) ? 0 : assignedGamesSession.hashCode());
    result = prime * result + attempt;
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + ((game == null) ? 0 : game.hashCode());
    result = prime * result + gameIndex;
    result = prime * result + (int) (id ^ (id >>> 32));
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
    GameResultData other = (GameResultData) obj;
    if (assignedGamesSession == null) {
      if (other.assignedGamesSession != null)
        return false;
    } else if (!assignedGamesSession.equals(other.assignedGamesSession))
      return false;
    if (attempt != other.attempt)
      return false;
    if (endDate == null) {
      if (other.endDate != null)
        return false;
    } else if (!endDate.equals(other.endDate))
      return false;
    if (game == null) {
      if (other.game != null)
        return false;
    } else if (!game.equals(other.game))
      return false;
    if (gameIndex != other.gameIndex)
      return false;
    if (id != other.id)
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
