
package virtual.threads.weather.intern;

import virtual.threads.server.protocol.Response;
import virtual.threads.weather.Weather;
import virtual.threads.weather.WeatherKind;

import java.io.Serializable;
import java.time.LocalDateTime;

public record WeatherResponse(
	String server,
	String area,
	LocalDateTime time,
	double temperature,
	WeatherKind kind
) implements Serializable, Response, Weather {

	@Override
	public String getArea() {
		return area();
	}

	@Override
	public LocalDateTime getTime() {
		return time();
	}

	@Override
	public WeatherKind getWeatherKind() {
		return kind();
	}

	@Override
	public double getTemperature() {
		return temperature();
	}

	@Override
	public String toString() {
		return asString() + "   (" + server + ")";
	}
    
}
