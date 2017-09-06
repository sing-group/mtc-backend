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

import java.util.Map;
import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.rest.entity.LocaleMessages;

public class IsEqualToLocaleMessages extends IsEqualToEntity<LocaleMessages, LocalizedMessage> {
  public IsEqualToLocaleMessages(LocaleMessages entity) {
    super(entity);
  }

  @Override
  protected boolean matchesSafely(LocalizedMessage actualEntity) {
    this.clearDescribeTo();

    if (actualEntity == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("key", LocaleMessages::getKey, LocalizedMessage::getId, actualEntity)
        && checkAttribute("messages", LocaleMessages::getMessages, LocalizedMessage::getMessages, actualEntity, IsEqualToLocaleMessages::compareMessages);
    }
  }
  
  private static boolean compareMessages(Map<String, String> messages1, Map<I18NLocale, String> messages2) {
    if (messages1.size() == messages2.size()) {
      for (I18NLocale locale : messages2.keySet()) {
        final String message1 = messages1.get(locale.getValue());
        final String message2 = messages2.get(locale);
        
        if (!message2.equals(message1)) {
          return false;
        }
      }
      
      return true;
    } else {
      return false;
    }
  }
  
  @Factory
  public static IsEqualToLocaleMessages equalToLocaleMessages(LocaleMessages actualEntity) {
    return new IsEqualToLocaleMessages(actualEntity);
  }
  
  @Factory
  public static Matcher<Iterable<? extends LocalizedMessage>> containsUserDatasInAnyOrder(LocaleMessages... messages) {
    return containsEntityInAnyOrder(IsEqualToLocaleMessages::equalToLocaleMessages, messages);
  }
  
  @Factory
  public static Matcher<Iterable<? extends LocalizedMessage>> containsUserDatasInAnyOrder(Iterable<LocaleMessages> messages) {
    return containsEntityInAnyOrder(IsEqualToLocaleMessages::equalToLocaleMessages, messages);
  }
  
  @Factory
  public static Matcher<Iterable<? extends LocalizedMessage>> containsUserDatasInAnyOrder(Stream<LocaleMessages> messages) {
    return containsEntityInAnyOrder(IsEqualToLocaleMessages::equalToLocaleMessages, messages);
  }
}
