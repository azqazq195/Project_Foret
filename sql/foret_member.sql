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

create sequence	seq_member_id	nocache nocycle;	

insert into member_table values (seq_member_id.nextval, '이름', '닉네임', '이메일', '19000000', sysdate, '구글', '서울시 강남구', 'url');

update member_table set 
member_name = '이름 수정',
member_nickname = '닉네임 수정',
member_email = '이메일 수정',
member_birth = '생일 수정',
member_chain = '연동 수정',
member_address = '주소 수정',
member_photo = '사진 수정' 
where member_id=1;

commit;