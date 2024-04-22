import java.util.List;

//1. 定义排行榜接口
public interface Leaderboard {
    void addPlayer(String playerId, double score);

    void removePlayer(String playerId);

    void updateScore(String playerId, double score);

    List<String> getTopPlayers(int k);

    int getPlayerRank(String playerId);

    void displayTopPlayers(int k);
}
