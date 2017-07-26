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

import static java.util.Objects.requireNonNull;
import static javax.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

@Entity
@Table(name = "user")
@Inheritance
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING, length = 7)
public abstract class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static String getRole(User user) {
    final Class<? extends User> userClass = user.getClass();
    final DiscriminatorValue dvAnnotation = userClass.getAnnotation(DiscriminatorValue.class);
    
    return dvAnnotation.value();
  }
  
  public static boolean haveSameRole(User user1, User user2) {
    return getRole(user1).equals(getRole(user2));
  }

  @Id
  @GeneratedValue(strategy = IDENTITY)
  protected Long id;
  
  @Column(length = 100, nullable = false, unique = true)
  protected String email;

  @Column(length = 32, nullable = false)
  protected String password;

  @Column(length = 100, nullable = false)
  protected String name;

  @Column(length = 100, nullable = false)
  protected String surname;

  User() {}
  
  public User(Long id, String email, String password, String name, String surname) {
    this(id, email, password, name, surname, true);
  }
  
  public User(String email, String password, String name, String surname) {
    this(email, password, name, surname, true);
  }
  
  public User(Long id, String email, String password, String name, String surname, boolean encodePassword) {
    this.id = id;
    this.setEmail(email);
    
    if (password != null) {
      if (encodePassword)
        this.changePassword(password);
      else
        this.setPassword(password);
    }
    
    this.name = name;
    this.surname = surname;
  }

  /**
   * Creates a new instance of {@code User}.
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
  public User(String email, String password, String name, String surname, boolean encodePassword) {
    this(null, email, password, name, surname, encodePassword);
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
    requireNonNull(email, "email can't be null");
    inclusiveBetween(1, 100, email.length(), "email must have a length between 1 and 100: " + email);

    this.email = email;
  }

  public long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Returns the MD5 of the user's password. Capital letters are used in the
   * returned string.
   * 
   * @return the MD5 of the user's password. Capital letters are used in the
   *         returned string.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the MD5 password of the user. The MD5 string is stored with capital
   * letters.
   * 
   * @param password
   *          the MD5 password of the user. This parameter must be a non
   *          {@code null} MD5 string.
   * @throws NullPointerException
   *           if {@code null} is passed as parameter.
   * @throws IllegalArgumentException
   *           if the string passed is not a valid MD5 string.
   */
  public void setPassword(String password) {
    requireNonNull(password, "password can't be null");
    matchesPattern(password, "[a-zA-Z0-9]{32}", "password must be a valid MD5 string: " + password);

    this.password = password.toUpperCase();
  }

  /**
   * Changes the password of the user. This method receives the raw value of
   * the password and stores it in MD5 format.
   * 
   * @param password
   *          the raw password of the user. This parameter must be a non
   *          {@code null} string with a minimum length of 6 chars.
   * 
   * @throws NullPointerException
   *           if the {@code password} is {@code null}.
   * @throws IllegalArgumentException
   *           if the length of the string passed is not valid.
   */
  public void changePassword(String password) {
    requireNonNull(password, "password can't be null");
    if (password.length() < 6)
      throw new IllegalArgumentException("password can't be shorter than 6");

    try {
      final MessageDigest digester = MessageDigest.getInstance("MD5");
      final HexBinaryAdapter adapter = new HexBinaryAdapter();

      this.password = adapter.marshal(digester.digest(password.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5 algorithm not found", e);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", surname=" + surname + "]";
  }
  
}