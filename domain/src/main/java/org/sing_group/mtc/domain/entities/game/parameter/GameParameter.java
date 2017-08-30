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
package org.sing_group.mtc.domain.entities.game.parameter;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.parameter.GameParameter.GameParameterId;

@Entity
@Table(name = "game_parameter")
@Cacheable
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(GameParameterId.class)
public abstract class GameParameter<T> {
  @Id
  @Column(name = "gameId", length = 255, columnDefinition = "VARCHAR(255)")
  private String gameId;
  
  @Id
  @Column(name = "id", length = 255, columnDefinition = "VARCHAR(255)")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "gameId",
    referencedColumnName = "id",
    updatable = false, insertable = false,
    foreignKey = @ForeignKey(name = "FK_gameparameter_game")
  )
  private Game game;
  
  // For JPA
  GameParameter() {}
  
  public GameParameter(String id, Game game) {
    this.id = id;
    this.setGame(game);
  }

  public String getId() {
    return id;
  }
  
  public Game getGame() {
    return game;
  }
  
  public void setGame(Game game) {
    if (this.game != null) {
      this.game.removeParameter(this);
      this.game = null;
      this.gameId = null;
    }
    
    if (game != null) {
      this.game = game;
      this.gameId = this.game.getId();
      this.game.addParameter(this);
    }
  }
  
  public abstract T getDefaultValue();
  
  public abstract boolean isValid(T value);
  
  @Embeddable
  public final static class GameParameterId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String gameId;
    
    private String id;

    public String getGameId() {
      return gameId;
    }
    
    public void setGameId(String gameId) {
      this.gameId = gameId;
    }

    public String getId() {
      return id;
    }
    
    public void setId(String id) {
      this.id = id;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
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
      GameParameterId other = (GameParameterId) obj;
      if (gameId == null) {
        if (other.gameId != null)
          return false;
      } else if (!gameId.equals(other.gameId))
        return false;
      if (id == null) {
        if (other.id != null)
          return false;
      } else if (!id.equals(other.id))
        return false;
      return true;
    }
  }
}
