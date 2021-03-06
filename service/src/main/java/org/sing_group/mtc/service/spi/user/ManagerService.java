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

package org.sing_group.mtc.service.spi.user;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.mtc.domain.dao.ListingOptions;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Therapist;

@Local
public interface ManagerService {
  public Manager get(String login);

  public Stream<Manager> list(ListingOptions listingOptions);
  
  public long count();

  public Manager create(Manager manager);

  public Manager update(Manager manager);

  public void delete(String login);
  
  public Stream<Institution> listInstitutions(String login, ListingOptions listingOptions);

  public Stream<Therapist> listTherapists(String login, ListingOptions options);

  public Therapist changeInstitution(String therapistLogin, long institutionId);
}
