package pl.pw.edu.mini.zpoif.fish_watch.gui.main_panel.tabs;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;


public class DotWaypointRenderer implements WaypointRenderer<Waypoint> {

    private BufferedImage img = null;

    public DotWaypointRenderer() {
        try {
            // Load image as a resource stream
            String imageUrl = "/img/dot.png"; // Path relative to the 'resources' folder
            InputStream is = getClass().getResourceAsStream(imageUrl);
            if (is == null) {
                throw new RuntimeException("Resource not found: " + imageUrl);
            }
            this.img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w) {
        if (this.img != null) {
            Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
            int x = (int)point.getX() - this.img.getWidth() / 2;
            int y = (int)point.getY() - this.img.getHeight();
            g.drawImage(this.img, x, y, null);
        }
    }

//    public void test(){
//        try {
//            // Load image as a resource stream
//            String resourcePath = "/img/dot.png"; // Path relative to the 'resources' folder
//            InputStream is = getClass().getResourceAsStream(resourcePath);
//            if (is == null) {
//                throw new RuntimeException("Resource not found: " + resourcePath);
//            }
//            this.img = ImageIO.read(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}
