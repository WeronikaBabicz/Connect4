package engine.gameplay;

import utilities.Player;

public class Point {
    private Player pickedByPlayer;

    public Point(Point other){
        this.pickedByPlayer = other.pickedByPlayer;
    }

    public Point(Player pickedByPlayer) {
        this.pickedByPlayer = pickedByPlayer;
    }

    public Player getPickedByPlayer() {
        return pickedByPlayer;
    }

    void setPickedByPlayer(Player pickedByPlayer) {
        this.pickedByPlayer = pickedByPlayer;
    }

    public boolean isPickedByPlayer(Player player){
        return pickedByPlayer == player;
    }

    public boolean isEmpty(){
        return pickedByPlayer == null;
    }

    @Override
    public String toString() {
        return (pickedByPlayer != null) ? pickedByPlayer.name() : "0";
    }
}
