package ru.college.practice.lab6;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

class Lab6Test {

    @Test
    void writesPositiveRowSumsToAntiDiagonal() {
        int[][] a = {
                {1, -2, 3},
                {-4, 5, 6},
                {7, 8, -9},
        };
        int[] sums = TaskA.writeRowSumsToAntiDiagonal(a);

        assertArrayEquals(new int[] {4, 11, 15}, sums);
        // A[0][2], A[1][1], A[2][0] заменены, остальное не тронуто
        assertArrayEquals(new int[][] {
                {1, -2, 4},
                {-4, 11, 6},
                {15, 8, -9},
        }, a);
    }

    @Test
    void rowWithoutPositivesGivesZero() {
        assertEquals(0, TaskA.sumPositive(new int[] {-1, 0, -5}));
    }

    @Test
    void diagonalAbsSums() {
        int[][] a = {
                {1, -2, 3},
                {-4, 5, 6},
                {7, -8, 9},
        };
        assertEquals(1, TaskB.absSumOfDiagonal(a, 0));      // |1|
        assertEquals(6, TaskB.absSumOfDiagonal(a, 1));      // |−2| + |−4|
        assertEquals(15, TaskB.absSumOfDiagonal(a, 2));     // побочная: 3 + 5 + 7
        assertEquals(14, TaskB.absSumOfDiagonal(a, 3));     // |6| + |−8|
        assertEquals(9, TaskB.absSumOfDiagonal(a, 4));      // |9|
    }

    @Test
    void minSkipsAntiDiagonalItself() {
        int[][] a = {
                {5, 5, 0},
                {5, 0, 5},
                {0, 5, 5},
        };
        // у побочной сумма 0, но она не учитывается; остальные: 5, 10, 10, 5 → минимум на s = 0
        assertEquals(0, TaskB.minDiagonalIndex(a));
        assertEquals(5, TaskB.absSumOfDiagonal(a, 0));
    }

    @Test
    void randomMatrixHasValuesInRange() {
        int[][] a = MatrixUtils.random(6, -9, 9, new Random(42));
        for (int[] row : a) {
            for (int v : row) {
                if (v < -9 || v > 9) {
                    throw new AssertionError("Вне диапазона: " + v);
                }
            }
        }
    }
}
