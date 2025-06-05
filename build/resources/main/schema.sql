CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS file_entity (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    data BYTEA,
    user_id INTEGER REFERENCES app_user(id)
);