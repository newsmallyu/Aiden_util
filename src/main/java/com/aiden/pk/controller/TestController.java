package com.aiden.pk.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("json/grafana")
public class TestController {

    @GetMapping("/get")
    public Map getData(){
        Map map = new HashMap();
        map.put("status", "success");
        map.put("message", "Success");
        return map;
    }
}
