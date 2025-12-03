package com.TechMarket.product_service.controller;

import com.TechMarket.product_service.constants.ApiPaths;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.ADMIN_URL)
public class AdminController {
    @GetMapping
    public String admin(){
        return "hello admin";
    }
}
