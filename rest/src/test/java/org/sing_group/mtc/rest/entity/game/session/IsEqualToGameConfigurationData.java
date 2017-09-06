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
import org.sing_group.mtc.domain.entities.game.session.GameConfigurationForSession;
import org.sing_group.mtc.rest.entity.session.GameConfigurationData;

public class IsEqualToGameConfigurationData extends IsEqualToEntity<GameConfigurationData, GameConfigurationForSession> {

  public IsEqualToGameConfigurationData(GameConfigurationData expected) {
    super(expected);
  }

  @Override
  protected boolean matchesSafely(GameConfigurationForSession actualEntity) {
    System.err.println("Matching: " + actualEntity);
    
    this.clearDescribeTo();

    if (actualEntity == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      return checkAttribute("gameId", GameConfigurationData::getGameId, config -> config.getGame().map(Game::getId).orElse(null), actualEntity)
          && checkAttribute("gameOrder", GameConfigurationData::getGameOrder, GameConfigurationForSession::getGameOrder, actualEntity)
          && checkAttribute("parameters", GameConfigurationData::getParameterValues, GameConfigurationForSession::getParamValues, actualEntity);
    }
  }

  @Factory
  public static IsEqualToGameConfigurationData equalToGameConfigurationData(GameConfigurationData data) {
    return new IsEqualToGameConfigurationData(data);
  }
  
  @Factory
  public static Matcher<Iterable<? extends GameConfigurationForSession>> containsGameConfigurationDataInAnyOrder(GameConfigurationData... gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfigurationData::equalToGameConfigurationData, gameConfigs);
  }

  @Factory
  public static Matcher<Iterable<? extends GameConfigurationForSession>> containsGameConfigurationDataInAnyOrder(Iterable<GameConfigurationData> gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfigurationData::equalToGameConfigurationData, gameConfigs);
  }

  @Factory
  public static Matcher<Iterable<? extends GameConfigurationForSession>> containsGameConfigurationDataInAnyOrder(Stream<GameConfigurationData> gameConfigs) {
    return containsEntityInAnyOrder(IsEqualToGameConfigurationData::equalToGameConfigurationData, gameConfigs);
  }
}
