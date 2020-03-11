package com.gardenlink_mobile.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class CustomSSLSocketFactory {
    public static SSLSocketFactory getSSLSocketFactory() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        String rootca = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFsDCCBJigAwIBAgISAzE0QH5qeMn5uBGu6QFGDsxEMA0GCSqGSIb3DQEBCwUA\n" +
                "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
                "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0yMDAyMTAxMjExNTNaFw0y\n" +
                "MDA1MTAxMjExNTNaMBcxFTATBgNVBAMTDGFydGhlcmlvbS5mcjCCASIwDQYJKoZI\n" +
                "hvcNAQEBBQADggEPADCCAQoCggEBAMK85hA+HZDPnVjnfvvBgsx5epAD93qWByjl\n" +
                "BK+Ist2hWLUr3yeqahsFSFXeZW7ITYvFeMMXaK4k3kIAOOe/4YnttLlaSC2IR8nn\n" +
                "t4hWQFjr/4r9htIY+XuaYpyl1rPN/ak34TJwI+2As2JiYSE8bH/MFX0f1yvwJ81e\n" +
                "+r8hw37uqg9v87zdTT0Vvx3lA7K6mFrONiP9xNjoTFEZOyFq+lRRPYTKjqOcOBdY\n" +
                "uZ2M8IkYo7k5OMljydHFeS0hTCaRGY1wSMm7qs90NLyOe2Y9w578zXQrpE46pgiQ\n" +
                "t604rO0t/kPE3Sfdll8qpFb0HmwXXcP5XDKDvZvwmE6XHfn35EcCAwEAAaOCAsEw\n" +
                "ggK9MA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUH\n" +
                "AwIwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUNnIOByOajDFGQGLb4nccnPbMQhAw\n" +
                "HwYDVR0jBBgwFoAUqEpqYwR93brm0Tm3pkVl7/Oo7KEwbwYIKwYBBQUHAQEEYzBh\n" +
                "MC4GCCsGAQUFBzABhiJodHRwOi8vb2NzcC5pbnQteDMubGV0c2VuY3J5cHQub3Jn\n" +
                "MC8GCCsGAQUFBzAChiNodHRwOi8vY2VydC5pbnQteDMubGV0c2VuY3J5cHQub3Jn\n" +
                "LzB1BgNVHREEbjBsggsqLmFyY2hhbC5mcoIOKi5hcnRoZXJpb20uZnKCDCouY2xp\n" +
                "LWJkZS5mcoINKi5nb2xkaGVpbS5mcoIJYXJjaGFsLmZyggxhcnRoZXJpb20uZnKC\n" +
                "CmNsaS1iZGUuZnKCC2dvbGRoZWltLmZyMEwGA1UdIARFMEMwCAYGZ4EMAQIBMDcG\n" +
                "CysGAQQBgt8TAQEBMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly9jcHMubGV0c2VuY3J5\n" +
                "cHQub3JnMIIBBgYKKwYBBAHWeQIEAgSB9wSB9ADyAHcA5xLysDd+GmL7jskMYYTx\n" +
                "6ns3y1YdESZb8+DzS/JBVG4AAAFwLzpIqgAABAMASDBGAiEAzNPJo5KylpLHwdgZ\n" +
                "1c/pWmmh+WzDv/Yp9Nv4LMhuNEcCIQDGS5qQ3WGSatnUsdoKDkhV6YQq6LqA8mNv\n" +
                "c/gGazS2YAB3AAe3XBvlfWj/8bDGHSMVx7rmV3xXlLdq7rxhOhpp06IcAAABcC86\n" +
                "SNoAAAQDAEgwRgIhAODOSTfDLvZianw291nv1r0kecPntfNsn1aFdnKbWx0/AiEA\n" +
                "9ufEZlKqxVDfCDwZRADs/X1LTWOUmWCVcgDBxrK5u5owDQYJKoZIhvcNAQELBQAD\n" +
                "ggEBAE7+kLcrIiOnydF4mz7tmqpYh5qzbsGAU6gaVzwyq3v09lMzwOgnZGsV1IZE\n" +
                "FRHe6GdMFJsw2fhhz5Dw5fPaxtpWHbWf9463vqH5iNdDKolnUfHsNB11DMcduyGc\n" +
                "B97Qv+5OAwLz16/PwtcAPW5tt1i6ICUoViWhOZ9N1IAuEIXFAmgNoRXK6cEBJHJB\n" +
                "QT5SvIvABQ/AHym6uUsajIWIgvDMDGUQAhMJrCy66/LX0Rh6cvKILz0FdUZYdU9G\n" +
                "qsMiatoFKcyczV5fiENB7B/9AguH/EohwmHhNkaa3cnfkdi0ZCTrDP+z2oFBgdH5\n" +
                "xng/e0PYOIxZK6xCk17yten3L8g=\n" +
                "-----END CERTIFICATE-----\n";
        InputStream caInput = new BufferedInputStream(new ByteArrayInputStream(rootca.getBytes()));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }
}
