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
package org.sing_group.mtc.rest.resource.entity.session;

import java.util.HashMap;
import java.util.Map;

import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.rest.resource.entity.LocaleMessages;
import org.sing_group.mtc.rest.resource.entity.session.GameConfigurationData;
import org.sing_group.mtc.rest.resource.entity.session.GameParamData;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionCreationData;

public final class GamesSessionDataDataset {
  private GamesSessionDataDataset() {}

  public static GamesSessionCreationData newGamesSessionData() {
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

    final Map<String, String> nameMessages = new HashMap<>();
    final Map<String, String> descriptionMessages = new HashMap<>();

    nameMessages.put(I18NLocale.EN_US.name(), "Recognition 2");
    nameMessages.put(I18NLocale.ES_ES.name(), "Reconocimiento 2");
    nameMessages.put(I18NLocale.GL_ES.name(), "Recoñecemento 2");

    descriptionMessages.put(I18NLocale.EN_US.name(), "Recognition game.");
    descriptionMessages.put(I18NLocale.ES_ES.name(), "Juego de reconocimiento.");
    descriptionMessages.put(I18NLocale.GL_ES.name(), "Xogo de recoñecemento.");
    
    return new GamesSessionCreationData(games, new LocaleMessages("name", nameMessages), new LocaleMessages("description", descriptionMessages));
  }
}