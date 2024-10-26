package com.example.hilla;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ContentController {

  @GetMapping
  public String index(Model model) {
    model.addAttribute("title", "Welcome to Hilla main page");
    return "index";
  }
}
