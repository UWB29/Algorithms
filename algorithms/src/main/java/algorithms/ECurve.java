/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.security.spec.ECPoint;


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
     * computes the curve point 𝑏 = 𝑎^𝑚 given a point 𝑎 represented as above, a
        BigInteger exponent 𝑚, and values of 𝑑 and 𝑝.
     */
    static BigInteger[] exp(BigInteger[] a, BigInteger m, BigInteger d, 
        BigInteger p){

        BigInteger [] b = new BigInteger [2];
        BigInteger two =  BigInteger.TWO;
        
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
     *  Given points 𝑎 and 𝑏 such that 𝑏 = 𝑎^𝑚 and the values of 𝑑, 𝑝, and
        𝑛, recovers the exponent (“discrete logarithm”) 𝑚 modulo 𝑛 and
        counts the number 𝑘 of steps necessary for that computation. The
        result must be contained in a BigInteger array containing exactly 2
        elements, 𝑚 and 𝑘, in this order.
     */
    static BigInteger[] rho(BigInteger[] a, BigInteger[] b, BigInteger d, 
                                                BigInteger p, BigInteger n) {

        BigInteger m;
        int k = 0;
        BigInteger[] result = new BigInteger[2]; // [m,k]
        BigInteger[] zval = new BigInteger[2];
        zval[0] = BigInteger.ZERO;
        zval[1] = BigInteger.ONE;
        ECTuple kvals = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, zval);
        ECTuple kvals2 = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, zval);

        // set values for k = 1
        k += 1;
        kvals = rho_update(kvals, a, b, d, p);
        kvals2 = rho_update(kvals, a, b, d, p);

        // repeat until value is found                
        while (kvals.Z != kvals2.Z) { // until z_k == z_2k
            k += 1;
            System.out.println(k);
            // update k values
            kvals = rho_update(kvals, a, b, d, p);
            // update 2k values, running rho_update twice
            kvals2 = rho_update(rho_update(kvals2, a, b, d, p), a, b, d, p);
            // handle exceptions
            if (kvals.A.subtract(kvals2.A) == BigInteger.ZERO.mod(n)) {
                throw new RuntimeException("Error at k = " + k + 
                                " (alpha_k == alpha_k2)");
            }
        }

        // Determine m from sextuple values, then return results
        m = Dev(kvals2.B.subtract(kvals.B), kvals2.A.subtract(kvals.A), p);
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
                newvals.A = (kvalTuple.A).add(BigInteger.ONE).mod(p);
                newvals.B = kvalTuple.B;
                break;
            case 1:
                newvals.Z = mul(kvalTuple.Z, kvalTuple.Z, d, p);
                newvals.A = kvalTuple.A.multiply(two).mod(p);
                newvals.B = kvalTuple.B.multiply(two).mod(p); 
                break;
            case 2:
                newvals.Z = mul(a,kvalTuple.Z, d, p);
                newvals.A = kvalTuple.A;
                newvals.B = kvalTuple.B.add(BigInteger.ONE).mod(p);
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
            System.out.println(b);
            throw new RuntimeException("Error in b");
            //t = a.multiply(BigInteger.ONE).mod(p);
        }
        return t;
    }
 
}
