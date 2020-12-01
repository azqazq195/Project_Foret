COMMIT; 

SELECT * FROM V_MEMBER_AND_PHOTO;
SELECT * FROM V_MEMBER_T; 
SELECT * FROM V_MEMBER_R;
SELECT * FROM V_MEMBER_BL;
select * from like_board;
select * from like_comment;
SELECT * FROM V_MEMBER_CL;
SELECT * FROM V_MEMBER_F;
SELECT * FROM V_TAG_RANK;
SELECT * FROM V_REGION_RANK;

SELECT * FROM V_MEMBER_F where member_id = 1;


select * from member;
select * from member_tag;
select * from member_region;
select * from tag;
select * from region;
select * from like_board;
select * from like_comment;
select * from board;
select * from board_comment order by group_no asc, id asc;
select * from foret;
select * from foret_member order by foret_id asc, member_id asc;
select * from foret_region;

delete from board_comment where id = 1;
update foret set name = '포레2', introduce = '포레2입니다' where id=2;

insert into board_comment values(seq_board_comment_id.nextval, 2, 2, '댓글', sysdate, 2);

insert into like_board values(2, 3);
insert into like_board values(2, 3);

insert into like_comment values(2, 1);
insert into like_comment values(2, 2);

select tag_name, count(tag_name) as cnt from V_MEMBER_T group by tag_name ORDER by cnt desc;

select * from member_tag;
select * from member;
select * from tag;


insert into member_tag values (1, 1);
insert into member_tag values (1, 2);
insert into member_tag values (1, 3);
insert into member_tag values (2, 2);
insert into member_tag values (3, 1);
insert into member_tag values (4, 2);
insert into member_tag values (4, 1);
insert into member_tag values (4, 2);
insert into member_tag values (5, 1);
insert into member_tag values (5, 2);
insert into member_region values (1, 2);

select count(*) from member_tag;

select * from member;
select * from member_photo;
select * from member_photo where member_id = 8;
select * from member_tag;
select * from member_region;
select * from foret;
select * from foret_tag;
select * from foret_member;
select * from tag;
select * from region;
select * from board;
select * from board_photo;
select * from board_comment
    order by group_no asc, id asc;

delete from board_comment where board_id = 31;
insert into board_comment values (SEQ_BOARD_COMMENT_ID.nextval, 31, 10, '부모', sysdate, SEQ_BOARD_COMMENT_ID.nextval);
insert into board_comment values (SEQ_BOARD_COMMENT_ID.nextval, 31, 10, '자식', sysdate, 43);
delete from board_comment where id = 18;
select * from board_comment where group_no = 14;
delete from board_comment where id = 44;

update board_comment set content = '수정' where id = 39;
select Count(*) cnt from board_comment where group_no = 14;


-- CREATE OR REPLACE VIEW V_MEMBER AS    
------------- 목차--------------   
-- 멤버 모든 데이터 조회   
SELECT * 
  FROM v_member; 

-- 포레 모든 데이터 조회
SELECT * 
  FROM V_FORET; 
  
-- 보드 모든 데이터 조회
SELECT * 
  FROM V_BOARD; 

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

SELECT Count(*)     AS cnt, 
         tag.tag_id   AS tag_id,
         tag.tag_name AS tag_name
    FROM member_tag 
         LEFT OUTER JOIN tag 
                      ON member_tag.tag_id = tag.tag_id 
   GROUP BY tag.tag_name, 
            tag.tag_id 
   ORDER BY cnt DESC, 
            tag.tag_id;

select * from member_tag;
select * from foret_tag;

SELECT Count(*)     AS cnt, 
       tag.tag_id   AS tag_id, 
       tag.tag_name AS tag_name 
FROM  (SELECT * 
       FROM   member_tag 
       UNION ALL 
       SELECT * 
       FROM   foret_tag)ref_tag 
      LEFT OUTER JOIN tag 
                   ON ref_tag.tag_id = tag.tag_id 
GROUP  BY tag.tag_name, 
          tag.tag_id 
ORDER  BY cnt DESC, 
          tag.tag_id; 

 select * from member_tag 
union all 
select * from foret_tag;           
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
            
-- 모든 게시판 정보 조회
CREATE OR replace VIEW v_board 
AS 
  SELECT board.id              AS id, 
         board.writer          AS writer, 
         board.foret_id        AS foret_id, 
         board.TYPE            AS TYPE, 
         board.hit             AS hit, 
         board.subject         AS subject, 
         board.content         AS content, 
         board.reg_date        AS reg_date, 
         board.edit_date       AS edit_date, 
         board_comment.content AS board_comment,
         board_photo.filename  AS photo_name
     FROM   board 
         -- board_comment     
         left outer join board_comment 
                      ON board.id = board_comment.board_id 
         -- board_photo     
         left outer join board_photo 
                      ON board.id = board_photo.board_id 
  ORDER  BY board.id; 
             