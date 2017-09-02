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

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * An abstract {@link Matcher} that can be used to create new matchers that
 * compare entities by their attributes.
 *
 * @author Miguel Reboiro-Jato
 *
 * @param <E>
 *          the type of the expected entities.
 * @param <A>
 *          the type of the actual entities.
 */
public abstract class IsEqualToEntity<E, A> extends TypeSafeMatcher<A> {
  /**
   * The expected entity.
   */
  protected final E expected;

  private Consumer<Description> describeTo;

  /**
   * Constructs a new instance of {@link IsEqualToEntity}.
   *
   * @param entity
   *          the expected entity.
   */
  public IsEqualToEntity(final E entity) {
    this.expected = requireNonNull(entity);
    this.describeTo = d -> {};
  }

  @Override
  public void describeTo(final Description description) {
    this.describeTo.accept(description);
  }

  /**
   * Adds a new description as a function that consumes the {@code Description}
   * object.
   * 
   * @param descriptionFunction
   *          function that consumes a {@code Description} instance, adding the
   *          additional description.
   */
  protected void addDescription(Consumer<Description> descriptionFunction) {
    this.describeTo = this.describeTo.andThen(descriptionFunction);
  }

  /**
   * Adds a new description.
   * 
   * @param description
   *          the new description.
   */
  protected void addDescription(final String description) {
    this.addDescription(d -> d.appendText(description));
  }

  /**
   * Adds a new description using the template:
   * <p>
   * {@code <expected class> entity with value '<expected>' for <attribute>}
   * </p>
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param expected
   *          the expected value.
   */
  protected void addTemplatedValueDescription(final String attribute, final Object expected) {
    this.addDescription(
      d -> d.appendText(
        String.format(
          "%s entity with value '%s' for %s",
          this.expected.getClass().getSimpleName(),
          expected, attribute
        )
      )
    );
  }

  /**
   * Adds a new description using the template:
   * <p>
   * {@code <expected class> entity <expectation> for <attribute>}
   * </p>
   * 
   * @param attribute
   *          the name of the attribute compared.
   * @param expectation
   *          the expected value.
   */
  protected void addTemplatedDescription(final String attribute, final String expectation) {
    this.addDescription(
      String.format(
        "%s entity %s for %s",
        this.expected.getClass().getSimpleName(),
        expectation, attribute
      )
    );
  }

  /**
   * Adds as the description of this matcher the
   * {@link Matcher#describeTo(Description)} method of other matcher.
   *
   * @param matcher
   *          the matcher whose description will be used.
   */
  protected void addMatcherDescription(final Matcher<?> matcher) {
    this.addDescription(matcher::describeTo);
  }

  /**
   * Cleans the current description.
   */
  protected void clearDescribeTo() {
    this.describeTo = d -> {};
  }

  /**
   * Compares the expected and the actual value of an attribute. If the
   * comparison fails, the description of the error will be updated.
   * 
   * Both values will be compared using the
   * {@linkplain java.lang.Object#equals(Object)} method.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          actual entity to be compared.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkAttribute(
    final String attribute,
    final Function<E, R> getterExpected,
    final Function<A, S> getterActual,
    final A actualEntity
  ) {
    return checkAttribute(attribute, getterExpected, getterActual, actualEntity, Object::equals);
  }

  /**
   * Compares the expected and the actual value of an attribute. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          actual entity to be compared.
   * @param comparator
   *          comparing function used to compare the actual and expected
   *          attribute values.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkAttribute(
    final String attribute,
    final Function<E, R> getterExpected,
    final Function<A, S> getterActual,
    final A actualEntity,
    final BiPredicate<R, S> comparator
  ) {
    final R expectedValue = getterExpected.apply(this.expected);
    final S actualValue = getterActual.apply(actualEntity);

    if (expectedValue == null && actualValue == null) {
      return true;
    } else if (expectedValue == null || actualValue == null) {
      this.addTemplatedValueDescription(attribute, expectedValue);

      return false;
    } else {
      return comparator.test(expectedValue, actualValue);
    }
  }

  /**
   * Compares the expected and the actual value of an attribute. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          actual entity to be compared.
   * @param matcherFactory
   *          factory function that creates a matcher that will be used to
   *          compare the attribute values.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean matchAttribute(
    final String attribute,
    final Function<E, R> getterExpected,
    final Function<A, S> getterActual,
    final A actualEntity,
    final Function<R, Matcher<S>> matcherFactory
  ) {
    return this.checkAttribute(
      attribute, getterExpected, getterActual, actualEntity, (expectedValue, actualValue) -> {
        final Matcher<S> matcher = matcherFactory.apply(expectedValue);
        if (matcher.matches(actualValue)) {
          return true;
        } else {
          this.addMatcherDescription(matcher);

          return false;
        }
      }
    );
  }

  /**
   * Compares the expected and the actual value of an array attribute. The array
   * value of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * Arrays will be compared using
   * {@linkplain java.util.Arrays#deepEquals(Object[], Object[])} method.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          actual entity to be compared.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkArrayAttribute(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity
  ) {
    return this.checkArrayAttribute(attribute, getterExpected, getterActual, actualEntity, Arrays::deepEquals);
  }

  /**
   * Compares the expected and the actual value of an array attribute. The array
   * value of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          actual entity to be compared.
   * @param comparator
   *          comparing function used to compare the actual and expected
   *          attribute values. Both arrays should have the same length.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkArrayAttribute(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity,
    final BiPredicate<R[], S[]> comparator
  ) {
    final R[] expectedValue = getterExpected.apply(this.expected);
    final S[] actualValue = getterActual.apply(actualEntity);

    if (expectedValue == null && actualValue == null) {
      return true;
    } else if (expectedValue == null || actualValue == null) {
      this.addTemplatedValueDescription(attribute, Arrays.toString(expectedValue));

      return false;
    } else if (expectedValue.length != actualValue.length) {
      this.addTemplatedDescription(attribute, "to have an array length of " + expectedValue.length);

      return false;
    } else if (comparator.test(expectedValue, actualValue)) {
      return true;
    } else {
      this.addTemplatedValueDescription(attribute, Arrays.toString(expectedValue));

      return false;
    }
  }

  /**
   * Compares the expected and the actual value of an array attribute. The array
   * value of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param matcherFactory
   *          a function that creates a matcher for the expected array value.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean matchArrayAttribute(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity,
    final Function<R[], Matcher<Iterable<? extends S>>> matcherFactory
  ) {
    return this.checkArrayAttribute(
      attribute, getterExpected, getterActual, actualEntity, (expectedV, actualV) -> {
        final Matcher<Iterable<? extends S>> matcher = matcherFactory.apply(expectedV);

        if (matcher.matches(asList(actualV))) {
          return true;
        } else {
          this.addMatcherDescription(matcher);

          return false;
        }
      }
    );
  }

  /**
   * Compares the expected and the actual value of an array attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both arrays. If the comparison fails, the description of the error will
   * be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkArrayAttributeValues(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity
  ) {
    return this.checkArrayAttributeValues(attribute, getterExpected, getterActual, actualEntity, Object::equals);
  }

  /**
   * Compares the expected and the actual value of an array attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both arrays. If the comparison fails, the description of the error will
   * be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param comparator
   *          comparing function used to compare the actual and expected values
   *          of each array index. Both arrays should have the same length.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkArrayAttributeValues(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity,
    final BiPredicate<R, S> comparator
  ) {
    final R[] expectedValue = getterExpected.apply(this.expected);
    final S[] actualValue = getterActual.apply(actualEntity);

    if (expectedValue == null && actualValue == null) {
      return true;
    } else if (expectedValue == null || actualValue == null) {
      this.addTemplatedValueDescription(attribute, Arrays.toString(expectedValue));

      return false;
    } else if (expectedValue.length != actualValue.length) {
      this.addTemplatedDescription(attribute, "to have an array length of " + expectedValue.length);

      return false;
    } else {
      for (int i = 0; i < expectedValue.length; i++) {
        if (!comparator.test(expectedValue[i], actualValue[i])) {
          this.addTemplatedDescription(
            attribute,
            String.format("to have the '%s' value at index %d", expectedValue[i].toString(), i)
          );

          return false;
        }
      }

      return true;
    }
  }

  /**
   * Compares the expected and the actual value of an array attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both arrays. If the comparison fails, the description of the error will
   * be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param matcherFactory
   *          a function that creates a matcher for the expected array values.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean matchArrayAttributeValues(
    final String attribute,
    final Function<E, R[]> getterExpected,
    final Function<A, S[]> getterActual,
    final A actualEntity,
    final Function<R, Matcher<S>> matcherFactory
  ) {
    return this.checkArrayAttributeValues(
      attribute, getterExpected, getterActual, actualEntity, (expectedValue, actualValue) -> {
        final Matcher<S> matcher = matcherFactory.apply(expectedValue);

        return matcher.matches(actualEntity);
      }
    );
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * elements of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkIterableAttribute(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity
  ) {
    return this.checkAttribute(attribute, getterExpected, getterActual, actualEntity, Object::equals);
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * elements of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param comparator
   *          comparing function used to compare the actual and expected values.
   *          Both arrays should have the same length.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkIterableAttribute(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity,
    final BiPredicate<Iterable<R>, Iterable<S>> comparator
  ) {
    final Iterable<R> expectedValue = getterExpected.apply(this.expected);
    final Iterable<S> actualValue = getterActual.apply(actualEntity);

    if (expectedValue == null && actualValue == null) {
      return true;
    } else if (expectedValue == null || actualValue == null) {
      this.addTemplatedValueDescription(attribute, expectedValue);

      return false;
    } else if (iterableSize(expectedValue) != iterableSize(actualValue)) {
      this.addTemplatedDescription(attribute, "to have an iterable length of " + iterableSize(expectedValue));

      return false;
    } else if (comparator.test(expectedValue, actualValue)) {
      return true;
    } else {
      this.addTemplatedValueDescription(attribute, expectedValue);

      return false;
    }
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * elements of the attribute will be checked using a custom matcher. If the
   * comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param matcherFactory
   *          a function that creates a matcher for the expected iterable
   *          values.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean matchIterableAttribute(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity,
    final Function<Iterable<R>, Matcher<Iterable<? extends S>>> matcherFactory
  ) {
    return this.checkIterableAttribute(
      attribute, getterExpected, getterActual, actualEntity, (expectedValue, actualValue) -> {
        final Matcher<Iterable<? extends S>> matcher = matcherFactory.apply(expectedValue);

        if (matcher.matches(actualValue)) {
          return true;
        } else {
          this.addMatcherDescription(matcher);

          return false;
        }
      }
    );
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both iterables. If the comparison fails, the description of the error
   * will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkIterableAttributeValues(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity
  ) {
    return this.checkIterableAttributeValues(attribute, getterExpected, getterActual, actualEntity, Object::equals);
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both iterables. If the comparison fails, the description of the error
   * will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param comparator
   *          comparing function used to compare the actual and expected values
   *          of each iterable index. Both arrays should have the same length.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean checkIterableAttributeValues(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity,
    final BiPredicate<R, S> comparator
  ) {
    final Iterable<R> expectedIterable = getterExpected.apply(this.expected);
    final Iterable<S> actualIterable = getterActual.apply(actualEntity);

    if (expectedIterable == null && actualIterable == null) {
      return true;
    } else if (expectedIterable == null || actualIterable == null) {
      this.addTemplatedValueDescription(attribute, expectedIterable);

      return false;
    } else {
      final Iterator<R> itExpected = expectedIterable.iterator();
      final Iterator<S> itActual = actualIterable.iterator();

      int i = 0;
      while (itExpected.hasNext() && itActual.hasNext()) {
        final R expectedValue = itExpected.next();
        final S actualValue = itActual.next();

        if (!comparator.test(expectedValue, actualValue)) {
          this.addTemplatedValueDescription(
            attribute,
            String.format("different expected (%s) and actual (%s) values at index %d", expectedValue, actualValue, i)
          );

          return false;
        }

        i++;
      }

      if (itExpected.hasNext() || itExpected.hasNext()) {
        this.addTemplatedValueDescription(attribute, "expected and actual values have different lengths");

        return false;
      }

      return true;
    }
  }

  /**
   * Compares the expected and the actual value of an iterable attribute. The
   * comparison is done side by side, comparing the values in the same indexes
   * of both iterables. If the comparison fails, the description of the error
   * will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @param matcherFactory
   *          a function that creates a matcher for the expected iterable
   *          values.
   * @param <R>
   *          type of the value returned by the {@code getterExpected}.
   * @param <S>
   *          type of the value returned by the {@code getterActual}.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected <R, S> boolean matchIterableAttributeValues(
    final String attribute,
    final Function<E, Iterable<R>> getterExpected,
    final Function<A, Iterable<S>> getterActual,
    final A actualEntity,
    final Function<R, Matcher<? extends S>> matcherFactory
  ) {
    return this.checkIterableAttributeValues(
      attribute, getterExpected, getterActual, actualEntity, (expectedValue, actualValue) -> {
        final Matcher<? extends S> matcher = matcherFactory.apply(expectedValue);

        return matcher.matches(actualEntity);
      }
    );
  }

  /**
   * Compares the expected and the actual value of an int array attribute. If
   * the comparison fails, the description of the error will be updated.
   *
   * @param attribute
   *          the name of the attribute compared.
   * @param getterExpected
   *          the getter function of the attribute for the expected entity.
   * @param getterActual
   *          the getter function of the attribute for the actual entity.
   * @param actualEntity
   *          the actual entity being compared to the expected entity.
   * @return {@code true} if the value of the expected and actual attributes are
   *         equals and {@code false} otherwise. If the result is {@code false},
   *         the current description will be updated.
   */
  protected boolean checkIntArrayAttribute(
    final String attribute,
    final Function<E, int[]> getterExpected,
    final Function<A, int[]> getterActual,
    final A actualEntity
  ) {
    final int[] expectedValue = getterExpected.apply(this.expected);
    final int[] actualValue = getterActual.apply(actualEntity);

    if (expectedValue == null && actualValue == null) {
      return true;
    } else if (expectedValue == null || actualValue == null) {
      this.addTemplatedValueDescription(attribute, Arrays.toString(expectedValue));
      return false;
    } else if (!Arrays.equals(expectedValue, actualValue)) {
      this.addTemplatedValueDescription(attribute, Arrays.toString(expectedValue));
      return false;
    } else
      return true;
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities.
   */
  @SafeVarargs
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInAnyOrder(
    final Function<E, Matcher<? super A>> converter, final E... entities
  ) {
    return containsEntityInAnyOrder(converter, stream(entities));
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities.
   */
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInAnyOrder(
    final Function<E, Matcher<? super A>> converter, final Iterable<E> entities
  ) {
    return containsEntityInAnyOrder(converter, StreamSupport.stream(entities.spliterator(), false));
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities.
   */
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInAnyOrder(
    final Function<E, Matcher<? super A>> converter, final Stream<E> entities
  ) {
    final Collection<Matcher<? super A>> entitiesMatchers = entities
      .map(converter)
      .collect(toList());

    return containsInAnyOrder(entitiesMatchers);
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities in the same received order.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values, in the order to be
   *          compared.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities in the same
   *         received order.
   */
  @SafeVarargs
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInOrder(
    final Function<E, Matcher<? super A>> converter, final E... entities
  ) {
    return containsEntityInOrder(converter, stream(entities));
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities in the same received order.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values, in the order to be
   *          compared.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities in the same
   *         received order.
   */
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInOrder(
    final Function<E, Matcher<? super A>> converter, final Iterable<E> entities
  ) {
    return containsEntityInOrder(converter, StreamSupport.stream(entities.spliterator(), false));
  }

  /**
   * Utility method that generates a {@link Matcher} that compares several
   * entities in the same received order.
   *
   * @param converter
   *          a function to create a matcher for an entity.
   * @param entities
   *          the entities to be used as the expected values, in the order to be
   *          compared.
   * @param <E>
   *          the type of the expected entities.
   * @param <A>
   *          the type of the actual entity.
   * @return a new {@link Matcher} that compares several entities in the same
   *         received order.
   */
  protected static <E, A> Matcher<Iterable<? extends A>> containsEntityInOrder(
    final Function<E, Matcher<? super A>> converter, final Stream<E> entities
  ) {
    final List<Matcher<? super A>> matchersList = entities
      .map(converter)
      .collect(toList());

    return contains(matchersList);
  }

  /**
   * Returns the size of an iterable.
   * 
   * @param iterable
   *          the iterable to be measured.
   * @return the size of the iterable.
   */
  protected static int iterableSize(Iterable<?> iterable) {
    return (int) StreamSupport.stream(iterable.spliterator(), false)
      .count();
  }
  
  protected static <R, S> Function<R, Iterable<S>> wrapArrayToIterableFunction(Function<R, S[]> array) {
    return array.andThen(Arrays::asList);
  }
  
  protected static <R, S> Function<R, Iterable<S>> wrapStreamToIterableFunction(Function<R, Stream<S>> array) {
    return array.andThen(input -> input.collect(toList()));
  }
  
  protected static <R, S> Function<R, S> unwrapOptionalFuncion(Function<R, Optional<S>> getter) {
    return getter.andThen(value -> value.orElse(null));
  }
}
