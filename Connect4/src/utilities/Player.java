package utilities;

public enum Player {
    FIRST_PLAYER,
    SECOND_PLAYER;

    @Override
    public String toString() {
        switch(this) {
            case FIRST_PLAYER: return "First player";
            case SECOND_PLAYER: return "Second player";
        }
        return "";
    }

    public String getColor(){
        switch(this) {
            case FIRST_PLAYER: return "Yellow";
            case SECOND_PLAYER: return "Red";
        }
        return "";
    }
}
