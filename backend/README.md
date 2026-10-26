# JCF Microcrédito — API inicial

API Spring Boot 3 / Java 21 / PostgreSQL com migrações Flyway. **Não está pronta para produção.**

## Configuração
- `DATABASE_URL`: URL JDBC, por exemplo `jdbc:postgresql://localhost:5432/jcf`
- `DB_USER`, `DB_PASSWORD`: credenciais PostgreSQL
- `JCF_ADMIN_USER`: utilizador de desenvolvimento
- `JCF_ADMIN_PASSWORD_HASH`: hash BCrypt (não guardar password em texto)
- `JCF_FRONTEND_ORIGIN`: origem exata autorizada (por omissão `http://localhost:5173`)

Criar um hash BCrypt através de ferramenta segura e guardar as variáveis no gestor de segredos do ambiente. Não commitar credenciais.

## Executar
```bash
cd backend
mvn test
mvn spring-boot:run
```

## Endpoints
- `GET /actuator/health`: health check
- `GET /api/v1/clients`: lista limitada a 100 clientes (HTTP Basic, ADMIN)
- `POST /api/v1/clients`: criar cliente e registo de auditoria (HTTP Basic, ADMIN)

## Limitações / bloqueios de produção
- HTTP Basic com administrador único é **apenas uma proteção inicial para desenvolvimento**, não uma solução de autenticação de produção. Implementar OIDC/MFA, RBAC granular e gestão de sessões.
- Não existe integração entre frontend e API, nem endpoints de empréstimos, pagamentos ou cobranças.
- Necessários testes de integração com PostgreSQL, paginação, validação adicional, privacidade, consentimento, proteção de dados, backups, monitorização e revisão regulatória.
- Nunca colocar credenciais no frontend nem permitir dados reais sem revisão de segurança.
