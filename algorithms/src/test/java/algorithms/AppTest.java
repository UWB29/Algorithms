package algorithms;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.lang.Math;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void testApp() {
        BigInteger[] a = new BigInteger[2];
        a[0] = new BigInteger("12");
        a[1] = new BigInteger("61833");
        BigInteger d = new BigInteger("154");
        BigInteger n = new BigInteger("16339");
        int N = 1000;
        Double[] p = new Double[3];
        p[0] = 2.0;
        p[1] = 16.0; 
        p[2] = 17.0;
        BigInteger primeInt;
        long result;

        // calculate the average k given current request
        primeInt = ECurve.calcPrime(p[0], p[1], p[2]);
        result = App.driver(N, a, d, primeInt, n);
        System.out.print(result);
    }
}
