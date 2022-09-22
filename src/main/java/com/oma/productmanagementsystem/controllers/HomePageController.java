package com.oma.productmanagementsystem.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @RequestMapping(value = {"/", "/home"})
    public String index() {
        return "Home Page";
    }
}
