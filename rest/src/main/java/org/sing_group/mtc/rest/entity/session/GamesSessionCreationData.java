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

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.rest.entity.LocaleMessages;

@XmlRootElement(name = "games-session-creation", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class GamesSessionCreationData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElementWrapper(name = "games", nillable = false, required = true)
  @XmlElement(name = "game", required = true)
  private GameConfigurationData[] games;

  @XmlElement(name = "name", required = true)
  private LocaleMessages name;
  
  @XmlElement(name = "description", required = true)
  private LocaleMessages description;
  
  GamesSessionCreationData() {}

  public GamesSessionCreationData(
    GameConfigurationData[] games,
    LocaleMessages name,
    LocaleMessages description
  ) {
    this.games = games;
    this.name = name;
    this.description = description;
  }
  
  public LocaleMessages getName() {
    return name;
  }

  public void setName(LocaleMessages name) {
    this.name = name;
  }

  public GameConfigurationData[] getGames() {
    return games;
  }

  public void setGames(GameConfigurationData[] games) {
    this.games = games;
  }

  public LocaleMessages getDescription() {
    return description;
  }

  public void setDescription(LocaleMessages description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + Arrays.hashCode(games);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    GamesSessionCreationData other = (GamesSessionCreationData) obj;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (!Arrays.equals(games, other.games))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
