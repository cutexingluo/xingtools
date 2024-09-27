package top.cutexingluo.tools.utils.se.file;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 使用jdk原生的文件IO 方式
 * <br>
 * <p>1. 拥有7种以上IO调用方法</p>
 * <p>2. IO方式可以通过方法名得到</p>
 * <p>3. 如果有能使用的参数为File的方法，就不重载String</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 14:40
 */
public class XTIOUtil {
    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    //在不引入hu tools生成File
    public static File getFile(String path) {
        return XTFileUtil.getFile(path);
    }

    // ***************默认推荐读写
    // 读采取方法二 流读入
    public static Stream<String> readFile(String fileName) throws IOException {
        return readFileLinesByStreamLine(fileName, null);
    }

    public static Stream<String> readFile(String fileName, Consumer<Stream<String>> getLines) throws IOException {
        return readFileLinesByStreamLine(fileName, getLines);
    }

    //写采取jdk1.7 写入
    public static void writeFile(String fileName, List<String> content, boolean isAppend) throws IOException {
        if (isAppend) Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        else Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8);
    }

    // 覆盖写入
    public static void writeFile(String fileName, List<String> content) throws IOException {
        Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8);
    }

    public static void writeFile(String fileName, byte[] bytes) throws IOException {
        Files.write(Paths.get(fileName), bytes);
    }

    // 追加写入
    public static void appendFile(String fileName, List<String> content) throws IOException {
        Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    }


    //*************读
    // 方法一：Scanner
    // Java 1.5
    public static void readFileLineByScanner(String fileName, Consumer<String> consumer) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                consumer.accept(line);
            }
        }
    }

    // 使用分隔符
    public static void readFileWordByScanner(String fileName, Consumer<String> consumer, String delimiter) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            sc.useDelimiter(delimiter);
            while (sc.hasNext()) {
                String word = sc.next();
                consumer.accept(word);
            }
        }
    }

    // 使用分隔符，读取数字
    public static void readFileIntByScanner(String fileName, Consumer<Integer> consumer, String delimiter) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            sc.useDelimiter(delimiter);
            while (sc.hasNextInt()) {
                int intValue = sc.nextInt();
                consumer.accept(intValue);
            }
        }
    }

    // 方法二 StreamLine  特别推荐
    // 按行读取为流 ，特别方便 ， 不用一次加载，内存交换小 Java8
    // 可以用lines.parallel() 的到并行流
//    @ConditionalOnJava(JavaVersion.EIGHT)
    public static Stream<String> readFileLinesByStreamLine(String fileName, Consumer<Stream<String>> consumer) throws IOException {
        Stream<String> lines = Files.lines(Paths.get(fileName));
        if (consumer != null) consumer.accept(lines);
        return lines;
    }

    public static Stream<String> readFileLinesByStreamLine(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName));
    }

    // 方法三 readAllLines java8
//    @ConditionalOnJava(JavaVersion.EIGHT)
    public static List<String> readFileLinesByReadLines(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
    }

    //    @ConditionalOnJava(JavaVersion.EIGHT)
    public static byte[] readFileLinesByReadBytes(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileName));
    }


    // 方法四 readString  JDK11 读取文件不能超过2G 与内存相关
//    @ConditionalOnJava(JavaVersion.ELEVEN)
//    @ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER, value = JavaVersion.ELEVEN)
    public static String readFileStringByReadString(String fileName) throws IOException {
        String o = null;
        try {
            Method method = Files.class.getMethod("readString", Path.class, Charset.class);
            o = (String) method.invoke(null, Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            if (exceptionHandler != null) exceptionHandler.accept(e);
            else if (printTrace) e.printStackTrace();
        }
        return o;
//        return Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
    }

    // 方法五 readAllBytes  JDK7 读取文件不能超过2G 与内存相关
    public static String readFileStringByReadBytes(String fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    //方法六 BufferedReader 流读入 默认缓冲区8k
    public static void readFileLineByBufferedReader(String fileName, Consumer<String> consumer) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                consumer.accept(line);
            }
        }
    }

    // jdk 8也可使用
    public static void readFileLineByNewBufferedReader(String fileName, Consumer<String> consumer) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                consumer.accept(line);
            }
        }
    }

    // 流读取对象
    public static Object readFileObjectByStream(String fileName, Consumer<String> consumer) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        }
    }

    /**
     * 读取文本文件内容到字符串
     *
     * @param fileName 文件名
     * @return 字符串
     */
    public static String readUTF8ByInputStream(String fileName) throws IOException {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(fileContent);
        in.close();
        return new String(fileContent, encoding);
    }

    //*****************写
    // 方法一 newBufferedWriter java8 创建并写入
//    @ConditionalOnJava(JavaVersion.EIGHT)
    public static void writeFileLinesByNewBufferedWriter(String fileName, List<String> content) throws IOException {
        writeFileLinesByNewBufferedWriter(fileName, content, false);
    }

    public static void writeFileLinesByNewBufferedWriter(String fileName, List<String> content, boolean isAppend) throws IOException {
        try (BufferedWriter writer = isAppend ? Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8, StandardOpenOption.APPEND) :
                Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
            for (String s : content) {
                writer.write(s);
            }
        }
    }

    // 方法二 FilesWhite jdk1.7 最方便，创建并写入
    public static void writeFileStringByFilesWrite(String fileName, List<String> content, boolean isAppend) throws IOException {
        if (isAppend) Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        else Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8);
    }

    public static void writeFileStringByFilesWrite(String fileName, List<String> content) throws IOException {
        Files.write(Paths.get(fileName), content, StandardCharsets.UTF_8);
    }

    //方法三 PrintWriter 特别适合一行行写入 可创建写入
    public static void writeFileLineByPrintWriter(String fileName, List<String> content) throws IOException {
        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8.name())) { // JDk10标准编码
            content.forEach(writer::println);
        }
    }

    //方法四 FileWriter  不能创建文件写入
    public static void writeFileLineByFileWriter(File file, List<String> content) throws IOException {
        file.createNewFile();
        try (FileWriter writer = new FileWriter(file)) { // JDk10标准编码
            for (String s : content) {
                writer.write(s);
            }
        }
    }

    //方法五 FileStream 原始方法 流写入
    public static void writeFileLineByBufferedWriter(String fileName, List<String> content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(osw)) {
            for (String s : content) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
        }
    }
}
