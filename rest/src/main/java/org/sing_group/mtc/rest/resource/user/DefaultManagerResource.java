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
import static org.sing_group.mtc.rest.entity.mapper.UserMapper.toManager;

import java.net.URI;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
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

import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.rest.entity.mapper.UserMapper;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.user.ManagerResource;
import org.sing_group.mtc.service.spi.user.ManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@Path("manager")
@Api(
  value = "manager",
  authorizations = @Authorization("basicAuth")
)
@ApiResponses({
  @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE),
  @ApiResponse(code = 403, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
})
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
@Default
@CrossDomain
public class DefaultManagerResource implements ManagerResource {
  @Inject
  private ManagerService service;
  
  @Context
  private UriInfo uriInfo;
  
  public URI buildUriFor(Institution institution) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(institution.getManager().map(Manager::getLogin).orElseThrow(IllegalStateException::new))
      .path("institution")
      .path(institution.getId().toString())
    .build();
  }
  
  @Override
  @GET
  @Path("{login}")
  @ApiOperation(
    value = "Finds managers by login.",
    response = ManagerData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login} | 'login' should have a length between 1 and 100")
  )
  public Response get(@PathParam("login") String login) {
    final Manager user = this.service.get(login);

    return Response.ok(toData(user)).build();
  }
  
  @Override
  @GET
  @ApiOperation(
    value = "Returns all the managers in the database.",
    response = ManagerData.class,
    responseContainer = "List",
    code = 200
  )
  public Response list() {
    final ManagerData[] managers = this.service.list()
      .map(this::toData)
    .toArray(ManagerData[]::new);
    
    return Response.ok(managers).build();
  }
  
  @Override
  @POST
  @ApiOperation(
    value = "Creates a new manager.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new manager created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  public Response create(ManagerEditionData data) {
    final Manager manager = this.service.create(toManager(data));
    
    final URI userUri = this.buildUriFor(manager);
    
    return Response.created(userUri).build();
  }
  
  @Override
  @PUT
  @ApiOperation(
    value = "Modifies an existing manager.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response update(
    ManagerEditionData data
  ) {
    this.service.update(UserMapper.toManager(data));
    
    return Response.ok().build();
  }
  
  @Override
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing manager.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
  
  private URI buildUriFor(Manager manager) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(manager.getLogin())
    .build();
  }
  
  private ManagerData toData(Manager manager) {
    return UserMapper.toData(manager, this::buildUriFor);
  }
}
