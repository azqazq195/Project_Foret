package member.bean;

public class Member {
	private int member_id;				// 회원 번호
	private String member_name;			// 회원 이름
	private String member_email;		// 회원 메일
	private String member_nickname;		// 회원 닉네임
	private String member_birth;		// 회원 생일
	private String member_join_date;	// 회원 가입 날짜
	private String member_chain;		// 회원 연동여부
	private String member_address;		// 회원 주소
	private String member_photo;		// 회원 프로필 사진
	
	public Member() {
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_email() {
		return member_email;
	}

	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}

	public String getMember_nickname() {
		return member_nickname;
	}

	public void setMember_nickname(String member_nickname) {
		this.member_nickname = member_nickname;
	}

	public String getMember_birth() {
		return member_birth;
	}

	public void setMember_birth(String member_birth) {
		this.member_birth = member_birth;
	}

	public String getMember_join_date() {
		return member_join_date;
	}

	public void setMember_join_date(String member_join_date) {
		this.member_join_date = member_join_date;
	}

	public String getMember_chain() {
		return member_chain;
	}

	public void setMember_chain(String member_chain) {
		this.member_chain = member_chain;
	}

	public String getMember_address() {
		return member_address;
	}

	public void setMember_address(String member_address) {
		this.member_address = member_address;
	}

	public String getMember_photo() {
		return member_photo;
	}

	public void setMember_photo(String member_photo) {
		this.member_photo = member_photo;
	}
	
}