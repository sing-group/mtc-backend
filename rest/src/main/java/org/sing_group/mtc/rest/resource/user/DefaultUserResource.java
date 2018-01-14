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

package org.sing_group.mtc.rest.resource.user;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.user.UserResource;
import org.sing_group.mtc.service.spi.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("user")
@Api("user")
@Stateless
@Default
@CrossDomain
public class DefaultUserResource implements UserResource {
  private Logger LOG = LoggerFactory.getLogger(DefaultUserResource.class);
  
  @Context
  private HttpServletRequest request;
  
  @Inject
  private UserService userService;

  @Override
  @GET
  @Path("role")
  @ApiOperation(
    value = "Checks the provided credentials",
    response = RoleType.class,
    code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
    @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE)
  })
  public Response role(
    @QueryParam("login") String login,
    @QueryParam("password") String password
  ) {
    try {
      request.logout();
      request.login(login, password);
      
      return Response.ok(
        User.getRoleName(this.userService.getCurrentUser())
      ).build();
    } catch (ServletException e) {
      LOG.warn("Login attempt error", e);
      
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }
}
