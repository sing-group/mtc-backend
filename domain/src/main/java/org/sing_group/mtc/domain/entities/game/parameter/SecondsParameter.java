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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.sing_group.mtc.domain.entities.game.Game;

@Entity
@Table(name = "seconds_parameter")
@Cacheable
public class SecondsParameter extends GameParameter<Integer> {
  @Column(name = "defaultValue")
  private int defaultValue;
  
  public SecondsParameter() {
    super();
  }

  public SecondsParameter(Game game, String name, int defaultValue) {
    super(game, name);
    
    this.defaultValue = defaultValue;
  }

  public Integer getDefaultValue() {
    return defaultValue;
  }

  @Override
  public boolean isValid(Integer value) {
    return value >= 1 && value <= 60;
  }
}
