package algorithms; 

import java.lang.Math;
import java.lang.Integer;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Hello world!
 */
public final class App {
    public App() {

    }
    
    /**
     * method that, given a point 𝑎 and the values 𝑑, 𝑝, and 𝑛, does the
     * following: 
     * 1) generates a random BigInteger exponent 𝑚 modulo 𝑛,
     * 2)computes 𝑏 = 𝑎^𝑚 using method exp(), 
     * 3) recovers the discrete logarithm 𝑚′ from 𝑎 and 𝑏 using method rho(), 
     * 4) checks whether 𝑚 = 𝑚′ (and throws
     * a RuntimeException if they don’t match) 
     * 5) returns the number of steps 𝑘 that method rho needed to compute 𝑚.
     */
    public static long check(BigInteger[] a, BigInteger d, BigInteger p, BigInteger n) {
        // generate random exponent
        SecureRandom random = new SecureRandom();
        BigInteger m = new BigInteger(n.bitLength(), random);
        m = m.mod(n);
        // compute b
        BigInteger[] b = ECurve.exp(a, m, d, p);
        // recover discrete log
        BigInteger[] m_k = ECurve.rho(a, b, d, p, n);
        // validate
        if (m == m_k[0]) {
            return m_k[1].longValue();
        } else {
            throw new RuntimeException("m does not match m'");
        }
    }

    /**
     * driver program that does the following:
     * 1) calls method check(...) a given number 𝑁 of times,
     * 2) computes the average number 〈𝑘〉 = of steps needed to compute 𝑁 random
     * discrete logarithms
     * 3) prints the result.
     * 
     * @param args The arguments of the program.
     */
    public static long driver(int N, BigInteger[] a, BigInteger d, 
        BigInteger p, BigInteger n) {
        long sum_k = 0;
        long mean_k = 0;
        
        // run N draws of check, adding k for each to a running total
        System.out.println("input values: ");
        System.out.println("a = (" +a[0]+","+a[1]+ "), d = " +d+ ", p = " +p+ ", n=" +n+ "\n");
        for(int draw=1; draw<=N ; draw++)
        {
            sum_k += (check(a, d, p, n));
        }
        // compute number 〈𝑘〉 = of steps needed to compute 𝑁 random discrete
        // logarithms
        mean_k = sum_k/N;
        System.out.println("mean k = " + mean_k);
        return mean_k;
         
    }
}
