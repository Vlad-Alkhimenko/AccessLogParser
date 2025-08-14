import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// В классе Statistics реализуйте метод, который будет возвращать список всех существующих страниц сайта.
// Для этого создайте в классе переменную класса HashSet. В эту переменную при выполнении метода addEntry добавляйте
// адреса существующих страниц (с кодом ответа 200) сайта.
// В классе Statistics также реализуйте метод, который будет возвращать статистику операционных систем пользователей сайта.
// Для этого создайте в классе переменную класса HashMap<String, Integer>, в которой подсчитывайте частоту
// встречаемости каждой операционной системы.
// При выполнении метода addEntry проверяйте, есть ли в этом HashMap запись с такой операционной системой. Если нет,
// вставляйте такую запись. Если есть, добавляйте к соответствующему значению единицу. В итоге получится HashMap, ключи
// которого будут названиями операционных систем, а значения — их количествами в лог-файле.
// Метод в результате должен создавать новый HashMap<String, Double> и в качестве ключей рассчитывать долю для каждой
// операционной системы (от 0 до 1). Чтобы рассчитать долю конкретной операционной системы, нужно разделить количество
// конкретной операционной системы на общее количество для всех операционных систем.

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private HashSet<String> pages=new HashSet<>(); // Список страниц
    private HashMap<String,Integer> countOs=new HashMap<>(); // Статистика операционных систем пользователей сайта

    // Конструктор без параметров
    public Statistics() {
        this.totalTraffic = 0;
    }

    // Метод добавления записи
    public void addEntry(LogEntry entry) {
        // Добавляем объём данных
        int size=entry.getDataSize();
        totalTraffic=totalTraffic+size;

        LocalDateTime current=entry.getDateTime();

        if (minTime==null) {
            minTime=current;
            maxTime=current;
        }
        else if (current.isBefore(minTime)) {
            minTime=current;
        }
        else if (current.isAfter(maxTime)) {
            maxTime=current;
        }

        // Добавляем страницу, если код 200
        if (entry.getResponseCode()==200) {
            pages.add(entry.getPath());
        }

        // Считаем ОС
        String os=entry.getUserAgent().getOs();
        countOs.put(os,countOs.getOrDefault(os,0)+1);
    }

    // Метод для расчёта среднего трафика в час
    public double getTrafficRate() {
        if (minTime==null||maxTime==null) {
            return 0;
        }
        // Считаем разницу в часах
        long hours = java.time.Duration.between(minTime, maxTime).toHours();

        // Если прошёл меньше часа — считаем как 1 час
        if (hours==0) {
            hours=1;
        }

        // Делим общий трафик на часы и возвращаем
        return (double) totalTraffic/hours;
    }

    // Получить список страниц (с кодом 200)
    public Set<String> getPages() {
        Set<String> res=new HashSet<>();
        for (String page:pages) {
            res.add(page);
        }
        return res;
    }

    // Метод: статистика ОС в долях (от 0 до 1)
    public Map<String,Double> getOsStatistics() {
        Map<String,Double> res=new HashMap<>();

        int total=0;
        for (int count:countOs.values()) {
            total+=count;
        }
        if (total==0) {
            return res;
        }

        // Cчитаем долю count/total
        for (String os:countOs.keySet()) {
            int count=countOs.get(os);
            double fraction=(double) count/total;
            res.put(os,fraction);
        }
        return res;
    }
}