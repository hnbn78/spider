package com.a3.lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@RequestMapping(value = "index")
	public String index() {
		return "index.html";
	}

	@RequestMapping(value = "test")
	public String test() {
		return "test.html";
	}

}