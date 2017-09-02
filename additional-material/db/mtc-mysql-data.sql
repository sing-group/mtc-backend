-- MTC Game v0.1.0

INSERT INTO `user` (`role`, `login`, `password`) VALUES ('ADMIN', 'admin', '25E4EE4E9229397B6B17776BFCEAF8E7');
INSERT INTO `user` (`role`, `login`, `password`) VALUES ('MANAGER', 'manager', '3FD7488B6FD40F33C5A8E857B6A944AA');
INSERT INTO `user` (`role`, `login`, `password`) VALUES ('THERAPIST', 'therapist', 'F15A0A52502E22F1E9D85DF282AACBCA');
INSERT INTO `user` (`role`, `login`, `password`) VALUES ('PATIENT', 'patient', '03645D1E2E298B6D172C5126BDED3F60');

INSERT INTO `administrator` (`email`, `name`, `surname`, `login`) VALUES ('admin@email.com', 'Admin', 'Nimda', 'admin');
INSERT INTO `manager` (`email`, `name`, `surname`, `login`) VALUES ('admin@email.com', 'Manager', 'Reganam', 'manager');
INSERT INTO `institution` (`name`, `address`, `description`, `manager`) VALUES ('Institution 1', 'Institution St. 1', 'First institution', 'manager');
INSERT INTO `therapist` (`email`, `name`, `surname`, `login`, `institution`) VALUES ('therapist', 'Therapist', 'Tsipareht', 'therapist', 1);
INSERT INTO `patient` (`login`, `therapist`) VALUES ('patient', 'therapist');


INSERT INTO `game` (`id`)
VALUES ('recognition'),
       ('verbalFluency');

INSERT INTO `game_type` (`game`, `name`)
VALUES ('recognition', 'RECOGNITION'),
       ('verbalFluency', 'VERBAL_FLUENCY');
       
INSERT INTO `integer_parameter` (`game`, `id`, `defaultValue`, `max`, `min`)
VALUES ('recognition','maxRepetitions',1,1,5),
       ('recognition','numOfStimuli',6,1,12);

INSERT INTO `seconds_parameter` (`game`, `id`, `defaultValue`)
VALUES ('recognition','diceShowTime',5);

INSERT INTO `session` (`id`, `therapist`)
VALUES (1,'therapist');

INSERT INTO `session_game` (`game`, `gameOrder`, `session`)
VALUES ('recognition',1,1),
       ('verbalFluency',2,1),
       ('recognition',3,1);

INSERT INTO `session_game_param_value` (`game`, `gameOrder`, `session`, `value`, `param`)
VALUES ('recognition',1,1,3,'diceShowTime'),
       ('recognition',1,1,2,'maxRepetitions'),
       ('recognition',1,1,6,'numOfStimuli'),
       ('recognition',3,1,3,'diceShowTime'),
       ('recognition',3,1,2,'maxRepetitions'),
       ('recognition',3,1,10,'numOfStimuli');

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