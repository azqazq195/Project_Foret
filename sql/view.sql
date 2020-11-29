COMMIT; 

select * from member;
select * from member_photo;
select * from member_photo where member_id = 8;
select * from member_tag;
select * from member_region;
select * from tag;
select * from region;

-- CREATE OR REPLACE VIEW V_MEMBER AS    
------------- 목차--------------   
-- 멤버 모든 데이터 조회   
SELECT * 
  FROM v_member; 

-- 포레 모든 데이터 조회
SELECT * 
  FROM V_FORET; 

-- 멤버_테그 조회   
SELECT * 
  FROM v_member_t; 

-- 멤버_지역 조회   
SELECT * 
  FROM v_member_r; 

-- 멤버_사진 조회   
SELECT * 
  FROM v_member_p; 

-- 멤버_태그 등록 갯수 내림차순    
SELECT * 
  FROM cv_member_t; 

-- 멤버_지역 등록 갯수 내림차순    
SELECT * 
  FROM cv_member_r; 

-- 멤버_보드 좋아요
select * from board;
insert into board values(SEQ_BOARD_ID.nextval, 4, null, 0, 0, '제목', '내용', sysdate, sysdate);
select * from like_board;
delete from like_board where id = 6 and board_id = 2;
insert into like_board values(6,2);
-- 멤버_댓글 좋아요
--   
--   
--   
--   
--   
--   
--   
--   
--   
--   
-- 모든 데이터 조회  
CREATE OR REPLACE VIEW V_MEMBER 
AS 
  SELECT member.id       AS id, 
         member.name     AS member_name, 
         member.nickname AS nickname, 
         member.email    AS email, 
         member.reg_date AS member_reg_date, 
         tag.tag_name    AS tag_name, 
         region.si, 
         region.gu, 
         member_photo.filename  AS photo_name, 
         foret.name      AS foret_name 
    FROM member 
         -- member_tag    
         LEFT OUTER JOIN member_tag 
                      ON member.id = member_tag.id 
         LEFT OUTER JOIN tag 
                      ON member_tag.tag_id = tag.tag_id 
         -- member_region    
         LEFT OUTER JOIN member_region 
                      ON member.id = member_region.id 
         LEFT OUTER JOIN region 
                      ON member_region.region_id = region.region_id 
         -- member_photo    
         LEFT OUTER JOIN member_photo 
                      ON member.id = member_photo.member_id 
         -- member_foret    
         LEFT OUTER JOIN foret_member 
                      ON member.id = foret_member.member_id 
         LEFT OUTER JOIN foret 
                      ON foret_member.foret_id = foret.id 
   ORDER BY member.id, 
            tag.tag_id; 

-- 멤버_태그 조회    
CREATE OR REPLACE VIEW V_MEMBER_T 
AS 
  SELECT member.id    AS member_id, 
         tag.tag_name AS tag 
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
  SELECT member.id AS member_id, 
         region.si AS si, 
         region.gu AS gu 
    FROM member 
         -- member_tag     
         LEFT OUTER JOIN member_region 
                      ON member.id = member_region.id 
         LEFT OUTER JOIN region 
                      ON member_region.region_id = region.region_id 
   ORDER BY member.id, 
            region.region_id; 

-- 멤버_사진 조회  
CREATE OR REPLACE VIEW V_MEMBER_P 
AS 
  SELECT member.id             AS member_id, 
         member_photo.filename AS filename 
    FROM member 
         -- member_photo     
         LEFT OUTER JOIN member_photo 
                      ON member.id = member_photo.member_id 
   ORDER BY member.id, 
            member_photo.id; 

-- 멤버_태그 등록 갯수 내림차순 
CREATE OR REPLACE VIEW CV_MEMBER_T 
AS 
  SELECT Count(*)     AS cnt, 
         tag.tag_name AS tag 
    FROM member_tag 
         LEFT OUTER JOIN tag 
                      ON member_tag.tag_id = tag.tag_id 
   GROUP BY tag.tag_name, 
            tag.tag_id 
   ORDER BY cnt DESC, 
            tag.tag_id; 

-- 멤버_지역 등록 갯수 내림차순 
CREATE OR REPLACE VIEW CV_MEMBER_R 
AS 
  SELECT Count(*)  AS cnt, 
         region.si AS si, 
         region.gu AS gu 
    FROM member_region 
         LEFT OUTER JOIN region 
                      ON member_region.region_id = region.region_id 
   GROUP BY region.region_id, 
            region.si, 
            region.gu 
   ORDER BY cnt DESC, 
            region.region_id; 
            
-- 모든 포레 정보 조회
CREATE OR REPLACE VIEW V_FORET
AS 
  SELECT foret.id            AS id, 
         foret.leader_id     AS leader_id, 
         foret.name          AS name, 
         foret.introduce     AS introduce, 
         foret.max_member    AS max_member, 
         foret.reg_date      AS reg_date,
         tag.tag_name        AS tag_name, 
         region.si, 
         region.gu, 
         foret_photo.filename  AS photo_name 
    FROM foret 
         -- foret_tag    
         LEFT OUTER JOIN foret_tag 
                      ON foret.id = foret_tag.id 
         LEFT OUTER JOIN tag 
                      ON foret_tag.tag_id = tag.tag_id 
         -- foret_region    
         LEFT OUTER JOIN foret_region 
                      ON foret.id = foret_region.id 
         LEFT OUTER JOIN region 
                      ON foret_region.region_id = region.region_id 
         -- foret_photo    
         LEFT OUTER JOIN foret_photo 
                      ON foret.id = foret_photo.foret_id 
         -- foret_member    
   ORDER BY foret.id, 
            tag.tag_id; 