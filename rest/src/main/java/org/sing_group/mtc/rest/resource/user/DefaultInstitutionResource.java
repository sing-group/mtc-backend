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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.net.URI;
import java.util.Arrays;

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
import org.sing_group.mtc.rest.entity.user.InstitutionEditionData;
import org.sing_group.mtc.rest.entity.user.TherapistData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.route.BaseRestPathBuilder;
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
  
  @Inject
  private UserMapper userMapper;
  
  @Context
  private UriInfo uriInfo;
  
  private BaseRestPathBuilder pathBuilder;
  
  @PostConstruct
  private void createPathBuilder() {
    this.pathBuilder = new BaseRestPathBuilder(this.uriInfo.getBaseUriBuilder());
  }

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
  public Response get(@PathParam("id") long id) {
    final Institution institution = this.service.get(id);
    
    return Response
      .ok(this.mapper.toData(institution, this.uriInfo.getBaseUriBuilder()))
    .build();
  }
  
  @GET
  @ApiOperation(
    value = "Returns all the institutions in the database.",
    response = InstitutionData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of institutions in the database.")
  )
  @Override
  public Response list(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final ListingOptions options = new ListingOptions(start, end, sortField, order);
    
    final InstitutionData[] institutions = this.service.list(options)
      .map(institution -> this.mapper.toData(institution, this.uriInfo.getBaseUriBuilder()))
    .toArray(InstitutionData[]::new);
    
    System.out.println(Arrays.toString(institutions));
    
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
    
    final URI institutionUri = this.buildUriForInstitution(institution);
    
    return Response.created(institutionUri).build();
  }

  @PUT
  @Path("{id}")
  @ApiOperation(
    value = "Modifies an existing institution.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the institution modified."),
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id}")
  )
  @Override
  public Response update(
    @PathParam("id") long id,
    InstitutionEditionData data
  ) {
    Institution institution = this.service.update(mapper.toInstitution(id, data));
    
    final boolean isManagerChanged = institution.getManager()
      .map(Manager::getLogin)
      .map(login -> !login.equals(data.getManager()))
    .orElse(false);
    
    if (isManagerChanged) {
      institution = this.service.changeManager(institution, data.getManager());
    }
    
    return Response.ok()
      .header("Location", this.buildUriForInstitution(institution))
    .build();
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
  public Response delete(@PathParam("id") long id) {
    this.service.delete(id);
    
    return Response.ok().build();
  }
  
  @GET
  @Path("{id}/manager")
  @ApiOperation(
    value = "Returns the manager of an institution.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id}")
  )
  @Override
  public Response getManager(@PathParam("id") long id) {
    final Institution institution = this.service.get(id);
    final Manager manager = institution.getManager()
      .orElseThrow(() -> new IllegalArgumentException("No manager found for institution: " + id));
    
    return Response.ok(this.userMapper.toData(manager, this.uriInfo.getBaseUriBuilder())).build();
  }
  
  @GET
  @Path("{id}/therapist")
  @ApiOperation(
    value = "Returns all the therapists that belong to an institution.",
    response = TherapistData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of therapists that belong to the institution.")
  )
  @Override
  public Response listTherapists(
    @PathParam("id") long id,
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("sort") String sortField,
    @QueryParam("order") @DefaultValue("NONE") SortDirection order
  ) {
    final ListingOptions options = new ListingOptions(start, end, sortField, order);
    
    final Institution institution = this.service.get(id);
    final TherapistData[] therapists = this.service.listTherapists(id, options)
      .map(therapist -> this.userMapper.toData(therapist, this.uriInfo.getBaseUriBuilder()))
    .toArray(TherapistData[]::new);
    
    return Response.ok(therapists)
      .header("X-Total-Count", institution.getTherapists().count())
    .build();
  }
  
  @GET
  @Path("{id}/therapist/{login}")
  @ApiOperation(
    value = "Returns a therapists that belongs to an institution.",
    response = TherapistData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown institution: {id} | Unknown login: {login}")
  )
  @Override
  public Response getTherapists(
    @PathParam("id") long id,
    @PathParam("login") String login
  ) {
    final Institution institution = this.service.get(id);
    final Therapist therapist = institution.getTherapists()
      .filter(t -> t.getLogin().equals(login))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Unknown therapist: " + login));
    
    return Response
      .ok(this.userMapper.toData(therapist, this.uriInfo.getBaseUriBuilder()))
    .build();
  }
  
  private URI buildUriForInstitution(Institution institution) {
    return this.pathBuilder.institution(institution).build();
  }
}
