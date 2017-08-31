-- MTC Game v0.1.0

INSERT INTO `game` (`id`)
VALUES ('recognition'),
       ('verbalFluency');

INSERT INTO `game_type` (`game`, `name`)
VALUES ('recognition', 'RECOGNITION'),
       ('verbalFluency', 'VERBAL_FLUENCY');

INSERT INTO `user` (role, password, email, name, surname, therapistId) VALUES
  ('ADMIN','25e4ee4e9229397b6b17776bfceaf8e7', 'admin@email.com', 'Admin', 'Nimda', null),
  ('PATIENT','03645d1e2e298b6d172c5126bded3f60', 'patient@email.com', 'Patient', 'Tneitap', null),
  ('THERAPIST','f15a0a52502e22f1e9d85df282aacbca', 'therapist@email.com', 'Therapist', 'Tsipareht', null);
    
SET @therapistId = (SELECT id FROM `user` WHERE email = 'therapist@email.com');
UPDATE user SET therapistId = @therapistId WHERE role = 'PATIENT';

INSERT INTO `session` (`id`, `therapistId`)
VALUES (1,3);

INSERT INTO `session_game` (`game`, `gameOrder`, `session`)
VALUES ('recognition',1,1);
       
INSERT INTO `integer_parameter` (`game`, `id`, `defaultValue`, `max`, `min`)
VALUES ('recognition','maxRepetitions',1,1,5),
       ('recognition','numOfStimuli',6,1,12);

INSERT INTO `seconds_parameter` (`game`, `id`, `defaultValue`)
VALUES ('recognition','diceShowTime',5);

INSERT INTO `session_game_param_value` (`game`, `gameOrder`, `session`, `value`, `param`)
VALUES ('recognition',1,1,3,'diceShowTime'),
       ('recognition',2,1,2,'maxRepetitions'),
       ('recognition',3,1,6,'numOfStimuli');

INSERT INTO `i18n` (`messageKey`, `locale`, `value`)
VALUES ('session.1.description','EN_US','Recognition game.'),
       ('session.1.description','ES_ES','Juego de reconocimiento.'),
       ('session.1.description','GL_ES','Xogo de recoñecemento.'),
       ('session.1.name','EN_US','Recognition'),
       ('session.1.name','ES_ES','Reconocimiento'),
       ('session.1.name','GL_ES','Recoñecemento');

INSERT INTO `session_i18n` (`session`, `i18nKey`, `i18nLocale`)
VALUES (1,'session.1.description','EN_US'),
       (1,'session.1.description','ES_ES'),
       (1,'session.1.description','GL_ES'),
       (1,'session.1.name','EN_US'),
       (1,'session.1.name','ES_ES'),
       (1,'session.1.name','GL_ES');