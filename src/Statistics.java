import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

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
}