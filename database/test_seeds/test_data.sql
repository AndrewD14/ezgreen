TRUNCATE TABLE public.plant;
TRUNCATE TABLE public.environment;
TRUNCATE TABLE public.sensor;
TRUNCATE TABLE public.relay;

INSERT INTO public.board
(id,bus,number)
VALUES
(1,1,1),
(2,1,2),
(3,1,3);

INSERT INTO public.sensor
(id,type_id,number,board_id,port,low_calibration,high_calibration,environment_id,delete,created_by,updated_by,created_ts,updated_ts)
VALUES
(1,1,1,1,2,20.2,1.8,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,2,1,1,6,NULL,NULL,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,2,2,2,6,NULL,NULL,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,2,3,2,8,NULL,NULL,4,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(5,2,4,3,6,NULL,NULL,4,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(6,3,1,1,3,NULL,NULL,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(7,3,2,1,4,NULL,NULL,5,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(8,3,3,2,4,NULL,NULL,3,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(9,4,1,1,1,1.4,3.8,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(10,4,2,1,5,1.44,3.9,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(11,4,3,1,7,1.38,3.78,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(12,4,4,2,1,1.42,3.8,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(13,4,5,2,5,1.39,3.82,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


INSERT INTO public.relay
(number,type_id,board_id,relay,environment_id,delete,created_by,updated_by,created_ts,updated_ts)
VALUES
(1,1,1,1,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,1,1,2,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,1,1,3,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,1,1,4,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(5,1,1,5,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(6,1,1,6,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(1,2,1,7,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,2,1,8,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,2,2,1,3,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(1,3,2,2,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,3,2,3,3,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(1,4,2,4,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,4,2,5,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,4,2,6,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,4,2,7,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(1,6,2,8,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,6,3,1,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,6,3,2,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,6,3,3,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(5,6,3,4,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(6,6,3,5,2,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(7,6,3,6,3,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(8,6,3,7,3,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(1,5,3,8,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


INSERT INTO public.environment
(id,name,sensor_type,low_desire,high_desire,target,humidity,time_start,time_end,delete,created_by,updated_by,created_ts,updated_ts)
VALUES
(1,'North Lights',3,NULL,NULL,84,NULL,'14:00','02:00',0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(2,'North Temp',2,65,80,72,NULL,NULL,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(3,'Center Lights',3,NULL,NULL,80,NULL,'14:00','02:00',0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(4,'Center Humidity/Temp',2,65,80,72,60,NULL,NULL,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
(5,'South Lights',3,NULL,NULL,80,NULL,'14:00','02:00',0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO public.plant
(name,number,pot_size_id,plant_type_id,high_moisture,low_moisture,sensor_id,date_obtain,monitor,dead,delete,created_by,updated_by,created_ts,updated_ts)
VALUES
('Fiddle-Leaf Fig',NULL,3,3,75,35,9,NULL,1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Philodendron',1,2,2,70,30,10,'2023-01-10',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Philodendron',2,2,2,70,30,11,'2023-02-02',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Orchid',1,2,10,65,30,NULL,NULL,0,1,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Orchid',2,2,10,65,30,13,'2023-02-02',1,0,0,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('African Mask',NULL,2,2,60,20,NULL,'2022-11-12',0,1,1,'a.damico','a.damico',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);