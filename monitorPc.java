import java.awt.HeadlessException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import java.util.Timer;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class monitorPc extends javax.swing.JFrame {

    private static final String filepath = "C:\\Users\\User\\Documents\\NetBeansProjects\\NetNexus\\src\\netnexus.json";
    private Timer autoLogoutTimer;

    public monitorPc() {
        initComponents();
        loadUserData();
        startAutoLogoutTimer();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        refBtn = new javax.swing.JButton();
        outBtn = new javax.swing.JButton();
        backBtn2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Pc No.", "Username", "Time", "Balance", "Logins", "VIP rank"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        refBtn.setText("Refresh");
        refBtn.addActionListener(evt -> refBtnActionPerformed(evt));

        outBtn.setText("Force Logout");
        outBtn.addActionListener(evt -> outBtnActionPerformed(evt));

        backBtn2.setText("Back");
        backBtn2.addActionListener(evt -> backBtn2ActionPerformed(evt));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(refBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(outBtn))
                        .addContainerGap(12, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(backBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backBtn2)
                        .addGap(118, 118, 118)
                        .addComponent(refBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outBtn)
                        .addContainerGap(101, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }

    private void backBtn2ActionPerformed(java.awt.event.ActionEvent evt) {
        Admin adminScreen = new Admin();
        adminScreen.setVisible(true);
        this.dispose();
    }

    private void outBtnActionPerformed(java.awt.event.ActionEvent evt) {
        forceLogoutUser();
    }

    private void refBtnActionPerformed(java.awt.event.ActionEvent evt) {
        loadUserData();
        JOptionPane.showMessageDialog(this, "Table refreshed successfully.");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new monitorPc().setVisible(true);
        });
    }

    private void startAutoLogoutTimer() {
        if (autoLogoutTimer != null) {
            autoLogoutTimer.cancel();
        }
        autoLogoutTimer = new Timer(true);
        autoLogoutTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkAndLogoutUsers();
                    handleUsersWithZeroRemainingTime();
                } catch (Exception e) {
                    System.err.println("Error in TimerTask: " + e.getMessage());
                }
            }
        }, 0, 1000);
    }

    private void checkAndLogoutUsers() {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                System.err.println("JSON file not found. Skipping logout check.");
                return;
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");

            boolean dataUpdated = false;
            long currentTime = System.currentTimeMillis() / 1000;

            for (Object obj : sessions) {
                JSONObject session = (JSONObject) obj;

                Boolean isActive = (Boolean) session.get("active");
                if (isActive == null || !isActive) {
                    continue;
                }

               

