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

@XmlRootElement(name = "game-configuration", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameConfigurationData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @XmlAttribute(name = "id", required = true)
  private String id;
  
  @XmlElementWrapper(name = "parameters", nillable = false, required = true)
  @XmlElement(name = "parameter", nillable = false, required = true)
  private GameParamData[] parameters;
  
  GameConfigurationData() {}
  
  public GameConfigurationData(String id, GameParamData[] parameters) {
    this.id = id;
    this.parameters = parameters;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameParamData[] getParameters() {
    return parameters;
  }

  public void setParameters(GameParamData[] parameters) {
    this.parameters = parameters;
  }
  
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (!Arrays.equals(parameters, other.parameters))
      return false;
    return true;
  }
}
