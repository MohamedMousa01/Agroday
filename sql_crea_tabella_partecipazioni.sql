-- ============================================================================
-- Script per creare la tabella PARTECIPAZIONI
-- Database: MOHAMED
-- ============================================================================

USE MOHAMED;

-- Crea la tabella partecipazioni
CREATE TABLE IF NOT EXISTS partecipazioni (
    id_partecipazione VARCHAR(45) PRIMARY KEY COMMENT 'UUID univoco della partecipazione',
    id_annuncio VARCHAR(45) NOT NULL COMMENT 'ID dell''annuncio a cui si partecipa',
    id_agricoltore VARCHAR(100) NOT NULL COMMENT 'Username dell''agricoltore che partecipa',
    quantita_richiesta INT NOT NULL COMMENT 'Quantità richiesta dall''agricoltore',
    data_partecipazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data di partecipazione',
    
    -- Indici per ottimizzare le query
    INDEX idx_annuncio (id_annuncio),
    INDEX idx_agricoltore (id_agricoltore),
    
    -- Constraint: un agricoltore può partecipare una sola volta ad un annuncio
    UNIQUE KEY uk_annuncio_agricoltore (id_annuncio, id_agricoltore),
    
    -- Foreign key (opzionale, se vuoi integrità referenziale)
    -- FOREIGN KEY (id_annuncio) REFERENCES Annunci(idAnnuncio) ON DELETE CASCADE
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabella per memorizzare le partecipazioni agli annunci di gruppo';

-- Query di verifica
SELECT 'Tabella partecipazioni creata con successo!' AS Status;
DESCRIBE partecipazioni;
