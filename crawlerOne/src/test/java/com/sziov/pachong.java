package com.sziov;

import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pachong {

    @org.junit.Test
    public void xiushibaike() throws Exception{
        Set<String> set=new HashSet();
        String regex = "[a-zA-z]+://[^\\s]*";
        Pattern p = Pattern.compile(regex);
        for (int i = 501;i<=1016;i++){
            URL url = new URL("http://m.qiubaichengren.net/"+i+".html");
            URLConnection urlConnection = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String buf = null;
            while ((buf = br.readLine()) != null) {
                Matcher m = p.matcher(buf);
                while (m.find()) {
                    set.add(m.group());
                }
            }
            br.close();
        }
        System.out.println(set.size());
        for (String l :set){
            downloadPicture(l.substring(0,l.length()-1));
        }
    }

    @Test
    public void youguo()throws Exception{
        String regex = "[a-zA-z]+://www.ugirls.com/Models/[^\\s]*.html";
        int zs = 0;
        for (int i=2;i<100;i++){
            String url = "https://www.ugirls.com/Content/Page-"+ i +".html";
            Set<String> set = getUrl(url, regex);
            System.out.println("本网页下:"+url+"有"+set.size()+"个网页");
            String regex2 = "[a-zA-z]+://img.ugirls.tv/uploads/magazine/content[^\\s]*.jpg";
            int j = 1;
            for (String l :set){
                Set<String> set2 = getUrl(l, regex2);
                System.out.println("第"+j+"个网页下:"+l+"有"+set2.size()+"张图片");
                zs+=set2.size();
                System.out.println("共"+zs+"张图片");
                for (String b : set2){
                    System.out.println("图片:"+b);
                    downloadPicture(b);
                }
                j++;
            }
        }
    }
 
    /**
     * 获取匹配的url存入set集合
     * @param websiteUrl
     * @param regex
     * @return
     * @throws Exception
     */
    private static Set<String> getUrl(String websiteUrl,String regex) throws Exception{
        Set<String> set=new HashSet();
        Pattern p = Pattern.compile(regex);
        URL url = new URL(websiteUrl);
        URLConnection urlConnection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String buf = null;
        while ((buf = br.readLine()) != null) {
            Matcher m = p.matcher(buf);
            while (m.find()) {
                set.add(m.group());
            }
        }
        br.close();
        return set;
    }
 
    /**
     * 根据url下载图片
     * @param urlList
     * @throws Exception
     */
    private static void downloadPicture(String urlList) throws Exception{
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
 
            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\设计\\素材\\ceshi\\"+System.currentTimeMillis()+".jpg"));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
 
            byte[] buffer = new byte[1024];
            int length;
 
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("成功:"+urlList);
        }
    }
}