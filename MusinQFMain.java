import java.io.*;
import java.util.*;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class MusinQFMain {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        TaskDistS solver = new TaskDistS();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskDistS {
        public void solve(int testNumber, InputReader in, OutputWriter out) {
            if (false) {
                new Tester().test();
            }
            char[] s = in.next().toCharArray();
            int n = in.readInt();
            out.print(new Solver().solve(s, n));
        }

        class Tester {
            Random rnd = new Random(239);

            char[] genString(int alphaSize, int len) {
                char[] s = new char[len];
                for (int i = 0; i < len; i++) {
                    s[i] = (char) ('a' + rnd.nextInt(alphaSize));
                }
                return s;
            }

            void test() {
                for (int t = 0; ; t++) {
                    int k = rnd.nextInt(5) + 1;
                    int n = rnd.nextInt(20) + k;
                    char[] s = genString(3, k);
                    long sol = new Solver().solve(s, n);
                    long nai = new Naive().solve(s, n);
                    if (sol != nai) {
                        new Solver().solve(s, n);
                        new Naive().solve(s, n);
                    }
                }
            }

        }

        class Naive {
            long solve(char[] s, int n) {
                String t = new String(s);
                while (t.length() < n) {
                    t += new String(s);
                }
                while (t.length() > n) {
                    t = t.substring(0, t.length() - 1);
                }
                Set<String> all = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j <= n; j++) {
                        all.add(t.substring(i, j));
                    }
                }
                return all.size();
            }

        }

        class Solver {
            char[] s;
            int k;
            int n;
            int PRIME = 31;
            long[] primePow;
            long[] prefixH;
            long[] suffixH;

            long solve(char[] s, int n) {
                this.s = s;
                this.n = n;
                k = s.length;
                init();
                long ans = 0;
                Set<Long>[] used = new Set[k * 3 + 1];
                for (int i = 0; i < used.length; i++) {
                    used[i] = new HashSet<>();
                }
                for (int suf = k; suf >= 1; suf--) {
                    for (int pre = k; pre >= 1; pre--) {
                        long h = suffixH[suf] + prefixH[pre] * primePow[suf];
                        if (k + pre <= n && used[suf + pre].add(h)) {
                            ans++;
                        }
                        h = +
                                suffixH[suf] +
                                suffixH[k] * primePow[suf] +
                                prefixH[pre] * primePow[suf + k];
                        int midCount = (n - (k + k + pre)) / k + 1;
                        if (k + k + pre <= n && midCount > 0 && used[suf + k + pre].add(h)) {
                            ans += midCount;
                        }
                    }
                }
                for (int i = 0; i < k; i++) {
                    long h = 0;
                    for (int j = i; j < k; j++) {
                        h += (s[j] - 'a' + 1) * primePow[j - i];
                        if (used[j - i + 1].add(h)) {
                            ans++;
                        }
                    }
                }
                return ans;
            }

            void init() {
                primePow = new long[2 * k + 1];
                primePow[0] = 1;
                for (int i = 1; i < primePow.length; i++) {
                    primePow[i] = primePow[i - 1] * PRIME;
                }
                prefixH = new long[k + 1];
                for (int i = 0; i < k; i++) {
                    prefixH[i + 1] = prefixH[i] + (s[i] - 'a' + 1) * primePow[i];
                }
                suffixH = new long[k + 1];
                for (int i = 0; i < k; i++) {
                    suffixH[i + 1] = (s[k - i - 1] - 'a' + 1) + suffixH[i] * PRIME;
                }
            }

        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void close() {
            writer.close();
        }

        public void print(long i) {
            writer.print(i);
        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return readString();
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}

