package top.cutexingluo.tools.utils.se.algo.cpp.common;

import java.io.*;

/**
 * IO 快速读取
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 15:26
 * @since 1.0.3
 */
public class ExtremeIO extends BufferedWriter {
    private final BufferedReader br;

    public ExtremeIO() {
        super(new BufferedWriter(new OutputStreamWriter(System.out)));
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public ExtremeIO(InputStream in, OutputStream out) {
        super(new BufferedWriter(new OutputStreamWriter(out)));
        br = new BufferedReader(new InputStreamReader(in));
    }

    public String next() {
        StringBuilder sb = new StringBuilder();
        try {
            int c = br.read();
            while (c <= 32) {
                c = br.read();
            }
            while (c > 32) {
                sb.append((char) c);
                c = br.read();
            }
        } catch (Exception ignored) {
        }
        return sb.toString();
    }

    public int nextInt() {
        return (int) nextLong();
    }

    public long nextLong() {
        boolean negative = false;
        long x = 0;
        try {
            int c = br.read();
            while (c <= 32) {
                c = br.read();
            }
            if (c == '-') {
                negative = true;
                c = br.read();
            }
            while (c > 32) {
                x = x * 10 + c - '0';
                c = br.read();
            }
        } catch (Exception ignored) {
        }
        return negative ? -x : x;
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }
}
