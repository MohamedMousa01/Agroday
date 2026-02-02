package engclasses.pattern.ViewFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManagerGUI {

        private static Stage stage;
        private static FXMLLoader currentLoader;
        private static boolean mostraSoloMieiAnnunci = false;
        private static boolean mostraPartecipazioniUtente = false;

        private SceneManagerGUI() {
            // Costruttore privato per utility class
        }

        /**
         * Imposta il flag per mostrare solo gli annunci dell'utente loggato
         * @param soloMiei true per mostrare solo i propri annunci, false per tutti
         */
        public static void setMostraSoloMieiAnnunci(boolean soloMiei) {
            mostraSoloMieiAnnunci = soloMiei;
        }

        /**
         * Ottiene il flag per il filtro annunci
         * @return true se mostrare solo i propri annunci
         */
        public static boolean isMostraSoloMieiAnnunci() {
            return mostraSoloMieiAnnunci;
        }

        /**
         * Imposta il flag per mostrare solo le partecipazioni dell'utente
         * @param mostraPartecipazioni true per mostrare annunci a cui ha partecipato
         */
        public static void setMostraPartecipazioniUtente(boolean mostraPartecipazioni) {
            mostraPartecipazioniUtente = mostraPartecipazioni;
        }

        /**
         * Ottiene il flag per le partecipazioni
         * @return true se mostrare solo partecipazioni
         */
        public static boolean isMostraPartecipazioniUtente() {
            return mostraPartecipazioniUtente;
        }

        public static void setStage(Stage primaryStage) {
            stage = primaryStage;
        }

        public static Stage getStage() {
            return stage;
        }

        /**
         * Carica una nuova scena FXML e la mostra nello stage principale
         * @param fxmlPath percorso del file FXML (es: "/login-view.fxml")
         */
        public static void load(String fxmlPath) {
            try {
                currentLoader = new FXMLLoader(
                        SceneManagerGUI.class.getResource(fxmlPath)
                );
                
                // Se è la view degli annunci, imposta i flag PRIMA di caricare
                if (fxmlPath.contains("VisualizzaAnnunci")) {
                    currentLoader.setControllerFactory(controllerClass -> {
                        try {
                            Object controller = controllerClass.getDeclaredConstructor().newInstance();
                            if (controller instanceof controllers.grafico.gui.VisualizzaAnnunciGUIController) {
                                ((controllers.grafico.gui.VisualizzaAnnunciGUIController) controller)
                                    .setMostraSoloMieiAnnunci(mostraSoloMieiAnnunci);
                                ((controllers.grafico.gui.VisualizzaAnnunciGUIController) controller)
                                    .setMostraPartecipazioni(mostraPartecipazioniUtente);
                                // Reset flag dopo l'uso
                                mostraSoloMieiAnnunci = false;
                                mostraPartecipazioniUtente = false;
                            }
                            return controller;
                        } catch (Exception e) {
                            throw new RuntimeException("Errore nella creazione del controller", e);
                        }
                    });
                }
                
                Scene scene = new Scene(currentLoader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException("Errore caricamento scena " + fxmlPath, e);
            }
        }

        /**
         * Carica una nuova scena con titolo personalizzato
         * @param fxmlPath percorso del file FXML
         * @param title titolo della finestra
         */
        public static void load(String fxmlPath, String title) {
            load(fxmlPath);
            stage.setTitle(title);
        }

        /**
         * Carica una scena con dimensioni personalizzate
         * @param fxmlPath percorso del file FXML
         * @param width larghezza della finestra
         * @param height altezza della finestra
         */
        public static void load(String fxmlPath, double width, double height) {
            try {
                currentLoader = new FXMLLoader(
                        SceneManagerGUI.class.getResource(fxmlPath)
                );
                Scene scene = new Scene(currentLoader.load(), width, height);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException("Errore caricamento scena " + fxmlPath, e);
            }
        }

        /**
         * Ottiene il controller dell'ultima scena caricata
         * @param <T> tipo del controller
         * @return il controller della scena corrente
         */
        public static <T> T getController() {
            if (currentLoader == null) {
                throw new IllegalStateException("Nessuna scena caricata");
            }
            return currentLoader.getController();
        }

        /**
         * Chiude la finestra principale
         */
        public static void close() {
            if (stage != null) {
                stage.close();
            }
        }

        /**
         * Imposta se la finestra è ridimensionabile
         * @param resizable true se ridimensionabile
         */
        public static void setResizable(boolean resizable) {
            if (stage != null) {
                stage.setResizable(resizable);
            }
        }
    }

