package Currify;

import java.util.function.Function;

public class Curried {
    public static void main(String[] args) {
        // 柯里化的加法函数：接收一个整数，返回一个函数，再接收一个整数，返回结果
        Function<Integer, Function<Integer, Integer>> addCurried =
                a -> b -> a + b;
        // 使用方式
        Function<Integer, Integer> add5 = addCurried.apply(5); // 固定第一个参数为5
        int result = add5.apply(3); // 输出 8
        System.out.println("结果: " + result);
    }
}
