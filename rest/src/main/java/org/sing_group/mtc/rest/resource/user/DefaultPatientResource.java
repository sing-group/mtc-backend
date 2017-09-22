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
import static org.sing_group.mtc.rest.entity.mapper.UserMapper.toPatient;

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
import org.sing_group.mtc.domain.entities.game.session.AssignedGamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.rest.entity.mapper.UserMapper;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.PatientEditionData;
import org.sing_group.mtc.rest.filter.CrossDomain;
import org.sing_group.mtc.rest.mapper.SecurityExceptionMapper;
import org.sing_group.mtc.rest.resource.spi.user.PatientResource;
import org.sing_group.mtc.service.spi.user.PatientService;
import org.sing_group.mtc.service.spi.user.TherapistService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.ResponseHeader;

@Path("patient")
@Api(
  value = "patient",
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
@CrossDomain(allowedHeaders = { "X-Total-Count" }, allowRequestHeaders = true)
public class DefaultPatientResource implements PatientResource {
  @Inject
  private PatientService service;
  
  @Inject
  private TherapistService therapistService;
  
  @Context
  private UriInfo uriInfo;
  
  @Override
  @GET
  @Path("{login}")
  @ApiOperation(
    value = "Finds patients by login.",
    response = PatientData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login} | 'login' should have a length between 1 and 100")
  )
  public Response get(@PathParam("login") String login) {
    final Patient user = this.service.get(login);

    return Response.ok(toData(user)).build();
  }
  
  @Override
  @GET
  @ApiOperation(
    value = "Returns all the patients in the database.",
    response = PatientData.class,
    responseContainer = "List",
    code = 200,
    responseHeaders = @ResponseHeader(name = "X-Total-Count", description = "Total number of therapists in the database.")
  )
  public Response list(
    @QueryParam("start") @DefaultValue("-1") int start,
    @QueryParam("end") @DefaultValue("-1") int end,
    @QueryParam("order") String order,
    @QueryParam("sort") @DefaultValue("NONE") SortDirection sort
  ) {
    final ListingOptions options = new ListingOptions(start, end, order, sort);
    
    final PatientData[] patients = this.service.list(options)
      .map(this::toData)
    .toArray(PatientData[]::new);
    
    return Response.ok(patients)
      .header("X-Total-Count", this.service.count())
    .build();
  }
  
  @Override
  @POST
  @ApiOperation(
    value = "Creates a new patient.",
    responseHeaders = @ResponseHeader(name = "Location", description = "Location of the new patient created."),
    code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  public Response create(PatientEditionData data) {
    final Patient patient = this.service.create(toPatient(data, therapistService.get(data.getTherapist())));
    
    final URI userUri = this.buildUriFor(patient);
    
    return Response.created(userUri).build();
  }
  
  @Override
  @PUT
  @ApiOperation(
    value = "Modifies an existing patient.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response update(
    PatientEditionData data
  ) {
    this.service.update(toPatient(data, therapistService.get(data.getTherapist())));
    
    return Response.ok().build();
  }
  
  @Override
  @DELETE
  @Path("{login}")
  @ApiOperation(
    value = "Deletes an existing patient.",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown user: {login}")
  )
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
  
  private URI buildUriFor(Patient patient) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(patient.getLogin())
    .build();
  }
  
  private URI buildUriForTherapist(Patient patient) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(patient.getLogin())
      .path("therapist")
    .build();
  }
  
  private URI buildUriForAssignedSession(AssignedGamesSession session) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(session.getPatient().map(Patient::getLogin).orElseThrow(IllegalStateException::new))
      .path("assignedsession")
      .path(Integer.toString(session.getId()))
    .build();
  }
  
  private PatientData toData(Patient patient) {
    return UserMapper.toData(patient, therapist -> this.buildUriForTherapist(patient), this::buildUriForAssignedSession);
  }
}
