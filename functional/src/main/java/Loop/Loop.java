package Loop;

import java.util.stream.IntStream;

public class Loop {
    public static void main(String[] args) {
        // 演示 range (左闭右开)
        foriloop(0, 5);

        // 演示 rangeClosed (左右均闭)
        foriloop2(0, 5);

        // 打印简单乘法表（嵌套流）
        mulLoop();
    }

    // for-i
    // 左闭右开 [start, end)
    public static void foriloop(int start, int end) {
        IntStream.range(start, end)
                .forEach(n -> System.out.println(n));
    }

    // 左闭右闭 [start, end]
    public static void foriloop2(int start, int end) {
        IntStream.rangeClosed(start, end)
                .forEach(n -> System.out.println(n));
    }

    // 多层循环示例：打印 1..9 的乘法表
    public static void mulLoop() {
        IntStream.rangeClosed(1, 9)
                .forEach(i -> {
                            IntStream.rangeClosed(1, i)
                                    .forEach(j -> System.out.printf("%d×%d=%d  ", i, j, i * j));
                            System.out.println();
                        }
                );
    }
}