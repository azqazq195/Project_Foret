package com.project.foret.jsp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JspController {
    @RequestMapping("/jsp")
    public String jsp() throws Exception {
        return "main";
    }

    @RequestMapping("/abcd")
    public @ResponseBody String abcd() throws Exception {
        return "abcd";
    }

    @RequestMapping("/mav")
    public ModelAndView mav() throws Exception {
        ModelAndView mav = new ModelAndView("mavSample");
        mav.addObject("key", "fruits");

        List<String> fruitList = new ArrayList<>();

        fruitList.add("apple");
        fruitList.add("orange");
        fruitList.add("banana");

        mav.addObject("value", fruitList);

        return mav;
    }
}
