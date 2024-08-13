/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package radioscreen;

import Socket.Client;
import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class RadioScreen extends javax.swing.JFrame {

    private static javax.swing.Timer volumeDisplayTimer;
    static Client clientScrn;
    final int MAX_Volume = 100, MIN_Volume = 0, MAX_FM = 110, MIN_FM = 90;
    static int volume;
    static double fm;
    static boolean sleep, mute, power;

    ///
    static boolean[] seatBelt = new boolean[5];
    static boolean[] isSeated = new boolean[5];
    static boolean[] isSeated1 = new boolean[5];
    static boolean[] tireProb = new boolean[4];
    static boolean[] door = new boolean[5];
    static int[] tirePressure = new int[4];
    static int[] tirePressure1 = new int[4];

    static boolean oilTB, oilPB, handBrake;
    static int oilPressure, oilTemperature;
    static ArrayList<String> warningList, wList, iList;
    static JLabel[] tires, tires1;
    static JPanel[] tireW, tireW1;
    static JPanel[] seats, seats1;
    static JPanel[] doors, doors1;
    static DefaultTableModel model;
    static boolean sleepReminder;
    static int speed;
    static String[] wMessages;
    static int wListSize, iListSize, wS, iS;

    public RadioScreen() throws Exception {

        warningList = new ArrayList() {
        };
        wList = new ArrayList() {
        };
        iList = new ArrayList() {
        };

        volume = (MAX_Volume + MIN_Volume) / 2;
        fm = (MAX_FM + MIN_FM) / 2;
        sleepReminder = sleep = true;
        mute = true;
        power = true;
        initComponents();
        startTimers();
        setSleep(sleep);
        setMute(mute);
        setFmLabel();
        setVolumeLabel();
        sleepScreen.setVisible(sleep);

        ///
        tires = new JLabel[]{tire0, tire1, tire2, tire3};
        tires1 = new JLabel[]{tire01, tire11, tire6, tire7};

        tireW = new JPanel[]{tire0W, tire1W, tire2W, tire3W};
        tireW1 = new JPanel[]{tire0W1, tire1W1, tire2W1, tire3W1};

        seats = new JPanel[]{seat0, seat1, seat2, seat3, seat4};
        seats1 = new JPanel[]{seat01, seat11, seat21, seat31, seat41};

        doors = new JPanel[]{door0, door1, door2, door3, door4};
        doors1 = new JPanel[]{door5, door6, door7, door8, door9};

        model = new DefaultTableModel();
        wMessages = new String[]{"oilTemperature", "oilPressure", "tirePressure", "test"};

        wListSize = iListSize = 0;
        createTable();
        setWarningScreen();
    }

    private static void functionFinder(String cmd) throws InterruptedException {
        String[] cmdArr = cmd.split(" ");
//fm 200
        switch (cmdArr[0]) {
            case "v":
                setVolume(Integer.parseInt(cmdArr[1]));
                break;
            case "fm":
                setFm(Double.parseDouble(cmdArr[1]));
                break;
            case "s":
                setSleep(Boolean.parseBoolean(cmdArr[1]));
                break;
            case "m":
                setMute(Boolean.parseBoolean(cmdArr[1]));
                break;
            case "p":
                System.exit(0);
                break;
            case "variable":
                setVar(cmdArr[1]);
                break;
            case "Warning":
                if (cmdArr.length > 1) {
                    getWarningList(cmdArr[1]);
                } else {
                    setDefault();
                    getWarningList();
                    setWarningScreen();
                }
                break;
            case "wListSize":
                wS = Integer.parseInt(cmdArr[1]);
                getWarningList();
                break;
            case "iListSize":
                iS = Integer.parseInt(cmdArr[1]);
                getWarningList();
                break;
            default:

                break;
        }

    }

    private static void setVolume(int msg) throws InterruptedException {
        volume = msg;
        setVolumeLabel();
    }

    private static void setFm(double msg) {
        fm = msg;
        setFmLabel();
    }

    private static void setSleep(Boolean b) {
        sleep = b;
        setWarningScreen();
    }

    private static void setDefault() {

        infoPanel.setBorder(null);
        infoPanel1.setBorder(null);
        for (int i = 0; i < seats.length; i++) {

            if (!isSeated[i]) {
                seats[i].setBackground(Color.lightGray);
            } else {
                seats[i].setBackground(Color.green);
            }
        }

        for (int i = 0; i < seats1.length; i++) {

            if (!isSeated[i]) {
                seats1[i].setBackground(Color.lightGray);
            } else {
                seats1[i].setBackground(Color.green);
            }
        }

        for (JPanel dr : doors) {
            dr.setVisible(false);
        }
        for (JPanel dr : doors1) {
            dr.setVisible(false);
        }
        for (JPanel tr : tireW) {
            tr.setVisible(false);
        }
        for (JPanel tr : tireW1) {
            tr.setVisible(false);
        }

        for (JLabel tr : tires) {
            tr.setVisible(true);
        }
        for (JLabel tr : tires1) {
            tr.setVisible(true);
        }

    }

    private static void setWarning() {
        setDefault();
        String wrArr[];

        for (int i = 0; i < warningList.size(); i++) {

            wrArr = warningList.get(i).split("_");

            switch (wrArr[0]) {
                case "seatBelt":

                    seats[Integer.parseInt(wrArr[1])].setBackground(Color.red);
                    seats1[Integer.parseInt(wrArr[1])].setBackground(Color.red);
                    break;

                case "door":

                    doors[Integer.parseInt(wrArr[1])].setVisible(true);
                    doors1[Integer.parseInt(wrArr[1])].setVisible(true);
                    break;

                case "tirePressure":

                    tireW[Integer.parseInt(wrArr[1])].setVisible(true);
                    tireW1[Integer.parseInt(wrArr[1])].setVisible(true);
                    break;
                case "handBrake":
                    infoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 5));
                    infoPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 5));
                    break;

                case "oilPressure":

                    break;
                case "oilTemperature":

                    break;
                default:
                    break;

            }
        }
        setWarningScreen();
    }

    private static void setVar(String message) {
        setDefault();
        String[] infoArr = message.split("#");

        switch (infoArr[0]) {

            case "tirePressure":
                for (int i = 0; i < tirePressure.length; i++) {
                    tirePressure[i] = Integer.parseInt(infoArr[i + 1]);
                    tirePressure1[i] = Integer.parseInt(infoArr[i + 1]);
                    tires[i].setText(infoArr[i + 1]);
                    tires1[i].setText(infoArr[i + 1]);
                }
                break;
            case "oilPressure":
                oilPressure = Integer.parseInt(infoArr[1]);
                break;
            case "oilTemperature":
                oilTemperature = Integer.parseInt(infoArr[1]);
                break;
            case "isSeated":
                for (int i = 0; i < isSeated.length; i++) {
                    isSeated[i] = Boolean.parseBoolean(infoArr[i + 1]);
                    isSeated1[i] = Boolean.parseBoolean(infoArr[i + 1]);
                }
                break;
            case "speed":
                speed = Integer.parseInt(infoArr[1]);
                break;

            default:
                throw new AssertionError();
        }
        setWarning();
    }

    private static void getWarningList(String message) {

        warningList.clear();
        wList.clear();
        wListSize = 0;
        iList.clear();
        iListSize = 0;

        String[] infoArr = message.split("\\$");
        for (String warning : infoArr) {

            warningList.add(warning);

            for (String wMessage : wMessages) {
                if (warning.contains(wMessage) && !wList.contains(warning)) {
                    wList.add(warning);
                    wListSize++;
                } else {
                    if (!iList.contains(warning)) {
                        iList.add(warning);
                        iListSize++;
                    }
                }
            }
        }
        setWList(wS);
        setIList(iS);
        setWarning();
    }

    private static void getWarningList() {

        setWList(wS);
        setIList(iS);
        setWarning();
    }

    private static void setWarningScreen() {

        if (wList != null && !wList.isEmpty()) {
            arrayListToList(wList);

            mainScreen1.setVisible(true);
            mainScreen2.setVisible(false);
            sleepScreen.setVisible(false);
        } else if (iList != null && !iList.isEmpty() || !sleep) {

            mainScreen1.setVisible(false);
            mainScreen2.setVisible(true);
            sleepScreen.setVisible(false);
        } else {

            mainScreen1.setVisible(false);
            mainScreen2.setVisible(false);
            sleepScreen.setVisible(true);
        }
    }

    private static void arrayListToList(ArrayList<String> list) {
        model.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < wMessages.length; j++) {
                if (list.get(i).contains(wMessages[j])) {
                    model.addRow(new Object[]{list.get(i)});
                }
            }
        }
    }

    private static void setWList(int size) {
        System.out.println("size: " + size + " wList.size(): " + wList.size() + " wListSize: " + wListSize);
                System.out.println("size < wList.size(): " + size + " < " + wList.size());

        if (size > wListSize) {
            for (int i = wList.size(); i < size; i++) {
                wList.add("testWarning");
                System.out.println("added");
            }
        }
        else if(size < wList.size()) {
            System.out.println("size: " + size + " wList.size(): " + wList.size() + " wListSize: " + wListSize);
            for (int i = wList.size(); i > wListSize; i--) {
                wList.remove(i);
                System.out.println("remove");
            }
        }

    }

    private static void setIList(int size) {
        if (size > iListSize) {
            for (int i = iList.size(); i < size; i++) {
                iList.add("testInfo");
                System.out.println("added");
            }
        }

    }

    private static void createTable() {

        warningTable.setModel(model);
        //model.addColumn("ID");
        //model.addColumn("Priority");
        model.addColumn("Warning Message");

    }

    private static void setMute(Boolean mt) {
        mute = mt; // Mute durumunu tersine çevir
        String m = (mute) ? "mute" : "open";
        muteLabel.setText(m);
        muteLabel1.setText(m);
    }

    private static void setTimeLabel() {
        Date date = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(date);
        timeLabel.setText(time);
        sleepTime.setText(time);
        timeLabel1.setText(time);

    }

    private static void setDateLabel() {
        Date date = new Date();
        DateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = dFormat.format(date);
        dateLabel.setText(d);
        dateLabel1.setText(d);

        sleepDate.setText(d);
    }

    private static void setFmLabel() {
        mainLabel.setText(String.valueOf(fm));
        mainLabel1.setText(String.valueOf(fm));
    }

    private static void setVolumeLabel() {
        mainLabel.setText("volume " + volume);
        mainLabel1.setText("volume " + volume);
        // Cancel the previous timer if it exists
        if (volumeDisplayTimer != null) {
            volumeDisplayTimer.stop();
        }

        // Create a new timer that reverts to FM value after 1 second
        volumeDisplayTimer = new javax.swing.Timer(1000, e -> {
            setFmLabel();
        });
        volumeDisplayTimer.setRepeats(false); // Ensure it only runs once
        volumeDisplayTimer.start();
    }

    private void startTimers() {
        Timer timer = new Timer(1000, e -> {
            setTimeLabel();
            setDateLabel();
        });
        timer.start();
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
        mainScreen2 = new javax.swing.JPanel();
        muteLabel1 = new javax.swing.JLabel();
        timeLabel1 = new javax.swing.JLabel();
        mainLabel1 = new javax.swing.JLabel();
        dateLabel1 = new javax.swing.JLabel();
        infoPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        seat01 = new javax.swing.JPanel();
        seat11 = new javax.swing.JPanel();
        seat21 = new javax.swing.JPanel();
        seat31 = new javax.swing.JPanel();
        seat41 = new javax.swing.JPanel();
        tire01 = new javax.swing.JLabel();
        tire11 = new javax.swing.JLabel();
        tire6 = new javax.swing.JLabel();
        tire7 = new javax.swing.JLabel();
        tire2W1 = new javax.swing.JPanel();
        tire1W1 = new javax.swing.JPanel();
        tire0W1 = new javax.swing.JPanel();
        tire3W1 = new javax.swing.JPanel();
        door5 = new javax.swing.JPanel();
        door6 = new javax.swing.JPanel();
        door7 = new javax.swing.JPanel();
        door8 = new javax.swing.JPanel();
        door9 = new javax.swing.JPanel();
        sleepScreen = new javax.swing.JPanel();
        sleepTime = new javax.swing.JLabel();
        sleepDate = new javax.swing.JLabel();
        mainScreen1 = new javax.swing.JPanel();
        muteLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        mainLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        infoPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        seat0 = new javax.swing.JPanel();
        seat1 = new javax.swing.JPanel();
        seat2 = new javax.swing.JPanel();
        seat3 = new javax.swing.JPanel();
        seat4 = new javax.swing.JPanel();
        tire0 = new javax.swing.JLabel();
        tire1 = new javax.swing.JLabel();
        tire2 = new javax.swing.JLabel();
        tire3 = new javax.swing.JLabel();
        tire2W = new javax.swing.JPanel();
        tire1W = new javax.swing.JPanel();
        tire0W = new javax.swing.JPanel();
        tire3W = new javax.swing.JPanel();
        door0 = new javax.swing.JPanel();
        door1 = new javax.swing.JPanel();
        door2 = new javax.swing.JPanel();
        door4 = new javax.swing.JPanel();
        door3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        warningTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 455));
        setMinimumSize(new java.awt.Dimension(800, 455));
        setPreferredSize(new java.awt.Dimension(800, 455));
        setResizable(false);

        jPanel1.setLayout(new java.awt.CardLayout());

        mainScreen2.setBackground(new java.awt.Color(169, 144, 126));

        muteLabel1.setBackground(new java.awt.Color(169, 144, 126));
        muteLabel1.setForeground(new java.awt.Color(171, 196, 170));
        muteLabel1.setLabelFor(muteLabel);
        muteLabel1.setOpaque(true);

        timeLabel1.setBackground(new java.awt.Color(243, 222, 186));
        timeLabel1.setFont(new java.awt.Font("Leelawadee UI", 1, 18)); // NOI18N
        timeLabel1.setForeground(new java.awt.Color(171, 196, 170));
        timeLabel1.setText("jLabel2");
        timeLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        mainLabel1.setBackground(new java.awt.Color(243, 222, 186));
        mainLabel1.setFont(new java.awt.Font("Leelawadee UI", 1, 24)); // NOI18N
        mainLabel1.setForeground(new java.awt.Color(171, 196, 170));
        mainLabel1.setText("100.0");
        mainLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, new java.awt.Color(166, 144, 126), null, null));
        mainLabel1.setFocusable(false);
        mainLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mainLabel1.setOpaque(true);

        dateLabel1.setBackground(new java.awt.Color(243, 222, 186));
        dateLabel1.setFont(new java.awt.Font("Leelawadee UI", 1, 18)); // NOI18N
        dateLabel1.setForeground(new java.awt.Color(171, 196, 170));
        dateLabel1.setText("jLabel1");
        dateLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        infoPanel1.setBackground(new java.awt.Color(200, 200, 200));
        infoPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 5));
        infoPanel1.setOpaque(false);

        jPanel3.setBackground(new java.awt.Color(236, 227, 206));

        javax.swing.GroupLayout seat01Layout = new javax.swing.GroupLayout(seat01);
        seat01.setLayout(seat01Layout);
        seat01Layout.setHorizontalGroup(
            seat01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        seat01Layout.setVerticalGroup(
            seat01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat11Layout = new javax.swing.GroupLayout(seat11);
        seat11.setLayout(seat11Layout);
        seat11Layout.setHorizontalGroup(
            seat11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        seat11Layout.setVerticalGroup(
            seat11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat21Layout = new javax.swing.GroupLayout(seat21);
        seat21.setLayout(seat21Layout);
        seat21Layout.setHorizontalGroup(
            seat21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        seat21Layout.setVerticalGroup(
            seat21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat31Layout = new javax.swing.GroupLayout(seat31);
        seat31.setLayout(seat31Layout);
        seat31Layout.setHorizontalGroup(
            seat31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );
        seat31Layout.setVerticalGroup(
            seat31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        seat41.setMaximumSize(new java.awt.Dimension(12, 12));
        seat41.setMinimumSize(new java.awt.Dimension(12, 12));
        seat41.setPreferredSize(new java.awt.Dimension(12, 12));

        javax.swing.GroupLayout seat41Layout = new javax.swing.GroupLayout(seat41);
        seat41.setLayout(seat41Layout);
        seat41Layout.setHorizontalGroup(
            seat41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        seat41Layout.setVerticalGroup(
            seat41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(seat01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(seat11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(seat21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(seat31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(seat41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seat01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seat11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seat21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seat41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seat31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tire01.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        tire01.setMaximumSize(new java.awt.Dimension(12, 12));
        tire01.setMinimumSize(new java.awt.Dimension(12, 12));
        tire01.setPreferredSize(new java.awt.Dimension(12, 12));

        tire11.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        tire11.setMaximumSize(new java.awt.Dimension(12, 12));
        tire11.setMinimumSize(new java.awt.Dimension(12, 12));
        tire11.setPreferredSize(new java.awt.Dimension(12, 12));

        tire6.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        tire6.setMaximumSize(new java.awt.Dimension(12, 12));
        tire6.setMinimumSize(new java.awt.Dimension(12, 12));
        tire6.setPreferredSize(new java.awt.Dimension(12, 12));

        tire7.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        tire7.setMaximumSize(new java.awt.Dimension(12, 12));
        tire7.setMinimumSize(new java.awt.Dimension(12, 12));
        tire7.setPreferredSize(new java.awt.Dimension(12, 12));

        tire2W1.setBackground(new java.awt.Color(255, 0, 51));
        tire2W1.setToolTipText("");
        tire2W1.setMaximumSize(new java.awt.Dimension(12, 12));
        tire2W1.setMinimumSize(new java.awt.Dimension(12, 12));

        javax.swing.GroupLayout tire2W1Layout = new javax.swing.GroupLayout(tire2W1);
        tire2W1.setLayout(tire2W1Layout);
        tire2W1Layout.setHorizontalGroup(
            tire2W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        tire2W1Layout.setVerticalGroup(
            tire2W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        tire1W1.setBackground(new java.awt.Color(255, 0, 51));
        tire1W1.setMaximumSize(new java.awt.Dimension(12, 12));
        tire1W1.setMinimumSize(new java.awt.Dimension(12, 12));

        javax.swing.GroupLayout tire1W1Layout = new javax.swing.GroupLayout(tire1W1);
        tire1W1.setLayout(tire1W1Layout);
        tire1W1Layout.setHorizontalGroup(
            tire1W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        tire1W1Layout.setVerticalGroup(
            tire1W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        tire0W1.setBackground(new java.awt.Color(255, 0, 51));
        tire0W1.setMaximumSize(new java.awt.Dimension(12, 12));
        tire0W1.setMinimumSize(new java.awt.Dimension(12, 12));

        javax.swing.GroupLayout tire0W1Layout = new javax.swing.GroupLayout(tire0W1);
        tire0W1.setLayout(tire0W1Layout);
        tire0W1Layout.setHorizontalGroup(
            tire0W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        tire0W1Layout.setVerticalGroup(
            tire0W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tire3W1.setBackground(new java.awt.Color(255, 0, 51));
        tire3W1.setMaximumSize(new java.awt.Dimension(12, 12));
        tire3W1.setMinimumSize(new java.awt.Dimension(12, 12));

        javax.swing.GroupLayout tire3W1Layout = new javax.swing.GroupLayout(tire3W1);
        tire3W1.setLayout(tire3W1Layout);
        tire3W1Layout.setHorizontalGroup(
            tire3W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        tire3W1Layout.setVerticalGroup(
            tire3W1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        door5.setBackground(new java.awt.Color(255, 102, 51));
        door5.setMaximumSize(new java.awt.Dimension(4, 20));
        door5.setMinimumSize(new java.awt.Dimension(4, 20));
        door5.setPreferredSize(new java.awt.Dimension(4, 20));

        javax.swing.GroupLayout door5Layout = new javax.swing.GroupLayout(door5);
        door5.setLayout(door5Layout);
        door5Layout.setHorizontalGroup(
            door5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        door5Layout.setVerticalGroup(
            door5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        door6.setBackground(new java.awt.Color(255, 102, 51));
        door6.setMaximumSize(new java.awt.Dimension(4, 20));
        door6.setMinimumSize(new java.awt.Dimension(4, 20));
        door6.setPreferredSize(new java.awt.Dimension(4, 20));

        javax.swing.GroupLayout door6Layout = new javax.swing.GroupLayout(door6);
        door6.setLayout(door6Layout);
        door6Layout.setHorizontalGroup(
            door6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        door6Layout.setVerticalGroup(
            door6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        door7.setBackground(new java.awt.Color(255, 102, 51));
        door7.setMaximumSize(new java.awt.Dimension(4, 20));
        door7.setMinimumSize(new java.awt.Dimension(4, 20));
        door7.setPreferredSize(new java.awt.Dimension(4, 20));

        javax.swing.GroupLayout door7Layout = new javax.swing.GroupLayout(door7);
        door7.setLayout(door7Layout);
        door7Layout.setHorizontalGroup(
            door7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        door7Layout.setVerticalGroup(
            door7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        door8.setBackground(new java.awt.Color(255, 102, 51));
        door8.setPreferredSize(new java.awt.Dimension(67, 4));

        javax.swing.GroupLayout door8Layout = new javax.swing.GroupLayout(door8);
        door8.setLayout(door8Layout);
        door8Layout.setHorizontalGroup(
            door8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 67, Short.MAX_VALUE)
        );
        door8Layout.setVerticalGroup(
            door8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        door9.setBackground(new java.awt.Color(255, 102, 51));
        door9.setMaximumSize(new java.awt.Dimension(4, 20));
        door9.setMinimumSize(new java.awt.Dimension(4, 20));
        door9.setPreferredSize(new java.awt.Dimension(4, 20));

        javax.swing.GroupLayout door9Layout = new javax.swing.GroupLayout(door9);
        door9.setLayout(door9Layout);
        door9Layout.setHorizontalGroup(
            door9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        door9Layout.setVerticalGroup(
            door9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout infoPanel1Layout = new javax.swing.GroupLayout(infoPanel1);
        infoPanel1.setLayout(infoPanel1Layout);
        infoPanel1Layout.setHorizontalGroup(
            infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addComponent(tire0W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tire01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addComponent(tire2W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tire6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(door5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(door7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(door8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(door9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(door6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tire11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tire1W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tire7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tire3W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        infoPanel1Layout.setVerticalGroup(
            infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanel1Layout.createSequentialGroup()
                        .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tire1W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tire11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tire7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tire3W1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(infoPanel1Layout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addComponent(door5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(door7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(17, 17, 17))
                        .addGroup(infoPanel1Layout.createSequentialGroup()
                            .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, infoPanel1Layout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(door6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(door9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)))
                            .addGap(0, 0, 0)
                            .addComponent(door8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(infoPanel1Layout.createSequentialGroup()
                            .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tire0W1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tire01, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(infoPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tire6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tire2W1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainScreen2Layout = new javax.swing.GroupLayout(mainScreen2);
        mainScreen2.setLayout(mainScreen2Layout);
        mainScreen2Layout.setHorizontalGroup(
            mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainScreen2Layout.createSequentialGroup()
                .addGroup(mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainScreen2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(muteLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(mainScreen2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(infoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainScreen2Layout.createSequentialGroup()
                        .addGap(452, 452, 452)
                        .addGroup(mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(mainLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        mainScreen2Layout.setVerticalGroup(
            mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainScreen2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainScreen2Layout.createSequentialGroup()
                        .addComponent(dateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(infoPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(mainScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainScreen2Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(muteLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 142, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainScreen2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mainLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))))
        );

        jPanel1.add(mainScreen2, "card2");

        sleepScreen.setBackground(new java.awt.Color(169, 144, 126));
        sleepScreen.setPreferredSize(new java.awt.Dimension(200, 450));

        sleepTime.setFont(new java.awt.Font("Leelawadee UI", 1, 48)); // NOI18N
        sleepTime.setForeground(new java.awt.Color(171, 196, 170));

        sleepDate.setFont(new java.awt.Font("Leelawadee UI", 1, 48)); // NOI18N
        sleepDate.setForeground(new java.awt.Color(171, 196, 170));

        javax.swing.GroupLayout sleepScreenLayout = new javax.swing.GroupLayout(sleepScreen);
        sleepScreen.setLayout(sleepScreenLayout);
        sleepScreenLayout.setHorizontalGroup(
            sleepScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sleepScreenLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(sleepScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sleepDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sleepTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(763, Short.MAX_VALUE))
        );
        sleepScreenLayout.setVerticalGroup(
            sleepScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sleepScreenLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(sleepTime, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sleepDate, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(313, Short.MAX_VALUE))
        );

        jPanel1.add(sleepScreen, "card3");

        mainScreen1.setBackground(new java.awt.Color(169, 144, 126));

        muteLabel.setBackground(new java.awt.Color(169, 144, 126));
        muteLabel.setForeground(new java.awt.Color(171, 196, 170));
        muteLabel.setLabelFor(muteLabel);
        muteLabel.setOpaque(true);

        timeLabel.setBackground(new java.awt.Color(243, 222, 186));
        timeLabel.setFont(new java.awt.Font("Leelawadee UI", 1, 18)); // NOI18N
        timeLabel.setForeground(new java.awt.Color(171, 196, 170));
        timeLabel.setText("jLabel2");
        timeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        mainLabel.setBackground(new java.awt.Color(243, 222, 186));
        mainLabel.setFont(new java.awt.Font("Leelawadee UI", 1, 24)); // NOI18N
        mainLabel.setForeground(new java.awt.Color(171, 196, 170));
        mainLabel.setText("100.0");
        mainLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, new java.awt.Color(166, 144, 126), null, null));
        mainLabel.setFocusable(false);
        mainLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mainLabel.setOpaque(true);

        dateLabel.setBackground(new java.awt.Color(243, 222, 186));
        dateLabel.setFont(new java.awt.Font("Leelawadee UI", 1, 18)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(171, 196, 170));
        dateLabel.setText("jLabel1");
        dateLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        infoPanel.setBackground(new java.awt.Color(200, 200, 200));
        infoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 5));
        infoPanel.setOpaque(false);

        jPanel2.setBackground(new java.awt.Color(236, 227, 206));

        javax.swing.GroupLayout seat0Layout = new javax.swing.GroupLayout(seat0);
        seat0.setLayout(seat0Layout);
        seat0Layout.setHorizontalGroup(
            seat0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        seat0Layout.setVerticalGroup(
            seat0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat1Layout = new javax.swing.GroupLayout(seat1);
        seat1.setLayout(seat1Layout);
        seat1Layout.setHorizontalGroup(
            seat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        seat1Layout.setVerticalGroup(
            seat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat2Layout = new javax.swing.GroupLayout(seat2);
        seat2.setLayout(seat2Layout);
        seat2Layout.setHorizontalGroup(
            seat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        seat2Layout.setVerticalGroup(
            seat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat3Layout = new javax.swing.GroupLayout(seat3);
        seat3.setLayout(seat3Layout);
        seat3Layout.setHorizontalGroup(
            seat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        seat3Layout.setVerticalGroup(
            seat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat4Layout = new javax.swing.GroupLayout(seat4);
        seat4.setLayout(seat4Layout);
        seat4Layout.setHorizontalGroup(
            seat4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        seat4Layout.setVerticalGroup(
            seat4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(seat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seat0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(seat3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seat4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seat1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(seat1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seat0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(52, 52, 52)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(seat4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seat3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tire0.setText("35");

        tire1.setText("35");

        tire2.setText("35");

        tire3.setText("35");

        tire2W.setBackground(new java.awt.Color(255, 0, 51));
        tire2W.setToolTipText("");

        javax.swing.GroupLayout tire2WLayout = new javax.swing.GroupLayout(tire2W);
        tire2W.setLayout(tire2WLayout);
        tire2WLayout.setHorizontalGroup(
            tire2WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        tire2WLayout.setVerticalGroup(
            tire2WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        tire1W.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout tire1WLayout = new javax.swing.GroupLayout(tire1W);
        tire1W.setLayout(tire1WLayout);
        tire1WLayout.setHorizontalGroup(
            tire1WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        tire1WLayout.setVerticalGroup(
            tire1WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        tire0W.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout tire0WLayout = new javax.swing.GroupLayout(tire0W);
        tire0W.setLayout(tire0WLayout);
        tire0WLayout.setHorizontalGroup(
            tire0WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        tire0WLayout.setVerticalGroup(
            tire0WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tire3W.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout tire3WLayout = new javax.swing.GroupLayout(tire3W);
        tire3W.setLayout(tire3WLayout);
        tire3WLayout.setHorizontalGroup(
            tire3WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );
        tire3WLayout.setVerticalGroup(
            tire3WLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        door0.setBackground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout door0Layout = new javax.swing.GroupLayout(door0);
        door0.setLayout(door0Layout);
        door0Layout.setHorizontalGroup(
            door0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        door0Layout.setVerticalGroup(
            door0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        door1.setBackground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout door1Layout = new javax.swing.GroupLayout(door1);
        door1.setLayout(door1Layout);
        door1Layout.setHorizontalGroup(
            door1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        door1Layout.setVerticalGroup(
            door1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        door2.setBackground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout door2Layout = new javax.swing.GroupLayout(door2);
        door2.setLayout(door2Layout);
        door2Layout.setHorizontalGroup(
            door2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        door2Layout.setVerticalGroup(
            door2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        door4.setBackground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout door4Layout = new javax.swing.GroupLayout(door4);
        door4.setLayout(door4Layout);
        door4Layout.setHorizontalGroup(
            door4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        door4Layout.setVerticalGroup(
            door4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        door3.setBackground(new java.awt.Color(255, 102, 51));

        javax.swing.GroupLayout door3Layout = new javax.swing.GroupLayout(door3);
        door3.setLayout(door3Layout);
        door3Layout.setHorizontalGroup(
            door3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        door3Layout.setVerticalGroup(
            door3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 39, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(door2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(door0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(tire0W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(tire2W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tire2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tire0, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(door4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(door1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(door3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tire3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tire1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tire1W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tire3W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(tire1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(door1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(door3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tire3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tire3W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, infoPanelLayout.createSequentialGroup()
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(infoPanelLayout.createSequentialGroup()
                                        .addComponent(tire1W, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(63, 63, 63))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                                        .addComponent(door0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)))
                                .addComponent(door2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(0, 0, 0)
                        .addComponent(door4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tire0W, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tire0, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tire2W, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tire2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        warningTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        warningTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                warningTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(warningTable);

        javax.swing.GroupLayout mainScreen1Layout = new javax.swing.GroupLayout(mainScreen1);
        mainScreen1.setLayout(mainScreen1Layout);
        mainScreen1Layout.setHorizontalGroup(
            mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainScreen1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainScreen1Layout.createSequentialGroup()
                        .addGroup(mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(66, 66, 66)
                        .addGroup(mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(muteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );
        mainScreen1Layout.setVerticalGroup(
            mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainScreen1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainScreen1Layout.createSequentialGroup()
                        .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(mainScreen1Layout.createSequentialGroup()
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addGroup(mainScreen1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mainLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(muteLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel1.add(mainScreen1, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void warningTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warningTableMouseClicked
        int clicked = warningTable.getSelectedRow();
        String msg = (String) warningTable.getValueAt(clicked, 0);
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete the warning?", "",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            try {

                clientScrn.sendCommand("delete#" + msg, 3636);
                if (iList.contains(warningList.get(clicked))) {
                    iList.remove(warningList.get(clicked));
                }
                if (wList.contains(warningList.get(clicked))) {
                    wList.remove(warningList.get(clicked));
                }

                warningList.remove(clicked);
                setWarningScreen();
            } catch (IOException ex) {
                Logger.getLogger(RadioScreen.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_warningTableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, Exception {
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
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RadioScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new RadioScreen().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(RadioScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        clientScrn = new Client(2828);

        while (true) {
            functionFinder(clientScrn.getMessage());
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel dateLabel;
    private static javax.swing.JLabel dateLabel1;
    private static javax.swing.JPanel door0;
    private static javax.swing.JPanel door1;
    private static javax.swing.JPanel door2;
    private static javax.swing.JPanel door3;
    private static javax.swing.JPanel door4;
    private static javax.swing.JPanel door5;
    private static javax.swing.JPanel door6;
    private static javax.swing.JPanel door7;
    private static javax.swing.JPanel door8;
    private static javax.swing.JPanel door9;
    private static javax.swing.JPanel infoPanel;
    private static javax.swing.JPanel infoPanel1;
    private static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JLabel mainLabel;
    private static javax.swing.JLabel mainLabel1;
    private static javax.swing.JPanel mainScreen1;
    private static javax.swing.JPanel mainScreen2;
    private static javax.swing.JLabel muteLabel;
    private static javax.swing.JLabel muteLabel1;
    private static javax.swing.JPanel seat0;
    private static javax.swing.JPanel seat01;
    private static javax.swing.JPanel seat1;
    private static javax.swing.JPanel seat11;
    private static javax.swing.JPanel seat2;
    private static javax.swing.JPanel seat21;
    private static javax.swing.JPanel seat3;
    private static javax.swing.JPanel seat31;
    private static javax.swing.JPanel seat4;
    private static javax.swing.JPanel seat41;
    private static javax.swing.JLabel sleepDate;
    private static javax.swing.JPanel sleepScreen;
    private static javax.swing.JLabel sleepTime;
    private static javax.swing.JLabel timeLabel;
    private static javax.swing.JLabel timeLabel1;
    private static javax.swing.JLabel tire0;
    private static javax.swing.JLabel tire01;
    private static javax.swing.JPanel tire0W;
    private static javax.swing.JPanel tire0W1;
    private static javax.swing.JLabel tire1;
    private static javax.swing.JLabel tire11;
    private static javax.swing.JPanel tire1W;
    private static javax.swing.JPanel tire1W1;
    private static javax.swing.JLabel tire2;
    private static javax.swing.JPanel tire2W;
    private static javax.swing.JPanel tire2W1;
    private static javax.swing.JLabel tire3;
    private static javax.swing.JPanel tire3W;
    private static javax.swing.JPanel tire3W1;
    private static javax.swing.JLabel tire6;
    private static javax.swing.JLabel tire7;
    private static javax.swing.JTable warningTable;
    // End of variables declaration//GEN-END:variables
}
