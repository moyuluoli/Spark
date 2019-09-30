package com.cw.c01.dao;

import com.cw.c01.domain.GoodsInfo;

import java.util.List;

/**
 * 商品Dao层
 * @author ZGJ
 * @date 2017年7月15日
 */
public interface GoodsInfoDao {
    /**
     * 插入商品信息
     * @param infos
     */
    void saveBatch(List<GoodsInfo> infos);
}