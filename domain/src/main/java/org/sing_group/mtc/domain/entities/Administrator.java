/*
 * #%L
 * Domain
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
package org.sing_group.mtc.domain.entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An administrator of the application.
 * 
 * @author Miguel Reboiro Jato
 */
@Entity
@DiscriminatorValue("ADMIN")
@XmlRootElement(name = "admin", namespace = "http://entity.domain.javaee.sing-group.org")
public class Administrator extends User implements Serializable {
  private static final long serialVersionUID = 1L;

  // Required for JPA
  Administrator() {}

  /**
   * Creates a new instance of {@code Administrator}.
   * 
   * @param email
   *          the email that identifies the user. This parameter must be a non
   *          empty and non {@code null} string with a maximum length of 100
   *          chars.
   * @param password
   *          the raw password of the user. This parameter must be a non
   *          {@code null} string with a minimum length of 6 chars.
   * 
   * @throws NullPointerException
   *           if a {@code null} value is passed as the value for any parameter.
   * @throws IllegalArgumentException
   *           if value provided for any parameter is not valid according to its
   *           description.
   */
  public Administrator(String email, String password, String name, String surname, boolean encodePassword) {
    super(email, password, name, surname, encodePassword);
  }

  public Administrator(Long id, String email, String password, String name, String surname, boolean encodePassword) {
    super(id, email, password, name, surname, encodePassword);
  }

  public Administrator(Long id, String email, String password, String name, String surname) {
    super(id, email, password, name, surname);
  }

  public Administrator(String email, String password, String name, String surname) {
    super(email, password, name, surname);
  }
  
}
