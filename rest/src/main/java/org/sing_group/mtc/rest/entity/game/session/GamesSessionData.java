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
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.MapI18NLocaleDataStringAdapter;
import org.sing_group.mtc.rest.entity.user.UserUri;

import io.swagger.annotations.ApiModel;

/**
 * Games session.
 * 
 * @author Miguel Reboiro-Jato
 */
@XmlRootElement(name = "games-session", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "game-session", description = "Games session.")
public class GamesSessionData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int id;

  private UserUri therapist;
  
  @XmlElementWrapper(name = "configurations", nillable = false, required = true)
  private GameConfigurationData[] gameConfiguration;

  @XmlJavaTypeAdapter(MapI18NLocaleDataStringAdapter.class)
  @XmlElementWrapper(name = "name", required = true)
  @XmlElement(name = "name", required = true)
  private Map<I18NLocaleData, String> nameMessage;

  @XmlJavaTypeAdapter(MapI18NLocaleDataStringAdapter.class)
  @XmlElementWrapper(name = "description", required = true)
  @XmlElement(name = "description", required = true)
  private Map<I18NLocaleData, String> descriptionMessage;
  
  GamesSessionData() {}

  public GamesSessionData(
    int id,
    URI therapistUri,
    String therapistLogin,
    GameConfigurationData[] gameConfiguration,
    Map<I18NLocaleData, String> nameMessage,
    Map<I18NLocaleData, String> descriptionMessage
  ) {
    this.id = id;
    this.therapist = new UserUri(therapistLogin, therapistUri);
    this.gameConfiguration = gameConfiguration;
    this.nameMessage = nameMessage;
    this.descriptionMessage = descriptionMessage;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UserUri getTherapist() {
    return therapist;
  }

  public void setTherapist(UserUri therapist) {
    this.therapist = therapist;
  }

  public GameConfigurationData[] getGameConfiguration() {
    return gameConfiguration;
  }

  public void setGameConfiguration(GameConfigurationData[] gameConfiguration) {
    this.gameConfiguration = gameConfiguration;
  }

  public Map<I18NLocaleData, String> getNameMessage() {
    return nameMessage;
  }

  public void setNameMessage(Map<I18NLocaleData, String> nameMessage) {
    this.nameMessage = nameMessage;
  }

  public Map<I18NLocaleData, String> getDescriptionMessage() {
    return descriptionMessage;
  }

  public void setDescriptionMessage(Map<I18NLocaleData, String> descriptionMessage) {
    this.descriptionMessage = descriptionMessage;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((descriptionMessage == null) ? 0 : descriptionMessage.hashCode());
    result = prime * result + Arrays.hashCode(gameConfiguration);
    result = prime * result + id;
    result = prime * result + ((nameMessage == null) ? 0 : nameMessage.hashCode());
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
    if (descriptionMessage == null) {
      if (other.descriptionMessage != null)
        return false;
    } else if (!descriptionMessage.equals(other.descriptionMessage))
      return false;
    if (!Arrays.equals(gameConfiguration, other.gameConfiguration))
      return false;
    if (id != other.id)
      return false;
    if (nameMessage == null) {
      if (other.nameMessage != null)
        return false;
    } else if (!nameMessage.equals(other.nameMessage))
      return false;
    if (therapist == null) {
      if (other.therapist != null)
        return false;
    } else if (!therapist.equals(other.therapist))
      return false;
    return true;
  }
}
