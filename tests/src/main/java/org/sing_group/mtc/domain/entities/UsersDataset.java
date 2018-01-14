/*-
 * #%L
 * Tests
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

package org.sing_group.mtc.domain.entities;

import static java.util.Arrays.asList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.mtc.domain.dao.SortDirection;
import org.sing_group.mtc.domain.entities.user.Administrator;
import org.sing_group.mtc.domain.entities.user.Institution;
import org.sing_group.mtc.domain.entities.user.Manager;
import org.sing_group.mtc.domain.entities.user.Patient;
import org.sing_group.mtc.domain.entities.user.RoleType;
import org.sing_group.mtc.domain.entities.user.Therapist;
import org.sing_group.mtc.domain.entities.user.User;

public class UsersDataset {
  public final static String ADMIN_HTTP_BASIC_AUTH = "Basic YWRtaW46YWRtaW5wYXNz";
  public final static String MANAGER_HTTP_BASIC_AUTH = "Basic bWFuYWdlcjptYW5hZ2VycGFzcw==";
  public final static String THERAPIST_HTTP_BASIC_AUTH = "Basic dGhlcmFwaXN0OnRoZXJhcGlzdHBhc3M=";
  public final static String PATIENT_HTTP_BASIC_AUTH = "Basic cGF0aWVudDE6cGF0aWVudDFwYXNz";
  
  private final static Map<String, String> LOGIN_TO_PASSWORD = new HashMap<>();
  private final static Map<String, String> LOGIN_TO_NEW_PASSWORD = new HashMap<>();
  
  static {
    LOGIN_TO_PASSWORD.put("admin", "adminpass");
    LOGIN_TO_PASSWORD.put("admin2", "admin2pass");
    LOGIN_TO_PASSWORD.put("admin3", "admin3pass");
    LOGIN_TO_PASSWORD.put("admin4", "admin4pass");
    LOGIN_TO_PASSWORD.put("therapist", "therapistpass");
    LOGIN_TO_PASSWORD.put("therapist2", "therapist2pass");
    LOGIN_TO_PASSWORD.put("therapist3", "therapist3pass");
    LOGIN_TO_PASSWORD.put("therapist4", "therapist4pass");
    LOGIN_TO_PASSWORD.put("manager", "managerpass");
    LOGIN_TO_PASSWORD.put("manager2", "manager2pass");
    LOGIN_TO_PASSWORD.put("manager3", "manager3pass");
    LOGIN_TO_PASSWORD.put("manager4", "manager4pass");
    LOGIN_TO_PASSWORD.put("patient1", "patient1pass");
    LOGIN_TO_PASSWORD.put("patient2", "patient2pass");
    LOGIN_TO_PASSWORD.put("patient3", "patient3pass");
    LOGIN_TO_PASSWORD.put("patient4", "patient4pass");
    LOGIN_TO_PASSWORD.put("adminNew", "adminNewpass");
    LOGIN_TO_PASSWORD.put("managerNew", "managerNewpass");
    LOGIN_TO_PASSWORD.put("therapistNew", "therapistNewpass");
    LOGIN_TO_PASSWORD.put("patientNew", "patientNewpass");
    
    LOGIN_TO_NEW_PASSWORD.put("admin", "adminModifiedpass");
    LOGIN_TO_NEW_PASSWORD.put("manager", "managerModifiedpass");
    LOGIN_TO_NEW_PASSWORD.put("therapist", "therapistModifiedpass");
    LOGIN_TO_NEW_PASSWORD.put("patient1", "patient1Modifiedpass");
  }
  
  public static Institution institution() {
    return institution(1);
  }
  
  public static Administrator admin() {
    return user("admin");
  }
  
  public static Manager manager() {
    return user("manager");
  }

  public static Therapist therapist() {
    return user("therapist");
  }

  public static Patient patient() {
    return user("patient1");
  }
  
  public static Stream<User> users() {
    final List<Institution> institutions = asList(
      new Institution(1L, "Institution 1", null),
      new Institution(2L, "Institution 2", null),
      new Institution(3L, "Institution 3", null),
      new Institution(4L, "Institution 4", null)
    );
    
    final Manager manager = new Manager("manager", "manager@email.com", passwordOf("manager"), "Man", "Manager", institutions.subList(0, 2));
    
    final Therapist therapist = new Therapist("therapist", "therapist@email.com", passwordOf("therapist"), institutions.get(0), "Thera", "Therapist", null, null);
    
    return Stream.of(
      new Administrator("admin", "admin@email.com", passwordOf("admin"), "Admin", "Administrator"),
      new Administrator("admin2", "admin2@email.com", passwordOf("admin2"), "Admin2", "Administrator2"),
      new Administrator("admin3", "admin3@email.com", passwordOf("admin3"), "Admin3", "Administrator3"),
      new Administrator("admin4", "admin4@email.com", passwordOf("admin4"), "Admin4", "Administrator4"),
      manager,
      new Manager("manager2", "manager2@email.com", passwordOf("manager2"), "Man2", "Manager2", null),
      new Manager("manager3", "manager3@email.com", passwordOf("manager3"), "Man3", "Manager3", institutions.subList(2, 3)),
      new Manager("manager4", "manager4@email.com", passwordOf("manager4"), "Man4", "Manager4", institutions.subList(3, 4)),
      therapist,
      new Therapist("therapist2", "therapist2@email.com", passwordOf("therapist2"), institutions.get(1), "Thera2", "Therapist2", null, null),
      new Therapist("therapist3", "therapist3@email.com", passwordOf("therapist3"), institutions.get(1), "Thera3", "Therapist3", null, null),
      new Therapist("therapist4", "therapist4@email.com", passwordOf("therapist4"), institutions.get(1), "Thera4", "Therapist4", null, null),
      new Patient("patient1", passwordOf("patient1"), therapist),
      new Patient("patient2", passwordOf("patient2"), therapist),
      new Patient("patient3", passwordOf("patient3"), therapist),
      new Patient("patient4", passwordOf("patient4"), therapist)
    );
  }
  
  @SuppressWarnings("unchecked")
  private static <U extends User> Stream<U> users(RoleType role) {
    return (Stream<U>) users().filter(user -> User.getRole(user).equals(role));
  }
  
  @SuppressWarnings("unchecked")
  public static <U extends User> U user(String login) {
    return (U) users()
      .filter(user -> user.getLogin().equals(login))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Invalid login: " + login));
  }

  public static String passwordOf(User user) {
    return passwordOf(user.getLogin());
  }

  public static String passwordOf(String login) {
    if (LOGIN_TO_PASSWORD.containsKey(login)) {
      return LOGIN_TO_PASSWORD.get(login);
    } else if (isInvalidLogin(login)) {
      return login + "pass";
    } else {
      throw new IllegalArgumentException("Invalid email: " + login);
    }
  }

  public static String newPasswordOf(User user) {
    return newPasswordOf(user.getLogin());
  }
  
  public static String newPasswordOf(String login) {
    if (LOGIN_TO_NEW_PASSWORD.containsKey(login)) {
      return LOGIN_TO_NEW_PASSWORD.get(login);
    } else {
      throw new IllegalArgumentException("Invalid user for new password: " + login);
    }
  }

  public static Stream<String> validLogins() {
    return users().map(User::getLogin);
  }

  public static Stream<String> invalidLogins() {
    return Stream.of("invalid1", "fake", "notvalid");
  }

  private static boolean isInvalidLogin(String login) {
    return invalidLogins().anyMatch(login::equals);
  }
  
  public static Stream<Administrator> admins() {
    return users(RoleType.ADMIN);
  }

  public static <T extends Comparable<T>> Stream<Administrator> admins(
    int start, int end, Function<Administrator, T> getter, SortDirection sort
  ) {
    return filterUsers(admins(), start, end, getter, sort);
  }

  public static Administrator newAdministrator() {
    return new Administrator("adminNew", "adminNew@email.com", passwordOf("adminNew"), "AdminNew", "AdministratorNew");
  }

  public static Administrator modifiedAdministrator() {
    return new Administrator("admin", "adminModified@email.com", newPasswordOf("admin"), "AdminModified", "Administrator");
  }

  public static Administrator administratorToDelete() {
    return user("admin2");
  }

  public static Stream<Manager> managers() {
    return users(RoleType.MANAGER);
  }

  public static <T extends Comparable<T>> Stream<Manager> managers(
    int start, int end, Function<Manager, T> getter, SortDirection sort
  ) {
    return filterUsers(managers(), start, end, getter, sort);
  }

  public static Manager newManager() {
    return new Manager("managerNew", "managerNew@email.com", passwordOf("managerNew"), "ManagerNew", "ManagerNew", null);
  }

  public static Manager modifiedManager() {
    return new Manager("manager", "managerModified@email.com", newPasswordOf("manager"), "ManModified", "Manager", null);
  }

  public static Manager modifiedManagerWithNewInstitution() {
    return new Manager("manager", "managerModified@email.com", newPasswordOf("manager"), "ManModified", "Manager", null);
  }

  public static Manager managerToDelete() {
    return user("manager");
  }

  public static Stream<Therapist> therapists() {
    return users(RoleType.THERAPIST);
  }

  public static <T extends Comparable<T>> Stream<Therapist> therapists(
    int start, int end, Function<Therapist, T> getter, SortDirection sort
  ) {
    return therapists(therapists(), start, end, getter, sort);
  }

  public static <T extends Comparable<T>> Stream<Therapist> therapists(
    Stream<Therapist> therapists, int start, int end, Function<Therapist, T> getter, SortDirection sort
  ) {
    return filterUsers(therapists, start, end, getter, sort);
  }

  public static Therapist newTherapist() {
    return new Therapist("therapistNew", "therapistNew@email.com", passwordOf("therapistNew"), institution(1), "TheraNew", "TherapistNew", null, null);
  }

  public static Therapist modifiedTherapist() {
    return new Therapist("therapist", "therapistModified@email.com", newPasswordOf("therapist"), institution(1), "TheraModified", "Therapist", null, null);
  }

  public static Therapist modifiedTherapistWithNewInstitution() {
    return new Therapist("therapist", "therapistModified@email.com", newPasswordOf("therapist"), institution(3), "TheraModified", "Therapist", null, null);
  }

  public static Therapist therapistToDelete() {
    return user("therapist");
  }

  public static Stream<Patient> patients() {
    return users(RoleType.PATIENT);
  }

  public static Stream<Patient> patientsOfTherapist(String therapistLogin) {
    return UsersDataset.<Patient>users(RoleType.PATIENT)
      .filter(patient -> patient.getTherapist().getLogin().equals(therapistLogin));
  }

  public static <T extends Comparable<T>> Stream<Patient> patients(
    int start, int end, Function<Patient, T> getter, SortDirection sort
  ) {
    return filterUsers(patients(), start, end, getter, sort);
  }

  public static <T extends Comparable<T>> Stream<Patient> patientsOfTherapist(
    String therapist, int start, int end, Function<Patient, T> getter, SortDirection sort
  ) {
    return patients(start, end, getter, sort)
      .filter(patient -> patient.getTherapist().getLogin().equals(therapist));
  }

  private static <U extends User, T extends Comparable<T>> Stream<U> filterUsers(
    Stream<U> users, int start, int end, Function<U, T> getter, SortDirection sort
  ) {
    final Comparator<T> compare = sort == SortDirection.ASC
      ? (c1, c2) -> c1.compareTo(c2)
      : (c1, c2) -> -c1.compareTo(c2);
    
    return users
      .sorted((c1, c2) -> compare.compare(getter.apply(c1), getter.apply(c2)))
      .skip(start)
      .limit(end - start + 1);
  }
  
  public static Patient newPatient() {
    return new Patient("patientNew", passwordOf("patientNew"), therapist());
  }
  
  public static Patient modifiedPatient() {
    return new Patient("patient1", newPasswordOf("patient1"), therapist());
  }
  
  public static Patient patientToDelete() {
    return user("patient2");
  }
  
  public static Institution newInstitution() {
    return new Institution("New Institution", user("manager4"));
  }
  
  public static int newInstitutionId() {
    return (int) countInstitutions() + 1;
  }
  
  public static int institutionToModify() {
    return 1;
  }
  
  public static Institution modifiedInstitution() {
    return new Institution("Modified Institution", user("manager"), "Modified Description", "Modified Address", asList(user("therapist")));
  }
  
  public static Institution modifiedInstitutionWithNewManager() {
    return new Institution("Modified Institution", user("manager2"), "Modified Description", "Modified Address", asList(user("therapist")));
  }
  
  public static Institution institutionToDelete() {
    return institution(1);
  }
  
  public static <T extends Comparable<T>> Stream<Institution> institutions() {
    return managers()
      .flatMap(Manager::getInstitutions);
  }
  
  public static <T extends Comparable<T>> Stream<Institution> institutions(
    int start, int end, Function<Institution, T> getter, SortDirection sort
  ) {
    return institutions(institutions(), start, end, getter, sort);
  }
  
  public static <T extends Comparable<T>> Stream<Institution> institutions(
    Stream<Institution> institutions, int start, int end, Function<Institution, T> getter, SortDirection sort
  ) {
    final Comparator<T> compare = sort == SortDirection.ASC
      ? (c1, c2) -> c1.compareTo(c2)
      : (c1, c2) -> -c1.compareTo(c2);
      
    return institutions
      .sorted((c1, c2) -> compare.compare(getter.apply(c1), getter.apply(c2)))
      .skip(start)
      .limit(end - start + 1);
  }
  
  public static Institution institution(long id) {
    return institutions()
      .filter(institution -> institution.getId().equals(id))
      .findFirst()
    .orElseThrow(() -> new IllegalArgumentException("Unknown institution: " + id));
  }

  public static long countAdmins() {
    return admins().count();
  }

  public static long countManagers() {
    return managers().count();
  }
  
  public static long countTherapists() {
    return therapists().count();
  }
  
  public static long countPatients() {
    return patients().count();
  }
  
  public static long countPatientsOfTherapist(String therapistLogin) {
    return patients()
      .filter(patient -> patient.getTherapist().getLogin().equals(therapistLogin))
    .count();
  }
  
  public static long countInstitutions() {
    return institutions().count();
  }
  
}
