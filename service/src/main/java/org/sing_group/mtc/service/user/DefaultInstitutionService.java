/*
 * #%L
 * Service
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
package org.sing_group.mtc.service.user;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.spi.user.InstitutionDAO;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.service.spi.user.InstitutionService;

@Default
@Stateless
@RolesAllowed({ "MANAGER", "THERAPIST" })
public class DefaultInstitutionService implements InstitutionService {
  @Inject
  private InstitutionDAO dao;
  

  @Override
  public Institution get(String name) {
    return this.dao.get(name);
  }

}
