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
package org.sing_group.mtc.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.sing_group.mtc.rest.resource.entity.mapper.UserMapper.toAdministrator;
import static org.sing_group.mtc.rest.resource.entity.mapper.UserMapper.toData;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.rest.resource.entity.mapper.UserMapper;
import org.sing_group.mtc.rest.resource.entity.user.AdministratorData;
import org.sing_group.mtc.rest.resource.entity.user.AdministratorEditionData;
import org.sing_group.mtc.service.spi.user.AdministratorService;

@Path("admin")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
public class AdministratorResource {
  @Inject
  private AdministratorService service;
  
  @Context
  private UriInfo uriInfo;
  
  public URI buildUriFor(Administrator admin) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(admin.getLogin())
    .build();
  }
  
  @GET
  @Path("{login}")
  public Response get(@PathParam("login") String login) {
    final Administrator user = this.service.get(login);

    return Response.ok(toData(user)).build();
  }
  
  @GET
  public Response list() {
    final AdministratorData[] admins = this.service.list()
      .map(UserMapper::toData)
    .toArray(AdministratorData[]::new);
    
    return Response.ok(admins).build();
  }
  
  @POST
  public Response create(AdministratorEditionData data) {
    final Administrator admin = this.service.create(toAdministrator(data));
    
    final URI userUri = this.buildUriFor(admin);
    
    return Response.created(userUri).build();
  }
  
  @PUT
  public Response update(
    AdministratorEditionData data
  ) {
    this.service.update(toAdministrator(data));
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{login}")
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
}
