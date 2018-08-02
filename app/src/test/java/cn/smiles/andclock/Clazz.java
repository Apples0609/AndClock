package cn.smiles.andclock;

import java.io.File;
import java.io.IOException;

/**
 * @author kaifang
 * @date 2018年8月2日上午9:39:45
 */
public class Clazz {
    public static String showFiles(String path) throws IOException {
        File ph = new File(path);
        if (ph.exists()) {
            if (ph.isDirectory()) {
                StringBuilder sbr = new StringBuilder();
                sbr.append("==============").append('\n');
                sbr.append(path).append('\n');
                File[] list = ph.listFiles();
                for (File f : list) {
                    sbr.append(f.getCanonicalPath()).append('\n');
                }
                sbr.append("====================共").append('\n');
                sbr.append(list.length).append('\n');
                sbr.append("文件================").append('\n');
                return sbr.toString();
            } else {
                return path + "\n不是目录";
            }
        } else {
            return "路径不存在";
        }
    }
}

