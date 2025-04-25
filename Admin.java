import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.time.format.DateTimeFormatter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author User
 */
public class Admin extends javax.swing.JFrame {

    private static final String FILE_PATH = "src\\netnexus.json";
    private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());
    private static final double RATE_PER_HOUR = 20.0;

    private javax.swing.Timer dynamicTimer;

    public Admin() {
        initComponents();
        LOGGER.info("Initializing components.");
        loadSessionData();
        startAutoLogoutCheck();
         updateStatistics();
    }

    /**
     * Loads session data from the JSON file and populates the JTable.
     */
    private void loadSessionData() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            showError("JSON file not found at: " + FILE_PATH);
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

                Integer pcNo = getInteger(session, "pcNo");
                String username = getString(session, "username", "N/A");
                String remainingTime = getString(session, "remainingTime", "00:00:00");
                Boolean isActive = (Boolean) session.get("active");

                if (Boolean.TRUE.equals(isActive)) {
                    tableModel.addRow(new Object[]{pcNo, username, remainingTime});
                }
            }
        } catch (IOException | ParseException e) {
            showError("Error loading session data: " + e.getMessage());
        }
    }

    /**
     * Starts the timer to handle auto-logout and session updates.
     */
    private void startAutoLogoutCheck() {
        dynamicTimer = new javax.swing.Timer(1000, e -> {
            updateRemainingTime();
            checkAndLogoutExpiredSessions();
        });
        dynamicTimer.start();
    }

    /**
     * Updates the remaining time of active sessions in the JTable.
     */
    private void updateRemainingTime() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String remainingTimeStr = (String) tableModel.getValueAt(i, 2);

            try {
                LocalTime remainingTime = LocalTime.parse(remainingTimeStr).minusSeconds(1);

                if (remainingTime.isBefore(LocalTime.of(0, 0, 0))) {
                    tableModel.setValueAt("00:00:00", i, 2);
                } else {
                    tableModel.setValueAt(remainingTime.toString(), i, 2);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating remaining time for row {0}: {1}", new Object[]{i, e.getMessage()});
            }
        }
    }

    /**
     * Checks for expired sessions and removes them.
     */
    private void checkAndLogoutExpiredSessions() {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            String remainingTimeStr = (String) tableModel.getValueAt(i, 2);

            if ("00:00:00".equals(remainingTimeStr)) {
                Integer pcNo = (Integer) tableModel.getValueAt(i, 0);
                removeSessionFromJSON(pcNo);
                tableModel.removeRow(i);
            }
        }
    }

    /**
     * Removes a session from the JSON file by PC number.
     */
    private void removeSessionFromJSON(Integer pcNo) {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");

            sessions.removeIf(obj -> {
                JSONObject session = (JSONObject) obj;
                return getInteger(session, "pcNo").equals(pcNo);
            });

            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(jsonObject.toJSONString());
            }
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, "Error removing session for PC No {0}: {1}", new Object[]{pcNo, e.getMessage()});
        }
    }

    /**
     * Processes a top-up for a user.
     */
    private void processTopUp() {
        String username = usnTop.getText();
        String amountText = amtTop.getText();

        if (username.isEmpty() || amountText.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            int totalSeconds = (int) ((amount / RATE_PER_HOUR) * 3600);
            String timeEquivalent = formatTime(totalSeconds);

            timeTop.setText(timeEquivalent);

            updateSession(username, amount, timeEquivalent);
        } catch (NumberFormatException e) {
            showError("Invalid amount entered.");
        }
    }

    /**
     * Updates a user's session in the JSON file and JTable.
     */
    private void updateSession(String username, double amount, String timeEquivalent) {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray sessions = (JSONArray) jsonObject.get("sessions");

            for (Object obj : sessions) {
                JSONObject session = (JSONObject) obj;

                if (getString(session, "username", "").equals(username)) {
                    Double currentBalance = getDouble(session, "balance", 0.0);
                    session.put("balance", currentBalance + amount);

                    String currentRemainingTime = getString(session, "remainingTime", "00:00:00");
                    LocalTime updatedTime = LocalTime.parse(currentRemainingTime).plusSeconds(parseSeconds(timeEquivalent));
                    session.put("remainingTime", updatedTime.toString());

                    updateJTable(username, updatedTime.toString());
                    break;
                }
            }

            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(jsonObject.toJSONString());
                JOptionPane.showMessageDialog(this, "User topped up successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | ParseException e) {
            showError("Error updating session: " + e.getMessage());
        }
    }

    /**
     * Updates the JTable with the new remaining time for a user.
     */
    private void updateJTable(String username, String updatedTime) {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (username.equals(tableModel.getValueAt(i, 1))) {
                tableModel.setValueAt(updatedTime, i, 2);
                break;
            }
        }
    }
    


private void updateStatistics() {
    double totalRevenue = 0.0;
    int totalUsers = 0;
    int activeSessions = 0;

    File file = new File(FILE_PATH);

    if (!file.exists()) {
        showError("JSON file not found at: " + FILE_PATH);
        return;
    }

    try (FileReader reader = new FileReader(file)) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        JSONArray sessions = (JSONArray) jsonObject.get("sessions");
        JSONArray users = (JSONArray) jsonObject.get("users"); // Cross-reference users array

        if (sessions != null) {
            totalUsers = sessions.size();

            for (Object sessionObj : sessions) {
                JSONObject session = (JSONObject) sessionObj;

                // Count active sessions
                Boolean isActive = (Boolean) session.get("active");
                if (Boolean.TRUE.equals(isActive)) {
                    activeSessions++;
                }
            }
        }

        if (users != null) {
            String todayDate = LocalDate.now().toString();

            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;

                String lastLogin = (String) user.get("lastLogin");
                Double balance = user.get("amount") != null
                    ? Double.valueOf(user.get("amount").toString())
                    : 0.0;

                if (lastLogin != null) {
                    // Handle both "T" and space as separators in the timestamp
                    String lastLoginDate = lastLogin.contains("T")
                        ? lastLogin.split("T")[0]
                        : lastLogin.split(" ")[0];

                    if (lastLoginDate.equals(todayDate)) {
                        totalRevenue += balance;
                        System.out.println("Adding Balance for User: " + user.get("username") + ", Balance: " + balance);
                    }
                }
            }
        }

        // Update the UI fields
        jTextField1.setText(String.format("%.2f", totalRevenue)); // Total Revenue
        jTextField2.setText(String.valueOf(activeSessions));      // Active Sessions
        jTextField3.setText(String.valueOf(totalUsers));          // Total Users
    } catch (IOException | ParseException e) {
        showError("Error updating statistics: " + e.getMessage());
    }
}
  
    /**
     * Utility methods for JSON parsing.
     */
    private Integer getInteger(JSONObject obj, String key) {
        return obj.get(key) != null ? Integer.valueOf(obj.get(key).toString()) : null;
    }

    private Double getDouble(JSONObject obj, String key, double defaultValue) {
        return obj.get(key) != null ? Double.valueOf(obj.get(key).toString()) : defaultValue;
    }

    private String getString(JSONObject obj, String key, String defaultValue) {
        return obj.get(key) != null ? obj.get(key).toString() : defaultValue;
    }

    private int parseSeconds(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 3600 + Integer.parseInt(parts[1]) * 60 + Integer.parseInt(parts[2]);
    }

    private String formatTime(int totalSeconds) {
        return String.format("%02d:%02d:%02d", totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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

        amtTop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                amtTopKeyReleased(evt);
            }
        });

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

    private void amtTopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amtTopKeyReleased
        
    final double RATE_PER_HOUR = 20.0; // Define the rate per hour

    // Get the amount entered in the text field
    String amountText = amtTop.getText();

    // Validate the input
    if (amountText.isEmpty()) {
        timeTop.setText(""); // Clear the time field if no input
        return;
    }

    try {
        double amount = Double.parseDouble(amountText);

        // Calculate time equivalent in HH:mm:ss format
        int totalSeconds = (int) ((amount / RATE_PER_HOUR) * 3600);
        String timeEquivalent = String.format("%02d:%02d:%02d",
                totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60);

        // Display the calculated time in the timeTop field
        timeTop.setText(timeEquivalent);
    } catch (NumberFormatException e) {
        // Handle invalid input (e.g., non-numeric characters)
        timeTop.setText("Invalid input");
    }

    }//GEN-LAST:event_amtTopKeyReleased

    /**
     * @param  args the command line arguments
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

