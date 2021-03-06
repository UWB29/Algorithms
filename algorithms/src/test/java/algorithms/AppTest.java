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
     * Test settings from main instructions
     */
    public long test_main() {
        Double[] p = new Double[3];
        p[0] = 2.0;
        p[1] = 16.0;
        p[2] = 17.0;
        BigInteger d = new BigInteger("154");
        BigInteger n = new BigInteger("16339");
        BigInteger[] a = new BigInteger[2];
        a[0] = new BigInteger("12");
        a[1] = new BigInteger("61833");
        int N = 1000;
        
        return (settingsTest(p, d, n, a, N));
    }
    
    /**
     * Test "a" settings
     */
    public long test_a() {
        Double[] p = new Double[3];
        p[0] = 2.0;
        p[1] = 18.0;
        p[2] = 5.0;
        BigInteger d = new BigInteger("294");
        BigInteger n = new BigInteger("65717");
        BigInteger[] a = new BigInteger[2];
        a[0] = new BigInteger("5");
        a[1] = new BigInteger("261901");
        int N = 1000;
       
        return (settingsTest(p, d, n, a, N));
    }

    /**
     * Test "a" settings
     */
    public long test_b() {
        Double[] p = new Double[3];
        p[0] = 2.0;
        p[1] = 20.0;
        p[2] = 5.0;
        BigInteger d = new BigInteger("47");
        BigInteger n = new BigInteger("262643");
        BigInteger[] a = new BigInteger[2];
        a[0] = new BigInteger("3");
        a[1] = new BigInteger("111745");
        int N = 1000;

        return (settingsTest(p, d, n, a, N));
    }

    /**
     * Test "b" settings
     */
    public long test_c() {
        Double[] p = new Double[3];
        p[0] = 2.0;
        p[1] = 22.0;
        p[2] = 17.0;
        BigInteger d = new BigInteger("314");
        BigInteger n = new BigInteger("1049497");
        BigInteger[] a = new BigInteger[2];
        a[0] = new BigInteger("4");
        a[1] = new BigInteger("85081");
        int N = 1000;

        return(settingsTest(p, d, n, a, N));
    }

    /**
     * Accept settings as arguments
     */
    private long settingsTest(Double[] p, BigInteger d, BigInteger n, 
                                BigInteger[] a, int N) {
        BigInteger primeInt;
        long result;

        // calculate the average k given current request
        primeInt = ECurve.calcPrime(p[0], p[1], p[2]);
        result = App.driver(N, a, d, primeInt, n);
        System.out.print(result);
        return result;
    }

    /**
     * Rigorous Test.
     */
    @Test
    public void testApp() {
        long[] results = new long[4];
        results[0] = test_main();
        results[1] = test_a();
        results[2] = test_b();
        results[3] = test_c();
        System.out.print("\n"+results[0] + ",  " + results[1] + ",  " +
            results[2] +" " + results[2] + "\n");
    }
}
