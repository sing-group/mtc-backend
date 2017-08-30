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

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.METHOD_NOT_ALLOWED;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

public class HasHttpStatus extends TypeSafeMatcher<Response> {
  private StatusType status;

  public HasHttpStatus(StatusType status) {
    this.status = status;
  }

  public HasHttpStatus(int statusCode) {
    this(Response.Status.fromStatusCode(statusCode));
  }
  
  @Override
  protected void describeMismatchSafely(Response item, Description mismatchDescription) {
    mismatchDescription.appendText("was ").appendValue(item.getStatusInfo());
  }

  @Override
  public void describeTo(Description description) {
    description.appendValue(this.status);
  }

  @Override
  protected boolean matchesSafely(Response item) {
    return this.status.getStatusCode() == item.getStatus();
  }

  @Factory
  public static HasHttpStatus hasHttpStatus(int statusCode) {
    return new HasHttpStatus(statusCode);
  }

  @Factory
  public static HasHttpStatus hasHttpStatus(StatusType status) {
    return new HasHttpStatus(status);
  }

  @Factory
  public static HasHttpStatus hasOkStatus() {
    return new HasHttpStatus(OK);
  }

  @Factory
  public static HasHttpStatus hasCreatedStatus() {
    return new HasHttpStatus(CREATED);
  }

  @Factory
  public static HasHttpStatus hasMethodNotAllowedStatus() {
    return new HasHttpStatus(METHOD_NOT_ALLOWED);
  }

  @Factory
  public static HasHttpStatus hasBadRequestStatus() {
    return new HasHttpStatus(BAD_REQUEST);
  }

  public static HasHttpStatus hasUnauthorizedStatus() {
    return new HasHttpStatus(UNAUTHORIZED);
  }

  public static HasHttpStatus hasForbiddenStatus() {
    return new HasHttpStatus(FORBIDDEN);
  }
}
