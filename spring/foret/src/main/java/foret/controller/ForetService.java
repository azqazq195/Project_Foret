package foret.controller;

import foret.bean.ForetDTO;

public interface ForetService {
	// 포레 데이터
	public int foretWrite(ForetDTO foretDTO);
	public int foretModify(ForetDTO foretDTO);
	public int foretDelete(ForetDTO foretDTO);
}
