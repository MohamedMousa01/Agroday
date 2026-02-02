-- ============================================================================
-- DEBUG: Verifica i valori ESATTI degli annunci nel database
-- ============================================================================

USE MOHAMED;

SELECT 
    id_annuncio AS ID,
    titolo,
    DATE_FORMAT(data_scadenza, '%Y-%m-%d') AS data_scadenza,
    DATE_FORMAT(CURDATE(), '%Y-%m-%d') AS oggi,
    DATEDIFF(CURDATE(), data_scadenza) AS giorni_passati,
    stato AS stato_nel_db,
    CASE 
        WHEN CURDATE() > data_scadenza THEN 'SCADUTO (passato)'
        WHEN CURDATE() = data_scadenza THEN 'SCADE OGGI'
        WHEN CURDATE() < data_scadenza THEN 'ATTIVO (futuro)'
    END AS stato_calcolato_da_data,
    CASE
        WHEN stato = 'SCADUTO' AND CURDATE() <= data_scadenza THEN '⚠️ INCONSISTENTE: stato=SCADUTO ma data futura!'
        WHEN stato = 'ATTIVO' AND CURDATE() > data_scadenza THEN '⚠️ INCONSISTENTE: stato=ATTIVO ma data passata!'
        ELSE '✅ OK'
    END AS verifica_coerenza
FROM annunci
ORDER BY data_scadenza ASC;

-- Conta quanti annunci ci sono per stato
SELECT 
    '=== STATISTICHE ===' AS Info;

SELECT 
    stato AS Stato,
    COUNT(*) AS Numero,
    GROUP_CONCAT(titolo SEPARATOR ', ') AS Annunci
FROM annunci
GROUP BY stato;

-- Verifica annunci che DOVREBBERO essere scaduti
SELECT 
    '=== ANNUNCI CHE DOVREBBERO ESSERE SCADUTI ===' AS Info;

SELECT 
    id_annuncio,
    titolo,
    data_scadenza,
    stato,
    CASE 
        WHEN stato = 'SCADUTO' THEN '✅ Corretto'
        ELSE '❌ BUG: dovrebbe essere SCADUTO!'
    END AS verifica
FROM annunci
WHERE data_scadenza < CURDATE();
