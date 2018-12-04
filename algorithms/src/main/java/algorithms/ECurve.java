/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.security.spec.ECPoint;
import java.util.Arrays;

/**
 *
 * @author fly_b
 */
public class ECurve {
    
    
    public static class ECTuple {
        /**
         * Class to hold k values in rho function
         */
        public BigInteger A = BigInteger.ZERO;
        public BigInteger B = BigInteger.ONE;
        public BigInteger[] Z = new BigInteger[2];

        public ECTuple(BigInteger alpha, BigInteger beta, BigInteger[] zeta) {
            this.A = alpha;
            this.B = beta;
            this.Z = zeta;
        }
    }

    /**
     * Returns a prime BigInteger calculated as q^r + s
     */
    public static BigInteger calcPrime(Double q, Double r, Double s) {
        Double tempPrime;
        BigInteger primeNum;
        System.out.print("primeInt = " +q+ "^" +r+ " - " +s+ "\n");
        tempPrime = Math.pow(q, r) - s;
        primeNum = BigDecimal.valueOf(tempPrime).toBigInteger();
        return primeNum;
    }
 
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
        t1 = a1[0].multiply(a2[1]).mod(p); // (x1*y2)%p
        t2 = a1[1].multiply(a2[0]).mod(p); // (y1*x2)%p
        t3 = t1.add(t2).mod(p); // (t1+t2)%p

        t4 = t1.multiply(t2).mod(p); // (t1*t2)%p
        t4 = t4.multiply(d).mod(p); // (t4*d)%p
        t4 = BigInteger.ONE.add(t4).mod(p);// (1+t4)%P

        a3[0] = Dev(t3, t4, p); // (t3/t4)%P

        // calculate y for a3
        t1 = a1[1].multiply(a2[1]).mod(p); // (y1*y2)%p
        t2 = a1[0].multiply(a2[0]).mod(p); // (x1*x2)%p
        t3 = t1.subtract(t2).mod(p); // (t1-t2)%p

        t4 = t1.multiply(t2).mod(p); // (t1*t2)%p
        t4 = t4.multiply(d).mod(p); // (t4*d)%p
        t4 = BigInteger.ONE.subtract(t4).mod(p);// (1+t4)%P

        a3[1] = Dev(t3, t4, p); // (t3/t4)%p
        return a3;
    }
    
    /**
     * computes the curve point 𝑏 = 𝑎^𝑚 given a point 𝑎 represented as above, a
        BigInteger exponent 𝑚, and values of 𝑑 and 𝑝.
     */
    static BigInteger[] exp(BigInteger[] a, BigInteger m, BigInteger d, 
        BigInteger p){

        BigInteger [] b = new BigInteger [2];
        b[0] = BigInteger.ZERO;
        b[1] = BigInteger.ONE;

        for (int i = m.bitLength() - 1; i >= 0; i--) {
            b = mul(b, b, d, p);
            if (m.testBit(i)) {
                b = mul(a, b, d, p);
            }
        }
        return b;
    }
    

    /**
     *  Given points 𝑎 and 𝑏 such that 𝑏 = 𝑎^𝑚 and the values of 𝑑, 𝑝, and
        𝑛, recovers the exponent (“discrete logarithm”) 𝑚 modulo 𝑛 and
        counts the number 𝑘 of steps necessary for that computation. The
        result must be contained in a BigInteger array containing exactly 2
        elements, 𝑚 and 𝑘, in this order.
     */
    static BigInteger[] rho(BigInteger[] a, BigInteger[] b, BigInteger d, 
                                                BigInteger p, BigInteger n) {

        BigInteger m;
        int k;
        BigInteger[] result = new BigInteger[2]; // [m,k]
        BigInteger[] zval = new BigInteger[2];
        zval[0] = BigInteger.ZERO;
        zval[1] = BigInteger.ONE;
        ECTuple kvals = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, zval);
        ECTuple kvals2 = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, zval);

        // Iiniialize values for K and 2K
        k = 1;
        kvals = rho_update(kvals, a, b, d, p);
        kvals2 = rho_update(rho_update(kvals2, a, b, d, p), a, b, d, p);
        
        // repeat until z_k == z_2k 
        while (!Arrays.equals(kvals.Z,kvals2.Z)) {               
            k += 1;
            // update k values
            kvals = rho_update(kvals, a, b, d, p);
            // update 2k values, running rho_update twice
            kvals2 = rho_update(rho_update(kvals2, a, b, d, p), a, b, d, p);
            // handle exceptions where " Alpha_z - Alpha_2z = 0(mod n) "
            if ((kvals.A.subtract(kvals2.A).mod(n)).equals(BigInteger.ZERO)) {
                throw new RuntimeException("Error at k = " + k + " (alpha_k == alpha_k2)");
            }
        }
        // Determine m from sextuple values, then return results
        m = Dev(kvals2.B.subtract(kvals.B),kvals.A.subtract(kvals2.A), n);
        result[0] = m;
        result[1] = BigInteger.valueOf(k);
        return result;
    }

    /**
     * Returns a new array with rho-updated values based on the the input array
     * For use with rho function.
     */
    private static ECTuple rho_update(ECTuple kvalTuple, BigInteger[] a, 
                                        BigInteger[] b, BigInteger d, BigInteger p) {

        BigInteger two = BigInteger.TWO;
        BigInteger three = new BigInteger("3");
        BigInteger xCoord = kvalTuple.Z[0];
        ECTuple newvals = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, kvalTuple.Z);

        switch (xCoord.mod(three).intValue()) {
            case 0:
                newvals.Z = mul(b, kvalTuple.Z, d, p);
                newvals.A = (kvalTuple.A).add(BigInteger.ONE);
                newvals.B = kvalTuple.B;
                break;
            case 1:
                newvals.Z = mul(kvalTuple.Z, kvalTuple.Z, d, p);
                newvals.A = kvalTuple.A.multiply(two);
                newvals.B = kvalTuple.B.multiply(two);
                break;
            case 2:
                newvals.Z = mul(a, kvalTuple.Z, d, p);
                newvals.A = kvalTuple.A;
                newvals.B = kvalTuple.B.add(BigInteger.ONE);
                break;
        }
        return newvals;
    }
    
    /**
     * Function to divide a BigInteger with mod p
     * 
     * @param a
     * @param b
     * @param p
     * @return 
     */
    static BigInteger Dev(BigInteger a, BigInteger b, BigInteger p) {
        BigInteger t = BigInteger.ONE;
        try {
            t = a.multiply(b.modInverse(p)).mod(p);
        } catch (Exception e) {
            throw new RuntimeException("Erroneous value for b: " + b);
        }
        return t;
    }
 
}
