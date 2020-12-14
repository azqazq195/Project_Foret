SELECT * FROM member;

# email check
SELECT * FROM member WHERE email = 'azqazq195@gmail.com';
# login
SELECT * FROM member WHERE email = 'azqazq195@gmail.com' AND password = 'qwe123';

# member insert
INSERT INTO member (NAME, email, PASSWORD, nickname, birth, reg_date)
VALUES('홍길동', 'hong@gmail.com', 'qwe123', 'hong', '1455-10-21', NOW());

