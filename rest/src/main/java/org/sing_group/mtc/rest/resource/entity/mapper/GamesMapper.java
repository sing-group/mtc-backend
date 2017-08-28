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
package org.sing_group.mtc.rest.resource.entity.mapper;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.session.GameConfigurationForSession;
import org.sing_group.mtc.domain.entities.session.GamesSession;
import org.sing_group.mtc.domain.entities.session.GamesSessionVersion;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.resource.entity.LocaleMessages;
import org.sing_group.mtc.rest.resource.entity.session.GameConfigurationData;
import org.sing_group.mtc.rest.resource.entity.session.GameParamData;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionCreationData;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionData;
import org.sing_group.mtc.rest.resource.entity.session.GamesSessionVersionData;
import org.sing_group.mtc.service.GameService;

@Default
@Transactional(value = MANDATORY)
public class GamesMapper {
  @Inject
  private GameService gamesService;
  
  public GamesSession mapToGameSession(GamesSessionCreationData data) {
    final GamesSession session = new GamesSession();
    
    final Map<I18NLocale, String> nameMessages = extractMessages(data.getName());
    final Map<I18NLocale, String> descriptionMessages = extractMessages(data.getDescription());
    
    final GamesSessionVersion version = new GamesSessionVersion(nameMessages, descriptionMessages);
    
    final SortedSet<GameConfigurationForSession> gameConfigs = mapToGameConfigurationForSession(data.getGames());
    
    session.addVersion(version);
    gameConfigs.forEach(version::addGameConfiguration);
    
    return session;
  }
  
  private Map<I18NLocale, String> extractMessages(LocaleMessages messages) {
    return messages.getMessages().entrySet().stream()
      .collect(toMap(
        entry -> I18NLocale.of(entry.getKey()),
        Entry::getValue
      ));
  }
  
  public SortedSet<GameConfigurationForSession> mapToGameConfigurationForSession(GameConfigurationData[] gameConfigurations) {
    final AtomicInteger gameOrder = new AtomicInteger(1);
    
    return stream(gameConfigurations)
      .map(game -> new GameConfigurationForSession(
        null,
        gamesService.getGame(game.getId()),
        gameOrder.getAndIncrement(),
        game.getParameterValues()
      ))
    .collect(toCollection(TreeSet::new));
  }
  
  public GamesSessionData mapToGameSessionData(GamesSession session, Function<Therapist, String> therapistUrlBuilder) {
    return new GamesSessionData(
      session.getId(),
      therapistUrlBuilder.apply(session.getTherapist()),
      session.getVersions()
        .map(this::mapToGamesSessionVersionData)
      .toArray(GamesSessionVersionData[]::new)
    );
  }
  
  public GamesSessionVersionData mapToGamesSessionVersionData(GamesSessionVersion sessionVersion) {
    return new GamesSessionVersionData(
      sessionVersion.getVersion(),
      sessionVersion.getGameConfigurations()
        .map(this::mapToGameConfigurationData)
      .toArray(GameConfigurationData[]::new),
      mapToLocaleMessages(sessionVersion.getName()),
      mapToLocaleMessages(sessionVersion.getDescription())
    );
  }
  
  public GameConfigurationData mapToGameConfigurationData(GameConfigurationForSession gameConfiguration) {
    return new GameConfigurationData(
      gameConfiguration.getGame().getId(),
      gameConfiguration.getParamValues().entrySet().stream()
        .map(entry -> new GameParamData(entry.getKey(), entry.getValue()))
      .toArray(GameParamData[]::new)
    );
  }
  
  public LocaleMessages mapToLocaleMessages(LocalizedMessage message) {
    final Map<String, String> messages = message.getMessages().entrySet().stream()
      .collect(toMap(
        entry -> entry.getKey().getValue(),
        Entry::getValue
      ));
    
    return new LocaleMessages(message.getId(), messages);
  }
}
