package com.odianyun.util.sensi;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class SensitiveFilterTest extends TestCase {

    public void test() throws Exception {

        // 使用默认单例（加载默认词典）
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        // 向过滤器增加一个词
        filter.put("婚礼上唱春天在哪里");

        // 待过滤的句子
        String sentence = "然后，市长在婚礼上唱春天在哪里。";
        // 进行过滤
        String filted = filter.filter(sentence, '*');

        // 如果未过滤，则返回输入的String引用
        if (sentence != filted) {
            // 句子中有敏感词
            System.out.println(filted);
        }

    }

    public void testLogic() {

        SensitiveFilter filter = new SensitiveFilter();

        filter.put("你好");
        filter.put("你好3");
        filter.put("抢银行");
        filter.put("某国");

        System.out.println(filter.filter("你好3天不见,非常想念,我们去某国抢银行", '*'));
        System.out.println(filter.check("你好3天不见,非常想念,我们去某国抢银行"));

    }

    public void testSpeed() throws Exception {

        PrintStream ps = new PrintStream("E:\\abcd.txt");

        File file = new File("E:\\abc.txt");

        List<String> testSuit = new ArrayList<String>(1048576);
        long length = 0;


            if (file.isFile() && file.getName().endsWith(".txt")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gb18030"));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (line.trim().length() > 0) {
                        testSuit.add(line);
                        length += line.length();
                    }
                }
                br.close();
            }


        System.out.println(String.format("待过滤文本共 %d 行，%d 字符。", testSuit.size(), length));


        SensitiveFilter filter = SensitiveFilter.DEFAULT;

        int replaced = 0;

        for (String sentence : testSuit) {
            String result = filter.filter(sentence, '*');
            if (result != sentence) {
                ps.println(sentence);
                ps.println(result);
                ps.println();
                replaced++;
            }
        }
        ps.close();

        long timer = System.currentTimeMillis();
        for (String line : testSuit) {
            filter.filter(line, '*');
        }
        timer = System.currentTimeMillis() - timer;
        System.out.println(String.format("过滤耗时 %1.3f 秒， 速度为 %1.1f字符/毫秒", timer * 1E-3, length / (double) timer));
        System.out.println(String.format("其中 %d 行有替换", replaced));

    }

}
