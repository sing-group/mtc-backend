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
import org.sing_group.mtc.domain.entities.game.session.GameConfigurationForSession.GameConfigurationForSessionId;

@Entity
@Table(name = "session_game_configuration")
@IdClass(GameConfigurationForSessionId.class)
public class GameConfigurationForSession implements Comparable<GameConfigurationForSession>, Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "session",
    referencedColumnName = "id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_sessiongame_gamesession")
  )
  private GamesSession session;

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
    name = "session_game_param_value", joinColumns = {
      @JoinColumn(name = "session", referencedColumnName = "session", nullable = false, updatable = false, insertable = false),
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false, updatable = false, insertable = false)
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
    
    this.setSession(session);
    this.paramValues = new HashMap<>(paramValues);
  }

  public int getGameOrder() {
    return gameOrder;
  }
  
  public Optional<Game> getGame() {
    return Optional.ofNullable(game);
  }

  public GamesSession getSession() {
    return session;
  }
  
  public void setSession(GamesSession session) {
    if (this.session != null) {
      this.session.directRemoveGameConfiguration(this);
      this.session = null;
    }
    
    if (session != null) {
      this.session = session;
      this.session.directAddGameConfiguration(this);
    }
  }

  public Map<String, String> getParamValues() {
    return unmodifiableMap(this.paramValues);
  }

  @Override
  public int compareTo(GameConfigurationForSession o) {
    return Compare.objects(this, o)
      .by(config -> config == null || config.getSession() == null || config.getSession().getId() == null ? -1 : config.getSession().getId())
      .thenBy(config -> config.gameOrder)
    .andGet();
  }

  @Embeddable
  public static class GameConfigurationForSessionId implements Serializable {
    private static final long serialVersionUID = 1L;

    private long session;
    
    private int gameOrder;
    
    // For JPA
    GameConfigurationForSessionId() {}
    
    public GameConfigurationForSessionId(long session, int gameOrder) {
      this.session = session;
      this.gameOrder = gameOrder;
    }
    
    public long getSession() {
      return this.session;
    }
    
    public void setSession(long session) {
      this.session = session;
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
      result = prime * result + (int) (session ^ (session >>> 32));
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
      if (gameOrder != other.gameOrder)
        return false;
      if (session != other.session)
        return false;
      return true;
    }
  }
}
