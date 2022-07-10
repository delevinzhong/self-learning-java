import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
        //jtextarea.setLineWrap(true);
        sp = new JScrollPane(jtextarea);
        sp.setBounds(5, 180, 1000, 500);
        //sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return sp;
    }

    private JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setBounds(10, 10, 80, 30);
            jLabel.setText("Input");
        }

        return jLabel;
    }

    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new javax.swing.JTextField();
            jTextField.setBounds(60, 10, 260, 30);
        }
        return jTextField;
    }

    private JButton getJButtonRun() {
        if (jButton == null) {
            jButton = new javax.swing.JButton();
            jButton.setBounds(10, 50, 80, 30);
            jButton.setText("Run");
        }
        return jButton;
    }

    private JButton getJButtoncancel() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setBounds(10, 90, 80, 30);
            jButton1.setText("stop");
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection conn = JDBCUtils.getConnection();
                        String sql = "SELECT * FROM personal_cost ORDER BY currentDate desc";
                        BaseDAO baseDAO = BaseDAO.class.newInstance();
                        baseDAO.selectAndGetOne(conn, Cost.class, sql);

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
            jButton2.setText("Clear");
        }
        return jButton2;
    }

    public static void main(String[] args) {
        TestJFrame w = new TestJFrame();
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        w.setVisible(true);
    }
}