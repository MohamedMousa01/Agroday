-- ============================================================================
-- FIX DEFINITIVO TABELLA APPUNTAMENTI
-- Eseguire RIGA PER RIGA in MySQL Workbench
-- ============================================================================

-- STEP 1: Seleziona il database
USE MOHAMED;

-- STEP 2: Verifica quale tabella esiste attualmente
SHOW TABLES LIKE 'appuntamenti';

-- STEP 3: Mostra la struttura attuale (per vedere cosa c'è)
DESCRIBE appuntamenti;

-- STEP 4: ELIMINA la tabella vecchia (ESEGUI QUESTO!)
DROP TABLE IF EXISTS appuntamenti;

-- STEP 5: Verifica che sia stata eliminata
SHOW TABLES LIKE 'appuntamenti';

-- STEP 6: RICREA la tabella con lo schema CORRETTO
CREATE TABLE appuntamenti (
    id_appuntamento VARCHAR(36) PRIMARY KEY,
    id_cliente VARCHAR(36) NOT NULL,
    id_consulente VARCHAR(36) NOT NULL,
    tipo_consulenza VARCHAR(20) NOT NULL,
    stato VARCHAR(30) NOT NULL,
    data_ora_inizio DATETIME NOT NULL,
    data_ora_fine DATETIME NOT NULL,
    luogo VARCHAR(255),
    note TEXT,
    motivo_cancellazione TEXT,
    data_creazione DATETIME NOT NULL,
    data_ultima_modifica DATETIME NOT NULL,
    google_calendar_event_id VARCHAR(100),
    INDEX idx_cliente (id_cliente),
    INDEX idx_consulente (id_consulente),
    INDEX idx_stato (stato),
    INDEX idx_data_inizio (data_ora_inizio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- STEP 7: VERIFICA che la nuova tabella sia stata creata correttamente
DESCRIBE appuntamenti;

-- STEP 8: Verifica che le colonne siano quelle giuste
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'MOHAMED' 
  AND TABLE_NAME = 'appuntamenti'
ORDER BY ORDINAL_POSITION;

-- Se vedi id_cliente e id_consulente → ✅ TUTTO OK!
-- Se vedi username_agricoltore → ❌ La vecchia tabella non è stata eliminata!
