import com.alibaba.fastjson.JSONObject;
import com.forthreal.crypto.AppCryptoException;
import com.forthreal.crypto.SignJson;
import com.forthreal.crypto.SignedJsonValidator;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestCases {
    @BeforeAll
    static void beforeAll() {
    }

    private static String generatedString;
    @Test
    @Order(0)
    @DisplayName("Test that we can generate a signed JSON package")
    void generationTest() {
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
    void parsingTest() {
        Assertions.assertDoesNotThrow(() -> {
            var validator = new SignedJsonValidator();

            validator.validateJsonFromSignedString(generatedString)
                    .orElseThrow(() -> new AppCryptoException("Unable to validate the signed package"));
        });
    }
}
