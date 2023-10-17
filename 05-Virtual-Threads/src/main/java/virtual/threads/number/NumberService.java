
package virtual.threads.number;

import virtual.threads.server.ServerId;
import virtual.threads.server.Service;

import java.io.IOException;
import java.net.UnknownHostException;

public class NumberService {

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_1   = new ServerId(21,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_2   = new ServerId(22,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_3   = new ServerId(23,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_4   = new ServerId(24,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_5   = new ServerId(25,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_6   = new ServerId(26,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_7   = new ServerId(27,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_8   = new ServerId(28,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_9   = new ServerId(29,  0);

	/**
	 * Ein Nummerndienst, der ohne Verzögerung antwortet.
	 */
	public static final ServerId SERVICE_WITH_NO_DELAY_10   = new ServerId(30,  0);

	/**
     * <b>Der</b> Nummerndienst, der ohne Verzögerung antwortet.
     */
    public static final ServerId SERVICE_WITH_NO_DELAY   = SERVICE_WITH_NO_DELAY_1;

	/**
	 * Ein Nummerndienst, der erst nach 10 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_10_MS_DELAY   = new ServerId(1021,  10);
    
	/**
	 * Ein Nummerndienst, der erst nach 20 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_20_MS_DELAY   = new ServerId(1022,  20);
    
	/**
	 * Ein Nummerndienst, der erst nach 30 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_30_MS_DELAY   = new ServerId(1023,  30);
    
	/**
	 * Ein Nummerndienst, der erst nach 40 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_40_MS_DELAY   = new ServerId(1024,  40);
    
	/**
	 * Ein Nummerndienst, der erst nach 50 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_50_MS_DELAY   = new ServerId(1025,  50);
    
	/**
	 * Ein Nummerndienst, der erst nach 60 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_60_MS_DELAY   = new ServerId(1026,  60);
    
	/**
	 * Ein Nummerndienst, der erst nach 70 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_70_MS_DELAY   = new ServerId(1027,  70);
    
	/**
	 * Ein Nummerndienst, der erst nach 80 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_80_MS_DELAY   = new ServerId(1028,  80);
    
	/**
	 * Ein Nummerndienst, der erst nach 90 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_90_MS_DELAY   = new ServerId(1029,  90);
    
	/**
	 * Ein Nummerndienst, der erst nach 100 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_100_MS_DELAY  = new ServerId(1030, 100);
    
	/**
	 * Ein Nummerndienst, der erst nach 200 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_200_MS_DELAY  = new ServerId(1031, 200);

	/**
	 * Ein Nummerndienst, der erst nach 300 ms antwortet.
	 */
    public static final ServerId SERVICE_WITH_300_MS_DELAY 	= new ServerId(1032, 300);

	/**
	 * Ein Nummerndienst, der nie antwortet.
	 */
    public static final ServerId SERVICE_WITH_MAX_DELAY		= new ServerId(1033, Integer.MAX_VALUE);

	/**
	 * Ein Nummerndienst, der nie antwortet.
	 */
	public static final ServerId SERVICE_WITH_ERROR		= new ServerId(1034, -1);

	/**
     * Meldet den gegebenen Benutzer mit seinem Passwort bei dem gewünschten Service an.
     * 
     * @param id Die ID des Service
     * @param name Der Benutzername
     * @param password Das Kennwort für den angegebenen Benutzer
     * 
     * @return Die Session, über den der Zugang zu den Services gewährt wird.
     * 
     * @throws IOException wenn ein I/O-Fehler aufgetreten ist
     * @throws UnknownHostException wenn der Server nicht bekannt ist
     */
    public static NumberSession login(
        ServerId id, 
        String name, 
        char[] password
    ) throws         
        IOException, 
        UnknownHostException
    {
        return new NumberSession(id, Service.login(id.id(), name, password));
    }

}
