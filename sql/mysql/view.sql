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