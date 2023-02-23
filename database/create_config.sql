CREATE TABLE config(
   id          SERIAL PRIMARY KEY,
   name        VARCHAR(100),
   created_by  VARCHAR(100),
   updated_by  VARCHAR(100),
   created_ts  TIMESTAMP WITHOUT TIME ZONE,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE
);