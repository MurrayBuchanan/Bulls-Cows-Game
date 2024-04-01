package BullsAndCows;

import java.util.Random;

public class NumbersCode extends SecretCode {
    String code = "";
    public NumbersCode(int len) {
        int[] digits = new int[len];
        Random random = new Random();
        for (int i = 0; i < digits.length; i++) {
            boolean unique;
            int nextDigit;
            do {
                unique = true;
                nextDigit = random.nextInt(10);
                for (int j = 0; j < i; j++) {
                    if (digits[j] == nextDigit) {
                        unique = false;
                        break;
                    }
                }
            } while (!unique);

            digits[i] = nextDigit;
            code += nextDigit;
        }
    }
}