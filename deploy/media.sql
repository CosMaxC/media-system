-- 建库
DROP database IF EXISTS `media`;
create database IF NOT EXISTS `media` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci; 
use media;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for douban_director
-- ----------------------------
DROP TABLE IF EXISTS `douban_director`;
CREATE TABLE `douban_director`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `director_id` bigint(255) NOT NULL COMMENT '导演id',
  PRIMARY KEY (`douban_id`, `director_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for douban_episode
-- ----------------------------
DROP TABLE IF EXISTS `douban_episode`;
CREATE TABLE `douban_episode`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `douban_episode` int(255) NOT NULL COMMENT '豆瓣集数',
  `name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本集中文名',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本集英文名',
  `play_time` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '播放时间',
  `episode_brief` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '剧情简介',
  PRIMARY KEY (`douban_id`, `douban_episode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for douban_info
-- ----------------------------
DROP TABLE IF EXISTS `douban_info`;
CREATE TABLE `douban_info`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `star_count` int(255) NULL DEFAULT NULL COMMENT '星星数，40为4颗星，45为4.5颗星',
  `score` float(255, 1) NULL DEFAULT NULL COMMENT '豆瓣评分',
  `made_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制片地区',
  `year` int(11) NULL DEFAULT NULL COMMENT '年份',
  `cost_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '片长',
  `on_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上映日期',
  `eng_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '译名',
  `douban_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '豆瓣链接',
  `avatar_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '豆瓣头像本地地址',
  `poster_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '豆瓣海报地址第一个',
  `sub_type` int(255) NULL DEFAULT NULL COMMENT '条目类型：0为电影，1为电视剧',
  `cn_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名',
  `brief` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  PRIMARY KEY (`douban_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for douban_kind
-- ----------------------------
DROP TABLE IF EXISTS `douban_kind`;
CREATE TABLE `douban_kind`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `kind_id` int(11) NOT NULL COMMENT '剧情类型',
  PRIMARY KEY (`douban_id`, `kind_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for douban_star
-- ----------------------------
DROP TABLE IF EXISTS `douban_star`;
CREATE TABLE `douban_star`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `star_id` bigint(11) NOT NULL COMMENT '演员id',
  PRIMARY KEY (`douban_id`, `star_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for douban_writer
-- ----------------------------
DROP TABLE IF EXISTS `douban_writer`;
CREATE TABLE `douban_writer`  (
  `douban_id` bigint(20) NOT NULL COMMENT '豆瓣id',
  `writer_id` bigint(255) NOT NULL COMMENT '编剧id',
  PRIMARY KEY (`douban_id`, `writer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for media_douban_schedule
-- ----------------------------
DROP TABLE IF EXISTS `media_douban_schedule`;
CREATE TABLE `media_douban_schedule`  (
  `media_id` bigint(20) NOT NULL COMMENT '媒体库id',
  `state` int(1) NOT NULL COMMENT '状态：0|未搜索，1|已搜索',
  PRIMARY KEY (`media_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for media_library
-- ----------------------------
DROP TABLE IF EXISTS `media_library`;
CREATE TABLE `media_library`  (
  `id` bigint(20) NOT NULL COMMENT '媒体库id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '媒体库名称',
  `directory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录,一个或者用|隔开的多个',
  `type` int(255) NOT NULL COMMENT '类型：0为电影，1为电视剧',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for media_library_schedule
-- ----------------------------
DROP TABLE IF EXISTS `media_library_schedule`;
CREATE TABLE `media_library_schedule`  (
  `library_id` bigint(20) NOT NULL COMMENT '媒体库id',
  `state` int(255) NOT NULL COMMENT '状态：0为创建|更新，1为删除',
  `is_success` int(255) NOT NULL COMMENT '0为未完成，1为完成',
  PRIMARY KEY (`library_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for media_video_douban
-- ----------------------------
DROP TABLE IF EXISTS `media_video_douban`;
CREATE TABLE `media_video_douban`  (
  `media_id` bigint(20) NOT NULL COMMENT '媒体库id',
  `item_id` bigint(255) NOT NULL COMMENT '电影或电视剧id',
  `douban_id` bigint(20) NULL DEFAULT NULL COMMENT '豆瓣id',
  PRIMARY KEY (`media_id`, `item_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `movie_id` bigint(20) NOT NULL COMMENT '电影id',
  `movie_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电影文件名',
  `movie_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电影位置',
  PRIMARY KEY (`movie_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for series_episode
-- ----------------------------
DROP TABLE IF EXISTS `series_episode`;
CREATE TABLE `series_episode`  (
  `series_id` bigint(20) NOT NULL COMMENT '电视剧id',
  `season` int(255) NOT NULL COMMENT '电视剧季度',
  `episode` int(255) NOT NULL COMMENT '电视剧集数',
  `episode_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '集位置',
  PRIMARY KEY (`series_id`, `season`, `episode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for series_info
-- ----------------------------
DROP TABLE IF EXISTS `series_info`;
CREATE TABLE `series_info`  (
  `series_id` bigint(20) NOT NULL COMMENT '电视剧id',
  `series_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电视剧文件名',
  `series_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电视剧位置',
  PRIMARY KEY (`series_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for star
-- ----------------------------
DROP TABLE IF EXISTS `star`;
CREATE TABLE `star`  (
  `star_id` bigint(11) NOT NULL COMMENT '演员id',
  `star_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '演员名',
  `star_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '演员照片位置',
  `star_brief` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '演员简单介绍',
  PRIMARY KEY (`star_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video_kind
-- ----------------------------
DROP TABLE IF EXISTS `video_kind`;
CREATE TABLE `video_kind`  (
  `kind_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型id',
  `kind_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名',
  PRIMARY KEY (`kind_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
