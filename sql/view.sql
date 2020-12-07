COMMIT; 
select * from member;
select foret.name from foret
union all
select tag.tag_name from tag;

select * from foret;
select * from V_SEARCH_KEYWORD;
select * from tag;
select * from member where email = 'jakejeook5@gmail.com';


select * from board_comment;
delete from board_comment where id = 188;
commit;


select * from board_comment;
SELECT * FROM V_MEMBER_AND_PHOTO;
SELECT * FROM V_MEMBER_AND_PHOTO where email = 'jakeje5@gmail.com' and password = '123qwe@';
SELECT * FROM V_MEMBER_T; 
SELECT * FROM V_MEMBER_R;
select * from like_board;
select * from like_comment;
SELECT * FROM V_MEMBER_F;

select id, name from foret;


-- 좋아요 갯수, 댓글 갯수
select * from board;
insert into board values (seq_board_id.nextval, 1, null, 0,0, '테스트', '테스트', sysdate, sysdate);
select * from v_board_all where type = 0 order by board_like desc nulls last, id desc;
select * from v_board_all where type = 4 and foret_id = 58;

select * from v_FORET_ALL;
select * from v_member_ALL;

select * from v_FORET_ALL where id = 58;
select * from foret_member;
select * from v_board_all where foret_id = 58;

select * from v_FORET_ALL
full outer join v_board_all
on v_foret_all.id = v_board_all.foret_id where v_FORET_ALL.id = 58;

select foret.*, foret_photo.filename as photo, foret_member.member_id as member_id, foret_tag.tag_id from foret
full outer join foret_member
on foret.id = foret_member.foret_id 
left outer join foret_photo
on foret.id = foret_photo.id
full outer join foret_tag
on foret.id = foret_tag.id
where foret.id = 58;


select * from foret;
select * from foret_photo;

select * from foret
union all
select * from foret_photo;

select * from foret_member;



select * from board where foret_id =1 ;


-- 인기 포레
select v_foret_all.*, cnt.cnt from v_foret_all
left outer join (select board.foret_id, count(*) as cnt from board where type != 0 and type != 1 group by foret_id) cnt
on v_foret_all.id = cnt.foret_id
order by 
cnt.cnt desc nulls last,
v_foret_all.id;

select board.foret_id, count(*) as cnt from board where type != 0 and type != 1 group by foret_id;

select * from board order by foret_id asc;












-- 포레 이름으로 찾기
SELECT * 
FROM   v_foret_all 
WHERE  id IN (SELECT id 
              FROM   v_foret_all 
              WHERE  NAME = '포레4'); 

-- 포레 태그로 찾기
SELECT * 
FROM   v_foret_all 
WHERE  id IN (SELECT id 
              FROM   v_foret_all 
              WHERE  tag_name = '태그1'); 

-- 포레 지역으로 찾기
SELECT * 
FROM   v_foret_all 
WHERE  id IN (SELECT id 
              FROM   v_foret_all 
              WHERE  si = '서울시' 
                AND  gu = '관악구'); 

SELECT * 
FROM   V_MEMBER_ALL ;
-- 멤버 아이디로 찾기
SELECT * 
FROM   V_MEMBER_ALL 
WHERE  id = 1; 
         
-- 홈 프레그먼트
SELECT * 
FROM   v_board_all 
WHERE  id IN (SELECT id 
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
                     AND row_num <= 5) 
ORDER  BY foret_id ASC, 
          type ASC, 
          id DESC; 
          
          


select * from v_tag_rank;
select * from v_region_rank where rank <= 10;


select * from v_search_keyword where name = '태그1';


select * from v_foret_all where tag_name = '태그1';
                           
                           
     
                           
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
              WHERE  name = '포레4') 
ORDER  BY cnt DESC nulls last, 
          id DESC; 



        
 
commit;
     
     
SELECT * 
FROM   member_tag 
UNION ALL 
SELECT * 
FROM   foret_tag;
-- 최신 순
select * from
(
select 
dense_rank() OVER(
order by id desc
) num,
v_board_all.*
from v_board_all 
where foret_id = 58 and type = 4
) temp
where temp.num between 1 and 5
;
-- 조회수 순
select * from v_board_all 
where foret_id = 1 and type = 4
order by hit desc, id desc;
-- 좋아요 순
select 
dense_rank() OVER(
order by board_like desc, id desc
) num,
v_board_all.*
from v_board_all 
where foret_id = 58 and type = 4;

select 
dense_rank() OVER(
order by board_comment desc, id desc
) num,
v_board_all.*
from v_board_all 
where foret_id = 58 and type = 4;

select ROWNUM, v_board_all.* from v_board_all;

-- 댓글 순
select * from v_board_all 
where foret_id = 58 and type = 4
order by board_comment desc, id desc;

select * from v_board_all 
where foret_id is null and type = 0
order by board_comment desc, id desc;

select * from v_board_all ;





commit;

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

select * from v_foret_and_photo;

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





select * from foret;


select * from board;


select v_foret_all.*,
board.id as board_id,
board.writer as writer,
board.type as type,
board.hit as hit,
board.subject as subject,
board.content as content,
board.reg_date as board_reg_date,
board.edit_date as board_edit_date
from v_foret_all
left outer join board
on v_foret_all.id = board.foret_id;



























select * from v_board_all where id = 42;
select * from board_comment where board_id = 42;

		SELECT v_board_all.*, 
		       board_comment.id       AS comment_id, 
		       board_comment.writer   AS comment_writer, 
		       board_comment.content  AS comment_content, 
		       board_comment.reg_date AS comment_reg_date, 
		       board_comment.group_no AS comment_group_no 
		FROM   v_board_all 
		       LEFT OUTER JOIN board_comment 
		                    ON v_board_all.id = board_comment.board_id 
		WHERE  v_board_all.id = 42
		ORDER  BY v_board_all.id DESC, 
		          board_comment.group_no ASC, 
		          board_comment.id ;

select * from V_MEMBER_ALL where id=3;


-- BOARD SELECT
SELECT v_board_all.id, 
       v_board_all.writer, 
       tempBoard.NAME           AS writer_name, 
       tempBoard.photo          AS writer_photo, 
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
       tempComment.id           AS comment_id, 
       tempComment.writer       AS comment_writer, 
       tempComment.writer_name  AS comment_writer_name, 
       tempComment.writer_photo AS comment_writer_photo, 
       tempComment.content      AS comment_content, 
       tempComment.reg_date     AS comment_reg_date, 
       tempComment.group_no     AS comment_group_no 
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
          
select * from board_comment;

select 
board_comment.id,
board_comment.board_id,
board_comment.writer,
temp.name as writer_name,
temp.nickname as writer_nickname,
temp.photo as writer_photo,
board_comment.content,
board_comment.reg_date,
board_comment.group_no
from board_comment
left outer join (SELECT member.id, 
       member.NAME, 
       member.nickname, 
       member_photo.filename AS photo 
FROM   member 
       LEFT OUTER JOIN member_photo 
                    ON member.id = member_photo.id) temp
                       on board_comment.writer = temp.id;
                       
                       

(SELECT member.id, 
       member.NAME, 
       member.nickname, 
       member_photo.filename AS photo 
FROM   member 
       LEFT OUTER JOIN member_photo 
                    ON member.id = 
                       member_photo.id) temp
                       on board_comment.writer = temp.id;

SELECT member.id, 
       member.NAME, 
       member.nickname, 
       member_photo.filename AS photo 
FROM   member 
       LEFT OUTER JOIN member_photo 
                    ON member.id = 
                       member_photo.id;         
          

          


select 
member.id,
member.name,
member.nickname,
member_photo.filename as photo
from member
left outer join member_photo
on member.id = member_photo.id;

select * from V_FORET_ALL where name = '포레4';

select * from V_FORET_ALL;
select * from V_FORET_AND_PHOTO;
select * from V_FORET_T;
select * from V_FORET_R;
select * from foret_member;

select * from v_member_B;
select * from v_member_b5;

SELECT * FROM V_TAG_RANK;
SELECT * FROM V_REGION_RANK;

select * from v_board where foret_id is null;
select * from foret;

select * from foret_tag;

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
            


select * from foret_region;
select * from region;

-- 1 공지사항
-- 2 포레 공지사항
-- 3 포레 일정 게시판
-- 4 포레 게시판
-- 0 익명 게시판

select * from v_board;








  SELECT foret.id             AS id, 
         foret.leader_id      AS leader_id, 
         foret.name           AS name, 
         foret.introduce      AS introduce, 
         foret.max_member     AS max_member, 
         foret.reg_date       AS reg_date, 
         tag.tag_name         AS tag_name, 
         region.si            AS si, 
         region.gu            AS gu, 
         foret_photo.filename AS filename 
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
  ORDER  BY foret.id, 
            tag.tag_id; 

-- 타입별, 최신 순
SELECT * 
FROM   v_board 
WHERE  type = 3
and foret_id = 1
ORDER  BY id DESC; 
-- 타입별, 조회 순
SELECT * 
FROM   v_board 
WHERE  type = 0 
ORDER  BY hit desc,
id desc; 
-- 타입별, 좋아요 순
SELECT * 
FROM   v_board 
WHERE  type = 0 
ORDER  BY board_like desc,
id desc; 
-- 타입별, 댓글 순
SELECT * 
FROM   v_board 
WHERE  type = 0 
ORDER  BY board_comment desc,
id desc;



-- 댓글 조회 board_id
select * from board_comment
where board_id = 42 order by group_no asc, id asc;

-- 보드 조회 페이징, type, foret_id, startNum, endNum
SELECT * 
FROM   (SELECT rownum rn, 
               tt. * 
        FROM   (SELECT * 
                FROM   v_board 
                WHERE  type = 0 
                ORDER  BY hit DESC,
                          id DESC) tt) 
WHERE  rn >= 1 
       AND rn <= 10; 
       
       
       
       








select * from board_comment;


select * from like_board;
insert into like_board values (3,41);
            

            
            
SELECT * 
FROM  (
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
            board.id DESC) where type = 3 and rownum <=5;
            
SELECT * 
FROM  (
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
            board.id DESC) where type = 4 and rownum <=5;

-- 내가 속한 포레의 최신 5개
SELECT * 
FROM  (SELECT member.id       AS id, 
              foret_member.foret_id, 
              board.id        AS board_id, 
              board.type      AS type, 
              board.hit       AS hit, 
              board.subject   AS subject, 
              board.content   AS content, 
              board.reg_date  AS reg_date, 
              board.edit_date AS edit_date 
       FROM   member 
              LEFT OUTER JOIN foret_member 
                           ON member.id = foret_member.member_id 
              LEFT OUTER JOIN board 
                           ON foret_member.foret_id = board.foret_id 
       WHERE  type = 3 
       ORDER  BY board.reg_date DESC) 
WHERE  rownum <= 5; 
       
       




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
             