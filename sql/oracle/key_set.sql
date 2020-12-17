ALTER TABLE member 
  ADD CONSTRAINT pk_member PRIMARY KEY ( id ); 

ALTER TABLE foret 
  ADD CONSTRAINT pk_foret PRIMARY KEY ( id ); 

ALTER TABLE tag 
  ADD CONSTRAINT pk_tag PRIMARY KEY ( tag_id ); 

ALTER TABLE region 
  ADD CONSTRAINT pk_region PRIMARY KEY ( region_id ); 

ALTER TABLE board 
  ADD CONSTRAINT pk_board PRIMARY KEY ( id ); 

ALTER TABLE board_comment 
  ADD CONSTRAINT pk_board_comment PRIMARY KEY ( id ); 

ALTER TABLE member_photo 
  ADD CONSTRAINT pk_member_photo PRIMARY KEY ( id ); 

ALTER TABLE foret_photo 
  ADD CONSTRAINT pk_foret_photo PRIMARY KEY ( id ); 

ALTER TABLE board_photo 
  ADD CONSTRAINT pk_board_photo PRIMARY KEY ( id ); 

ALTER TABLE member_tag 
  ADD CONSTRAINT fk_member_to_member_tag_1 FOREIGN KEY ( id ) REFERENCES member 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE member_tag 
  ADD CONSTRAINT fk_tag_to_member_tag_1 FOREIGN KEY ( tag_id ) REFERENCES tag ( 
  tag_id ) ON DELETE CASCADE; 

ALTER TABLE member_region 
  ADD CONSTRAINT fk_member_to_member_region_1 FOREIGN KEY ( id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE member_region 
  ADD CONSTRAINT fk_region_to_member_region_1 FOREIGN KEY ( region_id ) 
  REFERENCES region ( region_id ) ON DELETE CASCADE; 

ALTER TABLE foret 
  ADD CONSTRAINT fk_member_to_foret_1 FOREIGN KEY ( leader_id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_tag 
  ADD CONSTRAINT fk_foret_to_foret_tag_1 FOREIGN KEY ( id ) REFERENCES foret ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE foret_tag 
  ADD CONSTRAINT fk_tag_to_foret_tag_1 FOREIGN KEY ( tag_id ) REFERENCES tag ( 
  tag_id ) ON DELETE CASCADE; 

ALTER TABLE foret_region 
  ADD CONSTRAINT fk_foret_to_foret_region_1 FOREIGN KEY ( id ) REFERENCES foret 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_region 
  ADD CONSTRAINT fk_region_to_foret_region_1 FOREIGN KEY ( region_id ) 
  REFERENCES region ( region_id ) ON DELETE CASCADE; 

ALTER TABLE foret_member 
  ADD CONSTRAINT fk_foret_to_foret_member_1 FOREIGN KEY ( foret_id ) REFERENCES 
  foret ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_member 
  ADD CONSTRAINT fk_member_to_foret_member_1 FOREIGN KEY ( member_id ) 
  REFERENCES member ( id ) ON DELETE CASCADE; 

ALTER TABLE board 
  ADD CONSTRAINT fk_member_to_board_1 FOREIGN KEY ( writer ) REFERENCES member ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE board 
  ADD CONSTRAINT fk_foret_to_board_1 FOREIGN KEY ( foret_id ) REFERENCES foret ( 
  id ) ON DELETE CASCADE; 

ALTER TABLE board_comment 
  ADD CONSTRAINT fk_board_to_board_comment_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

ALTER TABLE board_comment 
  ADD CONSTRAINT fk_member_to_board_comment_1 FOREIGN KEY ( writer ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE like_board 
  ADD CONSTRAINT fk_member_to_like_board_1 FOREIGN KEY ( id ) REFERENCES member 
  ( id ) ON DELETE CASCADE; 

ALTER TABLE like_board 
  ADD CONSTRAINT fk_board_to_like_board_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

ALTER TABLE like_comment 
  ADD CONSTRAINT fk_member_to_like_comment_1 FOREIGN KEY ( id ) REFERENCES 
  member ( id ) ON DELETE CASCADE; 

ALTER TABLE like_comment 
  ADD CONSTRAINT fk_like_comment_1 FOREIGN KEY ( comment_id ) REFERENCES 
  board_comment ( id ) ON DELETE CASCADE; 

ALTER TABLE member_photo 
  ADD CONSTRAINT fk_member_to_member_photo_1 FOREIGN KEY ( member_id ) 
  REFERENCES member ( id ) ON DELETE CASCADE; 

ALTER TABLE foret_photo 
  ADD CONSTRAINT fk_foret_to_foret_photo_1 FOREIGN KEY ( foret_id ) REFERENCES 
  foret ( id ) ON DELETE CASCADE; 

ALTER TABLE board_photo 
  ADD CONSTRAINT fk_board_to_board_photo_1 FOREIGN KEY ( board_id ) REFERENCES 
  board ( id ) ON DELETE CASCADE; 

CREATE OR REPLACE FUNCTION get_board_photo_id RETURN NUMBER IS
BEGIN
     RETURN seq_board_photo_id.nextval;
END;
