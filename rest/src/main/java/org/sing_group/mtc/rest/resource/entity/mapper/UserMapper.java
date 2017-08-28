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
package org.sing_group.mtc.rest.resource.entity.mapper;

import static javax.transaction.Transactional.TxType.MANDATORY;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.domain.entities.user.User;
import org.sing_group.mtc.rest.resource.entity.UserEditionData;
import org.sing_group.mtc.service.UserService;

@Default
@Transactional(value = MANDATORY)
public class UserMapper {
  @Inject
  private UserService userService;
  
  public User toUser(int id, UserEditionData userData) {
    switch (userData.getRole()) {
      case "ADMIN":
        return new Administrator(id, userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), false);
      case "PATIENT":
        return new Patient(id, userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), (Therapist) userService.get(userData.getTherapistId()), false);
      case "THERAPIST":
        return new Therapist(id, userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), false);
      default:
        throw new IllegalArgumentException("Invalid userData");
    }
  }
  
  public User toUser(UserEditionData userData) {
    switch (userData.getRole()) {
      case "ADMIN":
        return new Administrator(userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), false);
      case "PATIENT":
        return new Patient(userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), (Therapist) userService.get(userData.getTherapistId()), false);
      case "THERAPIST":
        return new Therapist(userData.getEmail(), userData.getPassword(), userData.getName(), userData.getSurname(), false);
      default:
        throw new IllegalArgumentException("Invalid userData");
    }
  }
}
