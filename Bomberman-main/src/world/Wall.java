package world;

public class Wall extends Tile {
    public Wall() {
        super("images/wall.png");
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public boolean blocksFlame() {
        return true;
    }
}
