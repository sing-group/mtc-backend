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
package org.sing_group.mtc.rest.entity.mapper.user;

import static java.util.Collections.emptyList;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.net.URI;
import java.util.function.Function;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.rest.entity.mapper.spi.user.InstitutionMapper;
import org.sing_group.mtc.rest.entity.user.InstitutionData;
import org.sing_group.mtc.rest.entity.user.InstitutionEditionData;
import org.sing_group.mtc.service.spi.user.ManagerService;

@Default
@Transactional(value = MANDATORY)
public class DefaultInstitutionMapper implements InstitutionMapper {
  @Inject
  private ManagerService service;
  
  @Override
  public InstitutionData toData(
    Institution institution,
    Function<Manager, URI> managerToUri,
    Function<Therapist, URI> therapistToUri
  ) {
    return new InstitutionData(
      institution.getId(),
      institution.getName(),
      institution.getDescription().orElse(null),
      institution.getAddress().orElse(null),
      institution.getManager().map(managerToUri).orElseThrow(IllegalStateException::new),
      institution.getTherapists().map(therapistToUri).toArray(URI[]::new)
    );
  }

  @Override
  public Institution toInstitution(InstitutionEditionData data) {
    return new Institution(
      data.getName(),
      this.service.get(data.getManager()),
      data.getDescription(),
      data.getAddress(),
      emptyList()
    );
  }

  @Override
  public Institution toInstitution(int id, InstitutionEditionData data) {
    return new Institution(
      id,
      data.getName(),
      null,
      data.getDescription(),
      data.getAddress(),
      emptyList()
    );
  }

  @Override
  public InstitutionEditionData toEditionData(Institution institution) {
    return new InstitutionEditionData(
      institution.getName(),
      institution.getManager().map(Manager::getLogin).orElseThrow(IllegalArgumentException::new),
      institution.getDescription().orElse(null),
      institution.getAddress().orElse(null)
    );
  }
}
