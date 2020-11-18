package member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freeboard.dao.FreeBoardDAO;

@Service
public class MemberServiceImpl {

	@Autowired
	FreeBoardDAO freeBoardDAO;
	
}
