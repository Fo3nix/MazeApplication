package org.maze.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MazeNodeMap {

    // 2D hashmaps with x and y coordinates of mazenodes as keys. 1 hashmap for each quadrant.
    private final HashMap<Integer, HashMap<Integer, MazeNode>> mazeNodeMap;

    public MazeNodeMap(MazeNode origin) {
        mazeNodeMap = new HashMap<>();

        // Add origin to all quadrants
        mazeNodeMap.put(origin.getX(), new HashMap<>());
        mazeNodeMap.get(origin.getX()).put(origin.getY(), origin);
    }

    public MazeNode getMazeNodeAt(int x, int y) {

        MazeNode returnNode = null;

        if (mazeNodeMap.containsKey(x)) {
            if (mazeNodeMap.get(x).containsKey(y)) {
                returnNode = mazeNodeMap.get(x).get(y);
            }
        }

        return returnNode;
    }

    public boolean addMazeNode(MazeNode node){
        if(getMazeNodeAt(node.getX(), node.getY()) == null){

            mazeNodeMap.computeIfAbsent(node.getX(), k -> new HashMap<>());
            mazeNodeMap.get(node.getX()).put(node.getY(), node);
            return true;
        }
        return false;
    }

    public List<MazeNode> getMazeNodesLeftOf(MazeNode node) {
        List<MazeNode> returnList = new ArrayList<>();

        while(node.hasLeft()){
            node = node.getLeft();
            returnList.add(node);
        }

        return returnList;
    }

    public List<MazeNode> getMazeNodesRightOf(MazeNode node) {
        List<MazeNode> returnList = new ArrayList<>();

        while(node.hasRight()){
            node = node.getRight();
            returnList.add(node);
        }

        return returnList;
    }

    public List<MazeNode> getMazeNodesAbove(MazeNode node) {
        List<MazeNode> returnList = new ArrayList<>();

        while(node.hasUp()){
            node = node.getUp();
            returnList.add(node);
        }

        return returnList;
    }

    public List<MazeNode> getMazeNodesBelow(MazeNode node) {
        List<MazeNode> returnList = new ArrayList<>();

        while(node.hasDown()){
            node = node.getDown();
            returnList.add(node);
        }

        return returnList;
    }

    public void moveMazeNodeTo(MazeNode node, int i, int y) {
        Boolean nodeExists = false;
        if(mazeNodeMap.containsKey(node.getX())){
            if(mazeNodeMap.get(node.getX()).containsKey(node.getY())){
                if(mazeNodeMap.get(node.getX()).get(node.getY()) == node){
                    nodeExists = true;
                }
            }
        }

        if(getMazeNodeAt(i, y) == null && nodeExists){
            mazeNodeMap.get(node.getX()).remove(node.getY());
            node.setX(i);
            node.setY(y);
            mazeNodeMap.get(node.getX()).put(node.getY(), node);
        }
    }
}
