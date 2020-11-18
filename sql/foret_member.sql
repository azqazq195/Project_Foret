create table   member_table   (   
member_id   number(   30   ) primary key,
member_name   varchar2(   20   ) not null,
member_nickname   varchar2(   20   ) not null,
member_email   varchar2(   50   ) not null,
member_birth   varchar2(   30   ) not null,
member_join_date   date,
member_chain   varchar2(   30   ),
member_address   varchar2(   50   ),
member_photo   varchar2(   500   )
);  

select * from member_table;

create sequence member_no nocache nocycle;

insert into member_table value (member_no.nextval, '이메일', '닉네임', );
insert into freeboard_table values (freeboard_no.nextval, 0, 0, 0, #{member_id}, #{freeboard_subject}, #{freeboard_content}, sysdate, sysdate)

commit;