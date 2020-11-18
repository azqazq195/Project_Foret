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

insert into member_table values (seq_member_id.nextval, '�̸�', '�г���', '�̸���', '19000000', sysdate, '����', '����� ������', 'url');

update member_table set 
member_name = '�̸� ����',
member_nickname = '�г��� ����',
member_email = '�̸��� ����',
member_birth = '���� ����',
member_chain = '���� ����',
member_address = '�ּ� ����',
member_photo = '���� ����' 
where member_id=1;

commit;