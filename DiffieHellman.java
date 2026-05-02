// Name: Samy Laalioui
// Diffie-Hellman Key Exchange Simulation in Java

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class DiffieHellman {
    
    private static final SecureRandom random = new SecureRandom();
    
    
    public static boolean isPrime(BigInteger n, int certainty) {
        return n.isProbablePrime(certainty);
    }
    
    /**
     * Find the next prime greater than or equal to start
     */
    public static BigInteger nextPrime(BigInteger start) {
        if (start.compareTo(BigInteger.valueOf(2)) < 0) {
            start = BigInteger.valueOf(2);
        }
        while (!isPrime(start, 100)) {
            start = start.add(BigInteger.ONE);
        }
        return start;
    }
    
    /**
     * Find a primitive root modulo prime p
     * A primitive root g satisfies that for every prime factor q of p-1,
     * g^((p-1)/q) mod p != 1
     */
    public static BigInteger primitiveRoot(BigInteger p) {
        if (!isPrime(p, 100)) {
            throw new IllegalArgumentException("p must be prime");
        }
        
        BigInteger phi = p.subtract(BigInteger.ONE);
        List<BigInteger> primeFactors = getPrimeFactors(phi);
        
        // Test candidates from 2 to p-1
        for (BigInteger g = BigInteger.valueOf(2); g.compareTo(p) < 0; g = g.add(BigInteger.ONE)) {
            boolean isRoot = true;
            for (BigInteger q : primeFactors) {
                BigInteger exp = phi.divide(q);
                if (g.modPow(exp, p).equals(BigInteger.ONE)) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                return g;
            }
        }
        return null; // Should not happen for prime p
    }
    
    /**
     * Get distinct prime factors of n
     */
    private static List<BigInteger> getPrimeFactors(BigInteger n) {
        List<BigInteger> factors = new ArrayList<>();
        BigInteger temp = n;
        
        // Factor out 2
        if (temp.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            factors.add(BigInteger.TWO);
            while (temp.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                temp = temp.divide(BigInteger.TWO);
            }
        }
        
        // Factor out odd numbers
        BigInteger i = BigInteger.valueOf(3);
        while (i.multiply(i).compareTo(temp) <= 0) {
            if (temp.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                while (temp.mod(i).equals(BigInteger.ZERO)) {
                    temp = temp.divide(i);
                }
            }
            i = i.add(BigInteger.TWO);
        }
        
        if (temp.compareTo(BigInteger.ONE) > 0) {
            factors.add(temp);
        }
        
        return factors;
    }
    
    /**
     * Generate a random secret integer in range [2, p-2]
     */
    public static BigInteger generateSecret(BigInteger p) {
        BigInteger min = BigInteger.valueOf(2);
        BigInteger max = p.subtract(BigInteger.valueOf(2));
        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        BigInteger result;
        do {
            result = new BigInteger(range.bitLength(), random);
        } while (result.compareTo(range) >= 0);
        return result.add(min);
    }
    
    /**
     * Simulate Diffie-Hellman key exchange
     */
    public static DiffieHellmanResult diffieHellman(BigInteger p, BigInteger g, 
                                                     BigInteger a, BigInteger b) {
        BigInteger A = g.modPow(a, p);
        BigInteger B = g.modPow(b, p);
        
        BigInteger aliceKey = B.modPow(a, p);
        BigInteger bobKey = A.modPow(b, p);
        
        return new DiffieHellmanResult(p, g, a, b, A, B, aliceKey, bobKey);
    }
    
    /**
     * Result container class
     */
    static class DiffieHellmanResult {
        BigInteger p, g, a, b, A, B, aliceKey, bobKey;
        
        DiffieHellmanResult(BigInteger p, BigInteger g, BigInteger a, BigInteger b,
                           BigInteger A, BigInteger B, BigInteger aliceKey, BigInteger bobKey) {
            this.p = p;
            this.g = g;
            this.a = a;
            this.b = b;
            this.A = A;
            this.B = B;
            this.aliceKey = aliceKey;
            this.bobKey = bobKey;
        }
        
        void print() {
            System.out.println("p:" + p);
            System.out.println("g:" + g);
            System.out.println("Alice's secret (a): " + a);
            System.out.println("Bob's secret (b): " + b);
            System.out.println("A:" + A);
            System.out.println("B:" + B);
            System.out.println("Alice's key: " + aliceKey);
            System.out.println("Bob's key: " + bobKey);
            System.out.println("Keys match? " + aliceKey.equals(bobKey));
            System.out.println("----------------------------------------");
        }
    }
    
    public static void main(String[] args) {
        // Run 1: Small prime 23
        System.out.println("Run 1:");
        BigInteger p1 = BigInteger.valueOf(23);
        BigInteger g1 = primitiveRoot(p1);
        BigInteger a1 = generateSecret(p1);
        BigInteger b1 = generateSecret(p1);
        DiffieHellmanResult result1 = diffieHellman(p1, g1, a1, b1);
        result1.print();
        
        // Run 2: Medium prime 97
        System.out.println("Run 2:");
        BigInteger p2 = BigInteger.valueOf(97);
        BigInteger g2 = primitiveRoot(p2);
        BigInteger a2 = generateSecret(p2);
        BigInteger b2 = generateSecret(p2);
        DiffieHellmanResult result2 = diffieHellman(p2, g2, a2, b2);
        result2.print();
        
        // Run 3: Larger prime generated from random start
        BigInteger randomStart = BigInteger.valueOf(1000 + random.nextInt(4000));
        BigInteger p3 = nextPrime(randomStart);
        System.out.println("Run 3 (prime from start " + randomStart + "):");
        BigInteger g3 = primitiveRoot(p3);
        BigInteger a3 = generateSecret(p3);
        BigInteger b3 = generateSecret(p3);
        DiffieHellmanResult result3 = diffieHellman(p3, g3, a3, b3);
        result3.print();
    }
}


/* ==============================================
   OUTPUT OF 3 RUNS
   ==============================================

Run 1:
p:23
g:5
Alice's secret (a): 10
Bob's secret (b): 7
A:9
B:17
Alice's key: 4
Bob's key: 4
Keys match? true
----------------------------------------
Run 2:
p:97
g:5
Alice's secret (a): 68
Bob's secret (b): 53
A:4
B:76
Alice's key: 54
Bob's key: 54
Keys match? true
----------------------------------------
Run 3 (prime from start 1585):
p:1597
g:11
Alice's secret (a): 168
Bob's secret (b): 446
A:517
B:1145
Alice's key: 27
Bob's key: 27
Keys match? true
----------------------------------------

   ==============================================
   ANSWERS TO QUESTIONS
   ==============================================

1. How did you determine the secret integers for both Alice and Bob?
   Were there limitations or bounds on how you chose the secret integers?

   Answer: The secret integers a and b were generated randomly using 
   SecureRandom in Java. The bounds are 2 ≤ a, b ≤ p-2. The lower bound 
   of 2 avoids trivial secrets (0 or 1 would make A or B equal to 1 or g, 
   which weakens security). The upper bound p-2 ensures that a and b are 
   within the multiplicative group modulo p (the group of non-zero residues), 
   since values mod p are 0..p-1, but 0 yields A=0, and p-1 yields 
   A = g^(p-1) ≡ 1 mod p by Fermat's Little Theorem, making the key 
   predictable. So the valid range for secrets is [2, p-2].

2. What values could an eavesdropper determine by listening on the
   insecure channel? Is this enough to recreate the entire secret key?

   Answer: An eavesdropper can see p, g, A, and B. These are all sent 
   in plaintext over the insecure channel. However, without knowing a 
   or b, the eavesdropper cannot compute the shared secret key 
   = g^(a*b) mod p because that would require solving the discrete 
   logarithm problem: given g and A = g^a mod p, find a, or given g 
   and B = g^b mod p, find b. For large primes (e.g., 2048 bits), the 
   discrete logarithm problem is computationally infeasible. Therefore, 
   the eavesdropper cannot recreate the secret key from the intercepted 
   values alone.

*/