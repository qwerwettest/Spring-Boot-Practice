package ru.college.practice.lab6;

import java.util.Random;

/**
 * Лабораторная работа 6, задание А, вариант 5.
 *
 * Дан массив A[6,6]. Найти суммы положительных элементов строк и присвоить их
 * элементам побочной диагонали соответствующих строк.
 *
 * Побочная диагональ — элементы A[i][n−1−i] (из правого верхнего угла в левый нижний).
 */
public final class TaskA {

    public static final int SIZE = 6;

    private TaskA() {
    }

    /** Сумма положительных элементов строки. */
    public static int sumPositive(int[] row) {
        int sum = 0;
        for (int value : row) {
            if (value > 0) {
                sum += value;
            }
        }
        return sum;
    }

    /**
     * Для каждой строки i записывает сумму её положительных элементов в A[i][n−1−i].
     * Сумма считается по исходной строке, до замены элемента диагонали.
     *
     * @return найденные суммы по строкам
     */
    public static int[] writeRowSumsToAntiDiagonal(int[][] a) {
        int n = a.length;
        int[] sums = new int[n];
        for (int i = 0; i < n; i++) {
            sums[i] = sumPositive(a[i]);
            a[i][n - 1 - i] = sums[i];
        }
        return sums;
    }

    public static void main(String[] args) {
        int[][] a = MatrixUtils.random(SIZE, -9, 9, new Random());
        System.out.println("Исходная матрица A[" + SIZE + "," + SIZE + "]:");
        MatrixUtils.print(a);

        int[] sums = writeRowSumsToAntiDiagonal(a);

        System.out.println("\nСуммы положительных элементов строк:");
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("строка %d: %d%n", i + 1, sums[i]);
        }
        System.out.println("\nМатрица после записи сумм на побочную диагональ:");
        MatrixUtils.print(a);
    }
}
