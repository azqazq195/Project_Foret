package member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import freeboard.controller.FreeBoardService;

@Controller
public class MemberController {

	@Autowired
	FreeBoardService service;
	
}
