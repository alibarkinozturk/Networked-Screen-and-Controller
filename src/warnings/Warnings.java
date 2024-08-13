/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package warnings;

import Socket.Client;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;

/**
 *
 * @author aliba
 */
public class Warnings extends javax.swing.JFrame {

    static Client c;
    static ArrayList<Warning> warningList;
    static Warning w;
    static int id;

    static int MAX_TIREPRESSURE = 36, MIN_TIREPRESSURE = 28, MAX_OIL_PRESSURE = 65, MIN_OIL_PRESSURE = 25, MAX_OIL_TEMPERATURE = 127, MIN_OIL_TEMPERATURE = 100;
    static boolean[] isSeated = new boolean[5];
    static boolean[] seatBelt = new boolean[5];
    static boolean[] tireProb = new boolean[4];
    static boolean[] door = new boolean[5];
    static int[] tirePressure = new int[4];
    static boolean oilTB, oilPB, handBrake, work, oldWork;
    static int oilPressure, oilTemperature;
    static int speed;
    static int iListSize, wListSize;

    public Warnings() throws IOException, IOException {

        initComponents();
    }

    public static void sendList() throws IOException {
        //System.out.println("SENDLIST");
        String list = "";
        for (int i = 0; i < warningList.size(); i++) {
            ///System.out.println(warningList.get(i).getMessage());
            list += (warningList.get(i).getMessage() + "$");
        }
        //System.out.println("SENDLIST END");
        c.sendCommand("Warning " + list, 2828);
    }

    public static void sendTirePressure() throws IOException {
        c.sendCommand("variable tirePressure#" + tirePressure[0] + "#" + tirePressure[1] + "#" + tirePressure[2] + "#" + tirePressure[3], 2828);
        //c.sendCommand("info tireProb#" + tireProb[0] + "#" + tireProb[1] + "#" + tireProb[2] + "#" + tireProb[3], 2828);
    }

    public static void sendOilPressure() throws IOException {
        c.sendCommand("variable oilPressure#" + oilPressure, 2828);
        //c.sendCommand("info oilPB#" + oilPB, 2828);
    }

    public static void sendOilTemperature() throws IOException {
        c.sendCommand("variable oilTemperature#" + oilTemperature, 2828);
        //c.sendCommand("info oilTB#" + oilTB, 2828);
    }

    public static void sendSeatBelt() throws Exception {
        c.sendCommand("variable isSeated#" + isSeated[0] + "#" + isSeated[1] + "#" + isSeated[2] + "#" + isSeated[3] + "#" + isSeated[4], 2828);
        //c.sendCommand("info seatBelt#" + seatBelt[0] + "#" + seatBelt[1] + "#" + seatBelt[2] + "#" + seatBelt[3] + "#" + seatBelt[4], 2828);
    }

    public static void sendDoor() throws Exception {
        //c.sendCommand("info door#" + door[0] + "#" + door[1] + "#" + door[2] + "#" + door[3] + "#" + door[4], 2828);
    }

    public static void sendHandBrake() throws IOException {
        // c.sendCommand("info handBrake#" + handBrake, 2828);

    }

    public static void sendSpeed() throws IOException {
        c.sendCommand("info speed#" + speed, 2828);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void controlOilTemperature() throws IOException {

        oilTB = (oilTemperature > MAX_OIL_TEMPERATURE || oilTemperature < MIN_OIL_TEMPERATURE);

        if (oilTB) {

            w = new Warning(id++, 1, "oilTemperature_" + oilTemperature);
            if (controlWarning(w)) {
                warningList.add(w);
            }

            sendList();
        } else {
            deleteWar("oilTemperature_");
        }
        sendOilTemperature();
    }

    public static void controlOilPressure() throws IOException {

        oilPB = (oilPressure > MAX_OIL_PRESSURE || oilPressure < MIN_OIL_PRESSURE);

        if (oilPB) {

            w = new Warning(id++, 1, "oilPressure_" + oilPressure);
            if (controlWarning(w)) {
                warningList.add(w);
            }

            sendList();
        } else {
            deleteWar("oilPressure_");
        }
        sendOilPressure();
    }

    public static void controlTirePressure() throws IOException {
        for (int i = 0; i < tirePressure.length; i++) {

            tireProb[i] = (tirePressure[i] > MAX_TIREPRESSURE || tirePressure[i] < MIN_TIREPRESSURE);

            if (tireProb[i]) {

                w = new Warning(id++, 1, "tirePressure_" + i + "_" + tirePressure[i]);
                if (controlWarning(w)) {
                    warningList.add(w);
                }

                sendList();
            } else {
                deleteWar("tirePressure_" + i);
            }

        }
        sendTirePressure();
    }

    public static void controlSeatBelt() throws Exception {
        for (int i = 0; i < seatBelt.length; i++) {

            System.out.println("work " + work);
            System.out.println("speed " + speed);
            System.out.println("isSeated[" + i + "] " + isSeated[i]);
            System.out.println("!seatBelt[" + i + "] " + !seatBelt[i]);
            if (work && speed > 20 && isSeated[i] && !seatBelt[i]) {
                w = new Warning(id++, 1, "seatBelt_" + i + "_" + seatBelt[i]);
                if (controlWarning(w)) {
                    warningList.add(w);
                }

                sendList();
            } else {
                deleteWar("seatBelt_" + i);
            }

        }

        sendSeatBelt();
    }

    public static void controlDoor() throws Exception {

        for (int i = 0; i < door.length; i++) {

            if (door[i]) {

                w = new Warning(id++, 1, "door_" + i + "_" + door[i]);
                if (controlWarning(w)) {
                    warningList.add(w);
                }

                sendList();
            }

        }
        sendDoor();
    }

    public static void controlHandBrake() throws IOException {
        System.out.println(work + " " + handBrake);
        if (work && handBrake) {
            w = new Warning(id++, 1, "handBrake_" + handBrake);
            //System.out.println("control");
            if (controlWarning(w)) {
                warningList.add(w);

            }
            sendList();
        } else if ((!work) && (!handBrake)) {

            w = new Warning(id++, 1, "handBrake_" + handBrake);
            //System.out.println("control");
            if (controlWarning(w)) {
                warningList.add(w);

            }
            sendList();
        } else {
            deleteWar("handBrake_false");
            deleteWar("handBrake_true");
        }

        sendHandBrake();
    }

    private static void controlSpeed() throws IOException, Exception {
        sendSpeed();
        controlSeatBelt();

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void setSpeed(int value) throws Exception {
        speed = value;
        setSeatBelt();
    }

    private static void setOilTemperature(int value) throws IOException {

        oilTemperature = value;
        controlOilTemperature();
    }

    private static void setOilPressure(int value) throws IOException {

        oilPressure = value;
        controlOilPressure();
    }

    private static void setTirePressure(int index, int value) throws IOException {

        tirePressure[index] = value;
        controlTirePressure();
    }

    private static void setSeatBelt() throws Exception {

        seatBelt = new boolean[]{seatBelt0.isSelected(), seatBelt1.isSelected(), seatBelt2.isSelected(), seatBelt3.isSelected(), seatBelt4.isSelected()};
        controlSeatBelt();

    }

    private static void setIsSeated() throws Exception {
        isSeated = new boolean[]{isSeat0.isSelected(), isSeat1.isSelected(), isSeat2.isSelected(), isSeat3.isSelected(), isSeat4.isSelected()};
        controlSeatBelt();
    }

    private static void setDoor() throws Exception {

        door = new boolean[]{door0.isSelected(), door1.isSelected(), door2.isSelected(), door3.isSelected(), door4.isSelected()};
        controlDoor();
    }

    private static void setHandBrake() throws IOException {

        handBrake = handBrakeCB.isSelected();
        controlHandBrake();
    }

    private static Boolean controlWarning(Warning newWarning) {
        return (!warningList.contains(newWarning));
    }

    private static void deleteWar(String m) throws IOException {
        //System.out.println(m);
        for (int i = 0; i < warningList.size(); i++) {

            if (warningList.get(i).message.contains(m)) {
                warningList.remove(i);
                sendList();
            }
        }

    }
    private static void sendWListSize() throws IOException{
     c.sendCommand("wListSize " + wListSize, 2828);
    }
    
     private static void sendIListSize() throws IOException{
     c.sendCommand("iListSize " + iListSize, 2828);
    }
    
    private static void setDeafult() throws IOException {
        work = oldWork = false;
        handBrake = true;
        isSeated = new boolean[]{false, false, false, false, false};
        seatBelt = new boolean[]{false, false, false, false, false};
        door = new boolean[]{false, false, false, false, false};
        tirePressure = new int[]{32, 32, 32, 32};
        id = 0;

        warningList = new ArrayList() {
        };
        oilPressure = 30;
        oilTemperature = 100;
        wListSize = 0;
        iListSize = 0;
        sendWListSize();
        sendIListSize();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        door0 = new javax.swing.JCheckBox();
        door1 = new javax.swing.JCheckBox();
        door2 = new javax.swing.JCheckBox();
        door3 = new javax.swing.JCheckBox();
        door4 = new javax.swing.JCheckBox();
        seatBelt0 = new javax.swing.JCheckBox();
        seatBelt1 = new javax.swing.JCheckBox();
        seatBelt2 = new javax.swing.JCheckBox();
        seatBelt3 = new javax.swing.JCheckBox();
        seatBelt4 = new javax.swing.JCheckBox();
        handBrakeCB = new javax.swing.JCheckBox();
        oilT = new javax.swing.JTextField();
        oilP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tire0T = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tire1T = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tire2T = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tire3T = new javax.swing.JTextField();
        isSeat0 = new javax.swing.JCheckBox();
        isSeat1 = new javax.swing.JCheckBox();
        isSeat2 = new javax.swing.JCheckBox();
        isSeat3 = new javax.swing.JCheckBox();
        isSeat4 = new javax.swing.JCheckBox();
        ss = new javax.swing.JButton();
        speedTextBox = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        wListSizeSetter = new javax.swing.JTextField();
        iListSizeSetter = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        restart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        door0.setText("door0");
        door0.setToolTipText("");
        door0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door0ActionPerformed(evt);
            }
        });

        door1.setText("door1");
        door1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door1ActionPerformed(evt);
            }
        });

        door2.setText("door2");
        door2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door2ActionPerformed(evt);
            }
        });

        door3.setText("door3");
        door3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door3ActionPerformed(evt);
            }
        });

        door4.setText("door4");
        door4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                door4ActionPerformed(evt);
            }
        });

        seatBelt0.setText("seatBelt0");
        seatBelt0.setToolTipText("");
        seatBelt0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatBelt0ActionPerformed(evt);
            }
        });

        seatBelt1.setText("seatBelt1");
        seatBelt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatBelt1ActionPerformed(evt);
            }
        });

        seatBelt2.setText("seatBelt2");
        seatBelt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatBelt2ActionPerformed(evt);
            }
        });

        seatBelt3.setText("seatBelt3");
        seatBelt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatBelt3ActionPerformed(evt);
            }
        });

        seatBelt4.setText("seatBelt4");
        seatBelt4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatBelt4ActionPerformed(evt);
            }
        });

        handBrakeCB.setSelected(true);
        handBrakeCB.setText("handBrake");
        handBrakeCB.setToolTipText("");
        handBrakeCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handBrakeCBActionPerformed(evt);
            }
        });

        oilT.setText(String.valueOf(oilTemperature)
        );
        oilT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oilTKeyPressed(evt);
            }
        });

        oilP.setText(String.valueOf(oilPressure)
        );
        oilP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                oilPKeyPressed(evt);
            }
        });

        jLabel1.setText("Oil Temperature");
        jLabel1.setEnabled(false);

        jLabel2.setText("Oil Pressure");
        jLabel2.setEnabled(false);

        jLabel3.setText("Front Left Tire Pressure");
        jLabel3.setEnabled(false);

        tire0T.setText(String.valueOf(tirePressure[0])
        );
        tire0T.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tire0TKeyTyped(evt);
            }
        });

        jLabel4.setText("Front Right Tire Pressure");
        jLabel4.setEnabled(false);

        tire1T.setText(String.valueOf(tirePressure[1])
        );
        tire1T.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tire1TKeyTyped(evt);
            }
        });

        jLabel5.setText("Back Left Tire Pressure");
        jLabel5.setEnabled(false);

        tire2T.setText(String.valueOf(tirePressure[2])
        );
        tire2T.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tire2TKeyPressed(evt);
            }
        });

        jLabel6.setText("Back Right Tire Pressure");
        jLabel6.setEnabled(false);

        tire3T.setText(String.valueOf(tirePressure[3])
        );
        tire3T.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tire3TKeyPressed(evt);
            }
        });

        isSeat0.setText("isSeated0");
        isSeat0.setToolTipText("");
        isSeat0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSeat0ActionPerformed(evt);
            }
        });

        isSeat1.setText("isSeated1");
        isSeat1.setToolTipText("");
        isSeat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSeat1ActionPerformed(evt);
            }
        });

        isSeat2.setText("isSeated2");
        isSeat2.setToolTipText("");
        isSeat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSeat2ActionPerformed(evt);
            }
        });

        isSeat3.setText("isSeated3");
        isSeat3.setToolTipText("");
        isSeat3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSeat3ActionPerformed(evt);
            }
        });

        isSeat4.setText("isSeated4");
        isSeat4.setToolTipText("");
        isSeat4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isSeat4ActionPerformed(evt);
            }
        });

        ss.setText("start/stop");
        ss.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 10));
        ss.setBorderPainted(false);
        ss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ssActionPerformed(evt);
            }
        });

        speedTextBox.setText(String.valueOf(speed));
        speedTextBox.setMaximumSize(new java.awt.Dimension(77, 22));
        speedTextBox.setMinimumSize(new java.awt.Dimension(77, 22));
        speedTextBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedTextBoxActionPerformed(evt);
            }
        });
        speedTextBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                speedTextBoxKeyPressed(evt);
            }
        });

        jLabel7.setText("Speed");
        jLabel7.setEnabled(false);

        wListSizeSetter.setText(String.valueOf(wListSize));
        wListSizeSetter.setMaximumSize(new java.awt.Dimension(77, 22));
        wListSizeSetter.setMinimumSize(new java.awt.Dimension(77, 22));
        wListSizeSetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wListSizeSetterActionPerformed(evt);
            }
        });
        wListSizeSetter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                wListSizeSetterKeyPressed(evt);
            }
        });

        iListSizeSetter.setText(String.valueOf(iListSize));
        iListSizeSetter.setMaximumSize(new java.awt.Dimension(77, 22));
        iListSizeSetter.setMinimumSize(new java.awt.Dimension(77, 22));
        iListSizeSetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iListSizeSetterActionPerformed(evt);
            }
        });
        iListSizeSetter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                iListSizeSetterKeyPressed(evt);
            }
        });

        jLabel8.setText("wList size");
        jLabel8.setEnabled(false);

        jLabel9.setText("iList size");
        jLabel9.setEnabled(false);

        restart.setText("Restart");
        restart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(door4)
                            .addComponent(door1)
                            .addComponent(door0)
                            .addComponent(door2)
                            .addComponent(door3))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(seatBelt4)
                            .addComponent(seatBelt1)
                            .addComponent(seatBelt0)
                            .addComponent(seatBelt2)
                            .addComponent(seatBelt3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isSeat0, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isSeat2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isSeat1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isSeat3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(isSeat4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(handBrakeCB)
                            .addComponent(ss))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(oilT, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(oilP, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addComponent(jLabel3)
                                        .addGap(13, 13, 13))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tire0T)
                                    .addComponent(tire1T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(speedTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(109, 109, 109))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(iListSizeSetter, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(wListSizeSetter, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tire2T)
                                            .addComponent(tire3T))
                                        .addGap(252, 252, 252))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(79, 79, 79)
                                        .addComponent(restart)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(door0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(door1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(door2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(door3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(door4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(seatBelt0)
                            .addComponent(isSeat0))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seatBelt1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seatBelt2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seatBelt3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seatBelt4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(isSeat1)
                            .addComponent(ss, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(isSeat2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(isSeat3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(isSeat4)
                            .addComponent(handBrakeCB))))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(tire0T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oilT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(tire1T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(tire2T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oilP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(tire3T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(speedTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wListSizeSetter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(restart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iListSizeSetter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void door0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door0ActionPerformed
        if (door0.isSelected()) {
            try {
                setDoor();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("door_0_true");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_door0ActionPerformed

    private void door1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door1ActionPerformed
        if (door1.isSelected()) {
            try {
                setDoor();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("door_1_true");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_door1ActionPerformed

    private void door2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door2ActionPerformed
        if (door2.isSelected()) {
            try {
                setDoor();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("door_2_true");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_door2ActionPerformed

    private void door3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door3ActionPerformed
        if (door3.isSelected()) {
            try {
                setDoor();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("door_3_true");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_door3ActionPerformed

    private void door4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_door4ActionPerformed
        if (door4.isSelected()) {
            try {
                setDoor();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("door_4_true");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_door4ActionPerformed

    private void seatBelt0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatBelt0ActionPerformed
        if (!seatBelt0.isSelected()) {
            try {
                setSeatBelt();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("seatBelt_0_false");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_seatBelt0ActionPerformed

    private void seatBelt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatBelt1ActionPerformed
        if (!seatBelt1.isSelected()) {
            try {
                setSeatBelt();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("seatBelt_1_false");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_seatBelt1ActionPerformed

    private void seatBelt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatBelt2ActionPerformed
        if (!seatBelt2.isSelected()) {
            try {
                setSeatBelt();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("seatBelt_2_false");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_seatBelt2ActionPerformed

    private void seatBelt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatBelt3ActionPerformed
        if (!seatBelt3.isSelected()) {
            try {
                setSeatBelt();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("seatBelt_3_false");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_seatBelt3ActionPerformed

    private void seatBelt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatBelt4ActionPerformed
        if (!seatBelt4.isSelected()) {
            try {
                setSeatBelt();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                deleteWar("seatBelt_4_false");
            } catch (IOException ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_seatBelt4ActionPerformed

    private void handBrakeCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handBrakeCBActionPerformed
        try {
            setHandBrake();
        } catch (IOException ex) {
            Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_handBrakeCBActionPerformed

    private void tire0TKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tire0TKeyTyped

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(tire0T.getText());
                setTirePressure(0, pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tire0TKeyTyped

    private void tire1TKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tire1TKeyTyped

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(tire1T.getText());
                setTirePressure(1, pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_tire1TKeyTyped

    private void tire2TKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tire2TKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(tire2T.getText());
                setTirePressure(2, pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tire2TKeyPressed

    private void tire3TKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tire3TKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(tire3T.getText());
                setTirePressure(3, pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }

    }//GEN-LAST:event_tire3TKeyPressed

    private void oilTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oilTKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(oilT.getText());
                setOilTemperature(pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_oilTKeyPressed

    private void oilPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_oilPKeyPressed

        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int pressureValue = Integer.parseInt(oilP.getText());
                setOilPressure(pressureValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_oilPKeyPressed

    private void isSeat0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSeat0ActionPerformed
        if (isSeat0.isSelected()) {
            try {
                setSeatBelt();
                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (isSeated[0]) {
                    deleteWar("seatBelt_0_false");
                }
                setIsSeated();

            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_isSeat0ActionPerformed

    private void isSeat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSeat1ActionPerformed
        if (isSeat1.isSelected()) {
            try {
                setSeatBelt();

                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                if (isSeated[1]) {
                    deleteWar("seatBelt_1_false");
                }
                setIsSeated();

            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_isSeat1ActionPerformed

    private void isSeat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSeat2ActionPerformed
        if (isSeat2.isSelected()) {
            try {
                setSeatBelt();

                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (isSeated[2]) {
                    deleteWar("seatBelt_2_false");
                }
                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_isSeat2ActionPerformed

    private void isSeat3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSeat3ActionPerformed
        if (isSeat3.isSelected()) {
            try {
                setSeatBelt();

                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (isSeated[3]) {
                    deleteWar("seatBelt_3_false");
                }
                setIsSeated();

            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_isSeat3ActionPerformed

    private void isSeat4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isSeat4ActionPerformed
        if (isSeat4.isSelected()) {
            try {
                setSeatBelt();

                setIsSeated();
            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (isSeated[4]) {
                    deleteWar("seatBelt_4_false");
                }
                setIsSeated();

            } catch (Exception ex) {
                Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_isSeat4ActionPerformed

    private void ssActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ssActionPerformed
        try {
            work = (!work);

            ss.setBorderPainted(work);
            controlSeatBelt();
            controlHandBrake();

        } catch (Exception ex) {
            Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ssActionPerformed

    private void speedTextBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_speedTextBoxKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                int speedValue = Integer.parseInt(speedTextBox.getText());
                setSpeed(speedValue);
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_speedTextBoxKeyPressed

    private void speedTextBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedTextBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_speedTextBoxActionPerformed

    private void wListSizeSetterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wListSizeSetterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_wListSizeSetterActionPerformed

    private void wListSizeSetterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_wListSizeSetterKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                wListSize = Integer.parseInt(wListSizeSetter.getText());
                sendWListSize();
               
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_wListSizeSetterKeyPressed

    private void iListSizeSetterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iListSizeSetterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_iListSizeSetterActionPerformed

    private void iListSizeSetterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iListSizeSetterKeyPressed
         if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            try {
                iListSize = Integer.parseInt(iListSizeSetter.getText());
                sendIListSize();
               
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }//GEN-LAST:event_iListSizeSetterKeyPressed

    private void restartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartActionPerformed
        // Mevcut JFrame'i kapat
        this.dispose();
        try {
            setDeafult();
        } catch (IOException ex) {
            Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            controlDoor();
            controlHandBrake();
            controlSeatBelt();
            controlTirePressure();
            controlOilPressure();
            controlOilTemperature();
            sendList();
        } catch (Exception ex) {
            Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Yeni bir JFrame başlat
        Warnings newFrame;
        try {
            newFrame = new Warnings();
                    newFrame.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_restartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Warnings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Warnings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Warnings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Warnings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Warnings().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Warnings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        work = oldWork = false;
        handBrake = true;
        isSeated = new boolean[]{false, false, false, false, false};
        seatBelt = new boolean[]{false, false, false, false, false};
        door = new boolean[]{false, false, false, false, false};
        tirePressure = new int[]{32, 32, 32, 32};
        id = 0;

        warningList = new ArrayList() {
        };
        oilPressure = 30;
        oilTemperature = 100;

        c = new Client(3636);
        String message;
        controlDoor();
        controlHandBrake();
        controlSeatBelt();
        controlTirePressure();
        controlOilPressure();
        controlOilTemperature();

        while (true) {
            String msg = c.getMessage();
            //System.out.println(msg);
            String[] msgArr = msg.split("#");
            if (msgArr[0].equalsIgnoreCase("delete")) {
                //System.out.println(msgArr[1]);
                deleteWar(msgArr[1]);
            }

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JCheckBox door0;
    private static javax.swing.JCheckBox door1;
    private static javax.swing.JCheckBox door2;
    private static javax.swing.JCheckBox door3;
    private static javax.swing.JCheckBox door4;
    private static javax.swing.JCheckBox handBrakeCB;
    private static javax.swing.JTextField iListSizeSetter;
    private static javax.swing.JCheckBox isSeat0;
    private static javax.swing.JCheckBox isSeat1;
    private static javax.swing.JCheckBox isSeat2;
    private static javax.swing.JCheckBox isSeat3;
    private static javax.swing.JCheckBox isSeat4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private static javax.swing.JTextField oilP;
    private static javax.swing.JTextField oilT;
    private static javax.swing.JButton restart;
    private static javax.swing.JCheckBox seatBelt0;
    private static javax.swing.JCheckBox seatBelt1;
    private static javax.swing.JCheckBox seatBelt2;
    private static javax.swing.JCheckBox seatBelt3;
    private static javax.swing.JCheckBox seatBelt4;
    private static javax.swing.JTextField speedTextBox;
    private static javax.swing.JButton ss;
    private static javax.swing.JTextField tire0T;
    private static javax.swing.JTextField tire1T;
    private static javax.swing.JTextField tire2T;
    private static javax.swing.JTextField tire3T;
    private static javax.swing.JTextField wListSizeSetter;
    // End of variables declaration//GEN-END:variables
}
