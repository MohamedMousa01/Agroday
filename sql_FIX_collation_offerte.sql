-- ============================================================================
-- FIX: Allinea le collation tra tabella offerte e annunci
-- ============================================================================
-- Problema: Illegal mix of collations quando si fa JOIN
-- Soluzione: Convertiamo la tabella offerte alla stessa collation di annunci
-- ============================================================================

USE MOHAMED;

-- Prima vediamo le collation attuali
SELECT 
    TABLE_NAME,
    TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'MOHAMED' 
  AND TABLE_NAME IN ('annunci', 'offerte');

-- Mostra le collation delle colonne specifiche
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLLATION_NAME,
    COLUMN_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'MOHAMED' 
  AND TABLE_NAME IN ('annunci', 'offerte')
  AND COLUMN_NAME LIKE '%annuncio%';

-- ============================================================================
-- SOLUZIONE 1: Converti la tabella offerte a utf8mb4_unicode_ci
-- ============================================================================

ALTER TABLE offerte CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ============================================================================
-- SOLUZIONE 2: Se la soluzione 1 non funziona, modifica solo la colonna
-- ============================================================================

-- ALTER TABLE offerte 
-- MODIFY COLUMN id_annuncio VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL;

-- ============================================================================
-- Verifica dopo la modifica
-- ============================================================================

SELECT 
    'DOPO LA MODIFICA:' AS Info;

SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLLATION_NAME
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'MOHAMED' 
  AND TABLE_NAME IN ('annunci', 'offerte')
  AND COLUMN_NAME LIKE '%annuncio%';

-- Test query per verificare che il JOIN funzioni
SELECT 
    'TEST JOIN:' AS Info;

SELECT 
    o.id_offerta,
    o.id_annuncio,
    a.titolo,
    o.prezzo_al_kg
FROM offerte o
JOIN annunci a ON o.id_annuncio = a.idAnnuncio
LIMIT 3;

SELECT 'Se vedi i dati sopra, il problema è risolto!' AS Risultato;
