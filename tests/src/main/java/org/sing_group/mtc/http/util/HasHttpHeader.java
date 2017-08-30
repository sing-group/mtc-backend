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
package org.sing_group.mtc.http.util;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Predicate;

import javax.ws.rs.core.Response;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class HasHttpHeader extends TypeSafeMatcher<Response> {
  private final String header;
  private final Predicate<String> valueChecker;
  
  public HasHttpHeader(String header) {
    this(header, Objects::nonNull);
  }

  public HasHttpHeader(String header, String expected) {
    this(header, actual -> expected.equals(actual));
    
    requireNonNull(expected);
  }
  
  public HasHttpHeader(String header, Predicate<String> valueChecker) {
    this.header = requireNonNull(header);
    this.valueChecker = requireNonNull(valueChecker);
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(this.header + ": " + this.valueChecker);
  }

  @Override
  protected boolean matchesSafely(Response response) {
    return response.getHeaders().containsKey(this.header)
      && this.valueChecker.test(response.getHeaderString(this.header));
  }

  @Factory
  public static HasHttpHeader hasHttpHeader(String header) {
    return new HasHttpHeader(header);
  }

  @Factory
  public static HasHttpHeader hasHttpHeader(String header, String value) {
    return new HasHttpHeader(header, value);
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeader(String header, Predicate<String> valueChecker) {
    return new HasHttpHeader(header, valueChecker);
  }
}
