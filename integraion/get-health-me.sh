TOKEN=$(./get-token.sh | jq -r .access_token)

curl -v http://localhost:8080/api/v1/health/me \
  -H "Authorization: Bearer $TOKEN"

