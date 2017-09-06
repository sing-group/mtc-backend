---
-- #%L
-- Tests
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
---
DELETE FROM `session_i18n`;
DELETE FROM `i18n`;
DELETE FROM `game_result_value`;
DELETE FROM `game_result`;
DELETE FROM `session_game_param_value`;
DELETE FROM `session_game`;
DELETE FROM `session`;
DELETE FROM `integer_parameter`;
DELETE FROM `seconds_parameter`;
DELETE FROM `game_parameter`;
DELETE FROM `game_type`;
DELETE FROM `game`;

DELETE FROM `patient`;
DELETE FROM `therapist`;
DELETE FROM `institution`;
DELETE FROM `manager`;
DELETE FROM `administrator`;
DELETE FROM `user`;
