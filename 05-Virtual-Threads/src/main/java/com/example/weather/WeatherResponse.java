
package com.example.weather;

import java.io.Serializable;

import com.example.server.protocol.Response;

public record WeatherResponse(
	String server, 
	float temperature, 
	WeatherKind kind
) implements Serializable, Response, Weather {

	@Override
	public WeatherKind getWeatherKind() {
		return kind();
	}

	@Override
	public float getTemperature() {
		return temperature();
	}
    
}
