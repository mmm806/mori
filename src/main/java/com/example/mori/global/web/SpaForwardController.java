package com.example.mori.global.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 프론트 라우트는 index.html로 포워드 */
@Controller
public class SpaForwardController {

	// 필요한 경로만 우선 포워드
	@GetMapping({"/login", "/signup"})
	public String forwardAuthPages() { return "forward:/index.html"; }

	// 추후 더 필요하면 여기에 "/diary/**" 등 추가
}
