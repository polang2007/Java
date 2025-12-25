package fileRead;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileRead {
    public static void main(String[] args) {
        scannerDemo("chunxiao.txt");
//        System.out.println(System.getProperty("user.dir"));
    }

    private static void scannerDemo(String filePath) {
        File file = new File(filePath);          // 准备测试文件
        System.out.println("Working dir: " + System.getProperty("user.dir"));
        System.out.println("Trying path: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("File not found at given path.");
            // 尝试从 classpath 加载（例如 resources 或 src 下）
            InputStream in = FileRead.class.getResourceAsStream("/" + filePath);
            if (in == null) {
                System.out.println("Also not found on classpath: /" + filePath);
                // 如果你希望，也可以尝试常见位置，例如项目根或 resources 文件夹
                Path alt = Paths.get("src", "main", "resources", filePath);
                if (Files.exists(alt)) {
                    file = alt.toFile();
                    System.out.println("Found at: " + alt.toAbsolutePath());
                } else {
                    System.out.println("Not found at src/main/resources either: " + alt.toAbsolutePath());
                    // 提前返回或抛出异常
                    return;
                }
            } else {
                // 从 classpath 的 InputStream 读取
                try (Scanner sc = new Scanner(in, "UTF-8")) {
                    sc.useDelimiter("\\s+");
                    int words = 0;
                    while (sc.hasNext()) { sc.next(); words++; }
                    System.out.println("Total words (from classpath): " + words);
                    return;
                }
            }
        }

        int words = 0;
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            sc.useDelimiter("\\s+");               // 空白符分隔
            while (sc.hasNext()) {
                sc.next();
                words++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Total words: " + words);
    }

}
