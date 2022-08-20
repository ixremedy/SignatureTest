package com.forthreal.crypto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SignatureException;
import java.util.Base64;
import java.util.Optional;

public class SignedJsonValidator extends KeyHolder {
    private Logger logger = LogManager.getLogger(SignedJsonValidator.class);

    public Optional<JSONObject> validateJsonFromSignedString(String signedString) throws AppCryptoException, SignatureException {
        var parts = signedString.split("\\.");

        if(parts.length < 2) {
            logger.error("The input string doesn't seem to be a valid input");
            return Optional.empty();
        }

        var signature = getInitialisedPublicSignature().orElseThrow( () -> new AppCryptoException("Couldn't get public key for verification"));
        var decoder = Base64.getDecoder();
        var decodedBody = decoder.decode(parts[0]);
        var decodedSignature = decoder.decode(parts[1]);

        signature.update(decodedBody);

        if(signature.verify(decodedSignature)) {
            return Optional.ofNullable(JSON.parseObject(new String(decodedBody)));
        }

        logger.error("Signature validation failed");
        return Optional.empty();
    }
}
