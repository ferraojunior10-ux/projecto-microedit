package mz.co.jcf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
class ClientControllerTest {
  @Test void createClientWritesClientAndAuditEvent() {
    JdbcTemplate db = mock(JdbcTemplate.class);
    ClientController controller = new ClientController(db);
    var auth = new UsernamePasswordAuthenticationToken("auditor", "ignored");
    var client = controller.create(new ClientController.CreateClient("Maria Matola", "+258840000000", null, "Matola"), auth);
    assertNotNull(client.id());
    assertEquals("Maria Matola", client.fullName());
    verify(db).update(startsWith("INSERT INTO clients"), eq(client.id()), eq("Maria Matola"), eq("+258840000000"), isNull(), eq("Matola"));
    verify(db).update(startsWith("INSERT INTO audit_events"), any(UUID.class), eq("auditor"), eq("CREATE"), eq("CLIENT"), eq(client.id()));
  }
}
