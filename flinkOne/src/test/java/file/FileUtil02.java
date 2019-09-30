package file;

import java.io.*;

/**
 * 线程的方式实现文件的复制。
 文件的复制需要四个参数：1，路径或文件）,2路径或文件，3,是否覆盖，4，是否追加，
多文件复制还需要加时间参数（毫秒）.
 * 以及File类实例的简单创建，
 * 
 * @version 2
 * @author cw
 */
public class FileUtil02 extends Thread {

    static class FileUtilHelp {//辅助类

        private static FileUtil02[] farr = new FileUtil02[5];// 线程数量(java默认线程最多10)

        // 创建文件并复制
        private static void createFileAndCopy(File f1, File f2, boolean override, boolean append) {
            if (!f2.exists()) {
                createFile(f2, override);
            } else {
                if (!override)
                    return;
                else {
                    append = false;// 覆盖必然不会追加
                }
            }
            FileUtilHelp.createThread(f1, f2, append);
        }

        private static void createCopy(InputStream is, OutputStream os) {
            int i = 0;
            for (FileUtil02 f : farr) {
                if (f == null || !f.isAlive()) {
                    farr[i] = new FileUtil02();
                    farr[i].setInputStreamAndOutputStream(is, os);
                    farr[i].start();
                }
                i++;
            }
        }

        // 创建路径
        private static void createMkdirs(String path) {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        }

        // 开始线程
        private static void createThread(File f1, File f2, boolean append) {
            int i = 0;
            for (FileUtil02 f : farr) {
                if (f == null || !f.isAlive()) {
                    farr[i] = new FileUtil02();
                    farr[i].copyThread(f1, f2, append);
                    farr[i].start();
                }
                i++;
            }
        }

    }

    /**
     * 复制文件1到文件2(可以创建路径)
     * 
     * @param f1
     *            文件对象
     * @param f2
     *            文件对象
     * @param override
     *            是否覆盖
     * @param append
     *            是否追加文件内容
     */
    public static void copyFile(File f1, File f2, boolean override, boolean append) {
        if (exists(f1)) {
            FileUtilHelp.createMkdirs(parent(f2));
            FileUtilHelp.createFileAndCopy(f1, f2, override, append);
        }
    }

    /**
     * 复制文件1到文件2(可以创建路径)
     * 
     * @param fileName
     *            文件1
     * @param fileName2
     *            文件2
     * @param override
     *            是否覆盖
     */
    public static void copyFile(String fileName, String fileName2, boolean override, boolean append) {
        copyFile(newFile(fileName), newFile(fileName), override, append);
    }

    /**
     * 复制文件到指定路径(可创建路径)
     * 
     * @param f1
     *            文件
     * @param f2
     *            文件夹
     * @param override
     *            是否覆盖
     * @param append
     *            是否追加
     * @return
     */
    public static void copyFileToPath(File f1, File f2, boolean override, boolean append) {
        copyFile(f1, newFile(f2.getAbsolutePath(), f1.getName()), override, append);
    }

    /**
     * 根据流来复制。
     * 
     * @param is
     *            输入流
     * @param os
     *            输出流
     */
    public static void copyFile(InputStream is, OutputStream os) {
        FileUtilHelp.createCopy(is, os);
    }

    /**
     * 复制文件到指定路径(可创建路径)
     * 
     * @param fileName
     *            文件名
     * @param path
     *            路径
     * @param override
     *            覆盖
     * @param append
     *            是否追加
     */
    public static void copyFileToPath(String fileName, String path, boolean override, boolean append) {
        copyFileToPath(newFile(fileName), newFile(path), override, append);
    }

    /**
     * 复制指定路径下所有文件到指定路径(建议大文件不要太多)
     * 
     * @param f1
     *            文件对象
     * @param f2
     *            文件对象
     * @param override
     *            是否覆盖
     * @param append
     *            是否追加
     * @param time
     *            复制每个文件的间隔
     */
    public static void copyPathToPath(File f1, File f2, boolean override, boolean append, long time) {
        if (exists(f1) && isDirectory(f1)) {
            File[] farr = f1.listFiles();
            for (File f : farr) {
                try {
                    Thread.sleep(time);// 一次性复制超过5个，线程支持不住，慢点也不影响性能。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                copyFileToPath(f, f2, override, append);
            }
        }
    }

    /**
     * 复制指定路径下所有文件到指定路径(大文件需要更多时间。)
     * 
     * @param path1
     *            路径
     * @param path2
     *            路径
     * @param override
     *            是否覆盖
     * @param append
     *            是否追加
     * @param time
     *            复制每个文件的间隔
     */
    public static void copyPathToPath(String path1, String path2, boolean override, boolean append, long time) {
        copyPathToPath(newFile(path1), newFile(parent(newFile(path1)), path2), override, append, time);
    }

    /**
     * 创建目录
     * 
     * @param f
     */
    public static void createDire(File f) {
        FileUtilHelp.createMkdirs(f.getAbsolutePath());
    }

    /**
     * 根据路径创建目录
     * 
     * @param path
     */
    public static void createDire(String path) {
        createDire(newFile(path));
    }

    /**
     * 不覆盖的创建文件
     * 
     * @param f
     */
    public static void createFile(File f) {
        createFile(f, false);
    }

    /**
     * 创建文件
     * 
     * @param f
     *            文件对象
     * @param override
     *            是否覆盖
     */
    public static void createFile(File f, boolean override) {
        FileUtilHelp.createMkdirs(parent(f));
        if (override) {
            f.delete();
        }
        if (!exists(f)) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 不覆盖的创建文件
     * 
     * @param
     */
    public static void createFile(String fileName) {
        createFile(fileName, false);
    }

    /**
     * 创建文件
     * 
     * @param fileName
     *            创建文件
     * @param override
     *            是否覆盖
     */
    public static void createFile(String fileName, boolean override) {
        createFile(newFile(fileName), override);
    }

    /**
     * 检测文件对象是否存在
     * 
     * @param f
     */
    public static boolean exists(File f) {
        return f.exists() ? true : false;
    }

    /**
     * 检测路径是否存在
     * 
     * @param
     */
    public static boolean exists(String fileName) {
        return exists(new File(fileName));
    }

    /**
     * 检测文件对象是否目录
     * 
     * @param f
     */
    public static boolean isDirectory(File f) {
        return f.isDirectory();
    }

    /**
     * 检测路径是否目录
     * 
     * @param
     */
    public static boolean isDirectory(String fileName) {
        return isDirectory(newFile(fileName));
    }

    /**
     * 检测文件对象是否文件
     * 
     * @param f
     */
    public static boolean isFile(File f) {
        return f.isFile();
    }

    /**
     * 获取文件的后缀名
     * 
     * @param fileName
     * @return
     */
    public static String suffixName(String fileName) {
        return suffixName(newFile(fileName));
    }

    /**
     * 获取不带后缀名的文件名字符串
     * 
     * @param fileName
     * @return
     */
    public static String getNoSuffixName(String fileName) {
        return getNoSuffixName(newFile(fileName));
    }

    /**
     * 获取不带后缀名的文件名字符串
     * 
     * @param f
     * @return
     */
    public static String getNoSuffixName(File f) {
        return f.getName().substring(0, f.getName().lastIndexOf("."));
    }

    /**
     * 重命名
     * 
     * @param fileName1
     *            路径
     * @param fileName2
     *            路径
     */
    public static void rename(String fileName1, String fileName2) {
        rename(newFile(fileName1), newFile(fileName2));
    }

    /**
     * 重命名
     * 
     * @param f1
     * @param f2
     */
    public static void rename(File f1, File f2) {
        f1.renameTo(f2);
    }

    /**
     * 修改指定路径的文件后缀名。
     * 
     * @param fileName
     *            路径
     * @param suffix
     *            后缀名
     */
    public static void replaceSuffixName(String fileName, String suffix) {
        replaceSuffixName(newFile(fileName), suffix);
    }

    /**
     * 修改文件后缀名
     * 
     * @param file
     *            对象，
     * @param suffix
     *            后缀
     */
    public static void replaceSuffixName(File file, String suffix) {
        StringBuilder name = new StringBuilder();
        name.append(getNoSuffixName(file));
        name.append(".");
        name.append(suffix);
        rename(file, newFile(parent(file), name.toString()));
    }

    /**
     * 获取文件的后缀名
     * 
     * @param f
     * @return
     */
    public static String suffixName(File f) {
        String[] sarr = f.getName().split("\\.");
        return sarr.length > 1 ? sarr[sarr.length - 1] : null;
    }

    /**
     * 检测路径是否文件
     * 
     * @param
     */
    public static boolean isFile(String fileName) {
        return isFile(newFile(fileName));
    }

    /**
     * 根据路径创建文件对象，
     * 
     * @param fileName
     * @return
     */
    public static File newFile(String fileName) {
        return new File(fileName);
    }

    /**
     * 创建路径和文件名创建文件对象
     * 
     * @param path
     *            路径
     * @param fileName
     *            文件名字
     * @return
     */
    public static File newFile(String path, String fileName) {
        return new File(path, fileName);
    }

    /**
     * 返回对象的父目录
     * 
     * @param f
     * @return
     */
    public static String parent(File f) {
        String str = f.getName();
        String abso = f.getAbsolutePath();
        return abso.substring(0, abso.length() - str.length());
    }

    /**
     * 根据文件和样式获取文件大小的字符串
     * 
     * @param file
     *            根据文件路径
     * @param size
     *            文件大小样式(KB或MB或GB)
     * @return
     */
    public static String size(File file, String size) {
        long len = file.length();
        double d = 0;
        StringBuilder sb = new StringBuilder();
        switch (size) {
        case "k":
        case "K":
        case "kb":
        case "Kb":
        case "KB":
        case "kB":
            d = 1.0 * len / 1024;
            sb.append(String.format("%.3f", d));
            sb.append("MB");
            break;
        case "m":
        case "M":
        case "mb":
        case "MB":
        case "mB":
        case "Mb":
            d = 1.0 * len / 1024 / 1024;
            sb.append(String.format("%.3f", d));
            sb.append("MB");
            break;
        case "g":
        case "G":
        case "gb":
        case "GB":
        case "Gb":
        case "gB":
            d = 1.0 * len / 1024 / 1024 / 1024;
            sb.append(String.format("%.3f", d));
            sb.append("GB");
            break;
        default:
            sb.append(len);
            sb.append("B");
        }
        return sb.toString();
    }

    /**
     * 根据路径和样式获取文件大小的字符串
     * 
     * @param fileName
     *            根据文件路径
     * @param size
     *            文件大小样式(KB或MB或GB)
     * @return
     */
    public static String size(String fileName, String size) {
        return size(newFile(fileName), size);
    }

    private InputStream is;
    private OutputStream os;

    private FileUtil02() {
    }

    private void setInputStreamAndOutputStream(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    private void copyThread(File f1, File f2, boolean append) {
        try {
            is = new FileInputStream(f1);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            os = new FileOutputStream(f2, append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(os);
            byte[] barr = new byte[1024];
            int len = 0;
            while ((len = bis.read(barr)) != -1) {
                bos.write(barr, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}