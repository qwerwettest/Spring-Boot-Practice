package ru.college.practice.lab5;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Лабораторная работа 5.1, задание Б, вариант 5.
 *
 * Дан целочисленный массив размера N. Осуществить сдвиг вправо на k позиций,
 * где k — число чётных элементов. Элементы массива генерируются случайно.
 *
 * В условии не сказано, циклический ли сдвиг, поэтому сделаны оба варианта:
 * обычный (освободившиеся слева места заполняются нулями) и циклический.
 */
public final class TaskB {

    private TaskB() {
    }

    /** Количество чётных элементов (0 и отрицательные чётные тоже считаются). */
    public static int countEven(int[] a) {
        int count = 0;
        for (int value : a) {
            if (value % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    /** Обычный сдвиг вправо на k: элементы, вышедшие за правый край, теряются, слева — нули. */
    public static void shiftRight(int[] a, int k) {
        int n = a.length;
        for (int i = n - 1; i >= 0; i--) {     // идём с конца, чтобы не затереть нужные элементы
            a[i] = (i - k >= 0) ? a[i - k] : 0;
        }
    }

    /** Циклический сдвиг вправо на k: элементы, вышедшие за правый край, переходят в начало. */
    public static void cyclicShiftRight(int[] a, int k) {
        int n = a.length;
        if (n == 0) {
            return;
        }
        k %= n;                                // сдвиг на n позиций ничего не меняет
        for (int step = 0; step < k; step++) { // k раз сдвигаем на одну позицию
            int last = a[n - 1];
            for (int i = n - 1; i > 0; i--) {
                a[i] = a[i - 1];
            }
            a[0] = last;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n;
        do {
            System.out.print("Введите размер массива N: N = ");
            n = in.nextInt();
        } while (n < 1);

        Random random = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(100);        // диапазон 0…99
        }
        System.out.println("Сгенерированный массив:");
        System.out.println(Arrays.toString(a));

        int k = countEven(a);
        System.out.println("Число чётных элементов k = " + k);

        int[] plain = a.clone();
        shiftRight(plain, k);
        System.out.println("Сдвиг вправо на k (с заполнением нулями):");
        System.out.println(Arrays.toString(plain));

        int[] cyclic = a.clone();
        cyclicShiftRight(cyclic, k);
        System.out.println("Циклический сдвиг вправо на k:");
        System.out.println(Arrays.toString(cyclic));
    }
}
