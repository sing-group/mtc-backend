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
package org.sing_group.mtc.rest.resource;

import java.net.URI;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.dao.DuplicateEmailException;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.entity.UserData;
import org.sing_group.mtc.rest.resource.entity.UserEditionData;
import org.sing_group.mtc.rest.resource.entity.mapper.UserMapper;
import org.sing_group.mtc.service.UserService;

/**
 * Resource that represents the users in the application.
 * 
 * @author Miguel Reboiro-Jato
 */
@Path("user")
@Produces({
  MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@Consumes({
  MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
})
@Stateless
public class UserResource {
  @Inject
  private UserService service;

  @Context
  private UriInfo uriInfo;
  
  @Inject
  private UserMapper mapper;

  public URI buildUriForUser(User user) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(Integer.toString(user.getId()))
    .build();
  }

  public String buildPathForUser(User user) {
    return this.buildUriForUser(user).toString();
  }
  
  @Path("{id}")
  @GET
  public Response get(@PathParam("id") int id) {
    final User user = this.service.get(id);

    if (user == null)
      throw new IllegalArgumentException("User not found: " + id);
    else
      return Response.ok(UserData.of(user)).build();
  }

  @GET
  public Response list() {
    final UserData[] userData = this.service.list()
      .map(UserData::of)
    .toArray(UserData[]::new);
    
    return Response.ok(userData).build();
  }

  @POST
  public Response create(UserEditionData userData) {
    try {
      final User user = this.service.create(mapper.toUser(userData));
      
      final URI userUri = this.buildUriForUser(user);

      return Response.created(userUri).build();
    } catch (DuplicateEmailException eee) {
      throw new IllegalArgumentException("An user with the same email already exists", eee);
    } catch (RuntimeException re) {
      throw re;
    }
  }

  @PUT
  @Path("{id}")
  public Response update(
    @PathParam("id") int id,
    UserEditionData userData
  ) {
    this.service.update(mapper.toUser(id, userData));

    return Response.ok().build();
  }

  @Path("{id}")
  @DELETE
  public Response delete(@PathParam("id") int id) {
    this.service.remove(id);

    return Response.ok().build();
  }
}
