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
package org.sing_group.mtc.rest.resource.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "locale-messages", namespace = "http://entity.resource.rest.mtc.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocaleMessages implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlAttribute(name = "key", required = true)
  private String key;

  @XmlJavaTypeAdapter(MapStringStringAdapter.class)
  @XmlElementWrapper(name = "messages", nillable = false, required = true)
  @XmlElement(name = "message", nillable = false, required = true)
  private Map<String, String> messages;

  LocaleMessages() {}
  
  public LocaleMessages(String key, Map<String, String> messages) {
    this.key = key;
    this.messages = new HashMap<>(messages);
  }

  public String getKey() {
    return key;
  }

  public void setLocale(String key) {
    this.key = key;
  }

  public Map<String, String> getMessages() {
    return messages;
  }

  public void setMessages(Map<String, String> messages) {
    this.messages = messages;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((messages == null) ? 0 : messages.hashCode());
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
    LocaleMessages other = (LocaleMessages) obj;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    if (messages == null) {
      if (other.messages != null)
        return false;
    } else if (!messages.equals(other.messages))
      return false;
    return true;
  }
}
