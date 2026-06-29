curl -s -X POST "http://localhost:8081/realms/service-accounts/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=service-api" \
  -d "client_secret=service-api-secret" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password"