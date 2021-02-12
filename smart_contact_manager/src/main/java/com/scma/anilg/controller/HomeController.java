package com.scma.anilg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value={"/", "/home"})
	public String home(Model model) {
		model.addAttribute("title", " Home : smart contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About : Smart contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register : Smart contact manager");
		return "signup";
	}
	
}
