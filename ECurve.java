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
     * Implement a method BigInteger[] mul(BigInteger[] a1,
        BigInteger[] a2, BigInteger d, BigInteger p) that computes point
        BigInteger[] a3 as specified by the above formula from the given
        points 𝑎1 and 𝑎2 (all points represented as BigInteger arrays
        containing exactly 2 elements each, namely, its x-coordinate and
        its y-coordinate) and given values of 𝑑 and 𝑝.
     */
    static BigInteger[] mul(BigInteger[] a1,
        BigInteger[] a2, BigInteger d, BigInteger p) {
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
    static BigInteger[] exp(BigInteger[] a,
        BigInteger m, BigInteger d, BigInteger p){
        BigInteger [] b = new BigInteger [2];
        BigInteger two = new BigInteger("2");
        
        if (m.equals(BigInteger.ONE)) {
            b[0] = a[0];
            b[1] = a[1];
        } else if (myB.mod(two).equals(BigInteger.ONE)) {
            return myB.multiply(exp(myB,myP.subtract(BigInteger.ONE)));
        } else {
            BigInteger temp = exp(myB, myP.divide(two));
            return temp.multiply(temp);
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
    static BigInteger[] rho(BigInteger[] a,
        BigInteger[] b, BigInteger d, BigInteger p, BigInteger n) {
        
        return null;
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