INSERT INTO public.sensor_type
(id,type,arduino,created_by,updated_by,created_ts,updated_ts)
VALUES
(1,'Water level','w','system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,'Humidity/Temperature',NULL,'system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,'Light',NULL,'system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,'Soil moisture','m','system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);