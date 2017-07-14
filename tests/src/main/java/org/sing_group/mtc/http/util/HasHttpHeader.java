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

import javax.ws.rs.core.Response;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class HasHttpHeader extends TypeSafeMatcher<Response> {
  private final String header;
  private final String value;
  
  public HasHttpHeader(String header) {
    this(header, null);
  }

  public HasHttpHeader(String header, String value) {
    this.header = requireNonNull(header);
    this.value = null;
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(this.header + ": " + this.value);
  }

  @Override
  protected boolean matchesSafely(Response response) {
    final String headerValue = response.getHeaderString(this.header);
    
    if (this.value == null) {
      return headerValue != null;
    } else {
      return this.value.equals(headerValue);
    }
  }

  @Factory
  public static HasHttpHeader hasHttpHeader(String header) {
    return new HasHttpHeader(header);
  }

  @Factory
  public static HasHttpHeader hasHttpHeader(String header, String value) {
    return new HasHttpHeader(header, value);
  }
}
