package BullsAndCows;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Players{
    static ArrayList<Player> allPlayers = new ArrayList<>();
    public static void addPlayer(Player p){
        allPlayers.add(p);
    }
    public static void savePlayers() { // Save file
        try {
            readFile(); // Read original file contents
            PrintWriter printer = new PrintWriter("BullsAndCows/assets/players.txt");
            printer.print(""); // Empty file contents
            printer.close();
            FileWriter writer = new FileWriter("BullsAndCows/assets/players.txt");
            for (Player player : allPlayers) {
                writer.write(player.username + " ");
                writer.write(player.getBulls() + " ");
                writer.write(player.getCows() + " ");
                writer.write(player.getCodesAttempted() + " ");
                writer.write(player.getCodesDeciphered() + " ");
                writer.write("\n");
            }
            writer.close();
            System.out.println("\nSaved successfully.");
        }
        catch (FileNotFoundException e) {
            File players = new File("BullsAndCows/assets/players.txt");
            savePlayers();
        }
        catch (IOException e) {
            System.out.println("\nError saving to file");
        }
    }
    
    public static void findPlayer(Player p) throws Exception {
        try {
            readFile();
            for (int i=0; i < allPlayers.size(); i++) {
                if (allPlayers.get(i).username.equals(p.username)) {
                    System.out.println("Username: " + allPlayers.get(i).username);
                    System.out.println("Total bulls: " + allPlayers.get(i).getBulls());
                    System.out.println("Total cows: " + allPlayers.get(i).getCows());
                    System.out.println("Codes attempted: " + allPlayers.get(i).getCodesAttempted());
                    System.out.println("Codes deciphered: " + allPlayers.get(i).getCodesDeciphered());
                    return;
                }
            }
            System.out.println("\nUnable to find player.");
        } catch (Exception e) {
            System.out.println("\nError finding player.");
        }
    }
    public static void readFile() { // Store original file contents
        try {
            File file = new File("BullsAndCows/assets/players.txt");
            Scanner s = new Scanner(file);
            ArrayList<Player> temp = new ArrayList<>();
            while (s.hasNext()) { // Read in existing players from file
                Player p = new Player();
                p.username = s.next();
                if (p.username == null) {
                    return;
                }
                int bulls = s.nextInt();
                p.setBulls(bulls);
                int cows = s.nextInt();
                p.setCows(cows);
                p.setCodesAttempted(s.nextInt());
                p.setCodesDeciphered(s.nextInt());
                temp.add(p);
            }
            for (int i = 0; i < temp.size(); i++) {
                boolean playerUpdated = false;
                for (int j = 0; j < allPlayers.size(); j++) {
                    if (temp.get(i).username.equals(allPlayers.get(j).username)) { // update existing player data in allPlayers
                        int newTotalBulls = (allPlayers.get(j).getBulls() + temp.get(i).getBulls());
                        allPlayers.get(j).setBulls(newTotalBulls);
                        int newTotalCows = (allPlayers.get(j).getCows() + temp.get(i).getCows());
                        allPlayers.get(j).setCows(newTotalCows);
                        int newTotalAttempted = allPlayers.get(j).getCodesAttempted() + temp.get(i).getCodesAttempted();
                        allPlayers.get(j).setCodesAttempted(newTotalAttempted);
                        int newTotalDeciphered = allPlayers.get(j).getCodesDeciphered() + temp.get(i).getCodesDeciphered();
                        allPlayers.get(j).setCodesDeciphered(newTotalDeciphered);
                        playerUpdated = true; // flag to indicate data has been updated in allPlayers
                        break; // if found, move on to next player
                    }
                }
                if (!playerUpdated) {
                    allPlayers.add(temp.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("\nError reading from file: players.txt");
            e.printStackTrace();
        }
    }
    public static ArrayList<Player> getAllPlayers() {
        readFile();
        return allPlayers;
    }
    public int[] getAllPlayersBulls(){
        return null;
    }
    public int[] getAllPlayersCows(){
        return null;
    }
    public int[] getAllPlayersSecretCodesAttempted(){

        return null;
    }
    public int[] getAllPlayersSecretCodesDeciphered(){

        return null;
    }




}