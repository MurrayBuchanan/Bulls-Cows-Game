package BullsAndCows;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Game{
    Player currentPlayer;
    Boolean codeType;
    SecretCode secretCode = new SecretCode();
    ArrayList<Guess> guesses = new ArrayList<>();
    int hints = 0;
    public SecretCode createCode(){
        secretCode.code = "";
        secretCode.decipheredCode = "";
        secretCode.codeLength = 4;
        return secretCode;
    }
    public static class Guess {
        String code;
        int bulls;
        int cows;
    }
    public static Game.Guess createGuess(String code, int bulls, int cows){
        Game.Guess guess = new Game.Guess();
        guess.code = code;
        guess.bulls = bulls;
        guess.cows = cows;
        return guess;
    }
    public Game(Player p, Boolean type){
        currentPlayer = p;
        codeType = type;
        createCode();
    }
    public void playGame(){
        welcome();
        loadPlayer();
        while(true) { // Loop till endGame() || playGame() again
            try {
                enterGuess();
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException by enterGuess().");
            } catch (IOException e) {
                System.out.println("IOException by enterGuess().");
            }
        }
    }
    public void loadPlayer(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter a username: ");
        currentPlayer.username = scan.next();

        String input = "";
        boolean isValid = false;

        System.out.println("Would you like to guess a word or number? \nEnter either: [word] or [number]");
        while (!isValid) {
            input = scan.next();
            if (input.equalsIgnoreCase("word")) {
                codeType = true; isValid = true;
                getCodeLength();
                requestCode();
            } else if (input.equalsIgnoreCase("number")) {
                codeType = false; isValid = true;
                getCodeLength();
                requestCode();
            } else {
                System.out.println("Invalid input, try again.");
            }
        }
        commandList();
    }
    public void getCodeLength(){
        boolean valid = false;
        while (!valid) {
            System.out.println("Would you like to play a game with 4 or 8 characters?");
            Scanner s = new Scanner(System.in);
            String input = s.next();
            if (input.equals("4")) {
                secretCode.codeLength = 4;
                valid = true;
            } else if (input.equals("8")) {
                secretCode.codeLength = 8;
                valid = true;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
    }
    private void commandList() {
        System.out.println("----- Command List -----");
        System.out.println("[undo] to undo the last character.");
        System.out.println("[hint] to receive a character.");
        System.out.println("[solution] to show solution.");
        System.out.println("[quit] to end the game.");
        System.out.println("[leaderboard] to see top players.");
        System.out.println("[stats] to see current stats.");
        System.out.println("[save] to save game so far.");
        System.out.println("[load] to load game.");
        System.out.println("------------------------");
    }
    private void welcome() {
        System.out.println("----- Bulls and Cows -----");
        System.out.println("Bulls = correct code, correct position.");
        System.out.println("Cows = correct code, incorrect position.");
        System.out.println("--------------------------");
    }
    public void requestCode(){
        if (codeType) {
            secretCode.code = String.valueOf(new LettersCode(secretCode.codeLength).code);
        }
        else {
            secretCode.code = String.valueOf(new NumbersCode(secretCode.codeLength).code);
        }
    }
    public void enterGuess() throws IOException {
        currentPlayer.incrementCodesAttempted();
        String guess = "";
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a guess: ");
        while (!guess.equalsIgnoreCase(secretCode.getCode())) {
            System.out.print(guess);
            String input = scan.next();
            if (input.equalsIgnoreCase("undo") && guess.length() > 0) {
                guess = guess.substring(0, guess.length() - 1);
            } else if (input.equalsIgnoreCase("hint")) {
                getHint();
            } else if (input.equalsIgnoreCase("solution")){
                showSolution();
            } else if (input.equalsIgnoreCase("quit")) {
                endGame();
            } else if (input.equalsIgnoreCase("save")) {
                saveGame();
                endGame();
            } else if (input.equalsIgnoreCase("load")) {
                loadGame();
                hints = 0;
                return;
            } else if (input.equalsIgnoreCase("stats")) {
                displayStats();
                return;
            } else if (input.equalsIgnoreCase("leaderboard")){
                leaderboards();
            }else {
                guess += input;
            }
            if (guess.length() == secretCode.codeLength) {
                calculateCows(secretCode, guess); // Updates cows before repeating
                calculateBulls(secretCode, guess); // Updates cows before repeating
                if (guess.equalsIgnoreCase(secretCode.getCode())) {
                    currentPlayer.updateBulls(calculateBulls(secretCode, guess));
                    currentPlayer.updateCows(calculateCows(secretCode, guess));
                    currentPlayer.incrementCodesDeciphered();
                    displayStats();
                    System.out.println("Congratulations, you won!");
                    hints = 0;
                    requestCode();
                    enterGuess();
                    return;
                }
                Guess g = createGuess(guess, calculateBulls(secretCode, guess), calculateCows(secretCode, guess));
                guesses.add(g);
                System.out.println("Guess: " + guess + " | Bulls: "  + calculateBulls(secretCode, guess) + "| Cows:" + calculateCows(secretCode, guess));
                currentPlayer.updateBulls(calculateBulls(secretCode, guess));
                currentPlayer.updateCows(calculateCows(secretCode, guess)); // Update total number of player's bulls/cows
                return;
            } else if (guess.length() > secretCode.codeLength) {
                System.out.println("Invalid input, try again.");
                return;
            }
        }
    }

    private void displayStats() {
        int attempts = currentPlayer.getCodesAttempted();
        int deciphered = currentPlayer.getCodesDeciphered();
        double accuracy;
        if (attempts > 0) {
            accuracy = (double)deciphered / attempts * 100;
            accuracy = (double) Math.round(accuracy * 10) / 10;
        } else {
            accuracy = 0;
        }
        System.out.println("------- Player Stats -------");
        System.out.println("Player: " + currentPlayer.username);
        System.out.println("Codes attempted: " + attempts + " | Codes Deciphered: " + deciphered + " | Code accuracy: " + accuracy + "%");
        System.out.println("Hints used: " + hints);
        System.out.println("----------------------------");
    }
    public static int calculateBulls(SecretCode secretCode, String guess){
        int bulls = 0;
        for (int i = 0; i < secretCode.getCode().length(); i++) {
            if (guess.charAt(i) == secretCode.getCode().charAt(i)) {
                bulls++;
            }
        }
        return bulls;
    }
    public static int calculateCows(SecretCode secretCode, String guess){
        int cows = 0;
        String secretCodeString = secretCode.getCode();
        StringBuilder sb = new StringBuilder(secretCodeString);
        for(int i = 0; i < guess.length(); i++){
            String temp = String.valueOf(guess.charAt(i));
            if(sb.toString().contains(temp) && secretCodeString.charAt(i) != guess.charAt(i)){ //filters out bulls and not correct characters
                // System.out.println(sb.toString());
                int counter = 0;
                while(counter < sb.toString().length() && sb.toString().charAt(counter) != guess.charAt(i) ){
                    counter++;
                }
                if(counter < sb.toString().length()) {
                    sb.deleteCharAt(counter); //ensures same char isn't flagged twice
                }
                cows++;
            }
        }
        return cows;
    }
    public String undoGuess(String input){
        if (input.length() > 0) {
            input.substring(0, input.length()-1);
        } else {
            System.out.println("Input is already cleared.");
        }
        return input;
    }
    public String getHint() {
        if (hints >= secretCode.getCode().length()) {
            System.out.println("Code is already revealed.");
            return secretCode.getCode();
        }
        String hint = secretCode.getCode().substring(0, hints + 1);
        System.out.println("Hint: " + hint);

        hints++;
        return hint;
    }

    public void saveGame() throws IOException {
        try {
            File save = new File("BullsAndCows/assets/saves/" + currentPlayer.username + "_save.txt");
            if (save.exists()) {
                System.out.println("Save file already exits, would you like to overwrite? (yes/no)");
                Scanner scan = new Scanner(System.in);
                if (scan.next().equals("yes")){
                    writeFile(save);
                    System.out.println("Game has been saved.");
                }
                else if (scan.next().equals("no")) {
                    System.out.println("Save cancelled.");
                    return;
                }
                else {
                    System.out.println("Invalid input, please try again.");
                    saveGame();
                }
            }
            else {
                save.createNewFile();
                writeFile(save);
            }
        }
        catch (FileNotFoundException e) {
            File save = new File("BullsAndCows/assets/saves/" + currentPlayer.username + "_save.txt");
            saveGame();
        }
        catch (IOException e) {
            System.out.println("Error saving to file");
        }
    }
    public void writeFile(File save) throws IOException {
        try {
            PrintWriter printer = new PrintWriter(save);
            printer.print(""); // Empty file contents
            printer.close();
            FileWriter writer = new FileWriter(save);
            writer.write(currentPlayer.username + "\n");
            writer.write(currentPlayer.getBulls() + "\n");
            writer.write(currentPlayer.getCows() + "\n");
            writer.write(codeType.toString() + "\n");
            writer.write(secretCode.getCode() + "\n");
            for (int i = 0; i < guesses.size(); i++) {
                writer.write(guesses.get(i).code + "\n");
                writer.write(guesses.get(i).bulls + "\n");
                writer.write(guesses.get(i).cows + "\n");
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }
    public void loadGame() throws FileNotFoundException{
        try {
            File save = new File("BullsAndCows/assets/saves/" + currentPlayer.username + "_save.txt");
            if (!save.exists()) {
                System.out.println("No saved game found");
                return;
            }
            Scanner s = new Scanner(save);
            currentPlayer.username = s.next();
            currentPlayer.setBulls(s.nextInt());
            currentPlayer.setCows(s.nextInt());
            codeType = s.next().equals("true");
            secretCode.code = s.next();
            while (s.hasNext()) {
                String guess = s.next();
                int bulls = s.nextInt();
                int cows = s.nextInt();
                Guess g = createGuess(guess, bulls, cows);
                guesses.add(g);
            }
            s.close();
            System.out.println("Game has been loaded.");
        }
        catch (Exception e) {
            System.out.println("Error loading from file");
        }
    }
    public void leaderboards(){
        System.out.println("------------------ Leaderboard ------------------");
        int counter = 1;
        ArrayList<Player> players = Players.getAllPlayers();
        players.sort(Comparator.comparingInt(Player::getCodesDeciphered).reversed());
        for (int i = 0; i < 10; i++){
            try {
                Player player = players.get(i);
                if (player.username.length() < 5) {
                    System.out.println(counter + ".\t Name: " + player.username + "\t\t\tCodes Deciphered: " + player.getCodesDeciphered());
                }
                else {
                    System.out.println(counter + ".\t Name: " + player.username + "\t\tCodes Deciphered: " + player.getCodesDeciphered());
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println(counter + ".\t Empty Slot");
            }
            counter++;
        }
        System.out.println("-------------------------------------------------");
    }
    public String showSolution() {
        System.out.println("Solution is: " + secretCode.getCode());
        return secretCode.getCode();
    }
    public void endGame() {
        Players.addPlayer(currentPlayer);
        Players.savePlayers();
        System.exit(0);
    }
}