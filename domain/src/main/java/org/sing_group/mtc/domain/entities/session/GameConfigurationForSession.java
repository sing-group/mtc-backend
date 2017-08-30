/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.entities.session;

import static java.util.Collections.unmodifiableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
import org.sing_group.mtc.domain.entities.session.GameConfigurationForSession.GameConfigurationForSessionId;

@Entity
@Table(name = "session_game")
@IdClass(GameConfigurationForSessionId.class)
public class GameConfigurationForSession implements Comparable<GameConfigurationForSession>, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "gameOrder")
  private int gameOrder;

  @Id
  @Column(name = "gameId")
  private String gameId;

  @Id
  @Column(name = "sessionId")
  private Integer sessionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sessionId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false,
    foreignKey = @ForeignKey(name = "FK_sessiongame_gamesession"))
  private GamesSession session;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "gameId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false,
    foreignKey = @ForeignKey(name = "FK_sessiongame_game"))
  private Game game;

  @ElementCollection
  @MapKeyColumn(name = "param", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(
    name = "session_game_param_value", joinColumns = {
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "sessionId", referencedColumnName = "sessionId", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "gameId", referencedColumnName = "gameId", nullable = false, updatable = false, insertable = false)
    },
    foreignKey = @ForeignKey(name = "FK_sessiongame_paramvalues")
  )
  private Map<String, String> paramValues;
  
  // JPA
  GameConfigurationForSession() {}
  
  public GameConfigurationForSession(
    GamesSession session,
    Game game,
    int gameOrder,
    Map<String, String> paramValues
  ) {
    this.gameOrder = gameOrder;
    this.game = game;
    this.gameId = game == null ? null : game.getId();
    
    this.setSession(session);
    this.paramValues = new HashMap<>(paramValues);
  }

  public int getGameOrder() {
    return gameOrder;
  }

  public String getGameId() {
    return this.gameId;
  }
  
  public Game getGame() {
    return game;
  }

  public GamesSession getSession() {
    return session;
  }
  
  public void setSession(GamesSession session) {
    if (this.session != null) {
      this.session.directRemoveGameConfiguration(this);
      this.session = null;
      this.sessionId = null;
    }
    
    if (session != null) {
      this.session = session;
      this.sessionId = this.session.getId();
      this.session.directAddGameConfiguration(this);
    }
  }

  public Map<String, String> getParamValues() {
    return unmodifiableMap(this.paramValues);
  }

  @Override
  public int compareTo(GameConfigurationForSession o) {
    return Compare.objects(this, o)
      .by(config -> config.sessionId)
      .thenBy(config -> config.gameOrder)
    .andGet();
  }

  @Embeddable
  public static class GameConfigurationForSessionId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;

    private String gameId;

    private int gameOrder;
    
    // For JPA
    GameConfigurationForSessionId() {}
    
    public GameConfigurationForSessionId(int sessionId, String gameId, int gameOrder) {
      this.sessionId = sessionId;
      this.gameId = gameId;
      this.gameOrder = gameOrder;
    }
    
    public int getSessionId() {
      return this.sessionId;
    }

    public String getGameId() {
      return this.gameId;
    }

    public int getGameOrder() {
      return this.gameOrder;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
      result = prime * result + gameOrder;
      result = prime * result + sessionId;
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
      GameConfigurationForSessionId other = (GameConfigurationForSessionId) obj;
      if (gameId == null) {
        if (other.gameId != null)
          return false;
      } else if (!gameId.equals(other.gameId))
        return false;
      if (gameOrder != other.gameOrder)
        return false;
      if (sessionId != other.sessionId)
        return false;
      return true;
    }
  }
}
