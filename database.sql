-- ----------------------------
-- Table structure for `info_messages`
-- ----------------------------
DROP TABLE IF EXISTS `info_messages`;
CREATE TABLE `info_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_name` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `enabled` enum('false','true') NOT NULL DEFAULT 'true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of info_messages
-- ----------------------------
INSERT INTO `info_messages` VALUES ('1', 'Rule', 'No badwords!', 'true');
INSERT INTO `info_messages` VALUES ('2', 'Rule', 'No spam!', 'true');

-- ----------------------------
-- Table structure for `permissions_commands`
-- ----------------------------
DROP TABLE IF EXISTS `permissions_commands`;
CREATE TABLE `permissions_commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` varchar(255) NOT NULL,
  `min_prefix` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of permissions_commands
-- ----------------------------
INSERT INTO `permissions_commands` VALUES ('1', 'kick', '2');

-- ----------------------------
-- Table structure for `prefixes`
-- ----------------------------
DROP TABLE IF EXISTS `prefixes`;
CREATE TABLE `prefixes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `prefix` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of prefixes
-- ----------------------------
INSERT INTO `prefixes` VALUES ('1', 'Server', 'server', '009fd4');
INSERT INTO `prefixes` VALUES ('2', 'Moderator', 'mod', '09cc0e');
INSERT INTO `prefixes` VALUES ('3', 'Admin', 'admin', 'cc0909');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `prefix` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'Can', '123456', '3');

-- ----------------------------
-- Table structure for `wordfilter`
-- ----------------------------
DROP TABLE IF EXISTS `wordfilter`;
CREATE TABLE `wordfilter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word` varchar(255) NOT NULL,
  `replacement` varchar(255) NOT NULL DEFAULT '',
  `enabled` enum('false','true') NOT NULL DEFAULT 'true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of wordfilter
-- ----------------------------
INSERT INTO `wordfilter` VALUES ('1', 'Hure', '******', 'true');
INSERT INTO `wordfilter` VALUES ('2', 'Arsch', '******', 'true');
INSERT INTO `wordfilter` VALUES ('3', 'Missgeburt', '******', 'true');
