package Entities;

import Entities.Weapons.Weapon;
import Helper.MyMath;
import Main.Game;
import Map.GeneratedEnclosure;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    public float vx, vy;
    public float life;
    public float maxLife;
    public static int topSpeed = 5;
    public int shootTime;
    protected BufferedImage shootSprite, defaultSprite, deadSprite;
    protected GeneratedEnclosure map;

    Player(float x, float y, float orient, GeneratedEnclosure map) {
        super(x, y,  orient);
        this.map = map;
        hitColor = Color.red;
        resetVelocity();
        life = maxLife = 100.0f;
        shootTime = Game.currentTimeMillis();
        w = h = 36;
    }

    public void setWeapon(Weapon w) {
        weapon = w;
        if (w == null) {
            setSpriteToDefault(true);
        } else {
            weapon.user = this;
            setSpriteToDefault(false);
        }
    }

    protected void setSpriteToDefault(boolean bool) {
        if (bool) sprite = defaultSprite;
        else sprite = shootSprite;
    }

    public void resetVelocity() {
        vx = 0;
        vy = 0;
    }

    private void slowPlayer() {

        if (Math.abs(vx) > 0.05f) {
            vx *= 0.7f;
        } else {
            vx = 0;
        }

        if (Math.abs(vy) > 0.05f) {
            vy *= 0.7f;
        } else {
            vy = 0;
        }

    }

    public void updatePosition() {
        slowPlayer();
        if (!obstacleX(vx)) x += vx;
        if (!obstacleY(vy)) y += vy;
    }

    @Override
    public boolean hit(int amount, float angle) {
        life -= amount;
        if (life < 0) {
            life = 0;
            state = false;
        }
        return true;
    }

    public void update() {
//        if (vx != 0 && vy != 0) {
//            vx /= Math.sqrt(2);
//            vy /= Math.sqrt(2);
//        }
        updatePosition();
        updateCollideBox();
    }

    public Weapon getWeaponForDrop() {
        float spawnDist = MyMath.random(10, 24);
        Weapon w = weapon;
        w.x = x + spawnDist * (float)Math.cos(MyMath.random(0, 6.29f));
        w.y = y + spawnDist * (float)Math.sin(MyMath.random(0, 6.29f));
        w.orient = MyMath.random(0,6.29f);
        w.user = null;
        return w;
    }

    private boolean obstacleX(float vx) {

        Rectangle2D futureX = new Rectangle2D.Float(x - w/2 + vx, y - h/2, w, h);

//        boolean collidesX = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureX.intersectsLine(line)) {
//                collidesX = true;
//            }
//        }
//
//        return collidesX;

        return map.inverseRegion.intersects(futureX);
    }

    private boolean obstacleY(float vy) {

        Rectangle2D futureY = new Rectangle2D.Float(x - w/2, y - h/2 + vy, w, h);

//        boolean collidesY = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureY.intersectsLine(line)) {
//                collidesY = true;
//            }
//        }
//
//        return collidesY;

        return map.inverseRegion.intersects(futureY);

    }

//    public void updateVelX(float velX) {
//
//        Rectangle2D futureX = new Rectangle2D.Float(x - w/2 + velX, y - h/2, w, h);
//
//        boolean collidesX = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureX.intersectsLine(line)) {
//                collidesX = true;
//            }
//        }
//        if (!collidesX) vx = velX;
//
//    }
//
//    public void updateVelY(float velY) {
//
//        Rectangle2D futureY = new Rectangle2D.Float(x - w/2, y - h/2 + velY, w, h);
//
//        boolean collidesY = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureY.intersectsLine(line)) {
//                collidesY = true;
//            }
//        }
//        if (!collidesY) vy = velY;
//    }

//    public void updateVelY(float velY) {
//        velY *= topSpeed;
//
//        Rectangle2D future = new Rectangle2D.Float((x - w/2), (y - h/2 + velY), w, h);
//        boolean collides = false;
//        for (Line2D line : ge.walls) {
//            if (future.intersectsLine(line)) {
//                collides = true;
//            }
//        }
//        if (!collides) vy = velY;
//
//
////        if ((velY > 0 && !collideTile(0, h / 2, w * 0.3f, 0)) || (velY < 0 && !collideTile(0, -h / 2, w * 0.3f, 0)))
////            vy = velY;
//    }

//    protected boolean collideTile(float ax, float ay, float dx, float dy) {
//        return inTile(x + ax, y + ay) || inTile(x + ax + dx, y + ay + dy) || inTile(x + ax - dx, y + ay - dy);
//    }
//
//    private boolean inTile(float x, float y) {
////        x = x / TileMap.tileSize;
////        y = y / TileMap.tileSize;
//
//        //TEMP
//        return false;
////        int tileType = tileMap.tileArray[Math.round(x)][Math.round(y)]; //NOTE: if Math.round() doesn't work, cast it int instead
////        return tileType == TileMap.SOLID || tileType == TileMap.WINDOW;
//    }


    public void deathSequence() {
        weapon = null;
        resetVelocity();
        sprite = deadSprite;
        //animation???
    }
}
