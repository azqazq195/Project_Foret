package foret.bean;

public class ForetDTO {
	private int id;
	private int leader_id;
	private String name;
	private String introduce;
	private int max_member;
	private String reg_date;
	private String photo;
	private String[] tag;
	private String[] region_si;
	private String[] region_gu;
	private String[] member;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLeader_id() {
		return leader_id;
	}
	public void setLeader_id(int leader_id) {
		this.leader_id = leader_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getMax_member() {
		return max_member;
	}
	public void setMax_member(int max_member) {
		this.max_member = max_member;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String[] getTag() {
		return tag;
	}
	public void setTag(String[] tag) {
		this.tag = tag;
	}
	public String[] getRegion_si() {
		return region_si;
	}
	public void setRegion_si(String[] region_si) {
		this.region_si = region_si;
	}
	public String[] getRegion_gu() {
		return region_gu;
	}
	public void setRegion_gu(String[] region_gu) {
		this.region_gu = region_gu;
	}
	public String[] getMember() {
		return member;
	}
	public void setMember(String[] member) {
		this.member = member;
	}
}
