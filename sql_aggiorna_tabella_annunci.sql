-- ============================================================================
-- Script per aggiornare la tabella ANNUNCI con i nuovi campi
-- Database: MOHAMED
-- ============================================================================

USE MOHAMED;

-- Aggiungi colonna stato se non esiste
ALTER TABLE Annunci 
ADD COLUMN IF NOT EXISTS stato VARCHAR(20) NOT NULL DEFAULT 'ATTIVO' 
COMMENT 'Stato annuncio: ATTIVO, SCADUTO'
AFTER data_scadenza;

-- Aggiungi colonna quantita_totale se non esiste
ALTER TABLE Annunci 
ADD COLUMN IF NOT EXISTS quantita_totale INT NOT NULL DEFAULT 0
COMMENT 'Quantità totale richiesta (autore + partecipanti)'
AFTER quantita_desiderata;

-- Aggiorna quantita_totale per gli annunci esistenti
UPDATE Annunci 
SET quantita_totale = quantita_desiderata 
WHERE quantita_totale = 0;

-- Aggiorna lo stato degli annunci scaduti
UPDATE Annunci 
SET stato = 'SCADUTO' 
WHERE data_scadenza < CURDATE() AND stato = 'ATTIVO';

-- Aggiungi indice per lo stato
CREATE INDEX IF NOT EXISTS idx_stato ON Annunci(stato);

-- Query di verifica
SELECT 'Tabella Annunci aggiornata con successo!' AS Status;
DESCRIBE Annunci;

-- Mostra gli annunci con i nuovi campi
SELECT idAnnuncio, titolo, quantita_desiderata, quantita_totale, stato, data_scadenza 
FROM Annunci 
LIMIT 5;
