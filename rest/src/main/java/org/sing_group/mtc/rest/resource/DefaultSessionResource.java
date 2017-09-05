/*
 * #%L
 * REST
 * %%
 * Copyright (C) 2014 - 2017 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.mtc.rest.resource;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.SessionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("session")
@Api("session")
@Stateless
@Default
@CrossDomain
public class DefaultSessionResource implements SessionResource {
  private Logger LOG = LoggerFactory.getLogger(DefaultSessionResource.class);
  
  @Context
  private HttpServletRequest request;

  @Override
  @GET
  @ApiOperation(
    value = "Checks the provided credentials",
    code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
    @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE)
  })
  public Response check(
    @QueryParam("login") String login,
    @QueryParam("password") String password
  ) {
    try {
      request.logout();
      request.login(login, password);
      
      return Response.ok().build();
    } catch (ServletException e) {
      LOG.warn("Login attempt error", e);
      
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }
}
