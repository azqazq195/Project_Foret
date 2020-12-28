# 주 테이블
CREATE TABLE `member` (
	`id` 			BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` 		VARCHAR(30) NOT NULL COMMENT '30바이트, 10글자' COLLATE 'utf8_general_ci',
	`email` 		VARCHAR(100) NOT NULL COMMENT '100바이트, 33글자' COLLATE 'utf8_general_ci',
	`password` 	VARCHAR(100) NOT NULL COMMENT '50바이트, 13글자' COLLATE 'utf8_general_ci',
	`nickname` 	VARCHAR(30) NOT NULL COMMENT '30바이트, 10글자' COLLATE 'utf8_general_ci',
	`device_token` VARCHAR(100) NOT NULL COLLATE 'utf8_general_ci',
	`birth` 		DATE NOT NULL,
	`reg_date` 	DATETIME NOT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `email` (`email`) USING BTREE
)
COMMENT='회원정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `foret` (
	`id`			BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`leader_id`	BIGINT(20)		NOT NULL,
	`name`		VARCHAR(30)		NOT NULL COMMENT '30바이트 10자'		COLLATE 'utf8_general_ci',
	`introduce`	VARCHAR(600)	NOT NULL	COMMENT '600바이트 200자'	COLLATE 'utf8_general_ci',
	`max_member`INT(11)			NOT NULL,
	`reg_date`	DATETIME			NOT NULL
)
COMMENT='그룹정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `tag` (
	`id`			BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`tag_name`	VARCHAR(30)		NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci'
)
COMMENT='태그정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `region` (
	`id`			BIGINT(20)	NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`region_si`	VARCHAR(30)	NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci',
	`region_gu`	VARCHAR(30)	NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci'
)
COMMENT='지역정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `board` (
	`id`			BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`writer_id`	BIGINT(20)		NOT NULL,
	`foret_id`	BIGINT(20)		NOT NULL,
	`type`		INT(11)			NOT NULL	COMMENT 	'0 : 공지사항
															 	 1 : 포레 공지사항
																 2 : 포레 일정 게시판
																 3 : 포레 게시판
																 4 : 익명 게시판',
	`hit`			INT(11)			NOT NULL,
	`subject`	VARCHAR(100)	NOT NULL,
	`content`	TEXT				NOT NULL,
	`reg_date`	DATETIME			NOT NULL,
	`edit_date`	DATETIME			NOT NULL
)
COMMENT='게시판정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `board_comment` (
	`id`			BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`board_id`	BIGINT(20)		NOT NULL,
	`writer_id`	BIGINT(20)		NOT NULL,
	`group_no`	INT(11)			NOT NULL,
	`content`	VARCHAR(200)	NOT NULL,
	`reg_date`	DATETIME			NOT NULL
)
COMMENT='게시판정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `member_photo` (
	`id`				BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`member_id`		BIGINT(20)		NOT NULL,
	`dir`				VARCHAR(200)	NULL,
	`filename`		VARCHAR(200)	NULL,
	`originname`	VARCHAR(200)	NULL,
	`filesize`		INT(11)			NULL,
	`filetype`		VARCHAR(200)	NULL,
	`reg_date`		DATETIME			NOT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `FK_member_photo_member` (`member_id`) USING BTREE,
	CONSTRAINT `FK_member_photo_member` FOREIGN KEY (`member_id`) REFERENCES `Foret`.`member` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COMMENT='멤버사진정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `foret_photo` (
	`id`				BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`foret_id`		BIGINT(20)		NOT NULL,
	`dir`				VARCHAR(200)	NULL,
	`filename`		VARCHAR(200)	NULL,
	`originname`	VARCHAR(200)	NULL,
	`filesize`		INT(11)			NULL,
	`filetype`		VARCHAR(200)	NULL,
	`reg_date`		DATETIME			NOT NULL
)
COMMENT='그룹사진정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `board_photo` (
	`id`				BIGINT(20)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`board_id`		BIGINT(20)		NOT NULL,
	`dir`				VARCHAR(200)	NULL,
	`filename`		VARCHAR(200)	NULL,
	`originname`	VARCHAR(200)	NULL,
	`filesize`		INT(11)			NULL,
	`filetype`		VARCHAR(200)	NULL,
	`reg_date`		DATETIME			NOT NULL
)
COMMENT='게시판사진정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;





# 연결 테이블 

CREATE TABLE `member_tag` (
	`member_id`	BIGINT(20)	NOT NULL,
	`tag_id`		BIGINT(20)	NOT NULL,
	PRIMARY KEY (`member_id`, `tag_id`) USING BTREE,
	INDEX `FK_member_tag_tag` (`tag_id`) USING BTREE,
	CONSTRAINT `FK_member_tag_member` FOREIGN KEY (`member_id`) REFERENCES `Foret`.`member` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK_member_tag_tag` 	 FOREIGN KEY (`tag_id`) REFERENCES `Foret`.`tag` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE `member_region` (
	`member_id`	BIGINT(20)	NOT NULL,
	`region_id`	BIGINT(20)	NOT NULL,
	PRIMARY KEY (`member_id`, `region_id`) USING BTREE,
	INDEX `FK_member_region_region` (`region_id`) USING BTREE,
	CONSTRAINT `FK_member_region_member` FOREIGN KEY (`member_id`) REFERENCES `Foret`.`member` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK_member_region_region` FOREIGN KEY (`region_id`) REFERENCES `Foret`.`region` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE `member_foret` (
	`member_id`	BIGINT(20)	NOT NULL,
	`foret_id`	BIGINT(20)	NOT NULL,
	PRIMARY KEY (`member_id`, `foret_id`) USING BTREE,
	INDEX `FK_member_foret_foret` (`foret_id`) USING BTREE,
	CONSTRAINT `FK_member_foret_foret`  FOREIGN KEY (`foret_id`) REFERENCES `Foret`.`foret` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
	CONSTRAINT `FK_member_foret_member` FOREIGN KEY (`member_id`) REFERENCES `Foret`.`member` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE `like_board` (
	`member_id`	BIGINT(20)	NOT NULL,
	`board_id`	BIGINT(20)	NOT NULL
);

CREATE TABLE `like_comment` (
	`member_id`		BIGINT(20)	NOT NULL,
	`comment_id`	BIGINT(20)	NOT NULL
);




CREATE TABLE `foret_tag` (
	`foret_id`	BIGINT(20)	NOT NULL,
	`tag_id`		BIGINT(20)	NOT NULL
);

CREATE TABLE `foret_region` (
	`foret_id`	BIGINT(20)	NOT NULL,
	`region_id`	BIGINT(20)	NOT NULL
);



