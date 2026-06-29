# default target
default:
    @just --list

# show available commands
list:
    @just --list

# run Spring Boot app
run:
    ./gradlew bootRun

# debug Spring Boot app
debug:
    ./gradlew bootRun --args='--debug'

# clean build
build:
    ./gradlew clean build
    
    
docker:
    ./gradlew bootJar && ./gradlew bootBuildImage

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