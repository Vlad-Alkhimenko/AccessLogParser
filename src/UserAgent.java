public class UserAgent {
    private final String os;
    private final String browser;

    public UserAgent(String userAgentStr) {
        this.os=parseOS(userAgentStr);
        this.browser=parseBrowser(userAgentStr);
    }

    // Геттеры
    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }

    // Определяем ОС по ключевым словам
    private String parseOS(String stringOs) {
        if (stringOs.contains("Windows")) {
            return "Windows";
        } else if(stringOs.contains("Mac OS")){
            return "macOS";
        } else if(stringOs.contains("Linux")) {
            return "Linux";
        } else {
            return "Неизвестная Операционная Система";
        }
    }

    // Определяем браузер
    private String parseBrowser(String stringBrowser) {
        if (stringBrowser.contains("Edge")) {
            return "Edge";
        } else if(stringBrowser.contains("Firefox")) {
            return "Firefox";
        } else if (stringBrowser.contains("Chrome")&&!stringBrowser.contains("Edge")) {
            return "Chrome";
        } else if (stringBrowser.contains("Opera")||stringBrowser.contains("OPR")) {
            return "Opera";
        } else {
            return "другой";
        }
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "os='" + os + '\'' +
                ", browser='" + browser + '\'' +
                '}';
    }
}
