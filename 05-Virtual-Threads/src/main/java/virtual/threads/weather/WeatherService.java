
package virtual.threads.weather;

import virtual.threads.server.ServerId;
import virtual.threads.server.Service;

import java.io.IOException;
import java.net.UnknownHostException;

public class WeatherService {

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_1   = new ServerId(1,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_2   = new ServerId(2,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_3   = new ServerId(3,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_4   = new ServerId(4,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_5   = new ServerId(5,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_6   = new ServerId(6,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_7   = new ServerId(7,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_8   = new ServerId(8,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_9   = new ServerId(9,  0);

	/**
	 * Ein Wetterdienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_10   = new ServerId(10,  0);

	/**
     * <b>Der</b> Wetterdienst, der ohne Verzögerung antwortet.
     */
    public static final ServerId SERVICE_WITH_NO_DELAY   = SERVICE_WITH_NO_DELAY_1;

	/**
	 * Ein Wetterdienst, der erst nach 10 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_10_MS_DELAY   = new ServerId(1001,  10);
    
	/**
	 * Ein Wetterdienst, der erst nach 20 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_20_MS_DELAY   = new ServerId(1002,  20);
    
	/**
	 * Ein Wetterdienst, der erst nach 30 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_30_MS_DELAY   = new ServerId(1003,  30);
    
	/**
	 * Ein Wetterdienst, der erst nach 40 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_40_MS_DELAY   = new ServerId(1004,  40);
    
	/**
	 * Ein Wetterdienst, der erst nach 50 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_50_MS_DELAY   = new ServerId(1005,  50);
    
	/**
	 * Ein Wetterdienst, der erst nach 60 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_60_MS_DELAY   = new ServerId(1006,  60);
    
	/**
	 * Ein Wetterdienst, der erst nach 70 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_70_MS_DELAY   = new ServerId(1007,  70);
    
	/**
	 * Ein Wetterdienst, der erst nach 80 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_80_MS_DELAY   = new ServerId(1008,  80);
    
	/**
	 * Ein Wetterdienst, der erst nach 90 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_90_MS_DELAY   = new ServerId(1009,  90);
    
	/**
	 * Ein Wetterdienst, der erst nach 100 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_100_MS_DELAY  = new ServerId(1010, 100);
    
	/**
	 * Ein Wetterdienst, der erst nach 200 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_200_MS_DELAY  = new ServerId(1011, 200);

	/**
	 * Ein Wetterdienst, der erst nach 300 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_300_MS_DELAY 	= new ServerId(1012, 300);

	/**
	 * Ein Wetterdienst, der nie antwortet.
	 */
    public static final ServerId SERVICE_WITH_MAX_DELAY		= new ServerId(1013, Integer.MAX_VALUE);

	/**
	 * Ein Wetterdienst, der nie antwortet.
	 */
	public static final ServerId SERVICE_WITH_ERROR		= new ServerId(1014, -1);

	/**
     * Meldet den gegebenen Benutzer mit seinem Passwort bei dem gewünschten Service an.
     * 
     * @param id Die ID des Service
     * @param name Der Benutzername
     * @param password Das Kennwort für den angegebenen Benutzer
     * 
     * @return Die Session, über den der Zugang zu den Services gewährt wird.
     * 
     * @throws IOException
     * @throws UnknownHostException
     */
    public static WeatherSession login(
        ServerId id, 
        String name, 
        char[] password
    ) throws         
        IOException, 
        UnknownHostException
    {
        return new WeatherSession(id, Service.login(id.id(), name, password));
    }

}
