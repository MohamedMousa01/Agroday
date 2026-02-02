# 📋 Guida Setup Tabelle Database

## 🎯 Problema Risolto
Gli annunci scaduti ora si vedono correttamente! ✅

## ❌ Problema Attuale
Quando provi a proporre un prezzo, ottieni: **"Errore nel salvataggio dell'offerta"**

**Causa:** La tabella `offerte` non esiste nel database.

## ✅ Soluzione: Crea le Tabelle Mancanti

### Passo 1: Crea la Tabella Offerte

```bash
mysql -u root -p MOHAMED < sql_crea_tabella_offerte.sql
```

**Oppure** dal client MySQL (MySQL Workbench, phpMyAdmin, etc.):
```sql
USE MOHAMED;
source sql_crea_tabella_offerte.sql;
```

### Passo 2: Verifica che la Tabella sia Stata Creata

```sql
USE MOHAMED;
SHOW TABLES;
DESCRIBE offerte;
```

Dovresti vedere:
- ✅ `id_offerta` (VARCHAR)
- ✅ `id_annuncio` (VARCHAR)
- ✅ `username_venditore` (VARCHAR)
- ✅ `prezzo_al_kg` (DECIMAL)
- ✅ `prezzo_totale` (DECIMAL)
- ✅ `data_offerta` (DATETIME)
- ✅ `stato` (VARCHAR)

### Passo 3: Riprova a Proporre il Prezzo

Ora quando provi a proporre un prezzo come venditore, dovrebbe funzionare! 🎉

## 📚 Setup Completo Database

Se vuoi creare TUTTE le tabelle necessarie per l'applicazione, esegui questi script in ordine:

```bash
# 1. Tabella annunci (se non esiste già)
# mysql -u root -p MOHAMED < sql_crea_tabella_annunci.sql

# 2. Tabella partecipazioni
mysql -u root -p MOHAMED < sql_crea_tabella_partecipazioni.sql

# 3. Tabella offerte
mysql -u root -p MOHAMED < sql_crea_tabella_offerte.sql

# 4. Tabella appuntamenti
mysql -u root -p MOHAMED < sql_crea_tabella_appuntamenti.sql
```

## 🧪 Test della Funzionalità Offerte

Dopo aver creato la tabella:

1. ✅ **Login come VENDITORE**
2. ✅ **Visualizza annunci scaduti** (quelli con data nel passato)
3. ✅ **Clicca su "💰 Proponi Prezzo"**
4. ✅ **Inserisci un prezzo al kg**
5. ✅ **Conferma l'offerta**
6. ✅ **Verifica che l'offerta sia salvata:**
   ```sql
   SELECT * FROM offerte;
   ```

## ⚠️ Nota Importante

Le tabelle vanno create **una sola volta**. Gli script usano `CREATE TABLE IF NOT EXISTS`, quindi è sicuro rieseguirli senza problemi.

## ❓ Troubleshooting

### Errore: "Table already exists"
✅ Normale, la tabella esiste già. Vai avanti.

### Errore: "Access denied"
❌ Verifica username e password MySQL.

### Errore: "Database MOHAMED doesn't exist"
❌ Crea prima il database:
```sql
CREATE DATABASE IF NOT EXISTS MOHAMED;
```

---

**Creato da Rovo Dev** 🤖
