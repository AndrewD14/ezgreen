INSERT INTO public.sensor_type
(type,arduino,created_by,updated_by,created_ts,updated_ts)
VALUES
('Water level','w','system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Humidity/Temperature',NULL,'system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Light',NULL,'system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Soil moisture','m','system','system',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);