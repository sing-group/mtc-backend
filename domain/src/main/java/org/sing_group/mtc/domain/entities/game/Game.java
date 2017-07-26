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
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "game")
public class Game implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  private String id;
  
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
    name = "game_type",
    joinColumns = @JoinColumn(name = "gameId", referencedColumnName = "id")
  )
  @Column(name = "name")
  private Set<GameTaskType> types;
  
  @ElementCollection
  @CollectionTable(
    name = "game_parameter",
    joinColumns = @JoinColumn(name = "gameId", referencedColumnName = "id")
  )
  @Column(name = "value")
  private List<String> parameters;
  
  public String getId() {
    return id;
  }
  
  public Set<GameTaskType> getTypes() {
    return types;
  }
  
  public List<String> getParameters() {
    return parameters;
  }
}
