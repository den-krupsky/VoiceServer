package by.sparky;

import java.util.Arrays;

import static by.sparky.ClientStatus.ACCEPT;

public class Test {
    public static void main(String[] args) {
        byte[] data1 = new byte[] {3,3};
        byte[] data2 = new byte[] {2,2};

        System.out.println(Arrays.equals(data1, ACCEPT.getCode()));
    }
}
