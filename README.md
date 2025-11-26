# Assessing the Security Coverage of the Google Play Integrity API on Android

This repository contains the artefacts of the study **"Assessing the Security Coverage of the Google Play Integrity API on Android"**, including the Android test application, backend service, experimental scripts, and consolidated results.

The goal of this work is to empirically evaluate how far the Google Play Integrity API can go in terms of **security coverage** against realistic attack scenarios on Android, and to identify **gaps** and **mitigation strategies** from a backend-centric perspective.

---

## Repository Structure


- `app/`  
  Native Android app (Java/Kotlin) used to trigger **Classic** and **Standard** Play Integrity requests and to collect client-side logs.

- `backendpython/`  
  Python backend responsible for:
  - Receiving integrity tokens from the app  
  - Validating JWS responses using Google’s public keys  
  - Consolidating verdicts and contextual metadata  
  - Exporting logs for later analysis

- `experiments/`  
  Scripts and configuration files used to orchestrate the experimental scenarios (e.g., device setup notes, Frida/Magisk scripts, replay scripts).

- `data/`  
  Collected datasets and logs used in the study (sanitized).  
  Examples: JSON/CSV with verdicts, timestamps, device state, scenario labels.


---

## Experimental Scenarios

The study evaluates the Play Integrity API under multiple realistic conditions:

1. **Device Integrity**  
   - Clean devices, rooted devices, unlocked bootloader, and emulators.  
   - Analysis of how MEETS_BASIC/MEETS_DEVICE_INTEGRITY/MEETS_STRONG_INTEGRITY behave across device states and Android versions.

2. **APK Modification**  
   - Repackaging, resigning and tampering with the application.  
   - Evaluation of app identity and signature checks and how they are reflected in `appRecognitionVerdict`.

3. **Dynamic Instrumentation Environment**  
   - Execution under tools such as **Frida** and **Magisk/Xposed**.  
   - Investigation of whether (and how) these environments are captured by integrity verdicts.

4. **Replay and Concurrency of Requests**  
   - Re-use of previously captured integrity tokens.  
   - Concurrent requests and timing windows.  
   - Evaluation of the impact of nonce strategies (reused, unique, hash-based).


---

## Getting Started

### Prerequisites

#### Android side

- Android Studio (with an emulator that supports Google Play services)  
- Android SDK / build-tools aligned with this project (see `android-app/gradle` configuration)  
- JDK 17 (or compatible with the project’s Gradle configuration)

#### Backend side

- Python 3.x  
- Virtual environment (recommended)  
- Access to a **Google Cloud** project configured for the Google Play Integrity API  
- Service account credentials (JSON) to validate tokens server-side

---

## Backend Setup

```bash
cd backend

# Create and activate virtual environment
python -m venv .venv
# Linux/macOS
source .venv/bin/activate
# Windows (PowerShell)
# .venv\Scripts\Activate.ps1

# Install dependencies
pip install -r requirements.txt

# Set the environment variable pointing to your service account JSON
# Linux/macOS
export GOOGLE_APPLICATION_CREDENTIALS="path/to/service-account.json"
# Windows (PowerShell)
# $env:GOOGLE_APPLICATION_CREDENTIALS="path\to\service-account.json"

# Run the backend
python app.py
```

By default, the backend exposes an HTTP endpoint that receives integrity tokens from the Android app, validates them and stores verdicts/logs under `data/`.

---

## Android App Setup

1. Open the `android-app/` folder with **Android Studio**.  
2. Configure the **package name** and **SHA-256 certificate fingerprint** in the Google Play Console / Play Integrity configuration to match this app.  
3. Update the app configuration (e.g., backend URL, API endpoints) in the appropriate constants/config file.  
4. Build and run the app on:
   - a real device (non-rooted, Google Play-enabled), and  
   - a rooted or emulated device (when applicable to the scenario).

The app exposes UI elements to trigger:

- **Classic mode** requests (`IntegrityManager`)  
- **Standard mode** requests (`StandardIntegrityManager`)  
- **Combined/custom flows**, e.g., using nonce hashing to bind actions and prevent replay

---

## Running the Experiments

A typical experimental run follows these steps:

1. **Select the target scenario**  
   - Device type/state (real/emulator, rooted/non-rooted, unlocked bootloader)  
   - Android version  
   - Instrumentation / root tool (Frida, Magisk, etc.)  
   - APK variant (original / repackaged / resigned)  
   - Nonce strategy (reused / unique / hash-based)

2. **Prepare the environment**  
   - Start the backend (`backendpython/app.py`)  
   - Confirm that logs are being persisted under `data/` (e.g., JSON or CSV)  
   - Set up proxy, instrumentation, or replay tooling if required by the scenario

3. **Trigger Play Integrity requests from the app**  
   - Press the corresponding button (Classic / Standard / Combined)  
   - Optionally repeat requests with different timing, network conditions, or nonces to explore edge cases

4. **Collect and label the results**  
   - Integrity verdicts (device, app, account, licensing, etc.)  
   - Error codes, timeouts and any abnormal responses  
   - Scenario metadata: device model, Android version, root/emulator state, tools used, nonce strategy, timestamp

5. **Export and analyze**  
   - Use the scripts under `experiments/` (or your own notebooks) to parse and analyze logs  
   - Generate tables and plots to compare Classic vs Standard flows, false negatives/positives, replay behavior, etc.

---

## Reproducibility and Limitations

The artefacts in this repository are provided to support **reproducibility** and **independent validation** of the results. Some aspects are inherently environment-dependent, including:

- Specific device models, OEM customizations and Android versions  
- Availability and behavior of rooting, instrumentation and hiding tools over time  
- Changes in the Google Play Integrity API behaviour, rate limits and policies  

These dependencies, versions and specific configurations should be documented in:

- `docs/` (high-level documentation and diagrams)  
- Comments in code (`app/`, `backendpython/`, `experiments/`)  
- Additional notes in `data/` or `experiments/` (e.g., metadata files)

---

## Security and Ethical Considerations

This project focuses on **evaluating a security mechanism** in controlled environments. When using or extending these artefacts, keep in mind:

- Use rooted devices, instrumentation tools and network interception **only** on devices and systems that you own or are explicitly authorized to test.  
- Do not use these techniques to violate terms of service, break into systems, or bypass protections in production applications outside a legitimate research or testing scope.  
- When sharing datasets or logs, ensure that no personal or sensitive information is exposed.

The code and scripts are intended for **research and educational purposes**.

---

## Citation

If you use this repository, its artefacts, or build upon this work in academic publications or technical reports, please cite:

> *Assessing the Security Coverage of the Google Play Integrity API on Android*  
> Francis Luis Santos Vargas et al., 2025.

A placeholder BibTeX entry:

```bibtex
@misc{playintegrity2025,
  title        = {Assessing the Security Coverage of the Google Play Integrity API on Android},
  author       = {Vargas, Francis Luis Santos and others},
  year         = {2025},
  note         = {Manuscript in preparation},
}
```

Replace this with the final BibTeX entry once the paper is accepted and published (update venue, DOI, pages, etc.).

---

## License

This repository is released under the **MIT License**.  
See the [`LICENSE`](LICENSE) file for details.

---

## Contact

For questions, suggestions or collaboration, feel free to reach out:

- **Author:** Francis Luis Santos Vargas  
- **E-mail:** francisvargas.aluno@unipampa.edu.br 
- **GitHub:** https://github.com/francis-vargas
