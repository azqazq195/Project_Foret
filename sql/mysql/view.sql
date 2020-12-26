# member select all
CREATE OR REPLACE VIEW V_member_all AS
SELECT
member.id,
member.name,
member.email,
member.password,
member.nickname,
member.birth,
member.reg_date,
member.device_token,
member_tag.tag_name,
member_region.region_si,
member_region.region_gu,
member_photo.filename,
member_foret.foret_id
FROM member
LEFT OUTER
JOIN member_tag ON member.id = member_tag.member_id
LEFT OUTER
JOIN member_region ON member.id = member_region.member_id
LEFT OUTER
JOIN member_photo ON member.id = member_photo.member_id
LEFT OUTER
JOIN member_foret ON member.id = member_foret.member_id
ORDER BY member.id ASC;

# foret select all
CREATE OR REPLACE VIEW V_foret_all AS
SELECT 
foret.id,
foret.leader_id,
foret.name,
foret.introduce,
foret.max_member,
foret.reg_date,
foret_tag.tag_name,
foret_region.region_si,
foret_region.region_gu,
foret_photo.filename,
member_foret.member_id
FROM foret
LEFT OUTER
JOIN foret_tag ON foret.id = foret_tag.foret_id
LEFT OUTER
JOIN foret_region ON foret.id = foret_region.foret_id
LEFT OUTER
JOIN foret_photo ON foret.id = foret_photo.foret_id
LEFT OUTER
JOIN member_foret ON foret.id = member_foret.foret_id
ORDER BY foret.id ASC;

# tag_rank
CREATE OR REPLACE VIEW V_tag_rank AS
SELECT COUNT(*) AS cnt, tag_name.tag_name
FROM 
(SELECT tag_name
FROM member_tag UNION ALL
SELECT tag_name
FROM foret_tag) tag_name
GROUP BY tag_name
ORDER BY cnt DESC;

# foret_rank
CREATE OR REPLACE VIEW V_foret_rank AS
SELECT 
V_foret_all.id,
V_foret_all.leader_id,
V_foret_all.name,
V_foret_all.introduce,
V_foret_all.max_member,
V_foret_all.reg_date,
V_foret_all.tag_name,
V_foret_all.region_si,
V_foret_all.region_gu,
V_foret_all.filename,
V_foret_all.member_id,
temp.cnt
FROM V_foret_all
LEFT JOIN 
(SELECT board.foret_id, COUNT(*) AS cnt
FROM board
WHERE foret_id IS NOT NULL
GROUP BY foret_id
ORDER BY cnt DESC) temp
ON V_foret_all.id = temp.foret_id;
Foretmember