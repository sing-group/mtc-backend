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
import static org.sing_group.fluent.checker.Checks.requireMD5;
import static org.sing_group.fluent.checker.Checks.requirePattern;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING, length = 9)
public abstract class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static String getRoleName(User user) {
    return getRole(user).name();
  }
  
  public static RoleType getRole(User user) {
    final Class<? extends User> userClass = user.getClass();
    final DiscriminatorValue dvAnnotation = userClass.getAnnotation(DiscriminatorValue.class);
    
    return RoleType.valueOf(dvAnnotation.value());
  }
  
  public static boolean haveSameRole(User user1, User user2) {
    return getRole(user1).equals(getRole(user2));
  }

  @Id
  @Column(length = 100, nullable = false, unique = true)
  protected String login;

  @Column(length = 32, nullable = false)
  protected String password;

  // For JPA
  User() {}
  
  public User(String login, String password) {
    this(login, password, true);
  }
  
  public User(String login, String password, boolean encodedPassword) {
    this.setLogin(login);
    
    if (password != null) {
      if (encodedPassword)
        this.changePassword(password);
      else
        this.setPassword(password);
    }
  }

  public String getLogin() {
    return login;
  }
  
  void setLogin(String login) {
    this.login = requirePattern(login, "[a-zA-ZñÑ0-9]{1,100}", "'login' can only contain letters, numbers or underscore and should have a length between 1 and 100");
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

  public void setPassword(String password) {
    this.password = requireMD5(password, "'password' should be a valid MD5 string").toUpperCase();
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
    result = prime * result + ((login == null) ? 0 : login.hashCode());
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
    if (login == null) {
      if (other.login != null)
        return false;
    } else if (!login.equals(other.login))
      return false;
    return true;
  }
  
}