CREATE TABLE member 
  ( 
     id       NUMBER(30) NOT NULL, 
     name     VARCHAR2(20) NOT NULL, 
     email    VARCHAR2(50) NOT NULL, 
     password VARCHAR2(50) NOT NULL, 
     nickname VARCHAR2(20) NOT NULL, 
     birth    VARCHAR2(30) NOT NULL, 
     reg_date DATE NOT NULL 
  ); 

CREATE TABLE member_tag 
  ( 
     id     NUMBER(30) NOT NULL, 
     tag_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE member_region 
  ( 
     id        NUMBER(30) NOT NULL, 
     region_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE foret 
  ( 
     id         NUMBER(30) NOT NULL, 
     leader_id  NUMBER(30) NOT NULL, 
     name       VARCHAR2(50) NOT NULL, 
     introduce  VARCHAR2(600) NOT NULL, 
     max_member NUMBER(10) NOT NULL, 
     reg_date   DATE NOT NULL 
  ); 

CREATE TABLE foret_tag 
  ( 
     id     NUMBER(30) NOT NULL, 
     tag_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE foret_region 
  ( 
     id        NUMBER(30) NOT NULL, 
     region_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE tag 
  ( 
     tag_id   NUMBER(30) NOT NULL, 
     tag_name VARCHAR2(50) NOT NULL 
  ); 

CREATE TABLE region 
  ( 
     region_id NUMBER(30) NOT NULL, 
     si        VARCHAR2(50) NOT NULL, 
     gu        VARCHAR2(50) NOT NULL 
  ); 

CREATE TABLE foret_member 
  ( 
     foret_id  NUMBER(30) NOT NULL, 
     member_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE board 
  ( 
     id        NUMBER(30) NOT NULL, 
     writer    NUMBER(30) NOT NULL, 
     foret_id  NUMBER(30) NOT NULL, 
     TYPE      NUMBER(30) NULL, 
     hit       NUMBER(30) NULL, 
     subject   VARCHAR2(100) NULL, 
     content   VARCHAR2(1000) NULL, 
     reg_date  DATE NULL, 
     edit_date DATE NULL 
  ); 

CREATE TABLE board_comment 
  ( 
     id       NUMBER(30) NOT NULL, 
     board_id NUMBER(30) NOT NULL, 
     writer   NUMBER(30) NOT NULL, 
     content  VARCHAR2(200) NULL, 
     reg_date DATE NULL, 
     group_no NUMBER(30) NULL 
  ); 

CREATE TABLE like_board 
  ( 
     id       NUMBER(30) NOT NULL, 
     board_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE like_comment 
  ( 
     id         NUMBER(30) NOT NULL, 
     comment_id NUMBER(30) NOT NULL 
  ); 

CREATE TABLE member_photo 
  ( 
     id         NUMBER(30) NOT NULL, 
     member_id  NUMBER(30) NOT NULL, 
     dir        VARCHAR2(200) NULL, 
     filename   VARCHAR2(200) NULL, 
     originname VARCHAR2(200) NULL, 
     filesize   NUMBER(30) NULL, 
     filetype   VARCHAR2(200) NULL, 
     reg_date   DATE NULL 
  ); 

CREATE TABLE foret_photo 
  ( 
     id         NUMBER(30) NOT NULL, 
     foret_id   NUMBER(30) NOT NULL, 
     dir        VARCHAR2(200) NULL, 
     filename   VARCHAR2(200) NULL, 
     originname VARCHAR2(200) NULL, 
     filesize   NUMBER(30) NULL, 
     filetype   VARCHAR2(200) NULL, 
     reg_date   DATE NULL 
  ); 

CREATE TABLE board_photo 
  ( 
     id         NUMBER(30) NOT NULL, 
     board_id   NUMBER(30) NOT NULL, 
     dir        VARCHAR2(200) NULL, 
     filename   VARCHAR2(200) NULL, 
     originname VARCHAR2(200) NULL, 
     filesize   NUMBER(30) NULL, 
     filetype   VARCHAR2(200) NULL, 
     reg_date   DATE NULL 
  ); 

-- 태그이름 중복 금지
ALTER TABLE tag ADD CONSTRAINT U_tag_name UNIQUE (tag_name);
ALTER TABLE board MODIFY (foret_id NUMBER(30) NULL);

-- 시퀸스 
CREATE SEQUENCE seq_member_id; 

CREATE SEQUENCE seq_foret_id; 

CREATE SEQUENCE seq_board_id; 

CREATE SEQUENCE seq_tag_id; 

CREATE SEQUENCE seq_region_id; 

CREATE SEQUENCE seq_member_photo_id; 

CREATE SEQUENCE seq_foret_photo_id; 

CREATE SEQUENCE seq_board_photo_id; 