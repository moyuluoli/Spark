package com.sziov;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/21 16:54
 */
public class Demo01 {

    public static void main(String[] args) {
        String context = "";
        //观察源代码，发现共有28个页面
        for(int i=1;i<=28;i++) {
            //获取每个页面
            context = getUrlResource("https://doutushe.com/portal/index/index/p/"+i+"","utf-8");
            //获取该页面所有组图片的url
            String pictureUrl = findPictureUrl(context);
            downallPicture(pictureUrl,"D:\\设计\\素材\\images");
        }

    }



    /**
     * 获取网页源代码
     * @author Augustu
     * @param url 网页地址
     * @param encoding 网页编码
     * @return    网页源代码
     */
    public static String getUrlResource(String url,String encoding) {
        //网页源代码，用String这个容器记录
        String htmlResource = "";
        //记录读取网页的每一行数据
        String temp = null;
        try {
            //1,找到网站地址
            URL theUrl = new URL(url);
            //2，建立起与网站的连接
            URLConnection urlConnection = theUrl.openConnection();
            //3,创建输入流，此处读取的是网页的源代码
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream(),encoding);
            //4，对输入流进行缓冲，加快读取速度
            BufferedReader reader = new BufferedReader(isr);
            //5，一行一行读取源代码，存到htmlResource中
            while((temp = reader.readLine()) != null) {
                htmlResource += temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlResource;
    }

    /**
     * 获取页面所有组图片的UrL地址
     * @author Augustu
     * @param context 每个页面的urL
     * @return 获取页面所有组图片的UrL地址
     */
    public static String findPictureUrl(String context) {
        String temp = "";//暂时存储得到的每个url
        String pictureUrl = "";//得到所有URL
        //1，Jsoup将读取的网页源代码解析为Html文档，便可以使用Jsoup的方法操作html元素了，就像javascript一样
        Document document = Jsoup.parse(context);
        //2，观察网页源代码，发现每组图片都连接到了另一个URL地址，这个a标签的class为“link-2”
        Elements groupUrl = document.getElementsByClass("link-2");
        //3,遍历每个a标签，得到href
        for(Element ele: groupUrl) {
            //此处我发现每次Url都输出两次，也没找到原因，就用此方法先解决他
            if(ele.attr("href") == temp) {
                continue;
            }
            temp = ele.attr("href");
            //4，将所有URL存入String中，并使用空格分开，便于后面分割
            //本来我使用“|”分隔开来，分割的结果竟然是每个字符都分开了
            pictureUrl += "https://doutushe.com"+ele.attr("href")+" ";
        }
        return pictureUrl;
    }

    /**
     * 下载单张图片
     * @param picturl 图片地址
     * @param filePath    下载路径
     * @param fileName    下载名
     */
    public static void downPicture(String picturl,String filePath,String fileName) {
        FileOutputStream fos = null;//输出文件流
        BufferedOutputStream bos = null;//缓冲输出
        File file = null;//创建文件对象
        File dir = new File(filePath);//创建文件保存目录
        Connection.Response response;
        try {
            //1，Jsoup连接地址，得到响应流，ignoreContentType表示忽略网页类型，如果不加会报错（默认只支持文本），因为我们页面是图片
            response = Jsoup.connect(picturl).ignoreContentType(true).execute();
            //2,将页面内容按字节输出
            byte[] img = response.bodyAsBytes();
            //3，写入本地文件中
            //判断文件目录是否存在,
            if(!dir.exists() ){
                dir.mkdir();//创建文件夹
            }
            file = new File(filePath+"\\"+fileName);//创建文件
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(img);//写入本地
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //4,释放资源
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 下载所有图片
     * @author Augustu
     * @param pictureUrl 每组图片url
     */
    public static void downallPicture(String pictureUrl,String downLoadPath) {
        String picturl = "";
        String pictureName ="";//
        String[] pictureUrlArry = pictureUrl.split(" ");//图片组的url
        for(int i=0;i<pictureUrlArry.length;i++) {
            //遍历得到每组图片的url
            String pictureHtml = getUrlResource(pictureUrlArry[i],"utf-8");
            Document document = Jsoup.parse(pictureHtml);
            //得到该组图片的分类名称
            String dir =  document.getElementsByTag("blockquote").first().child(0).text();
            //该标签包含所有图片url
            Elements elements = document.getElementsByClass("lazy");
            for(Element ele: elements) {
                //得到每张图片url
                picturl = ele.attr("data-original");
                //观察源代码，发现获取的图片地址多了/themes/doutushe/Public/assets/images/doutushe-erweima.jpg，将其删除
                if(picturl.equals("/themes/doutushe/Public/assets/images/doutushe-erweima.jpg")) {
                    continue;
                }
                //得到每张图片的名字，别忘了加后缀
                pictureName = ele.attr("title")+".gif";
                //下载该图片
                downPicture(picturl,downLoadPath+"\\"+dir,pictureName);
            }
        }
    }


}
