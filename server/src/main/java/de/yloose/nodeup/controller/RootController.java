package de.yloose.nodeup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
	
	@RequestMapping(value={"/", "/nodes", "/nodes/**"})
	public String index() {
		return "index";
	}
}
