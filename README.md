# Service Account - Spring Boot

## 1. Overview

This project uses Spring Security OAuth2 Resource Server with Keycloak as the identity provider.

A key challenge in containerized environments is ensuring that the `iss` (Issuer) claim in the JWT remains consistent between the host machine (where tests run) and the Docker network (where the app runs).

### Single-Source Issuer Configuration

Keycloak is configured to force a specific hostname and port. By setting the `hostname` and `http-port` consistently in both environments, the JWT `iss` claim is generated predictably, allowing Spring Boot to validate tokens without environment-specific hacks.

## 2. Infrastructure (Docker Compose)

### Keycloak (`docker-compose.infra.yml`)

| Flag | Purpose |
|---|---|
| `--hostname=keycloak` | Allows services inside the Docker network to resolve the host |
| `--http-port=8081` | Standardizes the port |
| `--hostname-strict=false` | Prevents 401 errors during development by allowing non-matching `Host` headers |

### ClickHouse (`docker-compose.infra.yml`)

- Image: `clickhouse/clickhouse-server:24.3`
- Ports: `8123` (HTTP), `9000` (native)
- Default credentials: `admin` / `admin123`

### App (`docker-compose.app.yml`)

- Image: `acc:0.0.1-SNAPSHOT`
- Depends on both ClickHouse and Keycloak (healthy)
- Overrides `issuer-uri` to use the internal Docker service name `keycloak`

## 3. Application Security (Spring Boot)

OIDC Discovery is used. Spring Boot automatically fetches JWK sets and issuer details via `/.well-known/openid-configuration` at runtime.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(NON_SECURE_MATCHERS).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
```

## 4. Environment-Specific Configurations

### Local Development / CI Tests

`application.yaml` defines the default issuer for local runs:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/service-accounts
```

### Docker Compose

`docker-compose.app.yml` overrides the issuer to use the internal Docker service name:

```yaml
environment:
  SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://keycloak:8081/realms/service-accounts
```

## 5. Troubleshooting

### "The iss claim is not valid"

If you encounter `401 Unauthorized` errors with the log `The iss claim is not valid`:

1. **Extract the `iss` from the token:**
   ```java
   var jwt = JWTParser.parse(accessToken);
   System.out.println("DEBUG: Issuer in token: " + jwt.getJWTClaimsSet().getIssuer());
   ```

2. **Match the config:** Ensure the `issuer-uri` in your configuration matches the `iss` value printed in the debug logs exactly.

3. **Check Keycloak logs:** If you see `SocketException: Unexpected end of file`, verify that `KC_HOSTNAME_STRICT=false` is set in your `docker-compose.infra.yml`.

## Quick Start

```bash
# Start infrastructure (Keycloak + ClickHouse)
just infra

# Run the app locally
just run

# Run integration tests
just ci

# Start full stack (app + infra)
just compose
```

### Available Commands

| Command | Description |
|---|---|
| `just infra` | Start ClickHouse and Keycloak |
| `just run` | Run Spring Boot app locally |
| `just debug` | Run Spring Boot app with debug mode |
| `just build` | Clean build |
| `just test` | Run tests |
| `just ci` | Run integration tests |
| `just compose` | Start full Docker stack |
| `just up` / `just down` | Start/stop Docker stack |
| `just restart` | Restart everything |
| `just logs` | Tail Docker logs |
| `just check` | Verify all services are running |
