package com.sziov.cw.test;

import com.sziov.cw.constants.ConstantsSQL;
import com.sziov.cw.utils.QueryDB;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.dmg.pmml.Constant;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/12 16:50
 */
public class MotorDF {
    public static void main(String[] args) {

        Dataset<Row> ds1 = QueryDB.query(ConstantsSQL.aSQL);
        Dataset<Row> ds2 = QueryDB.query(ConstantsSQL.bSQL);



        //求ds1和ds2的差集
//        ds1.except(ds2).show();

        //求交集
//        ds1.intersect(ds2).show();

    }
}
