package member.bean;

public class MemberDTO {
	private int id; 
	private String name; 
	private String email; 
	private String password;
	private String nickname; 
	private String birth; 
	private String reg_date;
	private String photo;
	private String deviceToken;
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	private String[] tag;
	private String[] region_si;
	private String[] region_gu;
	private String[] like_board;
	private String[] like_comment;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
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
	public String[] getLike_board() {
		return like_board;
	}
	public void setLike_board(String[] like_board) {
		this.like_board = like_board;
	}
	public String[] getLike_comment() {
		return like_comment;
	}
	public void setLike_comment(String[] like_comment) {
		this.like_comment = like_comment;
	}
}
