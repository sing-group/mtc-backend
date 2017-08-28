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

import static java.util.Collections.unmodifiableMap;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;
import static org.sing_group.fluent.checker.Checks.requireNonNullCollection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalizedMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String id;
  
  private final Map<I18NLocale, String> messages;

  public LocalizedMessage(String id, Map<I18NLocale, String> messages) {
    requireNonEmpty(messages);
    
    this.id = id;
    this.messages = new HashMap<>(messages);
  }
  
  public String getId() {
    return id;
  }
  
  public Map<I18NLocale, String> getMessages() {
    return unmodifiableMap(this.messages);
  }
  
  public Stream<I18NLocale> getLocales() {
    return this.messages.keySet().stream();
  }
  
  public String getMessage(I18NLocale locale) {
    return this.messages.get(locale);
  }
  
  public boolean hasLocale(I18NLocale locale) {
    return this.messages.containsKey(locale);
  }
  
  public Stream<I18N> toI18N() {
    return this.getLocales()
      .map(locale -> new I18N(locale.getValue(), this.getId(), this.getMessage(locale)));
  }

  public static LocalizedMessage from(String key, Collection<I18N> i18ns) {
    requireNonNullCollection(i18ns);
    
    final Map<I18NLocale, String> messages = i18ns.stream()
      .filter(i18n -> i18n.getKey().equals(key))
      .collect(Collectors.toMap(
        i18n -> I18NLocale.of(i18n.getLocale()),
        I18N::getValue
      ));
    
    return new LocalizedMessage(key, messages);
  }
  
//  private static void checkSameKey(I18N... i18ns) {
//    requireNonNullArray(i18ns);
//    
//    if (stream(i18ns).map(I18N::getKey).distinct().count() == 1) {
//      throw new IllegalArgumentException("i18ns should have the same key");
//    }
//  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    LocalizedMessage other = (LocalizedMessage) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (messages == null) {
      if (other.messages != null)
        return false;
    } else if (!messages.equals(other.messages))
      return false;
    return true;
  }
}
