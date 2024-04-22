import org.w3c.dom.Node;

import java.util.*;


//2. 使用跳跃表实现排行榜
public class SkipListLeaderboard implements Leaderboard {
    private final Node head;
    private final int maxLevel;
    private int level;
    private final Random random;
    private Map<String, Node> playerMap; // 存储玩家ID和节点的映射

    public SkipListLeaderboard(int maxLevel) {
        this.maxLevel = maxLevel;
        this.level = 0;
        this.head = new Node(null, Double.NEGATIVE_INFINITY, maxLevel);
        this.random = new Random();
        this.playerMap = new HashMap<>();
    }

    //Node类应该是SkipListLeaderboard的一个内部类
    //定义跳跃表节点（Node），每个节点存储玩家ID、分数以及多层次的前进指针。
    class Node {
        String playerId;
        double score;
        Node[] forward; // 指向不同层的下一个节点

        public Node(String playerId, double score, int level) {
            this.playerId = playerId;
            this.score = score;
            this.forward = new Node[level + 1]; // 索引从0开始
        }
    }

    //实现Leaderboard接口方法
    @Override
    public void addPlayer(String playerId, double score) {
        Node current = head; // 从头节点开始
        Node[] update = new Node[maxLevel + 1]; // 用于记录每层需要更新的节点

        // 从顶层向下查找并更新update数组
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].score > score) {
                current = current.forward[i]; // 移动到下一个节点
            }
            update[i] = current; // 记录当前层需要更新的节点
        }

        // 生成一个随机层级
        int lvl = randomLevel();
        // 如果随机层级大于当前最大层级，则更新update数组
        if (lvl > level) {
            for (int i = level + 1; i <= lvl; i++) {
                update[i] = head; // 更新update数组为头节点
            }
            level = lvl; // 更新当前最大层级
        }

        // 创建新节点并插入到跳表中
        Node newNode = new Node(playerId, score, lvl);
        for (int i = 0; i <= lvl; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }

        playerMap.put(playerId, newNode); // 添加玩家到哈希表
//        System.out.println("Added player at level " + lvl + ": " + playerId + ", Score: " + score);
//        for (int i = 0; i <= lvl; i++) {
//            System.out.println("Level " + i + " forward to: " + (newNode.forward[i] != null ? newNode.forward[i].playerId : "null"));
//        }


    }

    @Override
    public List<String> getTopPlayers(int k) {
        List<String> topPlayers = new ArrayList<>();
        Node current = head.forward[0];
        while (current != null && topPlayers.size() < k) {
            topPlayers.add(current.playerId);
            current = current.forward[0];
        }
        // 添加null检查来避免NullPointerException
        if (current != null) {
            System.out.println("Traversing node: " + current.playerId + ", Score: " + current.score);
        }
        return topPlayers;
    }


    // 辅助方法，用于生成随机层级
    private int randomLevel() {
        int lvl = 1;
        while (lvl < maxLevel && random.nextDouble() < 0.5) {
            lvl++;
        }
        return lvl;

    }

    @Override
//    public void updateScore(String playerId, double score) {
//        Node nodeToUpdate = playerMap.get(playerId);
//        if (nodeToUpdate != null) {
//            int currentLevel = nodeToUpdate.forward.length - 1; // 获取当前节点的层级
//            removePlayer(playerId); // 从跳跃表和哈希表中移除旧节点
//            addPlayerAtLevel(playerId, score, currentLevel); // 在原层级添加玩家
//        } else {
//            addPlayer(playerId, score); // 玩家不存在，直接添加
//        }
//    }
    public void updateScore(String playerId, double score) {
        removePlayer(playerId); // 先移除玩家节点
        addPlayer(playerId, score); // 再添加更新后的玩家信息
    }

    private void addPlayerAtLevel(String playerId, double score, int level) {
        Node current = head;
        Node[] update = new Node[maxLevel + 1];
        Arrays.fill(update, head); // 初始化update数组，使所有元素都指向头节点

        for (int i = this.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].score < score) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        Node newNode = new Node(playerId, score, level);
        for (int i = 0; i <= level; i++) {
            newNode.forward[i] = update[i].forward[i]; // 此处可能会抛出NullPointerException
            update[i].forward[i] = newNode;
        }

        playerMap.put(playerId, newNode); // 添加玩家到哈希表
        System.out.println("Adding player at level " + level + ": " + playerId + ", Score: " + score);
        for (int i = 0; i <= level; i++) {
            System.out.println("Level " + i + " forward to: " + (newNode.forward[i] != null ? newNode.forward[i].playerId : "null"));
        }
    }

    private Node findNode(String playerId) {
        Node current = head; // 从头节点开始
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].playerId.compareTo(playerId) < 0) {
                current = current.forward[i]; // 在当前层向前移动
            }
        }
        current = current.forward[0]; // 移动到下一层的第一个节点
        // 检查当前节点是否是目标节点
        if (current != null && current.playerId.equals(playerId)) {
            return current; // 找到玩家ID，返回节点
        }
        System.out.println("Searching through node: " + current.playerId + ", Score: " + current.score);

        return null; // 未找到玩家ID，返回null

    }

//    @Override
//    public void removePlayer(String playerId) {
//        Node nodeToRemove = playerMap.get(playerId);
//        if (nodeToRemove != null) {
//            Node current = head;
//            for (int i = level; i >= 0; i--) {
//                while (current.forward[i] != null && current.forward[i] != nodeToRemove) {
//                    current = current.forward[i];
//                }
//                // 这里检查current.forward[i]是否实际指向nodeToRemove
//                if (current.forward[i] == nodeToRemove) {
//                    // 更新前面节点的forward指针，跳过要删除的节点
//                    current.forward[i] = nodeToRemove.forward[i];
//                }
//            }
//            // 在移除节点后更新跳表的层级
//            while (level > 0 && head.forward[level] == null) {
//                level--;
//            }
//            // 从哈希表中移除节点
//            playerMap.remove(playerId);
//        }
//    }
    @Override
    public void removePlayer(String playerId) {
        Node current = head; // 从头节点开始
        Node[] update = new Node[maxLevel + 1]; // 用于记录每层需要更新的节点

        // 从顶层向下查找并更新update数组
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && !current.forward[i].playerId.equals(playerId)) {
                current = current.forward[i]; // 移动到下一个节点
            }
            update[i] = current; // 记录当前层需要更新的节点
        }

        Node nodeToRemove = current.forward[0]; // 获取需要移除的节点
        // 如果找到了需要移除的节点
        if (nodeToRemove != null && nodeToRemove.playerId.equals(playerId)) {
            // 从每层中移除该节点
            for (int i = 0; i <= level; i++) {
                if (update[i].forward[i] != nodeToRemove) {
                    break; // 跳出循环
                }
                update[i].forward[i] = nodeToRemove.forward[i]; // 更新前一个节点的指针
            }
            // 更新当前最大层级
            while (level > 0 && head.forward[level] == null) {
                level--;
            }
        }
    }

    @Override
    public int getPlayerRank(String playerId) {
        Node current = head.forward[0]; // 从第一个有效节点开始
        int rank = 1; // 初始化排名为1

        // 遍历跳表直到末尾或者找到目标玩家
        while (current != null && !current.playerId.equals(playerId)) {
            rank++; // 排名加1
            current = current.forward[0]; // 移动到下一个节点
        }
        // 如果找到了目标玩家，则返回其排名，否则返回-1
        return current == null ? -1 : rank;
    }
    @Override
    public void displayTopPlayers(int k) {
        System.out.println("Rank\tPlayer ID\tScore");
        System.out.println("---------------------------");

        Node current = head.forward[0];
        int rank = 1;

        while (current != null && rank <= k) {
            System.out.println(rank + "\t" + current.playerId + "\t\t" + current.score);
            current = current.forward[0];
            rank++;
        }
    }

}