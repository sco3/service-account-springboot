#!/bin/bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"

TOKEN=$(./get-token.sh | jq -r .access_token)

curl -s -X GET "${BASE_URL}/api/v1/users" \
  -H "Authorization: Bearer $TOKEN"

