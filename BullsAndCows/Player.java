package BullsAndCows;

public class Player {
    public String username;
    private int numberOfBulls = 0;
    private int numberOfCows = 0;
    private int codesAttempted;
    private int codesDeciphered;

    public void setUsername(String newUsername){
        username = newUsername;
    }
    public void updateBulls(int guess){
        numberOfBulls += guess;
    }
    public void updateCows(int guess){
        numberOfCows += guess;
    }
    public void incrementCodesAttempted(){
        codesAttempted++;
    }
    public void decrementCodesAttempted(){
        codesAttempted--;
    } // When input == command
    public void incrementCodesDeciphered(){
        codesDeciphered++;
    }
    public int getBulls(){
        return numberOfBulls;
    }
    public void setBulls(int bulls){
        numberOfBulls = bulls;
    }
    public int getCows(){
        return numberOfCows;
    }
    public void setCows(int cows){
        numberOfCows = cows;
    }
    public int getCodesAttempted(){
        return codesAttempted;
    }
    public void setCodesAttempted(int codes){
        codesAttempted = codes;
    }
    public int getCodesDeciphered(){
        return codesDeciphered;
    }
    public void setCodesDeciphered(int codes){
        codesDeciphered = codes;
    }
}