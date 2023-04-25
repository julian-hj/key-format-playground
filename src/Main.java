import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.*;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String generatedPassword = "pass";
        String caCert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEMjCCApqgAwIBAgIRAKOjxX6t12yMPPzrsYavGlkwDQYJKoZIhvcNAQELBQAw\n" +
                "IjEMMAoGA1UEBhMDVVNBMRIwEAYDVQQKEwlzZWNyZXRnZW4wHhcNMjMwMzI3MjEz\n" +
                "NjEyWhcNMjQwMzI2MjEzNjEyWjAiMQwwCgYDVQQGEwNVU0ExEjAQBgNVBAoTCXNl\n" +
                "Y3JldGdlbjCCAaIwDQYJKoZIhvcNAQEBBQADggGPADCCAYoCggGBAKLJ0F3gTAzW\n" +
                "a+VitZ2rpUMNICDtJPWmeIQDv0HVns8cxGpp9Pom9l3Wuk2olJTtKhUatm/xJaCw\n" +
                "owZaR3HIEYTpD2G/I+Mml9+dM4zKHyxjKtfFINIkMliu6JQN6gVuEZcjl4La/sRc\n" +
                "LFHKiFPvvAj93UjHZVqZs/IyYNlUV5kgSgaICEirZkF5xwuGn4TrXunoFCyJlC12\n" +
                "4hSYHhspU/dTvuxuq2bXo8WCboJA36A3sB4GdiPjhl0mTBAS/4HbOUukSgymPauv\n" +
                "mk++s63C0z0qQLDAywJZf1U3+aTZkWo99qHoM4hali9h+oG7Rwr4v7ob19PH6jJ0\n" +
                "1LpEA8FKVkShPcGUKGVFEdGmzQ0l0dHj9KNI4CiPhrySa623MNMqav7lA2T66vuv\n" +
                "eHLSJVym14Vrne+LoJMXzFGggtxke44jIowqxfPzaDYPd+SDlVHirb9s0gUx1Wtt\n" +
                "zsE3g7vx2Bm62+G+bFs+d6JXATyglFqxE9F025F1TgHAgY+OxQK54QIDAQABo2Mw\n" +
                "YTAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zAdBgNVHQ4EFgQUqeg1\n" +
                "o3p/Vt7U1HYrXSX+vnpI1ZgwHwYDVR0jBBgwFoAUqeg1o3p/Vt7U1HYrXSX+vnpI\n" +
                "1ZgwDQYJKoZIhvcNAQELBQADggGBAHo4dNiF33ew7kHaTIDttBDbu8iqQDtM9Z74\n" +
                "wl2a8SaQeuEluVybvxuDeQdG1Iu/+6mHfrXHpWx0uDHYdEyTg1Bp75x8bV6uDkFC\n" +
                "4EmmiiXyGR+hWccbBlETNQPhCwSpF8udPBL4i9k2Uf8Aau/kMXCgI7uGK7Hi1/XA\n" +
                "wb06G+MpV9AGOWGO70+djX1vhaD/lOkRg9+dOuOlkpo/9iRY2WAT0iDe1Em1uHlh\n" +
                "+CVx6HhcPgWFCXqh5UOQ0maasvbC6rLEaBjA0aIMDnREKunRCJsCbfiBC8ScyMxW\n" +
                "FEDtF2rA7Me+bLhOeb75iuCkX7lhpJZPW3SB1lqvZGS03jMfn+7XdUgFLx2S4KVn\n" +
                "rAg5bVO7NZXbH5y51vUVbCEJTOWDCHKnWN1Jd4ZC2pFHipA/GnhxKpGQnjgkXDMl\n" +
                "4LMe3ttZqhAmiye1zMHi7N+vznvBMlJ8pGqxia/8kT/nrRDhqGRJepuq4kAjk6SR\n" +
                "7EPi/Fz7ctPbrHHvigewPWe1KUhzWw==\n" +
                "-----END CERTIFICATE-----\n";
        String clientCert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIEQzCCAqugAwIBAgIQNd1SG9+6Jx386otzcah4hjANBgkqhkiG9w0BAQsFADAi\n" +
                "MQwwCgYDVQQGEwNVU0ExEjAQBgNVBAoTCXNlY3JldGdlbjAeFw0yMzAzMjcyMTM2\n" +
                "MTJaFw0yNDAzMjYyMTM2MTJaMCIxDDAKBgNVBAYTA1VTQTESMBAGA1UEChMJc2Vj\n" +
                "cmV0Z2VuMIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAyYtw2VGoTdPy\n" +
                "jsCZa9CvvwOEGmCHSyFZtvS248dMxCXd8nvZs8q6CRPOi7C8rnVRIfyIyZxzUXr6\n" +
                "1ylfdPCvIqw8ByPTWVq6C6BNJ8o/exshnC+hNKBDWguwYuH30Eoh+hzDOrpxbO74\n" +
                "atfLywOaZvAiMkpET0G+aXPOk9cchZo7+SRCpCD6pOrLZuz1lDOPMJYvSh/wN3dC\n" +
                "1dl55uH+Zael2HEcP39lIuG2mN70HAD7H5gzJsALNy7C7m75K78i7oWGQEy1DXKu\n" +
                "8jWvLh/0KM3G46HV7QyV82O02rCZwk/iMZ+rSw9Amd+0FrOltcrWmdS7g8dALM9k\n" +
                "PFzAHw0wqGm878u4rjz2L9u3gEaNFMOPVcQivfVGvNQ8bQHwtoc7Bf/wYccqy5LL\n" +
                "+DqLVa6aB5y7uP0LtZX/vl3JUI8MQt0B74CoWRYevJfS6CGOrJoAtdxY8EWSwLaK\n" +
                "Hr+O69uHzdeuUpM80+6P8c8Az6u28TXA1fn5ko/E6iH0sGZ9nnqrAgMBAAGjdTBz\n" +
                "MA4GA1UdDwEB/wQEAwIFoDATBgNVHSUEDDAKBggrBgEFBQcDAjAMBgNVHRMBAf8E\n" +
                "AjAAMB0GA1UdDgQWBBTgRU9n0ZnYaPaROI4N8FCGbfx3/zAfBgNVHSMEGDAWgBSp\n" +
                "6DWjen9W3tTUditdJf6+ekjVmDANBgkqhkiG9w0BAQsFAAOCAYEAiHaqL1DAwnfD\n" +
                "YaVxcryAsdtYPkK1XtOLvN0njbaXbjHkd7/dCOA0kV4H2l1lmYRbIQgmio+tkB38\n" +
                "ToNuBLK0vr0mlOb+hKYyFBEHu1ezQ4fY/0PRGSzFxitAEMKauI8W1u3wuWxgPsvv\n" +
                "hYuntabmB+Tvmf0e9KrXHKY7k5o1rMtkexiJ4e67ypZZyYC1PVGDbP2MmNKPvIcz\n" +
                "hIB6yla1eWqLpHHZgFuo1fNhdPdaj9H9VRDtIDbvH80My0Epg+7QarBJvY81Abzg\n" +
                "RerChR2V474PrsYhtkxJGo80rg3vfTF/lTKpvlmU4a52KaILIgaQFeiNecsD85+V\n" +
                "sF0BC/4g+/XlSO8W7A4hqosbT9q7uUnKW482fk2SpBjroES2BSssqo0p/4d8FX1h\n" +
                "WsmdpgSS8uXKD+AuuOu3fze/UvT5ypgCbw8u7qoYRD/xPtXe78N+fUwD71ROdRnW\n" +
                "U7DLnX7nhPWRLRVdCIg95bHIiHasLeuBygl1VmlPKiBYl/JBgLON\n" +
                "-----END CERTIFICATE-----\n";
        String clientKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIG4wIBAAKCAYEAyYtw2VGoTdPyjsCZa9CvvwOEGmCHSyFZtvS248dMxCXd8nvZ\n" +
                "s8q6CRPOi7C8rnVRIfyIyZxzUXr61ylfdPCvIqw8ByPTWVq6C6BNJ8o/exshnC+h\n" +
                "NKBDWguwYuH30Eoh+hzDOrpxbO74atfLywOaZvAiMkpET0G+aXPOk9cchZo7+SRC\n" +
                "pCD6pOrLZuz1lDOPMJYvSh/wN3dC1dl55uH+Zael2HEcP39lIuG2mN70HAD7H5gz\n" +
                "JsALNy7C7m75K78i7oWGQEy1DXKu8jWvLh/0KM3G46HV7QyV82O02rCZwk/iMZ+r\n" +
                "Sw9Amd+0FrOltcrWmdS7g8dALM9kPFzAHw0wqGm878u4rjz2L9u3gEaNFMOPVcQi\n" +
                "vfVGvNQ8bQHwtoc7Bf/wYccqy5LL+DqLVa6aB5y7uP0LtZX/vl3JUI8MQt0B74Co\n" +
                "WRYevJfS6CGOrJoAtdxY8EWSwLaKHr+O69uHzdeuUpM80+6P8c8Az6u28TXA1fn5\n" +
                "ko/E6iH0sGZ9nnqrAgMBAAECggGAVij/cFhBXCw8qJxbukS3rTAinGghzpOOV/Gb\n" +
                "8hp0jfImRRB/TCZhi5nlFcDTmL1cluvvykjQ38d0TuFIBqUSKf95lg/RvYRUvr2j\n" +
                "3lvO8aDyqGOQRgAxauBOYqd5mpjml5cOOyATd19d4ccpPzvmGRWVeOxY8Y0IiupQ\n" +
                "KNGwRsld1C+VMbdLj480r2ajwdyc3UeHJegreHrKnsNfIr4AVJLsCnCog9jBcr0N\n" +
                "o6LEtugQczxMucclxEyAVxAoA3StBwGJ5x1n6f/v3Hrvq7bn5gz/zZSNvKYGxhl6\n" +
                "UDEmChVxlJWgV6Wzj4/0xW2IQ799CQ3MelqEAMueEF8P/Q1TMLj6zE30xcpt6S26\n" +
                "gBC9a41h5MoVjZAmy0qok6gC19r/bQC1oWLN9zWUzghTbcZa4GaYfsmZOOuM2r0v\n" +
                "8u1jz6kvYkDUv2/TuMlVvj4uj/6SIDJUr3eyqHLFhuqdxXMfjFsheZmZ1wkEx9pe\n" +
                "EgKVVzxGKu6MNfdaSlA3ev9fNpwZAoHBANyd3m6zrxcLXay8rfUPzzWE//+/ESdk\n" +
                "qfeyaee94ar5TY3hqD1lG8VlFwTnn+Uw8fPo841k2fkgYGx/5Bwyqsyn3fTAYDBA\n" +
                "OZ/Vfv+FCJOHjeUUx+uY2UZVk3432ePiFnrrI3q5AVd99VTa5+lNd4TWYjL7FrHQ\n" +
                "wr4IgkpuN8Ng891QEJUJIdg4JJ23x8EobEu2Y3syitUuC5FnvywNoVC6pBFqByCE\n" +
                "BYsQRY4AlKwFpFfntJ641VosATB5M9B3nwKBwQDp3oKKq88wlQq1ki+SDiDnJEip\n" +
                "fsIFGTPKyZEbuUVOYH9nfSpEqWSIbKYQ9nM2mmp8iic6BM/H2T1b3Qnz1pBwYht1\n" +
                "OLYykz/CeYSqEEIspvHSrHbZXOiaMwERgX0ZE7FGr+/ew4jIaezICli053vd5/B8\n" +
                "kWT82whngIlLuVo42BXl0SwB0E43uUkbo9ZXhL+sO/i/o/KQmEGlSmRENsmcRkvq\n" +
                "7nZEtbcHdbdHQSpQEJon78GlCMcIRF6T8F/x0XUCgcAfoFYJwqKACDYWlDnP3sNS\n" +
                "pc1ZKVfZMNoY0y/Rc+wM6Y5Pn6DRpFP12UcSge3vGjQlRijcBTAmQZjxFOhrjmvj\n" +
                "yNDNzGI9qIdaPW3mTXjsRUt3IqKCSLglGBF7z5/hxkB4wIqmhi0GEN2Bm5wRWvSe\n" +
                "S2lqa4pWVVoFigy7hM9jp9ttx5OI+fUrHOlK3tS5AQV9WRkryRQT+XTZFGlYdF/C\n" +
                "xe7NPEjnRWXSCLQf5j9p5akonOPMuSyEr7g/7bAa23sCgcBuya/BjV5bP1+RYC0U\n" +
                "3YiMts2NLbUvwR6d5BrPrVa9qEzyc4LAgBZV4lJRGs69gS0Mzk2C2KCtpaXBjNaY\n" +
                "In7OHkuvniwBjWvY7sP/5C/VA0jvai/rDG5MDflll5fxdzi1qcbSGoMDndpDcVNQ\n" +
                "b5BoMOXlvOLAPeqfTW685pNs9kB5XDuiqFFFaIrPgy+YyhQavVY7Qqk4AkimyrTw\n" +
                "85tSYqK4Kjrwlbyc1hD6uk4XG6ZLYPK1AZF3wxuirW5mGbkCgcEAirJRWhGhDNgP\n" +
                "r7zSdmEVSqcW3BzH4CTjgxI2s3x9GIQK6v2ubiNyk8VmimdJGHohJyWHIl5XNO3U\n" +
                "5oYr14PczVU9/tq4b3lUz/g9smYrMv3qGAohwRVQqs5Coa9WPmSCOwLWDUphOfjb\n" +
                "4eYYeGrVUF8huJiMA9VL5WCyHXT83xdnAwXUGtbYGyaFsy9P+YM9kL2M3TKMfQab\n" +
                "FVp/0U9P5w3vOrjA5Y+UiTxfzhxbKoiMfQcfMkiKQbArm9vHA69I\n" +
                "-----END RSA PRIVATE KEY-----\n";

        // Create a keystore
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null, generatedPassword.toCharArray());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(new ByteArrayInputStream(caCert.getBytes()));
            ks.setCertificateEntry("rootca", ca);
            Certificate client = cf.generateCertificate(new ByteArrayInputStream(clientCert.getBytes()));
            PemReader reader = new PemReader(new CharArrayReader(clientKey.toCharArray()));
            PemObject pemObject = reader.readPemObject();
            byte[] content = pemObject.getContent();

            PrivateKey key = getPKCS1PrivateKey(content, "RSA");


            KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(key, new Certificate[]{client});
            ks.setEntry("eureka", privateKeyEntry, new KeyStore.PasswordProtection(generatedPassword.toCharArray()));

            FileOutputStream fos = new FileOutputStream("client-keystore.p12");
            ks.store(fos, generatedPassword.toCharArray());
            fos.close();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to open keystore output file", e);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Unable to write keystore", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to write keystore", e);
        } catch (CertificateException e) {
            throw new IllegalStateException("Unable to process certificate", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Unable to process private key", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create keystore", e);
        }

    }

    private static PrivateKey getPKCS1PrivateKey(byte[] keyBytes, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        RSAPrivateKey asn1PrivKey = RSAPrivateKey.getInstance(ASN1Sequence.fromByteArray(keyBytes));
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(rsaPrivateKeySpec);
    }
}