import java.time.LocalDateTime;

public class LogEntry {
    private final String ip;
    private final LocalDateTime dateTime;
    private final Methods method;
    private final String path;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final UserAgent userAgent;

    // Конструктор, который парсит строку лога
    public LogEntry(String line) {
        // Разбиваем строку по пробелам
        String[] parts=line.split(" ", 8);

        this.ip=parts[0];

        // Дата и время
        String dateTimeStr=parts[3].substring(1);
        this.dateTime=parseDateTime(dateTimeStr);

        // Метод и путь
        String requestPart=parts[5].substring(1);
        String[] requestParts=requestPart.split(" ", 3);
        this.method=parseHttpMethod(requestParts[0]);
        this.path=requestParts[1];

        // Код ответа и размер данных
        String[] responseAndSize=parts[6].split(" ", 2);
        this.responseCode=Integer.parseInt(responseAndSize[0]);

        String sizeStr=responseAndSize.length > 1 ? responseAndSize[1] : "-";
        this.dataSize=sizeStr.equals("-") ? 0 : Integer.parseInt(sizeStr);

        // Referer
        int firstQuote=parts[6].indexOf('"');
        int secondQuote=parts[6].indexOf('"', firstQuote + 1);
        if (firstQuote!=-1&&secondQuote!=-1) {
            this.referer=parts[6].substring(firstQuote+1,secondQuote);
        } else{
            this.referer="-";
        }

        // User-Agent
        String userAgentLine=parts[7];
        if (userAgentLine.endsWith("\"")) {
            userAgentLine=userAgentLine.substring(0,userAgentLine.length()-1);
        }
        this.userAgent=new UserAgent(userAgentLine);
    }

    // Парсинг даты и времени
    private LocalDateTime parseDateTime(String dateTimeStr) {
        String[] parts=dateTimeStr.split("[:/]");
        int day=Integer.parseInt(parts[0]);
        String monthStr=parts[1];
        int year=Integer.parseInt(parts[2]);
        int hour=Integer.parseInt(parts[3]);
        int minute=Integer.parseInt(parts[4]);
        int second=Integer.parseInt(parts[5]);

        int month=1;
        if (monthStr.equals("Jan")) {
            month=1;
        } else if (monthStr.equals("Feb")) {
            month=2;
        } else if (monthStr.equals("Mar")) {
            month=3;
        } else if (monthStr.equals("Apr")) {
            month=4;
        } else if (monthStr.equals("May")) {
            month=5;
        } else if (monthStr.equals("Jun")) {
            month=6;
        } else if (monthStr.equals("Jul")) {
            month=7;
        } else if (monthStr.equals("Aug")) {
            month=8;
        } else if (monthStr.equals("Sep")) {
            month=9;
        } else if (monthStr.equals("Oct")) {
            month=0;
        } else if (monthStr.equals("Nov")) {
            month=11;
        } else if (monthStr.equals("Dec")) {
            month=12;
        }

        return LocalDateTime.of(year,month,day,hour,minute,second);
    }

    // Определяем HTTP-метод через if-else
    private Methods parseHttpMethod(String methodStr) {
        if (methodStr.equals("GET")) {
            return Methods.GET;
        } else if (methodStr.equals("POST")) {
            return Methods.POST;
        } else if (methodStr.equals("PUT")) {
            return Methods.PUT;
        } else if (methodStr.equals("DELETE")) {
            return Methods.DELETE;
        } else if (methodStr.equals("HEAD")) {
            return Methods.HEAD;
        } else if (methodStr.equals("OPTIONS")) {
            return Methods.OPTIONS;
        } else if (methodStr.equals("PATCH")) {
            return Methods.PATCH;
        } else {
            return Methods.UNKNOWN;
        }
    }

    // Геттеры
    public String getIp() {
        return ip;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Methods getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}