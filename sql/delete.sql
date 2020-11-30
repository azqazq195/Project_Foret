DELETE FROM member 
WHERE  id = 2; 

DELETE FROM foret 
WHERE  id = 2; 

DELETE FROM member_photo 
WHERE  member_id = 3; 

DELETE FROM member_tag 
WHERE  id = 6; 

DELETE FROM member_region 
WHERE  id = 9; 

SELECT * 
FROM   tag 
ORDER  BY tag_id; 

DELETE FROM tag 
WHERE  tag_id = 3 
        OR tag_name = NULL; 

SELECT * 
FROM   region; 

DELETE FROM region 
--WHERE region_id = 2; 
WHERE  si = '서울시' 
       AND gu = '중구'; 

COMMIT; 