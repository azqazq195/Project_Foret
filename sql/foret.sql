SELECT * FROM TAB;

commit;

--회원 talbe
create table   member_table   (   
member_id   number(   30   ) primary key,
member_email   varchar2(   50   ) not null,
member_nickname   varchar2(   20   ) not null,
member_birth   varchar2(   30   ) not null,
member_join_date   date,
member_chain   varchar2(   30   ),
member_address   varchar2(   50   ),
member_photo   varchar2(   500   )
);         

-- 회원 그룹 table
create table   member_group_table   (   
member_ib   number(   10   ) primary key,
group1   varchar2(   50   ),
group2   varchar2(   30   ),
group3   varchar2(   30   ),
group4   varchar2(   30   ),
group5   varchar2(   30   ),
group_join_count   number(   10   )
);         
--지역 tab table
create table   region_table   (   
region_number   number   primary key,
region_name   varchar2(   100   ),
region_count_member   number(   10   ),
region_count_board   number(   10   )
);         


--회원 태그 table
create table   member_tag_table   (   
member_Id   varchar2(   30   ) primary key,
member_tag_no1   number(   10   ),
member_tag_no2   number(   10   ),
member_tag_no3   number(   10   ),
member_tag_no4   number(   10   ),
member_tag_no5   number(   10   )
);         

--태그 table
create table   tag_table   (   
tag_no   number(   10   ) primary key,
tag_name   varchar2(   50   ),
tag_count_member   number(   10   ),
tag_count_board   number(   10   )
);         

--포레 그룹 table

create table   group_table   (   
group_no   number(   10   ) primary key,
group_name   varchar2(   50   ),
group_date_issued   date      ,
group_currunt_member_count   number(   10   ),
group_leader   varchar2(   30   ),
group_profile   varchar2(   600   ),
group_photo   varchar2(   300   ),
group_tag   varchar2(   50   ),
group_region   varchar2(   50   ),
group_max_member   number(   3   )
);      

--포레 회원 table
create table   group_member_table   (   
group_no   number(   10   ) primary key,
members_id   varchar2(   50   ),
members_count   number(   3   )
);         

--포레 게시글 table
create table   group_board_table   (   
group_no   number(   10   ) primary key,
board_no   varchar2(   50   ),
board_type   number(   10   ),
board_writed_date   date      ,
board_edited_date   date      ,
board_hit   number(   10   ),
board_like_count   number(   10   ),
board_writer   varchar2(   100   ),
board_subject   varchar2(   100   ),
board_content   varchar2(   1000   ),
board_photo_name   varchar2(   100   ),
board_comment_count   number(   10   )
);         

--포레 게시판 댓글 table
create table   group_board_comments_table   (   
board_no   number(   10   ) primary key,
comment_number   varchar2(   50   ),
comment_content   varchar2(   300   ),
comment_writer   varchar2(   30   ),
comment_date   date      ,
comment_to   varchar2(   10   )
);         

--익명 게시판 table
create table   freeboard_table   (   
freeboard_no   number       primary key,
freeboard_hit   number      ,
freeboard_like_count   number      ,
freeboard_comment_count   number      ,
member_id   varchar2(   50   ) not null,
freeboard_subject   varchar2(   200   ) not null,
freeboard_content   varchar2(   3000   ) not null,
freeboard_write_date   date      ,
freeboard_edit_date   date      
);         

--익명 게시판 댓글 관리 table
create table   freeboard_comment_table   (   
freeboard_no   number      ,
freeboard_comment_no   number      ,
freeboard_comment_writer   varchar2(   30   ),
freeboard_writer   varchar2(   30   ),
freeboard_comment_content   varchar2(   200   ),
freeboard_comment_date   varchar2(   50   )
);         

--내가 누른 좋아요 관리 table
create table   like_save_table   (   
member_id   number(   10   )  primary key,
board_type   number      ,
group_no   number      ,
text_no   number      
);         

--포레 일정 table
create table   group_schedule_table   (   
group_no   number(   30   ),
schedule_number   number(   30   ),
schedule_date   date      ,
schedule_subject   varchar2(   100   ),
schedule_issued_date   date      ,
member_id   varchar2(   50   )
);         


--채팅 table
create table   catting_table   (   
chatting_number   varchar2(   30   ),
chatting_type   varchar2(   30   ),
chatting_management_number   varchar2(   50   ),
firebase_id   varchar2(   50   ),
chatting_joined_member   varchar2(   50   )
);         

--관리자 table
create table   notice_table   (   
notice_number   number(   10   ),
notice_subject   varchar2(   100   ),
notice_content   varchar2(   2000   ),
notice_date   date      ,
notice_edit_date   date      ,
notice_photo   varchar2(   50   )
);         

--사진 관리 table
create table   photo_table   (   
photo_id   number(   10   ) primary key,
photo_name   varchar2(   100   ),
photo_path   varchar2(   100   ),
photo_type   varchar2(   50   ),
photo_date   varchar2(   50   ),
photo_added_date   date      
);         

select * from tab;

commit;

--시퀀스 생성
create sequence   seq_member_id   nocache nocycle;
create sequence   seq_tag_id   nocache nocycle;
create sequence   seq_group_id   nocache nocycle;
create sequence   seq_board_id   nocache nocycle;
create sequence   seq_freeboard_id   nocache nocycle;
create sequence   seq_foretschedule_id   nocache nocycle;
create sequence   seq_chat_id   nocache nocycle;
create sequence   seq_notice_id   nocache nocycle;
create sequence   seq_photo_id   nocache nocycle;


