package GameState;

import Main.Game;
import StaticManagers.InputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.Slider;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends MenuState {

    public PauseState(GameStateManager gsm) {
        super(gsm);
    }

    protected void initButtons() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        initDefault();
        addButton("MAIN MENU", ButtonC.MAINMENU);
        addButton("RESUME", ButtonC.RESUME);
    }

    public void init() {
        startY = Game.HEIGHT - 200;
        initButtons();
        gsm.cursor.setSprite(MouseCursor.CURSOR);
//        gsm.cursor.updateOldPos();
    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
//        gsm.cursor.setToOldPos();
    }

    public void draw(Graphics2D g) {

        for (Slider s : sliders) {
            s.draw(g);
        }

        for (ButtonC b : buttons) {
            b.update(gsm.cursor.x, gsm.cursor.y);
            b.draw(g);
        }

        gsm.cursor.draw(g);

    }

    public void update() {
    }

//    public void setCursor() {
//        gsm.cursor.setSprite(MouseCursor.CURSOR);
//    }

    public void inputHandle(InputManager inputManager) {
        defaultInputHandle(inputManager);
        if (inputManager.isKeyPressed("ESC") && Game.currentTimeMillis() - inputManager.getKeyTime("ESC") > 400) {
            gsm.unloadState(GameStateManager.PAUSE);
            inputManager.setKeyTime("ESC", Game.currentTimeMillis());
        }
    }

}
