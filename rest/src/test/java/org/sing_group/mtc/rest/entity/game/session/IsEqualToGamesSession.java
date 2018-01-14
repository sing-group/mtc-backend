/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2018 Miguel Reboiro-Jato, Adolfo Piñón Blanco,
 *     Hugo López-Fernández, Rosalía Laza Fidalgo, Reyes Pavón Rial,
 *     Francisco Otero Lamas, Adrián Varela Pomar, Carlos Spuch Calvar,
 *     and Tania Rivera Baltanás
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

import java.util.Map;
import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.user.IsEqualToUserUri;

public class IsEqualToGamesSession extends IsEqualToEntity<GamesSession, GamesSessionData> {
  public IsEqualToGamesSession(GamesSession expected) {
    super(expected);
  }

  @Override
  protected boolean matchesSafely(GamesSessionData actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", GamesSession::getId, GamesSessionData::getId, actual)
        && matchAttribute("therapist", unwrapOptionalFuncion(GamesSession::getTherapist), GamesSessionData::getTherapist, actual, IsEqualToUserUri::equalToTherapistUri)
        && checkAttribute("nameMessages", GamesSession::getName, GamesSessionData::getNameMessage, actual, this::matchLocaleMessages)
        && checkAttribute("descriptionMessages", GamesSession::getDescription, GamesSessionData::getDescriptionMessage, actual, this::matchLocaleMessages)
        && matchIterableAttribute("gameConfigurations",
          wrapStreamToIterableFunction(GamesSession::getGameConfigurations),
          wrapArrayToIterableFunction(GamesSessionData::getGameConfiguration),
          actual,
          IsEqualToGameConfiguration::containsGameConfigurationInAnyOrder
        );
    }
  }
  
  private boolean matchLocaleMessages(LocalizedMessage messages, Map<I18NLocaleData, String> data) {
    for (Map.Entry<I18NLocaleData, String> entry : data.entrySet()) {
      final I18NLocaleData locale = entry.getKey();
      final String message = entry.getValue();
      
      final String expectedMessage = messages.getMessage(I18NLocale.valueOf(locale.name()));
      
      if (!message.equals(expectedMessage)) {
        return false;
      }
    }
    
    return true;
  }

  @Factory
  public static IsEqualToGamesSession equalToGamesSession(GamesSession session) {
    return new IsEqualToGamesSession(session);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSessionData>> containsGamesSessionsInAnyOrder(GamesSession... gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSession::equalToGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSessionData>> containsGamesSessionsInAnyOrder(Iterable<GamesSession> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSession::equalToGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSessionData>> containsGamesSessionsInAnyOrder(Stream<GamesSession> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSession::equalToGamesSession, gameSessions);
  }
}
