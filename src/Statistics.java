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

    private HashMap<Integer,Integer> countVisitSecond=new HashMap<>(); // Секунда и кол-во посещений
    private LocalDateTime startTime; // Для подсчёта секунды
    private HashSet<String> countRefers=new HashSet<>(); //  Подсчет для referov
    private HashMap<String,Integer> countVisitUser=new HashMap<>(); // Подсчет по ip юзера

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

        if (startTime==null){
            startTime=entry.getDateTime();
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

            // Считаем, сколько раз заходит каждый пользователь
            String ip=entry.getIp();
            if (countVisitUser.containsKey(ip)) {
                int oldCount=countVisitUser.get(ip);
                countVisitUser.put(ip, oldCount + 1);
            } else {
                countVisitUser.put(ip, 1);
            }

            // Подсчет секунды с начала
            long secStart=java.time.Duration.between(startTime,entry.getDateTime()).getSeconds();

            if (secStart<=Integer.MAX_VALUE&&secStart>=Integer.MIN_VALUE){
                int sec=(int) secStart;

                // Обновляем счетчик посещений в сек.
                if (countVisitSecond.containsKey(sec)) {
                    int count=countVisitSecond.get(sec);
                    countVisitSecond.put(sec,count+1);
                } else {
                    countVisitSecond.put(sec,1);
                }
            }


        }

        // Считаем ошибки (4xx и 5xx)
        int code=entry.getResponseCode();
        if (code>=400&&code<600) {
            errorCount++;
        }

        // Проверяем откуда пришел пользователь
        String referer=entry.getReferer();
        if (!referer.equals("-")) {
            // Удаляем http://
            if (referer.startsWith("http://")) {
                referer=referer.substring(7);
            }

            // Удаляем www.
            if (referer.startsWith("www.")) {
                referer=referer.substring(4);
            }

            // Оставляем доменное имя
            String domain=referer.split("/")[0].split(":")[0];
            countRefers.add(domain);
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

    // Метод для пиковой посещаемости
    public int getVisitsSec(){
        if (countVisitSecond.isEmpty()) {
            return 0;
        }
        int max=0;
        for (int count:countVisitSecond.values()){
            if (count>max) {
                max=count;
            }
        }
        return max;
    }

    // Метод для получения списка доменов
    public Set<String> getReferrer() {
        return new HashSet<>(countRefers); // возвращаем копию
    }

    // Метод для получения максимума
    public int getMaxVisitUser() {
        if (countVisitUser.isEmpty()) {
            return 0;
        }

        int max=0;
        for (int count:countVisitUser.values()) {
            if (count>max) {
                max=count;
            }
        }
        return max;
    }

}