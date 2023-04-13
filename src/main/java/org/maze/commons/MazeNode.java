package org.maze.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MazeNode {
    private int x;
    private int y;
    private boolean isWall;
    private MazeNode up;
    private MazeNode down;
    private MazeNode left;
    private MazeNode right;

    public MazeNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.isWall = true;
    }

    public MazeNode(int x, int y, MazeNode up, MazeNode down, MazeNode left, MazeNode right) {
        this.x = x;
        this.y = y;
        this.isWall = true;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public MazeNode getUp() {
        return up;
    }

    public void setUp(MazeNode up) {
        this.up = up;
    }

    public MazeNode getDown() {
        return down;
    }

    public void setDown(MazeNode down) {
        this.down = down;
    }

    public MazeNode getLeft() {
        return left;
    }

    public void setLeft(MazeNode left) {
        this.left = left;
    }

    public MazeNode getRight() {
        return right;
    }

    public void setRight(MazeNode right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeNode mazeNode = (MazeNode) o;
        return x == mazeNode.x && y == mazeNode.y && isWall == mazeNode.isWall && Objects.equals(up, mazeNode.up) && Objects.equals(down, mazeNode.down) && Objects.equals(left, mazeNode.left) && Objects.equals(right, mazeNode.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, isWall, up, down, left, right);
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public boolean hasUp() {
        return up != null;
    }

    public boolean hasDown() {
        return down != null;
    }

    public boolean isEdgeNode() {
        return up==null || down==null || left==null || right==null;
    }

    public List<MazeNode> getAdjacentNodes() {
        List<MazeNode> returnList = new ArrayList<>();

        if(hasLeft()){
            returnList.add(left);
        }
        if(hasRight()){
            returnList.add(right);
        }
        if(hasUp()){
            returnList.add(up);
        }
        if(hasDown()){
            returnList.add(down);
        }

        return returnList;
    }

    public MazeNode getNodeInDirection(String direction) {
        return switch (direction) {
            case "up" -> up;
            case "down" -> down;
            case "left" -> left;
            case "right" -> right;
            default -> null;
        };
    }

    public List<MazeNode> getSurroundingNodes() {
        List<MazeNode> returnList = new ArrayList<>();

        returnList.addAll(this.getAdjacentNodes());
        if(right.hasUp()){
            returnList.add(right.getUp());
        }
        if(right.hasDown()){
            returnList.add(right.getDown());
        }
        if(left.hasUp()){
            returnList.add(left.getUp());
        }
        if(left.hasDown()){
            returnList.add(left.getDown());
        }

        return returnList;
    }

    public Collection<MazeNode> getAdjacentPaths() {
        List<MazeNode> returnList = this.getAdjacentNodes();
        returnList.removeIf(MazeNode::isWall);
        return returnList;
    }
}

