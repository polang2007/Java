package org.example;

import org.apache.commons.math3.linear.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!\n");

        // 定义一个2x2的矩阵
        double[][] matrixData = { {1, 2}, {3, 4} };
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        // 定义方程组右侧的常数项
        double[] vectorData = {5, 11};
        RealVector vector = new ArrayRealVector(vectorData);

        // 使用LU分解来解方程
        DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
        RealVector solution = solver.solve(vector);

        // 打印解
        System.out.println("方程组的解: " + solution);
    }
}