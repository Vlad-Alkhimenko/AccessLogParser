import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Выводим последовательно 2 числа в консоль
        System.out.println("Введите первое число:");
        int firstNumber=new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber=new Scanner(System.in).nextInt();

        //Выводим в консоль операции по работе с 2 числами
        System.out.println("Разность: " + (firstNumber-secondNumber));
        System.out.println("Сумма: " + (firstNumber+secondNumber));
        System.out.println("Произведение: " + (firstNumber*secondNumber));
        System.out.println("Частное: " + ((double)firstNumber/secondNumber));
    }
}
