public class MemoryTest {
    public static void main(String[] args) {
        int blockCount = Integer.parseInt(args[0]);
        System.out.println("blockCount=" + blockCount);

        int blockSizeMB = Integer.parseInt(args[1]);
        System.out.println("blockSizeMB=" + blockSizeMB);

        boolean sleep = Boolean.parseBoolean(args[2]);
        System.out.println("sleep=" + sleep);

        int r = (int) System.currentTimeMillis();
        int sum = 0;

        System.out.println("expected time=" + (long) blockCount * blockSizeMB * 1024 * 1024 / 4);
        for (int i = 0; i < blockCount; i++) {
            int[] block = new int[blockSizeMB * 1024 * 1024 / 4];
            for (int  j = 0; j < block.length; j++)
            {
                r += block[j / 2];
                block[j] = r;
            }
            for (int j = 0; j < block.length; j += 3)
                sum += block[j];
        }

        System.out.println("sum=" + sum);

        if (sleep)
            for (;;);   
    }
}
