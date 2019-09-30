package file;

import org.junit.Test;

public class fuzhiwenjianjiaTest01 {


    @Test
    public void test(){
        //源目录
        String srcPath = "D:\\设计\\素材\\sucai";
        //目标文件
        String destPath = "D:\\dataTmp\\gz";
        FileUtil.copyDir(srcPath,destPath);
    }

    public static void main(String args[]) {
        //源目录
        String srcPath = "d:/a/b";
        //目标文件
        String destPath = "d:/a/d";
        FileUtil.copyDir(srcPath,destPath);
    }
 
}