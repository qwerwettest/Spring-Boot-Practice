package ru.college.practice.lab5;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Lab5Test {

    @Test
    void sumsPositiveEvenBeforeFirstMatch() {
        int[] a = {4, -2, 3, 6, 7, 5, 8, 7};
        // до первой 7: 4 и 6 — положительные чётные, −2 отрицательное, 3 нечётное
        assertEquals(10, TaskA.sumPositiveEvenBeforeFirst(a, 7));
        assertEquals(4, TaskA.indexOf(a, 7));
    }

    @Test
    void returnsZeroWhenNoMatch() {
        int[] a = {2, 4, 6};
        assertEquals(0, TaskA.sumPositiveEvenBeforeFirst(a, 100));
        assertEquals(-1, TaskA.indexOf(a, 100));
    }

    @Test
    void matchAtFirstPositionGivesZero() {
        assertEquals(0, TaskA.sumPositiveEvenBeforeFirst(new int[] {5, 2, 4}, 5));
    }

    @Test
    void countsEvenIncludingZeroAndNegative() {
        assertEquals(3, TaskB.countEven(new int[] {0, 1, -4, 7, 10}));
    }

    @Test
    void plainShiftFillsWithZeros() {
        int[] a = {1, 2, 3, 4, 5};
        TaskB.shiftRight(a, 2);
        assertArrayEquals(new int[] {0, 0, 1, 2, 3}, a);
    }

    @Test
    void plainShiftByLengthOrMoreGivesZeros() {
        int[] a = {1, 2, 3};
        TaskB.shiftRight(a, 5);
        assertArrayEquals(new int[] {0, 0, 0}, a);
    }

    @Test
    void cyclicShiftWrapsAround() {
        int[] a = {1, 2, 3, 4, 5};
        TaskB.cyclicShiftRight(a, 2);
        assertArrayEquals(new int[] {4, 5, 1, 2, 3}, a);
    }

    @Test
    void cyclicShiftByMultipleOfLengthKeepsArray() {
        int[] a = {1, 2, 3};
        TaskB.cyclicShiftRight(a, 6);
        assertArrayEquals(new int[] {1, 2, 3}, a);
    }
}
