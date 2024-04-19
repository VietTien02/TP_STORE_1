package com.pts;

public class Test {
    public static void main(String[] args) {
        int a = 123;
        String newString = Integer.toString(a);
        System.out.println("New String: " + newString);
        System.out.println(newString.getClass());
        for (int x = 1; x <= 100; x++) {
            for (int y = 1; y <= 100; y++) {
                int z = 100 - x - y; // Số con trâu già
                if (5 * x + 3 * y + z / 3 == 100 && z % 3 == 0) {
                    System.out.println("Số con trâu đứng: " + x);
                    System.out.println("Số con trâu nằm: " + y);
                    System.out.println("Số con trâu già: " + z);
                }
            }
        }
    }
}
