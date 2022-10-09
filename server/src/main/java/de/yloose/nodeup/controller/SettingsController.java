package de.yloose.nodeup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.yloose.nodeup.service.SettingsService;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

	@Autowired
	SettingsService settingsService;
	
	@GetMapping("/update")
	public String update() {
		settingsService.update();
		
		return "Update started.";
	}
}
