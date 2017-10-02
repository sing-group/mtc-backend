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

import static java.util.Objects.requireNonNull;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class ListingOptionsQueryBuilder {
  private final ListingOptions options;

  public ListingOptionsQueryBuilder(ListingOptions options) {
    this.options = requireNonNull(options, "options can't be null");
  }
  
  public <T> CriteriaQuery<T> addOrder(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root) {
    if (options.hasOrder()) {
      final Path<Object> orderField = root.get(options.getOrder());
      final Order order;
      
      switch (options.getSort()) {
        case ASC:
          order = cb.asc(orderField);
          break;
        case DESC:
          order = cb.desc(orderField);
          break;
        default:
          throw new IllegalStateException("Invalid sort direction: " + options.getSort());
      }
      
      return query.orderBy(order);
    } else {
      return query;
    }
  }
  
  public <T> TypedQuery<T> addLimits(TypedQuery<T> query) {
    if (this.options.hasResultLimits()) {
      return query
        .setFirstResult(options.getStart())
        .setMaxResults(options.getMaxResults());
    } else {
      return query;
    }
  }
}