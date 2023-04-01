CREATE TABLE public.board(
   id       SMALLSERIAL PRIMARY KEY,
   bus      SMALLINT NOT NULL,
   number   SMALLINT NOT NULL
);

CREATE TABLE public.sensor_type(
   id          SMALLSERIAL PRIMARY KEY,
   type        VARCHAR(20) NOT NULL,
   arduino     VARCHAR(1),
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
   

CREATE TABLE public.sensor(
   id                SMALLSERIAL PRIMARY KEY,
   number            SMALLINT NOT NULL,
   type_id           SMALLINT NOT NULL,
   board_id          SMALLINT NOT NULL,
   port              SMALLINT NOT NULL,
   low_calibration   REAL,
   high_calibration  REAL,
   environment_id    SMALLINT,
   delete            SMALLINT NOT NULL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.relay_type(
   id          SMALLSERIAL PRIMARY KEY,
   type        VARCHAR(20) NOT NULL,
   arduino     VARCHAR(1),
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.relay(
   id             SMALLSERIAL PRIMARY KEY,
   number         SMALLINT NOT NULL,
   type_id        SMALLINT NOT NULL,
   board_id       SMALLINT NOT NULL,
   relay          SMALLINT NOT NULL,
   environment_id SMALLINT,
   delete         SMALLINT NOT NULL,
   created_by     VARCHAR(100) NOT NULL,
   updated_by     VARCHAR(100) NOT NULL,
   created_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.environment(
   id          SMALLSERIAL PRIMARY KEY,
   name        VARCHAR(100) NOT NULL,
   sensor_type SMALLINT NOT NULL,
   low_desire  REAL,
   high_desire REAL,
   target      REAL,
   humidity    REAL,
   time_start  TIME WITHOUT TIME ZONE,
   time_end    TIME WITHOUT TIME ZONE,
   delete      SMALLINT NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.pot_size(
   id          SMALLSERIAL PRIMARY KEY,
   name        VARCHAR(10) NOT NULL,
   size        VARCHAR(30) NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.plant_type(
   id          SMALLSERIAL PRIMARY KEY,
   name        VARCHAR(10) NOT NULL,
   arduino     SMALLINT NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.plant(
   id             SERIAL PRIMARY KEY,
   name           VARCHAR(100) NOT NULL,
   number         INTEGER,
   pot_size_id    SMALLINT NOT NULL,
   plant_type_id  SMALLINT NOT NULL,
   high_moisture  REAL NOT NULL,
   low_moisture   REAL NOT NULL,
   sensor_id      SMALLINT,
   date_obtain    DATE,
   monitor        SMALLINT NOT NULL,
   dead           SMALLINT NOT NULL,
   delete         SMALLINT NOT NULL,
   created_by     VARCHAR(100) NOT NULL,
   updated_by     VARCHAR(100) NOT NULL,
   created_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts     TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.history_temp(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   temp        REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.history_humidity(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   humidity    REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.history_luminosity(
   id          SERIAL PRIMARY KEY,
   read        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   lux         REAL NOT NULL,
   created_by  VARCHAR(100) NOT NULL,
   updated_by  VARCHAR(100) NOT NULL,
   created_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.history_water_level(
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

CREATE TABLE public.history_soil_moisture(
   id                BIGSERIAL PRIMARY KEY,
   plant_id          SMALLINT NOT NULL,
   read              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   volt              REAL NOT NULL,
   low_calibration   REAL NOT NULL,
   high_calibration  REAL NOT NULL,
   created_by        VARCHAR(100) NOT NULL,
   updated_by        VARCHAR(100) NOT NULL,
   created_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   updated_ts        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);