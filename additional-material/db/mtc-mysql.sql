CREATE DATABASE  IF NOT EXISTS `mtc`;
USE `mtc`;

DROP TABLE IF EXISTS `session_i18n`;
DROP TABLE IF EXISTS `session_game_param_value`;
DROP TABLE IF EXISTS `session_game`;
DROP TABLE IF EXISTS `assigned_session`;

DROP TABLE IF EXISTS `game_type`;
DROP TABLE IF EXISTS `i18n`;
DROP TABLE IF EXISTS `integer_parameter`;
DROP TABLE IF EXISTS `seconds_parameter`;
DROP TABLE IF EXISTS `session`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `game`;

CREATE TABLE `i18n` (
  `messageKey` varchar(64) NOT NULL,
  `locale` char(5) NOT NULL,
  `value` TEXT NOT NULL,
  PRIMARY KEY (`messageKey`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `game` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `game_type` (
  `game` varchar(255) NOT NULL,
  `name` varchar(18) NOT NULL,
  PRIMARY KEY (`game`,`name`),
  CONSTRAINT `FK_eykuvq5pkr68ice0l9x9kkn5m` FOREIGN KEY (`game`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `integer_parameter` (
  `game` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `defaultValue` int(11) DEFAULT NULL,
  `max` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  PRIMARY KEY (`game`,`id`),
  CONSTRAINT `FK_hb5sgkd9trj29u7kp7fb9krw3` FOREIGN KEY (`game`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `seconds_parameter` (
  `game` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `defaultValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`game`,`id`),
  CONSTRAINT `FK_qsu3e21qu3t4yts91yk0km9v1` FOREIGN KEY (`game`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `role` varchar(9) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `therapistId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  KEY `FK_patient_therapist` (`therapistId`),
  CONSTRAINT `FK_patient_therapist` FOREIGN KEY (`therapistId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `therapistId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_session_therapist` (`therapistId`),
  CONSTRAINT `FK_session_therapist` FOREIGN KEY (`therapistId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `session_game` (
  `game` varchar(255) NOT NULL,
  `gameOrder` int(11) NOT NULL,
  `session` int(11) NOT NULL,
  PRIMARY KEY (`game`,`gameOrder`,`session`),
  KEY `FK_sessiongame_gamesession` (`session`),
  CONSTRAINT `FK_sessiongame_game` FOREIGN KEY (`game`) REFERENCES `game` (`id`),
  CONSTRAINT `FK_sessiongame_gamesession` FOREIGN KEY (`session`) REFERENCES `session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `session_game_param_value` (
  `game` varchar(255) NOT NULL,
  `gameOrder` int(11) NOT NULL,
  `session` int(11) NOT NULL,
  `value` varchar(255) NOT NULL,
  `param` varchar(255) NOT NULL,
  PRIMARY KEY (`game`,`gameOrder`,`session`,`param`),
  CONSTRAINT `FK_8ekfakwduh98iv2tdy64tu52e` FOREIGN KEY (`game`, `gameOrder`, `session`) REFERENCES `session_game` (`game`, `gameOrder`, `session`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `session_i18n` (
  `session` int(11) NOT NULL,
  `i18nKey` varchar(64) NOT NULL,
  `i18nLocale` char(5) NOT NULL,
  PRIMARY KEY (`session`,`i18nKey`,`i18nLocale`),
  UNIQUE KEY `UK_2b1ev5fxk8otkgyqsvf25y5o3` (`i18nKey`,`i18nLocale`),
  CONSTRAINT `FK_2b1ev5fxk8otkgyqsvf25y5o3` FOREIGN KEY (`i18nKey`, `i18nLocale`) REFERENCES `i18n` (`messageKey`, `locale`),
  CONSTRAINT `FK_n9qi3b1ryv7lakq0gqjjtmrix` FOREIGN KEY (`session`) REFERENCES `session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `assigned_session` (
  `assignmentDate` datetime NOT NULL,
  `patientId` int(11) NOT NULL,
  `session` int(11) NOT NULL,
  `endDate` datetime NOT NULL,
  `startDate` datetime NOT NULL,
  PRIMARY KEY (`assignmentDate`,`patientId`,`session`),
  KEY `FK_assignedsession_patient` (`patientId`),
  KEY `FK_assignedsession_gamesession` (`session`),
  CONSTRAINT `FK_assignedsession_gamesession` FOREIGN KEY (`session`) REFERENCES `session` (`id`),
  CONSTRAINT `FK_assignedsession_patient` FOREIGN KEY (`patientId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

