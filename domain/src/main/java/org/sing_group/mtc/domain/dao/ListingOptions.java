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
package org.sing_group.mtc.domain.dao;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ListingOptions {
  private final int start;
  private final int end;
  private final String order;
  private final SortDirection sort;
  
  public ListingOptions(int start, int end, String order, SortDirection sort) {
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
    
    if (order != null && (sort == null || sort == SortDirection.NONE)) {
      throw new IllegalArgumentException("An sort direction should be provided when using an order field");
    }
    
    this.order = order;
    this.sort = sort;
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
    return this.order != null;
  }

  public String getOrder() {
    return this.order;
  }

  public SortDirection getSort() {
    return sort;
  }
  
}
