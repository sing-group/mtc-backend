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

import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.sing_group.fluent.compare.Compare;
import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.session.GameInGamesSession.GameInGamesSessionId;

@Entity
@Table(name = "game_in_games_session")
@IdClass(GameInGamesSessionId.class)
public class GameInGamesSession implements Comparable<GameInGamesSession>, Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "gamesSession",
    referencedColumnName = "id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_sessiongame_gamesession")
  )
  private GamesSession gamesSession;

  @Id
  @Column(name = "gameOrder")
  private int gameOrder;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "game",
    referencedColumnName = "id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_sessiongame_game")
  )
  private Game game;

  @ElementCollection
  @MapKeyColumn(name = "param", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(
    name = "game_in_games_session_param_value", joinColumns = {
      @JoinColumn(name = "gamesSession", referencedColumnName = "gamesSession", nullable = false),
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false)
    },
    foreignKey = @ForeignKey(name = "FK_sessiongame_paramvalues")
  )
  private Map<String, String> paramValues;
  
  // JPA
  GameInGamesSession() {}
  
  public GameInGamesSession(
    GamesSession session,
    Game game,
    int gameOrder,
    Map<String, String> paramValues
  ) {
    this.gameOrder = gameOrder;
    this.game = game;
    
    this.setGamesSession(session);
    this.paramValues = new HashMap<>(paramValues);
  }

  public int getGameOrder() {
    return gameOrder;
  }
  
  public Optional<Game> getGame() {
    return Optional.ofNullable(game);
  }

  public GamesSession getGamesSession() {
    return gamesSession;
  }
  
  public void setGamesSession(GamesSession session) {
    if (this.gamesSession != null) {
      this.gamesSession.directRemoveGameConfiguration(this);
      this.gamesSession = null;
    }
    
    if (session != null) {
      this.gamesSession = session;
      this.gamesSession.directAddGameConfiguration(this);
    }
  }

  public Map<String, String> getParamValues() {
    return unmodifiableMap(this.paramValues);
  }

  @Override
  public int compareTo(GameInGamesSession o) {
    return Compare.objects(this, o)
      .by(GameInGamesSession::getGameOrder)
    .andGet();
  }

  @Embeddable
  public static class GameInGamesSessionId implements Serializable {
    private static final long serialVersionUID = 1L;

    private long gamesSession;
    
    private int gameOrder;
    
    // For JPA
    GameInGamesSessionId() {}
    
    public GameInGamesSessionId(long gamesSession, int gameOrder) {
      this.gamesSession = gamesSession;
      this.gameOrder = gameOrder;
    }
    
    public long getGamesSession() {
      return this.gamesSession;
    }
    
    public void setGamesSession(long session) {
      this.gamesSession = session;
    }

    public int getGameOrder() {
      return this.gameOrder;
    }
    
    public void setGameOrder(int gameOrder) {
      this.gameOrder = gameOrder;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + gameOrder;
      result = prime * result + (int) (gamesSession ^ (gamesSession >>> 32));
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
      GameInGamesSessionId other = (GameInGamesSessionId) obj;
      if (gameOrder != other.gameOrder)
        return false;
      if (gamesSession != other.gamesSession)
        return false;
      return true;
    }
  }
}
