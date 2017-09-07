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

import java.net.URI;
import java.util.Map;
import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.session.GamesSessionData;

public class IsEqualToGamesSessionData extends IsEqualToEntity<GamesSessionData, GamesSession> {
  public IsEqualToGamesSessionData(GamesSessionData expected) {
    super(expected);
  }

  @Override
  protected boolean matchesSafely(GamesSession actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("id", GamesSessionData::getId, GamesSession::getId, actual)
        && checkAttribute("therapist", GamesSessionData::getTherapist, unwrapOptionalFuncion(GamesSession::getTherapist), actual, this::matchUriAndTherapist)
        && checkAttribute("nameMessages", GamesSessionData::getNameMessage, GamesSession::getName, actual, this::matchLocaleMessages)
        && checkAttribute("descriptionMessages", GamesSessionData::getDescriptionMessage, GamesSession::getDescription, actual, this::matchLocaleMessages)
        && matchIterableAttribute("gameConfigurations",
          wrapArrayToIterableFunction(GamesSessionData::getGameConfiguration),
          wrapStreamToIterableFunction(GamesSession::getGameConfigurations),
          actual,
          IsEqualToGameConfigurationData::containsGameConfigurationDataInAnyOrder
        );
    }
  }
  
  private boolean matchUriAndTherapist(URI uri, Therapist therapist) {
    return uri.toString().endsWith("/therapist/" + therapist.getLogin())
      || uri.toString().endsWith("therapist");
  }
  
  private boolean matchLocaleMessages(Map<I18NLocaleData, String> data, LocalizedMessage messages) {
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
  public static IsEqualToGamesSessionData equalToGameSession(GamesSessionData session) {
    return new IsEqualToGamesSessionData(session);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsUserDatasInAnyOrder(GamesSessionData... gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGameSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsUserDatasInAnyOrder(Iterable<GamesSessionData> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGameSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsUserDatasInAnyOrder(Stream<GamesSessionData> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGameSession, gameSessions);
  }
}
