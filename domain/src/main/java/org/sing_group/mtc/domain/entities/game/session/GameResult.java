/*-
 * #%L
 * Domain
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

package org.sing_group.mtc.domain.entities.game.session;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static javax.persistence.GenerationType.IDENTITY;
import static org.sing_group.fluent.checker.Checks.requireAfter;
import static org.sing_group.fluent.checker.Checks.requirePositive;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.sing_group.mtc.domain.entities.game.Game;

@Entity
@Table(
  name = "game_result",
  uniqueConstraints = @UniqueConstraint(columnNames = {
    "assignedGamesSession", "gameOrder", "gamesSession", "attempt"
  })
)
public class GameResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns(
    value = @JoinColumn(
      name = "assignedGamesSession", referencedColumnName = "id", nullable = false
    ),
    foreignKey = @ForeignKey(name = "FK_gameresult_assignedgamessession")
  )
  private AssignedGamesSession assignedGamesSession;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns(
    value = {
      @JoinColumn(name = "gamesSession", referencedColumnName = "gamesSession", nullable = false),
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false)
    },
    foreignKey = @ForeignKey(name = "FK_gameresult_sessiongame")
  )
  private GameInGamesSession gameConfiguration;

  @Column(name = "attempt", nullable = false, length = 2)
  private int attempt;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start", nullable = false)
  private Date start;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end", nullable = true)
  private Date end;

  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "result", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(
    name = "game_result_value",
    joinColumns = @JoinColumn(
      name = "gameResult", referencedColumnName = "id",
      nullable = false, updatable = false, insertable = false
    ),
    foreignKey = @ForeignKey(name = "FK_gameresult_resultvalues")
  )
  private Map<String, String> resultValues;
  
  // For JPA
  GameResult() {}
  
  public GameResult(int attempt) {
    this(attempt, null, null, null, null, emptyMap());
  }
  
  public GameResult(
    int attempt,
    Date start,
    Date end,
    AssignedGamesSession assignedSession,
    GameInGamesSession gameConfiguration,
    Map<String, String> results
  ) {
    this.attempt = requirePositive(attempt, "attempt should be a positive number");
    this.start = start;
    this.end = end;
    this.resultValues = new HashMap<>(results);
    
    this.setAssignedGamesSession(assignedSession);
    this.setGameConfiguration(gameConfiguration);
  }
  
  public Long getId() {
    return id;
  }

  public int getAttempt() {
    return attempt;
  }

  public Date getStart() {
    return start;
  }

  public Optional<Date> getEnd() {
    return Optional.ofNullable(end);
  }

  public void setEnd(Date end) {
    this.end = requireAfter(end, this.start, "end should be after start");
  }
  
  public Optional<AssignedGamesSession> getAssignedGamesSession() {
    return Optional.ofNullable(assignedGamesSession);
  }
  
  public void setAssignedGamesSession(AssignedGamesSession assignedSession) {
    if (assignedSession != null) {
      checkSameGameSession(assignedSession, this.gameConfiguration);
    }
    
    if (this.assignedGamesSession != null) {
      this.assignedGamesSession.directRemoveGameResult(this);
      this.assignedGamesSession = null;
    }
    
    if (assignedSession != null) {
      this.assignedGamesSession = assignedSession;
      this.assignedGamesSession.directAddGameResult(this);
    }
  }
  
  public Optional<GameInGamesSession> getGameConfiguration() {
    return Optional.ofNullable(gameConfiguration);
  }
  
  public Optional<GamesSession> getGamesSession() {
    if (this.gameConfiguration != null) {
      return this.getGameConfiguration().map(GameInGamesSession::getGamesSession);
    } else if (this.assignedGamesSession != null) {
      return this.getAssignedGamesSession().map(AssignedGamesSession::getGamesSession).get();
    } else {
      return Optional.empty();
    }
  }
  
  public void setGameConfiguration(GameInGamesSession gameConfiguration) {
    if (gameConfiguration != null) {
      checkSameGameSession(this.assignedGamesSession, gameConfiguration);
    }
    
    this.gameConfiguration = gameConfiguration;
  }
  
  public Map<String, String> getResultValues() {
    return unmodifiableMap(this.resultValues);
  }
  
  private static void checkSameGameSession(
    AssignedGamesSession assignedSession,
    GameInGamesSession gameConfiguration
  ) {
    if (assignedSession != null && gameConfiguration != null) {
      if (gameConfiguration.getGamesSession() == null) 
        throw new IllegalArgumentException("gameConfiguration should have an assigned session");
      
      if (assignedSession.getGamesSession() == null)
        throw new IllegalArgumentException("assignedSession should have an assigned session");
      
      final long gsIdConfig = gameConfiguration.getGamesSession().getId();
      final long gsIdAssigned = assignedSession.getGamesSession().map(GamesSession::getId).get();
      
      if (gsIdConfig != gsIdAssigned) {
        throw new IllegalArgumentException("gameConfiguration should have the same session as the assignedSession");
      }
    }
  }
  
  public String getGameId() {
    return this.getGameConfiguration()
      .flatMap(GameInGamesSession::getGame)
      .map(Game::getId)
    .orElseThrow(() -> new IllegalStateException("gameId can't be retrieved if no game configuration is assigned"));
  }
  
  public int getGameIndex() {
    return this.assignedGamesSession.getGameIndex(this.getGameConfiguration().orElse(null));
  }

  public String getPatientLogin() {
    return this.assignedGamesSession.getPatientLogin();
  }

  public String getTherapistLogin() {
    return this.assignedGamesSession.getTherapistLogin();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GameResult other = (GameResult) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
