package com.sziov.hive;
 
/**
 * @ClassName: xxxx
 * @Description: TODO JDBC Impala Demo
 * @author:
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class Demo{
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Demo ar = new Demo();
		long currentTimeMillis = System.currentTimeMillis();
		ar.count();
		System.out.println("耗时:"+(System.currentTimeMillis() - currentTimeMillis));
	}
	
    public  Connection getConnection() throws ClassNotFoundException, SQLException{
        String driver = "org.apache.hive.jdbc.HiveDriver";
        String JDBCUrl = "jdbc:hive2://d1.cdh.com:21050/;auth=noSasl";
        String username = "";
        String password = "";
        Connection conn = null;
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(JDBCUrl,username,password);
        return conn;
    }
    
    public void count() throws ClassNotFoundException, SQLException{
        Connection conn = getConnection();
        String sql = "select count(1) from ods_xxx.xxx_other_article_hive where created_date >= 20180101 and created_date <=20181231 and text regexp '(厦门大学|厦大).{0,6}(女研究生|博士生|学生|女学生|吉吉良)' and text regexp '精日|辱华|两面人';";
        System.out.println("查询语句："+sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
       int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()){
            for(int i=1;i<=columnCount;i++){
                System.out.print(rs.getString(i)+"\t");
            }
            System.out.println("");
        }
    }
}