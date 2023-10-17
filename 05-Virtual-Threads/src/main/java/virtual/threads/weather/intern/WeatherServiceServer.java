
package virtual.threads.weather.intern;

import virtual.threads.server.ServiceServer;
import virtual.threads.server.protocol.Request;
import virtual.threads.server.protocol.Response;
import virtual.threads.weather.WeatherKind;

import java.time.LocalDateTime;

public class WeatherServiceServer extends ServiceServer {

    private final double offset;

    public WeatherServiceServer(int id, int delay) {
        super(id, delay);
        offset = ((id % 10) - 5) * 0.1;
    }

    @Override
    protected Response handleRequest(Request request) {
        return switch (request) {
            case WeatherRequest r -> handleWeatherRequest(r);
            default -> null;
        };
    }

    private Response handleWeatherRequest(WeatherRequest request) {
        var time = request.time();
        var area = request.area();

        var temp = calculateTemperature(offset, area, time);
        var weather = calculateWeather(offset, area, time);
        return new WeatherResponse(id, area, time, temp, weather);

//        return new ErrorResponse(
//            id,
//            Reason.ILLEGAL_PARAMETER,
//            request.area() + ": " + request.time()
//        );
    }

    private static final double[] AVERAGE_TEMPERATURE = {
            Double.NaN,
            3.5,    // Januar
            3.2,    // Februar
            5.7,    // März
            7.5,    // April
            13.1,   // Mai
            18.5,   // Juni
            18.7,   // Juli
            18.3,   // August
            17.5,   // September
            12.5,   // Oktober
            6.4,    // November
            1.8     // Dezember
    };

    private static final double[] AVERAGE_TEMPERATURE_DELTA = {
            Double.NaN,
            4.5,    // Januar
            5.2,    // Februar
            6.7,    // März
            7.5,    // April
            8.1,    // Mai
            10.5,   // Juni
            12.7,   // Juli
            10.3,   // August
            8.5,    // September
            7.5,    // Oktober
            5.4,    // November
            3.0     // Dezember
    };

    private static final double[] AVERAGE_TEMPERATURE_HIGH = {
            Double.NaN,
            13.0,   // Januar
            13.2,   // Februar
            14.7,   // März
            15.5,   // April
            16.1,   // Mai
            17.0,   // Juni
            17.7,   // Juli
            16.3,   // August
            15.5,   // September
            14.5,   // Oktober
            13.4,   // November
            13.0    // Dezember
    };

    private static final double[] DAILY_DISPLACEMENT = {
        Double.NaN
        +1.3, -2.4, +2.7, +3.0, +2.0, +1.0, -1.1, -2.2, +4.0, +3.1,
        -2.1, +1.3, -0.3, -0.7, -1.1, +0.0, +2.1, +1.2, +0.4, -0.2,
        -2.9, +1.5, +0.6, -0.1, +1.8, -2.3, -2.7, -2.2, +0.1, +1.8,
        +0.0
    };

    private static final WeatherKind[] WEATHER_PERIOD = {
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLOUDY,
        WeatherKind.CLEAR,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.RAINY,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,

        WeatherKind.RAINY,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLEAR,
        WeatherKind.CLOUDY,

        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLEAR,

        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLEAR,

        WeatherKind.CLOUDY,
        WeatherKind.RAINY,
        WeatherKind.RAINY,
        WeatherKind.STORMY,
        WeatherKind.STORMY,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,

        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.CLEAR,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.CLEAR,

        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.RAINY,
        WeatherKind.CLOUDY,
        WeatherKind.CLOUDY
    };

    private static double toRad(double time) {
        return (time / 24.0) * 2.0 * Math.PI;
    }

    private static double calculateFactor(double peek_time, double current_time) {
        var pt = toRad(peek_time);
        var ct = toRad(current_time);
        return Math.abs(Math.cos(0.5 * (ct - pt)));
    }

    public static double calculateTemperature(double offset, String area, LocalDateTime time_and_date) {
        var area_diff_temp = (area.length() - 5) * 0.17;
        var month = time_and_date.getMonth().getValue();
        var day = time_and_date.getDayOfMonth();
        var hour = ((double) time_and_date.getHour()) + time_and_date.getMinute() / 60.0;

        // Die Basis für die Temperatur ist Durchschnittstemperatur, die mit der
        // Ortstemperatur korrigiert wird.
        var average_temp = AVERAGE_TEMPERATURE[month] + area_diff_temp;
        var daily_delta = DAILY_DISPLACEMENT[day];

        // Die Temperatur schwankt abhängig vom Monat.
        var temp_delta = AVERAGE_TEMPERATURE_DELTA[month];
        // Dabei wird die Höchsttemperatur zu einer vom Monat abhängigen Uhrzeit erreicht.
        var temp_high = AVERAGE_TEMPERATURE_HIGH[month];

        var factor = calculateFactor(temp_high, hour);
        return average_temp + factor * temp_delta + daily_delta + offset;
    }

    private static WeatherKind calculateWeather(
        double offset,
        int year,
        int month,
        int day,
        double hour
    ) {
        var index = (int) (
            ((year * 365.25 + month * 30 + day) * 24.0 + hour + 8 * offset) / 16.0
        );
        return WEATHER_PERIOD[index % 67];
    }

    private static WeatherKind calculateWeather(double offset, String area, LocalDateTime time_and_date) {
        var year    = time_and_date.getYear();
        var month   = time_and_date.getMonth().getValue();
        var day     = time_and_date.getDayOfMonth();
        var hour    = ((double) time_and_date.getHour()) + time_and_date.getMinute() / 60.0;

        return calculateWeather(offset, year, month, day, hour);
    }

}

