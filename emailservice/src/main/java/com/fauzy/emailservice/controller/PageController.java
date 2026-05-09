package com.fauzy.emailservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fauzy.emailservice.dto.ContactRequestDto;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(Model model){
        
        model.addAttribute("contactForm", new ContactRequestDto(
            "",
            "",
            ""
        ));

        return "index";
    }
}
