SELECT * FROM member;
SELECT * FROM foret;
SELECT * FROM foret_tag;
SELECT * FROM foret_region;
SELECT * FROM foret_photo;
SELECT * FROM tag;
SELECT * FROM region;
SELECT * FROM member_tag;
SELECT * FROM member_region;
SELECT * FROM member_photo;
SELECT * FROM member_foret;
SELECT * FROM member_like_board;
SELECT * FROM member_like_comment;
SELECT * FROM board;
SELECT * FROM board_photo;

INSERT INTO board (writer_id, foret_id, type, subject, content, reg_date, edit_date)
VALUES(1, 1, 2, '제목', '내용', NOW(), NOW());

DELETE FROM member_tag
WHERE member_id = 10;
            
# email check
SELECT * FROM member
WHERE email = 'azqazq195@gmail.com';
# login
SELECT * FROM member
WHERE email = 'azqazq195@gmail.com' AND PASSWORD = 'qwe123';

# member insert
INSERT INTO member (NAME, email, PASSWORD, nickname, birth, reg_date)
VALUES('홍길동', 'hong@gmail.com', 'qwe123', 'hong', '1455-10-21', NOW());

# member update
UPDATE member
SET NAME = '문성하수정', PASSWORD = 'qwe123qwe', nickname = 'moseoh2', birth = '1994-10-25', email = 'azqazq195@gmail.com'
WHERE id = 14;

# member delete
DELETE
FROM member
WHERE email = 'inje@gmail.com';

# member select all
SELECT
member.id,
member.name,
member.email,
member.password,
member.nickname,
member.birth,
member.reg_date,
member_tag.tag_name,
member_region.region_si,
member_region.region_gu,
member_photo.filename
FROM member
LEFT OUTER JOIN member_tag
ON member.id = member_tag.member_id
LEFT OUTER JOIN member_region
ON member.id = member_region.member_id
LEFT OUTER JOIN member_photo
ON member.id = member_photo.member_id
ORDER BY member.id asc;