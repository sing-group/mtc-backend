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
import static org.sing_group.mtc.rest.resource.entity.mapper.UserMapper.toTherapist;

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

import org.sing_group.mtc.domain.entities.session.GamesSession;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.resource.entity.mapper.UserMapper;
import org.sing_group.mtc.rest.resource.entity.user.TherapistData;
import org.sing_group.mtc.rest.resource.entity.user.TherapistEditionData;
import org.sing_group.mtc.service.spi.user.InstitutionService;
import org.sing_group.mtc.service.spi.user.TherapistService;

@Path("therapist")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
@Consumes({ APPLICATION_JSON, APPLICATION_XML })
@Stateless
public class TherapistResource {
  @Inject
  private TherapistService service;
  
  @Inject
  private InstitutionService institutionService;
  
  @Context
  private UriInfo uriInfo;
  
  public URI buildUriFor(Therapist therapist) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(therapist.getLogin())
    .build();
  }
  
  public URI buildUriForInstitution(Therapist therapist) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(therapist.getLogin())
      .path("institution")
    .build();
  }
  
  public URI buildUriForPatient(Patient patient) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(patient.getTherapist().getLogin())
      .path("patient")
      .path(patient.getLogin())
    .build();
  }
  
  public URI buildUriForSession(GamesSession session) {
    return uriInfo.getBaseUriBuilder()
      .path(this.getClass().getAnnotation(Path.class).value())
      .path(session.getTherapist().getLogin())
      .path("session")
      .path(session.getId().toString())
    .build();
  }
  
  @GET
  @Path("{login}")
  public Response get(@PathParam("login") String login) {
    final Therapist user = this.service.get(login);

    return Response.ok(toData(user)).build();
  }
  
  @GET
  public Response list() {
    final TherapistData[] therapists = this.service.list()
      .map(this::toData)
    .toArray(TherapistData[]::new);
    
    return Response.ok(therapists).build();
  }
  
  @POST
  public Response create(TherapistEditionData data) {
    final Therapist therapist = this.service.create(toTherapist(data, this.institutionService.get(data.getInstitution())));
    
    final URI userUri = this.buildUriFor(therapist);
    
    return Response.created(userUri).build();
  }
  
  @PUT
  public Response update(
    TherapistEditionData data
  ) {
    this.service.update(toTherapist(data, this.institutionService.get(data.getInstitution())));
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{login}")
  public Response delete(@PathParam("login") String login) {
    this.service.delete(login);
    
    return Response.ok().build();
  }
  
  private TherapistData toData(Therapist therapist) {
    return UserMapper.toData(
      therapist,
      institution -> this.buildUriForInstitution(therapist),
      this::buildUriForPatient,
      this::buildUriForSession
    );
  }
}
