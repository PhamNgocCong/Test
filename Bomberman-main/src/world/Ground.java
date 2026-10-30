package world;


public class Ground extends Tile {
    public Ground() {
        super("images/floor.png");
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean blocksFlame() {
        return false;
    }
}
