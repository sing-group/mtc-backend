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
package org.sing_group.mtc.domain.entities.session;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.mtc.domain.entities.i18n.I18N;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;
import org.sing_group.mtc.domain.entities.i18n.LocalizedMessage;
import org.sing_group.mtc.domain.entities.user.Therapist;

@Entity
@Table(name = "session")
@Access(AccessType.FIELD)
public class GamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final String NAME_SUFFIX = ".name";
  private static final String DESCRIPTION_SUFFIX = ".description";

  @Transient
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "therapist",
    referencedColumnName = "login",
    foreignKey = @ForeignKey(name = "FK_session_therapist")
  )
  private Therapist therapist;

  @OneToMany(
    mappedBy = "session",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @OrderBy("gameOrder ASC")
  private SortedSet<GameConfigurationForSession> gameConfigurations;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "session_i18n", joinColumns = {
      @JoinColumn(name = "session", referencedColumnName = "id", insertable = false, updatable = false)
    },
    inverseJoinColumns = {
      @JoinColumn(name = "i18nLocale", referencedColumnName = "locale"),
      @JoinColumn(name = "i18nKey", referencedColumnName = "messageKey")
    },
    foreignKey = @ForeignKey(name = "FK_gamesession_i18n")
  )
  private Set<I18N> messages;

  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedGamesSession> assigned;

  public GamesSession() {
    this.id = null;
    this.therapist = null;
    
    this.gameConfigurations = new TreeSet<>();
    this.assigned = new HashSet<>();
    this.messages = new HashSet<>();
  }

  public GamesSession(
    int id,
    Therapist therapist,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages
  ) {
    this.id = id;
    
    this.gameConfigurations = new TreeSet<>();
    this.assigned = new HashSet<>();
    this.messages = new HashSet<>();

    this.setTherapist(therapist);
    
    this.setNameMessages(nameMessages);
    this.setDescriptionMessages(descriptionMessages);
  }

  public GamesSession(
    int id,
    Therapist therapist,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages,
    Set<GameConfigurationForSession> gameConfigs,
    Set<AssignedGamesSession> assignations
  ) {
    this(id, therapist, nameMessages, descriptionMessages);

    gameConfigs.forEach(this::addGameConfiguration);
    assignations.forEach(this::addAssigned);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(AccessType.PROPERTY)
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
    this.id = id;
    
    this.updateMessagesKey();
  }

  public Therapist getTherapist() {
    return this.therapist;
  }

  public void setTherapist(Therapist therapist) {
    if (this.therapist != null) {
      this.therapist.removeSession(this);
      this.therapist = null;
    }

    if (therapist != null) {
      this.therapist = therapist;
      this.therapist.addSession(this);
    }
  }

  private String getKeyPrefix() {
    return "session." + this.id;
  }

  public String getNameKey() {
    return this.getKeyPrefix() + NAME_SUFFIX;
  }

  public String getDescriptionKey() {
    return this.getKeyPrefix() + DESCRIPTION_SUFFIX;
  }

  public LocalizedMessage getName() {
    return LocalizedMessage.from(this.getNameKey(), this.messages);
  }
  
  public void setNameMessage(I18NLocale locale, String newName) {
    this.setMessage(locale, this.getNameKey(), newName);
  }
  
  public void setNameMessages(Map<I18NLocale, String> messages) {
    this.setMessages(this.getNameKey(), messages);
  }

  public LocalizedMessage getDescription() {
    return LocalizedMessage.from(this.getDescriptionKey(), this.messages);
  }
  
  public void setDescriptionMessage(I18NLocale locale, String newDescription) {
    this.setMessage(locale, this.getDescriptionKey(), newDescription);
  }
  
  public void setDescriptionMessages(Map<I18NLocale, String> messages) {
    this.setMessages(this.getDescriptionKey(), messages);
  }

  public Stream<GameConfigurationForSession> getGameConfigurations() {
    return this.gameConfigurations.stream();
  }

  public boolean hasGameConfiguration(GameConfigurationForSession gameConfiguration) {
    return this.gameConfigurations.contains(gameConfiguration);
  }

  public boolean addGameConfiguration(GameConfigurationForSession config) {
    requireNonNull(config, "config can't be null");

    if (this.hasGameConfiguration(config)) {
      return false;
    } else {
      config.setSession(this);
      return true;
    }
  }

  public boolean removeGameConfiguration(GameConfigurationForSession config) {
    requireNonNull(config, "config can't be null");

    if (this.hasGameConfiguration(config)) {
      config.setSession(null);
      return true;
    } else {
      return false;
    }
  }

  protected boolean directAddGameConfiguration(GameConfigurationForSession config) {
    return this.gameConfigurations.add(config);
  }

  protected boolean directRemoveGameConfiguration(GameConfigurationForSession config) {
    return this.gameConfigurations.remove(config);
  }

  public Stream<AssignedGamesSession> getAssigned() {
    return this.assigned.stream();
  }

  public boolean hasAssigned(AssignedGamesSession assigned) {
    return this.assigned.contains(assigned);
  }

  public void addAssigned(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");

    assigned.setSession(this);
  }

  public void removeAssigned(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");

    assigned.setSession(null);
  }

  protected boolean directAddAssigned(AssignedGamesSession assigned) {
    return this.assigned.add(assigned);
  }

  protected boolean directRemoveAssigned(AssignedGamesSession assigned) {
    return this.assigned.remove(assigned);
  }

  protected void updateMessagesKey() {
    final Set<I18N> messages = new HashSet<>(this.messages);
    
    this.messages.clear();
    
    final Map<String, String> keySuppliers = new HashMap<>();
    keySuppliers.put(NAME_SUFFIX, this.getNameKey());
    keySuppliers.put(DESCRIPTION_SUFFIX, this.getDescriptionKey());
    
    for (Map.Entry<String, String> keySupplier : keySuppliers.entrySet()) {
      final String suffix = keySupplier.getKey();
      final String key = keySupplier.getValue();
      
      messages.stream()
        .filter(message -> message.getKey().endsWith(suffix))
        .peek(message -> message.setKey(key))
      .forEach(this.messages::add);
    }
  }
  
  protected void updateConfigurations() {
    final List<GameConfigurationForSession> configs = this.getGameConfigurations().collect(toList());
    
    configs.forEach(config -> config.setSession(this));
  }
  
  protected void setMessage(I18NLocale locale, String key, String newMessage) {
    final I18N newI18n = new I18N(locale, key, newMessage);
    
    final Optional<I18N> toRemove = this.messages.stream()
      .filter(message -> message.getKey().equals(key))
      .filter(message -> message.getLocale().equals(locale.getValue()))
    .findAny();
    
    toRemove.ifPresent(this.messages::remove);
    
    this.messages.add(newI18n);
  }
  
  protected void setMessages(String key, Map<I18NLocale, String> messages) {
    requireNonNull(key);
    requireNonEmpty(messages);
    
    final Set<I18N> toRemove = this.messages.stream()
      .filter(i18n -> i18n.getKey().equals(key))
    .collect(toSet());
    
    this.messages.removeAll(toRemove);
    
    I18N.from(key, messages).forEach(this.messages::add);
  }
}
