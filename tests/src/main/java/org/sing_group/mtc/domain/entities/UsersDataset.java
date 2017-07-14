/*
 * #%L
 * Tests
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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UsersDataset {
  public final static String ADMIN_HTTP_BASIC_AUTH = "Basic YWRtaW5AZW1haWwuY29tOmFkbWlucGFzcw==";
  
  private final static Map<String, String> EMAIL_TO_PASSWORDS = new HashMap<>();
  
  static {
    EMAIL_TO_PASSWORDS.put("admin@email.com", "adminpass");
    EMAIL_TO_PASSWORDS.put("patient1@email.com", "patient1pass");
    EMAIL_TO_PASSWORDS.put("patient2@email.com", "patient2pass");
    EMAIL_TO_PASSWORDS.put("patient3@email.com", "patient3pass");
    EMAIL_TO_PASSWORDS.put("patient4@email.com", "patient4pass");
    EMAIL_TO_PASSWORDS.put("patientNew@email.com", "patientNewpass");
    EMAIL_TO_PASSWORDS.put("patientChanged@email.com", "patientChangedpass");
  }
  
  public static Administrator admin() {
    return new Administrator(1L, "admin@email.com", EMAIL_TO_PASSWORDS.get("admin@email.com"), "Admin", "Administrator");
  }
  
  public static Stream<User> users() {
    return Stream.of(
      admin(),
      
      new Patient(2L, "patient1@email.com", EMAIL_TO_PASSWORDS.get("patient1@email.com"), "Patient1", "One"),
      patientToDelete(),
      new Patient(4L, "patient3@email.com", EMAIL_TO_PASSWORDS.get("patient3@email.com"), "Patient3", "Three"),
      new Patient(5L, "patient4@email.com", EMAIL_TO_PASSWORDS.get("patient4@email.com"), "Patient4", "Four")
    );
  }
  
  public static Patient newPatient() {
    return new Patient("patientNew@email.com", EMAIL_TO_PASSWORDS.get("patientNew@email.com"), "Patient", "New");
  }
  
  public static User modifiedUser() {
    return new Patient(2L, "patientChanged@email.com", EMAIL_TO_PASSWORDS.get("patientChanged@email.com"), "Changed", "User");
  }
  
  public static User patientToDelete() {
    return new Patient(3L, "patient2@email.com", EMAIL_TO_PASSWORDS.get("patient2@email.com"), "Patient2", "Two");
  }
  
  public static String getAdminHttpBasicAuthenticationToken() {
    final Administrator admin = admin();
    final String adminToken = admin.getEmail() + ":" + admin.getPassword();
    
    return Base64.getEncoder().encodeToString(adminToken.getBytes());
  }
  
  public static Stream<String> validEmails() {
    return users().map(User::getEmail);
  }
  
  public static Stream<String> invalidEmails() {
    return Stream.of(
      "invalid1@email.com",
      "fake@email.com",
      "notvalid@email.com"
    );
  }
  
  public static User userWithEmail(String email) {
    return users()
      .filter(user -> user.getEmail().equals(email))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Invalid email: " + email));
  }
  
  private static boolean isInvalidEmail(String email) {
    return invalidEmails().anyMatch(email::equals);
  }
  
  public static String passwordOfUser(User user) {
    return passwordOfUser(user.getEmail());
  }
  
  public static String passwordOfUser(String email) {
    if (EMAIL_TO_PASSWORDS.containsKey(email)) {
      return EMAIL_TO_PASSWORDS.get(email);
    } else if (isInvalidEmail(email)) {
      return email + "pass";
    } else {
      throw new IllegalArgumentException("Invalid email: " + email);
    }
  }
}
