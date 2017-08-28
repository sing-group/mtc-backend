/*
 * #%L
 * Tests
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
package org.sing_group.mtc.domain.entities;

import java.util.function.BiPredicate;
import java.util.function.Function;

import org.hamcrest.Matcher;

/**
 * An abstract {@link Matcher} that can be used to create new matchers that
 * compare entities by their attributes.
 *
 * @author Miguel Reboiro-Jato
 *
 * @param <T>
 *          the type of the entities to be matched.
 */
public abstract class IsEqualToEntityOfSameType<T> extends IsEqualToEntity<T, T> {
  /**
   * Constructs a new instance of {@link IsEqualToEntityOfSameType}.
   *
   * @param entity
   *          the expected entity.
   */
  public IsEqualToEntityOfSameType(final T entity) {
    super(entity);
  }
  
  protected <R> boolean checkAttribute(
    final String attribute,
    final Function<T, R> getter,
    final T actual
  ) {
    return super.checkAttribute(attribute, getter, getter, actual);
  }

  protected <R> boolean checkAttribute(
    final String attribute,
    final Function<T, R> getter,
    final T actual,
    final BiPredicate<R, R> comparator
  ) {
    return super.checkAttribute(attribute, getter, getter, actual, comparator);
  }

  protected <R> boolean matchAttribute(
    final String attribute,
    final Function<T, R> getter,
    final T actual,
    final Function<R, Matcher<R>> matcherFactory
  ) {
    return super.matchAttribute(attribute, getter, getter, actual, matcherFactory);
  }
  
  protected <R> boolean checkArrayAttributeValues(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual
  ) {
    return super.checkArrayAttributeValues(attribute, getter, getter, actual);
  }
  
  protected <R> boolean checkArrayAttributeValues(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual,
    final BiPredicate<R, R> comparator
  ) {
    return super.checkArrayAttributeValues(attribute, getter, getter, actual, comparator);
  }
  
  protected <R> boolean matchArrayAttributeValues(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual,
    final Function<R, Matcher<R>> matcherFactory
  ) {
    return super.matchArrayAttributeValues(attribute, getter, getter, actual, matcherFactory);
  }
  
  protected <R> boolean checkArrayAttribute(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual
  ) {
    return super.checkArrayAttribute(attribute, getter, getter, actual);
  }
  
  protected <R> boolean checkArrayAttribute(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual,
    final BiPredicate<R[], R[]> comparator
  ) {
    return super.checkArrayAttribute(attribute, getter, getter, actual, comparator);
  }
  
  protected <R> boolean matchArrayAttribute(
    final String attribute,
    final Function<T, R[]> getter,
    final T actual,
    final Function<R[], Matcher<Iterable<? extends R>>> matcherFactory
  ) {
    return super.matchArrayAttribute(attribute, getter, getter, actual, matcherFactory);
  }
  
  protected <R> boolean checkIterableAttribute(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual
  ) {
    return super.checkAttribute(attribute, getter, getter, actual);
  }
  
  protected <R> boolean checkIterableAttribute(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual,
    final BiPredicate<Iterable<R>, Iterable<R>> comparator
  ) {
    return super.checkAttribute(attribute, getter, getter, actual, comparator);
  }
  
  protected <R> boolean matchIterableAttribute(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual,
    final Function<Iterable<R>, Matcher<Iterable<? extends R>>> matcherFactory
  ) {
    return super.matchIterableAttribute(attribute, getter, getter, actual, matcherFactory);
  }
  
  protected <R> boolean checkIterableAttributeValues(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual
  ) {
    return super.checkIterableAttributeValues(attribute, getter, getter, actual);
  }
  
  protected <R> boolean checkIterableAttributeValues(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual,
    final BiPredicate<R, R> comparator
  ) {
    return super.checkIterableAttributeValues(attribute, getter, getter, actual, comparator);
  }
  
  protected <R> boolean matchIterableAttributeValues(
    final String attribute,
    final Function<T, Iterable<R>> getter,
    final T actual,
    final Function<R, Matcher<? extends R>> matcherFactory
  ) {
    return super.matchIterableAttributeValues(attribute, getter, getter, actual, matcherFactory);
  }
}
