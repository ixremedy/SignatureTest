package com.forthreal.crypto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

public abstract class KeyHolder {
    private static Logger logger = LogManager.getLogger(KeyHolder.class);
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    static {
        var logger = LogManager.getLogger(KeyHolder.class);
        var loader = Thread.currentThread().getContextClassLoader();
        try {
            var privateStream = loader.getResourceAsStream("generated_private.key.der");
            var privateBytes = privateStream.readAllBytes();

            var keySpec = new PKCS8EncodedKeySpec(privateBytes);
            var keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);

            var publicStream = loader.getResourceAsStream("generated_public.der");
            var publicBytes = publicStream.readAllBytes();
            var pubSpec = new X509EncodedKeySpec(publicBytes);
            publicKey = keyFactory.generatePublic(pubSpec);
        } catch (Exception exc) {
            logger.error("Caught error in loading keys: " + exc.getLocalizedMessage());

            throw new RuntimeException(exc);
        }
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    protected Optional<Signature> getInitialisedPrivateSignature() {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(getPrivateKey());
            return Optional.of(signature);
        } catch (Exception exc) {
            logger.error("Error in instantiating signature keys: " + exc.getLocalizedMessage());
        }
        return Optional.empty();
    }

    protected Optional<Signature> getInitialisedPublicSignature() {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            return Optional.of(signature);
        } catch (Exception exc) {
            logger.error("Error in instantiating verification keys: " + exc.getLocalizedMessage());
        }
        return Optional.empty();
    }
}
