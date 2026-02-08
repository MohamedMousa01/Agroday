package view.cli;
import misc.UIConstants;

import engclasses.beans.AnnuncioBean;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * View CLI per visualizzare e gestire gli annunci
 */
public class VisualizzaAnnunciCLIView {

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ==================== OUTPUT ====================

    public void mostraTitolo(boolean mieiAnnunci) {
        clearScreen();
        if (mieiAnnunci) {
            System.out.println(UIConstants.BOX_TOP_LONG);
            System.out.println("║            📋 I MIEI ANNUNCI 📋                        ║");
            System.out.println(UIConstants.BOX_BOTTOM_LONG);
        } else {
            System.out.println(UIConstants.BOX_TOP_LONG);
            System.out.println("║            📦 TUTTI GLI ANNUNCI 📦                     ║");
            System.out.println(UIConstants.BOX_BOTTOM_LONG);
        }
    }

    public void mostraListaAnnunci(List<AnnuncioBean> annunci) {
        if (annunci == null || annunci.isEmpty()) {
            mostraMessaggio("Nessun annuncio disponibile al momento.");
            return;
        }

        System.out.println("\nTrovati " + annunci.size() + " annunci:\n");
        
        for (int i = 0; i < annunci.size(); i++) {
            AnnuncioBean ann = annunci.get(i);
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│  [" + (i + 1) + "] " + padRight(ann.getTitolo(), 49) + "│");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.println("│  👤 Autore:      " + padRight(ann.getAutore(), 37) + "│");
            System.out.println("│  📝 Descrizione: " + padRight(truncate(ann.getDescrizione(), 35), 37) + "│");
            System.out.println("│  📦 Quantità:    " + padRight(ann.getQuantita() + " kg", 37) + "│");
            System.out.println("│  📍 Città:       " + padRight(ann.getCitta(), 37) + "│");
            
            if (ann.getDataScadenza() != null) {
                System.out.println("│  📅 Scadenza:    " + padRight(ann.getDataScadenza().format(dateFormatter), 37) + "│");
            }
            
            System.out.println("└────────────────────────────────────────────────────────┘\n");
        }
    }

    public void mostraDettaglioAnnuncio(AnnuncioBean ann) {
        clearScreen();
        System.out.println(UIConstants.BOX_TOP_LONG);
        System.out.println("║            📋 DETTAGLIO ANNUNCIO                       ║");
        System.out.println(UIConstants.BOX_BOTTOM_LONG);
        System.out.println("\n  📌 Titolo:        " + ann.getTitolo());
        System.out.println("  👤 Autore:        " + ann.getAutore());
        System.out.println("  📝 Descrizione:   " + ann.getDescrizione());
        System.out.println("  📦 Quantità:      " + ann.getQuantita() + " kg");
        System.out.println("  📍 Città:         " + ann.getCitta());
        
        if (ann.getDataCreazione() != null) {
            System.out.println("  📅 Creato il:     " + ann.getDataCreazione().format(dateFormatter));
        }
        if (ann.getDataScadenza() != null) {
            System.out.println("  📅 Scade il:      " + ann.getDataScadenza().format(dateFormatter));
        }
        if (ann.getPrezzo() > 0) {
            System.out.println("  💰 Prezzo:        €" + String.format("%.2f", ann.getPrezzo()));
        }
        
        mostraSeparatore();
    }

    public void mostraMenuAzioni(boolean isMioAnnuncio) {
        System.out.println("\n┌────────────────────────────────────────────────────────┐");
        System.out.println("│  AZIONI                                                │");
        System.out.println("├────────────────────────────────────────────────────────┤");
        
        if (isMioAnnuncio) {
            System.out.println("│  1) ✏️  Modifica Annuncio                               │");
            System.out.println("│  2) 🗑️  Elimina Annuncio                                │");
            System.out.println("│  3) 👁️  Visualizza Dettagli                             │");
        } else {
            System.out.println("│  1) 👁️  Visualizza Dettagli                             │");
            System.out.println("│  2) 📧 Contatta Venditore                              │");
            System.out.println("│  3) 🛒 Effettua Ordine                                 │");
        }
        
        System.out.println("│  0) 🔙 Torna Indietro                                  │");
        System.out.println("└────────────────────────────────────────────────────────┘");
    }

    public void mostraMessaggio(String messaggio) {
        System.out.println("\n💬 " + messaggio);
    }

    public void mostraSuccesso(String messaggio) {
        System.out.println("\n✅ " + messaggio);
    }

    public void mostraErrore(String errore) {
        System.out.println("\n❌ ERRORE: " + errore);
    }

    public void nessunoAnnuncio() {
        mostraMessaggio("Non hai ancora creato nessun annuncio.");
        mostraMessaggio("Crea il tuo primo annuncio dal menu principale!");
    }

    // ==================== INPUT ====================

    public int chiediScelta() {
        System.out.print("\n➤ Scegli un'opzione: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int chiediNumeroAnnuncio(int max) {
        System.out.print("\n➤ Inserisci il numero dell'annuncio (1-" + max + "): ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void attendiInvio() {
        System.out.print("\nPremi INVIO per continuare...");
        scanner.nextLine();
    }

    public boolean confermaEliminazione() {
        System.out.print("\n⚠️  Sei sicuro di voler eliminare questo annuncio? (S/N): ");
        String risposta = scanner.nextLine().trim().toUpperCase();
        return risposta.equals("S") || risposta.equals("SI") || risposta.equals("Y") || risposta.equals("YES");
    }

    // ==================== UTILITY ====================

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private String padRight(String s, int n) {
        if (s == null) s = "";
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        // Padding manuale con spazi (più efficiente e evita problemi di format)
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 3) + "...";
    }

    public void mostraSeparatore() {
        System.out.println(UIConstants.BOX_SEPARATOR_LONG);
    }
}
