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

import java.util.stream.Stream;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.session.GameInGamesSession;
import org.sing_group.mtc.rest.entity.game.session.GameConfigurationData;

public class IsEqualToGameConfiguration extends IsEqualToEntity<GameInGamesSession, GameConfigurationData> {

  public IsEqualToGameConfiguration(GameInGamesSession expected) {
    super(expected);
  }

  @Override
  protected boolean matchesSafely(GameConfigurationData actualEntity) {
    this.clearDescribeTo();

    if (actualEntity == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("gameId", config -> config.getGame().map(Game::getId).orElse(null), GameConfigurationData::getGameId, actualEntity)
          && checkAttribute("gameOrder", GameInGamesSession::getGameOrder, GameConfigurationData::getGameOrder, actualEntity)
          && checkAttribute("parameters", GameInGamesSession::getParamValues, GameConfigurationData::getParameterValues, actualEntity);
    }
  }

  @Factory
  public static IsEqualToGameConfiguration equalToGameConfiguration(GameInGamesSession data) {
    return new IsEqualToGameConfiguration(data);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GameConfigurationData>> containsGameConfigurationInAnyOrder(GameInGamesSession... gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfiguration::equalToGameConfiguration, gameConfigs);
  }

  @Factory
  public static Matcher<Iterable<? extends GameConfigurationData>> containsGameConfigurationInAnyOrder(Iterable<GameInGamesSession> gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfiguration::equalToGameConfiguration, gameConfigs);
  }

  @Factory
  public static Matcher<Iterable<? extends GameConfigurationData>> containsGameConfigurationInAnyOrder(Stream<GameInGamesSession> gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfiguration::equalToGameConfiguration, gameConfigs);
  }
}
