package mz.co.jcf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest
@AutoConfigureMockMvc
class PostgresIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JdbcTemplate db;
  @Test void flywayCreatedSchema() {
    Integer count = db.queryForObject("SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name IN ('clients','loans','payments','audit_events','loan_applications')", Integer.class);
    assertEquals(5, count);
  }
  @Test void anonymousCannotListClients() throws Exception {
    mvc.perform(get("/api/v1/clients")).andExpect(status().isUnauthorized());
  }
  @Test @WithMockUser(roles = "ADMIN")
  void createAndListClientPersistsAudit() throws Exception {
    String payload = "{\"fullName\":\"Cliente de Teste\",\"phone\":\"+258840001111\",\"district\":\"Maputo\"}";
    mvc.perform(post("/api/v1/clients").contentType(MediaType.APPLICATION_JSON).content(payload))
      .andExpect(status().isCreated()).andExpect(jsonPath("$.fullName").value("Cliente de Teste"));
    mvc.perform(get("/api/v1/clients")).andExpect(status().isOk())
      .andExpect(jsonPath("$[0].fullName").value("Cliente de Teste"));
    Integer audits = db.queryForObject("SELECT count(*) FROM audit_events WHERE action='CREATE' AND entity_type='CLIENT'",Integer.class);
    assertTrue(audits != null && audits > 0);
  }
}
