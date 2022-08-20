package com.forthreal.crypto;

import com.alibaba.fastjson.JSONObject;
import com.forthreal.crypto.dto.RequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;

public class SignJson extends KeyHolder {
    private Logger logger = LogManager.getLogger(SignJson.class);

    public RequestDto getSignedJson(JSONObject object) throws AppCryptoException, SignatureException {
        var text = object.toJSONString();
        var signature = getInitialisedPrivateSignature().orElseThrow(() -> new AppCryptoException("Unable to get private key"));
        signature.update(text.getBytes(StandardCharsets.UTF_8));

        var signed = signature.sign();
        var encoder = Base64.getEncoder();
        return new RequestDto(encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8)), encoder.encodeToString(signed));
    }

    public String getSignedJsonString(JSONObject object) throws AppCryptoException, SignatureException {
        var dto = getSignedJson(object);
        return dto.getRequestString() + "." + dto.getSignatureText();
    }
}
