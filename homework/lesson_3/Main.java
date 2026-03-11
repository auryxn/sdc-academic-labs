package  homework.lesson_3;

public class Main {
    public static void main(String[] args) {
        int currentProfit = 100;
        if (currentProfit >= 10000) {
            System.out.println("Цель империи достигнута! Переходим в Beast Mode.");
        }
        if (currentProfit < 10000 && currentProfit >= 5000) {
            System.out.println("Неплохо, но нужно больше автоматизации.")
        }
        else {
            System.out.println("Срочно пишем новых ботов, сэр!")
        }
    }
}