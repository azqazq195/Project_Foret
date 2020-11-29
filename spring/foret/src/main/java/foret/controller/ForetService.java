package foret.controller;

import foret.bean.ForetDTO;
import foret.bean.ForetMemberDTO;

public interface ForetService {
	// 포레 데이터
	public int foretWrite(ForetDTO foretDTO);
	public int foretModify(ForetDTO foretDTO);
	public int foretDelete(ForetDTO foretDTO);
	
	// 포레_멤버 데이터
	public int foretMemberWrite(ForetMemberDTO foretMemberDTO);
	public int foretMemberDelete(ForetMemberDTO foretMemberDTO);
}
