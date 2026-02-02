-- ============================================================================
-- Script di Test per la Tabella Annunci
-- Database: MOHAMED
-- Da eseguire in MySQL Workbench
-- ============================================================================

USE MOHAMED;

-- 1. Verifica che la tabella esista
SELECT 'Verifica struttura tabella Annunci:' AS Info;
DESCRIBE Annunci;

-- 2. Inserisci un annuncio di test
INSERT INTO Annunci 
(idAnnuncio, nome_autore, titolo, descrizione, data_pubblicazione, data_scadenza, citta, quantita_desiderata)
VALUES 
('test-uuid-12345', 'Mario Rossi', 'Cercasi fertilizzante organico', 
 'Necessito di 100 kg di fertilizzante', 
 CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'Roma', 100);

SELECT '✅ Annuncio di test inserito con successo!' AS Status;

-- 3. Verifica l'inserimento
SELECT 'Tutti gli annunci presenti nel database:' AS Info;
SELECT 
    idAnnuncio,
    nome_autore,
    titolo,
    descrizione,
    citta,
    quantita_desiderata,
    data_pubblicazione,
    data_scadenza,
    CASE 
        WHEN data_scadenza >= CURDATE() THEN '✅ ATTIVO'
        ELSE '❌ SCADUTO'
    END AS stato
FROM Annunci
ORDER BY data_pubblicazione DESC;

-- 4. Test query di ricerca per autore
SELECT '📋 Annunci di Mario Rossi:' AS Info;
SELECT titolo, citta, quantita_desiderata FROM Annunci WHERE nome_autore = 'Mario Rossi';

-- 5. Test query annunci attivi
SELECT '🟢 Annunci attivi (non scaduti):' AS Info;
SELECT COUNT(*) AS totale_attivi FROM Annunci WHERE data_scadenza >= CURDATE();

-- 6. Pulizia (opzionale - decommentare per eliminare il test)
-- DELETE FROM Annunci WHERE idAnnuncio = 'test-uuid-12345';
-- SELECT '🗑️ Annuncio di test eliminato' AS Status;

SELECT '
╔══════════════════════════════════════════════════════════════╗
║              ✅ Test completato con successo!                 ║
╚══════════════════════════════════════════════════════════════╝
' AS Risultato;
