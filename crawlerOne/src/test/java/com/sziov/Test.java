package com.sziov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 简单测试类
 * 包含内容：京东商品页查询
 * 不包含京东商品排除个性化查询和后半部动态加载，如有想要了解，请联系作者
 * */
public class Test {
    
    public static void main(String[] args) throws Exception {
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&psort=3&page=3";//第二页商品
        //网址分析
        /*keyword:关键词（京东搜索框输入的信息）
         * enc：编码方式（可改动:默认UTF-8）
         * psort=3 //搜索方式  默认按综合查询 不给psort值
         * page=分业（不考虑动态加载时按照基数分业，每一页30条，这里就不演示动态加载）
         * 注意：受京东商品个性化影响，准确率无法保障
         * */
        Document doc = Jsoup.connect(url).maxBodySize(0).get();
        //doc获取整个页面的所有数据
        Elements ulList = doc.select("ul[class='gl-warp clearfix']");
        Elements liList = ulList.select("li[class='gl-item']");
        //循环liList的数据
        for (Element item : liList) {
            //排除广告位置
            if (!item.select("span[class='p-promo-flag']").text().trim().equals("广告")) {
                //如果向存到数据库和文件里请自行更改
                System.out.println(item.select("div[class='p-name p-name-type-2']").select("em").text());//打印商品标题到控制台
            }
        }
    }
}