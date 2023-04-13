package org.maze.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Maze {

    private MazeNode entrance;
    private MazeNode exit;
    private final MazeNode origin;
    private final List<MazeNode> mazeNodeList;
    private final MazeNodeMap mazeNodeMap;

    public Maze() {
        this.origin = new MazeNode(0,0);
        mazeNodeList = new ArrayList<>();
        mazeNodeList.add(origin);
        mazeNodeMap = new MazeNodeMap(origin);
    }

    public Maze(int size) {
        this.origin = new MazeNode(0,0);
        mazeNodeList = new ArrayList<>();
        mazeNodeList.add(origin);
        mazeNodeMap = new MazeNodeMap(origin, size);
    }

    public MazeNode getOrigin() {
        return origin;
    }

    public MazeNode getEntrance() {
        return entrance;
    }

    public void setEntrance(MazeNode entrance) {
        this.entrance = entrance;
    }

    public MazeNode getExit() {
        return exit;
    }

    public void setExit(MazeNode exit) {
        this.exit = exit;
    }

    public List<MazeNode> getMazeNodeList() {
        return mazeNodeList;
    }

    private void generateEntranceAndExit(){
        List<MazeNode> edgeNodes = this.getEdgeNodes();
        int randomIndexForEntrance = (int) (Math.random()*edgeNodes.size());
        int randomIndexForExit = (int) (Math.random()*edgeNodes.size());

        Collections.shuffle(edgeNodes);

        this.setEntrance(edgeNodes.remove(randomIndexForEntrance));
        this.setExit(edgeNodes.get(randomIndexForExit));
        edgeNodes.add(this.getEntrance());

        int exitX = this.getExit().getX();
        int exitY = this.getExit().getY();
        int entranceX = this.getEntrance().getX();
        int entranceY = this.getEntrance().getY();
        int distanceX = Math.abs(exitX - entranceX);
        int distanceY = Math.abs(exitY - entranceY);

        if(distanceX <= 2 || distanceY <= 2){
            generateEntranceAndExit();
        }
    }

    public MazeNode getMazeNodeAt(int x, int y){
        return mazeNodeMap.getMazeNodeAt(x, y);
    }

    public int getMinY(){
        return mazeNodeList.stream().mapToInt(MazeNode::getY).min().orElse(0);
    }

    public int getMaxY(){
        return mazeNodeList.stream().mapToInt(MazeNode::getY).max().orElse(0);
    }

    public int getMinX(){
        return mazeNodeList.stream().mapToInt(MazeNode::getX).min().orElse(0);
    }

    public int getMaxX(){
        return mazeNodeList.stream().mapToInt(MazeNode::getX).max().orElse(0);
    }

    public MazeNode getMinXAtY(int y){
        return mazeNodeList.stream().filter(mazeNode -> mazeNode.getY() == y).min((mazeNode1, mazeNode2) -> mazeNode1.getX() - mazeNode2.getX()).orElse(null);
    }

    public MazeNode getMaxXAtY(int y){
        return mazeNodeList.stream().filter(mazeNode -> mazeNode.getY() == y).max((mazeNode1, mazeNode2) -> mazeNode1.getX() - mazeNode2.getX()).orElse(null);
    }

    //TODO: remove duplicate codes
    public MazeNode createNewMazeNodeToRightOf(MazeNode node) {
        MazeNode newNode = new MazeNode(node.getX() + 1, node.getY());
        if(mazeNodeMap.addMazeNode(newNode)){
            setAdjacentNodes(newNode);
            mazeNodeList.add(newNode);
            return newNode;
        }

        return null;
    }

    public MazeNode createNewMazeNodeToLeftOf(MazeNode node) {
        MazeNode newNode = new MazeNode(node.getX() - 1, node.getY());
        if(mazeNodeMap.addMazeNode(newNode)){
            setAdjacentNodes(newNode);
            mazeNodeList.add(newNode);
            return newNode;
        }

        return null;
    }

    public MazeNode createNewMazeNodeAbove(MazeNode node) {
        MazeNode newNode = new MazeNode(node.getX(), node.getY() + 1);
        if(mazeNodeMap.addMazeNode(newNode)){
            setAdjacentNodes(newNode);
            mazeNodeList.add(newNode);
            return newNode;
        }

        return null;
    }

    public MazeNode createNewMazeNodeBelow(MazeNode node) {
        MazeNode newNode = new MazeNode(node.getX(), node.getY() - 1);

        if(mazeNodeMap.addMazeNode(newNode)){
            setAdjacentNodes(newNode);
            mazeNodeList.add(newNode);
            return newNode;
        }

        return null;
    }

    public MazeNode addMazeNodeBetween(MazeNode node1, MazeNode node2){
        if(node1==node2){
            return null;
        }

        if(Math.abs(node1.getX() - node2.getX()) > 1 || Math.abs(node1.getY() - node2.getY()) > 1){
            return null;
        }

        // TODO: Implement diagonal adding
        if(node1.getX()!=node2.getX() && node1.getY()!=node2.getY()){
            return null;
        }

        if(node1.getY() >= 0 && node2.getY() >= 0 && node1.getY() < node2.getY()){
            MazeNode temp = node1;
            node1 = node2;
            node2 = temp;
        }

        if(node1.getY() <= 0 && node2.getY() <= 0 && node1.getY() > node2.getY()){
            MazeNode temp = node1;
            node1 = node2;
            node2 = temp;
        }

        if(node1.getX() <=0 && node2.getX() <= 0 && node1.getX() > node2.getX()){
            MazeNode temp = node1;
            node1 = node2;
            node2 = temp;
        }

        if(node1.getX() >= 0 && node2.getX() >= 0 && node1.getX() < node2.getX()){
            MazeNode temp = node1;
            node1 = node2;
            node2 = temp;
        }

        boolean succeeded = false;
        MazeNode newNode = new MazeNode(node1.getX(), node1.getY());

        // node1 is left of node2
        // Create node between node1 and node2 and decrement x values of all nodes left of node 1, including node 1
        if(node1.getX() < node2.getX()){

            List<MazeNode> nodesToDecrement = mazeNodeMap.getMazeNodesLeftOf(node1);
            Collections.reverse(nodesToDecrement);
            nodesToDecrement.add(node1);

            // Move all nodes left of node1 and including node1 to the left by 1
            for(MazeNode node : nodesToDecrement){
                mazeNodeMap.moveMazeNodeTo(node, node.getX() - 1, node.getY());
            }

            // Set the adjacent nodes of the moved nodes
            for(MazeNode node : nodesToDecrement){
                setAdjacentNodes(node);
            }

            // Set the adjacent nodes of the new node
            succeeded = true;
        }

        // node1 is right of node2
        // Create node between node1 and node2 and increment x values of all nodes right of node 1, including node 1
        if(node1.getX() > node2.getX()){

            List<MazeNode> nodesToIncrement = mazeNodeMap.getMazeNodesRightOf(node1);
            Collections.reverse(nodesToIncrement);
            nodesToIncrement.add(node1);

            // Move all nodes right of node1 and including node1 to the right by 1
            for(MazeNode node : nodesToIncrement){
                mazeNodeMap.moveMazeNodeTo(node, node.getX() + 1, node.getY());
            }

            // Set the adjacent nodes of the moved nodes
            for(MazeNode node : nodesToIncrement){
                setAdjacentNodes(node);
            }

            succeeded = true;
        }

        // node1 is above node2
        // Create node between node1 and node2 and increment y values of all nodes above node 1, including node 1
        if(node1.getY() > node2.getY()){

            List<MazeNode> nodesToIncrement = mazeNodeMap.getMazeNodesAbove(node1);
            Collections.reverse(nodesToIncrement);
            nodesToIncrement.add(node1);

            // Move all nodes above node1 and including node1 to the above by 1
            for(MazeNode node : nodesToIncrement){
                mazeNodeMap.moveMazeNodeTo(node, node.getX(), node.getY() + 1);
            }

            // Set the adjacent nodes of the moved nodes
            for(MazeNode node : nodesToIncrement){
                setAdjacentNodes(node);
            }

            succeeded = true;
        }

        // node1 is below node2
        // Create node between node1 and node2 and decrement y values of all nodes below node 1, including node 1
        if(node1.getY() < node2.getY()){

            List<MazeNode> nodesToDecrement = mazeNodeMap.getMazeNodesBelow(node1);
            Collections.reverse(nodesToDecrement);
            nodesToDecrement.add(node1);

            // Move all nodes below node1 and including node1 to the below by 1
            for(MazeNode node : nodesToDecrement){
                mazeNodeMap.moveMazeNodeTo(node, node.getX(), node.getY() - 1);
            }

            // Set the adjacent nodes of the moved nodes
            for(MazeNode node : nodesToDecrement){
                setAdjacentNodes(node);
            }

            succeeded = true;
        }

        if(succeeded){
            mazeNodeMap.addMazeNode(newNode);

            // Set the adjacent nodes of the new node
            setAdjacentNodes(newNode);

            // Add the new node to the list of nodes
            mazeNodeList.add(newNode);

            return newNode;
        }

        return null;

    }

    private void setAdjacentNodes(MazeNode node){
        MazeNode up = mazeNodeMap.getMazeNodeAt(node.getX(), node.getY() + 1);
        MazeNode down = mazeNodeMap.getMazeNodeAt(node.getX(), node.getY() - 1);
        MazeNode left = mazeNodeMap.getMazeNodeAt(node.getX() - 1, node.getY());
        MazeNode right = mazeNodeMap.getMazeNodeAt(node.getX() + 1, node.getY());

        node.setUp(up);
        if(up!=null){
            up.setDown(node);
        }


        node.setDown(down);
        if(down!=null){
            down.setUp(node);
        }

        node.setLeft(left);
        if(left!=null){
            left.setRight(node);
        }

        node.setRight(right);
        if(right!=null){
            right.setLeft(node);
        }
    }

    public static Maze generateSquareMaze(int distanceFromOrigin){
        return generateRectangularMaze(distanceFromOrigin, distanceFromOrigin, distanceFromOrigin, distanceFromOrigin);
    }

    public static Maze generateRectangularMaze(int distanceFromOriginLeft, int distanceFromOriginRight, int distanceFromOriginUp, int distanceFromOriginDown){

        //size is maximum distance from origin in any direction
        int size = Math.max(distanceFromOriginLeft, distanceFromOriginRight);
        size = Math.max(size, distanceFromOriginUp);
        size = Math.max(size, distanceFromOriginDown);

        Maze maze = new Maze(size*2+1);
        MazeNode horizontalNode = maze.getOrigin();
        MazeNode verticalNode = maze.getOrigin();

        // Fill left
        for (int i = 0; i > -distanceFromOriginLeft; i--) {
            horizontalNode = maze.createNewMazeNodeToLeftOf(horizontalNode);

            fillUp(maze, horizontalNode, distanceFromOriginUp);

            fillDown(maze, horizontalNode, distanceFromOriginDown);
        }

        // Reset horizontalNode
        horizontalNode = maze.getOrigin();

        // Fill Right
        for (int i = 0; i < distanceFromOriginLeft; i++) {
            horizontalNode = maze.createNewMazeNodeToRightOf(horizontalNode);

            fillUp(maze, horizontalNode, distanceFromOriginUp);

            fillDown(maze, horizontalNode, distanceFromOriginDown);
        }

        return maze;
    }

    public static Maze generateCircularMaze(int distanceFromOrigin){
        Maze maze = new Maze(distanceFromOrigin*2+1);
        MazeNode horizontalNode = maze.getOrigin();

        // Fill up from origin
        fillUp(maze, horizontalNode, distanceFromOrigin);

        // Fill down from origin
        fillDown(maze, horizontalNode, distanceFromOrigin);

        // Fill left
        for (int i = -1; i >= -distanceFromOrigin; i--) {
            horizontalNode = maze.createNewMazeNodeToLeftOf(horizontalNode);

            // calculate fillDistance
            int fillDistance = (int) Math.sqrt(Math.pow(distanceFromOrigin, 2) - Math.pow(i, 2));

            fillUp(maze, horizontalNode, fillDistance);

            fillDown(maze, horizontalNode, fillDistance);
        }

        // Reset horizontalNode
        horizontalNode = maze.getOrigin();

        // Fill Right
        for (int i = 1; i <= distanceFromOrigin; i++) {

            horizontalNode = maze.createNewMazeNodeToRightOf(horizontalNode);

            // calculate fillDistance
            int fillDistance = (int) Math.sqrt(Math.pow(distanceFromOrigin, 2) - Math.pow(i, 2));

            fillUp(maze, horizontalNode, fillDistance);

            fillDown(maze, horizontalNode, fillDistance);
        }

        return maze;
    }

    private static void fillUp(Maze maze, MazeNode node, int distance){
        MazeNode verticalNode = node;
        // Fill up
        for (int j = 0; j < distance; j++) {
            verticalNode = maze.createNewMazeNodeAbove(verticalNode);
        }
    }

    private static void fillDown(Maze maze, MazeNode node, int distance){
        MazeNode verticalNode = node;
        // Fill down
        for (int k = 0; k > -distance; k--) {
            verticalNode = maze.createNewMazeNodeBelow(verticalNode);
        }
    }

    public static void generateMazePaths(Maze maze){
        maze.generateEntranceAndExit();;

        MazeNode previousNode = null;
        MazeNode currentNode = maze.getEntrance();
        currentNode.setWall(false);

        int pathMaxLength = (int) (maze.getAverageRowSize() / 5);
        List<String> directions = List.of("up", "down", "left", "right");

        // TODO: generate paths using algorithm
        List<MazeNode> availableNodes = new ArrayList<MazeNode>();
        availableNodes.add(currentNode);

        while(availableNodes.size()!=0){

            int pathLength = (int) (Math.random() * pathMaxLength);
            String direction = directions.get((int) (Math.random() * (directions.size()-1)));

            for(int i = 0; i < pathLength; i++){
                previousNode = currentNode;
                currentNode = currentNode.getNodeInDirection(direction);
                availableNodes.add(currentNode);

                if(pathingPossible(currentNode, previousNode)){
                    currentNode.setWall(false);

                }
                else{
                    break;
                }
            }

            if(!pathingPossible(currentNode, null)){
                availableNodes.remove(currentNode);
            }

            int randomIndex = (int) (Math.random() * (availableNodes.size()-1));
            currentNode = availableNodes.get(randomIndex);

        }


        maze.getExit().setWall(false);

    }

    private static boolean pathingPossible(MazeNode currentNode, MazeNode previousNode) {
        if(currentNode == null){
            return false;
        }

        List<MazeNode> surroundingNodes = currentNode.getSurroundingNodes();
        surroundingNodes.remove(previousNode);
        surroundingNodes.removeAll(previousNode.getAdjacentPaths());

        if(surroundingNodes.size()>8 || surroundingNodes.size()<6){
            return false;
        }

        for (MazeNode node : surroundingNodes) {
            if(!node.isWall()){
                return false;
            }
        }

        return true;
    }

    private int getAverageRowSize() {
        int total = 0;
        for (int y = getMaxY(); y >= getMinY(); y--) {
            total += getRowSize(y);
        }

        return total / (getMaxY() - getMinY());
    }

    private int getRowSize(int y) {
        int size = 0;
        for (int x = getMinX(); x <= getMaxX(); x++) {
            if(mazeNodeMap.getMazeNodeAt(x, y) != null){
                size++;
            }
        }

        return size;
    }

    private List<MazeNode> getEdgeNodes() {
        List<MazeNode> edgeNodes = new ArrayList<MazeNode>();

        for (MazeNode node : mazeNodeList) {
            if(node.isEdgeNode()){
                edgeNodes.add(node);
            }
        }

        return edgeNodes;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        for (int y = getMaxY(); y >= getMinY(); y--) {
            for (int x = getMinX(); x <= getMaxX(); x++) {
                MazeNode node = mazeNodeMap.getMazeNodeAt(x, y);
                if (node == null) {
                    sb.append("   ");
                } else {
                    sb.append(" X ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}

