
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author User
 */
public class Admin extends javax.swing.JFrame {

    private javax.swing.Timer dynamicTimer;
    private final String filepath = "src\\netnexus.json"; // Update with your JSON file path
       private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());

// Example: Replace System.out.println with LOGGER
public Admin() {
    initComponents();
    LOGGER.info("Initializing components.");
    startAutoLogoutCheck();
    loadSessionData();
    updateStatistics();
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

            // Initialize table model
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0); // Clear existing rows

            for (Object obj : sessions) {
                JSONObject session = (JSONObject) obj;

                // Safely extract session data
                Integer pcNo = session.get("pcNo") != null ? Integer.valueOf(session.get("pcNo").toString()) : null;
                String username = session.get("username") != null ? session.get("username").toString() : "N/A";
                String remainingTime = session.get("remainingTime") != null ? session.get("remainingTime").toString() : "00:00:00";
                Boolean isActive = (Boolean) session.get("active");

                // Add only active sessions to the JTable
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

        // Start the timer for dynamic updates
        startDynamicTimer();
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

            Object todayRevenueObj = dailyRevenue.get(todayDate);
            double currentDayRevenue = 0.0;
            if (todayRevenueObj instanceof Number) {
                currentDayRevenue = ((Number) todayRevenueObj).doubleValue();
            }

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
        usnTop = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        amtTop = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        timeTop = new javax.swing.JTextField();
        tpBtn = new javax.swing.JButton();

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

        tpBtn.setText("TOP UP");
        tpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tpBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(usnTop, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(amtTop))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeTop, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tpBtn))
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
                    .addComponent(usnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amtTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tpBtn))
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
        
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void tpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tpBtnActionPerformed
    processTopUp();        // TODO add your handling code here:
    }//GEN-LAST:event_tpBtnActionPerformed

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



    private void startDynamicTimer() {
    // Create a timer that updates the remaining time every second
    dynamicTimer = new javax.swing.Timer(1000, e -> {
        updateRemainingTime(); // Update countdown for each active session
    });
    dynamicTimer.start(); // Start the timer
}

private void updateRemainingTime() {
    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        String remainingTimeStr = (String) tableModel.getValueAt(i, 2); // Get remaining time
        LOGGER.log(Level.INFO, "Processing row {0} with remaining time: {1}", new Object[]{i, remainingTimeStr});

        try {
            LocalTime remainingTime = LocalTime.parse(remainingTimeStr); // Parse time
            LocalTime updatedTime = remainingTime.minusSeconds(1); // Subtract 1 second

            if (!updatedTime.isBefore(LocalTime.of(0, 0, 0))) { // If time is still valid
                String formattedTime = updatedTime.toString();
                tableModel.setValueAt(formattedTime, i, 2); // Update JTable
                LOGGER.log(Level.INFO, "Updated remaining time for row {0}: {1}", new Object[]{i, formattedTime});
            } else {
                tableModel.setValueAt("00:00:00", i, 2); // Set to zero if expired
                LOGGER.log(Level.WARNING, "Session expired for row {0}", i);
                removeSessionFromTableAndJSON(i, (Integer) tableModel.getValueAt(i, 0)); // Remove session
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing remaining time for row {0}: {1}", new Object[]{i, e.getMessage()});
        }
    }
}
// Remove session from JTable and mark as inactive in JSON


    private void startAutoLogoutCheck() {
        // Create a timer that runs every second
        dynamicTimer = new javax.swing.Timer(1000, e -> {
            updateRemainingTime(); // Update countdown for each active session
            checkAndLogoutExpiredSessions(); // Handle auto-logout for expired sessions
        });
        dynamicTimer.start();
    }

    private void checkAndLogoutExpiredSessions() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) { // Iterate in reverse to avoid index shifting
            String remainingTimeStr = (String) tableModel.getValueAt(i, 2);

            if ("00:00:00".equals(remainingTimeStr)) {
                Integer pcNo = (Integer) tableModel.getValueAt(i, 0); // Get PC Number
                removeSessionFromTableAndJSON(i, pcNo); // Remove session from JTable and JSON
            }
        }
    }

   private void removeSessionFromTableAndJSON(int rowIndex, Integer pcNo) {
    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
    tableModel.removeRow(rowIndex); // Remove the row from JTable

    // Update the JSON file
    try (FileReader reader = new FileReader(filepath)) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        JSONArray sessions = (JSONArray) jsonObject.get("sessions");

        // Find and update the session in the JSON array
        for (Object obj : sessions) {
            JSONObject session = (JSONObject) obj;
            if (session.get("pcNo") != null && session.get("pcNo").toString().equals(pcNo.toString())) {
                session.put("active", false); // Mark as inactive
                session.put("remainingTime", "00:00:00"); // Set remaining time to "00:00:00"
                break;
            }
        }

        // Write the updated JSON back to the file
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(jsonObject.toJSONString());
            LOGGER.info("Updated JSON after session expiration for PC No " + pcNo);
        }
    } catch (IOException | ParseException e) {
        LOGGER.severe("Error updating JSON file for PC No " + pcNo + ": " + e.getMessage());
    }
}
   
  
   private void processTopUp() {
    String username = usnTop.getText(); // Username field
    String amountText = amtTop.getText(); // Amount field
    final double RATE_PER_HOUR = 20.0; // Rate per hour

    // Validate input
    if (username.isEmpty() || amountText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double amount;
    try {
        amount = Double.parseDouble(amountText);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Calculate time equivalent in HH:mm:ss format
    int totalSeconds = (int) ((amount / RATE_PER_HOUR) * 3600);
    String timeEquivalent = String.format("%02d:%02d:%02d", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);

    // Display calculated time equivalent in the time text field
    timeTop.setText(timeEquivalent);

    File file = new File(filepath);

    if (!file.exists()) {
        JOptionPane.showMessageDialog(this, "JSON file not found at: " + filepath, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (FileReader reader = new FileReader(file)) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        JSONArray sessions = (JSONArray) jsonObject.get("sessions");

        boolean userFound = false;

        for (Object obj : sessions) {
            JSONObject session = (JSONObject) obj;

            if (session.get("username") != null && session.get("username").toString().equals(username)) {
                // Update balance
                Double currentBalance = session.get("balance") != null ? Double.valueOf(session.get("balance").toString()) : 0.0;
                session.put("balance", currentBalance + amount);

                // Update remaining time
                String currentRemainingTime = session.get("remainingTime") != null ? session.get("remainingTime").toString() : "00:00:00";
                LocalTime currentTime = LocalTime.parse(currentRemainingTime);
                LocalTime incrementTime = LocalTime.parse(timeEquivalent);
                LocalTime updatedTime = currentTime.plusHours(incrementTime.getHour())
                        .plusMinutes(incrementTime.getMinute())
                        .plusSeconds(incrementTime.getSecond());
                session.put("remainingTime", updatedTime.toString());

                // Update JTable
                DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Object usernameObj = tableModel.getValueAt(i, 1); // Column index for username
                    if (usernameObj != null && usernameObj.toString().equals(username)) {
                        tableModel.setValueAt(updatedTime.toString(), i, 2); // Update the "Remaining Time" column
                        break;
                    }
                }

                userFound = true;
                break;
            }
        }

        if (!userFound) {
            JOptionPane.showMessageDialog(this, "User not found: " + username, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Write updated JSON back to the file
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(jsonObject.toJSONString());
            JOptionPane.showMessageDialog(this, "User topped up successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (IOException | ParseException e) {
        JOptionPane.showMessageDialog(this, "Error processing top-up: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amtTop;
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
    private javax.swing.JButton logoutbtn;
    private javax.swing.JTextField timeTop;
    private javax.swing.JButton tpBtn;
    private javax.swing.JButton userBtn;
    private javax.swing.JTextField usnTop;
    // End of variables declaration//GEN-END:variables
}
