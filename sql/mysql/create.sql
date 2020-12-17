# 주 테이블
CREATE TABLE `member` (
	`id`			INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name`		VARCHAR(30)		NOT NULL NOT NULL COMMENT '30바이트, 10글자' 	COLLATE 'utf8_general_ci',
	`email`		VARCHAR(100)	NOT NULL NOT NULL COMMENT '100바이트, 33글자' 	COLLATE 'utf8_general_ci',
	`password`	VARCHAR(50)		NOT NULL NOT NULL COMMENT '50바이트, 13글자' 	COLLATE 'utf8_general_ci',
	`nickname`	VARCHAR(30)		NOT NULL NOT NULL COMMENT '30바이트, 10글자' 	COLLATE 'utf8_general_ci',
	`birth`		DATE				NOT NULL,
	`reg_date`	DATETIME			NOT NULL
)
COMMENT='회원정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `foret` (
	`id`			INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`leader_id`	INT(11)			NOT NULL,
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
	`tag_id`		INT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`tag_name`	VARCHAR(30)	NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci'
)
COMMENT='태그정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `region` (
	`region_id`	INT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`region_si`	VARCHAR(30)	NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci',
	`region_gu`	VARCHAR(30)	NOT NULL COMMENT '30바이트 10자' COLLATE 'utf8_general_ci'
)
COMMENT='지역정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `board` (
	`id`			INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`writer_id`	INT(11)			NOT NULL,
	`foret_id`	INT(11)			NOT NULL,
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
	`id`			INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`board_id`	INT(11)			NOT NULL,
	`writer_id`	INT(11)			NOT NULL,
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
	`id`				INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`member_id`		INT(11)			NOT NULL,
	`dir`				VARCHAR(200)	NULL,
	`filename`		VARCHAR(200)	NULL,
	`originname`	VARCHAR(200)	NULL,
	`filesize`		INT(11)			NULL,
	`filetype`		VARCHAR(200)	NULL,
	`reg_date`		DATETIME			NOT NULL
)
COMMENT='멤버사진정보 관리 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `foret_photo` (
	`id`				INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`foret_id`		INT(11)			NOT NULL,
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
	`id`				INT(11)			NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`board_id`		INT(11)			NOT NULL,
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
	`member_id`	INT(11)	NOT NULL,
	`tag_id`		INT(11)	NOT NULL
);

CREATE TABLE `member_region` (
	`member_id`	INT(11)	NOT NULL,
	`region_id`	INT(11)	NOT NULL
);

CREATE TABLE `member_foret` (
	`member_id`	INT(11)	NOT NULL,
	`foret_id`	INT(11)	NOT NULL
);

CREATE TABLE `like_board` (
	`member_id`	INT(11)	NOT NULL,
	`board_id`	INT(11)	NOT NULL
);

CREATE TABLE `like_comment` (
	`member_id`		INT(11)	NOT NULL,
	`comment_id`	INT(11)	NOT NULL
);




CREATE TABLE `foret_tag` (
	`foret_id`	INT(11)	NOT NULL,
	`tag_id`		INT(11)	NOT NULL
);

CREATE TABLE `foret_region` (
	`foret_id`	INT(11)	NOT NULL,
	`region_id`	INT(11)	NOT NULL
);



