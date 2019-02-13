package xeed;

/*
 * 360/n
 *
 * 
 */
import forms.RelationsForm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Erik
 */
public class RelationGraphPanel extends javax.swing.JPanel {

    private int intNoObjects = 0; //Numbers of dots
    private int ReservedBorders_sides = 20; //Borders not used
    private int ReservedBorders_topbot = 10;
    private int DotSize = 15; //Dot cirle diameter
    private int Radius; //Radius of circle formed by dots
    private dot[] Dots; //Array of dots
    private boolean ready = false; //Ready to paint
    private int fontsize_names = 12; //Font size of the names
    private int fontsize_relation = 12; //Font size of the relations
    private double arg; //argument of polar form, args * intNoObjects = 2*PI
    private int center[]; //X and Y of center point of the circle of dots.

    public RelationGraphPanel() {
        initComponents();
    }

    public BufferedImage GetCloneImage(Color bg, Color text) {

        BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!ready) {
            return null;
        }

        g.setColor(bg);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(text);
        g.setFont(new Font("Arial", Font.PLAIN, fontsize_names));
        CalculatePosAndDrawDots(g);
        DrawNames(g);
        g.setFont(new Font("Arial", Font.PLAIN, fontsize_relation));
        DrawRelations(g);
        return img;

    }

    public boolean LoadData(Character[] chars, Group[] grps) {

        intNoObjects = 0;

        if (chars == null && grps == null) {
            ready = false;
            return false;
        }

        ArrayList<dot> tmp = new ArrayList(0);
        if (chars != null) {
            intNoObjects += chars.length;
            for (int x = 0; x < chars.length; x++) {
                dot d = new dot();
                d.entity = chars[x];
                d.type = 0;
                d.lngID = chars[x].characterID;
                tmp.add(d);
            }
        }

        if (grps != null) {
            intNoObjects += grps.length;
            for (int x = 0; x < grps.length; x++) {
                dot d = new dot();
                d.entity = grps[x];
                d.type = 1;
                d.lngID = grps[x].lngID;
                tmp.add(d);
            }
        }

        Dots = new dot[tmp.size()];
        tmp.toArray(Dots);

        if (intNoObjects < 2) {
            return false;
        }

        ready = true;
        repaint();
        return true;

    }

    public void CalculatePosAndDrawDots(Graphics2D g) {

        ReservedBorders_sides = 20;

        FontMetrics fm = g.getFontMetrics();
        for (int x = 0; x < Dots.length; x++) {
            if (Dots[x].type == 0) {
                ReservedBorders_sides = Math.max(ReservedBorders_sides,
                        fm.stringWidth(((Character) Dots[x].entity).GetCharacterName()) + DotSize);
            } else if (Dots[x].type == 1) {
                ReservedBorders_sides = Math.max(ReservedBorders_sides, fm.stringWidth(((Group) Dots[x].entity).szName)
                        + DotSize);
            }
        }

        ReservedBorders_topbot = fm.getHeight() + DotSize / 2;

        int D = Math.min(getWidth() - ReservedBorders_sides * 2, getHeight() - ReservedBorders_topbot * 2); //Diameter of circle
        Radius = D / 2;
        arg = (2 * Math.PI) / intNoObjects; //argument
        center = new int[2];
        center[0] = getWidth() / 2;
        center[1] = getHeight() / 2; //Centerpoint of circle

        for (int x = 0; x < Dots.length; x++) {

            Dots[x].x = (int) (center[0] + Radius * Math.cos(arg * x) - DotSize / 2);
            Dots[x].y = (int) (center[1] + Radius * Math.sin(arg * x) - DotSize / 2);
            Dots[x].angle = arg * x;
            g.fillOval(Dots[x].x, Dots[x].y, DotSize, DotSize);

        }

    }

    public void DrawNames(Graphics2D g) {

        for (int x = 0; x < Dots.length; x++) {

            String szName = "";
            if (Dots[x].type == 0) {
                szName = ((Character) Dots[x].entity).GetCharacterName();
            } else if (Dots[x].type == 1) {
                szName = ((Group) Dots[x].entity).szName;
            }

            FontMetrics fm = g.getFontMetrics();
            int xmod;
            if (Math.cos(Dots[x].angle) < 0) {
                xmod = -1 * (fm.stringWidth(szName));
            } else {
                xmod = DotSize + 2;
            }

            int ymod;
            if (Math.sin(Dots[x].angle) < 0) {
                ymod = -2;
            } else {
                ymod = fm.getHeight() + DotSize / 2;
            }

            g.drawString(szName, Dots[x].x + xmod, Dots[x].y + ymod);

        }
    }

    private void DrawRelations(Graphics2D g) {

        FontMetrics fm = g.getFontMetrics();

        for (int x = 0; x < Dots.length; x++) {
            for (int y = x + 1; y < Dots.length; y++) {

                String szRelXtoY = "";
                String szRelYtoX = "";
                if (Dots[x].type == 0) {
                    szRelXtoY = ((Character) Dots[x].entity).GetRelation(Dots[y].lngID, Dots[y].type);
                } else {
                    szRelXtoY = ((Group) Dots[x].entity).GetRelation(Dots[y].lngID, Dots[y].type);
                }
                if (Dots[y].type == 0) {
                    szRelYtoX = ((Character) Dots[y].entity).GetRelation(Dots[x].lngID, Dots[x].type);
                } else {
                    szRelYtoX = ((Group) Dots[y].entity).GetRelation(Dots[x].lngID, Dots[x].type);
                }

                if (!szRelYtoX.isEmpty() || !szRelXtoY.isEmpty()) {

                    g.drawLine(Dots[x].x + DotSize / 2, Dots[x].y + DotSize / 2, Dots[y].x + DotSize / 2, Dots[y].y
                            + DotSize / 2);

                    double deltaX = Dots[x].x - Dots[y].x;
                    double deltaY = Dots[x].y - Dots[y].y;

                    double alpha;
                    if (deltaX != 0) {
                        alpha = Math.atan(deltaY / deltaX);
                    } else {
                        alpha = Math.PI / 2;
                    }

                    int i; //index of the start dot
                    int j; //index of end dot
                    String szTo;
                    String szBack;
                    if (Dots[x].x < Dots[y].x + 2 && Dots[x].x > Dots[y].x - 2) {

                        if (Dots[x].y < Dots[y].y) {
                            i = x;
                            j = y;
                            szTo = szRelXtoY;
                            szBack = szRelYtoX;
                        } else {
                            i = y;
                            j = x;
                            szTo = szRelYtoX;
                            szBack = szRelXtoY;
                        }

                    } else {

                        if (Dots[x].x < Dots[y].x) {
                            i = x;
                            j = y;
                            szTo = szRelXtoY;
                            szBack = szRelYtoX;
                        } else {
                            i = y;
                            j = x;
                            szTo = szRelYtoX;
                            szBack = szRelXtoY;
                        }

                    }

                    g.rotate(alpha, center[0], center[1]);

                    double distance = Math.hypot(Dots[j].x - Dots[i].x, Dots[j].y - Dots[i].y);

                    double text_adjust_from_dot;
                    if (Math.max(distance * 0.15, 2 * DotSize) < DotSize) {
                        text_adjust_from_dot = Math.max(distance * 0.15, 2 * DotSize);
                    } else {
                        text_adjust_from_dot = 3 * DotSize / 2;
                    }

                    int dispos_char = (int) (distance - 3 * text_adjust_from_dot / 2) / fm.charWidth('X');
                    if (dispos_char < 0) {
                        dispos_char = 0;
                    }

                    szTo = szTo.substring(0, Math.min(szTo.length(), dispos_char)).replace('\n', ' ');
                    szBack = szBack.substring(0, Math.min(szBack.length(), dispos_char)).replace('\n', ' ');

                    int x_offset = (int) Math.max((int) (distance - fm.stringWidth(szBack) - text_adjust_from_dot / 2),
                            3 * text_adjust_from_dot / 2);

                    g.drawString(szTo,
                            (int) (center[0] + Radius * Math.cos(Dots[i].angle - alpha) - DotSize / 2 + text_adjust_from_dot),
                            (int) (center[1] + Radius * Math.sin(Dots[i].angle - alpha)) - 2);
                    g.drawString(szBack, (int) (center[0] + Radius * Math.cos(Dots[i].angle - alpha) - DotSize / 2)
                            + x_offset, (int) (center[1] + Radius * Math.sin(Dots[i].angle - alpha) + fm.getHeight() - 3));

                    g.rotate(-alpha, center[0], center[1]);

                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        if (!ready) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);

        g2.setFont(new Font("Arial", Font.PLAIN, fontsize_names));
        CalculatePosAndDrawDots(g2);
        DrawNames(g2);
        g2.setFont(new Font("Arial", Font.PLAIN, fontsize_relation));
        DrawRelations(g2);

    }

    @Override
    public Dimension getPreferredSize() {

        return super.getPreferredSize();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(500, 500));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 344,
                Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 303,
                Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

        if (!ready) {
            return;
        }

        for (int x = 0; x < Dots.length; x++) {
            for (int y = x + 1; y < Dots.length; y++) {

                String szRelXtoY = "";
                String szRelYtoX = "";
                if (Dots[x].type == 0) {
                    szRelXtoY = ((Character) Dots[x].entity).GetRelation(Dots[y].lngID, Dots[y].type);
                } else {
                    szRelXtoY = ((Group) Dots[x].entity).GetRelation(Dots[y].lngID, Dots[y].type);
                }
                if (Dots[y].type == 0) {
                    szRelYtoX = ((Character) Dots[y].entity).GetRelation(Dots[x].lngID, Dots[x].type);
                } else {
                    szRelYtoX = ((Group) Dots[y].entity).GetRelation(Dots[x].lngID, Dots[x].type);
                }

                if (!szRelXtoY.isEmpty() || !szRelYtoX.isEmpty()) {

                    if (Dots[x].x < Dots[y].x + 2 && Dots[x].x > Dots[y].x - 2) {

                        if (evt.getX() <= Dots[x].x + 8 && evt.getX() >= Dots[x].x - 8
                                && evt.getY() < Math.max(Dots[x].y, Dots[y].y) && evt.getY() > Math.min(Dots[x].y, Dots[y].y)) {
                            if (XEED.hwndRelations == null) {
                                XEED.hwndRelations = new RelationsForm();
                            }
                            XEED.hwndRelations.setVisible(true);
                            XEED.hwndRelations.LoadSpecificRelation(Dots[x].entity, Dots[y].entity);
                            return;
                        }

                    } else { //In case the line is vertical.

                        double k = (double) (Dots[x].y - Dots[y].y) / (Dots[x].x - Dots[y].x);
                        double m = (double) Dots[x].y + DotSize / 2 - k * (Dots[x].x + DotSize / 2);

                        if (evt.getY() <= (double) (k * evt.getX() + m + 8)
                                && evt.getY() >= (double) (k * evt.getX() + m - 8)
                                && evt.getX() > Math.min(Dots[x].x, Dots[y].x) && evt.getX() < Math.max(Dots[x].x, Dots[y].x)) {
                            if (XEED.hwndRelations == null) {
                                XEED.hwndRelations = new RelationsForm();
                            }
                            XEED.hwndRelations.setVisible(true);
                            XEED.hwndRelations.LoadSpecificRelation(Dots[x].entity, Dots[y].entity);
                            return;
                        }
                    }

                }

            }

        }

    }//GEN-LAST:event_formMouseReleased

    class dot {

        int type; //0-char,1-group
        long lngID; //Official id
        int x;
        int y;
        double angle;
        Object entity;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
