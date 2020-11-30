package member.bean;

public class MemberLikeDTO {
	private int id;
	private int reference_id;
	private int board_id;
	private int comment_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReference_id() {
		return reference_id;
	}
	public void setReference_id(int board_id) {
		this.reference_id = board_id;
	}
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
}
