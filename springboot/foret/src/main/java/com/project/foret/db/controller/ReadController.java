package com.project.foret.db.controller;

import com.project.foret.db.service.ReadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReadController {
    @Autowired
    ReadService readService;
}
