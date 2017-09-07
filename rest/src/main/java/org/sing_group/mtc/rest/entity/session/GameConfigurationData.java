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
package org.sing_group.mtc.rest.entity.session;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Game configuration data for a session.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "game-configuration", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "game-configuration", description = "Game configuration data for a session.")
public class GameConfigurationData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @XmlAttribute(name = "gameId", required = true)
  private String gameId;
  
  @XmlAttribute(name = "gameOrder", required = true)
  private int gameOrder;
  
  @XmlElementWrapper(name = "parameters", required = true)
  @XmlElement(name = "parameter", required = true)
  private GameParamData[] parameters;
  
  GameConfigurationData() {}
  
  public GameConfigurationData(String gameId, int gameOrder, GameParamData[] parameters) {
    this.gameId = gameId;
    this.gameOrder = gameOrder;
    this.parameters = parameters;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String id) {
    this.gameId = id;
  }

  public int getGameOrder() {
    return gameOrder;
  }

  public void setGameOrder(int gameOrder) {
    this.gameOrder = gameOrder;
  }

  public GameParamData[] getParameters() {
    return parameters;
  }

  public void setParameters(GameParamData[] parameters) {
    this.parameters = parameters;
  }
  
  @XmlTransient
  @ApiModelProperty(hidden = true)
  public Map<String, String> getParameterValues() {
    return stream(this.parameters)
      .collect(toMap(
        GameParamData::getKey,
        GameParamData::getValue
      ));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
    result = prime * result + gameOrder;
    result = prime * result + Arrays.hashCode(parameters);
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
    GameConfigurationData other = (GameConfigurationData) obj;
    if (gameId == null) {
      if (other.gameId != null)
        return false;
    } else if (!gameId.equals(other.gameId))
      return false;
    if (gameOrder != other.gameOrder)
      return false;
    if (!Arrays.equals(parameters, other.parameters))
      return false;
    return true;
  }
}
