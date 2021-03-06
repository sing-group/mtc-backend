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

package org.sing_group.mtc.rest.entity.mapper.game;

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
import javax.ws.rs.core.UriBuilder;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameInGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.I18NLocaleData;
import org.sing_group.mtc.rest.entity.IdAndUri;
import org.sing_group.mtc.rest.entity.game.GameConfigurationData;
import org.sing_group.mtc.rest.entity.game.GameParamData;
import org.sing_group.mtc.rest.entity.game.GameResultData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionCreationData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.AssignedGamesSessionEditionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.game.session.GamesSessionEditionData;
import org.sing_group.mtc.rest.entity.mapper.spi.game.GamesMapper;
import org.sing_group.mtc.rest.entity.user.UserUri;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;
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
  public GamesSession mapToGameSession(GamesSessionEditionData data) {
    final GamesSession session = new GamesSession();
    
    session.setNameMessages(extractMessages(data.getName()));
    session.setDescriptionMessages(extractMessages(data.getDescription()));
    
    mapToGameConfigurationForSession(data.getGames())
      .forEach(session::addGameConfiguration);
    
    return session;
  }

  @Override
  public GamesSession mapToGameSession(long id, GamesSessionEditionData data) {
    final GamesSession session = mapToGameSession(data);
    
    session.setId(id);
    
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
  public GamesSessionEditionData mapToGameSessionEditionData(GamesSession session) {
    return new GamesSessionEditionData(
      session.getGameConfigurations()
        .map(this::mapToGameConfigurationData)
      .toArray(GameConfigurationData[]::new),
      this.mapToLocaleMessages(session.getName()),
      this.mapToLocaleMessages(session.getDescription())
    );
  }
  
  @Override
  public SortedSet<GameInGamesSession> mapToGameConfigurationForSession(GameConfigurationData[] gameConfigurations) {
    final AtomicInteger gameOrder = new AtomicInteger(1);
    
    return stream(gameConfigurations)
      .map(game -> new GameInGamesSession(
        null,
        gamesService.getGame(game.getGameId()),
        gameOrder.getAndIncrement(),
        game.getParameterValues()
      ))
    .collect(toCollection(TreeSet::new));
  }
  
  @Override
  public GamesSessionData mapToGameSessionData(GamesSession session, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Function<Therapist, URI> therapistUrlBuilder = therapist -> pathBuilder.therapist(therapist).build();
    
    return new GamesSessionData(
      session.getId(),
      therapistUrlBuilder.apply(session.getTherapist().orElseThrow(IllegalStateException::new)),
      session.getTherapist().map(Therapist::getLogin).orElseThrow(IllegalStateException::new),
      session.getGameConfigurations()
        .map(this::mapToGameConfigurationData)
      .toArray(GameConfigurationData[]::new),
      mapToLocaleMessages(session.getName()),
      mapToLocaleMessages(session.getDescription())
    );
  }
  
  @Override
  public GameConfigurationData mapToGameConfigurationData(GameInGamesSession gameConfiguration) {
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
  
  @Override
  public AssignedGamesSessionData mapToAssignedGamesSession(AssignedGamesSession assignedSession, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final GamesSession gamesSession = assignedSession.getGamesSession().orElseThrow(IllegalStateException::new);
    final Patient patient = assignedSession.getPatient().orElseThrow(IllegalStateException::new);
    
    return new AssignedGamesSessionData(
      assignedSession.getId(),
      assignedSession.getAssignmentDate(),
      assignedSession.getStartDate(),
      assignedSession.getEndDate(),
      new IdAndUri(gamesSession.getId(), pathBuilder.gamesSession(gamesSession).build()),
      new UserUri(patient.getLogin(), pathBuilder.patient(patient).build()),
      assignedSession.getGameResults()
        .map(result -> new IdAndUri(result.getId(), pathBuilder.gameResult(result).build()))
      .toArray(IdAndUri[]::new)
    );
  }
  
  public AssignedGamesSession mapToAssignedGamesSession(int sessionId, AssignedGamesSessionEditionData data) {
    return new AssignedGamesSession(sessionId, data.getStartDate(), data.getEndDate());
  }
  
  @Override
  public AssignedGamesSessionCreationData mapToAssignedGamesSessionCreation(AssignedGamesSession assignedSession) {
    return new AssignedGamesSessionCreationData(
      assignedSession.getStartDate(),
      assignedSession.getEndDate(),
      assignedSession.getGamesSession()
        .map(GamesSession::getId)
      .orElseThrow(IllegalStateException::new)
    );
  }
  
  @Override
  public GameResultData mapToGameResultData(GameResult result, UriBuilder uriBuilder) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    final Long assignedGamesSessionId = result.getAssignedGamesSession()
      .map(AssignedGamesSession::getId)
    .orElseThrow(IllegalArgumentException::new);
    
    return new GameResultData(
      result.getId(),
      new IdAndUri(assignedGamesSessionId, pathBuilder.gamesSessionAssigned(assignedGamesSessionId).build()),
      result.getGameId(),
      result.getGameIndex(),
      result.getAttempt(),
      result.getStart(),
      result.getEnd().orElseThrow(IllegalArgumentException::new),
      result.getResultValues()
    );
  }
}
