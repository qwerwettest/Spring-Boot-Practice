package ru.college.practice.lab5;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Лабораторная работа 5.1, задание А, вариант 5.
 *
 * Если у массива A(30) есть элемент, равный B, то переменной X присвоить значение, равное сумме
 * всех положительных чётных элементов, предшествующих первому по порядку такому элементу,
 * иначе переменной X присвоить 0.
 */
public final class TaskA {

    public static final int SIZE = 30;

    private TaskA() {
    }

    /**
     * Сумма положительных чётных элементов, стоящих до первого элемента, равного {@code b}.
     * Если такого элемента нет — 0.
     */
    public static int sumPositiveEvenBeforeFirst(int[] a, int b) {
        int sum = 0;
        for (int value : a) {
            if (value == b) {
                return sum;           // нашли первый элемент, равный B
            }
            if (value > 0 && value % 2 == 0) {
                sum += value;
            }
        }
        return 0;                     // элемента, равного B, в массиве нет
    }

    /** Номер первого элемента, равного {@code b}, или −1. Нужен только для вывода. */
    public static int indexOf(int[] a, int b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Random random = new Random();
        int[] a = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            a[i] = random.nextInt(21) - 10;   // диапазон −10…10
        }
        System.out.println("Массив A(" + SIZE + "):");
        System.out.println(Arrays.toString(a));

        Scanner in = new Scanner(System.in);
        System.out.print("Введите B: B = ");
        int b = in.nextInt();

        int index = indexOf(a, b);
        int x = sumPositiveEvenBeforeFirst(a, b);
        if (index >= 0) {
            System.out.printf("Первый элемент, равный %d, стоит на позиции %d (нумерация с 1)%n", b, index + 1);
        } else {
            System.out.printf("Элемента, равного %d, в массиве нет%n", b);
        }
        System.out.println("X = " + x);
    }
}
