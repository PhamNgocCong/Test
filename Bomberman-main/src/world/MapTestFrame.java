package world;

import entities.Bomber;
import entities.Enemy;
import entities.Bomb;
import utils.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTestFrame extends JFrame {

    private GameMap gameMap;
    private Bomber bomber;
    private List<Enemy> enemies;
    private List<Bomb> activeBombs; // Danh sách các quả bom đang đặt trên map
    private InputHandler inputHandler;
    private Timer gameLoopTimer;
    private boolean spaceReleased = true; // Chống lặp phím liên tục khi giữ Space

    public MapTestFrame() {
        setTitle("Bomberman - Test Map, Character & Enemies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gameMap = new GameMap();
        bomber = new Bomber(1 * GameMap.TILE_SIZE, 1 * GameMap.TILE_SIZE);

        enemies = new ArrayList<>();
        enemies.add(new Enemy(6 * GameMap.TILE_SIZE, 11 * GameMap.TILE_SIZE));
        enemies.add(new Enemy(11 * GameMap.TILE_SIZE, 11 * GameMap.TILE_SIZE));

        activeBombs = new ArrayList<>(); // Khởi tạo danh sách bom
        inputHandler = new InputHandler();

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                gameMap.render(g2d);

                // Vẽ tất cả bom đang chờ nổ
                for (Bomb bomb : activeBombs) {
                    bomb.render(g2d);
                }

                for (Enemy enemy : enemies) {
                    enemy.render(g2d);
                }

                bomber.render(g2d);
            }
        };

        canvas.setPreferredSize(new Dimension(gameMap.getWidth(), gameMap.getHeight()));
        canvas.setOpaque(true);
        add(canvas);

        pack();
        setLocationRelativeTo(null);
        setFocusable(true);
        canvas.setFocusable(true);

        gameLoopTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                canvas.repaint();
            }
        });
        gameLoopTimer.start();
    }

    private void updateGame() {
        int dx = 0;
        int dy = 0;

        if (inputHandler.isUp()) dy = -1;
        if (inputHandler.isDown()) dy = 1;
        if (inputHandler.isLeft()) dx = -1;
        if (inputHandler.isRight()) dx = 1;

        // Gọi hàm di chuyển của Bomber, truyền kèm danh sách activeBombs để chặn va chạm bom
        if (dx != 0 || dy != 0) {
            bomber.move(dx, dy, gameMap, activeBombs);
        }

        // Xử lý đặt bom khi bấm phím Space
        if (inputHandler.isSpacePressed()) {
            if (spaceReleased) {
                // Tính toán tọa độ ô lưới (Tile) dựa theo vị trí tâm của Bomber
                int bombCol = (bomber.getX() + GameMap.TILE_SIZE / 2) / GameMap.TILE_SIZE;
                int bombRow = (bomber.getY() + GameMap.TILE_SIZE / 2) / GameMap.TILE_SIZE;

                // Thêm bom vào danh sách
                activeBombs.add(new Bomb(bombCol, bombRow));
                spaceReleased = false; // Khóa lại, phải thả phím ra mới đặt được quả tiếp theo
            }
        } else {
            spaceReleased = true;
        }

        // Cập nhật trạng thái đếm ngược của bom và xóa bom đã nổ
        Iterator<Bomb> iterator = activeBombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            bomb.update(gameMap);
            if (bomb.isExploded()) {
                iterator.remove(); // Xóa bom khỏi danh sách sau khi đã nổ xong
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(gameMap, bomber, activeBombs); // Truyền thêm activeBombs vào đây
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapTestFrame frame = new MapTestFrame();
            frame.setVisible(true);
        });
    }
}
