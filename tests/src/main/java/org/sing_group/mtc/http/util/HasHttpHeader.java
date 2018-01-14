/*-
 * #%L
 * Tests
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
  private final String valueDescription;
  
  public HasHttpHeader(String header) {
    this(header, Objects::nonNull, " to be present");
  }

  public HasHttpHeader(String header, String expected) {
    this(header, actual -> expected.equals(actual), " to have value \"" + expected + "\"");
    
    requireNonNull(expected);
  }
  
  public HasHttpHeader(String header, Predicate<String> valueChecker, String valueDescription) {
    this.header = requireNonNull(header);
    this.valueChecker = requireNonNull(valueChecker);
    this.valueDescription = requireNonNull(valueDescription);
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(this.header).appendText(" header").appendText(this.valueDescription);
  }

  @Override
  protected void describeMismatchSafely(Response response, Description description) {
    description.appendValue(this.header).appendText(" header");
    
    if (response.getHeaders().containsKey(this.header)) {
      description.appendText(" had value ").appendValue(response.getHeaderString(this.header));
    } else {
      description.appendText(" was not present");
    }
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
  public static HasHttpHeader hasHttpHeader(String header, int value) {
    return new HasHttpHeader(header, Integer.toString(value));
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeader(String header, long value) {
    return new HasHttpHeader(header, Long.toString(value));
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeader(String header, boolean value) {
    return new HasHttpHeader(header, Boolean.toString(value));
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeader(String header, Predicate<String> valueChecker, String valueDescription) {
    return new HasHttpHeader(header, valueChecker, valueDescription);
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeaderEndingWith(String header, String value) {
    return new HasHttpHeader(header, h -> h.endsWith(value), " that ends with \"" + value + "\" value");
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeaderStartingWith(String header, String value) {
    return new HasHttpHeader(header, h -> h.startsWith(value), " that starts with \"" + value + "\" value");
  }
  
  @Factory
  public static HasHttpHeader hasHttpHeaderContaining(String header, String value) {
    return new HasHttpHeader(header, h -> h.contains(value), " that contains \"" + value + "\" value");
  }
}
