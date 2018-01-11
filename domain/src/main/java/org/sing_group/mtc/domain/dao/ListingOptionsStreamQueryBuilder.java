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

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

public class ListingOptionsStreamQueryBuilder {
  private final ListingOptions options;

  public ListingOptionsStreamQueryBuilder(ListingOptions options) {
    this.options = requireNonNull(options, "options can't be null");
  }
  
  public <T, X extends Comparable<? super X>> Stream<T> build(
    Stream<T> stream, Function<String, Function<T, X>> nameToFunction
  ) {
    if (options.hasOrder()) {
      final Function<T, X> valueExtractor = nameToFunction.apply(options.getSortField());
      final Comparator<T> comparator = comparing(valueExtractor);
      
      stream = stream.sorted(comparator);
    }
    
    if (options.hasResultLimits()) {
      stream = stream
        .skip(options.getStart())
        .limit(options.getEnd() - options.getStart() + 1);
    }
    
    return stream;
  }
}
