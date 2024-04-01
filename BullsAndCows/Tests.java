package BullsAndCows;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Tests{

    @Test
    public void test(){
        assertTrue(true);
    }

    @Test
    public void wordCows(){
        SecretCode secretCode = new LettersCode(4);
        Player player = new Player();
        secretCode.code = "test";

        assertEquals(0, Game.calculateCows(secretCode, "test"));
        assertEquals(2, Game.calculateCows(secretCode, "tets"));
        assertEquals(4, Game.calculateCows(secretCode, "etts"));
        assertEquals(0, Game.calculateCows(secretCode, "aaaa"));

        secretCode.code = "aaaa";
        assertEquals(0, Game.calculateCows(secretCode, "aaaa"));

        assertEquals(0, Game.calculateCows(secretCode, "abaa"));
    }

    @Test
    public void wordBulls(){
        SecretCode secretCode = new LettersCode(4);

        secretCode.code = "test";

        assertEquals(4, Game.calculateBulls(secretCode, "test"));

        assertEquals(2, Game.calculateBulls(secretCode, "tets"));

        assertEquals(0, Game.calculateBulls(secretCode, "etts"));

        assertEquals(0, Game.calculateBulls(secretCode, "aaaa"));
    }

    @Test
    public void numbersCows(){
        SecretCode secretCode = new NumbersCode(4);
        Player player = new Player();
        secretCode.code = "1234";

        assertEquals(0, Game.calculateCows(secretCode, "1234"));
        assertEquals(2, Game.calculateCows(secretCode, "1243"));
        assertEquals(4, Game.calculateCows(secretCode, "4321"));
        assertEquals(0, Game.calculateCows(secretCode, "5555"));

        secretCode.code = "1111";
        assertEquals(0, Game.calculateCows(secretCode, "1111"));
        assertEquals(0, Game.calculateCows(secretCode, "1211"));
    }

    @Test
    public void numbersBulls(){
        SecretCode secretCode = new NumbersCode(4);

        secretCode.code = "1234";

        assertEquals(4, Game.calculateBulls(secretCode, "1234"));
        assertEquals(2, Game.calculateBulls(secretCode, "1243"));
        assertEquals(0, Game.calculateBulls(secretCode, "4321"));
        assertEquals(0, Game.calculateBulls(secretCode, "5555"));

        secretCode.code = "1111";
        assertEquals(4, Game.calculateBulls(secretCode, "1111"));
        assertEquals(3, Game.calculateBulls(secretCode, "1211"));
    }

    @Test
    public void numberRequestCode(){
        Player player = new Player();
        Game game = new Game(player, false);
        game.requestCode();
        int number;
        try {
            number = Integer.parseInt(game.secretCode.getCode());
        }
        catch (NumberFormatException e) {
            assertTrue(false);
            number = 0;
        }
    }

    @Test
    public void lettersRequestCode(){
        Player player = new Player();
        Game game = new Game(player, true);
        game.requestCode();
        int number;
        try {
            number = Integer.parseInt(game.secretCode.getCode());
        }
        catch (NumberFormatException e) {
            assertTrue(true);
        }
    }

    @Test
    public void secretCodesInstantiation(){
        try {
            SecretCode lettersCode = new LettersCode(4);
            SecretCode numbersCode = new NumbersCode(4);
        }catch(Exception e){
            assertTrue(false); //fail test if instantiation doesn't work
        }
    }

    @Test
    public void testDecipheredCodesTracking() {
        Player player = new Player();
        player.incrementCodesDeciphered();
        assertEquals(1, player.getCodesDeciphered());
    }

    @Test
    public void testAttemptedCodesTracking() {
        Player player = new Player();
        player.incrementCodesAttempted();
        assertEquals(1, player.getCodesAttempted());
    }

    @Test
    public void testBullsAndCowsTracking() {
        Player player = new Player();
        player.updateBulls(2);
        player.updateCows(1);
        assertEquals(2, player.getBulls());
        assertEquals(1, player.getCows());
    }

    @Test
    public void testSaveGame() throws IOException {
        Player player = new Player();
        Game game = new Game(player, true);
        game.playGame();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.saveGame();
        assertEquals("Game has been saved.", outContent.toString().trim());

        File saveFile = new File("BullsAndCows/assets/saves/" + player.username + "_save.txt");
        assertTrue(saveFile.exists());
        saveFile.delete();
    }

    @Test
    public void testStorePlayerName() {
        Player player = new Player();
        Game game = new Game(player, true);

        game.currentPlayer.username = "tester";

        assertEquals("tester", game.currentPlayer.username);
    }

    @Test
    public void testTrackStatistics() {
        Player player = new Player();
        Game game = new Game(player, true);

        game.currentPlayer.incrementCodesDeciphered();
        game.currentPlayer.incrementCodesAttempted();
        game.currentPlayer.updateBulls(3);
        game.currentPlayer.updateCows(1);

        assertEquals(1, game.currentPlayer.getCodesDeciphered());
        assertEquals(1, game.currentPlayer.getCodesAttempted());
        assertEquals(3, game.currentPlayer.getBulls());
        assertEquals(1, game.currentPlayer.getCows());
    }

    @Test
    public void testLoadPlayerDetails() throws Exception {
        Player player = new Player();
        Game game = new Game(player, true);

        Players.findPlayer(player);

        assertEquals("tester", game.currentPlayer.username);
        assertEquals(0, game.currentPlayer.getBulls());
        assertEquals(0, game.currentPlayer.getCows());
        assertEquals(0, game.currentPlayer.getCodesAttempted());
        assertEquals(0, game.currentPlayer.getCodesDeciphered());
    }

    @Test
    public void testGetHint() {
        Player player = new Player();
        Game game = new Game(player, true);
        game.requestCode();
        game.getHint();
        game.getHint();

        assertEquals(2, game.hints);
    }

    @Test
    public void testShowSolution() {
        Player player = new Player();
        Game game = new Game(player, true);
        game.requestCode();

        assertEquals(game.secretCode.getCode(), game.showSolution());
    }

    @Test
    public void testLeaderboard() {
        Player player = new Player();
        Game game = new Game(player, true);
        game.leaderboards();
    }

    @Test
    public void testFourDigitCode(){
        SecretCode secretCode = new LettersCode(4);

        secretCode.code = "test";

        assertEquals(4, Game.calculateBulls(secretCode, "test"));

        assertEquals(2, Game.calculateBulls(secretCode, "tets"));

        assertEquals(0, Game.calculateBulls(secretCode, "etts"));

        assertEquals(0, Game.calculateBulls(secretCode, "aaaa"));
    }

    @Test
    public void testEightDigitCode() {
            SecretCode secretCode = new LettersCode(8);

            secretCode.code = "triangle";

            assertEquals(8, Game.calculateBulls(secretCode, "triangle"));

            assertEquals(2, Game.calculateBulls(secretCode, "txrixaxe"));

            assertEquals(0, Game.calculateBulls(secretCode, "usjbohmf"));

            assertEquals(0, Game.calculateBulls(secretCode, "xxxxxxxx"));
    }
}