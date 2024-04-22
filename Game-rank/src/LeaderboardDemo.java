public class LeaderboardDemo {

    public static void main(String[] args) {
        SkipListLeaderboard leaderboard = new SkipListLeaderboard(10); // Max level is 10

        // Add players and their scores
        leaderboard.addPlayer("player1", 100.0);
        leaderboard.addPlayer("player2", 150.0);
        leaderboard.addPlayer("player3", 200.0);
        leaderboard.addPlayer("player4", 130.0);
        leaderboard.addPlayer("player5", 170.0);

        // Display initial leaderboard
        System.out.println("Initial leaderboard:");
        leaderboard.displayTopPlayers(3); // Display top 3 players

        // Update player scores
        leaderboard.updateScore("player2", 250.0); // player2 should now be in the first position
        leaderboard.updateScore("player5", 190.0); // player5 should now be in the third position

        // Display updated leaderboard
        System.out.println("\nUpdated leaderboard:");
        leaderboard.displayTopPlayers(3); // Display top 3 players

        // Get and display the rank of a specific player
        System.out.println("\nRank of player2: " + leaderboard.getPlayerRank("player2"));

        // Remove a player
        leaderboard.removePlayer("player3"); // Remove player3

        // Display leaderboard after removing a player
        System.out.println("\nLeaderboard after removing player3:");
        leaderboard.displayTopPlayers(3); // Display top 3 players
    }
}
