TRUNCATE TABLE public.plant;
TRUNCATE TABLE public.environment;
TRUNCATE TABLE public.sensor;

INSERT INTO public.sensor
(id,type,port,board,low_calibration,high_calibration,created_by,updated_by,created_ts,updated_ts)
VALUES
(1,'Water level',1,1,20.2,1.8,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,'Humidity/Temperature',1,2,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,'Humidity/Temperature',1,3,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,'Humidity/Temperature',1,4,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(5,'Humidity/Temperature',1,5,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(6,'Light',1,6,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(7,'Light',1,7,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(8,'Light',1,8,NULL,NULL,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(9,'Soil moisture',2,1,1.4,3.8,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(10,'Soil moisture',2,3,1.44,3.9,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(11,'Soil moisture',2,5,1.38,3.78,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(12,'Soil moisture',2,7,1.42,3.8,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(13,'Soil moisture',3,1,1.39,3.82,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO public.environment
(location,sensor_id,created_by,updated_by,created_ts,updated_ts)
VALUES
('North',2,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('South',3,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('East',4,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('West',5,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Water Tank',1,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('East',6,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('West',7,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('North',8,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO public.plant
(name,number,pot_size_id,high_moisture,low_moisture,sensor_id,date_obtain,monitor,dead,delete,created_by,updated_by,created_ts,updated_ts)
VALUES
('Fiddle-Leaf Fig',NULL,3,75,35,9,NULL,1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Philodendron',1,2,70,30,10,'2023-01-10',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Philodendron',2,2,70,30,11,'2023-02-02',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Orchid',1,2,65,30,NULL,NULL,0,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Orchid',2,2,65,30,13,'2023-02-02',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('African Mask',NULL,2,60,20,NULL,'2022-11-12',0,1,1,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);