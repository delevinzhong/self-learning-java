import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UI extends JFrame {

    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                createAndShowGUI();
//            }
//        });

        JFrame frame = new JFrame("Personal Cost");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    public static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // INPUT Label
        JLabel inputLabel = new JLabel("Input");
        inputLabel.setBounds(10, 20, 80, 25);
        panel.add(inputLabel);

        // INPUT Text
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 750, 25);
        panel.add(userText);


        // DATE Label
        JLabel dateLabel = new JLabel("Date");
        dateLabel.setBounds(10, 50, 80, 25);
        panel.add(dateLabel);

        // DATE Text
        JTextField dateText = new JTextField(20);
        dateText.setBounds(100, 50, 150, 25);
        panel.add(dateText);

        // COST ITEM Label
        JLabel costItemLabel = new JLabel("Cost Item");
        costItemLabel.setBounds(10, 80, 80, 25);
        panel.add(costItemLabel);

        // COST ITEM Text
        JTextField costItemText = new JTextField(20);
        costItemText.setBounds(100, 80, 150, 25);
        panel.add(costItemText);

        // COST Label
        JLabel costLabel = new JLabel("Cost");
        costLabel.setBounds(10, 110, 80, 25);
        panel.add(costLabel);

        // COST Text
        JTextField costText = new JTextField(20);
        costText.setBounds(100, 110, 150, 25);
        panel.add(costText);

        // COST Type Label
        JLabel costTypeLabel = new JLabel("Cost Type");
        costTypeLabel.setBounds(10, 140, 80, 25);
        panel.add(costTypeLabel);

        // COST Type Text
        JTextField costTypeText = new JTextField(20);
        costTypeText.setBounds(100, 140, 150, 25);
        panel.add(costTypeText);

        // Create Run button
        JButton runButton = new JButton("Run");
        runButton.setBounds(100, 170, 150, 25);
        panel.add(runButton);

        // Create monthly query button
        JButton curMonthButton = new JButton("Current Month Cost");
        curMonthButton.setBounds(265, 170, 150, 25);
        panel.add(curMonthButton);

        // Result Label
        JLabel resultLabel = new JLabel("Output");
        resultLabel.setBounds(10, 210, 80, 25);
        panel.add(resultLabel);

        // Result test area
//        JTextArea resultTextArea = new JTextArea();
//        resultTextArea.setBounds(100, 210, 750, 500);
//        resultTextArea.setLineWrap(true);
//        JScrollPane scrollPane = new JScrollPane(resultTextArea);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        panel.add(scrollPane);
//        panel.add(resultTextArea);

        JTextArea resultArea = new JTextArea();
        JScrollPane sp = new JScrollPane(resultArea);
        sp.setBounds(100, 210, 750, 500);
        panel.add(sp);


        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/cost_center?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true",
                            "root",
                            "root"
                    );

                    // For Insert
                    if (userText.getText().isEmpty()) {
                        String costItem = costItemText.getText();
                        String cost = costText.getText();
                        String costType = costTypeText.getText();
                        java.util.Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDate = dateText.getText();
                        if (currentDate.isEmpty()) {
                            currentDate = dateFormat.format(date);
                        }
                        String query = String.format("INSERT INTO `%s` VALUES (`%s`, `%s`, `%s`, `%s`);",
                                ConnectToMysql.TABLE_NAME, costItem, cost, costType, currentDate);
                        ConnectToMysql.insertNewRecord(connection, currentDate, costItem, cost, costType,
                                ConnectToMysql.TABLE_NAME);
                        resultArea.append(query + "\n");
                    } else {
                        // For custom query
                        String customQuery = userText.getText();
                        ResultSet rs = ConnectToMysql.queryCostTable(connection, customQuery);
                        List<Object> list = convertList(rs);
                        for (Object l : list) {
                            resultArea.append(l + "\n");
                            // select * from cost_center.personal_cost order by currentDate desc;
                        }
                    }
                } catch (SQLException | ClassNotFoundException sqlException) {
                    sqlException.printStackTrace();
                }

            }
        });
    }

    private static List<Object> convertList(ResultSet resultSet) throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<Object, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

}
