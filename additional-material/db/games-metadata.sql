-- MTC Game v0.1.0

INSERT INTO `game` (`id`)
VALUES ('recognition'),
       ('verbalFluency');

INSERT INTO `game_type` (`gameId`, `name`)
VALUES ('recognition', 'RECOGNITION'),
       ('verbalFluency', 'VERBAL_FLUENCY');

INSERT INTO `integer_parameter` (`gameId`, `id`, `defaultValue`, `max`, `min`)
VALUES ('recognition', 'numOfStimuli', '6', '1', '12'),
       ('recognition', 'maxRepetitions', '1', '1', '5');

INSERT INTO `seconds_parameter` (`gameId`, `id`, `defaultValue`)
VALUES ('recognition', 'diceShowTime', '5');

INSERT INTO `user` (role, password, email, name, surname, therapistId) VALUES
  ('ADMIN','25e4ee4e9229397b6b17776bfceaf8e7', 'admin@email.com', 'Admin', 'Nimda', null),
  ('PATIENT','03645d1e2e298b6d172c5126bded3f60', 'patient@email.com', 'Patient', 'Tneitap', null),
  ('THERAPIST','f15a0a52502e22f1e9d85df282aacbca', 'therapist@email.com', 'Therapist', 'Tsipareht', null);
    
SET @therapistId = (SELECT id FROM `user` WHERE email = 'therapist@email.com');
UPDATE user SET therapistId = @therapistId WHERE role = 'PATIENT';

INSERT INTO `session` (`id`, `therapistId`)
VALUES (1,3);

INSERT INTO `session_version` (`sessionId`, `version`)
VALUES (1,1);

INSERT INTO `session_game` (`gameId`, `gameOrder`, `sessionId`, `sessionVersion`)
VALUES ('recognition',1,1,1);

INSERT INTO `session_game_param_value` (`gameId`, `gameOrder`, `sessionId`, `sessionVersion`, `value`, `param`)
VALUES ('recognition',1,1,1,'3','diceShowTime'),
       ('recognition',1,1,1,'2','maxRepetitions'),
       ('recognition',1,1,1,'6','numOfStimuli');
       
INSERT INTO `integer_parameter` (`gameId`, `id`, `defaultValue`, `max`, `min`)
VALUES ('recognition','maxRepetitions',1,1,5),
       ('recognition','numOfStimuli',6,1,12);

INSERT INTO `seconds_parameter` (`gameId`, `id`, `defaultValue`)
VALUES ('recognition','diceShowTime',5);

INSERT INTO `i18n` (`messageKey`, `locale`, `value`)
VALUES ('session.1.version.1.description','en_US','Recognition game.'),
       ('session.1.version.1.description','es_ES','Juego de reconocimiento.'),
       ('session.1.version.1.description','gl_ES','Xogo de recoñecemento.'),
       ('session.1.version.1.name','en_US','Recognition'),
       ('session.1.version.1.name','es_ES','Reconocimiento'),
       ('session.1.version.1.name','gl_ES','Recoñecemento');

INSERT INTO `session_i18n` (`sessionId`, `sessionVersion`, `i18nKey`, `i18nLocale`)
VALUES (1,1,'session.1.version.1.description','en_US'),
       (1,1,'session.1.version.1.description','es_ES'),
       (1,1,'session.1.version.1.description','gl_ES'),
       (1,1,'session.1.version.1.name','en_US'),
       (1,1,'session.1.version.1.name','es_ES'),
       (1,1,'session.1.version.1.name','gl_ES');