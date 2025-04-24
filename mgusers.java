
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author User
 */
public class mgusers extends javax.swing.JFrame {

    private static final String filepath = "src\\netnexus.json";
    private static final JSONParser jsonParser = new JSONParser();
    private static JSONObject record = new JSONObject();
    private static JSONArray users = new JSONArray();

    public mgusers() {
        initComponents();
        
         // Now apply the rounded border to your buttons
        SveBttn.setBorder(new RoundedBorder(12));
        AddBttn.setBorder(new RoundedBorder(12));
        AddUsBttn.setBorder(new RoundedBorder(12));
        EdtBttn.setBorder(new RoundedBorder(12));
        DltBttn.setBorder(new RoundedBorder(12));
        userBtn.setBorder(new RoundedBorder(12));
        backbtn1.setBorder(new RoundedBorder(12));

        jScrollPane1.getViewport().setBackground(new java.awt.Color(24, 26, 32));
        jTable1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setBackground(new java.awt.Color(24, 26, 32));
        jTable1.setGridColor(new java.awt.Color(33, 35, 44));
        jTable1.setRowHeight(24);
        jTable1.setSelectionBackground(new java.awt.Color(30, 41, 59));
        jTable1.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTable1.setShowGrid(true);

        // Header Styling
        javax.swing.table.JTableHeader header = jTable1.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        header.setForeground(new java.awt.Color(255, 255, 255));
        header.setBackground(new java.awt.Color(33, 35, 44));
        header.setOpaque(true);

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            {
                setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                setForeground(new java.awt.Color(255, 255, 255));
                setBackground(new java.awt.Color(24, 26, 32));
                setOpaque(true);
                setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }
        });

        // Cell Renderer for all cells
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
        cellRenderer.setBackground(new java.awt.Color(33, 35, 44));
        cellRenderer.setForeground(new java.awt.Color(255, 255, 255));
        cellRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Apply renderer to all columns
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        setupRowSelectionListener();
        try {
            filecheck(); // Load data from JSON file
        } catch (IOException | ParseException ex) {
            Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private TableRowSorter<DefaultTableModel> rowSorter;

    private void setupTableFilter() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        if (rowSorter == null) {
            rowSorter = new TableRowSorter<>(model);
            jTable1.setRowSorter(rowSorter);
        }

        srchTF.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void filterTable() {
        String searchText = srchTF.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);

        if (searchText.isEmpty()) {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    String username = (String) entry.getValue(0);
                    return !"admin".equalsIgnoreCase(username); // Exclude admin
                }
            });
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
     public class RoundedBorder implements Border {

        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        AddUsBttn = new javax.swing.JButton();
        EdtBttn = new javax.swing.JButton();
        DltBttn = new javax.swing.JButton();
        backbtn1 = new javax.swing.JButton();
        userBtn = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        srchTF = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        editpnl = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        SveBttn = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        EdtCb = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        EdtUsnTF = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        EdtPssTF = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        newEdittimeTF = new javax.swing.JTextField();
        EdtNewUsnTF = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        NewLogDtTF = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        EdtBlncTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        addpnl = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        AddBttn = new javax.swing.JButton();
        AddPssTF = new javax.swing.JTextField();
        AddLoginDteTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        AddBlncTF = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        AddUsnTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        vipcmb = new javax.swing.JComboBox<>();
        addtimeTF = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(24, 26, 32));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddUsBttn.setBackground(new java.awt.Color(37, 99, 235));
        AddUsBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AddUsBttn.setForeground(new java.awt.Color(255, 255, 255));
        AddUsBttn.setText("ADD USERS");
        AddUsBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddUsBttnActionPerformed(evt);
            }
        });
        jPanel1.add(AddUsBttn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 150, 30));

        EdtBttn.setBackground(new java.awt.Color(34, 197, 94));
        EdtBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        EdtBttn.setForeground(new java.awt.Color(255, 255, 255));
        EdtBttn.setText("EDIT USERS");
        EdtBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdtBttnActionPerformed(evt);
            }
        });
        jPanel1.add(EdtBttn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 150, 30));

        DltBttn.setBackground(new java.awt.Color(255, 0, 0));
        DltBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        DltBttn.setForeground(new java.awt.Color(255, 255, 255));
        DltBttn.setText("DELETE USER");
        DltBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DltBttnActionPerformed(evt);
            }
        });
        jPanel1.add(DltBttn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 150, 30));

        backbtn1.setBackground(new java.awt.Color(255, 0, 0));
        backbtn1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        backbtn1.setForeground(new java.awt.Color(255, 255, 255));
        backbtn1.setText("HOME\n");
        backbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbtn1ActionPerformed(evt);
            }
        });
        jPanel1.add(backbtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 150, 30));

        userBtn.setBackground(new java.awt.Color(37, 99, 235));
        userBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        userBtn.setForeground(new java.awt.Color(255, 255, 255));
        userBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Users.png"))); // NOI18N
        userBtn.setText("USERS");
        userBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userBtnActionPerformed(evt);
            }
        });
        jPanel1.add(userBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 193, 150, 30));

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 27)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 255));
        jLabel13.setText("NET");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 27)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("NEXUS");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, -1, -1));

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 8)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("INTERNET CAFE SOFTWARE");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("ADMIN");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 170, 10));

        jPanel2.setLayout(new java.awt.CardLayout());

        jPanel3.setBackground(new java.awt.Color(24, 26, 32));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(33, 35, 44));

        jTable1.setBackground(new java.awt.Color(30, 41, 59));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "USERNAME", "TIME", "BALANCE", "LOGIN DATE"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        srchTF.setBackground(new java.awt.Color(24, 26, 32));
        srchTF.setForeground(new java.awt.Color(255, 255, 255));
        srchTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        srchTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                srchTFKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("USERNAME");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(srchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 890, 440));

        jPanel2.add(jPanel3, "card6");

        editpnl.setBackground(new java.awt.Color(24, 26, 32));
        editpnl.setMinimumSize(new java.awt.Dimension(925, 500));
        editpnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(33, 35, 44));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        SveBttn.setBackground(new java.awt.Color(37, 99, 235));
        SveBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SveBttn.setForeground(new java.awt.Color(255, 255, 255));
        SveBttn.setText("SAVE");
        SveBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SveBttnActionPerformed(evt);
            }
        });

        jTextField2.setBackground(new java.awt.Color(24, 26, 32));
        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));
        jTextField2.setText("0");
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("LOGINS");

        EdtCb.setBackground(new java.awt.Color(24, 26, 32));
        EdtCb.setForeground(new java.awt.Color(255, 255, 255));
        EdtCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Bronze", "Silver", "Gold" }));
        EdtCb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("VIP STATUS");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("SEARCH USERNAME");

        EdtUsnTF.setBackground(new java.awt.Color(24, 26, 32));
        EdtUsnTF.setForeground(new java.awt.Color(255, 255, 255));
        EdtUsnTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        EdtUsnTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EdtUsnTFMouseClicked(evt);
            }
        });
        EdtUsnTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdtUsnTFActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("NEW PASSWORD");

        EdtPssTF.setBackground(new java.awt.Color(24, 26, 32));
        EdtPssTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        EdtPssTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EdtPssTFMouseClicked(evt);
            }
        });
        EdtPssTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdtPssTFActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("NEW TIME SESSION");

        newEdittimeTF.setBackground(new java.awt.Color(24, 26, 32));
        newEdittimeTF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        newEdittimeTF.setForeground(new java.awt.Color(255, 255, 255));
        newEdittimeTF.setText("00:00:00");
        newEdittimeTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        newEdittimeTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEdittimeTFActionPerformed(evt);
            }
        });
        newEdittimeTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                newEdittimeTFKeyReleased(evt);
            }
        });

        EdtNewUsnTF.setBackground(new java.awt.Color(24, 26, 32));
        EdtNewUsnTF.setForeground(new java.awt.Color(255, 255, 255));
        EdtNewUsnTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        EdtNewUsnTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EdtNewUsnTFMouseClicked(evt);
            }
        });
        EdtNewUsnTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdtNewUsnTFActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("EDIT USERNAME");

        NewLogDtTF.setBackground(new java.awt.Color(24, 26, 32));
        NewLogDtTF.setForeground(new java.awt.Color(255, 255, 255));
        NewLogDtTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        NewLogDtTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NewLogDtTFMouseClicked(evt);
            }
        });
        NewLogDtTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewLogDtTFActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("NEW LOGIN DATE");

        EdtBlncTF.setBackground(new java.awt.Color(24, 26, 32));
        EdtBlncTF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        EdtBlncTF.setForeground(new java.awt.Color(255, 255, 255));
        EdtBlncTF.setText("0.0");
        EdtBlncTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        EdtBlncTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EdtBlncTFMouseClicked(evt);
            }
        });
        EdtBlncTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdtBlncTFActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("BALANCE");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SveBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(397, 397, 397))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel11))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EdtUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EdtPssTF, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16)))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EdtNewUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(EdtBlncTF, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NewLogDtTF, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newEdittimeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19))))
                        .addGap(0, 181, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(EdtCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EdtUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EdtPssTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newEdittimeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel17)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EdtNewUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NewLogDtTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EdtBlncTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel11))
                .addGap(14, 14, 14)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EdtCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(SveBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        editpnl.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 900, 350));

        jPanel8.setBackground(new java.awt.Color(33, 35, 44));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel8.setForeground(new java.awt.Color(37, 99, 235));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("EDIT USER");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(379, 379, 379)
                .addComponent(jLabel7)
                .addContainerGap(380, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(17, 17, 17))
        );

        editpnl.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 900, 70));

        jPanel2.add(editpnl, "card2");
        editpnl.getAccessibleContext().setAccessibleDescription("");

        addpnl.setBackground(new java.awt.Color(24, 26, 32));

        jPanel5.setBackground(new java.awt.Color(33, 35, 44));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        AddBttn.setBackground(new java.awt.Color(37, 54, 235));
        AddBttn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AddBttn.setForeground(new java.awt.Color(255, 255, 255));
        AddBttn.setText("ADD");
        AddBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBttnActionPerformed(evt);
            }
        });

        AddPssTF.setBackground(new java.awt.Color(24, 26, 32));
        AddPssTF.setForeground(new java.awt.Color(255, 255, 255));
        AddPssTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        AddPssTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddPssTFMouseClicked(evt);
            }
        });

        AddLoginDteTF.setBackground(new java.awt.Color(24, 26, 32));
        AddLoginDteTF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AddLoginDteTF.setForeground(new java.awt.Color(255, 255, 255));
        AddLoginDteTF.setText("mm-dd-yyyy");
        AddLoginDteTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        AddLoginDteTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddLoginDteTFMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("LOGIN DATE");

        AddBlncTF.setBackground(new java.awt.Color(24, 26, 32));
        AddBlncTF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AddBlncTF.setForeground(new java.awt.Color(255, 255, 255));
        AddBlncTF.setText("0.0");
        AddBlncTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        AddBlncTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddBlncTFMouseClicked(evt);
            }
        });
        AddBlncTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBlncTFActionPerformed(evt);
            }
        });
        AddBlncTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AddBlncTFKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("BALANCE");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("PASSWORD");

        AddUsnTF.setBackground(new java.awt.Color(24, 26, 32));
        AddUsnTF.setForeground(new java.awt.Color(255, 255, 255));
        AddUsnTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        AddUsnTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddUsnTFMouseClicked(evt);
            }
        });
        AddUsnTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddUsnTFActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("USERNAME");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("LOGINS");

        jTextField1.setBackground(new java.awt.Color(24, 26, 32));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("0");
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("VIP STATUS");

        vipcmb.setBackground(new java.awt.Color(24, 26, 32));
        vipcmb.setForeground(new java.awt.Color(255, 255, 255));
        vipcmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NONE", "BRONZE", "SILVER", "GOOD" }));
        vipcmb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));

        addtimeTF.setBackground(new java.awt.Color(24, 26, 32));
        addtimeTF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addtimeTF.setForeground(new java.awt.Color(255, 255, 255));
        addtimeTF.setText("00:00:00");
        addtimeTF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85)));
        addtimeTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addtimeTFMouseClicked(evt);
            }
        });
        addtimeTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addtimeTFActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("TIME SESSION");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(131, 131, 131)
                                .addComponent(jLabel3))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(125, 125, 125))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(AddLoginDteTF)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jLabel4))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(vipcmb, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addtimeTF)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(AddUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AddPssTF, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AddBlncTF)))
                .addContainerGap(190, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AddBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(381, 381, 381))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AddUsnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddBlncTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddPssTF, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vipcmb, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addtimeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddLoginDteTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addComponent(AddBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        jPanel6.setBackground(new java.awt.Color(33, 35, 44));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ADD USER");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(377, 377, 377))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout addpnlLayout = new javax.swing.GroupLayout(addpnl);
        addpnl.setLayout(addpnlLayout);
        addpnlLayout.setHorizontalGroup(
            addpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addpnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        addpnlLayout.setVerticalGroup(
            addpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addpnlLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(addpnl, "card1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBttnActionPerformed
        CardLayout cl = (CardLayout) jPanel2.getLayout();
        cl.show(jPanel2, "card1");
    }//GEN-LAST:event_AddBttnActionPerformed

    private void EdtUsnTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtUsnTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EdtUsnTFActionPerformed
    // TODO add your handling code here:

    private void EdtNewUsnTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtNewUsnTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EdtNewUsnTFActionPerformed

    private void EdtBlncTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtBlncTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EdtBlncTFActionPerformed

    private void AddUsnTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddUsnTFActionPerformed
        AddUsnTF.setText("");
    }//GEN-LAST:event_AddUsnTFActionPerformed

    private void EdtBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtBttnActionPerformed

        // Get the selected row from the JTable
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            // If no row is selected, show a warning message
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Retrieve data from the selected row
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String username = (String) model.getValueAt(selectedRow, 0);
        String userTime = (String) model.getValueAt(selectedRow, 1);
        String balance = String.valueOf(model.getValueAt(selectedRow, 2));
        String lastLogin = (String) model.getValueAt(selectedRow, 3);

        // Populate the edit panel fields with the selected row's data
        EdtUsnTF.setText(username);        // Username
        newEdittimeTF.setText(userTime);   // User Time
        EdtBlncTF.setText(balance);        // Balance
        NewLogDtTF.setText(lastLogin);     // Last Login Date

        // Switch to the edit panel
        CardLayout cl = (CardLayout) jPanel2.getLayout();
        cl.show(jPanel2, "card2"); // Assuming "card2" is the edit panel

    }//GEN-LAST:event_EdtBttnActionPerformed

    private void DltBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DltBttnActionPerformed
        try {
            removeUsersInactiveFor30Days();
        } catch (IOException | ParseException ex) {
            Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(this, "Inactive users removed successfully.");
        try {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "No user selected for deletion.");
                return;
            }

            String usernameToDelete = (String) jTable1.getValueAt(selectedRow, 0);
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                if (user.get("username").equals(usernameToDelete)) {
                    users.remove(i);
                    break;
                }
            }

            record.put("users", users);
            save();

            // Remove table row
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(null, "User deleted successfully!");
        } catch (HeadlessException | IOException ex) {
            Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_DltBttnActionPerformed

    private void EdtPssTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtPssTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EdtPssTFActionPerformed
    // TODO add your handling code here:

    private void NewLogDtTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewLogDtTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewLogDtTFActionPerformed

    private void backbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbtn1ActionPerformed

    }//GEN-LAST:event_backbtn1ActionPerformed

    private void SrchClrBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SrchClrBttnActionPerformed

    }//GEN-LAST:event_SrchClrBttnActionPerformed

    private void UserDltBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserDltBttnActionPerformed


    }//GEN-LAST:event_UserDltBttnActionPerformed

    private void SveBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SveBttnActionPerformed

        try {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "No user selected for editing.");
                return;
            }

            String currentUsername = (String) jTable1.getValueAt(selectedRow, 0);
            String newUsername = EdtNewUsnTF.getText().trim();
            String newPassword = EdtPssTF.getText().trim();
            String newBalanceStr = EdtBlncTF.getText().trim();
            double newBalance = Double.parseDouble(newBalanceStr);
            String newVip = (String) EdtCb.getSelectedItem();

            for (Object obj : users) {
                JSONObject user = (JSONObject) obj;
                if (user.get("username").equals(currentUsername)) {
                    user.put("username", newUsername);
                    user.put("password", newPassword);
                    user.put("amount", newBalanceStr);
                    user.put("VipStatus", newVip);
                    user.put("userTime", calculateUserTime(newBalance));
                    break;
                }
            }
            save();

            // Update table row
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setValueAt(newUsername, selectedRow, 0);
            model.setValueAt(newBalanceStr, selectedRow, 2);
            model.setValueAt(newVip, selectedRow, 3);
            JOptionPane.showMessageDialog(null, "User updated successfully!");
        } catch (HeadlessException | IOException | NumberFormatException ex) {
            Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_SveBttnActionPerformed

    private void EdtClrBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtClrBttnActionPerformed

    }//GEN-LAST:event_EdtClrBttnActionPerformed

    private void EdtCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdtCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EdtCbActionPerformed


    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed

        try {
            String username = AddUsnTF.getText().trim();
            String password = AddPssTF.getText().trim();
            String balanceStr = AddBlncTF.getText().trim();
            double balance = Double.parseDouble(balanceStr);
            String vipStatus = vipcmb.getSelectedItem().toString();
            String loginDate = getCurrentDateTime();

            // Check if user already exists
            for (Object obj : users) {
                JSONObject user = (JSONObject) obj;
                if (user.get("username").equals(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists!");
                    return;
                }
            }

            String userTime = calculateUserTime(balance);
            JSONObject newUser = new JSONObject();
            newUser.put("username", username);
            newUser.put("password", password);
            newUser.put("amount", balanceStr);
            newUser.put("userTime", userTime);
            newUser.put("lastLogin", loginDate);
            newUser.put("VipStatus", vipStatus);

            users.add(newUser);
            record.put("users", users);
            save();

            // Add the new user to the table
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.addRow(new Object[]{username, userTime, balanceStr, loginDate});
            JOptionPane.showMessageDialog(null, "User added successfully!");
        } catch (HeadlessException | IOException | NumberFormatException ex) {
            Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_addbtnActionPerformed

    private void DltClrBttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DltClrBttnActionPerformed

    }//GEN-LAST:event_DltClrBttnActionPerformed

    private void clrbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clrbtnActionPerformed

    }//GEN-LAST:event_clrbtnActionPerformed

    private void AddBlncTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBlncTFActionPerformed

    }//GEN-LAST:event_AddBlncTFActionPerformed

    private void AddUsnTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddUsnTFMouseClicked
        AddUsnTF.setText("");
    }//GEN-LAST:event_AddUsnTFMouseClicked

    private void AddPssTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddPssTFMouseClicked
        AddPssTF.setText("");    }//GEN-LAST:event_AddPssTFMouseClicked

    private void addtimeTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtimeTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addtimeTFActionPerformed

    private void AddBlncTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddBlncTFMouseClicked
        AddBlncTF.setText("");
    }//GEN-LAST:event_AddBlncTFMouseClicked

    private void AddLoginDteTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddLoginDteTFMouseClicked
        AddLoginDteTF.setText("");
    }//GEN-LAST:event_AddLoginDteTFMouseClicked

    private void addtimeTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addtimeTFMouseClicked
        addtimeTF.setText("");
    }//GEN-LAST:event_addtimeTFMouseClicked

    private void EdtUsnTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EdtUsnTFMouseClicked
        EdtUsnTF.setText("");
    }//GEN-LAST:event_EdtUsnTFMouseClicked

    private void EdtNewUsnTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EdtNewUsnTFMouseClicked
        EdtNewUsnTF.setText(""); // TODO add your handling code here:
    }//GEN-LAST:event_EdtNewUsnTFMouseClicked

    private void EdtBlncTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EdtBlncTFMouseClicked
        EdtBlncTF.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_EdtBlncTFMouseClicked

    private void EdtPssTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EdtPssTFMouseClicked
        EdtPssTF.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_EdtPssTFMouseClicked

    private void NewLogDtTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewLogDtTFMouseClicked
        NewLogDtTF.setText("");        // TODO add your handling code here:
    }//GEN-LAST:event_NewLogDtTFMouseClicked

    private void AddBlncTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AddBlncTFKeyReleased
        AddBlncTF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    String balanceStr = AddBlncTF.getText().trim();
                    if (!balanceStr.isEmpty()) {
                        double balance = Double.parseDouble(balanceStr);
                        String calculatedTime = calculateUserTime(balance);
                        addtimeTF.setText(calculatedTime); // Set the calculated session time
                    } else {
                        addtimeTF.setText("00:00:00"); // Reset if balance is empty
                    }
                } catch (NumberFormatException e) {
                    addtimeTF.setText("00:00:00"); // Reset if invalid input
                }
            }
        });        // TODO add your handling code here:
    }//GEN-LAST:event_AddBlncTFKeyReleased

    private void newEdittimeTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEdittimeTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newEdittimeTFActionPerformed

    private void newEdittimeTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newEdittimeTFKeyReleased
        EdtBlncTF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    String balanceStr = EdtBlncTF.getText().trim();
                    if (!balanceStr.isEmpty()) {
                        double balance = Double.parseDouble(balanceStr);
                        String calculatedTime = calculateUserTime(balance);
                        newEdittimeTF.setText(calculatedTime); // Set the calculated session time
                    } else {
                        newEdittimeTF.setText("00:00:00"); // Reset if balance is empty
                    }
                } catch (NumberFormatException e) {
                    newEdittimeTF.setText("00:00:00"); // Reset if invalid input
                }
            }
        });        // TODO add your handling code here:
    }//GEN-LAST:event_newEdittimeTFKeyReleased

    private void srchTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_srchTFKeyReleased
        setupTableFilter();        // TODO add your handling code here:
    }//GEN-LAST:event_srchTFKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        setupRowClickListener();        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void userBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userBtnActionPerformed
        CardLayout cl = (CardLayout) jPanel2.getLayout();
        cl.show(jPanel2, "card6");        // TODO add your handling code here:
    }//GEN-LAST:event_userBtnActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mgusers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new mgusers().setVisible(true);
        });
    }

    public void filecheck() throws FileNotFoundException, IOException, ParseException {
        FileReader reader = new FileReader(filepath);

        if (reader.ready()) {
            Scanner scan = new Scanner(reader);
            String line = "";
            while (scan.hasNext()) {
                line = line + scan.nextLine();
            }

            reader.close();

            if (!line.equals("")) {
                reader.close();
                try (FileReader reader2 = new FileReader(filepath)) {
                    record = (JSONObject) jsonParser.parse(reader2);
                    users = (JSONArray) record.get("users");
                    loadTableData(); // Load data into the table
                }
            }
        }
    }

    public static void save() throws IOException {
        try (FileWriter file = new FileWriter(filepath)) {
            file.write(record.toJSONString());
        }
    }

    private String getCurrentDateTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private String calculateUserTime(double balance) {
        int timeInSeconds = (int) ((balance / 20) * 3600); // 1 hour per 20 balance
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void loadTableData() {
        // Get the DefaultTableModel of the table
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing rows

        // Populate table rows with user data from the JSON array
        for (Object obj : users) {
            JSONObject user = (JSONObject) obj;

            // Skip the admin user
            if ("admin".equalsIgnoreCase((String) user.get("username"))) {
                continue;
            }

            model.addRow(new Object[]{
                user.get("username"),
                user.get("userTime"),
                user.get("amount"),
                user.get("lastLogin")
            });
        }
    }

    private void setupRowSelectionListener() {
        jTable1.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                // Populate fields with the selected row's data
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                EdtUsnTF.setText((String) model.getValueAt(selectedRow, 0));
                newEdittimeTF.setText((String) model.getValueAt(selectedRow, 1));
                EdtBlncTF.setText(String.valueOf(model.getValueAt(selectedRow, 2)));
                NewLogDtTF.setText((String) model.getValueAt(selectedRow, 3));
            }
        });
    }

    private void setupRowClickListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) { // Single click
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        String username = (String) model.getValueAt(selectedRow, 0);

                        // Search for the user in the JSON array for additional details
                        JSONObject userDetails = null;
                        for (Object obj : users) {
                            JSONObject user = (JSONObject) obj;
                            if (user.get("username").equals(username)) {
                                userDetails = user;
                                break;
                            }
                        }

                        if (userDetails != null) {
                            // Display the user details in a dialog
                            String userInfo = "Username: " + userDetails.get("username")
                                    + "\nPassword: " + userDetails.get("password")
                                    + "\nBalance: " + userDetails.get("amount")
                                    + "\nUser Time: " + userDetails.get("userTime")
                                    + "\nLast Login: " + userDetails.get("lastLogin")
                                    + "\nVIP Status: " + userDetails.get("VipStatus")
                                    + "\nLogins: " + userDetails.get("logins");
                            JOptionPane.showMessageDialog(null, userInfo, "User Details", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void removeUsersInactiveFor30Days() throws FileNotFoundException, IOException, ParseException {
        
            File file = new File(filepath);
            if (!file.exists()) {
                System.err.println("JSON file not found. Skipping removal process.");
                return;
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            JSONArray userss = (JSONArray) jsonObject.get("users");

            long currentTime = System.currentTimeMillis();
            long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            boolean[] updated = {false};
            userss.removeIf((Object obj) -> {
                JSONObject user = (JSONObject) obj;
                String lastLoginDateStr = (String) user.get("lastLogin");
                if (lastLoginDateStr == null) {
                    return false;
                }
                try {
                    Date lastLoginDate = dateFormat.parse(lastLoginDateStr);
                    long lastLoginTime = lastLoginDate.getTime();
                    if ((currentTime - lastLoginTime) >= thirtyDaysInMillis) {
                        updated[0] = true;
                        return true;
                    }
                } catch (java.text.ParseException e) {
                    System.err.println("Invalid date format for lastLogin: " + lastLoginDateStr);
                }
                return false;
            });
            if (updated[0]) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(jsonObject.toJSONString());
                } catch (IOException ex) {
                    Logger.getLogger(mgusers.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Removed users inactive for 30 days or more successfully.");
            } else {
                System.out.println("No users found inactive for 30 days or more.");
            }
        }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddBlncTF;
    private javax.swing.JButton AddBttn;
    private javax.swing.JTextField AddLoginDteTF;
    private javax.swing.JTextField AddPssTF;
    private javax.swing.JButton AddUsBttn;
    private javax.swing.JTextField AddUsnTF;
    private javax.swing.JButton DltBttn;
    private javax.swing.JTextField EdtBlncTF;
    private javax.swing.JButton EdtBttn;
    private javax.swing.JComboBox<String> EdtCb;
    private javax.swing.JTextField EdtNewUsnTF;
    private javax.swing.JTextField EdtPssTF;
    private javax.swing.JTextField EdtUsnTF;
    private javax.swing.JTextField NewLogDtTF;
    private javax.swing.JButton SveBttn;
    private javax.swing.JPanel addpnl;
    private javax.swing.JTextField addtimeTF;
    private javax.swing.JButton backbtn1;
    private javax.swing.JPanel editpnl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField newEdittimeTF;
    private javax.swing.JTextField srchTF;
    private javax.swing.JButton userBtn;
    private javax.swing.JComboBox<String> vipcmb;
    // End of variables declaration//GEN-END:variables
}
