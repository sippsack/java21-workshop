
package virtual.threads.weather;

public enum WeatherKind {
    
    CLEAR("heiter"),

    CLOUDY("heiter bis wolkig"),

    RAINY("regnerisch"),

    STORMY("stürmisch");

    private final String text;

    WeatherKind(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
