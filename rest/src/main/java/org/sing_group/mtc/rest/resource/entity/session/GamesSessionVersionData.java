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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.mtc.rest.resource.entity.LocaleMessages;

@XmlRootElement(name = "games-session-version", namespace = "http://entity.resource.rest.mtc.sing-group.org")
public class GamesSessionVersionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @XmlAttribute(name = "version", required = true)
  private int version;
  
  @XmlElementWrapper(name = "configurations", nillable = false, required = true)
  private GameConfigurationData[] gameConfiguration;

  @XmlElement(name = "name", nillable = false, required = true)
  private LocaleMessages nameMessage;

  @XmlElement(name = "description", nillable = false, required = true)
  private LocaleMessages descriptionMessage;
  
  GamesSessionVersionData() {}
  
  public GamesSessionVersionData(
    int version,
    GameConfigurationData[] gameConfiguration,
    LocaleMessages nameMessage,
    LocaleMessages descriptionMessage
  ) {
    this.version = version;
    this.gameConfiguration = gameConfiguration;
    this.nameMessage = nameMessage;
    this.descriptionMessage = descriptionMessage;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public GameConfigurationData[] getGameConfiguration() {
    return gameConfiguration;
  }

  public void setGameConfiguration(GameConfigurationData[] gameConfiguration) {
    this.gameConfiguration = gameConfiguration;
  }

  public LocaleMessages getNameMessage() {
    return nameMessage;
  }

  public void setNameMessage(LocaleMessages nameMessage) {
    this.nameMessage = nameMessage;
  }

  public LocaleMessages getDescriptionMessage() {
    return descriptionMessage;
  }

  public void setDescriptionMessage(LocaleMessages descriptionMessage) {
    this.descriptionMessage = descriptionMessage;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((descriptionMessage == null) ? 0 : descriptionMessage.hashCode());
    result = prime * result + Arrays.hashCode(gameConfiguration);
    result = prime * result + ((nameMessage == null) ? 0 : nameMessage.hashCode());
    result = prime * result + version;
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
    GamesSessionVersionData other = (GamesSessionVersionData) obj;
    if (descriptionMessage == null) {
      if (other.descriptionMessage != null)
        return false;
    } else if (!descriptionMessage.equals(other.descriptionMessage))
      return false;
    if (!Arrays.equals(gameConfiguration, other.gameConfiguration))
      return false;
    if (nameMessage == null) {
      if (other.nameMessage != null)
        return false;
    } else if (!nameMessage.equals(other.nameMessage))
      return false;
    if (version != other.version)
      return false;
    return true;
  }
}
