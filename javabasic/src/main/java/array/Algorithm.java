package array;

public class Algorithm {

    public static void main(String[] args) {
        int arrLength = 10;
        int arrMax = getArrMax(arrLength);
        System.out.println("The max number in the array is " + arrMax);
        int arrMin = getArrMin(arrLength);
        System.out.println("The min number in the array is " + arrMin);
        int arrSum = getArrSum(arrLength);
        System.out.println("The sum of the array is " + arrSum);
    }

    /*
     *  求数值型数组中元素的最大值、最小值、平均数、总和等
     *
     *  定义一个int型一维数组，包含10个元素，分别赋一些随机整数，然后求出所有元素的最大值、最小值、总和、平均值并输出
     *  要求： 所有随机数都是两位数
     */
    public static int getArrMax(int arrLength){
        int[] arr = new int[arrLength];

        // [10,99]
        for (int i=0; i<arr.length; i++) {
            arr[i] = (int)(Math.random() * (99 - 10 + 1) + 10);
        }
        System.out.print("Generated array is :");
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        int max = 0;
        for (int j : arr) {
            if (max < j) {
                max = j;
            }
        }
        return max;
    }

    public static int getArrMin(int arrLength){
        int[] arr = new int[arrLength];

        // [10,99]
        for (int i=0; i<arr.length; i++) {
            arr[i] = (int)(Math.random() * (99 - 10 + 1) + 10);
        }
        System.out.print("Generated array is :");
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        int min = 100;
        for (int j : arr) {
            if (min > j) {
                min = j;
            }
        }
        return min;
    }

    public static int getArrSum(int arrLength){
        int[] arr = new int[arrLength];

        // [10,99]
        for (int i=0; i<arr.length; i++) {
            arr[i] = (int)(Math.random() * (99 - 10 + 1) + 10);
        }
        System.out.print("Generated array is :");
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
        int sum = 0;
        for (int j : arr) {
            sum += j;
        }
        return sum;
    }
}
