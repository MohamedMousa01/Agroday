-- ============================================================================
-- Script per creare la tabella APPUNTAMENTI
-- Database: MOHAMED
-- Schema compatibile con AppuntamentoDAODB.java
-- ============================================================================

USE MOHAMED;

-- Elimina la tabella se esiste già (per ricrearla con lo schema corretto)
DROP TABLE IF EXISTS appuntamenti;

-- Crea la tabella appuntamenti con lo schema CORRETTO
CREATE TABLE appuntamenti (
    id_appuntamento VARCHAR(36) PRIMARY KEY COMMENT 'UUID univoco dell''appuntamento',
    id_cliente VARCHAR(36) NOT NULL COMMENT 'ID dell''agricoltore (username)',
    id_consulente VARCHAR(36) NOT NULL COMMENT 'ID del consulente (username)',
    tipo_consulenza VARCHAR(20) NOT NULL COMMENT 'ONLINE, IN_UFFICIO, SUL_CAMPO',
    stato VARCHAR(30) NOT NULL COMMENT 'PRENOTATO, CONFERMATO, COMPLETATO, ANNULLATO...',
    data_ora_inizio DATETIME NOT NULL COMMENT 'Data e ora inizio appuntamento',
    data_ora_fine DATETIME NOT NULL COMMENT 'Data e ora fine appuntamento',
    luogo VARCHAR(255) COMMENT 'Luogo (indirizzo o link meeting)',
    note TEXT COMMENT 'Note aggiuntive',
    motivo_cancellazione TEXT COMMENT 'Motivo se annullato',
    data_creazione DATETIME NOT NULL COMMENT 'Data creazione richiesta',
    data_ultima_modifica DATETIME NOT NULL COMMENT 'Data ultima modifica',
    google_calendar_event_id VARCHAR(100) COMMENT 'ID evento Google Calendar',
    
    -- Indici per ottimizzare le query
    INDEX idx_cliente (id_cliente),
    INDEX idx_consulente (id_consulente),
    INDEX idx_stato (stato),
    INDEX idx_data_inizio (data_ora_inizio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabella appuntamenti - Schema compatibile con AppuntamentoDAODB';

-- Query di verifica
SELECT 'Tabella appuntamenti creata con successo!' AS Status;
DESCRIBE appuntamenti;

SELECT '
╔══════════════════════════════════════════════════════════════╗
║  ✅ Tabella creata con lo schema CORRETTO!                   ║
║  Ora puoi creare consulenze dall''applicazione Java.         ║
╚══════════════════════════════════════════════════════════════╝
' AS Info;
