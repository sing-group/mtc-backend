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
package org.sing_group.mtc.rest.entity.mapper.spi.game;

import java.util.Map;
import java.util.SortedSet;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameConfigurationForSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionCreationData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.GameConfigurationData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionEditionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;

public interface GamesMapper {

  public GamesSession mapToGameSession(GamesSessionEditionData data);
  
  public GamesSession mapToGameSession(int id, GamesSessionEditionData data);

  public GamesSessionEditionData mapToGameSessionEditionData(GamesSession modifiedSession);

  public SortedSet<GameConfigurationForSession> mapToGameConfigurationForSession(GameConfigurationData[] gameConfigurations);

  public GamesSessionData mapToGameSessionData(GamesSession session, UriBuilder uriBuilder);

  public GameConfigurationData mapToGameConfigurationData(GameConfigurationForSession gameConfiguration);

  public Map<I18NLocaleData, String> mapToLocaleMessages(LocalizedMessage message);

  public AssignedGamesSessionData mapAssignedGamesSesion(AssignedGamesSession assignedSession, UriBuilder uriBuilder);

  public AssignedGamesSessionCreationData mapAssignedGamesSesionCreation(AssignedGamesSession assignedSession);

}