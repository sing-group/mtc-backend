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
package org.sing_group.mtc.rest.entity.user;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import javax.ws.rs.core.UriBuilder;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.sing_group.mtc.domain.entities.IsEqualToEntity;
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.game.session.GameResult;
import org.sing_group.mtc.domain.entities.game.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;

public class IsEqualToIdAndUri<T> extends IsEqualToEntity<T, IdAndUri> {
  private final BiFunction<BaseRestPathBuilder, T, URI> uriBuilder;
  private final ToLongFunction<T> idBuilder;
  
  public IsEqualToIdAndUri(T entity, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder, ToLongFunction<T> idBuilder) {
    super(entity);
    
    this.idBuilder = idBuilder;
    this.uriBuilder = uriBuilder;
  }

  @Override
  protected boolean matchesSafely(IdAndUri actual) {
    this.clearDescribeTo();

    if (actual == null) {
      this.addTemplatedValueDescription("actual", expected.toString());
      return false;
    } else {
      if (this.getExpectedLong() != actual.getId()) {
        this.addDescription(String.format("actual login '%s' is different from expected login '%s'",
          actual.getId(), this.getExpectedLong()));
        
        return false;
      } else if (!this.checkUri(actual.getUri())) {
        this.addDescription(String.format("actual URI '%s' does not ends in '%s'",
          actual.getUri().getPath(), this.getExpectedUri().getPath()));
        
        return false;
      } else {
        return true;
      }
    }
  }
  
  private long getExpectedLong() {
    return this.idBuilder.applyAsLong(this.expected);
  }
  
  private URI getExpectedUri() {
    final UriBuilder uriBuilder = UriBuilder.fromPath("http://localhost");
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);
    
    return this.uriBuilder.apply(pathBuilder, expected);
  }
  
  private boolean checkUri(URI actualUri) {
    final URI expectedUri = this.getExpectedUri();
    
    return actualUri.getPath().endsWith(expectedUri.getPath());
  }
  
  @Factory
  public static <T> IsEqualToIdAndUri<T> equalToIdAndUri(T entity, BiFunction<BaseRestPathBuilder, T, URI> uriBuilder, ToLongFunction<T> idBuilder) {
    return new IsEqualToIdAndUri<T>(entity, uriBuilder, idBuilder);
  }
  
  @Factory
  public static IsEqualToIdAndUri<Institution> equalToInstitutionIdAndUri(Institution institution) {
    return equalToIdAndUri(institution, (pathBuilder, i) -> pathBuilder.institution(i).build(), Institution::getId);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsInstitutionIdAndUrisInAnyOrder(Institution... institutions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToInstitutionIdAndUri, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsInstitutionIdAndUrisInAnyOrder(Iterable<Institution> institutions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToInstitutionIdAndUri, institutions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsInstitutionIdAndUrisInAnyOrder(Stream<Institution> institutions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToInstitutionIdAndUri, institutions);
  }
  
  @Factory
  public static IsEqualToIdAndUri<GamesSession> equalToGamesSessionIdAndUri(GamesSession gamesSession) {
    return equalToIdAndUri(gamesSession, (pathBuilder, ags) -> pathBuilder.gamesSession(ags).build(), GamesSession::getId);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGamesSessionIdAndUrisInAnyOrder(GamesSession... gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGamesSessionIdAndUri, gamesSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGamesSessionIdAndUrisInAnyOrder(Iterable<GamesSession> gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGamesSessionIdAndUri, gamesSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGamesSessionIdAndUrisInAnyOrder(Stream<GamesSession> gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGamesSessionIdAndUri, gamesSessions);
  }
  
  @Factory
  public static IsEqualToIdAndUri<AssignedGamesSession> equalToAssignedSessionIdAndUri(AssignedGamesSession assignedSession) {
    return equalToIdAndUri(assignedSession, (pathBuilder, ags) -> pathBuilder.gamesSessionAssigned(ags).build(), AssignedGamesSession::getId);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsAssignedSessionIdAndUrisInAnyOrder(AssignedGamesSession... assignedSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToAssignedSessionIdAndUri, assignedSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsAssignedSessionIdAndUrisInAnyOrder(Iterable<AssignedGamesSession> assignedSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToAssignedSessionIdAndUri, assignedSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsAssignedSessionIdAndUrisInAnyOrder(Stream<AssignedGamesSession> assignedSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToAssignedSessionIdAndUri, assignedSessions);
  }
  
  @Factory
  public static IsEqualToIdAndUri<GameResult> equalToGameResultIdAndUri(GameResult gamesSession) {
    return equalToIdAndUri(gamesSession, (pathBuilder, gs) -> pathBuilder.gameResult(gs.getId()).build(), GameResult::getId);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGameResultIdAndUrisInAnyOrder(GameResult... gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGameResultIdAndUri, gamesSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGameResultIdAndUrisInAnyOrder(Iterable<GameResult> gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGameResultIdAndUri, gamesSessions);
  }
  
  @Factory
  public static Matcher<Iterable<? extends IdAndUri>> containsGameResultIdAndUrisInAnyOrder(Stream<GameResult> gamesSessions) {
    return containsEntityInAnyOrder(IsEqualToIdAndUri::equalToGameResultIdAndUri, gamesSessions);
  }
}
