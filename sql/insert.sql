--- member
commit;
select * from member order by id asc;
select * from member_photo order by id asc;
insert into member values(seq_member_id.nextval, '이름1', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름2', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름3', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름4', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름5', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름6', '이메일','password', '닉네임', '생일',  sysdate);
insert into member values(seq_member_id.nextval, '이름7', '이메일','password', '닉네임', '생일',  sysdate);




--- tag
commit;
select * from member;
select * from tag;
select * from region;

SELECT tag_id 
		FROM   tag 
		WHERE  tag_name = '과학'
        or tag_name = '수학';
insert into tag values(seq_tag_id.nextval, '영어');
insert into tag values(seq_tag_id.nextval, '국어');
insert into tag values(seq_tag_id.nextval, '수학');
insert into tag values(seq_tag_id.nextval, '과학');


--- region
select * from region;
SELECT  region_id 
		FROM   region 
		WHERE  si = '서울시' And gu = '강남구'
        or si = '성남시' And gu = '수정구';
insert into region values (seq_region_id.nextval, '서울시', '강남구');
insert into region values (seq_region_id.nextval, '서울시', '종로구');
insert into region values (seq_region_id.nextval, '서울시', '중구');
insert into region values (seq_region_id.nextval, '서울시', '관악구');
insert into region values (seq_region_id.nextval, '서울시', '송파구');
insert into region values (seq_region_id.nextval, '성남시', '분당구');
insert into region values (seq_region_id.nextval, '성남시', '수정구');
insert into region values (seq_region_id.nextval, '성남시', '중원구');


--- photo
select * from member_photo;
insert into member_photo values (seq_member_photo_id.nextval, 1, '경로', '이름1', '이름', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 2, '경로', '이름2', '이름', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 3, '경로', '이름3', '이름', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 4, '경로', '이름4', '이름', 23, 'jpg', sysdate);

select * from foret_photo;
insert into foret_photo values (seq_foret_photo_id.nextval, 1, '경로', '이름1', '이름', 23, 'jpg', sysdate);

select * from board_photo;
insert into board_photo values (seq_board_photo_id.nextval, 2, '경로', '이름1', '이름', 23, 'jpg', sysdate);


--- foret
select * from member;
select * from foret;
select * from tag;

UPDATE foret
SET    leader_id = 6,
        name = '포레수정',
        introduce = '소개수정',
        max_member = 2
WHERE  id = 3;

insert into foret values (SEQ_FORET_ID.nextval, 6, '포레1', '소개', 10, sysdate);
insert into foret values (SEQ_FORET_ID.nextval, 2, '포레2', '소개', 10, sysdate);
insert into foret values (SEQ_FORET_ID.nextval, 3, '포레3', '소개', 10, sysdate);

--- 포레 생성시 리더를 foret_member에 추가시켜야 한다.
--- 포레 아이디, 멤버 아이디
select * from foret_member;
insert into foret_member values (1, 1);
insert into foret_member values (2, 2);
insert into foret_member values (3, 3);

-- 멤버 추가
insert into foret_member values (1, 3);
insert into foret_member values (1, 4);
insert into foret_member values (1, 5);


-- 멤버가 태그 등록시
-- 6번 등록 안함
select * from member_tag;
INSERT ALL 
INTO member_tag 
VALUES (4, 1) 
INTO member_tag 
VALUES (4, 2) 
SELECT * 
FROM   dual; 
insert into member_tag (id, tag_id) values (1,1), (1, 2);
insert into member_tag values (1,1);
insert into member_tag values (1,2);
insert into member_tag values (1,3);
insert into member_tag values (2,4);
insert into member_tag values (2,3);
insert into member_tag values (3,3);
insert into member_tag values (4,2);
insert into member_tag values (5,1);
-- insert into member_tag values (6,2);
insert into member_tag values (7,3);
insert into member_tag values (8,4);
insert into member_tag values (9,3);
insert into member_tag values (7,2);
insert into member_tag values (7,4);
insert into member_tag values (5,2);
insert into member_tag values (5,3);


-- 멤버가 지역 등록시
-- 7번 등록 안함
select * from member_region;
insert into member_region values (1,3);
insert into member_region values (2,1);
insert into member_region values (3,2);
insert into member_region values (4,4);
insert into member_region values (5,6);
insert into member_region values (6,8);
-- insert into member_region values (7,3);
insert into member_region values (8,7);
insert into member_region values (9,3);
