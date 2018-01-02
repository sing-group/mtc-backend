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

import javax.annotation.PostConstruct;
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
import org.sing_group.mtc.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.ManagerCreationData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.ManagerEditionData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;
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
@CrossDomain(allowedHeaders = { "X-Total-Count", "Location" }, allowRequestHeaders = true)
public class DefaultManagerResource implements ManagerResource {
  @Inject
  private ManagerService service;
  
  @Inject
  private UserMapper mapper;
  
  @Inject
  private InstitutionMapper institutionMapper;
  
  @Context
  private UriInfo uriInfo;
  
  private BaseRestPathBuilder pathBuilder;
  
  @PostConstruct
  private void createPathBuilder() {
    this.pathBuilder = new BaseRestPathBuilder(this.uriInfo.getBaseUriBuilder());
  }
  
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
  @Override
  public Response get(@PathParam("login") String login) {
    final Manager user = this.service.get(login);

    return Response.ok(toManagerData(user)).build();
  }
  
  @GET
  @ApiOperation(
    value = "Returns all the managers in the database.",
    response = ManagerData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of managers in the database.")
  )
  @Override
  public Response list(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final ListingOptions options = new ListingOptions(start, end, sortField, order);
    
    final ManagerData[] managers = this.service.list(options)
      .map(this::toManagerData)
    .toArray(ManagerData[]::new);
    
    return Response.ok(managers)
      .header("X-Total-Count", this.service.count())
    .build();
  }
  
  @POST
  @ApiOperation(
    value = "Creates a new manager.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new manager created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  @Override
  public Response create(ManagerCreationData data) {
    final Manager manager = this.service.create(mapper.toManager(data));
    
    final URI userUri = this.pathBuilder.manager(manager).build();
    
    return Response.created(userUri).build();
  }
  
  @PUT
  @Path("{login}")
  @ApiOperation(
    value = "Modifies an existing manager.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the manager modified."),
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response update(
    @PathParam("login") String login,
    ManagerEditionData data
  ) {
    final Manager manager = this.service.update(mapper.toManager(login, data));
    
    final URI userUri = this.pathBuilder.manager(manager).build();
    
    return Response.ok()
      .header("Location", userUri)
    .build();
  }
  
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing manager.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  @Override
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }

  @GET
  @Path("{login}/institution")
  @ApiOperation(
    value = "Returns all the institutions of a manager.",
    response = InstitutionData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of institutions of the manager.")
  )
  @Override
  public Response getInstitutions(
    @PathParam("login") String login,
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final ListingOptions options = new ListingOptions(start, end, sortField, order);
    
    final Manager manager = this.service.get(login);
    final InstitutionData[] institutions = this.service.listInstitutions(login, options)
      .map(this::toInstitutionData)
    .toArray(InstitutionData[]::new);
    
    return Response.ok(institutions)
      .header("X-Total-Count", manager.getInstitutions().count())
    .build();
  }

  @GET
  @Path("{login}/institution/{id}")
  @ApiOperation(
    value = "Returns an institutions.",
    response = InstitutionData.class,
    code = 200
  )
  @Override
  public Response getInstitution(
    @PathParam("login") String login,
    @PathParam("id") long id
  ) {
    final Manager manager = this.service.get(login);
    final Institution institution = manager.getInstitutions()
      .filter(inst -> inst.getId() == id)
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Unknown institution with id: " + id));
    
    return Response.ok(this.toInstitutionData(institution)).build();
  }

  @GET
  @Path("{login}/therapist")
  @ApiOperation(
    value = "Returns all the institutions of a manager.",
    response = InstitutionData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of institutions of the manager.")
  )
  @Override
  public Response getTherapists(
    @PathParam("login") String login,
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final ListingOptions options = new ListingOptions(start, end, sortField, order);
    
    final TherapistData[] therapists = this.service.listTherapists(login, options)
      .map(this::toTherapistData)
    .toArray(TherapistData[]::new);
    
    final Manager manager = this.service.get(login);
    
    final long institutionCount = manager.getManagedTherapists().count();
    
    return Response.ok(therapists)
      .header("X-Total-Count", institutionCount)
    .build();
  }
  
  private TherapistData toTherapistData(Therapist therapist) {
    return mapper.toData(therapist, this.uriInfo.getBaseUriBuilder());
  }
  
  private ManagerData toManagerData(Manager manager) {
    return mapper.toData(manager, this.uriInfo.getBaseUriBuilder());
  }
  
  private InstitutionData toInstitutionData(Institution institution) {
    return institutionMapper.toData(institution, this.uriInfo.getBaseUriBuilder());
  }
}
