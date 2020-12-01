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
ALTER TABLE member add (device_token VARCHAR2(1000));

-- 시퀸스 
CREATE SEQUENCE seq_member_id; 

CREATE SEQUENCE seq_member_photo_id; 

CREATE SEQUENCE seq_foret_id; 

CREATE SEQUENCE seq_foret_photo_id; 

CREATE SEQUENCE seq_board_id; 

CREATE SEQUENCE seq_board_photo_id; 

CREATE SEQUENCE seq_board_comment_id; 

CREATE SEQUENCE seq_tag_id; 

CREATE SEQUENCE seq_region_id; 


-- 멤버와 사진 조회
CREATE OR REPLACE VIEW V_MEMBER_AND_PHOTO 
AS 
  SELECT member.*,
         member_photo.filename AS photo 
    FROM member 
         -- member_photo    
         LEFT OUTER JOIN member_photo 
                      ON member.id = member_photo.member_id
   ORDER BY member.id;

-- 멤버_태그 조회    
CREATE OR REPLACE VIEW V_MEMBER_T 
AS 
  SELECT member.id    AS id, 
         tag.tag_name AS tag_name
    FROM member 
         -- member_tag     
         LEFT OUTER JOIN member_tag 
                      ON member.id = member_tag.id 
         LEFT OUTER JOIN tag 
                      ON member_tag.tag_id = tag.tag_id 
   ORDER BY member.id, 
            tag.tag_id; 

-- 멤버_지역 조회    
CREATE OR REPLACE VIEW V_MEMBER_R 
AS 
  SELECT member.id AS id, 
         region.si AS region_si, 
         region.gu AS region_gu 
    FROM member 
         -- member_tag     
         LEFT OUTER JOIN member_region 
                      ON member.id = member_region.id 
         LEFT OUTER JOIN region 
                      ON member_region.region_id = region.region_id 
   ORDER BY member.id, 
            region.region_id; 

-- 멤버_보드_좋아요 조회  
CREATE OR REPLACE VIEW V_MEMBER_BL 
AS 
  SELECT member.id AS id, 
         like_board.board_id AS board_id
    FROM member 
         -- member_like_board     
         LEFT OUTER JOIN like_board 
                      ON member.id = like_board.id 
   ORDER BY member.id, 
            like_board.board_id; 

-- 멤버_댓글_좋아요 조회  
CREATE OR REPLACE VIEW V_MEMBER_CL 
AS 
  SELECT member.id AS id, 
         like_comment.comment_id AS comment_id
    FROM member 
         -- member_like_comment    
         LEFT OUTER JOIN like_comment 
                      ON member.id = like_comment.id 
   ORDER BY member.id, 
            like_comment.comment_id; 

SELECT * FROM V_MEMBER_F;
-- 멤버가 속한 포레 조회
CREATE OR replace VIEW V_MEMBER_F 
AS 
  SELECT member_id AS id,
         foret_id  AS foret_id
  FROM   foret_member 
  ORDER  BY foret_id ASC; 

CREATE OR REPLACE VIEW V_MEMBER_F
AS 
  SELECT member.id AS id, 
         foret_member.foret_id AS foret_id
    FROM member 
         -- member_like_comment    
         LEFT OUTER JOIN foret_member 
                      ON member.id = foret_member.member_id
   ORDER BY member.id, 
            foret_member.foret_id; 

-- 추천 태그 정렬
CREATE OR replace VIEW v_tag_rank 
AS 
  SELECT ROWNUM AS RANK, 
         a.tag_id, 
         a.tag_name 
  FROM  (SELECT Count(*)     AS cnt, 
                tag.tag_id   AS tag_id, 
                tag.tag_name AS tag_name 
         FROM   (SELECT * 
                 FROM   member_tag 
                 UNION ALL 
                 SELECT * 
                 FROM   foret_tag)ref_tag 
                left outer join tag 
                             ON ref_tag.tag_id = tag.tag_id 
         GROUP  BY tag.tag_name, 
                   tag.tag_id 
         ORDER  BY cnt DESC, 
                   tag.tag_id) a; 

-- 추천 지역 정렬
CREATE OR replace VIEW v_region_rank 
AS 
  SELECT ROWNUM AS RANK, 
         a.region_id, 
         a.region_si, 
         a.region_gu 
  FROM  (SELECT Count(*)         AS cnt, 
                region.region_id AS region_id, 
                region.si        AS region_si, 
                region.gu        AS region_gu 
         FROM   (SELECT * 
                 FROM   member_region 
                 UNION ALL 
                 SELECT * 
                 FROM   foret_region)ref_region 
                left outer join region 
                             ON ref_region.region_id = region.region_id 
         GROUP  BY region.region_id, 
                   region.si, 
                   region.gu 
         ORDER  BY cnt DESC, 
                   region.region_id)a; 

