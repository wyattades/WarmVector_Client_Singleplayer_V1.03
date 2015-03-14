package Visual;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import Entity.Entity;
import Main.Game;

/**
 * Created by Wyatt on 3/12/2015.
 */
public class Shadow2D {

    private final static float GRADIENT_SIZE = Game.HEIGHT;

    private final static Color GRADIENT_COLOR = new Color(40,40,40,255);

    private final static Polygon POLYGON = new Polygon();

    private int x, y;
    private double orient;
    private HashMap<String,ArrayList<Entity>> allEnts;

    public Shadow2D(int x, int y, double orient, HashMap<String,ArrayList<Entity>> allEnts) {
        this.x = x;
        this.y = y;
        this.orient = orient;
        this.allEnts = allEnts;
    }

    public void update(int x, int y, double orient, HashMap<String, ArrayList<Entity>> allEnts) {
        this.x = x;
        this.y = y;
        this.orient = orient;
        this.allEnts = allEnts;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        Paint oldPaint = g.getPaint();
        float minDistSq = GRADIENT_SIZE * GRADIENT_SIZE;
        final float SHADOW_EXTRUDE = GRADIENT_SIZE * GRADIENT_SIZE;
        final Point2D.Float center = new Point2D.Float(x, y);

        for (HashMap.Entry<String, ArrayList<Entity>> entry : allEnts.entrySet()) {
            if (!entry.getKey().equals("thisPlayer")) {
                for (Entity entity : entry.getValue()) {
                    if (entity.transparent == false) {
                        Rectangle2D.Float bounds = new Rectangle2D.Float((int) (entity.dx - entity.h / 2), (int) (entity.dy - entity.h / 2), (int) entity.w + 2, (int) entity.h + 2);

                        //radius of Entity's bounding circle
                        float r = (float) bounds.getWidth() / 2;

                        //get center of entity
                        float cx = (float) bounds.getX() + r;
                        float cy = (float) bounds.getY() + r;

                        //get direction from mouse to entity center
                        float dx = cx - center.x;
                        float dy = cy - center.y;

                        //get euclidean distance from mouse to center
                        float distSq = dx * dx + dy * dy; //avoid sqrt for performance

                        //normalize the direction to a unit vector
                        float len = (float) Math.sqrt(distSq);
                        float nx = dx;
                        float ny = dy;
                        if (len != 0) { //avoid division by 0
                            nx /= len;
                            ny /= len;
                        }

                        //get perpendicular of unit vector
                        float px = -ny;
                        float py = nx;

                        //our perpendicular points in either direction from radius
                        Point2D.Float A = new Point2D.Float(cx - px * r, cy - py * r);
                        Point2D.Float B = new Point2D.Float(cx + px * r, cy + py * r);

                        //project the points by our SHADOW_EXTRUDE amount
                        Point2D.Float C = project(center, A, SHADOW_EXTRUDE);
                        Point2D.Float D = project(center, B, SHADOW_EXTRUDE);

                        //construct a polygon from our points
                        POLYGON.reset();
                        POLYGON.addPoint((int) A.x, (int) A.y);
                        POLYGON.addPoint((int) B.x, (int) B.y);
                        POLYGON.addPoint((int) D.x, (int) D.y);
                        POLYGON.addPoint((int) C.x, (int) C.y);

                        //fill the polygon with the gradient paint
                        g.setPaint(GRADIENT_COLOR);
                        g.fill(POLYGON);
                    }
                }
            }
        }
        //reset to old Paint object
        g.setPaint(oldPaint);
    }

    /** Projects a point from end along the vector (end - start) by the given scalar amount. */
    private Point2D.Float project(Point2D.Float start, Point2D.Float end, float scalar) {
        float dx = end.x - start.x;
        float dy = end.y - start.y;
        //euclidean length
        float len = (float)Math.sqrt(dx * dx + dy * dy);
        //normalize to unit vector
        if (len != 0) {
            dx /= len;
            dy /= len;
        }
        //multiply by scalar amount
        dx *= scalar;
        dy *= scalar;
        return new Point2D.Float(end.x + dx, end.y + dy);
    }
}