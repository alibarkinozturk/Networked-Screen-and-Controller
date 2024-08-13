package radiocontrol;

import javax.swing.*;
import java.awt.*;

public class CirclePanel extends JPanel {

    private int diameter;
    private Color colorFill;
    private Color colorDraw;
    private boolean drawOutline;
    private boolean hasLine;

    public CirclePanel(int diameter, Color colorFill, Color colorDraw, boolean drawOutline, boolean hasLine) {
        this.diameter = diameter * 2;
        this.colorFill = colorFill;
        this.colorDraw = colorDraw;
        this.drawOutline = drawOutline;
        this.hasLine = hasLine;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Antialiasing'i etkinleştir
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Çemberin merkezi, panelin ortası olacak şekilde hesaplanır
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;
        int centerX = x + diameter / 2;
        int centerY = y + diameter / 2;

        // Çemberi çiz
        if (drawOutline) {
            g2d.setColor(colorDraw);
            g2d.setStroke(new BasicStroke(50));
            g2d.drawOval(x - 1, y - 1, diameter + 2, diameter + 2);
        } else {
            g2d.setColor(colorDraw);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawOval(x - 1, y - 1, diameter + 2, diameter + 2);
        }

        g2d.setColor(colorFill);
        g2d.fillOval(x, y, diameter, diameter);

        // Çemberi 6 eşit parçaya bölecek çizgiler çiz
        g2d.setColor(colorDraw);
        g2d.setStroke(new BasicStroke(2));

        if (hasLine) {
             g2d.setColor(Color.GRAY);
            for (int i = 0; i < 36; i++) {
                if(i%6==0){
                double angle = Math.toRadians(10 * i);
                int lX = centerX + (int) (diameter / 2.25 * Math.cos(angle- Math.PI/2));
                int lY = centerY + (int) (diameter / 2.25 * Math.sin(angle- Math.PI/2));
                int lineX = centerX + (int) (diameter / 2 * Math.cos(angle- Math.PI/2));
                int lineY = centerY + (int) (diameter / 2 * Math.sin(angle- Math.PI/2));
                g2d.drawLine(lX, lY, lineX, lineY);
                
                
                }
                else{double angle = Math.toRadians(10 * i);
                int lX = centerX + (int) (diameter / 2.1 * Math.cos(angle - Math.PI/2));
                int lY = centerY + (int) (diameter / 2.1 * Math.sin(angle- Math.PI/2));
                int lineX = centerX + (int) (diameter / 2 * Math.cos(angle- Math.PI/2));
                int lineY = centerY + (int) (diameter / 2 * Math.sin(angle- Math.PI/2));
                g2d.drawLine(lX, lY, lineX, lineY);}
            }
        }
    }

    public void setColorFill(Color colorFill) {
        this.colorFill = colorFill;
        repaint();
    }

    public void setColorDraw(Color colorDraw) {
        this.colorDraw = colorDraw;
        repaint();
    }

    public void setDrawOutline(boolean drawOutline) {
        this.drawOutline = drawOutline;
        repaint();
    }
    
    public void setHasLine(boolean hasLine) {
        this.hasLine = hasLine;
        repaint();
    }
}
