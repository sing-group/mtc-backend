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
package org.sing_group.mtc.domain.entities.user;

import static org.sing_group.fluent.checker.Checks.requireEmail;
import static org.sing_group.fluent.checker.Checks.requireStringSize;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdentifiedUser extends User {
  private static final long serialVersionUID = 1L;
  
  @Column(length = 100, nullable = false, unique = true)
  protected String email;

  @Column(length = 100, nullable = true)
  protected String name;

  @Column(length = 100, nullable = true)
  protected String surname;

  // For JPA
  IdentifiedUser() {}

  public IdentifiedUser(String login, String email, String password) {
    this(login, password, password, null, null);
  }

  public IdentifiedUser(String login, String email, String password, boolean encodedPassword) {
    this(login, password, password, null, null, encodedPassword);
  }

  public IdentifiedUser(String login, String email, String password, String name, String surname) {
    super(login, password);
    
    this.setEmail(email);
    this.setName(name);
    this.setSurname(surname);
  }

  public IdentifiedUser(String login, String email, String password, String name, String surname, boolean encodedPassword) {
    super(login, password, encodedPassword);
    
    this.setEmail(email);
    this.setName(name);
    this.setSurname(surname);
  }

  /**
   * Returns the email of this user.
   * 
   * @return the email of this user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of this user.
   * 
   * @param email
   *          the email of the user. This parameter must be a non empty and non
   *          {@code null} string with a maximum length of 100 chars.
   * @throws NullPointerException
   *           if {@code null} is passed as parameter.
   * @throws IllegalArgumentException
   *           if the length of the string passed is not valid.
   */
  public void setEmail(String email) {
    this.email = requireEmail(email, 100, "'email' should have email format and a length between 1 and 100");
  }
  
  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public void setName(String name) {
    this.name = name == null
      ? null
      : requireStringSize(name, 0, 100, "'name' should be null or have a length between 0 and 100");
  }

  public Optional<String> getSurname() {
    return Optional.ofNullable(this.surname);
  }

  public void setSurname(String surname) {
    this.surname = surname == null
      ? null
      : requireStringSize(surname, 0, 100, "'surname' should be null or have a length between 0 and 100");
  }

}
