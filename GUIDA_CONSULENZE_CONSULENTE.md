# 👨‍💼 Guida Consulenze per il Consulente

## 📋 Panoramica

Come **CONSULENTE**, puoi ricevere richieste di consulenza dagli agricoltori e gestirle attraverso il tuo pannello.

---

## 🔄 Flusso Completo

```
1. Agricoltore prenota consulenza → 2. Consulente riceve richiesta (PRENOTATO)
                                              ↓
3. Consulente accetta → 4. Stato diventa CONFERMATO → 5. Consulenza si svolge
```

---

## 📬 Come Visualizzare le Richieste

### Passo 1: Login
- Accedi come **CONSULENTE**
- Vai al tuo **menu principale**

### Passo 2: Visualizza Richieste
Nel menu principale hai due card:
- **📬 "Richieste Consulenza"** → Mostra tutte le consulenze (incluse quelle da confermare)
- **✅ "Consulenze Accettate"** → Stessa lista (puoi filtrare per stato)

Clicca su una delle due card.

### Passo 3: Lista Appuntamenti
Vedrai una tabella con:
- 📅 **Data e Ora**
- 📝 **Tipo Consulenza** (Online, In Ufficio, Sul Campo)
- 👤 **Cliente** (nome agricoltore)
- 📍 **Luogo**
- 🏷️ **Stato** (Prenotato, Confermato, etc.)

Gli appuntamenti sono colorati in base allo stato:
- 🟡 **Giallo** = PRENOTATO (da confermare)
- 🟢 **Verde** = CONFERMATO
- 🔴 **Rosso** = CANCELLATO

---

## ✅ Come Accettare una Consulenza

### Metodo: Tramite Dettagli

1. **Seleziona** un appuntamento con stato **"Prenotato"**
2. **Clicca** sul pulsante **"Dettagli"**
3. Si apre una finestra con tutti i dettagli:
   - ID appuntamento
   - Tipo di consulenza
   - Data e ora
   - Durata
   - Luogo
   - Cliente
   - Note

4. **Clicca** sul pulsante verde **"Conferma"**
5. ✅ **Appuntamento confermato!**
6. Lo stato cambia da "Prenotato" a "Confermato"
7. Il cliente riceve una notifica (se implementato)

### ⚠️ Nota Importante

Il pulsante **"Conferma"** appare **SOLO** se l'appuntamento è nello stato **"PRENOTATO"**.

Se non vedi il pulsante, significa che:
- L'appuntamento è già confermato
- L'appuntamento è stato cancellato
- L'appuntamento è già completato

---

## 📊 Stati Possibili

| Stato | Significato | Azioni Disponibili |
|-------|-------------|-------------------|
| 🟡 **PRENOTATO** | Richiesta dall'agricoltore | ✅ Conferma, ✏️ Modifica, ❌ Cancella |
| 🟢 **CONFERMATO** | Accettato dal consulente | ✏️ Modifica, ❌ Cancella |
| 🔵 **IN_CORSO** | Consulenza in svolgimento | - |
| ⚫ **COMPLETATO** | Consulenza terminata | - |
| 🔴 **CANCELLATO** | Annullato | - |

---

## 🔍 Filtrare le Richieste

Nella schermata lista appuntamenti:
1. Usa il **dropdown** "Filtro Stato"
2. Seleziona **"Prenotato"** per vedere solo le richieste in attesa
3. Seleziona **"Confermato"** per vedere quelle già accettate
4. Seleziona **"Tutti"** per vedere tutto

---

## ❌ Come Rifiutare una Consulenza

Se non puoi accettare una richiesta:

1. Seleziona l'appuntamento
2. Clicca **"Dettagli"**
3. Clicca **"Cancella"**
4. Inserisci un **motivo** (es. "Non disponibile in quella data")
5. Conferma la cancellazione
6. Lo stato diventa "CANCELLATO_CONSULENTE"

---

## ✏️ Come Modificare una Consulenza

Se vuoi cambiare data/ora/luogo:

1. Seleziona l'appuntamento (deve essere PRENOTATO o CONFERMATO)
2. Clicca **"Modifica"**
3. Modifica i campi necessari
4. Salva
5. Il cliente riceve una notifica della modifica

---

## 📅 Integrazione Google Calendar

Se configurato, gli appuntamenti vengono sincronizzati automaticamente con Google Calendar.

Nella schermata dettaglio, se disponibile, vedrai:
- 📅 **"Apri in Google Calendar"**
- Clicca per aprire l'evento nel tuo calendario

---

## 🧪 Come Testare

### Prerequisiti
1. Avere almeno un consulente registrato nel sistema
2. Avere un agricoltore che prenota una consulenza

### Test Completo

1. **Login come AGRICOLTORE**
   - Vai su "Chiedi Consulenza"
   - Seleziona te stesso come consulente
   - Prenota una consulenza
   - Logout

2. **Login come CONSULENTE**
   - Vai su "Richieste Consulenza"
   - Vedi la richiesta con stato "Prenotato" (giallo)
   - Clicca "Dettagli"
   - Clicca "Conferma" (pulsante verde)
   - ✅ Stato cambia a "Confermato"

3. **Verifica nel Database**
   ```sql
   SELECT * FROM appuntamenti WHERE id_consulente = 'TUO_ID';
   ```
   Dovresti vedere `stato = 'CONFERMATO'`

---

## ❓ Troubleshooting

### Non vedo il pulsante "Conferma"

**Causa:** L'appuntamento non è nello stato "PRENOTATO"

**Soluzione:** 
- Verifica lo stato dell'appuntamento nella lista
- Solo gli appuntamenti gialli (PRENOTATO) possono essere confermati
- Se è già verde, è già stato confermato

### Non vedo richieste

**Causa:** Nessun agricoltore ha prenotato consulenze con te

**Soluzione:**
- Chiedi a un agricoltore di prenotare una consulenza
- Oppure fai il test come descritto sopra

### L'appuntamento non si conferma

**Causa:** Problema con il database

**Soluzione:**
- Verifica la connessione al database
- Controlla i log per errori
- Verifica che la tabella `appuntamenti` esista

---

## 📞 Notifiche (Futura Implementazione)

Quando un agricoltore prenota, potresti ricevere:
- 📧 Email di notifica
- 🔔 Notifica push
- 💬 SMS

Quando confermi, l'agricoltore potrebbe ricevere:
- 📧 Email di conferma
- 🔔 Notifica dell'accettazione

---

## 🎯 Best Practices

1. ✅ **Rispondi rapidamente** alle richieste
2. ✅ **Conferma o rifiuta** entro 24 ore
3. ✅ **Se rifiuti, indica sempre un motivo** chiaro
4. ✅ **Se modifichi, avvisa il cliente** con una nota
5. ✅ **Controlla regolarmente** le nuove richieste

---

**Sviluppato da Rovo Dev** 🤖  
**Data:** Febbraio 2026
