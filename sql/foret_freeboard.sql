--�����Խ��� ���� ���̺�
create table freeboard ( 
    freeboard_seq number not null,
    freeboard_hit number,
    freeboard_like_count number,
    freeboard_reply_count number,
    member_number varchar2(50), --ȸ����ȣ
    freeboard_subject varchar2(200),
    freeboard_content varchar2(3000),
    freeboard_write_date varchar2(50),
    freeboard_edit_date varchar2(50)
);

drop table freeboard purge;

-- ������ ��ü ����
create sequence freeboard_seq nocache nocycle;

-- ������ ��ü ����
drop sequence freeboard_seq;

select*from freeboard;
select*from freeboard where freeboard_seq=1;

insert into freeboard values (freeboard_seq.nextval, 0, 0, 0, '91989202', '�����̿���', '�����̿���', sysdate, sysdate);

--��ȸ��
update freeboard set freeboard_hit=freeboard_hit+1 where freeboard_seq=1;

--���ƿ�, ��� �� ����, ����
update freeboard set freeboard_like_count=freeboard_like_count+1 where freeboard_seq=1;
update freeboard set freeboard_like_count=freeboard_like_count-1 where freeboard_seq=1;
update freeboard set freeboard_reply_count=freeboard_reply_count+1 where freeboard_seq=1;
update freeboard set freeboard_reply_count=freeboard_reply_count-1 where freeboard_seq=1;

--�� ����
update freeboard set freeboard_subject='���� �����ϱ�', freeboard_content='���� �����ϱ�', freeboard_edit_date=sysdate 
where freeboard_seq=1 and member_number='91989202';

--�� ����
delete from freeboard where freeboard_seq=1 and member_number='91989202';

--�� ����
--1. (�ֽűۼ�)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_write_date desc) tt)
where rn>=1 and rn<=20;
--2. (��ۼ�)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_reply_count desc) tt)
where rn>=1 and rn<=20;
--3. (���ƿ��)
select * from
(select rownum rn, tt.* from
(select * from freeboard order by freeboard_like_count desc) tt)
where rn>=1 and rn<=20;

commit;

------------------------------------------------------------------------------------------------
--�����Խ��� ��� ���� ���̺�
create table freeboard_reply ( 
    freeboard_seq number not null, --�Խ��Ǳ� ��ȣ
    freeboard_reply_seq number not null, -- ��� ��ȣ
    freeboard_reply_member_number varchar2(50), --��� �� ��� ȸ����ȣ
    freeboard_member_number varchar2(50), --�Խñ۾� ��� ȸ����ȣ
    freeboard_reply_content varchar2(500),
    freeboard_reply_count number,
    freeboard_reply_date varchar2(50)
);

drop table freeboard_reply purge;

-- ������ ��ü ����
create sequence freeboard_reply_seq nocache nocycle;

-- ������ ��ü ����
drop sequence freeboard_reply_seq;

select*from freeboard_reply;
select*from freeboard_reply where freeboard_seq=1;
select*from freeboard_reply where freeboard_reply_seq=1 and freeboard_seq=1;

insert into freeboard_reply values (1, freeboard_reply_seq.nextval,  '97309202', '91989202', '��� �����̿���', 1, sysdate);

-- ��� �� ����
update freeboard_reply set freeboard_reply_count=freeboard_reply_count+1 where freeboard_reply_seq=1;
update freeboard_reply set freeboard_reply_count=freeboard_reply_count-1 where freeboard_reply_seq=1;

--�� ����
update freeboard_reply set freeboard_reply_content='��� ���� �����ϱ�' where freeboard_reply_seq=1 and freeboard_reply_member_number='97309202';

--��� ����
delete from freeboard_reply where freeboard_reply_seq=1 and freeboard_reply_member_number='97309202';

commit;

-----------------------------------------------------------------------------------
create table like_save ( 
    boardtype number not null,          --�͸�Խ���(0), �����Խ���(1) ����
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