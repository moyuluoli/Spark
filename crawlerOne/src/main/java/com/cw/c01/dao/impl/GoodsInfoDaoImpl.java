package com.cw.c01.dao.impl;

import com.cw.c01.dao.GoodsInfoDao;
import com.cw.c01.domain.GoodsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
@Service
public class GoodsInfoDaoImpl implements GoodsInfoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveBatch(List<GoodsInfo> infos) {
        String sql = "REPLACE INTO goods_info(" + "goods_id," + "goods_name," + "goods_price," + "img_url) "
                + "VALUES(?,?,?,?)";
        for(GoodsInfo info : infos) {
            jdbcTemplate.update(sql, info.getGoodsId(), info.getGoodsName(), info.getGoodsPrice(), info.getImgUrl());
        }
    }
}