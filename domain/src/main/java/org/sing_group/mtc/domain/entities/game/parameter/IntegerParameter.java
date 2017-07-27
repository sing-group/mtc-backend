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
@Table(name = "integer_parameter")
@Cacheable
public class IntegerParameter extends GameParameter<Integer> {
  @Column(name = "defaultValue")
  private int defaultValue;
  
  @Column(name = "min")
  private int minValue;
  
  @Column(name = "max")
  private int maxValue;
  
  public IntegerParameter() {
    super();
  }

  public IntegerParameter(Game game, String name, int defaultValue, int minValue, int maxValue) {
    super(game, name);
    
    this.defaultValue = defaultValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public Integer getDefaultValue() {
    return defaultValue;
  }
  
  public int getMinValue() {
    return minValue;
  }
  
  public int getMaxValue() {
    return maxValue;
  }

  @Override
  public boolean isValid(Integer value) {
    return value >= this.minValue && value <= this.maxValue;
  }
}
