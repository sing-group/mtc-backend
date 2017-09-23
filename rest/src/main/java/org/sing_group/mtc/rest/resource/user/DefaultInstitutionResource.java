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

import java.net.URI;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.mapper.spi.user.InstitutionMapper;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.InstitutionEditionData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.user.InstitutionResource;
import org.sing_group.mtc.service.spi.user.InstitutionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;
@Path("institution")
@Api(
  value = "institution",
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
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultInstitutionResource implements InstitutionResource {
  @Inject
  private InstitutionService service;
  
  @Inject
  private InstitutionMapper mapper;
  
  @Context
  private UriInfo uriInfo;

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Finds institutions by id.",
    response = InstitutionData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id}")
  )
  @Override
  public Response get(@PathParam("id") int id) {
    System.err.println("ID: " + id);
    final Institution institution = this.service.get(id);
    
    return Response.ok(this.toData(institution)).build();
  }
  
  @GET
  @ApiOperation(
    value = "Returns all the institutions in the database.",
    response = AdministratorData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of institutions in the database.")
  )
  @Override
  public Response list(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("order") String order,
    @QueryParam("sort") @DefaultValue("NONE") SortDirection sort
  ) {
    final ListingOptions options = new ListingOptions(start, end, order, sort);
    
    final InstitutionData[] institutions = this.service.list(options)
      .map(this::toData)
    .toArray(InstitutionData[]::new);
    
    return Response.ok(institutions)
      .header("X-Total-Count", this.service.count())
    .build();
  }

  
  @POST
  @ApiOperation(
    value = "Creates a new institution.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new institution created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  @Override
  public Response create(InstitutionEditionData data) {
    final Institution institution = this.service.create(mapper.toInstitution(data));
    
    final URI institutionUri = this.buildUriFor(institution);
    
    return Response.created(institutionUri).build();
  }

  @PUT
  @Path("{id}")
  @ApiOperation(
    value = "Modifies an existing institution.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id}")
  )
  @Override
  public Response update(
    @PathParam("id") int id,
    InstitutionEditionData data
  ) {
    this.service.update(mapper.toInstitution(id, data));
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing institution.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id}")
  )
  @Override
  public Response delete(@PathParam("id") int id) {
    this.service.delete(id);
    
    return Response.ok().build();
  }
  
  private InstitutionData toData(Institution institution) {
    return this.mapper.toData(institution, this::buildUriFor, this::buildUriFor);
  }

  private URI buildUriFor(Institution institution) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path("institution")
      .path(institution.getId().toString())
    .build();
  }
  
  private URI buildUriFor(Manager manager) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path("manager")
      .path(manager.getLogin())
    .build();
  }
  
  private URI buildUriFor(Therapist therapist) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path("therapist")
      .path(therapist.getLogin())
    .build();
  }
}
