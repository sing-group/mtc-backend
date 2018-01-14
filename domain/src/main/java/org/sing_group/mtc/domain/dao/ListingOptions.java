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

package org.sing_group.mtc.domain.dao;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ListingOptions {
  private final int start;
  private final int end;
  private final String sortField;
  private final SortDirection sortDirection;
  
  public ListingOptions(int start, int end, String sortField, SortDirection sortDirection) {
    if (min(start, end) < 0) {
      if (max(start, end) >= 0) {
        throw new IllegalArgumentException("start and end parameters should be positive or negative at the same time");
      }
    } else {
      if (start > end)
        throw new IllegalArgumentException("start should be lower or equal to end");
    }
    
    this.start = start;
    this.end = end;
    
    if (sortField != null && (sortDirection == null || sortDirection == SortDirection.NONE)) {
      throw new IllegalArgumentException("An sort direction should be provided when using an order field");
    }
    
    this.sortField = sortField;
    this.sortDirection = sortDirection;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return this.end;
  }
  
  public boolean hasResultLimits() {
    return this.start >= 0;
  }
  
  public int getMaxResults() {
    if (this.hasResultLimits()) {
      return this.end - this.start + 1;
    } else {
      return -1;
    }
  }
  
  public boolean hasOrder() {
    return this.sortField != null;
  }

  public String getSortField() {
    return this.sortField;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }
  
}
