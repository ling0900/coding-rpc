package code.lh.algorithm.leetcode.day01;

import org.junit.Before;
import org.junit.Test;

/**
 * The type Solution.
 */
class Solution {

    /**
     * Divide int.
     *
     * @param dividend the 被除数
     * @param divisor  the 除数
     * @return the int
     */
    public static int divide(int dividend, int divisor) {


        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }

        // 标志结果是正数还是负数
        boolean resultNg = false;
        if ((dividend > 0 && divisor < 0) || (dividend < 0 && divisor > 0)) {
            resultNg = true;
        }

        // 防止溢出，全部弄成负数。
        if (dividend > 0 ) {
            dividend = -dividend;
        }

        if (divisor > 0) {
            divisor = -divisor;
        }

        if (dividend > divisor) {
            return 0;
        }

        // 判断是否相等
        if (dividend == divisor) {
            if (resultNg) {
                return -1;
            }
            return 1;
        }

        if (resultNg) {
            return - divided(dividend, divisor);
        }

        return divided(dividend, divisor);
    }

    private static int divided(int dividend, int divisor) {

        // dividend是被除数
        int m = 0;
        int orgindividend = dividend;

        while (dividend <= divisor) {
            System.out.println("减去" + (1 << m) + "倍的:" + (divisor << m));
            dividend = orgindividend - (divisor << m);
            System.out.println("结果是" + dividend);
            m ++;
        }



        m -= 1;

        int i = 1 << m;


        System.out.println("i=" + i);

        System.out.println(dividend + ":" + divisor);

        // 如果发现减的多了，这里需要再补偿回来。
        while (dividend > 0) {
            dividend += divisor;
            i --;
        }

        // 这里说明减去的还不够多，所以这里需要一倍的减。
        while (dividend - divisor < 0) {
            dividend -= divisor;
            i ++;
        }

        int abs = Math.abs(i);

        return abs;
    }

    public static void main(String[] args) {
        int divide = divide(Integer.MIN_VALUE, -2);
        System.out.println(divide);

    }

    @Test
    public void tets(){
        return;
    }

}
