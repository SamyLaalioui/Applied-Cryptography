# Applied Cryptography Implementations 🔐
 
A collection of cryptographic algorithm implementations built during Applied Cryptography coursework at Northwest Missouri State University. Each project demonstrates both the theoretical foundation and practical application of core cryptographic primitives used in real-world security systems.
 
---
 
## 📁 Projects in This Repository
 
| Project | Algorithm | Language | Concepts |
|---------|-----------|----------|----------|
| [Diffie-Hellman Key Exchange](#diffie-hellman-key-exchange) | DH | Java | Key exchange, discrete logarithm, primitive roots |
| [AES Encryption App](#aes-encryption-app) | AES-256-CBC | Java | Symmetric encryption, PBKDF2, IV, key derivation |
| [DES Encryption + Timing Analysis](#des-encryption--timing-analysis) | DES | Java | Symmetric cipher, performance benchmarking |
| [Euclidean GCD (Embedded)](#euclidean-gcd--embedded-hardware) | Euclid's Algorithm | C | Number theory, Raspberry Pi Pico, GPIO |
 
---
 
## 🔑 Diffie-Hellman Key Exchange
 
**File:** `DiffieHellman.java`
 
A full simulation of the Diffie-Hellman key exchange protocol, the foundation of modern secure communications (TLS, SSH, Signal Protocol).
 
### What It Does
- Finds primitive roots modulo a prime `p` using Euler's totient function and prime factorization
- Generates cryptographically random secrets using `SecureRandom` within safe bounds `[2, p-2]`
- Simulates Alice and Bob independently computing the same shared secret without ever transmitting it
- Verifies key agreement across 3 runs with primes of increasing size (23, 97, ~1597)
### Key Concepts Demonstrated
- **Discrete Logarithm Problem** — why an eavesdropper can see `p`, `g`, `A`, `B` but cannot recover the shared key
- **Primitive Roots** — ensuring the generator `g` covers the full multiplicative group mod `p`
- **Fermat's Little Theorem** — explains why secrets must stay in `[2, p-2]`
### Sample Output
```
Run 1:
p: 23 | g: 5
Alice's secret (a): 10 | Bob's secret (b): 7
A: 9 | B: 17
Alice's key: 4 | Bob's key: 4
Keys match? true
 
Run 3 (prime ~1597):
Alice's key: 27 | Bob's key: 27
Keys match? true
```
 
### Run It
```bash
javac DiffieHellman.java
java DiffieHellman
```
 
---
 
## 🛡️ AES Encryption App
 
**File:** `aestestapp.java`
 
Implements AES-256-CBC encryption and decryption using Java's `javax.crypto` library with PBKDF2 key derivation.
 
### What It Does
- Derives a 256-bit AES key from a passphrase using **PBKDF2WithHmacSHA256** (65,536 iterations)
- Encrypts and decrypts arbitrary plaintext using **AES/CBC/PKCS5Padding**
- Uses a fixed IV for demonstration; output is Base64-encoded ciphertext
### Key Concepts Demonstrated
- **AES-CBC mode** — how chaining blocks prevents identical plaintext blocks from producing identical ciphertext
- **PBKDF2 key derivation** — why raw passwords shouldn't be used directly as encryption keys
- **Padding** — PKCS5 padding and why block ciphers require it
### Sample Output
```
Original:  The super secret fight jet plans!
Encrypted: [Base64 ciphertext]
Decrypted: The super secret fight jet plans!
```
 
### Run It
```bash
javac aestestapp.java
java AESTestApp
```
 
---
 
## ⏱️ DES Encryption + Timing Analysis
 
**Files:** `DESTestApp.java` / `DES_TestAppCode.java`
 
Implements the DES (Data Encryption Standard) cipher and benchmarks its performance over 100 encrypt/decrypt cycles using nanosecond-precision timing.
 
### What It Does
- Generates a DES `SecretKey` using `KeyGenerator`
- Encrypts and decrypts a string 100 times, measuring each cycle with `System.nanoTime()`
- Calculates total time, average time per operation in nanoseconds and milliseconds
- Outputs ciphertext (Base64) and verified decrypted result
### Key Concepts Demonstrated
- **DES cipher mechanics** — 56-bit key, 64-bit block size, Feistel structure
- **Why DES is deprecated** — timing analysis reveals how fast modern hardware breaks it
- **Performance benchmarking** — practical measurement of cryptographic overhead
### Sample Output
```
Key: [Base64]
Original text: Samy Laalioui
Ciphertext: [Base64]
Decrypted text: Samy Laalioui
 
--- Timing Results ---
Total time for 100 iterations: [X] nanoseconds
Average time per encryption/decryption: [X] nanoseconds
Average time in milliseconds: [X] ms
```
 
### Run It
```bash
javac DESTestApp.java
java DESTestApp
```
 
---
 
## ⚙️ Euclidean GCD — Embedded Hardware
 
**File:** `EuclidGCDProject.c`
 
Implementation of Euclid's subtraction-based GCD algorithm running on a **Raspberry Pi Pico** microcontroller, with results displayed on physical GPIO-connected LEDs in binary.
 
### What It Does
- Computes GCD of two integers using Euclid's classic subtraction algorithm
- Outputs result to serial console via `stdio`
- **Bonus:** Displays GCD result in binary using 4 LEDs connected to GPIO pins 26–29
### Key Concepts Demonstrated
- **Euclid's Algorithm** — the mathematical foundation of RSA key generation and modular inverse computation
- **Embedded C / Raspberry Pi Pico SDK** — real hardware programming with `pico/stdlib.h`
- **Binary representation on GPIO** — bit manipulation to drive physical LEDs
### Hardware Setup
```
GPIO 26 → LED 0 (LSB)
GPIO 27 → LED 1
GPIO 28 → LED 2
GPIO 29 → LED 3 (MSB)
```
 
### Why This Matters for Cryptography
Euclid's algorithm is the backbone of RSA — it's used to compute modular inverses (`d = e⁻¹ mod φ(n)`) during key generation. Understanding it at the hardware level deepens the intuition for why RSA works.
 
### Build & Flash (Pico SDK)
```bash
mkdir build && cd build
cmake ..
make
# Flash .uf2 to Pico
```
 
---
 
## 🧠 Concepts Covered Across Projects
 
| Concept | Project |
|---------|---------|
| Symmetric encryption | AES, DES |
| Asymmetric / key exchange | Diffie-Hellman |
| Key derivation (PBKDF2) | AES |
| Number theory (GCD, primitive roots) | Euclid, Diffie-Hellman |
| Performance analysis | DES Timing |
| Embedded systems + crypto math | Euclid GCD on Pico |
| Why legacy ciphers fail | DES (56-bit key weakness) |
 
---
 
## 🎓 Course Context
 
These projects were completed as part of **Applied Cryptography** at Northwest Missouri State University. Each implementation was built from the algorithm up — not using high-level crypto libraries as black boxes, but understanding the math behind each primitive.
 
---
 
*Part of my [Cyber Portfolio](https://github.com/samylaalioui) — connect on [LinkedIn](https://linkedin.com/in/samylaalioui)*
