
package virtual.threads.weather;

import java.time.LocalDateTime;

public interface Weather {

	public String getArea();

	public LocalDateTime getTime();

	public double getTemperature();

	public WeatherKind getWeatherKind();

	public default String asString() {
		var time	= getTime();
		var year	= time.getYear();
		var month	= time.getMonth().getValue();
		var day		= time.getDayOfMonth();
		var hour	= time.getHour();
		var minute	= time.getMinute();
		return String.format(
			"%s [%02d.%02d.%04d %02d:%02d]: %s, %.1f °C",
			getArea(),
			day, month, year, hour, minute,
			getWeatherKind().getText(),
			getTemperature()
		);
	}
	
}
