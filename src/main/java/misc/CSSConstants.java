package misc;

/**
 * Costanti per gli stili CSS JavaFX.
 */
public final class CSSConstants {

    // Colori e testo
    public static final String ERROR_TEXT_STYLE = "-fx-text-fill: red; -fx-font-weight: bold;";
    public static final String BOLD_TEXT_STYLE = "-fx-font-weight: bold; -fx-text-fill: #333;";
    public static final String SUBTITLE_STYLE = "-fx-text-fill: #666; -fx-font-size: 12px;";
    
    // Dimensioni font
    public static final String FONT_SIZE_24 = "-fx-font-size: 24px;";
    
    // Bordi e padding
    public static final String ROUNDED_BUTTON_STYLE = "-fx-background-radius: 15; -fx-padding: 8 20;";
    
    private CSSConstants() {
        // Classe di costanti - non istanziabile
    }
}
