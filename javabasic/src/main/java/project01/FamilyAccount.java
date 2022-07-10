package project01;

public class FamilyAccount {

    public static void main(String[] args) {

        boolean flag = true;

        String details = "收支\t\t账户金额\t\t收支金额\t\t说明";

        int balance = 10000;

        while (flag) {

            System.out.println("-------------家庭收入支出系统-------------");
            System.out.println("\t\t\t 1.收支明细");
            System.out.println("\t\t\t 2.登记收入");
            System.out.println("\t\t\t 3.登记支出");
            System.out.println("\t\t\t 4.退   出\n");
            System.out.print("\t\t\t 请选择(1-4): ");

            char selection = Utility.readMenuSelection();

            switch (selection) {
                case '1':
                    System.out.println("-------------当前收支明细记录-------------");
                    System.out.println(details);
                    System.out.println("---------------------------------------");
                    break;
                case '2':
                    System.out.print("请输入收入金额：");
                    int income = Utility.readNum();
                    System.out.print("本次收入说明：");
                    String info = Utility.readString();
                    balance += income;
                    details = details + "\n" + "收入\t\t" + balance + "\t\t" + income + "\t\t" + info;
                    break;
                case '3':
                    System.out.print("请输入支出金额：");
                    int outcome = Utility.readNum();
                    System.out.print("本次支出说明：");
                    String infoOut = Utility.readString();
                    if (balance >= outcome) {
                        balance -= outcome;
                        details = details + "\n" + "收入\t\t" + balance + "\t\t" + outcome + "\t\t" + infoOut;
                    } else {
                        System.out.println("支出超出存款,无法购买");
                    }
                    break;
                case '4':
                    System.out.print("确认是否退出(Y/N):");
                    char isExit = Utility.readConfirmSelection();
                    if (isExit == 'Y') {
                        flag = false;
                    }
                    break;
            }

        }

    }
}
