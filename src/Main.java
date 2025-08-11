import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main{

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

            // --- ЧТЕНИЕ ФАЙЛА ---
            try (FileReader fileReader=new FileReader(file);
                 BufferedReader reader=new BufferedReader(fileReader)) {

                String line;
                int minLenght=Integer.MAX_VALUE;
                int maxLenght=0;
                int totalReq=0;
                int googleBotCount=0;
                int yandexBotCount=0;

                while ((line=reader.readLine())!=null) {
                    int length = line.length();

                    // Проверка на длину строки 1024
                    if (length>1024) {
                        throw new CheсkLongLineException("Строка длиннее 1024 символов!!!");
                    }
                    // Проверка на самую короткую строку
                    if (length<minLenght) {
                        minLenght=length;
                    }
                    // Проверка на самую длинную строку
                    if (length>maxLenght) {
                        maxLenght=length;
                    }

                    totalReq++;

                    // Извлечение User-Agent
                    int lastQuote=line.lastIndexOf("\"");
                    int secondLastQuote=line.lastIndexOf("\"", lastQuote - 1);

                    String userAgent=line.substring(secondLastQuote + 1, lastQuote);
                    String fragment=parseUserAgent(userAgent);

                    if (fragment!=null) {
                        if (isGoogleBot(fragment)) {
                            googleBotCount++;
                        } else if (isYandexBot(fragment)) {
                            yandexBotCount++;
                        }
                    }
                }

                // Вывод статистики
                System.out.println("Общее количество строк в файле: "+totalReq);
                System.out.println("Длина самой короткой строки в файле: "+minLenght);
                System.out.println("Длина самой длинной строки в файле: "+maxLenght);

                // Вывод доли ботов
                if (totalReq > 0) {
                    double googlePercent=(double)googleBotCount/totalReq*100;
                    double yandexPercent=(double)yandexBotCount/totalReq*100;

                    System.out.println("Доля запросов от Googlebot: "+(googlePercent)+"%");
                    System.out.println("Доля запросов от Googlebot: "+(yandexPercent)+"%");
                } else {
                    System.out.println("Файл пустой");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                System.out.println("Собрали статистику по файлу");
            }
        }
    }

    // Метод для извлечения фрагмента из User-Agent
    private static String parseUserAgent(String userAgent) {
        int start=userAgent.indexOf('(');
        int end=userAgent.indexOf(')');
        if (start==-1||end==-1) return null;

        String firstBrackets=userAgent.substring(start + 1, end);
        String[] parts=firstBrackets.split(";");
        if (parts.length>=2) {
            return parts[1].trim();
        }
        return null;
    }

    // Проверка бота
    private static boolean isGoogleBot(String botName) {
        return botName.contains("Googlebot");
    }

    private static boolean isYandexBot(String botName) {
        return botName.contains("YandexBot");
    }

    // Собственное исключение
    static class CheсkLongLineException extends RuntimeException {
        public CheсkLongLineException(String message) {
            super(message);
        }
    }
}