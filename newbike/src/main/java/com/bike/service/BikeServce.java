package com.bike.service;

import com.bike.pojo.Bike;

import java.util.List;

public interface BikeServce {

    public void save(Bike bike);

    public void save(String bike);

    List<Bike> findAll();
}
