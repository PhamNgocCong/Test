package entities;

import utils.ImageLoader;
import world.GameMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb {
    private int col, row; // Tọa độ ô lưới (Tile)
    private int x, y;     // Tọa độ pixel
    private int timer = 180; // Thời gian đếm ngược trước khi nổ (180 frame ~ 3 giây ở 60 FPS)
    private boolean exploded = false;
    private int radius = 1;  // Tầm nổ của bom (số ô lan ra các hướng)
    private BufferedImage bombImage;

    public Bomb(int col, int row) {
        this.col = col;
        this.row = row;
        this.x = col * GameMap.TILE_SIZE;
        this.y = row * GameMap.TILE_SIZE;
        this.bombImage = ImageLoader.load("images/bomb.png");
    }

    public void update(GameMap map) {
        timer--;
        if (timer <= 0) {
            triggerExplosion(map);
            exploded = true;
        }
    }

    private void triggerExplosion(GameMap map) {
        // 1. Phá hủy ô tại tâm bom
        map.destroyBrick(col, row);

        // 2. Lan 4 hướng: Lên, Xuống, Trái, Phải
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };

        for (int[] dir : directions) {
            for (int i = 1; i <= radius; i++) {
                int targetCol = col + dir[0] * i;
                int targetRow = row + dir[1] * i;

                // Nếu gặp vật cản chặn lửa (như tường cố định Wall) thì dừng hướng đó lại
                if (map.blocksFlame(targetCol, targetRow)) {
                    // Nếu là gạch Brick thì phá hủy nó trước khi dừng tia lửa
                    map.destroyBrick(targetCol, targetRow);
                    break;
                }

                // Phá hủy gạch trên đường đi của lửa
                map.destroyBrick(targetCol, targetRow);
            }
        }
    }

    public void render(Graphics2D g) {
        if (bombImage != null) {
            g.drawImage(bombImage, x, y, GameMap.TILE_SIZE, GameMap.TILE_SIZE, null);
        } else {
            // Hình vẽ dự phòng nếu chưa có ảnh bomb.png (hình tròn đen/đỏ nhấp nháy)
            g.setColor(Color.DARK_GRAY);
            g.fillOval(x + 6, y + 6, GameMap.TILE_SIZE - 12, GameMap.TILE_SIZE - 12);
            g.setColor(Color.RED);
            g.fillOval(x + 14, y + 14, GameMap.TILE_SIZE - 28, GameMap.TILE_SIZE - 28);
        }
    }
    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    public boolean isExploded() {
        return exploded;
    }
}
