-- ============================================================================
-- Script per creare la tabella OFFERTE
-- Database: MOHAMED
-- ============================================================================

USE MOHAMED;

CREATE TABLE IF NOT EXISTS offerte (
    id_offerta VARCHAR(45) PRIMARY KEY COMMENT 'UUID univoco dell''offerta',
    id_annuncio VARCHAR(45) NOT NULL COMMENT 'ID dell''annuncio per cui si fa l''offerta',
    username_venditore VARCHAR(100) NOT NULL COMMENT 'Username del venditore che fa l''offerta',
    prezzo_al_kg DECIMAL(10,2) NOT NULL COMMENT 'Prezzo proposto al kg',
    prezzo_totale DECIMAL(10,2) NOT NULL COMMENT 'Prezzo totale (prezzo_al_kg × quantità)',
    data_offerta DATETIME NOT NULL COMMENT 'Data e ora dell''offerta',
    stato VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'Stato: PENDING, ACCETTATA, RIFIUTATA',
    
    INDEX idx_annuncio (id_annuncio),
    INDEX idx_venditore (username_venditore),
    INDEX idx_stato (stato)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabella per memorizzare le offerte dei venditori agli annunci';

-- Verifica
SELECT 'Tabella offerte creata con successo!' AS Status;
DESCRIBE offerte;
