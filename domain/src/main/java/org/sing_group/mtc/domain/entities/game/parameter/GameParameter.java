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
  @Column(name = "gameId", updatable = false, insertable = false)
  private String gameId;
  
  @Id
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gameId", referencedColumnName = "id")
  private Game game;
  
  protected GameParameter() {}
  
  protected GameParameter(Game game, String name) {
    this.game = game;
    this.gameId = game.getId();
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public abstract T getDefaultValue();
  
  public abstract boolean isValid(T value);
  
  @Embeddable
  public final static class GameParameterId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String gameId;
    
    private String name;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      return true;
    }
  }
}
