/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.security.spec.ECPoint;
import java.util.Arrays;


/**
 *
 * @author fly_b
 */
public class ECurve {
    BigInteger two = new BigInteger("2");
    BigInteger three = new BigInteger("3");
    
    public class ECTuple {
        /**
         * Class to hold k values in rho function
         */
        public BigInteger A;
        public BigInteger B;
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
    public BigInteger calcPrime(Double q, Double r, Double s) {
        Double tempPrime;
        BigInteger primeNum;
        tempPrime = Math.pow(q, r) + s;
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
    BigInteger[] mul(BigInteger[] a1, BigInteger[] a2, 
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
    BigInteger[] exp(BigInteger[] a, BigInteger m, BigInteger d, 
        BigInteger p){

         BigInteger [] b = new BigInteger [2];
        b[0] = BigInteger.ZERO;
        b[1] = BigInteger.ONE;
        //BigInteger two =  BigInteger.TWO;
        
        /*
        if (m.equals(BigInteger.ZERO)) {
            b[0] = BigInteger.ZERO;
            b[1] = BigInteger.ONE;
        } else if (m.equals(BigInteger.ONE)) {
            b[0] = a[0];
            b[1] = a[1];
        } else if (m.mod(two).equals(BigInteger.ONE)) { // if m is odd
            b = exp(a, m.subtract(BigInteger.ONE), d, p); // recursive call
            b[0] = b[0].multiply(a[0]).mod(p);
            b[1] = b[1].multiply(a[1]).mod(p);
        } else { // m is even
            b = exp(a, m.divide(two), d, p); // recursive call
            b[0] = b[0].multiply(b[0]).mod(p);
            b[1] = b[1].multiply(b[1]).mod(p);
        }
        */
        for (int i = m.bitLength() -1; i >=0; i --){
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
    BigInteger[] rho(BigInteger[] a, BigInteger[] b, BigInteger d, 
                                                BigInteger p, BigInteger n) {

        BigInteger m;
        BigInteger k = BigInteger.ZERO;
        BigInteger[] result = new BigInteger[2]; // [m,k]
        BigInteger[] zval = new BigInteger[2];
        zval[0] = BigInteger.ZERO;
        zval[1] = BigInteger.ONE;
        ECTuple kvals = new ECTuple(BigInteger.ZERO, BigInteger.ZERO, zval);
        ECTuple kvals2;
        // set values for k = 1
        k = k.add(BigInteger.ONE);
        kvals = rho_update(kvals, a, b, d, p);
        kvals2 = rho_update(kvals, a, b, d, p);
        // repeat until value is found                
        while (!Arrays.equals(kvals.Z, kvals2.Z)) { // until z_k == z_2k
            k = k.add(BigInteger.ONE);
            // update k values
            kvals = rho_update(kvals, a, b, d, p);
            // update 2k values
            kvals2 = rho_update(rho_update(kvals2, a, b, d, p), a, b, d, p);
            System.out.println(k);
            // handle exceptions
            if (kvals.A.subtract(kvals2.A).equals(BigInteger.ZERO)) {
                throw new RuntimeException("Error at k = " + k + 
                                " (alpha_k == alpha_k2)");
            }
        }

        // Determine m from sextuple values, then return results
        //m = (kvals2.B.subtract(kvals.B)).divide(kvals.A.subtract(kvals2.A)).mod(n);
        m = Dev(kvals2.B.subtract(kvals.B), kvals.A.subtract(kvals2.A), n);
        result[0] = m;
        result[1] = k;
        return result;
    }

    /**
     * method that, given a point 𝑎 and the values 𝑑, 𝑝, and 𝑛, does the
     * following: 
     * 1) generates a random BigInteger exponent 𝑚 modulo 𝑛,
     * 2)computes 𝑏 = 𝑎^𝑚 using method exp(), 
     * 3) recovers the discrete logarithm 𝑚′ from 𝑎 and 𝑏 using method rho(), 
     * 4) check bs whether 𝑚 = 𝑚′ (and throws
     * a RuntimeException if they don’t match) 
     * 5) returns the number of steps 𝑘 that method rho needed to compute 𝑚.
     */
    
    long check(BigInteger[] a, BigInteger d, BigInteger p, BigInteger n) {
        // generate random exponent
        SecureRandom random = new SecureRandom();
        //BigInteger m = new BigInteger("17");
        BigInteger m = new BigInteger(n.bitLength(), random);
        m = m.mod(n);
        // compute b
        BigInteger[] b = exp(a, m, d, p);
        System.out.println(Arrays.toString(b));
        
        try {
            // recover discrete log
            BigInteger[] m_k = rho(a, b, d, p, n);
            // validate
            if (m.equals(m_k[0])) {
                return m_k[1].longValue();
            } else {
                throw new RuntimeException("m does not match m'" +
                        m + ", " + m_k[0].mod(p) + ", " 
                        + m_k[0]);
            }
        } catch(RuntimeException e) {
                throw new RuntimeException(e);
        }
        //return 0;
    }
    
    /**
     * Returns a new array with rho-updated values based on the the input array
     * For use with rho function.
     */
    private ECTuple rho_update(ECTuple kvalTuple, BigInteger[] a, 
                                BigInteger[] b, BigInteger d, BigInteger p) {

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
            t = a.multiply(BigInteger.ONE).mod(p);
        }
        return t;
    }
 
}
