package ru.college.practice.lab4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class Lab4Test {

    private static final double EPS = 1e-12;

    /** Прямой расчёт по формуле через Math.pow — эталон для сравнения. */
    private static double reference(int n, double x) {
        double p = 1;
        for (int k = 1; k <= n; k++) {
            p *= (double) k / (k + 1) - Math.pow(Math.cos(Math.abs(x)), k);
        }
        return p;
    }

    @Test
    void productAtZeroIsCountedByHand() {
        // x = 0 → cos|x| = 1:  (1/2 − 1) · (2/3 − 1) = (−1/2) · (−1/3) = 1/6
        assertEquals(-0.5, TaskA.productFor(1, 0), EPS);
        assertEquals(1.0 / 6, TaskA.productFor(2, 0), EPS);
    }

    @Test
    void allThreeLoopsGiveSameResult() {
        int[] ns = {1, 2, 5, 10};
        double[] xs = {-2.5, 0, 1, 3.7};
        for (int n : ns) {
            for (double x : xs) {
                double expected = reference(n, x);
                assertEquals(expected, TaskA.productFor(n, x), EPS);
                assertEquals(expected, TaskA.productWhile(n, x), EPS);
                assertEquals(expected, TaskA.productDoWhile(n, x), EPS);
            }
        }
    }

    @Test
    void rejectsNonNaturalN() {
        assertThrows(IllegalArgumentException.class, () -> TaskA.productDoWhile(0, 1));
    }

    @Test
    void amoebasAfter24HoursIs256() {
        assertEquals(1, TaskB.amoebasAfter(0));
        assertEquals(2, TaskB.amoebasAfter(3));
        assertEquals(16, TaskB.amoebasAfter(12));
        assertEquals(256, TaskB.amoebasAfter(24));
    }
}
