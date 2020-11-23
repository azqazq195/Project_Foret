package foret.bean;

public class ForetDTO {
	private int group_no;					// 포레 번호
	private String group_name;				//포레 이름
	private int group_currunt_member_count;	//현재 인원
	private String group_leader;			// 대표 회원
	private String group_profile;			// 포레 설명
	private String group_photo;				// 포레 사진
	private String group_tag;				// 포레 태그
	private String group_region;			// 지역(구 단위)
	private int group_max_member;			// 최대 인원
	private String group_date_issued;		// 생성 날짜
	
	public int getGroup_no() {
		return group_no;
	}
	public void setGroup_no(int group_no) {
		this.group_no = group_no;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public int getGroup_currunt_member_count() {
		return group_currunt_member_count;
	}
	public void setGroup_currunt_member_count(int group_currunt_member_count) {
		this.group_currunt_member_count = group_currunt_member_count;
	}
	public String getGroup_leader() {
		return group_leader;
	}
	public void setGroup_leader(String group_leader) {
		this.group_leader = group_leader;
	}
	public String getGroup_profile() {
		return group_profile;
	}
	public void setGroup_profile(String group_profile) {
		this.group_profile = group_profile;
	}
	public String getGroup_photo() {
		return group_photo;
	}
	public void setGroup_photo(String group_photo) {
		this.group_photo = group_photo;
	}
	public String getGroup_tag() {
		return group_tag;
	}
	public void setGroup_tag(String group_tag) {
		this.group_tag = group_tag;
	}
	public String getGroup_region() {
		return group_region;
	}
	public void setGroup_region(String group_region) {
		this.group_region = group_region;
	}
	public int getGroup_max_member() {
		return group_max_member;
	}
	public void setGroup_max_member(int group_max_member) {
		this.group_max_member = group_max_member;
	}
	public String getGroup_date_issued() {
		return group_date_issued;
	}
	public void setGroup_date_issued(String group_date_issued) {
		this.group_date_issued = group_date_issued;
	}
}
