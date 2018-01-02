--
-- #%L
-- MTC
-- %%
-- Copyright (C) 2017 Miguel Reboiro-Jato and Adolfo Piñón Blanco
-- %%
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public
-- License along with this program.  If not, see
-- <http://www.gnu.org/licenses/gpl-3.0.html>.
-- #L%
--

-- MySQL dump 10.13  Distrib 5.6.33, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: mtc
-- ------------------------------------------------------
-- Server version	5.6.33-0ubuntu0.14.04.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_jj3mmcc0vjobqibj67dvuwihk` (`email`),
  CONSTRAINT `FK7mdlakg18ui7hqe8de30yni45` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `assigned_games_session`
--

DROP TABLE IF EXISTS `assigned_games_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assigned_games_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assignmentDate` date NOT NULL,
  `endDate` date NOT NULL,
  `startDate` date NOT NULL,
  `gamesSession` bigint(20) NOT NULL,
  `patient` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_assignedsession_gamesession` (`gamesSession`),
  KEY `FK_assignedsession_patient` (`patient`),
  CONSTRAINT `FK_assignedsession_gamesession` FOREIGN KEY (`gamesSession`) REFERENCES `games_session` (`id`),
  CONSTRAINT `FK_assignedsession_patient` FOREIGN KEY (`patient`) REFERENCES `patient` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_in_games_session`
--

DROP TABLE IF EXISTS `game_in_games_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_in_games_session` (
  `gameOrder` int(11) NOT NULL,
  `gamesSession` bigint(20) NOT NULL,
  `game` varchar(255) NOT NULL,
  PRIMARY KEY (`gameOrder`,`gamesSession`),
  KEY `FK_sessiongame_gamesession` (`gamesSession`),
  KEY `FK_sessiongame_game` (`game`),
  CONSTRAINT `FK_sessiongame_game` FOREIGN KEY (`game`) REFERENCES `game` (`id`),
  CONSTRAINT `FK_sessiongame_gamesession` FOREIGN KEY (`gamesSession`) REFERENCES `games_session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_in_games_session_param_value`
--

DROP TABLE IF EXISTS `game_in_games_session_param_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_in_games_session_param_value` (
  `gameOrder` int(11) NOT NULL,
  `gamesSession` bigint(20) NOT NULL,
  `value` varchar(255) NOT NULL,
  `param` varchar(255) NOT NULL,
  PRIMARY KEY (`gameOrder`,`gamesSession`,`param`),
  CONSTRAINT `FK_sessiongame_paramvalues` FOREIGN KEY (`gameOrder`, `gamesSession`) REFERENCES `game_in_games_session` (`gameOrder`, `gamesSession`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_parameter`
--

DROP TABLE IF EXISTS `game_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_parameter` (
  `game` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`game`,`id`),
  CONSTRAINT `FK_gameparameter_game` FOREIGN KEY (`game`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_result`
--

DROP TABLE IF EXISTS `game_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `attempt` int(11) NOT NULL,
  `end` datetime DEFAULT NULL,
  `start` datetime NOT NULL,
  `assignedGamesSession` bigint(20) NOT NULL,
  `gameOrder` int(11) NOT NULL,
  `gamesSession` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK45knnseofwbonpb9nxkfei9nm` (`assignedGamesSession`,`gameOrder`,`gamesSession`),
  KEY `FK5nc6fvg4ig8jcpudqpo6xlgsc` (`gameOrder`,`gamesSession`),
  CONSTRAINT `FK5nc6fvg4ig8jcpudqpo6xlgsc` FOREIGN KEY (`gameOrder`, `gamesSession`) REFERENCES `game_in_games_session` (`gameOrder`, `gamesSession`),
  CONSTRAINT `FKd49ief596gy2786hhs7pgqloc` FOREIGN KEY (`assignedGamesSession`) REFERENCES `assigned_games_session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_result_value`
--

DROP TABLE IF EXISTS `game_result_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_result_value` (
  `gameResult` bigint(20) NOT NULL,
  `value` varchar(255) NOT NULL,
  `result` varchar(255) NOT NULL,
  PRIMARY KEY (`gameResult`,`result`),
  CONSTRAINT `FK_gameresult_resultvalues` FOREIGN KEY (`gameResult`) REFERENCES `game_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_type`
--

DROP TABLE IF EXISTS `game_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_type` (
  `game` varchar(255) NOT NULL,
  `name` varchar(18) NOT NULL,
  PRIMARY KEY (`game`,`name`),
  CONSTRAINT `FK_game_type` FOREIGN KEY (`game`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `games_session`
--

DROP TABLE IF EXISTS `games_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `games_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `therapist` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_session_therapist` (`therapist`),
  CONSTRAINT `FK_session_therapist` FOREIGN KEY (`therapist`) REFERENCES `therapist` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `games_session_i18n`
--

DROP TABLE IF EXISTS `games_session_i18n`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `games_session_i18n` (
  `gamesSession` bigint(20) NOT NULL,
  `i18nKey` varchar(64) NOT NULL,
  `i18nLocale` char(5) NOT NULL,
  PRIMARY KEY (`gamesSession`,`i18nKey`,`i18nLocale`),
  UNIQUE KEY `UK_sflkocxj7i22gsrg1gk5ia64w` (`i18nKey`,`i18nLocale`),
  CONSTRAINT `FK_gamesession_i18n` FOREIGN KEY (`gamesSession`) REFERENCES `games_session` (`id`),
  CONSTRAINT `FKau9ubg8w2y71prmynjfk21074` FOREIGN KEY (`i18nKey`, `i18nLocale`) REFERENCES `i18n` (`messageKey`, `locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `i18n`
--

DROP TABLE IF EXISTS `i18n`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `i18n` (
  `messageKey` varchar(64) NOT NULL,
  `locale` char(5) NOT NULL,
  `value` longtext NOT NULL,
  PRIMARY KEY (`messageKey`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `institution`
--

DROP TABLE IF EXISTS `institution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `institution` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(1023) DEFAULT NULL,
  `description` varchar(1023) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `manager` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qhw15h5f7nc4g3ndva8sory1u` (`name`),
  KEY `FK_institution_manager` (`manager`),
  CONSTRAINT `FK_institution_manager` FOREIGN KEY (`manager`) REFERENCES `manager` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `integer_parameter`
--

DROP TABLE IF EXISTS `integer_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `integer_parameter` (
  `defaultValue` int(11) DEFAULT NULL,
  `max` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `game` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`game`,`id`),
  CONSTRAINT `FKpdmon9xik697yh6paty3omabw` FOREIGN KEY (`game`, `id`) REFERENCES `game_parameter` (`game`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manager` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `login` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_ch4c0h9mgdd2c5lategqkpsyi` (`email`),
  CONSTRAINT `FK4ulkn06biim9gyxqh3tr2ls8j` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patient` (
  `login` varchar(100) NOT NULL,
  `therapist` varchar(100) NOT NULL,
  PRIMARY KEY (`login`),
  KEY `FK_patient_therapist` (`therapist`),
  CONSTRAINT `FK_patient_therapist` FOREIGN KEY (`therapist`) REFERENCES `therapist` (`login`),
  CONSTRAINT `FKkqd1x5dogixnklrkprr52sj2` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seconds_parameter`
--

DROP TABLE IF EXISTS `seconds_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seconds_parameter` (
  `defaultValue` int(11) DEFAULT NULL,
  `game` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`game`,`id`),
  CONSTRAINT `FKmhfwhcawq5phv2im9uh5smh3u` FOREIGN KEY (`game`, `id`) REFERENCES `game_parameter` (`game`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `therapist`
--

DROP TABLE IF EXISTS `therapist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `therapist` (
  `email` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `login` varchar(100) NOT NULL,
  `institution` bigint(20) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_faaj30mspthno6k9c85j09788` (`email`),
  KEY `FK_institution_therapist` (`institution`),
  CONSTRAINT `FK_institution_therapist` FOREIGN KEY (`institution`) REFERENCES `institution` (`id`),
  CONSTRAINT `FKda0woh35t1e0tbrtj4ajgvrh4` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `role` varchar(9) NOT NULL,
  `login` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-02 22:16:34
