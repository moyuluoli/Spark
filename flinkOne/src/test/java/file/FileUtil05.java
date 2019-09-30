package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @描述:
 * @公司:
 * @作者: 王聪
 * @版本: 1.0.0
 * @日期: 2019/9/20 10:39
 */
public class FileUtil05 {
    public void moveFiles(String oldPath, String newPath) throws IOException {
        String[] filePaths = new File(oldPath).list();
//        List<String> filePaths = new File(oldPath).list();


        if (filePaths!=null && filePaths.length > 0){
            if (!new File(newPath).exists()){
                new File(newPath).mkdirs();
            }

            for (int i=0; i<filePaths.length; i++){
                if (new File(oldPath + File.separator + filePaths[i]).isDirectory()){
                    moveFiles(oldPath + File.separator + filePaths[i], newPath + File.separator + filePaths[i]);
                }else if (new File(oldPath + File.separator + filePaths[i]).isFile()){
                    //复制文件到另一个目录
                    copyFile(oldPath + File.separator + filePaths[i], newPath + File.separator + filePaths[i]);
                    //移动文件至另一个目录
                    new File(oldPath + File.separator + filePaths[i]).renameTo(new File(newPath + File.separator + filePaths[i]));
                }
            }
        }
    }

    public void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        byte[] buffer=new byte[2097152];

        while((in.read(buffer)) != -1){
            out.write(buffer);
        }
    }
}
