package mz.co.jcf;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class SecurityConfigTest {
  @Test void passwordEncoderUsesBcrypt() {
    var encoder = new SecurityConfig().passwordEncoder();
    var hash = encoder.encode("temporary-test-password");
    assertTrue(hash.startsWith("$2"));
    assertTrue(encoder.matches("temporary-test-password", hash));
    assertFalse(encoder.matches("wrong-password", hash));
  }
}
