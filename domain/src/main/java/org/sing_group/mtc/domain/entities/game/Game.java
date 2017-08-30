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
package org.sing_group.mtc.domain.entities.game;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.game.parameter.GameParameter;

@Entity
@Table(name = "game")
@Cacheable
public class Game implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "id", length = 255, columnDefinition = "VARCHAR(255)")
  private String id;
  
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "game_type",
    joinColumns = @JoinColumn(name = "gameId", referencedColumnName = "id", nullable = false),
    foreignKey = @ForeignKey(name = "FK_game_type")
  )
  @Column(name = "name", length = 18, nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<GameTaskType> types;
  
  @OneToMany(mappedBy = "game", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GameParameter<?>> parameters;
  
  // For JPA
  Game() {}
  
  public Game(
    String id,
    Collection<GameTaskType> taskTypes,
    Collection<GameParameter<?>> parameters
  ) {
    this.id = id;
    this.types = new HashSet<>(taskTypes);
    this.parameters = new HashSet<>();
    
    parameters.forEach(this::addParameter);
  }
  
  public String getId() {
    return id;
  }
  
  public Stream<GameTaskType> getTypes() {
    return this.types.stream();
  }
  
  public Stream<GameParameter<?>> getParameters() {
    return this.parameters.stream();
  }
  
  public boolean addParameter(GameParameter<?> parameter) {
    if (this.parameters.add(parameter)) {
      if (parameter.getGame() != this)
        parameter.setGame(this);
        
      return true;
    } else {
      return false;
    }
  }
  
  public boolean removeParameter(GameParameter<?> parameter) {
    if (this.parameters.remove(parameter)) {
      if (parameter.getGame() != null)
        parameter.setGame(null);
      
      return true;
    } else {
      return false;
    }
  }
  
  public boolean hasParameter(GameParameter<?> parameter) {
    return this.parameters.contains(parameter);
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
    Game other = (Game) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
