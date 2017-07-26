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

import javax.annotation.security.RunAs;
import javax.inject.Inject;
import javax.resource.spi.SecurityException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.entity.UserData;
import org.sing_group.mtc.service.UserService;

@Path("session")
@Produces({
    MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@Consumes({
    MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@RunAs("PATIENT")
public class SessionResource {
  @Context
  private HttpServletRequest request;
  
  @Inject
  private UserService userService;

  @GET
  public Response check(
    @QueryParam("email") String email,
    @QueryParam("password") String password
  ) throws SecurityException {
    try {
      request.logout();
      request.login(email, password);
      
      final User user = userService.get(email);
      
      return Response.ok(UserData.of(user)).build();
    } catch (ServletException e) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }
}
