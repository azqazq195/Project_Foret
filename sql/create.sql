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

-- 멤버가 속한 포레 조회
CREATE OR REPLACE VIEW V_MEMBER_F
AS 
  SELECT member.id AS id, 
         foret_member.foret_id AS foret_id
    FROM member 
         -- member_like_comment    
         LEFT OUTER JOIN foret_member 
                      ON member.id = foret_member.member_id
   ORDER BY member.id asc, 
            foret_member.foret_id asc; 

-- 추천 태그 정렬
CREATE OR replace VIEW v_tag_rank 
AS 
  SELECT ROWNUM AS RANK, 
         a.tag_id, 
         a.tag_name,
         a.cnt
  FROM  (SELECT Count(*)     AS cnt, 
                tag.tag_id   AS tag_id, 
                tag.tag_name AS tag_name 
         FROM   (SELECT * 
                 FROM   member_tag 
                 UNION ALL 
                 SELECT * 
                 FROM   foret_tag)ref_tag 
                right outer join tag 
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
         a.region_gu,
         a.cnt
  FROM  (SELECT Count(*)         AS cnt, 
                region.region_id AS region_id, 
                region.si        AS region_si, 
                region.gu        AS region_gu 
         FROM   (SELECT * 
                 FROM   member_region 
                 UNION ALL 
                 SELECT * 
                 FROM   foret_region)ref_region 
                right outer join region 
                             ON ref_region.region_id = region.region_id 
         GROUP  BY region.region_id, 
                   region.si, 
                   region.gu 
         ORDER  BY cnt DESC, 
                   region.region_id)a; 

-- 내가 속한 포레의 모든 게시글, 멤버 아이디 순, 포레 아이디순, 보드 최신 순
CREATE OR replace VIEW v_member_b 
AS 
  SELECT member.id             AS member_id, 
         foret_member.foret_id AS foret_id, 
         board.id              AS board_id, 
         board.TYPE            AS TYPE, 
         board.hit             AS hit, 
         board.subject         AS subject, 
         board.content         AS content, 
         board.reg_date        AS reg_date, 
         board.edit_date       AS edit_date 
  FROM   member 
         join foret_member 
           ON member.id = foret_member.member_id 
         join board 
           ON foret_member.foret_id = board.foret_id 
  ORDER  BY member.id ASC, 
            foret_member.foret_id ASC, 
            board.id DESC; 
            
            
-- 내가 속한 포레의 게시글 5개씩, 멤버 아이디 순, 포레 아이디순, 보드 최신 순
--CREATE OR replace VIEW v_member_b5 
--AS 
  SELECT * 
  FROM  (SELECT * 
         FROM   v_board_all
         WHERE  TYPE = 2 
                AND ROWNUM <= 5 
                and foret_id in (select foret_id from v_member_f where id = 1)
         UNION 
         SELECT * 
         FROM   v_board_all
         WHERE  TYPE = 3 
                AND ROWNUM <= 5 
         UNION 
         SELECT * 
         FROM   v_board_all
         WHERE  TYPE = 4 
                AND ROWNUM <= 5); 
                
-- 보드 모든 정보     
select * from v_board_all;
CREATE OR REPLACE VIEW v_board_all
AS 
  SELECT board.*, 
         temp1.cnt AS board_like, 
         temp2.cnt AS board_comment,
         board_photo.filename AS photo
  FROM   board 
         left outer join(SELECT board_id, 
                                Count(*) AS cnt 
                         FROM   like_board 
                         GROUP  BY board_id) temp1 
                      ON board.id = temp1.board_id 
         left outer join(SELECT board_id, 
                                Count(*) AS cnt 
                         FROM   board_comment 
                         GROUP  BY board_id) temp2 
                      ON board.id = temp2.board_id 
         left outer join board_photo
                      ON board.id = board_photo.board_id
  ORDER  BY board.foret_id asc,
            board.type asc,
            board.id desc,
            board_photo.id asc; 
  
-- 포레_사진 조회
CREATE OR REPLACE VIEW V_FORET_AND_PHOTO 
AS 
  SELECT foret.*,
         foret_photo.filename AS photo 
    FROM foret 
         -- foret_photo    
         LEFT OUTER JOIN foret_photo 
                      ON foret.id = foret_photo.id
   ORDER BY foret.id asc;

-- 포레_태그 조회    
CREATE OR REPLACE VIEW V_FORET_T 
AS 
  SELECT foret.id     AS id, 
         tag.tag_name AS tag_name
    FROM foret 
         -- foret_tag     
         LEFT OUTER JOIN foret_tag 
                      ON foret.id = foret_tag.id 
         LEFT OUTER JOIN tag 
                      ON foret_tag.tag_id = tag.tag_id 
   ORDER BY foret.id, 
            tag.tag_id; 

-- 포레_지역 조회    
CREATE OR REPLACE VIEW V_FORET_R 
AS 
  SELECT foret.id AS id, 
         region.si AS region_si, 
         region.gu AS region_gu 
    FROM foret 
         -- foret_region    
         LEFT OUTER JOIN foret_region 
                      ON foret.id = foret_region.id 
         LEFT OUTER JOIN region 
                      ON foret_region.region_id = region.region_id 
   ORDER BY foret.id, 
            region.region_id; 
            
-- 포레 모든 정보
CREATE OR replace VIEW V_FORET_ALL
AS 
  SELECT foret.id             AS id, 
         foret.leader_id      AS leader_id, 
         foret.name           AS name, 
         foret.introduce      AS introduce, 
         foret.max_member     AS max_member, 
         foret.reg_date       AS reg_date, 
         foret_photo.filename AS photo,
         tag.tag_name         AS tag_name, 
         region.si            AS si, 
         region.gu            AS gu,   
         foret_member.member_id AS member_id
  FROM   foret 
         left outer join foret_tag 
                      ON foret.id = foret_tag.id 
         left outer join tag 
                      ON foret_tag.tag_id = tag.tag_id 
         left outer join foret_region 
                      ON foret.id = foret_region.id 
         left outer join region 
                      ON foret_region.region_id = region.region_id 
         left outer join foret_photo 
                      ON foret.id = foret_photo.id 
         left outer join foret_member
                      ON foret.id = foret_member.foret_id
  ORDER  BY foret.id asc, 
            tag.tag_id asc,
            region.region_id asc,
            foret_member.member_id asc; 
      
-- 멤버 모든 정보      
CREATE OR replace VIEW V_MEMBER_ALL 
AS 
  SELECT member.*, 
         member_photo.filename   AS photo, 
         tag.tag_name            AS tag_name, 
         region.si               AS region_si, 
         region.gu               AS region_gu, 
         like_board.board_id     AS like_board, 
         like_comment.comment_id AS like_comment, 
         v_member_f.foret_id     AS foret_id 
  FROM   member 
         left outer join member_photo 
                      ON member.id = member_photo.id 
         left outer join member_tag 
                      ON member.id = member_tag.id 
         left outer join tag 
                      ON member_tag.tag_id = tag.tag_id 
         left outer join member_region 
                      ON member.id = member_region.id 
         left outer join region 
                      ON member_region.region_id = region.region_id 
         left outer join like_board 
                      ON member.id = like_board.id 
         left outer join like_comment 
                      ON member.id = like_comment.id 
         left outer join v_member_f 
                      ON member.id = v_member_f.id 
  ORDER  BY member.id ASC, 
            tag.tag_id ASC, 
            region.region_id ASC, 
            like_board.board_id ASC, 
            like_comment.comment_id ASC,
            v_member_f.foret_id ASC; 
      
      
------------------------------------------------------------------------------------------------------------------------
-- 홈 프레그먼트
SELECT 
v_board_all.id as id,
v_board_all.writer as writer,
v_board_all.foret_id as foret_id,
v_board_all.type as type,
v_board_all.hit as hit,
v_board_all.subject as subject,
v_board_all.content as content,
v_board_all.reg_date as reg_date,
v_board_all.edit_date as edit_date,
v_board_all.board_like as board_like,
v_board_all.board_comment as board_comment,
v_board_all.photo as board_photo,
v_foret_and_photo.name as foret_name,
v_foret_and_photo.photo as foret_photo
FROM   v_board_all 
left outer join v_foret_and_photo
on v_board_all.foret_id = v_foret_and_photo.id
WHERE  v_board_all.id IN (SELECT id 
              FROM   (SELECT Row_number() 
                               OVER( 
                                 partition BY board.foret_id, board.type 
                                 ORDER BY board.foret_id ASC, board.type ASC, 
                               board.id 
                               DESC ) 
                             row_num 
                                     , 
                             board.* 
                      FROM   board 
                      ORDER  BY board.foret_id ASC, 
                                board.type ASC, 
                                board.id DESC) 
              WHERE  foret_id IN(SELECT foret_id 
                                 FROM   v_member_f 
                                 WHERE  id = 1) 
                     AND row_num <= 3) 
ORDER  BY v_board_all.foret_id ASC, 
          v_board_all.type ASC, 
          v_board_all.id DESC,
          v_board_all.photo ASC;
            
           
-------------------------------------------------------------------------------------------------------------------------
-- BOARD SELECT
SELECT v_board_all.id, 
       v_board_all.writer, 
       tempBoard.nickname          AS writer_nickname, 
       tempBoard.photo             AS writer_photo, 
       v_board_all.foret_id, 
       v_board_all.type, 
       v_board_all.hit, 
       v_board_all.subject, 
       v_board_all.content, 
       v_board_all.reg_date, 
       v_board_all.edit_date, 
       v_board_all.board_like, 
       v_board_all.board_comment, 
       v_board_all.photo, 
       tempComment.id              AS comment_id, 
       tempComment.writer          AS comment_writer, 
       tempComment.writer_nickname AS comment_writer_nickname, 
       tempComment.writer_photo    AS comment_writer_photo, 
       tempComment.content         AS comment_content, 
       tempComment.reg_date        AS comment_reg_date, 
       tempComment.group_no        AS comment_group_no 
FROM   v_board_all 
       LEFT OUTER JOIN (SELECT board_comment.id, 
                               board_comment.board_id, 
                               board_comment.writer, 
                               temp.NAME     AS writer_name, 
                               temp.nickname AS writer_nickname, 
                               temp.photo    AS writer_photo, 
                               board_comment.content, 
                               board_comment.reg_date, 
                               board_comment.group_no 
                        FROM   board_comment 
                               LEFT OUTER JOIN (SELECT member.id, 
                                                       member.NAME, 
                                                       member.nickname, 
                                                       member_photo.filename AS 
                                                       photo 
                                                FROM   member 
                               LEFT OUTER JOIN member_photo 
                                            ON 
                               member.id = member_photo.id) 
                                               temp 
                                            ON board_comment.writer = temp.id) 
                                          tempComment 
                    ON v_board_all.id = tempComment.board_id 
       LEFT OUTER JOIN (SELECT member.id, 
                               member.NAME, 
                               member.nickname, 
                               member_photo.filename AS photo 
                        FROM   member 
                               LEFT OUTER JOIN member_photo 
                                            ON member.id = member_photo.id) 
                       tempBoard 
                    ON v_board_all.writer = tempBoard.id 
WHERE  v_board_all.id = 42
ORDER  BY v_board_all.id DESC, 
          tempComment.group_no ASC, 
          tempComment.id; 
-------------------------------------------------------------------------------------------------------------------------
-- 인기 포레 
SELECT * 
FROM  (SELECT Dense_rank() 
                OVER( 
                  ORDER BY cnt.cnt DESC nulls last, v_foret_all.id ) row_num, 
              v_foret_all.*, 
              cnt.cnt 
       FROM   v_foret_all 
              LEFT OUTER JOIN (SELECT board.foret_id, 
                                      Count(*) AS cnt 
                               FROM   board 
                               WHERE  type != 0 
                                      AND type != 1 
                               GROUP  BY foret_id) cnt 
                           ON v_foret_all.id = cnt.foret_id) 
WHERE  row_num <= 10; 

-------------------------------------------------------------------------------------------------------------------------
-- 검색 키워드
CREATE OR replace VIEW v_search_keyword 
AS 
  SELECT DISTINCT 'tag'        AS TYPE, 
                  tag.tag_name AS NAME 
  FROM   tag 
  UNION ALL 
  SELECT DISTINCT 'region_si' AS TYPE, 
                  region.si   AS NAME 
  FROM   region 
  UNION ALL 
  SELECT DISTINCT 'region_gu' AS TYPE, 
                  region.gu   AS NAME 
  FROM   region 
  UNION ALL 
  SELECT DISTINCT 'foret'    AS TYPE, 
                  foret.name AS NAME 
  FROM   foret; 
  
---------------------------------------------------------------------------------------------------------------------------
-- 키워드 검색
SELECT * 
FROM   v_foret_all 
       LEFT OUTER JOIN (SELECT board.foret_id, 
                               Count(*) AS cnt 
                        FROM   board 
                        WHERE  type != 0 
                               AND type != 1 
                        GROUP  BY foret_id) cnt 
                    ON v_foret_all.id = cnt.foret_id 
WHERE  id IN (SELECT id 
              FROM   v_foret_all 
              WHERE  si = '성남시') 
ORDER  BY cnt DESC nulls last, 
          id DESC; 
