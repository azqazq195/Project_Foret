package search.bean;

public class BoardALLDTO {
	private int id; 
	private int writer; 
	private int foret_id; 
	private int type; 
	private int hit; 
	private String subject; 
	private String content;
	private String reg_date;
	private String edit_date;
	private int board_like;
	private int board_comment;
	private String board_photo;
	
	private String photo;
	
	private String foret_name;
	private String foret_photo;
	
	
	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getBoard_photo() {
		return board_photo;
	}
	public void setBoard_photo(String board_photo) {
		this.board_photo = board_photo;
	}
	public String getForet_name() {
		return foret_name;
	}
	public void setForet_name(String foret_name) {
		this.foret_name = foret_name;
	}
	public String getForet_photo() {
		return foret_photo;
	}
	public void setForet_photo(String foret_photo) {
		this.foret_photo = foret_photo;
	}
	private int comment_id;
	private int comment_writer;
	private String comment_content;
	private String comment_reg_date;
	private int comment_group_no;
	
	private String writer_nickname;
	private String writer_photo;
	private String comment_writer_nickname;
	private String comment_writer_photo;
	
	public String getWriter_nickname() {
		return writer_nickname;
	}
	public void setWriter_nickname(String writer_nickname) {
		this.writer_nickname = writer_nickname;
	}
	public String getWriter_photo() {
		return writer_photo;
	}
	public void setWriter_photo(String writer_photo) {
		this.writer_photo = writer_photo;
	}
	public String getComment_writer_nickname() {
		return comment_writer_nickname;
	}
	public void setComment_writer_nickname(String comment_writer_nickname) {
		this.comment_writer_nickname = comment_writer_nickname;
	}
	public String getComment_writer_photo() {
		return comment_writer_photo;
	}
	public void setComment_writer_photo(String comment_writer_photo) {
		this.comment_writer_photo = comment_writer_photo;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getComment_writer() {
		return comment_writer;
	}
	public void setComment_writer(int comment_writer) {
		this.comment_writer = comment_writer;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_reg_date() {
		return comment_reg_date;
	}
	public void setComment_reg_date(String comment_reg_date) {
		this.comment_reg_date = comment_reg_date;
	}
	public int getComment_group_no() {
		return comment_group_no;
	}
	public void setComment_group_no(int comment_group_no) {
		this.comment_group_no = comment_group_no;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWriter() {
		return writer;
	}
	public void setWriter(int writer) {
		this.writer = writer;
	}
	public int getForet_id() {
		return foret_id;
	}
	public void setForet_id(int foret_id) {
		this.foret_id = foret_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getEdit_date() {
		return edit_date;
	}
	public void setEdit_date(String edit_date) {
		this.edit_date = edit_date;
	}
	public int getBoard_like() {
		return board_like;
	}
	public void setBoard_like(int board_like) {
		this.board_like = board_like;
	}
	public int getBoard_comment() {
		return board_comment;
	}
	public void setBoard_comment(int board_comment) {
		this.board_comment = board_comment;
	}

}
