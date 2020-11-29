commit;
select * from member;
select * from member_photo;
UPDATE member_photo 
		SET    dir = 'dd', 
		       filename = 'dd', 
		       originname = 'dd', 
		       filesize = 2, 
		       filetype = 'dd' 
		WHERE  member_id = 3; 
        
delete from member_photo where member_id = 3;

UPDATE member
SET    name = '¼öÁ¤',
       email = 'fixed',
       password = 'fixed',
       nickname = 'fixed',
       birth = 'fixed'
WHERE  id = 2;
       

UPDATE member_photo 
SET    dir = 'fixed', 
       filename = 'fixed', 
       originname = 'fixed', 
       filesize = 123, 
       filetype = 'fixed' 
WHERE  member_id = 7; 

