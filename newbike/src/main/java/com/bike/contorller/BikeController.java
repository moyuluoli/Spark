package com.bike.contorller;


import com.bike.pojo.Bike;
import com.bike.service.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BikeController {

    @Autowired
    private BikeServce bikeServce;


    @GetMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String getById(Bike bike) {
        //调用Service保存map
        bikeServce.save(bike);
        return "success";
    }

    @PostMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String save(@RequestBody String bike) {
        //调用Service保存map
        bikeServce.save(bike);
        return "success";
    }

    @GetMapping("/bikes")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public List<Bike> findAll() {
        List<Bike> bikes = bikeServce.findAll();
        return bikes;
    }

    //先跳转到视图页面
    @GetMapping("/bike_list")
    public String toList() {
        return "bike/list";
    }


}
