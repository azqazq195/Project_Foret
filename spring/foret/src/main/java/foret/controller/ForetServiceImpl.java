package foret.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import foret.bean.ForetDTO;
import foret.bean.ForetMemberDTO;
import foret.dao.ForetDAO;

@Service
public class ForetServiceImpl implements ForetService{
	@Autowired
	ForetDAO foretDAO;
	
	@Override
	public int foretWrite(ForetDTO foretDTO) {
		return foretDAO.foretWrite(foretDTO);
	}

	@Override
	public int foretModify(ForetDTO foretDTO) {
		return foretDAO.foretModify(foretDTO);
	}

	@Override
	public int foretDelete(ForetDTO foretDTO) {
		return foretDAO.foretDelete(foretDTO);
	}

	@Override
	public int foretMemberWrite(ForetMemberDTO foretMemberDTO) {
		return foretDAO.foretMemberWrite(foretMemberDTO);
	}

	@Override
	public int foretMemberDelete(ForetMemberDTO foretMemberDTO) {
		return foretDAO.foretMemberDelete(foretMemberDTO);
	}

}
