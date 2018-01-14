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
package org.sing_group.mtc.domain.entities.game.session;

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
@Table(name = "games_session")
@Access(AccessType.FIELD)
public class GamesSession implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final String NAME_SUFFIX = ".name";
  private static final String DESCRIPTION_SUFFIX = ".description";

  @Transient
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "therapist",
    referencedColumnName = "login",
    foreignKey = @ForeignKey(name = "FK_session_therapist")
  )
  private Therapist therapist;

  @OneToMany(
    mappedBy = "gamesSession",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @OrderBy("gameOrder ASC")
  private SortedSet<GameInGamesSession> gameConfigurations;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "games_session_i18n",
    joinColumns = {
      @JoinColumn(name = "gamesSession", referencedColumnName = "id")
    },
    inverseJoinColumns = {
      @JoinColumn(name = "i18nLocale", referencedColumnName = "locale"),
      @JoinColumn(name = "i18nKey", referencedColumnName = "messageKey")
    },
    foreignKey = @ForeignKey(name = "FK_gamesession_i18n")
  )
  private Set<I18N> messages;

  @OneToMany(mappedBy = "gamesSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AssignedGamesSession> assignedGamesSessions;

  public GamesSession() {
    this.id = null;
    this.therapist = null;
    
    this.gameConfigurations = new TreeSet<>();
    this.assignedGamesSessions = new HashSet<>();
    this.messages = new HashSet<>();
  }

  public GamesSession(
    long id,
    Therapist therapist,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages
  ) {
    this.id = id;
    
    this.gameConfigurations = new TreeSet<>();
    this.assignedGamesSessions = new HashSet<>();
    this.messages = new HashSet<>();

    this.setTherapist(therapist);
    
    this.setNameMessages(nameMessages);
    this.setDescriptionMessages(descriptionMessages);
  }

  public GamesSession(
    long id,
    Therapist therapist,
    Map<I18NLocale, String> nameMessages,
    Map<I18NLocale, String> descriptionMessages,
    Set<GameInGamesSession> gameConfigs,
    Set<AssignedGamesSession> assignations
  ) {
    this(id, therapist, nameMessages, descriptionMessages);

    gameConfigs.forEach(this::addGameConfiguration);
    assignations.forEach(this::addAssignedGamesSession);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Access(AccessType.PROPERTY)
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
    
    this.updateMessagesKey();
  }

  public Optional<Therapist> getTherapist() {
    return Optional.ofNullable(this.therapist);
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
  
  public String getTherapistLogin() {
    return this.getTherapist()
      .map(Therapist::getLogin)
    .orElseThrow(() -> new IllegalStateException("No therapist assigned"));
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

  public Stream<GameInGamesSession> getGameConfigurations() {
    return this.gameConfigurations.stream();
  }
  
  public GameInGamesSession getGameConfiguration(int index) {
    final GameInGamesSession[] games = this.gameConfigurations.stream().toArray(GameInGamesSession[]::new);
    
    return games[index];
  }

  public int getGameConfigurationIndex(GameInGamesSession gameConfiguration) {
    if (!this.hasGameConfiguration(gameConfiguration)) {
      throw new IllegalArgumentException("gameConfiguration does not belong to this games session");
    }

    final GameInGamesSession[] games = this.gameConfigurations.stream().toArray(GameInGamesSession[]::new);
    for (int i = 0; i < games.length; i++) {
      if (games[i].equals(gameConfiguration)) {
        return i;
      }
    }
    
    throw new IllegalStateException("gameConfiguration was unexpectedly not found");
  }
  
  public void setGameConfigurations(Stream<GameInGamesSession> gameConfigurations) {
    this.gameConfigurations.stream().collect(toSet())
      .forEach(this::removeGameConfiguration);
    
    gameConfigurations.collect(toSet())
      .forEach(this::addGameConfiguration);
  }

  public boolean hasGameConfiguration(GameInGamesSession gameConfiguration) {
    return this.gameConfigurations.contains(gameConfiguration);
  }
  
  public boolean addGameConfiguration(GameInGamesSession config) {
    requireNonNull(config, "config can't be null");

    if (this.hasGameConfiguration(config)) {
      return false;
    } else {
      config.setGamesSession(this);
      return true;
    }
  }

  public boolean removeGameConfiguration(GameInGamesSession config) {
    requireNonNull(config, "config can't be null");

    if (this.hasGameConfiguration(config)) {
      config.setGamesSession(null);
      return true;
    } else {
      return false;
    }
  }

  protected boolean directAddGameConfiguration(GameInGamesSession config) {
    final boolean isNew = !this.gameConfigurations.remove(config);
    
    this.gameConfigurations.add(config);
    
    return isNew;
  }

  protected boolean directRemoveGameConfiguration(GameInGamesSession config) {
    return this.gameConfigurations.remove(config);
  }

  public Stream<AssignedGamesSession> getAssignedGamesSessions() {
    return this.assignedGamesSessions.stream();
  }

  public boolean hasAssigned(AssignedGamesSession assigned) {
    return this.assignedGamesSessions.contains(assigned);
  }

  public void addAssignedGamesSession(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");

    assigned.setGamesSession(this);
  }

  public void removeAssignedGamesSession(AssignedGamesSession assigned) {
    requireNonNull(assigned, "assigned can't be null");

    assigned.setGamesSession(null);
  }

  protected boolean directAddAssignedGamesSession(AssignedGamesSession assigned) {
    final boolean isNew = !this.assignedGamesSessions.remove(assigned);
    
    this.assignedGamesSessions.add(assigned);
    
    return isNew;
  }

  protected boolean directRemoveAssignedGamesSession(AssignedGamesSession assigned) {
    return this.assignedGamesSessions.remove(assigned);
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
    final List<GameInGamesSession> configs = this.getGameConfigurations().collect(toList());
    
    configs.forEach(config -> config.setGamesSession(this));
  }
  
  protected void setMessage(I18NLocale locale, String key, String newMessage) {
    final Optional<I18N> message = this.messages.stream()
      .filter(m -> m.getKey().equals(key) && m.getLocale() == locale)
    .findAny();
    
    if (message.isPresent()) {
      message.get().setValue(newMessage);
    } else {
      messages.add(new I18N(locale, key, newMessage));
    }
  }
  
  protected void setMessages(String key, Map<I18NLocale, String> messages) {
    requireNonNull(key);
    requireNonEmpty(messages);
    
    messages.entrySet().stream()
      .forEach(entry -> this.setMessage(entry.getKey(), key, entry.getValue()));
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
    GamesSession other = (GamesSession) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  
}
