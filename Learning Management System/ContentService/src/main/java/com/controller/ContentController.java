package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

	@GetMapping("/content")
	public String content() {
		return "Hi this is content controller";
	}
}
