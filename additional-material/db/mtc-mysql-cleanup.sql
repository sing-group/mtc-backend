DELETE FROM `session_i18n`;
DELETE FROM `i18n`;
DELETE FROM `session_game_param_value`;
DELETE FROM `session_game_configuration`;
DELETE FROM `session`;
DELETE FROM `integer_parameter`;
DELETE FROM `seconds_parameter`;
DELETE FROM `game_parameter`;
DELETE FROM `game_type`;
DELETE FROM `game`;
DELETE FROM `game_result_value`;
DELETE FROM `game_result`;
DELETE FROM `assigned_session`;

DELETE FROM `patient`;
DELETE FROM `therapist`;
DELETE FROM `institution`;
DELETE FROM `manager`;
DELETE FROM `administrator`;
DELETE FROM `user`;

ALTER TABLE `assigned_session` AUTO_INCREMENT = 1;
ALTER TABLE `game_result` AUTO_INCREMENT = 1;
ALTER TABLE `institution` AUTO_INCREMENT = 1;
ALTER TABLE `session` AUTO_INCREMENT = 1;
