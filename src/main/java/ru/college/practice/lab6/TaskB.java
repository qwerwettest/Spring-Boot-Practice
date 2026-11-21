package ru.college.practice.lab6;

import java.util.Random;
import java.util.Scanner;

/**
 * Лабораторная работа 6, задание Б, вариант 5.
 *
 * Дана целочисленная квадратная матрица. Определить минимум среди сумм модулей элементов
 * диагоналей, параллельных побочной диагонали матрицы.
 *
 * Элементы одной диагонали, параллельной побочной, имеют одинаковую сумму индексов i + j = s.
 * Для матрицы n×n s меняется от 0 до 2n−2; s = n−1 — сама побочная диагональ, она не учитывается.
 * Угловые «диагонали» из одного элемента (s = 0 и s = 2n−2) учитываются.
 */
public final class TaskB {

    private TaskB() {
    }

    /** Сумма модулей элементов диагонали с суммой индексов {@code s}. */
    public static int absSumOfDiagonal(int[][] a, int s) {
        int n = a.length;
        int sum = 0;
        // j = s − i должно лежать в [0, n−1], отсюда границы для i
        for (int i = Math.max(0, s - (n - 1)); i <= Math.min(s, n - 1); i++) {
            sum += Math.abs(a[i][s - i]);
        }
        return sum;
    }

    /**
     * Номер s диагонали (i + j = s), параллельной побочной, с минимальной суммой модулей.
     * Для матрицы 1×1 таких диагоналей нет — возвращается −1.
     */
    public static int minDiagonalIndex(int[][] a) {
        int n = a.length;
        int best = -1;
        int bestSum = Integer.MAX_VALUE;
        for (int s = 0; s <= 2 * n - 2; s++) {
            if (s == n - 1) {
                continue;          // сама побочная диагональ
            }
            int sum = absSumOfDiagonal(a, s);
            if (sum < bestSum) {
                bestSum = sum;
                best = s;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n;
        do {
            System.out.print("Введите порядок матрицы n (n >= 2): n = ");
            n = in.nextInt();
        } while (n < 2);

        int[][] a = MatrixUtils.random(n, -9, 9, new Random());
        System.out.println("Сгенерированная матрица:");
        MatrixUtils.print(a);

        System.out.println("\nСуммы модулей диагоналей, параллельных побочной (i + j = s):");
        for (int s = 0; s <= 2 * n - 2; s++) {
            if (s == n - 1) {
                System.out.printf("s = %2d: побочная диагональ — не учитывается%n", s);
                continue;
            }
            System.out.printf("s = %2d: %d%n", s, absSumOfDiagonal(a, s));
        }

        int best = minDiagonalIndex(a);
        System.out.printf("%nМинимум = %d (диагональ i + j = %d)%n", absSumOfDiagonal(a, best), best);
    }
}
