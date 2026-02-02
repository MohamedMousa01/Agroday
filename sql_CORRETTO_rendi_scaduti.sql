-- ============================================================================
-- SCRIPT CORRETTO: Rendi annunci SCADUTI con data GARANTITA nel passato
-- Database: MOHAMED
-- ============================================================================
-- IMPORTANTE: Il codice usa LocalDate.now().isAfter(dataScadenza)
--             Quindi la data deve essere IERI o prima, non OGGI!
-- ============================================================================

USE MOHAMED;

-- Prima vediamo la situazione attuale
SELECT 'PRIMA DELLA MODIFICA:' AS Info;
SELECT 
    id_annuncio,
    titolo,
    data_scadenza,
    CURDATE() AS oggi,
    DATEDIFF(CURDATE(), data_scadenza) AS giorni_passati,
    CASE 
        WHEN data_scadenza < CURDATE() THEN '✅ SCADUTO (ieri o prima)'
        WHEN data_scadenza = CURDATE() THEN '⚠️ SCADE OGGI (NON scaduto!)'
        ELSE '❌ FUTURO'
    END AS stato_calcolato
FROM annunci
ORDER BY data_scadenza;

-- ============================================================================
-- SOLUZIONE: Imposta la data a 3 GIORNI FA (per essere sicuri)
-- ============================================================================
UPDATE annunci
SET data_scadenza = DATE_SUB(CURDATE(), INTERVAL 3 DAY)
WHERE id_annuncio IN (
    SELECT id_annuncio FROM (
        SELECT id_annuncio 
        FROM annunci 
        ORDER BY data_pubblicazione ASC
        LIMIT 3
    ) AS temp
);

SELECT CONCAT('✅ Modificati ', ROW_COUNT(), ' annunci con data_scadenza a 3 giorni fa') AS Risultato;

-- Verifica il risultato
SELECT 'DOPO LA MODIFICA:' AS Info;
SELECT 
    id_annuncio,
    titolo,
    data_scadenza,
    CURDATE() AS oggi,
    DATEDIFF(CURDATE(), data_scadenza) AS giorni_passati,
    CASE 
        WHEN CURDATE() > data_scadenza THEN '✅ SCADUTO - Funziona!'
        WHEN data_scadenza = CURDATE() THEN '⚠️ SCADE OGGI - NON funziona'
        ELSE '❌ FUTURO - NON funziona'
    END AS verifica
FROM annunci
ORDER BY data_scadenza;

-- ============================================================================
-- ALTERNATIVE:
-- ============================================================================

-- Per rendere scaduti TUTTI gli annunci (3 giorni fa):
-- UPDATE annunci
-- SET data_scadenza = DATE_SUB(CURDATE(), INTERVAL 3 DAY);

-- Per rendere scaduto un annuncio specifico:
-- UPDATE annunci
-- SET data_scadenza = DATE_SUB(CURDATE(), INTERVAL 3 DAY)
-- WHERE id_annuncio = 'TUO_ID_QUI';

-- Per ripristinare (30 giorni nel futuro):
-- UPDATE annunci
-- SET data_scadenza = DATE_ADD(CURDATE(), INTERVAL 30 DAY);
