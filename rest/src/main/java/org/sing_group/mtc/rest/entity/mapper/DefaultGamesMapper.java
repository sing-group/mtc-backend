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
package org.sing_group.mtc.rest.entity.mapper;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.session.GameConfigurationForSession;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.session.GameConfigurationData;
import org.sing_group.mtc.rest.entity.session.GameParamData;
import org.sing_group.mtc.rest.entity.session.GamesSessionCreationData;
import org.sing_group.mtc.rest.entity.session.GamesSessionData;
import org.sing_group.mtc.service.spi.game.GameService;

@Default
@Transactional(value = MANDATORY)
public class DefaultGamesMapper implements GamesMapper {
  @Inject
  private GameService gamesService;
  
  public void setGamesService(GameService gamesService) {
    this.gamesService = gamesService;
  }
  
  @Override
  public GamesSession mapToGameSession(GamesSessionCreationData data) {
    final GamesSession session = new GamesSession();
    
    session.setNameMessages(extractMessages(data.getName()));
    session.setDescriptionMessages(extractMessages(data.getDescription()));
    
    mapToGameConfigurationForSession(data.getGames())
      .forEach(session::addGameConfiguration);
    
    return session;
  }
  
  private Map<I18NLocale, String> extractMessages(Map<I18NLocaleData, String> messages) {
    return messages.entrySet().stream()
      .collect(toMap(
        entry -> I18NLocale.valueOf(entry.getKey().name()),
        Entry::getValue
      ));
  }
  
  @Override
  public SortedSet<GameConfigurationForSession> mapToGameConfigurationForSession(GameConfigurationData[] gameConfigurations) {
    final AtomicInteger gameOrder = new AtomicInteger(1);
    
    return stream(gameConfigurations)
      .map(game -> new GameConfigurationForSession(
        null,
        gamesService.getGame(game.getGameId()),
        gameOrder.getAndIncrement(),
        game.getParameterValues()
      ))
    .collect(toCollection(TreeSet::new));
  }
  
  @Override
  public GamesSessionData mapToGameSessionData(GamesSession session, Function<Therapist, URI> therapistUrlBuilder) {
    return new GamesSessionData(
      session.getId(),
      therapistUrlBuilder.apply(session.getTherapist().orElseThrow(IllegalStateException::new)),
      session.getGameConfigurations()
        .map(this::mapToGameConfigurationData)
      .toArray(GameConfigurationData[]::new),
      mapToLocaleMessages(session.getName()),
      mapToLocaleMessages(session.getDescription())
    );
  }
  
  @Override
  public GameConfigurationData mapToGameConfigurationData(GameConfigurationForSession gameConfiguration) {
    return new GameConfigurationData(
      gameConfiguration.getGame().map(Game::getId).orElseThrow(IllegalStateException::new),
      gameConfiguration.getGameOrder(),
      gameConfiguration.getParamValues().entrySet().stream()
        .map(entry -> new GameParamData(entry.getKey(), entry.getValue()))
      .toArray(GameParamData[]::new)
    );
  }
  
  @Override
  public Map<I18NLocaleData, String> mapToLocaleMessages(LocalizedMessage message) {
    final Map<I18NLocaleData, String> messages = message.getMessages().entrySet().stream()
      .collect(toMap(
        entry -> I18NLocaleData.of(entry.getKey().getValue()),
        Entry::getValue
      ));
    
    return messages;
  }
}
