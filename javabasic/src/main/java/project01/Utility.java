package project01;

import java.util.Scanner;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);

    public static char readMenuSelection(){
        char c;
        for (;;) {
            String str = readKeyBoard(1);
            c = str.charAt(0);
            if (c != '1' && c != '2' && c != '3' && c != '4') {
                System.out.print("输入错误，请重新输入：");
            } else break;
        }
        return c;
    }

    public static int readNum() {
        int n;
        for (;;) {
            String str = readKeyBoard(4); // 不超过4位数的输入
            try {
                n = Integer.parseInt(str);
                break;
            } catch (NumberFormatException e) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
        return n;
    }

    // 描述收入说明
    public static String readString() {
        String str = readKeyBoard(8);
        return str;
    }

    public static char readConfirmSelection() {
        char c;
        for (;;) {
            String str = readKeyBoard(1).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("选择错误，请重新输入：");
            }
        }
        return c;
    }

    private static String readKeyBoard(int i) {
        String line = "";
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (line.length() < 1 || line.length() > i) {
                System.out.print("输入长度错误，请重新输入：");
                continue;
            }
            break;
        }
        return line;
    }
}
