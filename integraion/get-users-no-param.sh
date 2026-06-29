#!/bin/bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"

TOKEN=$(./get-token.sh | jq -r .access_token)

curl -s -w "\n%{http_code}" -X GET "${BASE_URL}/api/v1/users" \
  -H "Authorization: Bearer $TOKEN"
