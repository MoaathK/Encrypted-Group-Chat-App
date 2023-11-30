package com.project.ChatEncryptedApp.RSA;

import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class RSA {

    private final UserRepository userRepository;

   static RestTemplate restTemplete = new RestTemplate();

    public static int[] keysGenerator() {
        int[] keys = new int[3];

        int p = primeNumberGenerator();
        int q = primeNumberGenerator();
        int n = p * q;

        int phiN = (p - 1) * (q - 1);
        int e = getE(phiN);
        int d = getD(phiN, e);

        List<User> list = restTemplete.getForObject("http://localhost:7033/getAllUsersList", List.class);

        String listToString = list.toString();
        while (!validateKey(n, listToString) || e == d || p == q){
            p = primeNumberGenerator();
            q = primeNumberGenerator();
            n = p * q;

            phiN = (p - 1) * (q - 1);
            e = getE(phiN);
            d = getD(phiN, e);
        }

        keys[0] = n;
        keys[1] = e;
        keys[2] = d;

        return keys;
    }

    private static boolean validateKey(int n, String list) {
       if(list.contains("n="+n)) {
           return false;
       }
       return true;
    }

    private static int getD(int phiN, int e) {
        int d = 0;
        for (int i = 0; i <= phiN; i++) {
            if(i * e % phiN == 1){
                d = i;
                break;
            }
        }
        return d;
    }

    private static int getE(int phiN) {
        int e = 0;
        for (int i = 2; i < phiN; i++) {
            if(gcd(i, phiN) == 1){
                e = i;
                break;
            }
        }
        return e;
    }

    private static int gcd(int i, int phiN) {
        if (phiN == 0) {
            return i;
        }
        return gcd(phiN, i % phiN);
    }

    private static int primeNumberGenerator() {
        Random random = new Random();
        int number = random.nextInt(88) + 13;

        while (!isPrime(number)) {
            number = random.nextInt(88) + 13;
        }
        return number;
    }

    private static boolean isPrime(int number) {
        if (number <= 3 || number % 2 == 0)
            return number == 2 || number == 3;
        int divisor = 3;
        while ((divisor <= Math.sqrt(number)) && (number % divisor != 0))
            divisor += 2;
        return number % divisor != 0;

    }

    public static String Enctypt(String plainText, int n, int e) {
        StringBuilder cipherText = new StringBuilder();
        char[] plainTextArray = plainText.toCharArray();
        BigInteger m, result;
        BigInteger N = BigInteger.valueOf(n);
        BigInteger E = BigInteger.valueOf(e);
        for (int i = 0; i < plainTextArray.length; i++) {
            m = BigInteger.valueOf( (int)plainTextArray[i]);
            result = m.modPow(E, N);

            cipherText.append(result.intValue()).append(" ");
        }

        return cipherText.toString();
    }

    public static String Decrypt(String cipherText, int n, int d) {
        StringBuilder plainText = new StringBuilder();
        String[] cipherTextArray = cipherText.split(" ");
        BigInteger m, result;
        BigInteger N = BigInteger.valueOf(n);
        BigInteger D = BigInteger.valueOf(d);
        for (int i = 0; i < cipherTextArray.length; i++) {
            m = BigInteger.valueOf(Integer.parseInt(cipherTextArray[i]));
            result = m.modPow(D, N);

            plainText.append((char)result.intValue());
        }
        return plainText.toString();
    }

}
