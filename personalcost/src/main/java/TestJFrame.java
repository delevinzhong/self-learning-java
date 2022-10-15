import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJFrame extends JFrame{
    private JLabel jLabel;
    private JTextField jTextField;
    private JButton jButton, jButton1, jButton2;
    private JTextArea jtextarea;
    private JScrollPane sp;

    public TestJFrame() {
        super();
        this.setSize(1024, 768);
        this.getContentPane().setLayout(null);
        this.add(getJLabel(), null);
        this.add(getJTextField(), null);
        this.add(getJButtonRun(), null);
        this.add(getJButtoncancel(), null);
        this.add(getJButtonzero(), null);
        this.setBackground(Color.red);
        this.add(getJTextArea(), null);

        this.setTitle("Personal Cost");
    }

    private JScrollPane getJTextArea() {
        if (jtextarea == null) {
            jtextarea = new JTextArea();
            //jtextarea.setBounds(5, 45, 650, 400);
        }
        jtextarea.setLineWrap(true);
        sp = new JScrollPane(jtextarea);
        sp.setBounds(5, 180, 1000, 500);
//        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return sp;
    }

    private JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setBounds(10, 10, 80, 30);
            jLabel.setText("输入");
        }

        return jLabel;
    }

    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new javax.swing.JTextField();
            jTextField.setBounds(60, 10, 900, 30);
        }
        return jTextField;
    }

    private JButton getJButtonRun() {
        if (jButton == null) {
            jButton = new javax.swing.JButton();
            jButton.setBounds(10, 50, 80, 30);
            jButton.setText("执行");
        }
        return jButton;
    }

    private JButton getJButtoncancel() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setBounds(10, 90, 80, 30);
            jButton1.setText("停止");
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
//                        Connection conn = JDBCUtils.getConnection();
//                        String sql = "SELECT * FROM personal_cost ORDER BY currentDate desc";
//                        BaseDAO baseDAO = BaseDAO.class.newInstance();
//                        baseDAO.selectAndGetOne(conn, Cost.class, sql);
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/cost_center?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true",
                                "root",
                                "root"
                        );
                        String sql = "SELECT * FROM personal_cost ORDER BY currentDate desc";
                        ResultSet rs = ConnectToMysql.queryCostTable(connection, sql);
                        List<Object> list = convertList(rs);
                        for (Object l : list) {
                            JScrollPane jScrollPane = getJTextArea();
//                            sp.append(l + "\n");
                            // select * from cost_center.personal_cost order by currentDate desc;
                        }

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
        return jButton1;
    }

    private JButton getJButtonzero() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setBounds(10, 130, 80, 30);
            jButton2.setText("清除");
        }
        return jButton2;
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

    public static void main(String[] args) {
        TestJFrame w = new TestJFrame();
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setVisible(true);
    }
}