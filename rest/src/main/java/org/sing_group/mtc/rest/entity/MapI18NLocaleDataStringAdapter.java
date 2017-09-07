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

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import static java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.sing_group.mtc.rest.entity.MapStringStringAdapter.MapStringString;

public class MapI18NLocaleDataStringAdapter extends XmlAdapter<MapStringString, Map<I18NLocaleData, String>> {
  @Override
  public Map<I18NLocaleData, String> unmarshal(MapStringString v) throws Exception {
    return v.toMap().entrySet().stream()
      .collect(toMap(
        entry -> I18NLocaleData.of(entry.getKey()),
        Entry::getValue
      ));
  }

  @Override
  public MapStringString marshal(Map<I18NLocaleData, String> v) throws Exception {
    final Map<String, String> plainMap = v.entrySet().stream()
      .collect(toMap(
        entry -> entry.getKey().getValue(),
        Entry::getValue
      ));
    
    return new MapStringString(plainMap);
  }
  
}
