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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum I18NLocaleData {
  @XmlEnumValue("en_US") EN_US("en_US"),
  @XmlEnumValue("es_ES") ES_ES("es_ES"),
  @XmlEnumValue("gl_ES") GL_ES("gl_ES");
  
  private String value;
  
  private I18NLocaleData(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
  
  public static I18NLocaleData of(String locale) {
    return stream(I18NLocaleData.values())
      .filter(i18n -> i18n.getValue().equals(locale))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Invalid locale: " + locale));
  }
}
