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
package org.sing_group.mtc.rest.resource.entity.session;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "games-session", namespace = "http://entity.resource.rest.mtc.sing-group.org")
public class GamesSessionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int id;

  private String therapist;

  @XmlElementWrapper(name = "gameVersions", nillable = false, required = true)
  @XmlElement(name = "gameVersion", nillable = false, required = true)
  private GamesSessionVersionData[] gameVersions;
  
  GamesSessionData() {}

  public GamesSessionData(
    int id, String therapist,
    GamesSessionVersionData[] gameVersions
  ) {
    this.id = id;
    this.therapist = therapist;
    this.gameVersions = gameVersions;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTherapist() {
    return therapist;
  }

  public void setTherapist(String therapist) {
    this.therapist = therapist;
  }

  public GamesSessionVersionData[] getGameVersions() {
    return gameVersions;
  }

  public void setgameVersions(GamesSessionVersionData[] gameVersions) {
    this.gameVersions = gameVersions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(gameVersions);
    result = prime * result + id;
    result = prime * result + ((therapist == null) ? 0 : therapist.hashCode());
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
    GamesSessionData other = (GamesSessionData) obj;
    if (!Arrays.equals(gameVersions, other.gameVersions))
      return false;
    if (id != other.id)
      return false;
    if (therapist == null) {
      if (other.therapist != null)
        return false;
    } else if (!therapist.equals(other.therapist))
      return false;
    return true;
  }
}
