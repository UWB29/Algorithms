/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.ECPoint;

/**
 *
 * @author fly_b
 */
public class ECurve {
    
//    private final static BigInteger ZERO = new BigInteger("0");
//    private final static BigInteger ONE  = new BigInteger("1");
//    private final static BigInteger TWO  = new BigInteger("2");
//    private final static SecureRandom random = new SecureRandom();
//
//    public static BigInteger rho(BigInteger N) {
//        BigInteger divisor;
//        BigInteger c  = new BigInteger(N.bitLength(), random);
//        BigInteger x  = new BigInteger(N.bitLength(), random);
//        BigInteger xx = x;
//
//        // check divisibility by 2
//        if (N.mod(TWO).compareTo(ZERO) == 0) return TWO;
//
//        do {
//            x  =  x.multiply(x).mod(N).add(c).mod(N);
//            xx = xx.multiply(xx).mod(N).add(c).mod(N);
//            xx = xx.multiply(xx).mod(N).add(c).mod(N);
//            divisor = x.subtract(xx).gcd(N);
//        } while((divisor.compareTo(ONE)) == 0);
//
//        return divisor;
//    }
//
//    public static void factor(BigInteger N) {
//        if (N.compareTo(ONE) == 0) return;
//        if (N.isProbablePrime(20)) {System.out.println(N); return; }
//        BigInteger divisor = rho(N);
//        factor(divisor);
//        factor(N.divide(divisor));
//    }

    /**
     * ECPoint
     * 
     * @param a
     * @param m
     * @return 
     */
    /**
    ECPoint Exp(ECPoint a, BigInteger m) {
        ECPoint b = u; // NB: u is the point (0, 1)
        for (int i = m.bitLength() – 1; i >= 0; i--) {
            b = b.multiply(b); // i.e. 𝑏 = 𝑏^2
            if (m.testBit(i)) {
                b = b.multiply(a);
            }
        }
        return b;
    }
    */
    
 
    /**
     * Computes the product of two points on the elliptical curve 
     * Implement a method BigInteger[] mul(BigInteger[] a1,
        BigInteger[] a2, BigInteger d, BigInteger p) that computes point
        BigInteger[] a3 as specified by the above formula from the given
        points 𝑎1 and 𝑎2 (all points represented as BigInteger arrays
        containing exactly 2 elements each, namely, its x-coordinate and
        its y-coordinate) and given values of 𝑑 and 𝑝.
     */
    static BigInteger[] mul(BigInteger[] a1, BigInteger[] a2, 
        BigInteger d, BigInteger p) {

        BigInteger[] a3 = new BigInteger[2];
        BigInteger t1, t2, t3, t4;
        
        // calculate x for a3
        t1 = a1[0].multiply(a2[1]).mod(p); //(x1*y2)%p
        t2 = a1[1].multiply(a2[0]).mod(p); //(y1*x2)%p
        t3 = t1.add(t2).mod(p);            //(t1+t2)%p
        
        t4 = t1.multiply(t2).mod(p);       //(t1*t2)%p
        t4 = t4.multiply(d).mod(p);        //(t4*d)%p
        t4 = BigInteger.ONE.add(t4).mod(p);//(1+t4)%P
        
        a3[0] = Dev(t3,t4,p);              //(t3/t4)%P
        
        // calculate y for a3
        t1 = a1[1].multiply(a2[1]).mod(p); //(y1*y2)%p
        t2 = a1[0].multiply(a2[0]).mod(p); //(x1*x2)%p
        t3 = t1.subtract(t2).mod(p);       //(t1-t2)%p
        
        t4 = t1.multiply(t2).mod(p);       //(t1*t2)%p
        t4 = t4.multiply(d).mod(p);        //(t4*d)%p
        t4 = BigInteger.ONE.subtract(t4).mod(p);//(1+t4)%P
        
        a3[1] = Dev(t3,t4,p);              //(t3/t4)%p
        return a3;
    }
    
    /**
     * Implement a method BigInteger[] exp(BigInteger[] a,
        BigInteger m, BigInteger d, BigInteger p) that computes the
        curve point 𝑏 = 𝑎^𝑚 given a point 𝑎 represented as above, a
        BigInteger exponent 𝑚, and values of 𝑑 and 𝑝.
     */
    static BigInteger[] exp(BigInteger[] a, BigInteger m, BigInteger d, 
        BigInteger p){

        BigInteger [] b = new BigInteger [2];
        BigInteger two = new BigInteger("2");
        
        if (m.equals(BigInteger.ONE)) {
            b[0] = a[0];
            b[1] = a[1];
        } else if (m.mod(two).equals(BigInteger.ONE)) { // if m is odd
            b = exp(a,m.subtract(BigInteger.ONE),d,p); // recursive call
            b[0] = b[0].multiply(a[0]).mod(p);
            b[1] = b[1].multiply(a[1]).mod(p);
        } else { // m is even
            b = exp(a, m.divide(two),d,p);  // recursive call
            b[0] = b[0].multiply(b[0]).mod(p);
            b[1] = b[1].multiply(b[0]).mod(p);
        }
        
        return b;
    }
    

    /**
     * Implement a method BigInteger[] rho(BigInteger[] a,
        BigInteger[] b, BigInteger d, BigInteger p, BigInteger n) that,
        given points 𝑎 and 𝑏 such that 𝑏 = 𝑎^𝑚 and the values of 𝑑, 𝑝, and
        𝑛, recovers the exponent (“discrete logarithm”) 𝑚 modulo 𝑛 and
        counts the number 𝑘 of steps necessary for that computation. The
        result must be contained in a BigInteger array containing exactly 2
        elements, 𝑚 and 𝑘, in this order.
     */
    static BigInteger[] rho(BigInteger[] a, BigInteger[] b, BigInteger d, 
                                                BigInteger p, BigInteger n) {

        BigInteger k = BigInteger.ZERO;
        BigInteger m;
        BigInteger[] zval = new BigInteger[2];
        zval[0] = BigInteger.ZERO;
        zval[1] = BigInteger.ONE;
        BigInteger[] result = new BigInteger[2]; // [m,k]
        Tuple kvals = new Tuple(BigInteger.ZERO, BigInteger.ZERO, zval);
        Tuple kvals2 = new Tuple(BigInteger.ZERO, BigInteger.ZERO, zval);

        // set values for k = 1
        k.add(BigInteger.ONE);
        kvals = rho_update(kvals, a, b, d, p);
        kvals2 = rho_update(kvals, a, b, d, p);

        while (kvals.Z != kvals2.Z) { // until z_k == z_2k
            k.add(BigInteger.ONE);

            // update k values
            kvals = rho_update(kvals, a, b, d, p);

            // update 2k values
            kvals2 = rho_update(kvals2, a, b, d, p);
            kvals2 = rho_update(kvals2, a, b, d, p); // run it twice

            // handle exceptions
            if (kvals.A.subtract(kvals2.A) == BigInteger.ZERO.mod(n)) {
                throw new IllegalArgumentException("Error in initial variables");
            }
        }

        // Determine m from sextuple values, then return results
        m = (kvals2.B.subtract(kvals.B)).divide(kvals2.A.subtract(kvals.A)).mod(n);
        result[0] = m;
        result[1] = k;
        return result;
    }

    /**
     * Class to hold k values in rho function
     */
    public class Tuple {
        public BigInteger A;
        public BigInteger B;
        public BigInteger[] Z;

        public Tuple(BigInteger alpha, BigInteger beta, BigInteger[] zeta) {
            this.A = alpha;
            this.B = beta;
            this.Z = zeta;
        }
    }

    /**
     * Returns a new array with rho-updated values based on the the input array
     * For use with rho function.
     */
    private static BigInteger[] rho_update(Tuple theseKvals, BigInteger[] a, 
                                BigInteger[] b, BigInteger d, BigInteger p) {

        Tuple newvals;
        BigInteger two = new BigInteger("2");
        BigInteger three = new BigInteger("3");
        BigInteger xCoord = theseKvals.Z[0];

        switch (xCoord.mod(three).intValue()) {
            case 0:
                newvals.A = (theseKvals.A).add(BigInteger.ONE).mod(p);
                newvals.B = theseKvals.B;
                newvals.Z = BigInteger.mul(theseKvals.Z, b, d, p);
                break;
            case 1:
                newvals.A = theseKvals.A.multiply(two).mod(p);
                newvals.B = theseKvals.B.multiply(two).mod(p);
                newvals.Z = BigInteger.mul(theseKvals.Z, theseKvals.Z, d, p);
                break;
            case 2:
                newvals.A = theseKvals.A;
                newvals.B = theseKvals.B.add(BigInteger.ONE).mod(p);
                newvals.Z = BigInteger.mul(theseKvals.Z, a, d, p);
                break;
            return newvals; 
        }
    }

    /**
     * Implement a method long check(BigInteger[] a, BigInteger d,
        BigInteger p, BigInteger n) that, given a point 𝑎 and the values
        𝑑, 𝑝, and 𝑛, generates a random BigInteger exponent 𝑚 modulo 𝑛,
        computes 𝑏 = 𝑎^𝑚 using method exp(), recovers the discrete
        logarithm 𝑚′ from 𝑎 and 𝑏 using method rho(), checks whether 𝑚 =
        𝑚′ (and throws a RuntimeException if they don’t match) and returns
        the number of steps 𝑘 that method rho needed to compute 𝑚. Also
        implement a driver program that calls method check(...) a given
        number 𝑁 of times, computes the average number 〈𝑘〉 = of 
        steps needed to compute 𝑁 random discrete logarithms, and prints
        the result.
     */
    static long check(BigInteger[] a, BigInteger d, BigInteger p, BigInteger n) {
        return 0;
    }
    
    /**
     * Used to divide to BigInteger mod p
     * 
     * @param a
     * @param b
     * @param p
     * @return 
     */
    static BigInteger Dev(BigInteger a, BigInteger b, BigInteger p) {
        return a.multiply(b.modInverse(p)).mod(p);
    }
 
}

public final class App {
    private App() {
    }

    /**
     * Runs 
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        /*
        private string txtOutput = "";
        private int p =  Math.pow(2, 16) - 17;  // prime number
        private int d = 154;    // coefficient of the curve
        private int n = 16339;   // given number of points on the curve
        private List<Integer> a = new ArrayList<>(12, 61833);  // two points on the curve
        private int N = 1000;
        
        private List<Integer>  u = new ArrayList<>(0, 1);    // the unit (neutral) element of point multiplication

        BigInteger[] t = ECurve.mul(a, u, d, p[3]);
        txtOutput.append("\r\n");
        txtOutput.append("a1 * u = " + t[0] + ", " + t[1] + "\r\n");
        System.out.println(txtOutput);
        */
    }
}
