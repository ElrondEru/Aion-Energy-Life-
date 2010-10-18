/*
Navicat MySQL Data Transfer

Source Server         : aion
Source Server Version : 50136
Source Host           : localhost:3306
Source Database       : arf

Target Server Type    : MYSQL
Target Server Version : 50136
File Encoding         : 65001

Date: 2010-10-18 03:10:48
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `abyss_rank`
-- ----------------------------
DROP TABLE IF EXISTS `abyss_rank`;
CREATE TABLE `abyss_rank` (
  `player_id` int(11) NOT NULL,
  `ap` int(11) NOT NULL,
  `rank` int(2) NOT NULL DEFAULT '1',
  `all_kill` int(4) NOT NULL DEFAULT '0',
  `max_rank` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`player_id`),
  CONSTRAINT `abyss_rank_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of abyss_rank
-- ----------------------------
INSERT INTO `abyss_rank` VALUES ('74221', '10000', '3', '0', '3');
