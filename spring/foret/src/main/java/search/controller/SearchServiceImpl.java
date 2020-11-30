package search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import member.bean.MemberDTO;
import search.dao.SearchDAO;

@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	SearchDAO searchDAO;

	@Override
	public MemberDTO emailCheck(String email) {
		return searchDAO.emailCheck(email);
	}
	
	@Override
	public MemberDTO memberSelect(String email) {
		return searchDAO.memberSelect(email);
	}


	
}
