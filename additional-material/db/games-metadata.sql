-- MTC Game v0.1.0

INSERT INTO `mtc`.`game` (`id`)
VALUES ('recognition'),
       ('verbalFluency');

INSERT INTO `mtc`.`game_type` (`gameId`, `name`)
VALUES ('recognition', 'RECOGNITION'),
       ('verbalFluency', 'VERBAL_FLUENCY');

INSERT INTO `mtc`.`integer_parameter` (`gameId`, `name`, `defaultValue`, `max`, `min`)
VALUES ('recognition', 'numOfStimuli', '6', '1', '12'),
       ('recognition', 'maxRepetitions', '1', '1', '5');

INSERT INTO `mtc`.`seconds_parameter` (`gameId`, `name`, `defaultValue`)
VALUES ('recognition', 'diceShowTime', '5');


