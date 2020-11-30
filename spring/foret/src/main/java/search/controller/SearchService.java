package search.controller;

import member.bean.MemberDTO;

public interface SearchService {
	
	public MemberDTO emailCheck(String email);
	public MemberDTO memberSelect(String email);
	
}
