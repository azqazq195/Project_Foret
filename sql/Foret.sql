
select * from member;
commit;
SELECT * 
		FROM   dual;
select * from tag;


-- 데이터 --
insert into region values (seq_region_id.nextval, '서울시', '강남구');
insert into region values (seq_region_id.nextval, '서울시', '종로구');
insert into region values (seq_region_id.nextval, '서울시', '중구');
insert into region values (seq_region_id.nextval, '서울시', '관악구');
insert into region values (seq_region_id.nextval, '서울시', '송파구');
insert into region values (seq_region_id.nextval, '성남시', '분당구');
insert into region values (seq_region_id.nextval, '성남시', '수정구');
insert into region values (seq_region_id.nextval, '성남시', '중원구');

insert into tag values(seq_tag_id.nextval, '영어');
insert into tag values(seq_tag_id.nextval, '국어');
insert into tag values(seq_tag_id.nextval, '수학');
insert into tag values(seq_tag_id.nextval, '과학');




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
ALTER TABLE member ADD CONSTRAINT U_member_email UNIQUE (email);
ALTER TABLE board MODIFY (foret_id NUMBER(30) NULL);

-- 시퀸스 
DROP SEQUENCE seq_member_id; 

DROP SEQUENCE seq_foret_id; 

DROP SEQUENCE seq_board_id; 

DROP SEQUENCE seq_tag_id; 

DROP SEQUENCE seq_region_id; 

DROP SEQUENCE seq_member_photo_id; 

DROP SEQUENCE seq_foret_photo_id; 

DROP SEQUENCE seq_board_photo_id; 

DROP SEQUENCE seq_board_comment_id; 

CREATE SEQUENCE seq_member_id; 

CREATE SEQUENCE seq_member_photo_id; 

CREATE SEQUENCE seq_foret_id; 

CREATE SEQUENCE seq_foret_photo_id; 

CREATE SEQUENCE seq_board_id; 

CREATE SEQUENCE seq_board_photo_id; 

CREATE SEQUENCE seq_board_comment_id; 

CREATE SEQUENCE seq_tag_id; 

CREATE SEQUENCE seq_region_id; 

ALTER TABLE member 
  ADD CONSTRAINT pk_member PRIMARY KEY ( id ); 

ALTER TABLE foret 
  ADD CONSTRAINT pk_foret PRIMARY KEY ( id ); 

ALTER TABLE tag 
  ADD CONSTRAINT pk_tag PRIMARY KEY ( tag_id ); 

ALTER TABLE region 
  ADD CONSTRAINT pk_region PRIMARY KEY ( region_id ); 

ALTER TABLE board 
  ADD CONSTRAINT pk_board PRIMARY KEY ( id ); 

ALTER TABLE board_comment 
  ADD CONSTRAINT pk_board_comment PRIMARY KEY ( id ); 

ALTER TABLE member_photo 
  ADD CONSTRAINT pk_member_photo PRIMARY KEY ( id ); 

ALTER TABLE foret_photo 
  ADD CONSTRAINT pk_foret_photo PRIMARY KEY ( id ); 

ALTER TABLE board_photo 
  ADD CONSTRAINT pk_board_photo PRIMARY KEY ( id ); 

ALTER TABLE member_tag 
  ADD CONSTRAINT fk_member_to_member_tag_1 FOREIGN KEY ( id ) REFERENCES member 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE member_tag 
  ADD CONSTRAINT fk_tag_to_member_tag_1 FOREIGN KEY ( tag_id ) REFERENCES tag ( 
  tag_id ) ON DELETE CASCADE; 

ALTER TABLE member_region 
  ADD CONSTRAINT fk_member_to_member_region_1 FOREIGN KEY ( id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE member_region 
  ADD CONSTRAINT fk_region_to_member_region_1 FOREIGN KEY ( region_id ) 
  REFERENCES region ( region_id ) ON DELETE CASCADE; 

ALTER TABLE foret 
  ADD CONSTRAINT fk_member_to_foret_1 FOREIGN KEY ( leader_id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_tag 
  ADD CONSTRAINT fk_foret_to_foret_tag_1 FOREIGN KEY ( id ) REFERENCES foret ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE foret_tag 
  ADD CONSTRAINT fk_tag_to_foret_tag_1 FOREIGN KEY ( tag_id ) REFERENCES tag ( 
  tag_id ) ON DELETE CASCADE; 

ALTER TABLE foret_region 
  ADD CONSTRAINT fk_foret_to_foret_region_1 FOREIGN KEY ( id ) REFERENCES foret 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_region 
  ADD CONSTRAINT fk_region_to_foret_region_1 FOREIGN KEY ( region_id ) 
  REFERENCES region ( region_id ) ON DELETE CASCADE; 

ALTER TABLE foret_member 
  ADD CONSTRAINT fk_foret_to_foret_member_1 FOREIGN KEY ( foret_id ) REFERENCES 
  foret ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_member 
  ADD CONSTRAINT fk_member_to_foret_member_1 FOREIGN KEY ( member_id ) 
  REFERENCES member ( id ) ON DELETE CASCADE; 

ALTER TABLE board 
  ADD CONSTRAINT fk_member_to_board_1 FOREIGN KEY ( writer ) REFERENCES member ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE board 
  ADD CONSTRAINT fk_foret_to_board_1 FOREIGN KEY ( foret_id ) REFERENCES foret ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE board_comment 
  ADD CONSTRAINT fk_board_to_board_comment_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

ALTER TABLE board_comment 
  ADD CONSTRAINT fk_member_to_board_comment_1 FOREIGN KEY ( writer ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE like_board 
  ADD CONSTRAINT fk_member_to_like_board_1 FOREIGN KEY ( id ) REFERENCES member 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE like_board 
  ADD CONSTRAINT fk_board_to_like_board_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

ALTER TABLE like_comment 
  ADD CONSTRAINT fk_member_to_like_comment_1 FOREIGN KEY ( id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE like_comment 
  ADD CONSTRAINT fk_like_comment_1 FOREIGN KEY ( comment_id ) REFERENCES 
  board_comment ( id ) ON DELETE CASCADE; 

ALTER TABLE member_photo 
  ADD CONSTRAINT fk_member_to_member_photo_1 FOREIGN KEY ( member_id ) 
  REFERENCES member ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_photo 
  ADD CONSTRAINT fk_foret_to_foret_photo_1 FOREIGN KEY ( foret_id ) REFERENCES 
  foret ( id ) ON DELETE CASCADE; 

ALTER TABLE board_photo 
  ADD CONSTRAINT fk_board_to_board_photo_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

CREATE OR REPLACE FUNCTION get_board_photo_id RETURN NUMBER IS
BEGIN
     RETURN seq_board_photo_id.nextval;
END;




