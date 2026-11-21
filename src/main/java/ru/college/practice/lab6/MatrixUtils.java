package ru.college.practice.lab6;

import java.util.Random;

/** Общие действия с матрицами: заполнение случайными числами и вывод. */
public final class MatrixUtils {

    private MatrixUtils() {
    }

    /** Квадратная матрица n×n со случайными целыми числами из диапазона [min, max]. */
    public static int[][] random(int n, int min, int max, Random random) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = min + random.nextInt(max - min + 1);
            }
        }
        return a;
    }

    public static void print(int[][] a) {
        for (int[] row : a) {
            for (int value : row) {
                System.out.printf("%5d", value);
            }
            System.out.println();
        }
    }
}
