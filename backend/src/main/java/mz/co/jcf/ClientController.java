package mz.co.jcf;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
  private final JdbcTemplate db;
  public ClientController(JdbcTemplate db) { this.db = db; }
  public record ClientDto(UUID id, String fullName, String phone, String email, String district) {}
  public record CreateClient(
    @NotBlank @Size(max=200) String fullName,
    @NotBlank @Size(max=32) String phone,
    @Size(max=254) String email,
    @NotBlank @Size(max=120) String district
  ) {}
  @GetMapping
  public List<ClientDto> list() {
    return db.query("SELECT id, full_name, phone, email, district FROM clients ORDER BY created_at DESC LIMIT 100",
      (rs,n) -> new ClientDto(rs.getObject("id",UUID.class),rs.getString("full_name"),rs.getString("phone"),rs.getString("email"),rs.getString("district")));
  }
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Transactional
  public ClientDto create(@Valid @RequestBody CreateClient request, Authentication auth) {
    UUID id = UUID.randomUUID();
    db.update("INSERT INTO clients (id, full_name, phone, email, district) VALUES (?,?,?,?,?)",
      id, request.fullName(), request.phone(), request.email(), request.district());
    db.update("INSERT INTO audit_events (id,actor,action,entity_type,entity_id) VALUES (?,?,?,?,?)",
      UUID.randomUUID(), auth.getName(), "CREATE", "CLIENT", id);
    return new ClientDto(id, request.fullName(), request.phone(), request.email(), request.district());
  }
}
