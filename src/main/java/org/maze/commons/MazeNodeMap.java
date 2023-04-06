package org.maze.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MazeNodeMap {

    // 2D map with x and y coordinates of mazenodes as keys. 1 hashmap for each quadrant.
    private MazeNode[][] mazeNodeMap;

    public MazeNodeMap(MazeNode origin) {
        mazeNodeMap = new MazeNode[20][20];

        mazeNodeMap[0][0] = origin;
    }

    public MazeNodeMap(MazeNode origin, int size) {
        mazeNodeMap = new MazeNode[size][size];

        mazeNodeMap[0][0] = origin;
    }

    private int translateCoordinate(int c){
        int returnIndex = 0;

        if(c<0){
            returnIndex = c*-2 -1;
        }
        else{
            returnIndex = c*2;
        }

        return returnIndex;
    }

    private void enlargeMap(){
        int size = mazeNodeMap.length*2;

        MazeNode[][] newMap = new MazeNode[size][size];

        for(int x = 0; x < mazeNodeMap.length; x++){
            for(int y = 0; y < mazeNodeMap[x].length; y++){
                newMap[x][y] = mazeNodeMap[x][y];
            }
        }

        mazeNodeMap = newMap;
    }

    public MazeNode getMazeNodeAt(int x, int y) {

        x = translateCoordinate(x);
        y = translateCoordinate(y);

        if(x >= mazeNodeMap.length || y >= mazeNodeMap[x].length){
            enlargeMap();
        }

        return mazeNodeMap[x][y];
    }

    public boolean addMazeNode(MazeNode node){
        if(getMazeNodeAt(node.getX(), node.getY()) == null){

            int x = translateCoordinate(node.getX());
            int y = translateCoordinate(node.getY());
            mazeNodeMap[x][y] = node;
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

    public void moveMazeNodeTo(MazeNode node, int x, int y) {
        if(getMazeNodeAt(node.getX(), node.getY()) != null){
            int oldX = node.getX();
            int oldY = node.getY();
            node.setX(x);
            node.setY(y);

            if(addMazeNode(node)){
                removeMazeNodeAt(oldX, oldY);
            }
            else{
                node.setX(oldX);
                node.setY(oldY);
            }


        }
    }

    private boolean removeMazeNodeAt(int oldX, int oldY) {
        int x = translateCoordinate(oldX);
        int y = translateCoordinate(oldY);

        if(mazeNodeMap[x][y] != null){
            mazeNodeMap[x][y] = null;
            return true;
        }
        return false;
    }
}
