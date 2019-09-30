package com.bike.service;

import com.bike.mapper.BikeMapper;
import com.bike.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BikeServiceImpl implements BikeServce {

    //注入操作mongo数据库的模板
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BikeMapper bikeMapper;

    @Override
    public void save(Bike bike) {
        bikeMapper.save(bike);
        //int i = 100 / 0;
        bikeMapper.save(bike);
    }

    @Override
    public void save(String bike) {
        mongoTemplate.save(bike, "bike");
    }

    @Override
    public List<Bike> findAll() {
        return mongoTemplate.findAll(Bike.class,"bike");
    }
}
