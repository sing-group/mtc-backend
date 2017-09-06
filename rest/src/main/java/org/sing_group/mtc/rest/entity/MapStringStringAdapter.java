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
package org.sing_group.mtc.rest.entity;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.sing_group.mtc.rest.entity.MapStringStringAdapter.MapStringString;

public class MapStringStringAdapter extends XmlAdapter<MapStringString, Map<String, String>> {
  @Override
  public Map<String, String> unmarshal(MapStringString v) throws Exception {
    return v.toMap();
  }

  @Override
  public MapStringString marshal(Map<String, String> v) throws Exception {
    return new MapStringString(v);
  }
  
  @XmlRootElement(name = "map", namespace = "http://entity.resource.rest.mtc.sing-group.org")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class MapStringString implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private MapStringStringEntry[] values;
    
    public MapStringString() {}
    
    public MapStringString(Map<String, String> values) {
      this.values = values.entrySet().stream()
        .map(MapStringStringEntry::new)
      .toArray(MapStringStringEntry[]::new);
    }
    
    public MapStringStringEntry[] getValues() {
      return values;
    }
    
    public void setValues(MapStringStringEntry[] values) {
      this.values = values;
    }
    
    public Map<String, String> toMap() {
      return stream(this.values)
        .collect(Collectors.toMap(
          MapStringStringEntry::getKey,
          MapStringStringEntry::getValue
        ));
    }
  }


  @XmlRootElement(name = "entry", namespace = "http://entity.resource.rest.mtc.sing-group.org")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class MapStringStringEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlAttribute(name = "key")
    private String key;
    
    @XmlValue
    private String value;
    
    public MapStringStringEntry() {}
    
    public MapStringStringEntry(Map.Entry<String, String> entry) {
      this.key = entry.getKey();
      this.value = entry.getValue();
    }
    
    public String getKey() {
      return key;
    }
    
    public void setKey(String key) {
      this.key = key;
    }
    
    public String getValue() {
      return value;
    }
    
    public void setValue(String value) {
      this.value = value;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
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
      MapStringStringEntry other = (MapStringStringEntry) obj;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      if (value == null) {
        if (other.value != null)
          return false;
      } else if (!value.equals(other.value))
        return false;
      return true;
    }
  }
}
