import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private HashSet<String> pages=new HashSet<>(); // Список страниц
    private HashMap<String,Integer> countOs=new HashMap<>(); // Статистика операционных систем пользователей сайта

    private HashSet<String> pagesNot=new HashSet<>(); // Список страниц c кодом 404
    private HashMap<String,Integer> countBrowser=new HashMap<>(); // Статистика браузеров

    private int userCount=0; // Количество посещений пользователей
    private int errorCount=0; // Количество ошибочных ответов 4xx,5xx
    private Set<String> uniqueIpUsers=new HashSet<>(); // Уникальные IP обычных пользователей

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

        // Добавляем страницу, если код 404
        if (entry.getResponseCode()==404) {
            pagesNot.add(entry.getPath());
        }

        // Считаем Браузеры
        String browser=entry.getUserAgent().getBrowser();
        countBrowser.put(browser,countBrowser.getOrDefault(browser,0)+1);

        String userAgentStr=entry.getUserAgent().toString();
        boolean isRealUser=!entry.getUserAgent().isBot();

        // Считаем посещения не-ботов
        if (isRealUser) {
            userCount++;
            uniqueIpUsers.add(entry.getIp());
        }

        // Считаем ошибки (4xx и 5xx)
        int code=entry.getResponseCode();
        if (code>=400&&code<600) {
            errorCount++;
        }
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

    // Статистика ОС в долях (от 0 до 1)
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

    // Получить список страниц (с кодом 404)
    public Set<String> getErrorPages() {
        Set<String> res=new HashSet<>();
        for (String page:pagesNot) {
            res.add(page);
        }
        return res;
    }

    // Статистика Браузера в долях (от 0 до 1)
    public Map<String,Double> getBrowserStatistics() {
        Map<String,Double> res=new HashMap<>();

        int total=0;
        for (int count:countBrowser.values()) {
            total+=count;
        }
        if (total==0) {
            return res;
        }

        // Cчитаем долю count/total
        for (String os:countBrowser.keySet()) {
            int count=countBrowser.get(os);
            double fraction=(double) count/total;
            res.put(os,fraction);
        }
        return res;
    }

    // Метод подсчёта среднего количества посещений сайта за час
    public double getAverageVisits() {
        if (minTime==null||maxTime==null) {
            return 0.0;
        }
        long hours=java.time.Duration.between(minTime, maxTime).toHours();
        if (hours==0)hours=1;
        return (double) userCount/hours;
    }

    // Метод подсчёта среднего количества ошибочных запросов в час
    public double getAverageErrors() {
        if (minTime==null||maxTime==null) {
            return 0.0;
        }
        long hours=java.time.Duration.between(minTime, maxTime).toHours();
        if (hours==0) hours=1;
        return (double) errorCount/hours;
    }

    // Метод расчёта средней посещаемости одним пользователем.
    public double getAverageVisitsUser() {
        if (uniqueIpUsers.isEmpty()) {
            return 0.0;
        }
        return (double) userCount/ uniqueIpUsers.size();
    }

}