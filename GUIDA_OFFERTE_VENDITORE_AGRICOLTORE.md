# 💰 Sistema di Offerte - Guida Completa

## 📋 Panoramica

Il sistema di offerte permette ai **venditori** di proporre prezzi sugli annunci **scaduti** degli agricoltori, e agli **agricoltori** di ricevere, visualizzare e gestire queste offerte.

## 🔄 Flusso Completo

```
1. Agricoltore crea annuncio → 2. Annuncio scade → 3. Venditore propone prezzo 
     ↓                                                         ↓
   ATTIVO                                            Offerta PENDING
                                                              ↓
4. Agricoltore visualizza offerte → 5. Agricoltore accetta/rifiuta
                                              ↓
                                    Offerta ACCETTATA/RIFIUTATA
```

---

## 👨‍🌾 Per l'Agricoltore

### 1️⃣ Come Visualizzare le Offerte Ricevute

1. **Accedi** come Agricoltore
2. Dal menu principale, clicca su **"💰 Offerte Ricevute"**
3. Vedrai tutte le offerte ricevute sui tuoi annunci, raggruppate per annuncio

### 2️⃣ Cosa Vedi nella Schermata Offerte

Per ogni offerta visualizzi:
- 👤 **Nome del venditore** che ha proposto l'offerta
- 💰 **Prezzo al kg** e **prezzo totale**
- 📅 **Data e ora** dell'offerta
- ⏳ **Stato**: In Attesa | Accettata | Rifiutata

### 3️⃣ Come Gestire le Offerte

#### ✅ Accettare un'Offerta

1. Clicca su **"✅ Accetta"** accanto all'offerta desiderata
2. Conferma l'accettazione nel popup
3. L'offerta cambierà stato da "⏳ In Attesa" a "✅ Accettata"

#### ❌ Rifiutare un'Offerta

1. Clicca su **"❌ Rifiuta"** accanto all'offerta
2. Conferma il rifiuto
3. L'offerta cambierà stato a "❌ Rifiutata"

⚠️ **Nota:** Puoi accettare/rifiutare solo le offerte in stato "⏳ In Attesa"

---

## 👔 Per il Venditore

### 1️⃣ Come Proporre un Prezzo

1. **Accedi** come Venditore
2. Naviga verso **"Visualizza Annunci"**
3. Trova un annuncio con stato **"⏰ SCADUTO"**
4. Clicca su **"💰 Proponi Prezzo"**
5. Inserisci il **prezzo al kg** che vuoi proporre
6. Clicca su **"Conferma"**

### 2️⃣ Requisiti per Proporre un'Offerta

- ✅ L'annuncio deve essere **SCADUTO** (data di scadenza passata)
- ✅ Il prezzo deve essere **maggiore di 0**
- ✅ Devi essere loggato come **VENDITORE**

### 3️⃣ Cosa Succede dopo aver Proposto

1. L'offerta viene salvata nel database con stato **"PENDING"**
2. L'agricoltore riceve l'offerta nella sua area **"Offerte Ricevute"**
3. L'agricoltore può **accettare** o **rifiutare** l'offerta
4. Ricevi una notifica (futura implementazione) sullo stato dell'offerta

---

## 🗄️ Database

### Tabella: `offerte`

```sql
CREATE TABLE offerte (
    id_offerta VARCHAR(45) PRIMARY KEY,
    id_annuncio VARCHAR(45) NOT NULL,
    username_venditore VARCHAR(100) NOT NULL,
    prezzo_al_kg DECIMAL(10,2) NOT NULL,
    prezzo_totale DECIMAL(10,2) NOT NULL,
    data_offerta DATETIME NOT NULL,
    stato VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);
```

### Stati Possibili

- **PENDING**: Offerta in attesa di risposta
- **ACCETTATA**: Offerta accettata dall'agricoltore
- **RIFIUTATA**: Offerta rifiutata dall'agricoltore

---

## 🧪 Come Testare

### Setup

1. **Crea la tabella offerte:**
   ```bash
   mysql -u root -p MOHAMED < sql_crea_tabella_offerte.sql
   ```

2. **Rendi alcuni annunci scaduti:**
   ```bash
   mysql -u root -p MOHAMED < sql_CORRETTO_rendi_scaduti.sql
   ```

3. **Avvia l'applicazione:**
   ```bash
   mvn clean javafx:run
   ```

### Test Completo

1. ✅ **Login come VENDITORE**
2. ✅ Visualizza annunci → Trova annunci SCADUTI
3. ✅ Clicca "💰 Proponi Prezzo" e inserisci un prezzo
4. ✅ **Logout e Login come AGRICOLTORE** (autore dell'annuncio)
5. ✅ Clicca "💰 Offerte Ricevute"
6. ✅ Visualizza l'offerta ricevuta
7. ✅ Accetta o rifiuta l'offerta
8. ✅ Verifica che lo stato cambi correttamente

---

## 📊 Architettura

### File Creati/Modificati

#### DAO Layer
- ✅ `OffertaDAO.java` - Aggiunto metodo `trovaOfferteRicevutePerAgricoltore()`
- ✅ `OffertaDAODB.java` - Implementazione con JOIN su annunci
- ✅ `OffertaDAOFile.java` - Implementazione base
- ✅ `OffertaDAOMemory.java` - Implementazione base

#### Controller Layer
- ✅ `OffertaController.java` - Aggiunti metodi:
  - `getOfferteRicevute()`
  - `getOfferteRicevutePending()`
  - `accettaOfferta()`
  - `rifiutaOfferta()`

#### GUI Layer
- ✅ `VisualizzaOfferteGUIController.java` - Nuovo controller per visualizzare offerte
- ✅ `VisualizzaOfferteView.fxml` - Nuova view
- ✅ `MainGUIController.java` - Aggiunto metodo `showOfferteRicevute()`
- ✅ `MainView-Agricoltore.fxml` - Aggiunta card "Offerte Ricevute"

#### Configuration
- ✅ `ViewType.java` - Aggiunto `VISUALIZZA_OFFERTE`
- ✅ `ViewManager.java` - Aggiunto routing per offerte

---

## 🎯 Funzionalità Implementate

### ✅ Complete

- [x] Visualizzazione offerte ricevute per l'agricoltore
- [x] Raggruppamento offerte per annuncio
- [x] Accettazione offerta con conferma
- [x] Rifiuto offerta con conferma
- [x] Interfaccia grafica moderna e responsive
- [x] Stati visivi per le offerte (pending/accettata/rifiutata)
- [x] Join SQL tra offerte e annunci
- [x] Integrazione completa nel menu agricoltore

### 🔮 Possibili Miglioramenti Futuri

- [ ] Notifiche push al venditore quando l'offerta viene accettata/rifiutata
- [ ] Storico completo delle offerte con filtri
- [ ] Chat tra venditore e agricoltore per negoziazione
- [ ] Sistema di rating per venditori
- [ ] Export delle offerte in PDF/Excel
- [ ] Dashboard analytics per venditori e agricoltori

---

## ❓ FAQ

### Q: Cosa succede se accetto un'offerta?
**A:** Lo stato dell'offerta cambia da "PENDING" a "ACCETTATA". In futuro, il venditore riceverà una notifica e potranno procedere con la transazione.

### Q: Posso modificare un'offerta dopo averla proposta?
**A:** No, attualmente non è possibile. Dovrai proporre una nuova offerta.

### Q: Posso vedere le offerte ricevute su annunci ancora attivi?
**A:** No, le offerte possono essere proposte solo su annunci SCADUTI.

### Q: Un venditore può proporre più offerte sullo stesso annuncio?
**A:** Sì, può proporre diverse offerte con prezzi diversi.

### Q: Cosa succede se non accetto/rifiuto un'offerta?
**A:** Rimane in stato "PENDING" indefinitamente. Puoi gestirla quando vuoi.

---

**Sviluppato da Rovo Dev** 🤖  
**Data:** Febbraio 2026
