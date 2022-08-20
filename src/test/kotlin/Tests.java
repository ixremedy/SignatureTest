import com.alibaba.fastjson.JSONObject;
import com.forthreal.crypto.AppCryptoException;
import com.forthreal.crypto.SignJson;
import com.forthreal.crypto.SignedJsonValidator;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Tests {
    private String generatedString;

    @Test
    @Order(0)
    @DisplayName("Test that we can generate a signed JSON package")
    public void testGeneration() {
        var inputObject = new JSONObject();
        inputObject.put("stringTest", "doTest");
        inputObject.put("intTest", 10);

        Assertions.assertDoesNotThrow( () -> {
            var signer = new SignJson();

            generatedString = signer.getSignedJsonString(inputObject);
        });
    }

    @Test
    @Order(1)
    @DisplayName("Check that we're able to validate generated JSON")
    public void testParsing() {
        Assertions.assertDoesNotThrow(() -> {
            var validator = new SignedJsonValidator();

            validator.validateJsonFromSignedString(generatedString)
                    .orElseThrow(() -> new AppCryptoException("Unable to validate the signed package"));
        });
    }
}
