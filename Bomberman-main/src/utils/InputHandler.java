package utils;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class InputHandler implements KeyEventDispatcher {
    private final Set<Integer> pressedKeys = new HashSet<>();

    public InputHandler() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        synchronized (InputHandler.class) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                pressedKeys.add(e.getKeyCode());
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                pressedKeys.remove(e.getKeyCode());
            }
        }
        return false;
    }

    public synchronized boolean isKeyDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isUp() {
        return isKeyDown(KeyEvent.VK_W) || isKeyDown(KeyEvent.VK_UP);
    }

    public boolean isDown() {
        return isKeyDown(KeyEvent.VK_S) || isKeyDown(KeyEvent.VK_DOWN);
    }

    public boolean isLeft() {
        return isKeyDown(KeyEvent.VK_A) || isKeyDown(KeyEvent.VK_LEFT);
    }

    public boolean isRight() {
        return isKeyDown(KeyEvent.VK_D) || isKeyDown(KeyEvent.VK_RIGHT);
    }

    // Kiểm tra phím Space để đặt bom (đã sửa dùng đúng biến pressedKeys)
    public boolean isSpacePressed() {
        return isKeyDown(KeyEvent.VK_SPACE);
    }
}
