--- member
commit;
select * from member order by id asc;
select * from member_photo order by id asc;
insert into member values(seq_member_id.nextval, '�̸�1', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�2', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�3', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�4', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�5', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�6', '�̸���','password', '�г���', '����',  sysdate);
insert into member values(seq_member_id.nextval, '�̸�7', '�̸���','password', '�г���', '����',  sysdate);




--- tag
commit;
select * from member;
select * from tag;
select * from region;

SELECT tag_id 
		FROM   tag 
		WHERE  tag_name = '����'
        or tag_name = '����';
insert into tag values(seq_tag_id.nextval, '����');
insert into tag values(seq_tag_id.nextval, '����');
insert into tag values(seq_tag_id.nextval, '����');
insert into tag values(seq_tag_id.nextval, '����');


--- region
select * from region;
SELECT  region_id 
		FROM   region 
		WHERE  si = '�����' And gu = '������'
        or si = '������' And gu = '������';
insert into region values (seq_region_id.nextval, '�����', '������');
insert into region values (seq_region_id.nextval, '�����', '���α�');
insert into region values (seq_region_id.nextval, '�����', '�߱�');
insert into region values (seq_region_id.nextval, '�����', '���Ǳ�');
insert into region values (seq_region_id.nextval, '�����', '���ı�');
insert into region values (seq_region_id.nextval, '������', '�д籸');
insert into region values (seq_region_id.nextval, '������', '������');
insert into region values (seq_region_id.nextval, '������', '�߿���');


--- photo
select * from member_photo;
insert into member_photo values (seq_member_photo_id.nextval, 1, '���', '�̸�1', '�̸�', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 2, '���', '�̸�2', '�̸�', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 3, '���', '�̸�3', '�̸�', 23, 'jpg', sysdate);
insert into member_photo values (seq_member_photo_id.nextval, 4, '���', '�̸�4', '�̸�', 23, 'jpg', sysdate);

select * from foret_photo;
insert into foret_photo values (seq_foret_photo_id.nextval, 1, '���', '�̸�1', '�̸�', 23, 'jpg', sysdate);

select * from board_photo;
insert into board_photo values (seq_board_photo_id.nextval, 2, '���', '�̸�1', '�̸�', 23, 'jpg', sysdate);


--- foret
select * from member;
select * from foret;
select * from tag;

UPDATE foret
SET    leader_id = 6,
        name = '��������',
        introduce = '�Ұ�����',
        max_member = 2
WHERE  id = 3;

insert into foret values (SEQ_FORET_ID.nextval, 6, '����1', '�Ұ�', 10, sysdate);
insert into foret values (SEQ_FORET_ID.nextval, 2, '����2', '�Ұ�', 10, sysdate);
insert into foret values (SEQ_FORET_ID.nextval, 3, '����3', '�Ұ�', 10, sysdate);

--- ���� ������ ������ foret_member�� �߰����Ѿ� �Ѵ�.
--- ���� ���̵�, ��� ���̵�
select * from foret_member;
insert into foret_member values (1, 1);
insert into foret_member values (2, 2);
insert into foret_member values (3, 3);

-- ��� �߰�
insert into foret_member values (1, 3);
insert into foret_member values (1, 4);
insert into foret_member values (1, 5);


-- ����� �±� ��Ͻ�
-- 6�� ��� ����
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


-- ����� ���� ��Ͻ�
-- 7�� ��� ����
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
