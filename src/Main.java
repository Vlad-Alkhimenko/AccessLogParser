import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static File path;

    public static void main(String[] args) throws CheсkLongLineException{
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

        // Цикл
        int count=0; // Создаем счетчик для хранения
        System.out.println("Введите путь к файлу: ");

        while (true) {
            String path=new Scanner(System.in).nextLine();

            File file=new File(path); // Существует ли файл, путь к которому был указан

            boolean fileExists=file.exists(); // Переменная fileExists будет равна true, если файл существует, и false, если не существует
            boolean isDirectory=file.isDirectory(); // Является ли указанный путь директорией

            // Проверка на наличие файла
            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Указанный файл не существует.");
                } else {
                    System.out.println("Указанный путь ведет к директории");
                }
                continue; // Продолжаем цикл
            }
            System.out.println("Путь указан верно. Это файл номер " + count);
            count++;
            System.out.println("Общее количество верных путей: " + count);

            // --- НАЧИНАЕМ ЧТЕНИЕ ФАЙЛА ---
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                int totalLenght=0;
                int minLenght=0;
                int maxLenght=0;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();

                    // Проверка на длину строки 1024
                    if (length>1024) {
                        throw new CheсkLongLineException("Строка длиннее 1024 символов!!!");
                    }
                    // Проверка на самую короткую строку
                    if (length<minLenght) {
                        maxLenght=length;
                    }
                    // Проверка на самую длинную строку
                    if (length>maxLenght) {
                        maxLenght=length;
                    }

                    totalLenght++;
                }

                System.out.println("Длина самой короткой строки в файле: "+minLenght);
                System.out.println("Длина самой длинной строки в файле: "+maxLenght);
                System.out.println("Общее количество строк в файле: "+totalLenght++);


            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                System.out.println("Собрали статистику по файлу");
            }
        }
    }

    // Собственный класс исключения
    static class CheсkLongLineException extends RuntimeException{
        public CheсkLongLineException(String message) {
            super(message);
        }
    }
}
