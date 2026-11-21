package ru.college.practice.lab4;

import java.util.Scanner;

/**
 * Лабораторная работа 4.1, задание А, вариант 5.
 *
 * <pre>
 *        n   (   k             k     )
 *   P =  ∏   ( ----- −  cos  |x|     )
 *       k=1  ( k + 1                 )
 * </pre>
 *
 * cos<sup>k</sup>|x| — это (cos|x|)<sup>k</sup>; степень накапливается по ходу цикла,
 * поэтому Math.pow не нужен.
 *
 * Одно и то же произведение посчитано тремя видами циклов (for, while, do-while).
 */
public final class TaskA {

    private TaskA() {
    }

    /** Цикл с параметром. */
    public static double productFor(int n, double x) {
        checkN(n);
        double cosAbsX = Math.cos(Math.abs(x));
        double cosPow = 1;      // cos^0 |x|
        double p = 1;           // нейтральный элемент для произведения
        for (int k = 1; k <= n; k++) {
            cosPow *= cosAbsX;  // теперь cos^k |x|
            p *= (double) k / (k + 1) - cosPow;
        }
        return p;
    }

    /** Цикл с предусловием. */
    public static double productWhile(int n, double x) {
        checkN(n);
        double cosAbsX = Math.cos(Math.abs(x));
        double cosPow = 1;
        double p = 1;
        int k = 1;
        while (k <= n) {
            cosPow *= cosAbsX;
            p *= (double) k / (k + 1) - cosPow;
            k++;
        }
        return p;
    }

    /** Цикл с постусловием (тело выполняется минимум один раз, поэтому n >= 1 обязательно). */
    public static double productDoWhile(int n, double x) {
        checkN(n);
        double cosAbsX = Math.cos(Math.abs(x));
        double cosPow = 1;
        double p = 1;
        int k = 1;
        do {
            cosPow *= cosAbsX;
            p *= (double) k / (k + 1) - cosPow;
            k++;
        } while (k <= n);
        return p;
    }

    private static void checkN(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n должно быть натуральным числом (n >= 1)");
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n;
        do {
            System.out.print("Введите n (натуральное число): n = ");
            n = in.nextInt();
        } while (n < 1);

        System.out.print("Введите x: x = ");
        double x = Double.parseDouble(in.next().replace(',', '.'));

        System.out.println("P (for) = " + productFor(n, x));
        System.out.println("P (while) = " + productWhile(n, x));
        System.out.println("P (do-while) = " + productDoWhile(n, x));
    }
}
