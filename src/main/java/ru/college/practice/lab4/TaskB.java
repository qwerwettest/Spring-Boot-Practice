package ru.college.practice.lab4;

/**
 * Лабораторная работа 4.1, задание Б, вариант 5.
 *
 * Одноклеточная амёба каждые три часа делится на 2 клетки.
 * Определить, сколько амёб будет через 3, 6, 9, 12, …, 24 часа (ответ: 256).
 */
public final class TaskB {

    /** Период деления, ч. */
    public static final int DIVISION_PERIOD = 3;
    /** Время наблюдения, ч. */
    public static final int TOTAL_HOURS = 24;

    private TaskB() {
    }

    /** Количество амёб через {@code hours} часов, если в начале была одна. */
    public static long amoebasAfter(int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("Время не может быть отрицательным");
        }
        long count = 1;
        for (int t = DIVISION_PERIOD; t <= hours; t += DIVISION_PERIOD) {
            count *= 2;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("Часы | Амёб");
        System.out.println("-----+-----");
        long count = 1;
        for (int t = DIVISION_PERIOD; t <= TOTAL_HOURS; t += DIVISION_PERIOD) {
            count *= 2;   // каждые 3 часа каждая амёба делится надвое
            System.out.printf("%4d | %d%n", t, count);
        }
        System.out.printf("%nЧерез %d часа будет %d амёб%n", TOTAL_HOURS, count);
    }
}
