# member select all
SELECT *
FROM V_member_all WHERE id = 1;

SELECT *
FROM member WHERE id = 1;















SELECT *
FROM V_foret_all;






SELECT *
FROM V_foret_rank;
SELECT *
FROM V_tag_rank;

SELECT * FROM
foret_photo;
SELECT *
FROM board;
SELECT *
FROM member_foret;
SELECT *
FROM foret;
SELECT *
FROM tag;
SELECT *
FROM foret;

SELECT COUNT(*).cnt
FROM boards
GROUP BY board.foret_id;

SELECT *
FROM V_foret_all
LEFT JOIN 
(SELECT board.foret_id, COUNT(*) AS cnt
FROM board
WHERE foret_id IS NOT NULL
GROUP BY foret_id
ORDER BY cnt DESC) temp
ON V_foret_all.id = temp.foret_id;

SELECT board.foret_id, COUNT(*) AS cnt
FROM board
WHERE foret_id IS NOT NULL
GROUP BY foret_id
ORDER BY cnt DESC;

