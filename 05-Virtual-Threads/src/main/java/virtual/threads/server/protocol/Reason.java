
package virtual.threads.server.protocol;

/**
 * Beschreibt den Grund, warum die Anfrage nicht bearbeitet werden konnte.
 */
public enum Reason {

    /**
     * Das übermittelte Objekt hat keinen bekannten Request-Typ.
     */
    UNKNOWN_REQUEST,
    
    /**
     * Die für das übermittelte Objekt benötigte Klassendatei kann nicht 
     * gefunden werden.
     */
    UNKNOWN_CLASS,
    
    /**
     * Ein Parameter innerhalb des Request-Objects ist ungültig.
     */
    ILLEGAL_PARAMETER,

    /**
     * Ein Parameter innerhalb des Request-Objects ist ungültig.
     */
    UNKNOWN_SESSION,
        
    /**
     * Der Service erzeugte einen Fehler.
     */
    SERVER_ERROR,
    
    /**
     * Die Zugangsdaten sind nicht korrekt.
     */
    LOGIN_FAILED,
    
    /**
     * Der Service wurde wegen fehlender Rechte verweigert.
     */
    ACCESS_DENIED,

    /**
     * Der Service wurde mit einem simulierten Fehler beendet.
     */
    ERROR_REQUEST,

    /**
     * Der Service wurde beim Warten unterbrochen.
     */
    INTERRUPTED,

    /**
     * Der Service wurde wegen eines I/O-Problems beendet.
     */
    IO_ERROR

}
