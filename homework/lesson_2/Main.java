package homework.lesson_2;

public class Main {
    public static void main(String[] args) {
        int targetIncome = 10000;
        int daysInMonth = 22;

        int dailyRate = targetIncome / daysInMonth;
        int remainder =  targetIncome % daysInMonth;
        System.out.println(targetIncome);
        System.out.println(remainder);
    }
}