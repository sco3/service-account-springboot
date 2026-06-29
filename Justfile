# default target
default:
    @just --list

# show available commands
list:
    @just --list

# check if required services are running
check:
    #!/usr/bin/env bash
    set -e
    FAIL=0
    echo "Checking required services..."
    for PORT in 8123 8081; do
        if netstat -ltnp 2>/dev/null | grep -q ":${PORT} "; then
            echo "  [OK] Port ${PORT} is listening"
        else
            echo "  [FAIL] Port ${PORT} is NOT listening"
            FAIL=1
        fi
    done
    echo "Checking docker containers..."
    for CNT in service-account-clickhouse service-account-keycloak; do
        if docker ps --filter "name=$CNT" --filter "status=running" --format "{{ '{{' }}.Names{{ '}}' }}" 2>/dev/null | grep -q "$CNT"; then
            echo "  [OK] ${CNT} is running"
        else
            echo "  [FAIL] ${CNT} is not running"
            FAIL=1
        fi
    done
    if [ $FAIL -eq 1 ]; then
        echo "Some services are down. Run 'just up' to start them."
        exit 1
    fi
    echo "All services OK."

# run Spring Boot app
run:
    ./gradlew bootRun

# debug Spring Boot app
debug:
    ./gradlew bootRun --args='--debug'

# clean build
build:
    ./gradlew clean build
    
# build docker image
docker:
    ./gradlew bootJar && ./gradlew bootBuildImage

# run app with clickhouse and keycloak
compose:
    docker compose -f docker-compose.yml -f docker-compose.app.yml up --build

# run tests
test:
    ./gradlew test

# stop docker stack
down:
    docker compose down

# start docker stack (keycloak + clickhouse)
up:
    docker compose up -d

# restart everything
restart:
    just down
    just up

# show logs
logs:
    docker compose logs -f

# run only keycloak logs
logs-keycloak:
    docker compose logs -f keycloak

# run only clickhouse logs
logs-clickhouse:
    docker compose logs -f clickhouse