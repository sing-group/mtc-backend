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

import static org.sing_group.mtc.domain.entities.game.session.GamesSessionDataset.parseDate;

import java.util.HashMap;
import java.util.Map;

import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.game.GameConfigurationData;
import org.sing_group.mtc.rest.entity.game.GameParamData;

public final class GamesSessionDataDataset {
  private GamesSessionDataDataset() {}

  public static GamesSessionEditionData newGamesSessionData() {
    final GameConfigurationData[] games = {
      new GameConfigurationData("recognition", 1, new GameParamData[] {
        new GameParamData("maxRepetitions", "2"),
        new GameParamData("numOfStimuli", "6"),
        new GameParamData("diceShowTime", "3")
      }),
      new GameConfigurationData("verbalFluency", 2, new GameParamData[0]),
      new GameConfigurationData("recognition", 3, new GameParamData[] {
        new GameParamData("maxRepetitions", "1"),
        new GameParamData("numOfStimuli", "8"),
        new GameParamData("diceShowTime", "5")
      })
    };

    final Map<I18NLocaleData, String> nameMessages = new HashMap<>();
    final Map<I18NLocaleData, String> descriptionMessages = new HashMap<>();

    nameMessages.put(I18NLocaleData.EN_US, "New Session");
    nameMessages.put(I18NLocaleData.ES_ES, "Nueva sesión");
    nameMessages.put(I18NLocaleData.GL_ES, "Nova sesión");

    descriptionMessages.put(I18NLocaleData.EN_US, "New session description.");
    descriptionMessages.put(I18NLocaleData.ES_ES, "Descripción de una nueva sesión.");
    descriptionMessages.put(I18NLocaleData.GL_ES, "Descrición dunha nova sesión.");
    
    return new GamesSessionEditionData(games, nameMessages, descriptionMessages);
  }
  
  public static AssignedGamesSessionEditionData modifiedAssignedGamesSessionData() {
    return new AssignedGamesSessionEditionData(
      parseDate("3017-12-01 00:00:00"),
      parseDate("3017-12-15 23:59:59")
    );
  }
}
