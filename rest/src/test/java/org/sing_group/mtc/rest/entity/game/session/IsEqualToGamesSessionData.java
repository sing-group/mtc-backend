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
import org.sing_group.mtc.rest.entity.user.IsUserUriEqualToUser;

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
        && matchAttribute("therapist", GamesSessionData::getTherapist, unwrapOptionalFuncion(GamesSession::getTherapist), actual, IsUserUriEqualToUser::equalToTherapistUri)
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
  public static IsEqualToGamesSessionData equalToGamesSession(GamesSessionData session) {
    return new IsEqualToGamesSessionData(session);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsGamesSessionsDatasInAnyOrder(GamesSessionData... gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsGamesSessionsInAnyOrder(Iterable<GamesSessionData> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGamesSession, gameSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GamesSession>> containsGamesSessionsInAnyOrder(Stream<GamesSessionData> gameSessions) {
    return containsEntityInAnyOrder(IsEqualToGamesSessionData::equalToGamesSession, gameSessions);
  }
}
