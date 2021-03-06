/*-
 * #%L
 * Service
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

package org.sing_group.mtc.service.user;

import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.dao.spi.user.InstitutionDAO;
import org.sing_group.mtc.domain.dao.spi.user.ManagerDAO;
import org.sing_group.mtc.domain.dao.spi.user.TherapistDAO;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.service.security.SecurityGuard;
import org.sing_group.mtc.service.security.check.SecurityCheckBuilder;
import org.sing_group.mtc.service.spi.user.InstitutionService;

@Default
@Stateless
@RolesAllowed("ADMIN")
public class DefaultInstitutionService implements InstitutionService {
  @Inject
  private InstitutionDAO dao;
  
  @Inject
  private TherapistDAO therapistDao;
  
  @Inject
  private ManagerDAO managerDao;

  @Inject
  private SecurityGuard securityGuard;
  
  @Inject
  private SecurityCheckBuilder checkThat;
  
  @RolesAllowed({ "MANAGER", "ADMIN", "THERAPIST" })
  @Override
  public Institution get(long id) {
    return this.securityGuard.ifAuthorized(
        checkThat.hasRole(RoleType.ADMIN),
        checkThat.hasLogin(() -> dao.get(id).getManager().map(Manager::getLogin).orElse("")),
        checkThat.hasAnyLoginOf(() -> dao.get(id).getTherapists().map(Therapist::getLogin).toArray(String[]::new))
      )
    .call(() ->  this.dao.get(id));
  }
  
  @Override
  public Stream<Institution> list(ListingOptions listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public long count() {
    return this.dao.count();
  }

  @Override
  public Institution create(Institution institution) {
    return this.dao.create(institution);
  }

  @Override
  public Institution update(Institution institution) {
    return this.dao.update(institution);
  }
  
  @Override
  public Institution changeManager(Institution institution, String managerLogin) {
    requireNonNull(institution, "institution can't be null");
    requireNonNull(managerLogin, "managerLogin can't be null");
    
    final Manager manager = this.managerDao.get(managerLogin);
    final Institution persistentInstitution = this.get(institution.getId());
    
    persistentInstitution.setManager(manager);
    
    return persistentInstitution;
  }

  @Override
  public void delete(long id) {
    this.dao.delete(id);
  }

  @RolesAllowed({ "MANAGER", "ADMIN" })
  @Override
  public Stream<Therapist> listTherapists(long id, ListingOptions options) {
    return this.securityGuard.ifAuthorized(
        checkThat.hasRole(RoleType.ADMIN),
        checkThat.hasLogin(() -> dao.get(id).getManager().map(Manager::getLogin).orElse(""))
      )
    .call(() -> this.therapistDao.listByInstitution(this.dao.get(id), options));
  }

}
