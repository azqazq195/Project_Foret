package foret.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import foret.bean.ForetDTO;
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
		// TODO Auto-generated method stub
		return foretDAO.foretModify(foretDTO);
	}

	@Override
	public int foretDelete(ForetDTO foretDTO) {
		// TODO Auto-generated method stub
		return foretDAO.foretDelete(foretDTO);
	}

}
