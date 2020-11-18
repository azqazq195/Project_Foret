--프리게시판 관리 테이블
create table freeboard ( 
    freeboard_seq number not null,
    freeboard_hit number,
    freeboard_like_count number,
    freeboard_reply_count number,
    member_number varchar2(50), --회원번호
    freeboard_subject varchar2(200),
    freeboard_content varchar2(3000),
    freeboard_write_date varchar2(50),
    freeboard_edit_date varchar2(50)
);

drop table freeboard purge;

-- 시퀸스 객체 생성
create sequence freeboard_seq nocache nocycle;

-- 시퀸스 객체 삭제
drop sequence freeboard_seq;

select*from freeboard;
select*from freeboard where freeboard_seq=1;

insert into freeboard values (freeboard_seq.nextval, 0, 0, 0, '91989202', '제목이에요', '내용이에요', sysdate, sysdate);

--조회수
update freeboard set freeboard_hit=freeboard_hit+1 where freeboard_seq=1;

--좋아요, 댓글 수 증가, 감소
update freeboard set freeboard_like_count=freeboard_like_count+1 where freeboard_seq=1;
update freeboard set freeboard_like_count=freeboard_like_count-1 where freeboard_seq=1;
update freeboard set freeboard_reply_count=freeboard_reply_count+1 where freeboard_seq=1;
update freeboard set freeboard_reply_count=freeboard_reply_count-1 where freeboard_seq=1;

--글 수정
update freeboard set freeboard_subject='제목 수정하기', freeboard_content='내용 수정하기', freeboard_edit_date=sysdate 
where freeboard_seq=1 and member_number='91989202';

--글 삭제
delete from freeboard where freeboard_seq=1 and member_number='91989202';

--글 정렬
--1. (최신글순)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_write_date desc) tt)
where rn>=1 and rn<=20;
--2. (댓글순)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_reply_count desc) tt)
where rn>=1 and rn<=20;
--3. (좋아요순)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_like_count desc) tt)
where rn>=1 and rn<=20;

commit;

------------------------------------------------------------------------------------------------
--프리게시판 댓글 관리 테이블
create table freeboard_reply ( 
    freeboard_seq number not null, --게시판글 번호
    freeboard_reply_seq number not null, -- 댓글 번호
    freeboard_reply_member_number varchar2(50), --댓글 쓴 사람 회원번호
    freeboard_member_number varchar2(50), --게시글쓴 사람 회원번호
    freeboard_reply_content varchar2(500),
    freeboard_reply_count number,
    freeboard_reply_date varchar2(50)
);

drop table freeboard_reply purge;

-- 시퀸스 객체 생성
create sequence freeboard_reply_seq nocache nocycle;

-- 시퀸스 객체 삭제
drop sequence freeboard_reply_seq;

select*from freeboard_reply;
select*from freeboard_reply where freeboard_seq=1;
select*from freeboard_reply where freeboard_reply_seq=1 and freeboard_seq=1;

insert into freeboard_reply values (1, freeboard_reply_seq.nextval,  '97309202', '91989202', '댓글 내용이에요', 1, sysdate);

-- 댓글 수 감소
update freeboard_reply set freeboard_reply_count=freeboard_reply_count+1 where freeboard_reply_seq=1;
update freeboard_reply set freeboard_reply_count=freeboard_reply_count-1 where freeboard_reply_seq=1;

--글 수정
update freeboard_reply set freeboard_reply_content='댓글 내용 수정하기' where freeboard_reply_seq=1 and freeboard_reply_member_number='97309202';

--댓글 삭제
delete from freeboard_reply where freeboard_reply_seq=1 and freeboard_reply_member_number='97309202';

commit;

-----------------------------------------------------------------------------------
create table like_save ( 
    boardtype number not null,          --익명게시판(0), 포레게시판(1) 구분
    foretNumber number,
    seq number
);

drop table like_save purge;

insert into like_save values (0, null, 1);
insert into like_save values (0, null, 2);
select*from like_save;
select*from like_save where typeboard=0 and seq=1;
select seq from like_save where typeboard=0 and seq=1;
delete from like_save where typeboard=0 and seq=1; 

commit;