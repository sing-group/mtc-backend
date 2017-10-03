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
package org.sing_group.mtc.rest.entity;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.sing_group.mtc.rest.entity.game.session.GamesSessionData;
import org.sing_group.mtc.rest.entity.user.AdministratorData;
import org.sing_group.mtc.rest.entity.user.IdentifiedUserData;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.ManagerData;
import org.sing_group.mtc.rest.entity.user.PatientData;
import org.sing_group.mtc.rest.entity.user.TherapistData;

/**
 * This class have methods to read list of entities
 * 
 * @author Miguel Reboiro-Jato
 *
 */
public final class GenericTypes {
  private GenericTypes() {}

  public static class UserDataListType extends GenericType<List<IdentifiedUserData>> {
    public static UserDataListType USER_DATA_LIST_TYPE = new UserDataListType();

    /**
     * Get a list of events from a response
     * 
     * @param response
     *          response received by rest
     * @return list of events
     */
    public static List<IdentifiedUserData> readEntity(Response response) {
      return response.readEntity(USER_DATA_LIST_TYPE);
    }
  }

  public static class AdministratorDataListType extends GenericType<List<AdministratorData>> {
    public static AdministratorDataListType ADMINISTRATOR_DATA_LIST_TYPE = new AdministratorDataListType();

    public static List<AdministratorData> readEntity(Response response) {
      return response.readEntity(ADMINISTRATOR_DATA_LIST_TYPE);
    }
  }
  
  public static class ManagerDataListType extends GenericType<List<ManagerData>> {
    public static ManagerDataListType MANAGER_DATA_LIST_TYPE = new ManagerDataListType();
    
    public static List<ManagerData> readEntity(Response response) {
      return response.readEntity(MANAGER_DATA_LIST_TYPE);
    }
  }
  
  public static class TherapistDataListType extends GenericType<List<TherapistData>> {
    public static TherapistDataListType THERAPIST_DATA_LIST_TYPE = new TherapistDataListType();
    
    public static List<TherapistData> readEntity(Response response) {
      return response.readEntity(THERAPIST_DATA_LIST_TYPE);
    }
  }
  
  public static class PatientDataListType extends GenericType<List<PatientData>> {
    public static PatientDataListType PATIENT_DATA_LIST_TYPE = new PatientDataListType();
    
    public static List<PatientData> readEntity(Response response) {
      return response.readEntity(PATIENT_DATA_LIST_TYPE);
    }
  }
  
  public static class GamesSessionDataListType extends GenericType<List<GamesSessionData>> {
    public static GamesSessionDataListType GAMES_SESSION_DATA_LIST_TYPE = new GamesSessionDataListType();
    
    public static List<GamesSessionData> readEntity(Response response) {
      return response.readEntity(GAMES_SESSION_DATA_LIST_TYPE);
    }
  }
  
  public static class InstitutionDataListType extends GenericType<List<InstitutionData>> {
    public static InstitutionDataListType INSTITUTION_DATA_LIST_TYPE = new InstitutionDataListType();
    
    public static List<InstitutionData> readEntity(Response response) {
      return response.readEntity(INSTITUTION_DATA_LIST_TYPE);
    }
  }
}