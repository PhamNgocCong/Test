package entities;

import utils.ImageLoader;
import world.GameMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

public class Bomber {
    private int x, y;
    private int speed = 8; // Tốc độ di chuyển
    private BufferedImage bomberImage;

    public Bomber(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        bomberImage = ImageLoader.load("images/player.png");
    }

    // Nhận thêm danh sách activeBombs vào hàm move để kiểm tra va chạm bom
    public void move(int dx, int dy, GameMap map, List<Bomb> activeBombs) {
        if (dx != 0) {
            int newX = x + dx * speed;
            if (canMove(newX, y, map, activeBombs)) x = newX;
        }
        if (dy != 0) {
            int newY = y + dy * speed;
            if (canMove(x, newY, map, activeBombs)) y = newY;
        }
    }

    private boolean canMove(int targetX, int targetY, GameMap map, List<Bomb> activeBombs) {
        int tileSize = GameMap.TILE_SIZE;
        int margin = 4; // Margin lướt tường mượt mà

        int leftTile = (targetX + margin) / tileSize;
        int rightTile = (targetX + tileSize - margin - 1) / tileSize;
        int topTile = (targetY + margin) / tileSize;
        int bottomTile = (targetY + tileSize - margin - 1) / tileSize;

        // 1. Kiểm tra va chạm bản đồ (tường, gạch)
        boolean walkable = map.isWalkable(leftTile, topTile) &&
                map.isWalkable(rightTile, topTile) &&
                map.isWalkable(leftTile, bottomTile) &&
                map.isWalkable(rightTile, bottomTile);

        if (!walkable) {
            return false;
        }

        // 2. Kiểm tra va chạm với danh sách quả bom đang đặt trên bản đồ
        if (activeBombs != null) {
            int targetCenterCol = (targetX + tileSize / 2) / tileSize;
            int targetCenterRow = (targetY + tileSize / 2) / tileSize;

            int currentCenterCol = (this.x + tileSize / 2) / tileSize;
            int currentCenterRow = (this.y + tileSize / 2) / tileSize;

            for (Bomb bomb : activeBombs) {
                // Nếu ô chuẩn bị bước tới trùng với ô chứa bom
                if (bomb.getCol() == targetCenterCol && bomb.getRow() == targetCenterRow) {
                    // Nếu Bomber KHÔNG đang đứng ở ô bom đó (nghĩa là đang cố đi từ ngoài vào lại) -> Chặn lại!
                    if (currentCenterCol != bomb.getCol() || currentCenterRow != bomb.getRow()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void render(Graphics2D g) {
        if (bomberImage != null) {
            g.drawImage(bomberImage, x, y, GameMap.TILE_SIZE, GameMap.TILE_SIZE, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x + 4, y + 4, GameMap.TILE_SIZE - 8, GameMap.TILE_SIZE - 8);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
