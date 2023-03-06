CREATE TABLE sensor(
   id                SMALLSERIAL PRIMARY KEY,
   type              VARCHAR(20) NOT NULL,
   port              INTEGER NOT NULL,
   board             INTEGER NOT NULL,
   low_calibration   REAL,
   high_calibration  REAL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE soil_moisture(
   id                SMALLSERIAL PRIMARY KEY,
   high_setting      REAL NOT NULL,
   low_setting       REAL NOT NULL,
   sensor_id         SMALLSERIAL NOT NULL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE greenhouse(
   id          SMALLSERIAL PRIMARY KEY,
   location    VARCHAR(100) NOT NULL,
   sensor_id   SMALLSERIAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE pot_size(
   id          SMALLSERIAL PRIMARY KEY,
   size        VARCHAR(30) NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE plant(
   id             SMALLSERIAL PRIMARY KEY,
   name           VARCHAR(100) NOT NULL,
   number         INTEGER,
   pot_size_id    SMALLSERIAL NOT NULL,
   date_obtain    DATE,
   soil_moisture  SMALLSERIAL NOT NULL,
   dead           SMALLINT NOT NULL,
   delete         SMALLINT,
   created_by     VARCHAR(100) NOT NULL,
   updated_by     VARCHAR(100) NOT NULL,
   created_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE history_temp(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   temp        REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE history_humidity(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   humidity    REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE history_luminosity(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   lux         REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE history_water_level(
   id                SERIAL PRIMARY KEY,
   read              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   cm                REAL NOT NULL,
   low_calibration   REAL NOT NULL,
   high_calibration  REAL NOT NULL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE history_soil_moisture(
   id                BIGSERIAL PRIMARY KEY,
   plant_id          SMALLSERIAL NOT NULL,
   read              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   volt              REAL NOT NULL,
   low_calibration   REAL NOT NULL,
   high_calibration  REAL NOT NULL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);