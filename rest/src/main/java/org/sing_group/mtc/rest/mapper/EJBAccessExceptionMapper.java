/*
 * #%L
 * REST
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
package org.sing_group.mtc.rest.mapper;

import static java.util.Objects.requireNonNull;
import static org.sing_group.mtc.rest.mapper.SecurityExceptionMapper.FORBIDDEN_MESSAGE;
import static org.sing_group.mtc.rest.mapper.SecurityExceptionMapper.UNAUTHORIZED_MESSAGE;

import java.security.Principal;

import javax.ejb.EJBAccessException;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class EJBAccessExceptionMapper
implements ExceptionMapper<EJBAccessException> {
  private final static Logger LOG = LoggerFactory.getLogger(IllegalArgumentException.class);

  private Principal principal;
  
  public EJBAccessExceptionMapper() {}
  
  public EJBAccessExceptionMapper(Principal principal) {
    this.setPrincipal(principal);
  }
  
  @Inject
  public void setPrincipal(Principal principal) {
    this.principal = requireNonNull(principal);
  }
  
  @Override
  public Response toResponse(EJBAccessException e) {
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
