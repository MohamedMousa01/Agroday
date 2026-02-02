-- ============================================================================
-- DEBUG: Verifica completa delle offerte e degli annunci
-- ============================================================================

USE MOHAMED;

-- 1. Mostra tutte le offerte
SELECT 
    '=== TUTTE LE OFFERTE ===' AS Info;

SELECT 
    o.id_offerta,
    o.id_annuncio,
    o.username_venditore,
    o.prezzo_al_kg,
    o.stato,
    o.data_offerta
FROM offerte o;

-- 2. Mostra tutti gli annunci
SELECT 
    '=== TUTTI GLI ANNUNCI ===' AS Info;

SELECT 
    idAnnuncio,
    titolo,
    nome_autore,
    data_scadenza,
    stato
FROM annunci;

-- 3. Test del JOIN (come fa l'applicazione)
SELECT 
    '=== TEST JOIN (come nell''applicazione) ===' AS Info;

SELECT 
    o.id_offerta,
    o.id_annuncio,
    o.username_venditore,
    o.prezzo_al_kg,
    a.idAnnuncio AS annuncio_id,
    a.titolo AS annuncio_titolo,
    a.nome_autore AS autore
FROM offerte o
JOIN annunci a ON o.id_annuncio COLLATE utf8mb4_unicode_ci = a.idAnnuncio COLLATE utf8mb4_unicode_ci;

-- 4. Per ogni autore, conta le offerte ricevute
SELECT 
    '=== OFFERTE PER AUTORE ===' AS Info;

SELECT 
    a.nome_autore,
    COUNT(o.id_offerta) AS numero_offerte,
    GROUP_CONCAT(o.username_venditore SEPARATOR ', ') AS venditori
FROM annunci a
LEFT JOIN offerte o ON o.id_annuncio COLLATE utf8mb4_unicode_ci = a.idAnnuncio COLLATE utf8mb4_unicode_ci
GROUP BY a.nome_autore;

-- 5. Verifica username esatti (case sensitive!)
SELECT 
    '=== VERIFICA MATCH USERNAME ===' AS Info;

SELECT DISTINCT
    a.nome_autore,
    CHAR_LENGTH(a.nome_autore) AS lunghezza,
    HEX(a.nome_autore) AS hex_value
FROM annunci a
WHERE EXISTS (
    SELECT 1 FROM offerte o 
    WHERE o.id_annuncio COLLATE utf8mb4_unicode_ci = a.idAnnuncio COLLATE utf8mb4_unicode_ci
);

-- 6. Simula la query dell'applicazione per uno specifico utente
-- SOSTITUISCI 'NOME_UTENTE' con l'username dell'agricoltore loggato
SELECT 
    '=== OFFERTE PER UTENTE SPECIFICO ===' AS Info;
    
-- Mostra tutti gli username disponibili
SELECT DISTINCT nome_autore FROM annunci;

-- Per testare, sostituisci 'SOSTITUISCI_QUI' con l'username reale
-- SELECT o.* 
-- FROM offerte o
-- JOIN annunci a ON o.id_annuncio COLLATE utf8mb4_unicode_ci = a.idAnnuncio COLLATE utf8mb4_unicode_ci
-- WHERE a.nome_autore = 'SOSTITUISCI_QUI'
-- ORDER BY o.data_offerta DESC;
