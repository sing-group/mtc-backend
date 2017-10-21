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
package org.sing_group.mtc.domain.entities.game.session;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.sing_group.mtc.domain.entities.UsersDataset.patient;
import static org.sing_group.mtc.domain.entities.UsersDataset.therapist;
import static org.sing_group.mtc.domain.entities.game.GameTaskType.RECOGNITION;
import static org.sing_group.mtc.domain.entities.game.GameTaskType.VERBAL_FLUENCY;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.mtc.domain.entities.game.Game;
import org.sing_group.mtc.domain.entities.game.parameter.IntegerParameter;
import org.sing_group.mtc.domain.entities.game.parameter.SecondsParameter;
import org.sing_group.mtc.domain.entities.i18n.I18NLocale;

public final class GamesSessionDataset {
  private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private final static Date parseDate(String date) {
    try {
      return DATE_FORMAT.parse(date);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  private GamesSessionDataset() {}

  public static Game recognitionGame() {
    final IntegerParameter maxRepetitions = new IntegerParameter("maxRepetitions", null, 1, 1, 2);
    final IntegerParameter numOfStimuli = new IntegerParameter("numOfStimuli", null, 6, 1, 12);
    final SecondsParameter diceShowTime = new SecondsParameter("diceShowTime", null, 5);

    return new Game(
      "recognition", singleton(RECOGNITION), asList(
        maxRepetitions, numOfStimuli, diceShowTime
      )
    );
  }

  public static Game verbalFluencyGame() {
    return new Game("verbalFluency", singleton(VERBAL_FLUENCY), emptySet());
  }

  public static Stream<GamesSession> sessions() {
    return Stream.of(session1());
  }

  public static GamesSession session1() {
    final Map<String, String> paramValues = new HashMap<>();
    paramValues.put("maxRepetitions", "2");
    paramValues.put("numOfStimuli", "6");
    paramValues.put("diceShowTime", "3");

    final Map<I18NLocale, String> nameMessages = new HashMap<>();
    final Map<I18NLocale, String> descriptionMessages = new HashMap<>();

    nameMessages.put(I18NLocale.EN_US, "Recognition");
    nameMessages.put(I18NLocale.ES_ES, "Reconocimiento");
    nameMessages.put(I18NLocale.GL_ES, "Recoñecemento");

    descriptionMessages.put(I18NLocale.EN_US, "Recognition game.");
    descriptionMessages.put(I18NLocale.ES_ES, "Juego de reconocimiento.");
    descriptionMessages.put(I18NLocale.GL_ES, "Xogo de recoñecemento.");

    final GamesSession session = new GamesSession(1, therapist(), nameMessages, descriptionMessages);

    new GameConfigurationForSession(session, recognitionGame(), 1, paramValues);
    new GameConfigurationForSession(session, verbalFluencyGame(), 2, emptyMap());

    return session;
  }

  public static GamesSession newGamesSession() {
    final Map<String, String> paramValues1 = new HashMap<>();
    paramValues1.put("maxRepetitions", "2");
    paramValues1.put("numOfStimuli", "6");
    paramValues1.put("diceShowTime", "3");

    final Map<String, String> paramValues2 = new HashMap<>();
    paramValues2.put("maxRepetitions", "1");
    paramValues2.put("numOfStimuli", "8");
    paramValues2.put("diceShowTime", "5");

    final Map<I18NLocale, String> nameMessages = new HashMap<>();
    final Map<I18NLocale, String> descriptionMessages = new HashMap<>();

    nameMessages.put(I18NLocale.EN_US, "Recognition 2");
    nameMessages.put(I18NLocale.ES_ES, "Reconocimiento 2");
    nameMessages.put(I18NLocale.GL_ES, "Recoñecemento 2");

    descriptionMessages.put(I18NLocale.EN_US, "Recognition game.");
    descriptionMessages.put(I18NLocale.ES_ES, "Juego de reconocimiento.");
    descriptionMessages.put(I18NLocale.GL_ES, "Xogo de recoñecemento.");

    final GamesSession session = new GamesSession(2, therapist(), nameMessages, descriptionMessages);

    new GameConfigurationForSession(session, recognitionGame(), 1, paramValues1);
    new GameConfigurationForSession(session, verbalFluencyGame(), 2, emptyMap());
    new GameConfigurationForSession(session, recognitionGame(), 3, paramValues2);

    return session;
  }
  
  public static AssignedGamesSession assignedGamesSession() {
    return assignedGamesSessions()[0];
  }
  
  public static AssignedGamesSession[] assignedGamesSessions() {
    return new AssignedGamesSession[] {
      new AssignedGamesSession(
        1,
        parseDate("2017-10-13 17:50:10"),
        parseDate("2017-11-01 00:00:00"),
        parseDate("2017-11-10 23:59:59"),
        session1(),
        patient(),
        emptySet()
      )
    };
  }
  
  public static AssignedGamesSession newAssignedGamesSession() {
    return new AssignedGamesSession(
      parseDate("3017-11-01 00:00:00"),
      parseDate("3017-11-10 23:59:59"),
      session1(),
      patient()
    );
  }
}
