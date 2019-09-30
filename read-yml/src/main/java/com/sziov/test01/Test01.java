package com.sziov.test01;

import com.sziov.utils.YmlPropUtils;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/29 13:58
 */
public class Test01 {
    public static void main(String[] args) {
        Object property = YmlPropUtils.getInstance().getProperty("spring.datasource.type");
        System.out.println(property);
    }
}
