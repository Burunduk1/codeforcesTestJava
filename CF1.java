import java.util.Arrays;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Locale;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;
 
public class CF1 {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskE solver = new TaskE();
        solver.solve(1, in, out);
        out.close();
    }
}
 
class TaskE {
    public void solve(int testNumber, InputReader in, PrintWriter out) {
        int n = in.nextInt();
        int k = in.nextInt();
        int[] v = new int[n];
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = 100 * in.nextInt();
        }
        for (int i = 0; i < n; i++) {
            c[i] = in.nextInt();
        }
 
        int[] p = new int[n];
        p[n - 1] = Math.min(c[n - 1], v[n - 1]);
        for (int i = n - 2; i >= 0; i--) {
            if (c[i] <= v[i]) {
                p[i] = c[i];
            } else {
                if (v[i] > p[i + 1]) {
                    p[i] = v[i];
                } else {
                    p[i] = Math.min(c[i], p[i + 1]);
                }
            }
        }
 
        Arrays.sort(p);
 
        double curPb = (k + 0D) / n;
        double res = 0d;
        for (int i = n - 1; i >= k - 1; i--) {
            res += curPb * p[n - i - 1];
            curPb = (1 - (k - 1d) / ((i - 1) + 1)) * curPb;
        }
        out.println(String.format(Locale.US, "%.7f", res));
    }
 
}
 
class InputReader {
    private BufferedReader reader;
    private String[] currentArray;
    private int curPointer;
 
    public InputReader(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }
 
 
    public int nextInt() {
        if ((currentArray == null) || (curPointer >= currentArray.length)) {
            try {
                currentArray = reader.readLine().split(" ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            curPointer = 0;
        }
        return Integer.parseInt(currentArray[curPointer++]);
    }
 
}