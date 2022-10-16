import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PersonalCostUI extends JFrame {

    private static final int XLABEL = 10;
    private static final int WIDTHLABEL = 160;
    private static final int HEIGHTLABEL = 25;
    private static final int XTEXT = 170;
    private static final int HEIGHTTEXT = 25;
    private static final int WIDTHBUTTON = 150;
    private static final int HEIGHTBUTTON = 25;
    private static final String SUMMONTHCOST = "SELECT SUM(cost) FROM personal_cost WHERE currentDate BETWEEN " +
            "'%s 00:00:00' AND '%s 23:59:59';";


    public static void main(String[] args) {
        JFrame frame = new JFrame("记账本");
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
        JLabel inputLabel = new JLabel("自定义查询");
        inputLabel.setBounds(XLABEL, 20, WIDTHLABEL, HEIGHTLABEL);
        panel.add(inputLabel);
        // INPUT Text
        JTextField userText = new JTextField(20);
        userText.setBounds(XTEXT, 20, 750, HEIGHTTEXT);
        panel.add(userText);


        // DATE Label
        JLabel dateLabel = new JLabel("日期（currentDate）");
        dateLabel.setBounds(XLABEL, 50, WIDTHLABEL, HEIGHTLABEL);
        panel.add(dateLabel);
        // DATE Text
        JTextField dateText = new JTextField(20);
        dateText.setBounds(XTEXT, 50, 150, HEIGHTTEXT);
        panel.add(dateText);

        // COST ITEM Label
        JLabel costItemLabel = new JLabel("花费项目（costItem）");
        costItemLabel.setBounds(XLABEL, 80, WIDTHLABEL, HEIGHTLABEL);
        panel.add(costItemLabel);
        // COST ITEM Text
        JTextField costItemText = new JTextField(20);
        costItemText.setBounds(XTEXT, 80, 150, HEIGHTTEXT);
        panel.add(costItemText);

        // COST Label
        JLabel costLabel = new JLabel("金额（cost）");
        costLabel.setBounds(XLABEL, 110, WIDTHLABEL, HEIGHTLABEL);
        panel.add(costLabel);
        // COST Text
        JTextField costText = new JTextField(20);
        costText.setBounds(XTEXT, 110, 150, HEIGHTTEXT);
        panel.add(costText);

        // COST Type Label
        JLabel costTypeLabel = new JLabel("花费类型（costType）");
        costTypeLabel.setBounds(XLABEL, 140, WIDTHLABEL, HEIGHTLABEL);
        panel.add(costTypeLabel);
        // COST Type Text
        JTextField costTypeText = new JTextField(20);
        costTypeText.setBounds(XTEXT, 140, 150, HEIGHTTEXT);
        panel.add(costTypeText);

        // Create Run button
        JButton runButton = new JButton("运行");
        runButton.setBounds(170, 170, WIDTHBUTTON, HEIGHTBUTTON);
        panel.add(runButton);

        // Create monthly query button
//        JButton curMonthButton = new JButton("Current Month Cost");
//        curMonthButton.setBounds(335, 170, 150, 25);
//        panel.add(curMonthButton);
        // 当月花费总计Button
        JButton sumCurMonthButton = new JButton("当月总计");
        sumCurMonthButton.setBounds(335, 170, WIDTHBUTTON, HEIGHTBUTTON);
        panel.add(sumCurMonthButton);

        // Result Label
        JLabel resultLabel = new JLabel("输出");
        resultLabel.setBounds(XLABEL, 210, 80, HEIGHTLABEL);
        panel.add(resultLabel);

        // Result test area
        JTextArea resultArea = new JTextArea();
        JScrollPane sp = new JScrollPane(resultArea);
        sp.setBounds(XTEXT, 210, 750, 500);
        panel.add(sp);

        runButtonAction(runButton, userText, costItemText, costText, costTypeText, dateText, resultArea);

        sumCurMonthButtonAction(sumCurMonthButton, resultArea);

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

    private static void runButtonAction(JButton runButton, JTextField userText, JTextField costItemText,
                                        JTextField costText, JTextField costTypeText, JTextField dateText,
                                        JTextArea resultArea) {
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
//                    Class.forName("com.mysql.cj.jdbc.Driver");
//                    Connection connection = DriverManager.getConnection(
//                            "jdbc:mysql://localhost:3306/cost_center?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true",
//                            "root",
//                            "123456"
//                    );
                    Connection connection = connectMysql(resultArea);

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
                                MySqlUtils.TABLE, costItem, cost, costType, currentDate);
                        MySqlUtils.insertNewRecord(connection, currentDate, costItem, cost, costType,
                                MySqlUtils.TABLE);
                        resultArea.append(query + "\n");
                    } else {
                        // For custom query
                        String customQuery = userText.getText();
                        if (customQuery.length() == 1){
                            customQuery = getCusSumMont(customQuery, resultArea);
                        }

                        ResultSet rs = MySqlUtils.queryCostTable(connection, customQuery);
                        List<Object> list = convertList(rs);
                        for (Object l : list) {
                            resultArea.append(l + "\n");
                        }
                    }
                } catch (SQLException sqlException) {
                    resultArea.append(sqlException + "\n");
                    sqlException.printStackTrace();
                }

            }
        });
    }

    private static void sumCurMonthButtonAction(JButton jButton, JTextArea resultArea) {
        ArrayList<String> curMonFirLasDayList = getCurrentDate();
        String firstDay = curMonFirLasDayList.get(0);
        String lastDay = curMonFirLasDayList.get(1);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = connectMysql(resultArea);
                String query = String.format(SUMMONTHCOST, firstDay, lastDay);
                try {
                    ResultSet resultSet = MySqlUtils.queryCostTable(connection, query);
                    List<Object> list = convertList(resultSet);
                    for (Object l : list) {
                        resultArea.append("月份：" + curMonFirLasDayList.get(3) + "\n");
                        resultArea.append(l + "\n");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private static Connection connectMysql(JTextArea resultArea) {
        Connection connection = null;
        String password = "";
        try {
            String hostname = InetAddress.getLocalHost().getCanonicalHostName();
            System.out.println(hostname);
            if (hostname.equals("LAPTOP-ETF2FTFQ")) {
                password = "123456";
                System.out.println(password);
            } else {
                password = "root";
                System.out.println(password);
            }
        } catch (Exception e) {
            resultArea.append(e + "\n");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cost_center?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    password
            );

        } catch (SQLException | ClassNotFoundException sqlException) {
            resultArea.append(sqlException + "\n");
            sqlException.printStackTrace();
        }
        return connection;

    }

    private static ArrayList<String> getCurrentDate() {
        ArrayList<String> curDateList = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        String currentYear = String.valueOf(instance.get(Calendar.YEAR));
        String currentMonth = String.valueOf(instance.get(Calendar.MONTH) + 1);
        String firstDay, lastDay;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        instance.add(Calendar.MONTH, 0);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = format.format(instance.getTime());
        instance.add(Calendar.MONTH, 1);
        instance.set(Calendar.DAY_OF_MONTH, 0);
        lastDay = format.format(instance.getTime());
        curDateList.add(firstDay);
        curDateList.add(lastDay);
        curDateList.add(currentYear);
        curDateList.add(currentMonth);
        return curDateList;
    }

    private static String getCusSumMont(String customQuery, JTextArea resultArea) {
        ArrayList<String> maxMonthList = new ArrayList<String>(){{
            add("1");
            add("3");
            add("5");
            add("7");
            add("8");
            add("10");
            add("12");
        }};
        ArrayList<String> minMonthList = new ArrayList<String>(){{
            add("4");
            add("6");
            add("9");
            add("11");
        }};

        int curYearNum = Integer.parseInt(getCurrentDate().get(2));
        String feb = curYearNum % 4 == 0 ? "29" : "28";
        String curYear = getCurrentDate().get(2);
        String firstday = String.format("%s-%s-01", curYear, customQuery);
        String lastDayFormat = "%s-%s-%s";
        String resultQuery = "";

        if (maxMonthList.contains(customQuery)) {
            String lastDay = "31";
            resultQuery = String.format(SUMMONTHCOST, firstday,
                    String.format(lastDayFormat, curYear, customQuery, lastDay));
        }
        if (minMonthList.contains(customQuery)) {
            String lastDay = "30";
            resultQuery = String.format(SUMMONTHCOST, firstday,
                    String.format(lastDayFormat, curYear, customQuery, lastDay));
        }
        if (customQuery.equals("2")) {
            resultQuery = String.format(SUMMONTHCOST, firstday,
                    String.format(lastDayFormat, curYear, customQuery, feb));
        }
        resultArea.append(String.format("%s月总计 ： ", customQuery));
        return resultQuery;
    }

}
