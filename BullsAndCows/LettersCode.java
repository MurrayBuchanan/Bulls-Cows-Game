package BullsAndCows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class LettersCode extends SecretCode{
    String code;
    public LettersCode(int len) {
        File file;
        if (len == 4) {
            file = new File("BullsAndCows/assets/words.txt").getAbsoluteFile();
        } else {
            file = new File("BullsAndCows/assets/words8.txt").getAbsoluteFile();
        }
        ArrayList<String> words = readFile(file);
        Random random = new Random();
        try {
            code = words.get(random.nextInt(15));
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Unable to retrieve code");
            if (len == 4) {
                code = "abcd";
            } else {
                code = "abcdefgh";
            }
        }
    }
    private ArrayList<String> readFile(File file) {
        ArrayList<String> array = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                array.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return array;
    }
}