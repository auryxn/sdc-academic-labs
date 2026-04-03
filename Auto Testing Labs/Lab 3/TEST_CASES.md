# Auto Testing Lab 3: Testing "Silent Bob" Coffee Shop System

## 1. System Requirements Summary
(Refer to COFFEE_SHOP_REQUIREMENTS.md for full details)
*   Zero human interaction.
*   AI facial analysis for mood-based coffee selection.
*   Automated fines and "Sad Bagpipe" audio for barista verbal mistakes.
*   Crypto and Postage Stamp payments.

---

## 2. Test Cases

### TC-01: Successful End-to-End Order (Happy Path)
- **Goal:** Verify that a client can order and receive coffee without any human interaction.
- **Steps:**
    1. Client enters a soundproof booth.
    2. Client selects "Espresso" in the app.
    3. Client pays via Bitcoin wallet.
    4. Barista prepares coffee in silence.
    5. Coffee appears in the rotating airlock.
- **Expected Result:** Coffee delivered, 0 words spoken, payment confirmed.

### TC-02: Barista Verbal Violation (Negative Path)
- **Goal:** Verify that the system correctly penalizes the barista for speaking.
- **Steps:**
    1. Barista says "Good morning" during preparation.
    2. Sound sensors detect the voice.
- **Expected Result:** 
    1. System logs a fine in the payroll database.
    2. "Sad Bagpipe" audio file plays immediately in the booth and service area.

### TC-03: AI Facial Analysis Logic
- **Goal:** Verify that the AI correctly suggests coffee based on facial expression.
- **Steps:**
    1. Client looks into the camera with a "severe morning hatred" expression (scowl/frown).
    2. AI processes the image.
- **Expected Result:** System suggests a "Triple Shot Espresso" or the strongest available drink.

### TC-04: Invalid Payment Method
- **Goal:** Verify that the system rejects standard currency.
- **Steps:**
    1. Client attempts to pay with a credit card or cash.
- **Expected Result:** System displays an error: "Only Cryptocurrency or Rare Postage Stamps are accepted for elitist transactions."

### TC-05: Rotating Airlock Safety
- **Goal:** Verify that the airlock prevents physical contact.
- **Steps:**
    1. Barista places coffee in the inner part of the airlock.
    2. Airlock rotates to the client's side.
- **Expected Result:** Client retrieves coffee; at no point are the barista's hands visible to the client.

---

## 3. Automation Strategy
- **UI Testing:** Selenium/Playwright for the mobile app interface.
- **API Testing:** REST Assured for the payment gateway and order processing.
- **Audio Testing:** Use specialized sound detection mocks to verify bagpipe triggers.
- **AI Testing:** Use a set of labeled face datasets (Angry, Neutral, Happy) to verify suggestion accuracy.
