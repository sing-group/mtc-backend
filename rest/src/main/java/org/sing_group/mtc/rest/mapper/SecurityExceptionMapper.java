/*-
 * #%L
 * REST
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

package org.sing_group.mtc.rest.mapper;

import static java.util.Objects.requireNonNull;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class SecurityExceptionMapper
implements ExceptionMapper<SecurityException> {
  private final static Logger LOG = LoggerFactory.getLogger(IllegalArgumentException.class);
  
  public final static String UNAUTHORIZED_MESSAGE = "Unauthorized operation";
  public final static String FORBIDDEN_MESSAGE = "Forbidden operation";

  private Principal principal;
  
  public SecurityExceptionMapper() {}
  
  public SecurityExceptionMapper(Principal principal) {
    this.setPrincipal(principal);
  }
  
  @Inject
  public void setPrincipal(Principal principal) {
    this.principal = requireNonNull(principal);
  }

  @Override
  public Response toResponse(SecurityException e) {
    LOG.error("Exception catched", e);
    
    if ("anonymous".equals(principal.getName())) {
      return Response.status(Response.Status.UNAUTHORIZED)
        .entity(UNAUTHORIZED_MESSAGE)
        .type(MediaType.TEXT_PLAIN)
      .build();
    } else {
      return Response.status(Response.Status.FORBIDDEN)
        .entity(FORBIDDEN_MESSAGE)
        .type(MediaType.TEXT_PLAIN)
      .build();
    }
  }
}
