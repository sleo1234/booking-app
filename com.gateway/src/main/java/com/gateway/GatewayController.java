package com.gateway;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GatewayController {

    @GetMapping("/home")
    public String homepage(){
        return "index";
    }
}
