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
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.session.SessionGame.SessionGameId;

@Entity
@Table(name = "session_game")
@IdClass(SessionGameId.class)
public class SessionGame implements Comparable<SessionGame>, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "gameOrder")
  private int gameOrder;

  @Id
  @Column(name = "gameId", updatable = false, insertable = false)
  private String gameId;

  @Id
  @Column(name = "sessionId", updatable = false, insertable = false)
  private int sessionId;

  @Id
  @Column(name = "sessionVersion", updatable = false, insertable = false)
  private int sessionVersion;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns({
    @JoinColumn(name = "sessionId", referencedColumnName = "sessionId", nullable = false),
    @JoinColumn(name = "sessionVersion", referencedColumnName = "version", nullable = false)
  })
  private SessionVersion session;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "gameId", referencedColumnName = "id", nullable = false)
  private Game game;

  @ElementCollection
  @MapKeyColumn(name = "param", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(
    name = "session_game_param_value",
    joinColumns = {
      @JoinColumn(name = "gameOrder", referencedColumnName = "gameOrder", nullable = false),
      @JoinColumn(name = "sessionId", referencedColumnName = "sessionId", nullable = false),
      @JoinColumn(name = "sessionVersion", referencedColumnName = "sessionVersion", nullable = false),
      @JoinColumn(name = "gameId", referencedColumnName = "gameId", nullable = false)
    }
  )
  private Map<String, String> paramValues;
  
  public int getGameOrder() {
    return gameOrder;
  }
  
  public Game getGame() {
    return game;
  }
  
  public SessionVersion getSession() {
    return session;
  }
  
  public Map<String, String> getParamValues() {
    return unmodifiableMap(paramValues);
  }

  @Override
  public int compareTo(SessionGame o) {
    return this.getGameOrder() - o.getGameOrder();
  }

  @Embeddable
  public static class SessionGameId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sessionId;
    
    private int sessionVersion;

    private String gameId;

    private int gameOrder;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
      result = prime * result + gameOrder;
      result = prime * result + sessionId;
      result = prime * result + sessionVersion;
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
      SessionGameId other = (SessionGameId) obj;
      if (gameId == null) {
        if (other.gameId != null)
          return false;
      } else if (!gameId.equals(other.gameId))
        return false;
      if (gameOrder != other.gameOrder)
        return false;
      if (sessionId != other.sessionId)
        return false;
      if (sessionVersion != other.sessionVersion)
        return false;
      return true;
    }
  }
}
