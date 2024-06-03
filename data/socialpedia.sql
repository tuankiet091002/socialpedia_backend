/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : socialpedia

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 03/06/2024 11:28:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel_members
-- ----------------------------
DROP TABLE IF EXISTS `channel_members`;
CREATE TABLE `channel_members`  (
  `channel_permission` tinyint NULL DEFAULT NULL,
  `joined_date` date NULL DEFAULT NULL,
  `member_permission` tinyint NULL DEFAULT NULL,
  `message_permission` tinyint NULL DEFAULT NULL,
  `status` enum('PENDING','ACCEPTED','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` int NOT NULL,
  `channel_id` int NOT NULL,
  PRIMARY KEY (`channel_id`, `user_id`) USING BTREE,
  INDEX `FKjunpxp98pv6g23t5d3w5hx8ji`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK5w7mqibtg5vbjt7xq6xyhc479` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKjunpxp98pv6g23t5d3w5hx8ji` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `channel_members_chk_1` CHECK (`channel_permission` between 0 and 4),
  CONSTRAINT `channel_members_chk_2` CHECK (`member_permission` between 0 and 4),
  CONSTRAINT `channel_members_chk_3` CHECK (`message_permission` between 0 and 4)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel_members
-- ----------------------------
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 1);
INSERT INTO `channel_members` VALUES (0, '2024-06-03', 0, 0, 'PENDING', 2, 1);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 5, 1);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 9, 1);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 11, 1);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 2);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 2, 2);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 6, 2);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 7, 2);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 8, 2);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 9, 2);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 2, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 3, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 4, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 5, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 6, 3);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 9, 3);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 4);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 6, 4);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 9, 4);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 11, 4);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 2, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 3, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 5, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 6, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 7, 5);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 8, 5);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 3, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 4, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 6, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 8, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 10, 6);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 11, 6);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 2, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 3, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 4, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 7, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 8, 52);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 9, 52);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 1, 53);
INSERT INTO `channel_members` VALUES (4, '2024-06-03', 4, 4, 'ACCEPTED', 2, 53);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 8, 53);
INSERT INTO `channel_members` VALUES (2, '2024-06-03', 2, 3, 'ACCEPTED', 10, 53);

-- ----------------------------
-- Table structure for channel_messages
-- ----------------------------
DROP TABLE IF EXISTS `channel_messages`;
CREATE TABLE `channel_messages`  (
  `channel_id` int NOT NULL,
  `message_id` int NOT NULL,
  UNIQUE INDEX `UK_bajtr3y7n13btcn6kx0p2fqa7`(`message_id` ASC) USING BTREE,
  INDEX `FKaeo3kenkaykvfn8qn644ohhcx`(`channel_id` ASC) USING BTREE,
  CONSTRAINT `FK1hco562fda1gdu8814t0m4pb` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKaeo3kenkaykvfn8qn644ohhcx` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel_messages
-- ----------------------------

-- ----------------------------
-- Table structure for channels
-- ----------------------------
DROP TABLE IF EXISTS `channels`;
CREATE TABLE `channels`  (
  `id` int NOT NULL,
  `created_date` datetime NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `is_active` tinyint(1) NULL DEFAULT 1,
  `modified_date` datetime NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` int NULL DEFAULT NULL,
  `created_by` int NULL DEFAULT NULL,
  `modified_by` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_dhs3qvl88m6likubt3bg9on9j`(`avatar` ASC) USING BTREE,
  INDEX `FKj5bvg2dapo5aqh7j9xc2wr8e3`(`created_by` ASC) USING BTREE,
  INDEX `FKixky43y5dxdjcqujs3r8mautl`(`modified_by` ASC) USING BTREE,
  CONSTRAINT `FK1yrwkdxa9d1h19foovnm0r0yo` FOREIGN KEY (`avatar`) REFERENCES `resources` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKixky43y5dxdjcqujs3r8mautl` FOREIGN KEY (`modified_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKj5bvg2dapo5aqh7j9xc2wr8e3` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channels
-- ----------------------------
INSERT INTO `channels` VALUES (1, '2024-06-03 10:32:35', 'TechTalk Lounge is the ultimate destination for tech enthusiasts to engage in lively discussions about the latest innovations, gadgets, and breakthroughs in the world of technology. Whether you\'re a seasoned developer, a curious consumer, or just someone fascinated by the rapid advancements in the tech realm, this channel provides a space to share insights, troubleshoot tech issues, and explore the future of digital evolution.', 1, '2024-06-03 10:58:27', 'TechTalk Lounge', 12, 1, 1);
INSERT INTO `channels` VALUES (2, '2024-06-03 10:33:15', 'Bookworm Haven is a virtual sanctuary for literary aficionados and bookworms alike. Here, the written word takes center stage as members dive into deep conversations about classic novels, contemporary bestsellers, and hidden literary gems. From genre-specific recommendations to thought-provoking discussions on character development and plot twists, this channel is a haven for those who find solace and joy in the pages of a good book.', 1, '2024-06-03 10:58:26', 'Bookworm Haven', 13, 1, 1);
INSERT INTO `channels` VALUES (3, '2024-06-03 10:33:43', 'Fitness Fanatics Hub is the go-to spot for health enthusiasts, gym buffs, and anyone committed to living an active lifestyle. Whether you\'re a seasoned athlete, a beginner on a fitness journey, or someone just looking for motivation, this channel provides a supportive community to share workout tips, nutrition advice, and celebrate personal milestones. Join the conversation and empower yourself on the path to a healthier, more active life.', 1, '2024-06-03 10:58:25', 'Fitness Fanatics Hub', 14, 1, 1);
INSERT INTO `channels` VALUES (4, '2024-06-03 10:34:17', 'Creative Minds Collective is a vibrant space where artists, writers, musicians, and creators of all kinds come together to inspire and be inspired. Share your latest artistic endeavors, seek feedback on your work, or collaborate with fellow creatives. This channel is a melting pot of imagination, fostering a community that encourages exploration, experimentation, and the celebration of diverse forms of self-expression.', 1, '2024-06-03 10:58:12', 'Creative Minds Collective', 15, 1, 1);
INSERT INTO `channels` VALUES (5, '2024-06-03 10:34:47', 'Green Living Oasis is a dedicated channel for eco-conscious individuals striving to make a positive impact on the environment. From sustainable living tips and eco-friendly product recommendations to discussions on climate change and conservation efforts, this channel serves as a gathering place for those passionate about fostering a greener, more sustainable future. Join us in the pursuit of eco-conscious living and share your insights on preserving our planet.', 1, '2024-06-03 10:58:24', 'Green Living Oasis', 16, 1, 1);
INSERT INTO `channels` VALUES (6, '2024-06-03 10:34:52', 'Global Gastronomy Galore is a delectable channel where foodies from around the world come together to share their love for culinary delights. From international recipes and cooking tips to discussions on food culture and unique dining experiences, this channel is a feast for the senses. Join fellow epicureans in exploring the rich tapestry of global cuisines and discover new flavors that will tantalize your taste buds.', 1, '2024-06-03 10:58:20', 'Global Gastronomy Galore', 18, 1, 1);
INSERT INTO `channels` VALUES (52, '2024-06-03 10:38:52', 'Adventure Seekers Network is the ultimate virtual basecamp for thrill-seekers and outdoor enthusiasts. Whether you\\\'re into hiking, rock climbing, backpacking, or extreme sports, this channel is your go-to for adrenaline-pumping discussions. Share your latest adventures, seek advice on gear, and connect with fellow adrenaline junkies who understand the call of the wild. Embark on a journey with like-minded individuals who live for the thrill of the great outdoors.', 1, '2024-06-03 10:58:23', 'Adventure Seekers', 19, 1, 1);
INSERT INTO `channels` VALUES (53, '2024-06-03 10:40:25', 'Language Exchange Plaza is a dynamic hub for language learners and polyglots to come together and embark on a linguistic journey. Whether you\'re brushing up on a second language or starting from scratch, this channel provides a supportive environment for practicing and learning new languages. Engage in language exchange sessions, share helpful resources, and connect with speakers of various languages around the globe. Immerse yourself in a diverse linguistic landscape and broaden your cultural horizons.', 1, '2024-06-03 10:57:30', 'Language Exchange Plaza', 20, 2, 1);

-- ----------------------------
-- Table structure for inbox_messages
-- ----------------------------
DROP TABLE IF EXISTS `inbox_messages`;
CREATE TABLE `inbox_messages`  (
  `inbox_id` int NOT NULL,
  `message_id` int NOT NULL,
  UNIQUE INDEX `UK_cytwemve5rf1lnv9du9u23gvn`(`message_id` ASC) USING BTREE,
  INDEX `FKichhcqrdhbr1emft9n11e2nfu`(`inbox_id` ASC) USING BTREE,
  CONSTRAINT `FKf61klkmo0opoufcg4dk2jv2ch` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKichhcqrdhbr1emft9n11e2nfu` FOREIGN KEY (`inbox_id`) REFERENCES `inboxs` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inbox_messages
-- ----------------------------

-- ----------------------------
-- Table structure for inboxs
-- ----------------------------
DROP TABLE IF EXISTS `inboxs`;
CREATE TABLE `inboxs`  (
  `id` int NOT NULL,
  `is_active` tinyint(1) NULL DEFAULT 1,
  `modified_date` datetime NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receiver_id` int NULL DEFAULT NULL,
  `sender_id` int NULL DEFAULT NULL,
  `modified_by` int NULL DEFAULT NULL,
  `receiver_last_seen` int NULL DEFAULT NULL,
  `sender_last_seen` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKgylbun5ao1vmf139bocycft6j`(`receiver_id` ASC, `sender_id` ASC) USING BTREE,
  INDEX `FK4x0wcifb5fpdu7bv1aq6cdl4`(`modified_by` ASC) USING BTREE,
  INDEX `FKpc7d0md198fh3a5o2mlaco8cd`(`receiver_last_seen` ASC) USING BTREE,
  INDEX `FKdllqoe3d4otrj4s7y58bgvdae`(`sender_last_seen` ASC) USING BTREE,
  CONSTRAINT `FK4x0wcifb5fpdu7bv1aq6cdl4` FOREIGN KEY (`modified_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKdllqoe3d4otrj4s7y58bgvdae` FOREIGN KEY (`sender_last_seen`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKgylbun5ao1vmf139bocycft6j` FOREIGN KEY (`receiver_id`, `sender_id`) REFERENCES `user_friendships` (`receiver_id`, `sender_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpc7d0md198fh3a5o2mlaco8cd` FOREIGN KEY (`receiver_last_seen`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inboxs
-- ----------------------------

-- ----------------------------
-- Table structure for message_resources
-- ----------------------------
DROP TABLE IF EXISTS `message_resources`;
CREATE TABLE `message_resources`  (
  `message_id` int NOT NULL,
  `resource_id` int NOT NULL,
  INDEX `FKtcv5g0l9pwbnr7rvm6vbv3aic`(`resource_id` ASC) USING BTREE,
  INDEX `FKqrmog074ocaadrj1cg2jkba2r`(`message_id` ASC) USING BTREE,
  CONSTRAINT `FKqrmog074ocaadrj1cg2jkba2r` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKtcv5g0l9pwbnr7rvm6vbv3aic` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_resources
-- ----------------------------

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `modified_date` datetime NULL DEFAULT NULL,
  `status` enum('ACTIVE','MODIFIED','INACTIVE','PINNED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_by` int NULL DEFAULT NULL,
  `reply_to` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqrh6snbfu5yme4sxyf8vkh5xt`(`created_by` ASC) USING BTREE,
  INDEX `FK70dbtbrm0exf9r8adkipf6rv3`(`reply_to` ASC) USING BTREE,
  CONSTRAINT `FK70dbtbrm0exf9r8adkipf6rv3` FOREIGN KEY (`reply_to`) REFERENCES `messages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKqrh6snbfu5yme4sxyf8vkh5xt` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of messages
-- ----------------------------

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `destination` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` tinyint NULL DEFAULT NULL,
  `avatar` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKlqgkuva6ippuy3npn0qyuic8i`(`avatar` ASC) USING BTREE,
  INDEX `FK9y21adhxn0ayjhfocscqox7bh`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKlqgkuva6ippuy3npn0qyuic8i` FOREIGN KEY (`avatar`) REFERENCES `resources` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `notifications_chk_1` CHECK (`type` between 0 and 2)
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (1, 'eOPOq3LnMO7gYZVjVh3ojrBRN7GSx4JYKjOLEVHlvmOvOsC4jGF2GxedJ/1uEIcUsI/hjcUQYOiV6VsZ/rwQnA==', '2024-06-03 09:48:56.136825', 'mWmx4KLNI1ocWWRapLkwmw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 1, 2);
INSERT INTO `notifications` VALUES (2, 'lFwPiJpqojq/jSya5NR5vvBp7+pqm36A/QupzmzjzUomp3VEC6/FgPNh6CrgiKMu', '2024-06-03 09:48:59.614911', 'ZG/EupuGQBFZ4oqImOfDvg==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 2, 1);
INSERT INTO `notifications` VALUES (3, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:23.244337', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 3);
INSERT INTO `notifications` VALUES (4, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:26.046274', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 4);
INSERT INTO `notifications` VALUES (5, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:28.842075', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 5);
INSERT INTO `notifications` VALUES (6, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:39.358768', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 6);
INSERT INTO `notifications` VALUES (7, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:45.632323', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 8);
INSERT INTO `notifications` VALUES (8, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:49.329641', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 10);
INSERT INTO `notifications` VALUES (9, 'UY0gLS7a6iR5AewELvrm+JuCTkpXoxOrYxGDIhgsF7VqRHeHx/Py0RLWWiqoTUF5', '2024-06-03 09:51:55.716332', 'ZG/EupuGQBFZ4oqImOfDvg==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 2, 9);
INSERT INTO `notifications` VALUES (10, 'DdRzFoWwBjvGQbW2rKBT9YrQ7l9CPMFoWtoWF8nCMDR4CRBs0nQzLx1/t1CM7xJ+', '2024-06-03 09:52:16.961665', 'j6NA7GAkqntIv9O4Qb9JJw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 3, 4);
INSERT INTO `notifications` VALUES (11, 'DdRzFoWwBjvGQbW2rKBT9YrQ7l9CPMFoWtoWF8nCMDR4CRBs0nQzLx1/t1CM7xJ+', '2024-06-03 09:52:28.271520', 'j6NA7GAkqntIv9O4Qb9JJw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 3, 6);
INSERT INTO `notifications` VALUES (12, 'DdRzFoWwBjvGQbW2rKBT9YrQ7l9CPMFoWtoWF8nCMDR4CRBs0nQzLx1/t1CM7xJ+', '2024-06-03 09:52:37.344954', 'j6NA7GAkqntIv9O4Qb9JJw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 3, 8);
INSERT INTO `notifications` VALUES (13, 'DdRzFoWwBjvGQbW2rKBT9YrQ7l9CPMFoWtoWF8nCMDR4CRBs0nQzLx1/t1CM7xJ+', '2024-06-03 09:52:43.731359', 'j6NA7GAkqntIv9O4Qb9JJw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 3, 9);
INSERT INTO `notifications` VALUES (14, 'VSHvGn/yMUaGXRvJ7tj4iPbMV9NU/U92mgebxBz6n0x8F4idFq/FOgP8Cm9+O5Fk', '2024-06-03 09:53:21.492578', '3jgSc9LHYM7UvEbfw5Y4/A==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 4, 6);
INSERT INTO `notifications` VALUES (15, 'VSHvGn/yMUaGXRvJ7tj4iPbMV9NU/U92mgebxBz6n0x8F4idFq/FOgP8Cm9+O5Fk', '2024-06-03 09:53:26.053862', '3jgSc9LHYM7UvEbfw5Y4/A==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 4, 7);
INSERT INTO `notifications` VALUES (16, 'VSHvGn/yMUaGXRvJ7tj4iPbMV9NU/U92mgebxBz6n0x8F4idFq/FOgP8Cm9+O5Fk', '2024-06-03 09:53:30.481567', '3jgSc9LHYM7UvEbfw5Y4/A==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 4, 9);
INSERT INTO `notifications` VALUES (17, 'VSHvGn/yMUaGXRvJ7tj4iPbMV9NU/U92mgebxBz6n0x8F4idFq/FOgP8Cm9+O5Fk', '2024-06-03 09:53:36.826427', '3jgSc9LHYM7UvEbfw5Y4/A==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 4, 11);
INSERT INTO `notifications` VALUES (18, 'MFsewrYrDb4mfjHR7RmSyNFuzszBozoJyMOrUli2Yo8xlsw75ucpQBINoqPkAk2a', '2024-06-03 09:54:29.793260', 'bDlImNTox3xX5ez+LNosqw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 5, 3);
INSERT INTO `notifications` VALUES (19, 'MFsewrYrDb4mfjHR7RmSyNFuzszBozoJyMOrUli2Yo8xlsw75ucpQBINoqPkAk2a', '2024-06-03 09:54:33.775751', 'bDlImNTox3xX5ez+LNosqw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 5, 11);
INSERT INTO `notifications` VALUES (20, 'MFsewrYrDb4mfjHR7RmSyNFuzszBozoJyMOrUli2Yo8xlsw75ucpQBINoqPkAk2a', '2024-06-03 09:54:36.898838', 'bDlImNTox3xX5ez+LNosqw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 5, 10);
INSERT INTO `notifications` VALUES (21, 'MFsewrYrDb4mfjHR7RmSyNFuzszBozoJyMOrUli2Yo8xlsw75ucpQBINoqPkAk2a', '2024-06-03 09:54:39.814702', 'bDlImNTox3xX5ez+LNosqw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 5, 9);
INSERT INTO `notifications` VALUES (22, 'SCEa06DY1lq9AyEnsmO6gfAWz2XaFXz6EsMdOPcns1hV5nihli0utFcYgNBlrmHI', '2024-06-03 09:54:57.895311', 'D7Hv+qrfoevWPfK/XQU7ew==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 6, 8);
INSERT INTO `notifications` VALUES (23, 'SCEa06DY1lq9AyEnsmO6gfAWz2XaFXz6EsMdOPcns1hV5nihli0utFcYgNBlrmHI', '2024-06-03 09:55:02.084804', 'D7Hv+qrfoevWPfK/XQU7ew==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 6, 11);
INSERT INTO `notifications` VALUES (24, 'oy7c2anaEstBYn7K7DsLLmIpx36M/hv25yzAi6MPKzLH+O1aQc4xXxafzAejbQCj', '2024-06-03 09:56:16.068134', 'RCiOPsc7tvVRGdWF52BvHw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 7, 3);
INSERT INTO `notifications` VALUES (25, 'oy7c2anaEstBYn7K7DsLLmIpx36M/hv25yzAi6MPKzLH+O1aQc4xXxafzAejbQCj', '2024-06-03 09:56:20.170624', 'RCiOPsc7tvVRGdWF52BvHw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 7, 6);
INSERT INTO `notifications` VALUES (26, 'oy7c2anaEstBYn7K7DsLLmIpx36M/hv25yzAi6MPKzLH+O1aQc4xXxafzAejbQCj', '2024-06-03 09:56:23.235472', 'RCiOPsc7tvVRGdWF52BvHw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 7, 10);
INSERT INTO `notifications` VALUES (27, 'oy7c2anaEstBYn7K7DsLLmIpx36M/hv25yzAi6MPKzLH+O1aQc4xXxafzAejbQCj', '2024-06-03 09:56:27.105762', 'RCiOPsc7tvVRGdWF52BvHw==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 7, 11);
INSERT INTO `notifications` VALUES (28, 'V9GhFMtxOwmJOljb/wz6pr02H7OLwyEO1FufUE1slgwg/XK6ba+OcYYA8GoW2B9k', '2024-06-03 09:57:55.036835', 'AMkke9PVQ9vurqoEKxcBXQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 8, 4);
INSERT INTO `notifications` VALUES (29, 'V9GhFMtxOwmJOljb/wz6pr02H7OLwyEO1FufUE1slgwg/XK6ba+OcYYA8GoW2B9k', '2024-06-03 09:57:57.533128', 'AMkke9PVQ9vurqoEKxcBXQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 8, 5);
INSERT INTO `notifications` VALUES (30, 'V9GhFMtxOwmJOljb/wz6pr02H7OLwyEO1FufUE1slgwg/XK6ba+OcYYA8GoW2B9k', '2024-06-03 09:58:03.382718', 'AMkke9PVQ9vurqoEKxcBXQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 8, 7);
INSERT INTO `notifications` VALUES (31, 'V9GhFMtxOwmJOljb/wz6pr02H7OLwyEO1FufUE1slgwg/XK6ba+OcYYA8GoW2B9k', '2024-06-03 09:58:07.415033', 'AMkke9PVQ9vurqoEKxcBXQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 8, 11);
INSERT INTO `notifications` VALUES (32, 'vvJLqh6cvUsA9PstGCkTGWiFRTyOdFZBqTOQwMH1hhGTrWXylBaeSedsXzXQPzSg', '2024-06-03 09:58:55.172919', 'kn1f+CNrmPktirrtJIcRpQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 9, 8);
INSERT INTO `notifications` VALUES (33, 'vvJLqh6cvUsA9PstGCkTGWiFRTyOdFZBqTOQwMH1hhGTrWXylBaeSedsXzXQPzSg', '2024-06-03 09:58:59.879136', 'kn1f+CNrmPktirrtJIcRpQ==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 9, 11);
INSERT INTO `notifications` VALUES (34, '1GCnuk6Vl86286Mi7/2yAmfpzzZU0UUl694StdXJsWfQJ9vT9C70+psU1AQyn/DP', '2024-06-03 09:59:35.277075', 'Fo+wVz+Bm+jS6BWF4iYu1g==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 10, 8);
INSERT INTO `notifications` VALUES (35, '1GCnuk6Vl86286Mi7/2yAmfpzzZU0UUl694StdXJsWfQJ9vT9C70+psU1AQyn/DP', '2024-06-03 09:59:38.923214', 'Fo+wVz+Bm+jS6BWF4iYu1g==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 10, 11);
INSERT INTO `notifications` VALUES (36, 'zhJN36gDWa7uHahL+3yxN5GFcoE0H3aFnk5Y80mAOLMWaHNWoojqdJRu8vyesBXX', '2024-06-03 10:00:03.051907', 'WqbCRf8/5kXMgk55Os6iog==', 'gEFAW3uMydOrWMlDLkPyMs2M91bjwjVi63lrc4LYNJk=', 2, 11, 3);
INSERT INTO `notifications` VALUES (37, 'uInUavPNkFbv9+rZ16kkV4RbdnoKASSzcrcK8kyrpVy63xWpP6FKQHvGh1CM2BV7', '2024-06-03 10:11:53.758505', 'j6NA7GAkqntIv9O4Qb9JJw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 3, 2);
INSERT INTO `notifications` VALUES (38, 'uInUavPNkFbv9+rZ16kkV4RbdnoKASSzcrcK8kyrpVy63xWpP6FKQHvGh1CM2BV7', '2024-06-03 10:12:14.258668', 'j6NA7GAkqntIv9O4Qb9JJw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 3, 5);
INSERT INTO `notifications` VALUES (39, 'uInUavPNkFbv9+rZ16kkV4RbdnoKASSzcrcK8kyrpVy63xWpP6FKQHvGh1CM2BV7', '2024-06-03 10:12:21.911964', 'j6NA7GAkqntIv9O4Qb9JJw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 3, 7);
INSERT INTO `notifications` VALUES (40, 'uInUavPNkFbv9+rZ16kkV4RbdnoKASSzcrcK8kyrpVy63xWpP6FKQHvGh1CM2BV7', '2024-06-03 10:12:22.615307', 'j6NA7GAkqntIv9O4Qb9JJw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 3, 11);
INSERT INTO `notifications` VALUES (41, 'VSHvGn/yMUaGXRvJ7tj4iF6N6aLSMGHFRuhIEDvzrnTmY11tG/m/OAnwlYFW1MLq', '2024-06-03 10:12:35.577690', '3jgSc9LHYM7UvEbfw5Y4/A==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 4, 8);
INSERT INTO `notifications` VALUES (42, 'VSHvGn/yMUaGXRvJ7tj4iF6N6aLSMGHFRuhIEDvzrnTmY11tG/m/OAnwlYFW1MLq', '2024-06-03 10:12:36.228002', '3jgSc9LHYM7UvEbfw5Y4/A==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 4, 3);
INSERT INTO `notifications` VALUES (43, 'VSHvGn/yMUaGXRvJ7tj4iF6N6aLSMGHFRuhIEDvzrnTmY11tG/m/OAnwlYFW1MLq', '2024-06-03 10:12:36.867752', '3jgSc9LHYM7UvEbfw5Y4/A==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 4, 2);
INSERT INTO `notifications` VALUES (44, 'DPFqyBxPAd3okhzQAubN9BEaQ6JDqUYOUmeZuoqH/4unnyU2T2AwSJ612CXBGTYh', '2024-06-03 10:12:48.187627', 'bDlImNTox3xX5ez+LNosqw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 5, 8);
INSERT INTO `notifications` VALUES (45, 'DPFqyBxPAd3okhzQAubN9BEaQ6JDqUYOUmeZuoqH/4unnyU2T2AwSJ612CXBGTYh', '2024-06-03 10:12:49.077658', 'bDlImNTox3xX5ez+LNosqw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 5, 2);
INSERT INTO `notifications` VALUES (46, 'SCEa06DY1lq9AyEnsmO6gUvizSOz+4abwPwph1F8LXusINCv6GI2j27ahFfzgRCBFzplrt6T0D2maMiebrTDvA==', '2024-06-03 10:12:58.868051', 'D7Hv+qrfoevWPfK/XQU7ew==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 6, 7);
INSERT INTO `notifications` VALUES (47, 'SCEa06DY1lq9AyEnsmO6gUvizSOz+4abwPwph1F8LXusINCv6GI2j27ahFfzgRCBFzplrt6T0D2maMiebrTDvA==', '2024-06-03 10:12:59.767669', 'D7Hv+qrfoevWPfK/XQU7ew==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 6, 4);
INSERT INTO `notifications` VALUES (48, 'SCEa06DY1lq9AyEnsmO6gUvizSOz+4abwPwph1F8LXusINCv6GI2j27ahFfzgRCBFzplrt6T0D2maMiebrTDvA==', '2024-06-03 10:13:00.997805', 'D7Hv+qrfoevWPfK/XQU7ew==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 6, 2);
INSERT INTO `notifications` VALUES (49, 'SCEa06DY1lq9AyEnsmO6gUvizSOz+4abwPwph1F8LXusINCv6GI2j27ahFfzgRCBFzplrt6T0D2maMiebrTDvA==', '2024-06-03 10:13:01.667853', 'D7Hv+qrfoevWPfK/XQU7ew==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 6, 3);
INSERT INTO `notifications` VALUES (50, 'oy7c2anaEstBYn7K7DsLLtejrVJYUmEeFmP7xu1OagX83iAbA0ev/tt2T4IEj0i6', '2024-06-03 10:13:10.852528', 'RCiOPsc7tvVRGdWF52BvHw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 7, 8);
INSERT INTO `notifications` VALUES (51, 'oy7c2anaEstBYn7K7DsLLtejrVJYUmEeFmP7xu1OagX83iAbA0ev/tt2T4IEj0i6', '2024-06-03 10:13:11.477579', 'RCiOPsc7tvVRGdWF52BvHw==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 7, 4);
INSERT INTO `notifications` VALUES (52, 'V9GhFMtxOwmJOljb/wz6pnfH+xHzndD8WsB1ofhV+cc5twnmXoCht6VCioME00Uu', '2024-06-03 10:13:21.307524', 'AMkke9PVQ9vurqoEKxcBXQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 8, 10);
INSERT INTO `notifications` VALUES (53, 'V9GhFMtxOwmJOljb/wz6pnfH+xHzndD8WsB1ofhV+cc5twnmXoCht6VCioME00Uu', '2024-06-03 10:13:22.147451', 'AMkke9PVQ9vurqoEKxcBXQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 8, 9);
INSERT INTO `notifications` VALUES (54, 'V9GhFMtxOwmJOljb/wz6pnfH+xHzndD8WsB1ofhV+cc5twnmXoCht6VCioME00Uu', '2024-06-03 10:13:22.787441', 'AMkke9PVQ9vurqoEKxcBXQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 8, 6);
INSERT INTO `notifications` VALUES (55, 'V9GhFMtxOwmJOljb/wz6pnfH+xHzndD8WsB1ofhV+cc5twnmXoCht6VCioME00Uu', '2024-06-03 10:13:23.977497', 'AMkke9PVQ9vurqoEKxcBXQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 8, 3);
INSERT INTO `notifications` VALUES (56, 'V9GhFMtxOwmJOljb/wz6pnfH+xHzndD8WsB1ofhV+cc5twnmXoCht6VCioME00Uu', '2024-06-03 10:13:24.499277', 'AMkke9PVQ9vurqoEKxcBXQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 8, 2);
INSERT INTO `notifications` VALUES (57, 'vvJLqh6cvUsA9PstGCkTGbZa3kdjTYdo4nWa2lRV2glf/j7hRq7vfPPGUS7XE6MFzYTo7SDeMncPuvtjTQ1AxA==', '2024-06-03 10:13:32.637778', 'kn1f+CNrmPktirrtJIcRpQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 9, 5);
INSERT INTO `notifications` VALUES (58, 'vvJLqh6cvUsA9PstGCkTGbZa3kdjTYdo4nWa2lRV2glf/j7hRq7vfPPGUS7XE6MFzYTo7SDeMncPuvtjTQ1AxA==', '2024-06-03 10:13:33.198651', 'kn1f+CNrmPktirrtJIcRpQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 9, 4);
INSERT INTO `notifications` VALUES (59, 'vvJLqh6cvUsA9PstGCkTGbZa3kdjTYdo4nWa2lRV2glf/j7hRq7vfPPGUS7XE6MFzYTo7SDeMncPuvtjTQ1AxA==', '2024-06-03 10:13:33.841128', 'kn1f+CNrmPktirrtJIcRpQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 9, 3);
INSERT INTO `notifications` VALUES (60, 'vvJLqh6cvUsA9PstGCkTGbZa3kdjTYdo4nWa2lRV2glf/j7hRq7vfPPGUS7XE6MFzYTo7SDeMncPuvtjTQ1AxA==', '2024-06-03 10:13:34.799847', 'kn1f+CNrmPktirrtJIcRpQ==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 9, 2);
INSERT INTO `notifications` VALUES (61, 'vEjak8TD/BluGet7We+1nJuN63XBmTCCEeKJejPsVxt/vIEug4hnPeBk8kgW+7G8', '2024-06-03 10:13:44.087402', 'Fo+wVz+Bm+jS6BWF4iYu1g==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 10, 7);
INSERT INTO `notifications` VALUES (62, 'vEjak8TD/BluGet7We+1nJuN63XBmTCCEeKJejPsVxt/vIEug4hnPeBk8kgW+7G8', '2024-06-03 10:13:44.667362', 'Fo+wVz+Bm+jS6BWF4iYu1g==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 10, 5);
INSERT INTO `notifications` VALUES (63, 'vEjak8TD/BluGet7We+1nJuN63XBmTCCEeKJejPsVxt/vIEug4hnPeBk8kgW+7G8', '2024-06-03 10:13:45.423651', 'Fo+wVz+Bm+jS6BWF4iYu1g==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 10, 2);
INSERT INTO `notifications` VALUES (64, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:13:56.474511', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 11, 10);
INSERT INTO `notifications` VALUES (65, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:13:57.039887', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 11, 9);
INSERT INTO `notifications` VALUES (66, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:13:58.397676', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 11, 8);
INSERT INTO `notifications` VALUES (67, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:13:58.842638', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 11, 7);
INSERT INTO `notifications` VALUES (68, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:14:00.322041', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 2, 11, 5);
INSERT INTO `notifications` VALUES (69, 'zhJN36gDWa7uHahL+3yxN/oan78l28QejqVKmOICOhENi2JOwFGqizSlU7N8Uc6+FAiT2bnjIu1TaZD7Rbwf3g==', '2024-06-03 10:14:01.029936', 'WqbCRf8/5kXMgk55Os6iog==', 'wQKv+Akmj6ic6iUMHWuRuYKp+zesS2fkDFGfz+HW1GE=', 0, 11, 4);
INSERT INTO `notifications` VALUES (70, 'i+G0PZDjzRTNOY99EgJbHT0H6HpnZBkVzv+WNKUC9LLPrqDeNhCcIRuEyKOK8+TU', '2024-06-03 10:51:37.627822', 'Tf5xqpFfvDDi0o6hn0z4m6KnGETxxEptHgESBfJylHA=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 2, 1);
INSERT INTO `notifications` VALUES (71, 'i+G0PZDjzRTNOY99EgJbHfBdBYNyusu9t5Nr0tgMcGR+sWMuMmaWkhmDnnYFBxnm', '2024-06-03 10:51:40.731390', 'nbylJnoRzP+VYeHEJvFfdJodb34yLuLMSVIPYV3X5bM=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 2, 1);
INSERT INTO `notifications` VALUES (72, 'i+G0PZDjzRTNOY99EgJbHZcjKK8b4foaj8CM9rVxMt/Q7wgtX9vCx8Z4w23oLTG2', '2024-06-03 10:51:44.250896', 'hJCwE0B1RzlHRQypKW/+4u8ZPPCLgXZWYTbh/5u3Bn8=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 2, 1);
INSERT INTO `notifications` VALUES (73, 'i+G0PZDjzRTNOY99EgJbHZIoPBEDGzFrh69ldMsEVZht+nY1bHkzHBznc1E9xXad', '2024-06-03 10:51:45.972096', 'x2o4t2fwXjUWfvt/sW4OwPdgnS6jHklS9rZkUZVLLkA=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 2, 1);
INSERT INTO `notifications` VALUES (74, 'i+G0PZDjzRTNOY99EgJbHSgawe8zGGH7EQBnzjTAfKKh9QtEPZwuLpw5A+dTxItb', '2024-06-03 10:51:48.959673', '76SQGeqlTWZN5GNsJbs5Fcbq6DGPlzaK4mUJTJCF8mw=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 2, 1);
INSERT INTO `notifications` VALUES (75, 'Wny/HdUsXr/4eu3StUwWBixnkhwiExp1C/hYpnLdRU8TGSoDCzAarrUcvIRzM6+Z', '2024-06-03 10:52:12.851636', 'hJCwE0B1RzlHRQypKW/+4rGKZ+hOBq/VYL6AxvUz5ig=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 3, 1);
INSERT INTO `notifications` VALUES (76, 'Wny/HdUsXr/4eu3StUwWBihZW+iKgahoqgESyQ3tqus2OllrcJdsSMpoM1C/3/nd', '2024-06-03 10:52:15.318149', 'x2o4t2fwXjUWfvt/sW4OwPAjb4VEDwRb9jS2blrf24s=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 3, 1);
INSERT INTO `notifications` VALUES (77, 'Wny/HdUsXr/4eu3StUwWBrroGpE/V6HfJXc44p7Z+sBN5q42O9uEbT06xti3/jtH7gg+FP8jvPbHXvusuV1AKw==', '2024-06-03 10:52:18.905936', 'DNaxStSE2JbonVjrNdOKQJvrofiCJ6MlTB7nzhquxDk=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 3, 1);
INSERT INTO `notifications` VALUES (78, 'Wny/HdUsXr/4eu3StUwWBtLBa4MkfzN+5uaQHAL5FDmlcPoHaw+GONsctzwipAho', '2024-06-03 10:52:22.144243', '76SQGeqlTWZN5GNsJbs5FYfJH8GEDPJQkcd5T0AHlOM=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 3, 1);
INSERT INTO `notifications` VALUES (79, 'O+q4lTtN7FFBJ978CUyQgPAU5LOZx2MGXAhQzaULb8eUODfAD7IlQW3IfIaRTRK5GspUXH7kPf5pnVhUmfbDTQ==', '2024-06-03 10:52:41.341977', 'hJCwE0B1RzlHRQypKW/+4mLWIXyes6eSQ1lLFSra6i0=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 4, 1);
INSERT INTO `notifications` VALUES (80, 'O+q4lTtN7FFBJ978CUyQgMulLPsI4+mPE386c3RC9khvHlGh6QKtyAOC8ce9uDweGoNfuikfAzgqNgS0XLQ0cw==', '2024-06-03 10:52:44.865133', 'DNaxStSE2JbonVjrNdOKQOyvsWIzKzWYzGEqrDLhl3w=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 4, 1);
INSERT INTO `notifications` VALUES (81, 'O+q4lTtN7FFBJ978CUyQgLAHtYDXgcr9Us34T5NrxuuvCJt3dN78TtjYKbsj7RTs', '2024-06-03 10:52:48.103722', '76SQGeqlTWZN5GNsJbs5FUoTMWDe/4X+y8utVxP9D/0=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 4, 1);
INSERT INTO `notifications` VALUES (82, '0ksYh0mC92W+Rg8qn7OslQF9ASkIuRUM7vy+qRBJoh1KpHjEHfXfgxooces0c6z/', '2024-06-03 10:53:18.331858', 'Tf5xqpFfvDDi0o6hn0z4m8ohpZ88O88kWKlG+GjIcOA=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 5, 1);
INSERT INTO `notifications` VALUES (83, '0ksYh0mC92W+Rg8qn7Osla3ap1ArK82o3nmW86DPRRs6KflqNR99/Pe0AkxEjjAA', '2024-06-03 10:53:21.121014', 'hJCwE0B1RzlHRQypKW/+4rAM8x7oWNK+EaQFEglWqbE=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 5, 1);
INSERT INTO `notifications` VALUES (84, '0ksYh0mC92W+Rg8qn7OslX2jlJsI0GHGB6AE5mhjOwr5bKEeg7bSQ2Zo46WFC6Z4', '2024-06-03 10:53:23.596259', 'x2o4t2fwXjUWfvt/sW4OwAWsNM3Hix3iCHxKwGp3dHA=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 5, 1);
INSERT INTO `notifications` VALUES (85, 'SCEa06DY1lq9AyEnsmO6gZPyD4RFntUzcSxDzZaQY70Q7x04S+QIGpvPkn2n/DEX', '2024-06-03 10:53:36.810161', 'nbylJnoRzP+VYeHEJvFfdAjN5SCtRLkABhYH2/uz6r0=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 6, 1);
INSERT INTO `notifications` VALUES (86, 'SCEa06DY1lq9AyEnsmO6gRBvxja607lX4FmDPiS1ez6dxmsm8eVbjy9OJrLTdBlGeKpXUwFjMW6y4qfDPaZraw==', '2024-06-03 10:53:38.688989', 'hJCwE0B1RzlHRQypKW/+4nLkb53JJT4cx0sqIRTjXtU=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 6, 1);
INSERT INTO `notifications` VALUES (87, 'SCEa06DY1lq9AyEnsmO6gXcaiWdI/5xTbYv9xiozc3lvKizIqfKNgcI3zFmIrbBVeDsoXzIX3Rl3cFRDryzMmQ==', '2024-06-03 10:53:40.363266', 'eJS+j0DadqGAfiS52Qvqp0TWHPeO0IHmrhbgpvSXTnc=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 6, 1);
INSERT INTO `notifications` VALUES (88, 'SCEa06DY1lq9AyEnsmO6gWXzwBPUq4qS3qv1ywYG0b0Z+5upWPabgdFHC95wFl+m5acgB7ens+bTUtLltg/0eQ==', '2024-06-03 10:53:41.941752', 'x2o4t2fwXjUWfvt/sW4OwBZYhoWLHGoiubMx1GLAaG4=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 6, 1);
INSERT INTO `notifications` VALUES (89, 'SCEa06DY1lq9AyEnsmO6gYyuMaESzk0fkHf/hyS9Ar5nMRtMJyA+Bx1i8V8oVrC+GIFJ0sdS8PoGsbBnepKNAw==', '2024-06-03 10:53:45.767020', 'DNaxStSE2JbonVjrNdOKQF/O0FVGOal9YU3MSYaoyNQ=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 6, 1);
INSERT INTO `notifications` VALUES (90, 'TE8eFYDSGYzVlptbCH2J8l1K3/r8trxSx0yhkEIV6VgrabLe/2HLJxMMU5dhA3Am', '2024-06-03 10:54:48.816688', 'nbylJnoRzP+VYeHEJvFfdMqNpI2dT4nL+3HcdjKcUd0=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 7, 1);
INSERT INTO `notifications` VALUES (91, 'TE8eFYDSGYzVlptbCH2J8qvGYroEKPKWrtZlPezhS1XoqjULXRu/8s5dfiGIaYhw', '2024-06-03 10:54:51.179863', 'x2o4t2fwXjUWfvt/sW4OwO1U/UESmIzIOFD0CTuWa38=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 7, 1);
INSERT INTO `notifications` VALUES (92, 'TE8eFYDSGYzVlptbCH2J8hyfVh+B8y2IOggiog3l4n8FoEMOmMRkMtMhTyqdrCPA', '2024-06-03 10:54:54.666578', '76SQGeqlTWZN5GNsJbs5Fc8y2VnQVeWBl0kI3zuQxbg=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 7, 1);
INSERT INTO `notifications` VALUES (93, 'NQ/o18i4izKsBMtkctCM3jvR7NJg9bI2zME81m4UCN+bRu/Aok7C5h8Mp0f06hbI', '2024-06-03 10:55:17.947936', 'nbylJnoRzP+VYeHEJvFfdFFUvfZqUukFXPzkFUD59/U=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 1);
INSERT INTO `notifications` VALUES (94, 'NQ/o18i4izKsBMtkctCM3mbN92llfK+CidytT9kxORHdMrF4Jole4JgK2QhlEh7e', '2024-06-03 10:55:22.708576', 'x2o4t2fwXjUWfvt/sW4OwE8UDS/NyOKA5Z1X5RfND4Q=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 1);
INSERT INTO `notifications` VALUES (95, 'NQ/o18i4izKsBMtkctCM3js5c96aIIY8XlEmlDVzSbop6/+BjTuB5azfOfc16xLFJQdmAMVOinEzgTsM68Btyw==', '2024-06-03 10:55:25.246047', 'DNaxStSE2JbonVjrNdOKQNwWlCufe95lQuhmD5AvyKg=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 1);
INSERT INTO `notifications` VALUES (96, 'NQ/o18i4izKsBMtkctCM3jg3/ua3ZyNA9XsLpkHA1DTk4OA4UY+3efA9Koh5dcSJ', '2024-06-03 10:55:30.661669', '76SQGeqlTWZN5GNsJbs5FYunbOiVEUlYuWQF6Q4y/bk=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 1);
INSERT INTO `notifications` VALUES (97, 'NQ/o18i4izKsBMtkctCM3rBMVfdrfITxacjcnTQ1vE1QEKen8bm/0nDWVPRw8roO2mLpFNQXdd6gVlleL9kc3w==', '2024-06-03 10:55:33.192684', 'crtVetMpEOn1PhmfXNMM2nGAhuBTbLOFwoKRprLRaUw=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 1);
INSERT INTO `notifications` VALUES (98, 'NQ/o18i4izKsBMtkctCM3rBMVfdrfITxacjcnTQ1vE1QEKen8bm/0nDWVPRw8roO2mLpFNQXdd6gVlleL9kc3w==', '2024-06-03 10:55:33.194687', 'crtVetMpEOn1PhmfXNMM2nGAhuBTbLOFwoKRprLRaUw=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 8, 2);
INSERT INTO `notifications` VALUES (99, 'aDPzf4rLqbkSa1UTDRm/xyj+IaceuQY6+D3JltU9mkypJ1HnvMs1Ge+E0ZzIKzpj', '2024-06-03 10:56:11.587355', 'Tf5xqpFfvDDi0o6hn0z4mwoyHDuel/q7Urodj2xdGNM=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 9, 1);
INSERT INTO `notifications` VALUES (100, 'aDPzf4rLqbkSa1UTDRm/x5XyaUgO0LA/hP0god7TFumFo4MRCqSMRmlmc9TFOD1b', '2024-06-03 10:56:13.500841', 'nbylJnoRzP+VYeHEJvFfdKlbhp8euOXDKPzDMJGo4fY=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 9, 1);
INSERT INTO `notifications` VALUES (101, 'aDPzf4rLqbkSa1UTDRm/xzEkyWMVf6AXB6O6SMmd04/IfkbgTSHEekg5RCC0Lo/Izk166aJVkoJVR3ZlIMNjTw==', '2024-06-03 10:56:15.186115', 'hJCwE0B1RzlHRQypKW/+4hCkoMAV/dUmsVutfndeHKI=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 9, 1);
INSERT INTO `notifications` VALUES (102, 'aDPzf4rLqbkSa1UTDRm/x59m3PZbaRO6z5nRBrhGwRKYm3Coy8A2TNgaaimeC2Sdsniqe/L3M6UvVNazMEsvwA==', '2024-06-03 10:56:16.954821', 'eJS+j0DadqGAfiS52Qvqp2XNpRg6osNgeJBmORPynk4=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 9, 1);
INSERT INTO `notifications` VALUES (103, 'aDPzf4rLqbkSa1UTDRm/x83CXhw1BCacCZ11cFhlDuUbzK4zVm8Gs5Gp3Gc929fr', '2024-06-03 10:56:19.387089', '76SQGeqlTWZN5GNsJbs5FeabnAd8H++RuC7B7ZNFymU=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 9, 1);
INSERT INTO `notifications` VALUES (104, 'iZTz3d3Qp7owLT5ko3Qu2R8D9d0BMcfpNtwm66hsG24zghJh5y/79zWLYjvfIJog+/eySaKIBUcK0MRSYzOGmw==', '2024-06-03 10:56:35.263143', 'DNaxStSE2JbonVjrNdOKQPByiVw+towXTqeL/lvS1q4=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 10, 1);
INSERT INTO `notifications` VALUES (105, 'iZTz3d3Qp7owLT5ko3Qu2cg+qXE4IGXu9dPPOszQZuwnJ+QkU2qeGcrnaSt1ldU5jzipIMzf05aL4marWv9nBA==', '2024-06-03 10:56:37.935820', 'crtVetMpEOn1PhmfXNMM2qm4Mj3ygIXIxxF93BV787I=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 10, 1);
INSERT INTO `notifications` VALUES (106, 'iZTz3d3Qp7owLT5ko3Qu2cg+qXE4IGXu9dPPOszQZuwnJ+QkU2qeGcrnaSt1ldU5jzipIMzf05aL4marWv9nBA==', '2024-06-03 10:56:37.937819', 'crtVetMpEOn1PhmfXNMM2qm4Mj3ygIXIxxF93BV787I=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 10, 2);
INSERT INTO `notifications` VALUES (107, 'zhJN36gDWa7uHahL+3yxNyfGQ7bdetlkcDKXuVFfb5WL3c4Hb3lfgj3hlVC8UCkM', '2024-06-03 10:56:58.027399', 'Tf5xqpFfvDDi0o6hn0z4m0Zj1LjtJ7W/jkjnzhZG6vk=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 11, 1);
INSERT INTO `notifications` VALUES (108, 'zhJN36gDWa7uHahL+3yxN5l2AAOTB5svrutCeps1NsY3yJgbgMmCykMLYDQE2phHGjKyMuFOmDFmgKfoYokjsw==', '2024-06-03 10:57:00.120684', 'eJS+j0DadqGAfiS52Qvqp6XKcl4eas6Oxl8lA5YbGo4=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 11, 1);
INSERT INTO `notifications` VALUES (109, 'zhJN36gDWa7uHahL+3yxN4tjAo5J2vIhLqJQpdJykGQxCYgNzo/1U+ldgAU9RXRQBE13JGU9zOkFeaqy9+iExA==', '2024-06-03 10:57:03.206268', 'DNaxStSE2JbonVjrNdOKQMAtJKcebeIEvhA2YkIbvgs=', 'TnFhK44SaB6cXhRS2GVYVg==', 2, 11, 1);
INSERT INTO `notifications` VALUES (110, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:57:19.641499', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 11);
INSERT INTO `notifications` VALUES (111, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHrQfLygEqhkbLUpDM8XnN/cmVYddD0Kff1gyRm3DHvIaA==', '2024-06-03 10:57:20.433165', '3P9jun6mSJY6L0ZMT+l75w==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 15, 11);
INSERT INTO `notifications` VALUES (112, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHoaynjpUc1QHZ/8UQWAYvDSKJsZJ6sNjvjEUKehq7IK8g==', '2024-06-03 10:57:21.142132', 'DmBdJnwLy4cqFKjs9qKB8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 12, 11);
INSERT INTO `notifications` VALUES (113, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHror4RfyXBWmhZAShR12/Q7o7tetCj4iY+9GadmTP1jBA==', '2024-06-03 10:57:22.000875', '0RDvcBCi9QFR9TyMmMGk8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 20, 10);
INSERT INTO `notifications` VALUES (114, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:57:22.617874', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 10);
INSERT INTO `notifications` VALUES (115, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:57:24.200701', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 9);
INSERT INTO `notifications` VALUES (116, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:57:26.568169', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 9);
INSERT INTO `notifications` VALUES (117, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHrQfLygEqhkbLUpDM8XnN/cmVYddD0Kff1gyRm3DHvIaA==', '2024-06-03 10:57:27.190864', '3P9jun6mSJY6L0ZMT+l75w==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 15, 9);
INSERT INTO `notifications` VALUES (118, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHppDxnVmaUwOWKrwDESJUbk', '2024-06-03 10:57:27.916338', '5cJ/RwInkIDcLFDMHn7sEw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 13, 9);
INSERT INTO `notifications` VALUES (119, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHoaynjpUc1QHZ/8UQWAYvDSKJsZJ6sNjvjEUKehq7IK8g==', '2024-06-03 10:57:28.885919', 'DmBdJnwLy4cqFKjs9qKB8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 12, 9);
INSERT INTO `notifications` VALUES (120, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHror4RfyXBWmhZAShR12/Q7o7tetCj4iY+9GadmTP1jBA==', '2024-06-03 10:57:29.532942', '0RDvcBCi9QFR9TyMmMGk8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 20, 8);
INSERT INTO `notifications` VALUES (121, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:57:30.621224', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 8);
INSERT INTO `notifications` VALUES (122, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:57:31.790140', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 8);
INSERT INTO `notifications` VALUES (123, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:57:32.421038', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 8);
INSERT INTO `notifications` VALUES (124, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHppDxnVmaUwOWKrwDESJUbk', '2024-06-03 10:57:33.517169', '5cJ/RwInkIDcLFDMHn7sEw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 13, 8);
INSERT INTO `notifications` VALUES (125, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:57:34.093902', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 7);
INSERT INTO `notifications` VALUES (126, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:57:34.702129', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 7);
INSERT INTO `notifications` VALUES (127, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHppDxnVmaUwOWKrwDESJUbk', '2024-06-03 10:57:35.816969', '5cJ/RwInkIDcLFDMHn7sEw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 13, 7);
INSERT INTO `notifications` VALUES (128, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:57:36.767564', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 6);
INSERT INTO `notifications` VALUES (129, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:57:37.616621', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 6);
INSERT INTO `notifications` VALUES (130, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHrQfLygEqhkbLUpDM8XnN/cmVYddD0Kff1gyRm3DHvIaA==', '2024-06-03 10:58:11.632087', '3P9jun6mSJY6L0ZMT+l75w==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 15, 6);
INSERT INTO `notifications` VALUES (131, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:58:12.500422', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 6);
INSERT INTO `notifications` VALUES (132, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHppDxnVmaUwOWKrwDESJUbk', '2024-06-03 10:58:13.194347', '5cJ/RwInkIDcLFDMHn7sEw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 13, 6);
INSERT INTO `notifications` VALUES (133, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:58:14.036352', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 5);
INSERT INTO `notifications` VALUES (134, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:58:14.716728', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 5);
INSERT INTO `notifications` VALUES (135, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHoaynjpUc1QHZ/8UQWAYvDSKJsZJ6sNjvjEUKehq7IK8g==', '2024-06-03 10:58:15.756696', 'DmBdJnwLy4cqFKjs9qKB8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 12, 5);
INSERT INTO `notifications` VALUES (136, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:58:16.395927', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 4);
INSERT INTO `notifications` VALUES (137, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:58:17.103439', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 4);
INSERT INTO `notifications` VALUES (138, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:58:18.373668', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 4);
INSERT INTO `notifications` VALUES (139, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:58:19.447766', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 3);
INSERT INTO `notifications` VALUES (140, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHpF2waNWtv8H77C80yXVYZgnMkpnQbEC+idr0UWqWZE2Q==', '2024-06-03 10:58:20.473958', 'ygimkMW9eHad0PULOEwAGw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 18, 3);
INSERT INTO `notifications` VALUES (141, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:58:21.232401', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 3);
INSERT INTO `notifications` VALUES (142, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:58:22.210628', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 3);
INSERT INTO `notifications` VALUES (143, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq5VyPOFB5S6QICpKEFeWJarXB73/mvPbPAoQgn8CMKUQ==', '2024-06-03 10:58:22.801836', 'I1QOFIZa+B/EQMcAz52GQQ==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 19, 2);
INSERT INTO `notifications` VALUES (144, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHq+ny6W5/ln5wfga3cGhaKONz1fZnLJ+IJZ5RIGppCoMw==', '2024-06-03 10:58:23.643935', 'lK+amkdjqva28dhv67rkGA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 16, 2);
INSERT INTO `notifications` VALUES (145, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHo15oZQYl2qlTw4qTVVfQIHLjl/MnyaTnz1FvWAle1nEg==', '2024-06-03 10:58:25.048138', 'XmSwpulpk+sl6ONd3Dr6CA==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 14, 2);
INSERT INTO `notifications` VALUES (146, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHppDxnVmaUwOWKrwDESJUbk', '2024-06-03 10:58:25.647220', '5cJ/RwInkIDcLFDMHn7sEw==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 13, 2);
INSERT INTO `notifications` VALUES (147, 'vfjOTh1liHfNJZie8IAYzgq0cj/uq3afA3YL5yl3cHoaynjpUc1QHZ/8UQWAYvDSKJsZJ6sNjvjEUKehq7IK8g==', '2024-06-03 10:58:26.532152', 'DmBdJnwLy4cqFKjs9qKB8g==', 'zVwOjVROp+bxovnfZC7d1ariYcBXjvixVhycNde57Tk=', 0, 12, 2);

-- ----------------------------
-- Table structure for refresh_tokens
-- ----------------------------
DROP TABLE IF EXISTS `refresh_tokens`;
CREATE TABLE `refresh_tokens`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `expired_date` date NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_ghpmfn23vmxfu3spu3lfg4r2d`(`token` ASC) USING BTREE,
  INDEX `FK1lih5y2npsf8u5o3vhdb9y0os`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK1lih5y2npsf8u5o3vhdb9y0os` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refresh_tokens
-- ----------------------------
INSERT INTO `refresh_tokens` VALUES (1, '2024-07-03', 0, '93c4d8d3-7819-4bec-b92e-847efcc80c1e', 1);
INSERT INTO `refresh_tokens` VALUES (2, '2024-07-03', 0, 'd9ea8098-ea73-4199-95b5-1038204d47c5', 1);
INSERT INTO `refresh_tokens` VALUES (3, '2024-07-03', 0, '1de67e43-0fdc-4d7c-adad-0dd0e85b5052', 1);
INSERT INTO `refresh_tokens` VALUES (4, '2024-07-03', 0, 'ea344aca-0523-44f7-80df-a8b6c8eb5cd8', 2);
INSERT INTO `refresh_tokens` VALUES (5, '2024-07-03', 0, '611d7407-d7a0-4724-b876-615abb94f028', 2);
INSERT INTO `refresh_tokens` VALUES (6, '2024-07-03', 0, 'a573e42b-20ad-4e64-89f1-c4d6463f2de1', 3);
INSERT INTO `refresh_tokens` VALUES (7, '2024-07-03', 0, 'e123ee65-88dd-4f29-96a6-a2c61cc54a8b', 3);
INSERT INTO `refresh_tokens` VALUES (8, '2024-07-03', 0, 'c0d25b62-006e-41b1-a19f-35b7a10f3a3d', 4);
INSERT INTO `refresh_tokens` VALUES (9, '2024-07-03', 0, '48c3e0f2-d685-447c-8a96-50aa83499066', 5);
INSERT INTO `refresh_tokens` VALUES (10, '2024-07-03', 0, 'a487967c-211f-46a2-a321-e953ce946d50', 6);
INSERT INTO `refresh_tokens` VALUES (11, '2024-07-03', 0, 'ca1de26b-a70e-41d4-895d-1e276899ac40', 7);
INSERT INTO `refresh_tokens` VALUES (12, '2024-07-03', 0, 'd6eb3edb-f049-4672-8cfa-61f3d47b604b', 8);
INSERT INTO `refresh_tokens` VALUES (13, '2024-07-03', 0, '3ec3ed6b-0d66-4203-9325-1dc26c36c9d3', 8);
INSERT INTO `refresh_tokens` VALUES (14, '2024-07-03', 0, 'eff03450-64c3-4f86-8bff-5571716dda8f', 9);
INSERT INTO `refresh_tokens` VALUES (15, '2024-07-03', 0, 'e8786bd2-6d52-4c71-82d3-ffecc6c827f2', 10);
INSERT INTO `refresh_tokens` VALUES (16, '2024-07-03', 0, '3c3d3e26-5243-4010-8593-c0f57c4a866f', 11);
INSERT INTO `refresh_tokens` VALUES (17, '2024-07-03', 0, '7d80089c-c9e0-4703-9598-846f767eeba4', 1);
INSERT INTO `refresh_tokens` VALUES (18, '2024-07-03', 0, 'b247b933-ca49-4077-a463-cd5fa3e41357', 1);
INSERT INTO `refresh_tokens` VALUES (19, '2024-07-03', 0, '384b55a6-6d41-4879-b653-21091205c672', 1);
INSERT INTO `refresh_tokens` VALUES (20, '2024-07-03', 0, '22f21ca4-4eae-4dc9-b1cb-d4395fdaf792', 2);
INSERT INTO `refresh_tokens` VALUES (21, '2024-07-03', 0, '71ac9aa4-26bb-4514-81c0-c48c3e76cadf', 3);
INSERT INTO `refresh_tokens` VALUES (22, '2024-07-03', 0, '9aa36a1e-f83b-4e8f-940e-f89a7989745b', 4);
INSERT INTO `refresh_tokens` VALUES (23, '2024-07-03', 0, '23c7bbeb-dc74-450c-91b8-91bc84ca2feb', 5);
INSERT INTO `refresh_tokens` VALUES (24, '2024-07-03', 0, '35f10ad5-4a6c-4d4a-8aaa-449c023af9d4', 6);
INSERT INTO `refresh_tokens` VALUES (25, '2024-07-03', 0, 'e0618221-edec-4c63-ac9b-e1b2c442020d', 7);
INSERT INTO `refresh_tokens` VALUES (26, '2024-07-03', 0, '02d14e62-0cfc-4865-b4d9-77850e57e6f1', 8);
INSERT INTO `refresh_tokens` VALUES (27, '2024-07-03', 0, '7b4354c0-e2a3-4d7f-a255-587174a93389', 9);
INSERT INTO `refresh_tokens` VALUES (28, '2024-07-03', 0, 'e8a5d088-8ff9-4588-95f3-ff0d02cf4ede', 10);
INSERT INTO `refresh_tokens` VALUES (29, '2024-07-03', 0, '4167feda-6af6-415f-9a77-a0d7b70f43ab', 11);
INSERT INTO `refresh_tokens` VALUES (30, '2024-07-03', 0, '55bf3d3c-61b2-4741-87a3-e04dbc1f4c0a', 7);
INSERT INTO `refresh_tokens` VALUES (31, '2024-07-03', 0, 'e4f934a1-a263-4b5a-b0c5-93ad85926e7e', 6);
INSERT INTO `refresh_tokens` VALUES (32, '2024-07-03', 0, '1f69337a-f8e3-461f-b959-d32b470a15b1', 1);
INSERT INTO `refresh_tokens` VALUES (33, '2024-07-03', 0, '753383f8-cb1e-49b3-a375-433f7912de69', 6);
INSERT INTO `refresh_tokens` VALUES (34, '2024-07-03', 0, '369b212d-7e96-4cbe-a6e7-30134ab533b7', 1);
INSERT INTO `refresh_tokens` VALUES (35, '2024-07-03', 0, 'b5993c3e-9a6c-408c-90b7-40800400d43c', 2);
INSERT INTO `refresh_tokens` VALUES (36, '2024-07-03', 0, 'c2fecc5a-a3aa-4546-a271-cca10ca22250', 1);
INSERT INTO `refresh_tokens` VALUES (37, '2024-07-03', 0, '5ac92ff3-8fe3-4457-9c83-f98432584fd2', 2);
INSERT INTO `refresh_tokens` VALUES (38, '2024-07-03', 0, '954e5378-74fa-404d-863f-d93fa17303c9', 1);
INSERT INTO `refresh_tokens` VALUES (39, '2024-07-03', 0, '0a58e56f-543b-49d2-a42f-acf375985d2c', 2);
INSERT INTO `refresh_tokens` VALUES (40, '2024-07-03', 1, '7efa56d6-f995-43d9-a74d-b6bc87edcdf4', 3);
INSERT INTO `refresh_tokens` VALUES (41, '2024-07-03', 1, '4f0cc912-f6d2-4b26-9018-982f0a410dc6', 4);
INSERT INTO `refresh_tokens` VALUES (42, '2024-07-03', 1, 'db81be05-2d0f-4d75-b0fe-d1689beaa95c', 5);
INSERT INTO `refresh_tokens` VALUES (43, '2024-07-03', 1, '20a40556-a484-4904-b99a-04f9b4b94d6a', 6);
INSERT INTO `refresh_tokens` VALUES (44, '2024-07-03', 1, 'ef3a63c3-fef7-4c96-ad22-556e5cd211dc', 7);
INSERT INTO `refresh_tokens` VALUES (45, '2024-07-03', 1, 'e11d2b80-d088-4261-b079-698c3912d7e1', 8);
INSERT INTO `refresh_tokens` VALUES (46, '2024-07-03', 1, '39b1e6a5-e0f2-40b0-8aa4-1a3f5e290831', 9);
INSERT INTO `refresh_tokens` VALUES (47, '2024-07-03', 1, '6473b6cd-0ed4-49c1-a855-8b9a7c834d78', 10);
INSERT INTO `refresh_tokens` VALUES (48, '2024-07-03', 1, '65206229-bcb4-45b3-89de-d818ddbcb363', 11);
INSERT INTO `refresh_tokens` VALUES (49, '2024-07-03', 0, '5b307287-8b42-45d0-a17b-d3e064f216a5', 1);
INSERT INTO `refresh_tokens` VALUES (50, '2024-07-03', 1, '5778df92-bf1e-42fc-af5f-14024372aeb8', 2);
INSERT INTO `refresh_tokens` VALUES (51, '2024-07-03', 1, '69ff060c-fd65-4579-adea-bd843549361b', 1);

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_size` bigint NOT NULL DEFAULT 0,
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `generated_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resources
-- ----------------------------
INSERT INTO `resources` VALUES (1, 28187, 'PJie5b2L5ki5Rl+liq/0/A==', 'swmU5JHe3NjqWScDMFFfiA==', '0q54SBy3pky0TxR/Xei4xGHiR2LaqzsEwE4PAA1+ruzt9A3BMmNMZfuOy/d38fZz', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMc7mFu6huMnZrjRRBIPop6DBGF9YbQNzkZSj08zelPFBeC4RWboNoxqD5fvDvpFmH3shGAMpeVbyzDP69qBPT9uw==');
INSERT INTO `resources` VALUES (2, 29542, 'PJie5b2L5ki5Rl+liq/0/A==', 'geaWyx4n8t8rArC3ufIMgQ==', 'ciHxr7R8OMsqRSkBUIyEdea9RP7K+9U0kkqNHb/WGYhOELt3Ytttb+VBaAymwlaN', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcIahOLPPMXx7RZJnaCpq+MUt04OR4OF0Ioa89u+//HUF4it9XDjl1cyuX82FNkYTX8LdTSUiwDw1mcmg6QSHlgQ==');
INSERT INTO `resources` VALUES (3, 33576, 'PJie5b2L5ki5Rl+liq/0/A==', 'IsHMWb104wLJOL5FRXOwDQ==', 'AOFLr8LMOTKeDOhkrtkeL0GJiycAhxoJdgMJQ0U2FtMAZwIJxwv8CTmv94tE8Um9', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcEKwmFnFwkZdvTp7sgcRnfgobfZIyLmOQ6mEMcUfykw6SIkMYhx000DYAID+pBPCivJKSzVsshbluOaMc6Ugvvw==');
INSERT INTO `resources` VALUES (4, 28552, 'PJie5b2L5ki5Rl+liq/0/A==', '1+oODvBekWSRUFbCSR9Xmg==', 'OrH5lNM3SzKb5db3LNAoLFeG7lZt6md/02B3ZbG96yJ7iqsM5eAt9ryWeNxuFfsu', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcq81AKbtlXZxpVA5Rz/k17t8QgzrcsGF5fOmZ2nn9gvBW5MiCgJPUYBmWWB4rAXmErckUSpMBQAJUMCvGJHFkEA==');
INSERT INTO `resources` VALUES (5, 32847, 'PJie5b2L5ki5Rl+liq/0/A==', 'JBeu9Js/xxOx1x+dQWdROQ==', 'gB21W5KaOiykvRIGEdADsggJFfZOxcsqig0eY1XQaI1IED5VsixM0sq1ane3W710', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcU5Go9XTXVJrq83zGmamLjtoYXdh8eGPKIAzBvusJ8ukejQf1sTLqU4v5Dx6D+QaCAC1IbPJroVweM90+MtOQ1w==');
INSERT INTO `resources` VALUES (6, 29933, 'PJie5b2L5ki5Rl+liq/0/A==', 'skWlgeDETHbXWg6FUuf5vg==', 'FYq0tld1CXESEA8lkcHUZSL+YkcAbMHMqHyjtNsy5IYvgirL3brB9/W0v+efDEtR', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcvv/2pls4CkuhA9OK/NEYfyoDXliugFPI7cAfUGGlY4ky1pO8Cu51qF7378QLgxMzJD3CB1id6kubDAPpjHP0xw==');
INSERT INTO `resources` VALUES (7, 33446, 'PJie5b2L5ki5Rl+liq/0/A==', 'ufAZ5GJ59bq0vWGVn2ANBw==', '+YjDzHPi+EEKG5igNkNTI9o0wPBegF5O572hwoqv4+qX2lrBmgrAzzwnXK7NNK4j', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcEpVnAho9lvIZawVbpCGUgDYdghZ8NVys+/ti/nnqiMPNVWEE2705uJyVyv9N6/qyUOD4oimCeaR4LybbLEocPw==');
INSERT INTO `resources` VALUES (8, 41449, 'PJie5b2L5ki5Rl+liq/0/A==', 'U41Te7iTMqFOPjdZGBGAbQ==', 'jFmL4jqg/n/DLApOMV5fDQkh2w2dAS45hx72H01tdgWqcTMg7NI577gmTvjz5Mxs', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMc7H9dQQvbLdxFyhN1h+6y/14IdHUb4GSChMWJAk333s7FD2m+ozCmU2z58Mn5lAPx1bQtCLlU12XqHrTyuTIU3w==');
INSERT INTO `resources` VALUES (9, 40903, 'PJie5b2L5ki5Rl+liq/0/A==', 'ZF0VXSifqK05LmzKEjNYGg==', 'B13B5d0HTbj3lHbXEnH6qTKfyb/qt3hMdy2mj2PmCiB6+cz5L25eNeqHaVYxWu+o', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMc2vwXozAey+ECTFXMia25ajBWCvaCtDYbk19gaAc8UXMOnySj0UPeba8IchWjig/QSacg5panmCZo1S08sx98Ew==');
INSERT INTO `resources` VALUES (10, 32239, 'PJie5b2L5ki5Rl+liq/0/A==', 'capCrCpd57R7wvroFTbcVw==', '0XYaET6vYfdb2WCZwwEC9gvKr3CQvBENyH71sXI/Uj/QRnmXM21RQpl910G/lN7g', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcliTdmMuQxyeaRqqKDlTT5GEvNLBxmR0OddvEUBvLWdkvz6ZBltMwXBe7zePJ6qJ4YpGbjivBq9sgTua5Q/aX3w==');
INSERT INTO `resources` VALUES (11, 27928, 'PJie5b2L5ki5Rl+liq/0/A==', 'NJDl8VBEpElO1TRSQhHiww==', 'eyoqLjtymcZYDgbHU7ie/H1A9yzrQRxU33XnN8g/z2UnGLf1X+ZUuB1GB///ALsG', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcJAMq565SrqBRcNcIhUzZpXChs0RCV+jbX2Pn8mr6rY5VrLUVE8ITtUxtSMBlJnPOiQ8g6tL4cbqMy3yZ7VaIew==');
INSERT INTO `resources` VALUES (12, 5750196, '9uNYc5rDu+ULDmht2FKE6A==', 'bJ2GTEgcklAerNmqcleH1g==', 'F/6QveAAnl1o5irqBVLJ3s8/lG4YhWUenccWH7aT983WRX3bMEYJZ18XsyADUSFi', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcOcIziwjMtA6eTUUf5welf0amQPsLvLa9Y7pGg09ydXqfZeGZjiOL7GKJrJ292ZoUqTU8crO0k5ruHFnp2W9icg==');
INSERT INTO `resources` VALUES (13, 6224465, '9uNYc5rDu+ULDmht2FKE6A==', 'pL8/RZTtkq6i9G6Buop0yw==', 'xdTtsDj5xw1QUk0mvECoL7FT7ZT8qsxsoav39bovdPb0/Cko830Y8YT/a6KrepT+', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcsnUxD4zQ7KULdPeCT8WaCutrmQ51Nk6WD7keTitHxujMwgJcP6mtt9gghQVZA/rbqEGFg/+l6VBc5aRkoeaKBw==');
INSERT INTO `resources` VALUES (14, 1767769, '9uNYc5rDu+ULDmht2FKE6A==', 'B4bp7+OzKawVH/JuwlePdg==', '3EBdtEHOtFGDhcwMRakEjjrcGBLPDpZpznpk7rTud+1rj+Jvr5o/L3DA/dmI86W8', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcQ9nazXLUl9jVTYerqekxgxBBQD92YJ3ioZztroABskm1ogRTHdXGib1giU0E1CIUwJSrifGF8bHZ/WCmTld5fg==');
INSERT INTO `resources` VALUES (15, 3403824, '9uNYc5rDu+ULDmht2FKE6A==', 'Dt0bxCMPNKwpHVYVomlkJg==', 'CcDg7uvTS3McCKIRDrYh3FsQ1h/rvxjpUfTqeM9RuJAM1GD1ui8d+2HUnpYWOe4i', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcUuAXEdd1CX1qYwHTeO4O8ZmZ4dl+VQCkcrkzTAMNaOYmzaOiCcp/EI1iAxnZ130dAEaALDyhyRiHwNXXHI9EtQ==');
INSERT INTO `resources` VALUES (16, 2599281, '9uNYc5rDu+ULDmht2FKE6A==', 'WQnPU3YspbqNQT82iUj1OQ==', 'lZPAdOb1V6ZfbV9EH7pP7kTT2mE6UBaE2uOOCZ15Rm/B886PUlrztTGGFTV5W8DI', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcZQPHBYJULbsFbRcFpyOoeEAGfbf6OONxK1ebKAdnzadqGKIJNnKhzQ4uyVnHEc/2sda1jdhoiV/yRPpMBiz5iw==');
INSERT INTO `resources` VALUES (18, 7230574, '9uNYc5rDu+ULDmht2FKE6A==', 'NYIha0OCkOywZGZ+kFRY0w==', 'c5TCkjBS1soDUpdjdRXh7MvzPUHIroY9hF1tb4pxnI5y1GukOgaa4UxZ/5w8y1w4', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcATlKxW7JxqeXZrgkU2acQdEPrW9hcdpfk5ehD++50640tNVqkiIfKEjpHzpaVmhR/Wj6HvxSYpAMehW+ug54SQ==');
INSERT INTO `resources` VALUES (19, 3576718, '9uNYc5rDu+ULDmht2FKE6A==', 'vU820hj7HPQR2+8G2LDnsQ==', 'JOblfwZR1eVOabm8qzgWEn1deaMQpy17qnLENba1Q5MgpOs7b5Gf+gR86y+OIgM3', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcoqCWEhy07kuYUWk8luH0lkLYweRF9+e16Wv5IDPdLlLY2iXevDvS05UrMt2ediYgzCEqJGzEWr7TGXQ9JiZXnw==');
INSERT INTO `resources` VALUES (20, 2554601, '9uNYc5rDu+ULDmht2FKE6A==', 'TdrzQKIAer0Ab6ETAh6yYA==', 'Y9Vi/8UOeSINb/Jh+9sFXT1GeYAtTbR+teClGTQn9a8TfuAt/41yWgeWra7xdKWF', 'ikigbjYRwMBDYLgll4AILmqIjj/5w7w0g6iZ2Xi/eWHKYwZQ5bW4Q9DS1rBT+iMcBlIZmbLHYCE57uybm1O2sguI741X5wOLgxBoS0oRR8pgzI+lmejF9SslpZOP7wnHroXwfqGysMv3aEVrb+OoCg==');

-- ----------------------------
-- Table structure for space_seq
-- ----------------------------
DROP TABLE IF EXISTS `space_seq`;
CREATE TABLE `space_seq`  (
  `next_val` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of space_seq
-- ----------------------------
INSERT INTO `space_seq` VALUES (151);

-- ----------------------------
-- Table structure for user_friendships
-- ----------------------------
DROP TABLE IF EXISTS `user_friendships`;
CREATE TABLE `user_friendships`  (
  `modified_date` datetime NULL DEFAULT NULL,
  `status` enum('PENDING','ACCEPTED','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  PRIMARY KEY (`receiver_id`, `sender_id`) USING BTREE,
  INDEX `FK3cnijid6v4ppnj3itvt8pb1w8`(`sender_id` ASC) USING BTREE,
  CONSTRAINT `FK2ra0hm2hu8fmc0rb3m6s6ypbx` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK3cnijid6v4ppnj3itvt8pb1w8` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_friendships
-- ----------------------------
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:48:56', 'ACCEPTED', 1, 2);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:23', 'ACCEPTED', 2, 3);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:54:30', 'ACCEPTED', 5, 3);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:56:16', 'ACCEPTED', 7, 3);
INSERT INTO `user_friendships` VALUES ('2024-06-03 10:00:03', 'ACCEPTED', 11, 3);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:26', 'ACCEPTED', 2, 4);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:52:17', 'ACCEPTED', 3, 4);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:57:55', 'ACCEPTED', 8, 4);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:29', 'ACCEPTED', 2, 5);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:57:58', 'ACCEPTED', 8, 5);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:39', 'ACCEPTED', 2, 6);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:52:28', 'ACCEPTED', 3, 6);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:53:21', 'ACCEPTED', 4, 6);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:56:20', 'ACCEPTED', 7, 6);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:53:26', 'ACCEPTED', 4, 7);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:58:03', 'ACCEPTED', 8, 7);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:46', 'ACCEPTED', 2, 8);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:52:37', 'ACCEPTED', 3, 8);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:54:58', 'ACCEPTED', 6, 8);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:58:55', 'ACCEPTED', 9, 8);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:59:35', 'ACCEPTED', 10, 8);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:56', 'ACCEPTED', 2, 9);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:52:44', 'ACCEPTED', 3, 9);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:53:30', 'ACCEPTED', 4, 9);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:54:40', 'ACCEPTED', 5, 9);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:51:49', 'ACCEPTED', 2, 10);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:54:37', 'ACCEPTED', 5, 10);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:56:23', 'ACCEPTED', 7, 10);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:53:37', 'ACCEPTED', 4, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:54:34', 'ACCEPTED', 5, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:55:02', 'REJECTED', 6, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:56:27', 'ACCEPTED', 7, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:58:07', 'ACCEPTED', 8, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:59:00', 'ACCEPTED', 9, 11);
INSERT INTO `user_friendships` VALUES ('2024-06-03 09:59:39', 'ACCEPTED', 10, 11);

-- ----------------------------
-- Table structure for user_permissions
-- ----------------------------
DROP TABLE IF EXISTS `user_permissions`;
CREATE TABLE `user_permissions`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `channel_permission` enum('NO_ACCESS','SELF','VIEW','CREATE','MODIFY') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_permission` enum('NO_ACCESS','SELF','VIEW','CREATE','MODIFY') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_37rs1t064fvu26lg5hfjvxkmj`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_permissions
-- ----------------------------
INSERT INTO `user_permissions` VALUES (1, 'MODIFY', 'admin', 'MODIFY');
INSERT INTO `user_permissions` VALUES (2, 'VIEW', 'user', 'SELF');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dob` date NOT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gender` tinyint(1) NULL DEFAULT 1,
  `is_active` tinyint(1) NULL DEFAULT 1,
  `modified_date` datetime NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `avatar` int NULL DEFAULT NULL,
  `role` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7`(`email` ASC) USING BTREE,
  UNIQUE INDEX `UK_du5v5sr43g5bfnji4vb8hg5s3`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `UK_4gywe1rqnip1n35rovh51od01`(`avatar` ASC) USING BTREE,
  INDEX `FKowg63l42m6h8sdu0cjfkyxavj`(`role` ASC) USING BTREE,
  CONSTRAINT `FKowg63l42m6h8sdu0cjfkyxavj` FOREIGN KEY (`role`) REFERENCES `user_permissions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKsahl3ab7aa6qp3li362hhryy8` FOREIGN KEY (`avatar`) REFERENCES `resources` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '2002-10-09', 'iVN2MwidrmNOyducvbkDxQ==', 1, 1, '2024-06-03 09:46:30', 'Nguyễn Tuấn Kiệt', 'J20OdaYEugtrQ+SZOyftXEEJZc4UBSaYS3IjrEQWsab7d8v9CKdtwAbr5n8asJ9pQn12xMaXEOsy/FqgVSiaDQ==', '0963987949', 1, 1);
INSERT INTO `users` VALUES (2, '2005-07-01', '1ToYSFKZCZtfPavuhP0b7g==', 1, 1, '2024-06-03 09:38:06', 'Rita Poole', 'CIIs84dQsjC/e3EuXGxyjVYjzBRTDt8MMU7vGNJeN01w3pE2IvBeu6npxeYkxaUox6EOYstzzaIhXUcXH4JLiw==', '0986974956', 2, 2);
INSERT INTO `users` VALUES (3, '2002-06-17', 'zcx9Xm9rPxcbSmVoNgYNlw==', 0, 1, '2024-06-03 09:38:41', 'Avery Neal', 'b06o5z2fUmcSaLNiz8P2yPEgoUE9xAx7HI6u0v8JA+rKivHzO9vnXMfgziij2QcoYM8auu7MCvhSDVCnCm3qMg==', '0578365978', 3, 2);
INSERT INTO `users` VALUES (4, '1975-10-14', '4oqpH7CGO61PeFl43zJR+A==', 0, 1, '2024-06-03 09:39:20', 'Misty Gilbert', 'p8q3LfHEv/hHXZGeIXk7qyp4NOiir4qGz0MsmjLVbeBmgRSrUjnHhbhI1sAZpAeofkE/GYunwYaatpaCB4Q09g==', '0746698356', 4, 2);
INSERT INTO `users` VALUES (5, '1991-10-17', 'sKSKeKDLFM5TsZzUZq2esA==', 1, 1, '2024-06-03 09:40:05', 'Alice Rowe', 'QkPo2ezQ4bzFaUxyKYjvK8prQbHBRd018EwL2LHXqVXWQ31y5jB3OLcs4s1JEsC45HkVhrC2b3Qkm5DMuRkegw==', '0857079357', 5, 2);
INSERT INTO `users` VALUES (6, '2007-04-18', '5Jk5FbFgs4YVEchI0HkpvQ==', 1, 1, '2024-06-03 09:40:35', 'Linden Ferguson', 'wrnGIoOzI9twfEkCbc4CNXFyQlVIaZmLvyxGWgerIl9KtE9kYImSXRe6wdgIA/rqlRfd95Tq4JBCz740WvKrNg==', '0456783427', 6, 2);
INSERT INTO `users` VALUES (7, '2001-07-07', 'K7xzEE4xsaRKAk5b0hzf6g==', 1, 1, '2024-06-03 09:41:14', 'Edmond Paul', 'JB22H71b4Ms8DfxmXb+jk/JpLP83AAYyrdIz4fPi2hVEjmkm73LO4QjS1KicnwfLPxJtWWDVnKB7QYfaphzdow==', '0325467851', 7, 2);
INSERT INTO `users` VALUES (8, '2011-10-25', 'dDEaQQycF3BjerEDcN7CJA==', 0, 1, '2024-06-03 09:42:13', 'Quinn Martin', 'EbYqSKuSGc/b4y9O9uMMnK3LPnp+SFfcb3gi8Yzh8+pINKVbmr6w7iz0AqQT53qbf52CpNbbRKx/b+Ti92Q4ew==', '0963989470', 8, 2);
INSERT INTO `users` VALUES (9, '1994-10-19', 'rsONBKHcfuFM+++7jFIawg==', 1, 1, '2024-06-03 09:42:42', 'Chloe Alvarado', 'JB22H71b4Ms8DfxmXb+jk/JpLP83AAYyrdIz4fPi2hVEjmkm73LO4QjS1KicnwfLPxJtWWDVnKB7QYfaphzdow==', '0978675293', 9, 2);
INSERT INTO `users` VALUES (10, '1996-10-22', 'iV50COtX5HEXtMZZqbw+Tg==', 1, 1, '2024-06-03 09:43:18', 'Luke Wells', 'I8tyvzkbjTsLih8caHyakl3+kledcnHE4z9+356ETFM8GvfpnNwERfsHcANZpsp5ycVGWn30XABHi5ewZAYbNw==', '0987467098', 10, 2);
INSERT INTO `users` VALUES (11, '1986-03-26', 'QKhiUH/atpycKlZhg6qFGl9C0iOjgGjHy4+iq9QFCRI=', 0, 1, '2024-06-03 09:43:56', 'Rebecca Coleman', 'uaaRKL1qEqrbmXvolVc1pnY9hOrFvdpD0/grITFgMerd93eg0azvLpCKNIeZRA7N0QVOTrwq29NMH2hDBYLL8Q==', '0798547082', 11, 2);

-- ----------------------------
-- Table structure for users_friends
-- ----------------------------
DROP TABLE IF EXISTS `users_friends`;
CREATE TABLE `users_friends`  (
  `user_id` int NOT NULL,
  `friends_receiver_id` int NOT NULL,
  `friends_sender_id` int NOT NULL,
  UNIQUE INDEX `UK_lhl72m5s5tpdbonru38johet`(`friends_receiver_id` ASC, `friends_sender_id` ASC) USING BTREE,
  INDEX `FKry5pun2eg852sbl2l50p236bo`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK2rva8eb4hbugsway8ta1l7tid` FOREIGN KEY (`friends_receiver_id`, `friends_sender_id`) REFERENCES `user_friendships` (`receiver_id`, `sender_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKry5pun2eg852sbl2l50p236bo` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users_friends
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
