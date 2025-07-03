import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Выводим последовательно 2 числа в консоль
        System.out.println("Введите первое число:");
        int firstNumber=new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber=new Scanner(System.in).nextInt();

        // Выводим в консоль операции по работе с 2 числами
        System.out.println("Разность: " + (firstNumber-secondNumber));
        System.out.println("Сумма: " + (firstNumber+secondNumber));
        System.out.println("Произведение: " + (firstNumber*secondNumber));
        System.out.println("Частное: " + ((double)firstNumber/secondNumber));

        // Бесконечный цикл
        // Создаем счетчик для хранения
        int count=0;
        System.out.println("Введите путь к файлу: ");
        // Логика бесконечного цикла
        while (true) {
            String path=new Scanner(System.in).nextLine();
            // Существует ли файл, путь к которому был указан
            File file=new File(path);
            // Переменная fileExists будет равна true, если файл существует, и false, если не существует
            boolean fileExists=file.exists();
            // Является ли указанный путь директорией
            boolean isDirectory=file.isDirectory();
            //
            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Указанный файл не существует.");
                } else {
                    System.out.println("Указанный путь ведет к директории");
                }
                continue; // Продолжаем цикл
            }
            count++;
            System.out.println("Путь указан верно. Это файл номер " + count);
            System.out.println("Общее количество верных путей: " + count);
        }
    }
}
