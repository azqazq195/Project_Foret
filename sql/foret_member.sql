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

commit;