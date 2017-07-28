/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.entities.i18n;

import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.i18n.I18N.I18NId;

@Entity
@Table(name = "i18n")
@IdClass(I18NId.class)
public class I18N implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "locale", length = 5, columnDefinition = "CHAR(5)")
  private String locale;
  
  @Id
  @Column(name = "messageKey", length = 64, columnDefinition = "VARCHAR(64)")
  private String key;
  
  @Column(name = "value", length = 32768, nullable = false)
  private String value;
  
  // For JPA
  I18N() {}
  
  public I18N(String locale, String key, String value) {
    this.locale = locale;
    this.key = key;
    this.value = value;
  }

  public String getLocale() {
    return locale;
  }
  
  public void setLocale(String locale) {
    this.locale = locale;
  }
  
  public String getKey() {
    return key;
  }
  
  public void setKey(String key) {
    this.key = requireStringSize(key, 1, 64);
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = requireStringSize(value, 0, 32768);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((locale == null) ? 0 : locale.hashCode());
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
    I18N other = (I18N) obj;
    if (key == null) {
      if (other.key != null)
        return false;
    } else if (!key.equals(other.key))
      return false;
    if (locale == null) {
      if (other.locale != null)
        return false;
    } else if (!locale.equals(other.locale))
      return false;
    return true;
  }

  @Embeddable
  public static class I18NId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String locale;
    private String key;
    
    // For JPA
    I18NId() {}
    
    public I18NId(String locale, String key) {
      this.locale = requireStringSize(locale, 5, 5);
      this.key = requireStringSize(key, 1, 64);
    }
    
    public String getLocale() {
      return locale;
    }
    
    public String getKey() {
      return key;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((locale == null) ? 0 : locale.hashCode());
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
      I18NId other = (I18NId) obj;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      if (locale == null) {
        if (other.locale != null)
          return false;
      } else if (!locale.equals(other.locale))
        return false;
      return true;
    }
  }
}
