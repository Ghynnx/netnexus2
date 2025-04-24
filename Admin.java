import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author User
 */
public class Admin extends javax.swing.JFrame {

    private Timer autoLogoutTimer;
    private final String filepath = "src\\netnexus.json"; // Update with your JSON file path your JSON file path
// Update with your JSON file path

    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
        startAutoLogoutTimer();
        loadSessionData();
        updateStatistics(); 

    }

private void startAutoLogoutTimer() {
        System.out.println("Starting auto-logout timer...");
        if (autoLogoutTimer != null) {
            autoLogoutTimer.cancel();
            System.out.println("Previous timer canceled.");
        }
        autoLogoutTimer = new Timer(true);
        autoLogoutTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndLogoutUsers();
            }
        }, 0, 1000); // Run every second
        System.out.println("Auto-logout timer started.");
    }

    private void checkAndLogoutUsers() {
        System.out.println("Checking and updating session data...");
        try (FileReader reader = new FileReader(filepath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");

            boolean dataUpdated = false;
            LocalTime currentTime = LocalTime.now();

            for (Object obj : sessions) {
                JSONObject session = (JSONObject) obj;
                Boolean isActive = (Boolean) session.get("active");

                if (isActive == null || !isActive) {
                    continue; // Skip inactive sessions
                }

                String userTimeStr = (String) session.get("userTime");
                String startTimeStr = (String) session.get("startTime");

                if (userTimeStr == null || startTimeStr == null) {
                    System.out.println("Missing time data for session: " + session);
                    continue;
                }

                try {
                    LocalTime startTime = LocalTime.parse(startTimeStr);
                    LocalTime userDuration = LocalTime.MIN.plusSeconds(parseTimeToSeconds(userTimeStr));
                    LocalTime autoLogoutTime = startTime.plusSeconds(userDuration.toSecondOfDay());

                    if (currentTime.isBefore(autoLogoutTime)) {
                        int remainingSeconds = (int) java.time.Duration.between(currentTime, autoLogoutTime).getSeconds();
                        session.put("remainingTime", formatSecondsToTime(remainingSeconds));
                    } else {
                        session.put("active", false);
                        session.put("remainingTime", "00:00:00");
                        System.out.println("Session logged out automatically: PC No = " + session.get("pcNo"));
                        dataUpdated = true;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing session: " + e.getMessage());
                }
            }

            if (dataUpdated) {
                try (FileWriter writer = new FileWriter(filepath)) {
                    writer.write(jsonObject.toJSONString());
                    System.out.println("Session data updated successfully.");
                }
                loadSessionData(); // Reload session data into the table
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error checking session time: " + e.getMessage());
        }
    }

    private void loadSessionData() {
        System.out.println("Loading session data...");
        File file = new File(filepath);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "JSON file not found at: " + filepath, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("JSON file not found at: " + filepath);
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");

            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0); // Clear existing rows

            for (Object obj : sessions) {
                JSONObject session = (JSONObject) obj;

                Integer pcNo = session.get("pcNo") != null ? Integer.valueOf(session.get("pcNo").toString()) : null;
                String username = session.get("username") != null ? session.get("username").toString() : "N/A";
                String remainingTime = session.get("remainingTime") != null ? session.get("remainingTime").toString() : "00:00:00";
                Boolean isActive = (Boolean) session.get("active");

                if (isActive != null && isActive) {
                    tableModel.addRow(new Object[]{pcNo, username, remainingTime});
                    System.out.println("Added session to table: PC No = " + pcNo + ", Username = " + username + ", Remaining Time = " + remainingTime);
                }
            }
            System.out.println("Session data loaded successfully.");
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Error loading session data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error loading session data: " + e.getMessage());
        }
    }

    private void updateStatistics() {
        System.out.println("Updating statistics...");
        double totalRevenue = 0.0;
        int totalUsers = 0;
        int activeSessions = 0;
        String todayDate = getTodayDate(); // Get today's date

        try (FileReader reader = new FileReader(filepath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");
            JSONObject dailyRevenue = (JSONObject) jsonObject.getOrDefault("dailyRevenue", new JSONObject());

            if (sessions != null) {
                totalUsers = sessions.size();

                for (Object obj : sessions) {
                    JSONObject session = (JSONObject) obj;

                    Boolean isActive = (Boolean) session.get("active");
                    if (isActive != null && isActive) {
                        activeSessions++;
                    }

                    try {
                        Double amount = session.get("amount") != null ? Double.valueOf(session.get("amount").toString()) : 0.0;
                        totalRevenue += amount;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid revenue format for session: " + session);
                    }
                }
            }

            double currentDayRevenue = dailyRevenue.getOrDefault(todayDate, 0.0) instanceof Double
                    ? (double) dailyRevenue.get(todayDate)
                    : 0.0;

            dailyRevenue.put(todayDate, currentDayRevenue + totalRevenue);
            jsonObject.put("dailyRevenue", dailyRevenue);

            try (FileWriter writer = new FileWriter(filepath)) {
                writer.write(jsonObject.toJSONString());
                System.out.println("Statistics updated and saved successfully.");
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error updating statistics: " + e.getMessage());
        }

        jTextField1.setText(String.format("%.2f", totalRevenue)); // Total Revenue
        jTextField2.setText(String.valueOf(activeSessions));      // Active Sessions
        jTextField3.setText(String.valueOf(totalUsers));          // Total Sessions
        System.out.println("Statistics updated: Total Revenue = " + totalRevenue + ", Active Sessions = " + activeSessions + ", Total Sessions = " + totalUsers);
    }

    private String getTodayDate() {
        return LocalDate.now().toString();
    }

    private int parseTimeToSeconds(String timeStr) {
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }

    private String formatSecondsToTime(int seconds) {
        return LocalTime.ofSecondOfDay(seconds).toString();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        userBtn = new javax.swing.JButton();
        logoutbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(925, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(925, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userBtn.setText("Manage Users");
        userBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userBtnActionPerformed(evt);
            }
        });
        jPanel1.add(userBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 175, -1));

        logoutbtn.setText("Logout");
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });
        jPanel1.add(logoutbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 160, -1));

        jTable1.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"PC NO", "USERNAME", "TIME"}
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, 600, 170));

        jLabel1.setText("REVENUE");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, -1));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        jLabel3.setText("ACTIVE USERS");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, -1, -1));
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, -1, -1));

        jLabel4.setText("TOTAL USERS");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, -1, -1));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, -1, -1));

        jLabel5.setText("USERNAME");

        jLabel2.setText("AMOUNT");

        jLabel6.setText("TIME");

        jTextField6.setText("jTextField6");

        jButton1.setText("TOP UP");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField5))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 270, 150));

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userBtnActionPerformed
        mgusers b = new mgusers();
        b.setVisible(true);
        dispose();
    }//GEN-LAST:event_userBtnActionPerformed

    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
        new LoginW().setVisible(true);
        dispose();
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Admin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JButton userBtn;
    // End of variables declaration//GEN-END:variables
}





