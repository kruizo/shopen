-- src/main/resources/db/migration/V1__init.sql
CREATE TABLE test_flyway (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
