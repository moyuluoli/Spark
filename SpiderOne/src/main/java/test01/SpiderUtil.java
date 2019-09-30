package test01;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

public class SpiderUtil {
	public static String getHtml() throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://www.mzitu.com/86712");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(8000);
		connection.setReadTimeout(8000);
		InputStream in = connection.getInputStream();
		/* 下面对获取到的输入流进行读取 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		System.out.println(response.toString());
		return response.toString();
	}

	public static void getHtmlByJsoup() throws IOException {
		int i =1;
		String url = "http://www.mzitu.com/86712";
		Document doc = Jsoup.connect(url+"/"+i).get();
		Elements imageDiv = doc.getElementsByClass("meta-images");
	//	System.out.println(doc);
		Elements allsrc = doc.getElementsByTag("img");
		String src = allsrc.attr("src");
		if(!src.isEmpty()){
			i++;
		}
	}
	
	public static String getSrc(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		Elements imageDiv = doc.getElementsByClass("meta-images");
		Elements allsrc = doc.getElementsByTag("img");
		String src = allsrc.attr("src");
		System.out.println(src);
		return src;
	}
	
	public static void getAllSrc() throws IOException{
		int i = 1;
		String url = "http://www.mzitu.com/86712/1";
		int lastnumber = url.indexOf("m")+16;
		int beginnumber = url.indexOf("h");
		System.out.println(beginnumber);
		System.out.println(lastnumber);
		System.out.println(url.substring(beginnumber, lastnumber)+i);
		String realurl = url.substring(beginnumber, lastnumber)+i;
		while(!getSrc(realurl).isEmpty()){
			i++;
			System.out.println(url);
			System.out.println(getSrc(url));
		}
		
	}

	public static final String WORKSPACE = "D:\\设计\\素材\\spider02";
	public static void saveImage(ArrayList<String> urls) throws Exception{
		//        判断文件夹是否存在，不存在则创建
		File directory = new File(WORKSPACE);
		if (!directory.exists() && !directory.isDirectory()) {
			directory.mkdir();
		}
		System.out.println("开始下载!");
		File file = new File(WORKSPACE);
		URLConnection imageconnection = null ;
		InputStream imageInputStream=null;
		for(String url:urls){
			URL oneurl = new URL(url);
			try{
			imageconnection  = oneurl.openConnection();
			 imageInputStream = imageconnection.getInputStream();
			}catch(Exception e){
				System.out.println("下载失败");
				continue;
			}
			if (!file.exists()) {
				file.mkdir();
			}
			OutputStream imageoutputStream = new FileOutputStream(new File(WORKSPACE
					+ new Date().getTime() + ".jpg"));
			
		byte[] b = new byte[2048];
		int len = 0;
		while ((len = imageInputStream.read(b)) != -1) {
			imageoutputStream.write(b, 0, len);
		}
		}
		System.out.println("下载完成");
	}
}
//int lastnumber = src.indexOf("m")+16;
//int beginnumber = src.indexOf("h");
//String realurl = src.substring(beginIndex)
