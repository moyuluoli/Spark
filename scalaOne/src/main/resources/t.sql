SELECT id, vin, start_time, end_time, start_gps, end_gps, start_soc, end_soc, start_mileage, end_mileage, all_gps, run_mileage, electric_quantity_100km, expects_mileage, average_speed, driving_time, go_electric_quantity, consume_electric_quantity, vehicle_series, `year`, `month`, `day`
FROM dpp.endurance_mileage_gb;


SELECT id, vin, start_time, end_time, start_gps, end_gps, start_soc, end_soc, start_mileage, end_mileage, all_gps, run_mileage, electric_quantity_100km, expects_mileage, average_speed, driving_time, go_electric_quantity, consume_electric_quantity, vehicle_series, `year`, `month`, `day`
FROM dpp.endurance_mileage_gb LIMIT 5;

SELECT id, vin, start_time, end_time from dpp.endurance_mileage_gb where vin='LMGFJ1S50H1000604' AND end_mileage BETWEEN 0 AND 100;

SELECT * from dpp.endurance_mileage_gb where vin='LMGFJ1S50H1000604' LIMIT 6;

SELECT id, vin, start_time, end_time from dpp.endurance_mileage_gb where end_mileage BETWEEN 0 AND 100 LIMIT 5;

SELECT id, vin, start_time, end_time from dpp.endurance_mileage_gb where end_mileage BETWEEN 0 AND 100 LIMIT 5;


SELECT id,vin,start_time,end_time,count(1) as totalNum from endurance_mileage_gb
where vin in('LMGFJ1S50H1000604','LMGHP1S52J1002951','LMWHP1S21K1009272')
GROUP by vin 
#HAVING COUNT(DISCONNECT start_soc)>50
#ORDER by totalNum DESC
limit 10;



SELECT AVG(b.a) FROM (SELECT vin ,SUM(run_mileage) as a 
FROM endurance_mileage_gb WHERE start_time >= 1568304000000 and end_time <=1568563199000 group by vin ) b

SELECT
	vin ,
	SUM(run_mileage) as a
FROM
	endurance_mileage_gb
WHERE
	start_time >= 1568304000000
	and end_time <= 1568563199000
group by
	vin

select * from endurance_mileage_gb limit 6;

SELECT
 AVG(electric_quantity_100km)
FROM
 endurance_mileage_gb
WHERE
 start_time >= 1568304000000
 and end_time <= 1568563199000
 and vehicle_series ='A26'
 and run_mileage >=100
 and electric_quantity_100km>0 


/*bigint ʱ���תʱ���
 * ʱ�����ָ��������ʱ��1970��01��01��00ʱ00��00��(����ʱ��1970��01��01��08ʱ00��00��)�������ڵ��ܺ�������
 * ͨ�׵Ľ��� ʱ�����һ���ܹ���ʾһ��������һ���ض�ʱ����Ѿ����ڵ������Ŀ���֤�����ݡ� ���������Ҫ��Ϊ�û��ṩһ�ݵ���֤�ݣ� ��֤���û���ĳЩ���ݵĲ���ʱ�䡣 
 * ��ʵ��Ӧ���ϣ� ������ʹ���ڰ����������� ���ڻ�ĸ������棬 �����������֧�Ź�����Կ������ʩ�� �����ɷ��ϡ� ����
 *  */
SELECT to_timestamp(CAST(strleft(cast(start_time as string),character_length(cast(start_time as string))-3) as bigint )) as t  FROM energy_consumption_dcdc; 



SELECT
 to_timestamp(CAST(strleft(cast(start_time as string),
 character_length(cast(start_time as string))-3) as bigint )+ 28800) as start_time,
 to_timestamp(CAST(strleft(cast(end_time as string),
 character_length(cast(end_time as string))-3) as bigint )+ 28800) as end_time,
 run_mileage
FROM
 endurance_mileage_gb
WHERE
 vin = 'LNAA2AA15K5001881'
 and start_time >= 1568304000000
 and end_time <= 1568563199000
ORDER BY run_mileage 



SELECT  sum(
        CAST(signal_start_value AS decimal(15, 6))
    )
FROM energy_consumption_motor_status
where  
signal_name = 'BrakingEnergyRecoveryPower';


SELECT  sum(
        CAST(signal_start_value AS double)
    ) as sum
FROM energy_consumption_motor_status
where  
signal_name = 'BrakingEnergyRecoveryPower';


insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729903000','LNAA2AA16K5004269','A26','2019-08-02 15:11:43','2019-08-02 15:11:48','MotorInputPower','0.006117156','0.006117156','12.00','-11.00','367.00','367.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729908000','LNAA2AA16K5004269','A26','2019-08-02 15:11:48','2019-08-02 15:11:53','BrakingEnergyRecoveryPower','0.005607393','0.005607393','-11.00','-10.00','367.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729913000','LNAA2AA16K5004269','A26','2019-08-02 15:11:53','2019-08-02 15:11:58','BrakingEnergyRecoveryPower','0.00508374','0.00508374','-10.00','26.00','366.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729918000','LNAA2AA16K5004269','A26','2019-08-02 15:11:58','2019-08-02 15:12:03','MotorInputPower','0.013217724','0.013217724','26.00','24.00','366.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729923000','LNAA2AA16K5004269','A26','2019-08-02 15:12:03','2019-08-02 15:12:08','MotorInputPower','0.012200976','0.012200976','24.00','-1.00','366.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729928000','LNAA2AA16K5004269','A26','2019-08-02 15:12:08','2019-08-02 15:12:13','BrakingEnergyRecoveryPower','0.000508374','0.000508374','-1.00','1.00','366.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729933000','LNAA2AA16K5004269','A26','2019-08-02 15:12:13','2019-08-02 15:12:18','MotorInputPower','0.000508374','0.000508374','1.00','-18.00','366.00','367.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729938000','LNAA2AA16K5004269','A26','2019-08-02 15:12:18','2019-08-02 15:12:23','BrakingEnergyRecoveryPower','0.009175734','0.009175734','-18.00','16.00','367.00','366.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729943000','LNAA2AA16K5004269','A26','2019-08-02 15:12:23','2019-08-02 15:12:28','MotorInputPower','0.008133984','0.008133984','16.00','-15.00','366.00','367.00');
insert into `energy_consumption_motor_status` (`id`, `vin`, `vehicle_series`, `start_time`, `end_time`, `signal_name`, `signal_start_value`, `signal_end_value`, `startI`, `endI`, `startU`, `endU`) values('LNAA2AA16K5004269_1564729948000','LNAA2AA16K5004269','A26','2019-08-02 15:12:28','2019-08-02 15:12:34','BrakingEnergyRecoveryPower','0.009176835','0.009176835','-15.00','-1.00','367.00','367.00');


CREATE external TABLE `dw_dim_vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vin` varchar(30) NOT NULL DEFAULT '' COMMENT 'VIN��',
  `mtoc` varchar(30) DEFAULT NULL COMMENT 'MTOC/MT�����ʹ���',
  `battery_type` varchar(20) DEFAULT NULL COMMENT '��ع�Ӧ��',
  `battery_count` varchar(20) DEFAULT NULL COMMENT '��ص�������',
  `vehicle_type` int(11) DEFAULT NULL COMMENT '���ͱ���',
  `vehicle_status` int(11) DEFAULT NULL COMMENT '״̬����',
  `user_type` int(11) DEFAULT NULL COMMENT '�û����Ա���',
  `create_date` datetime DEFAULT NULL COMMENT '��������',
  `inshop_date` timestamp NULL DEFAULT NULL COMMENT '����ʱ��',
  `sell_date` timestamp NULL DEFAULT NULL COMMENT '����ʱ��',
  `sell_address` varchar(200) DEFAULT '' COMMENT '���۵�ַ',
  `property` int(11) DEFAULT NULL COMMENT '�������Ա���',
  `store_address` varchar(200) DEFAULT '' COMMENT '�ֿ��ַ',
  `property_name` varchar(32) DEFAULT NULL COMMENT '��������',
  `user_type_name` varchar(32) DEFAULT NULL COMMENT '�û�����',
  `vin_type` varchar(32) DEFAULT NULL COMMENT '����',
  `vehicle_status_name` varchar(32) DEFAULT NULL COMMENT '����״̬',
  `dealer_code` varchar(255) DEFAULT NULL COMMENT '�����̱��',
  `dealer_name` varchar(255) DEFAULT NULL COMMENT '���۵���',
  `vehicle_config` varchar(50) DEFAULT '' COMMENT '��������',
  `vehicle_config_code` int(11) DEFAULT NULL COMMENT '�������ô���',
  `access_time` timestamp NULL DEFAULT NULL COMMENT '����ʱ��',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  UNIQUE KEY `dw_dim_vehicle_vin_IDX` (`vin`) USING BTREE
) row format delimited fields terminated by ',';


create   external  table  ext_msisdncsv

(

   msisdn string,

   tt   string

)

row format delimited fields terminated by ',';



compute stats;
analyze table;



SELECT start_time,end_time from energy_consumption_dcdc;

from_unixtime(int, 'yyyy/MM/dd HH:mm')
to_timestamp(bigint unixtime)

SELECT to_timestamp(start_time) as t,start_time,end_time from energy_consumption_dcdc limit 5;
from_unixtime(unix_timestamp( t1 ),'yyyyMMdd HH:mm')

SELECT from_unixtime(unix_timestamp(start_time),'yyyyMMdd HH:mm') as t,start_time,end_time from energy_consumption_dcdc limit 5;





/**
select to_timestamp('2018-03-08 18:55:33','yyyy-MM-dd hh24:mi:ss') from energy_consumption_dcdc;

select from_unixtime(to_unix_timestamp('16/Mar/2017:12:25:01 +0800', 'dd/MMM/yyy:HH:mm:ss Z'));
**/







select regexp_replace(substr(effc_start_dt,1,10),'-','') from tmp_lj_shenpi_shaixuan_0102
limit 5;



INSERT INTO dpp.energy_consumption_outside_temperature
(id, vin, vehicle_series, start_time, end_time, start_temperature, end_temperature)
VALUES(?, ?, ?, ?, ?, ?, ?);



show functions;


select from_timestamp(start_time,'yyyy/MM/dd') as d,count(*)
from energy_consumption_motor_status
group by from_timestamp(start_time,'yyyy/MM/dd')
order BY d ASC;


select concat(cast(year(first_buycar_time) as STRING),'���',cast(weekofyear(first_buycar_time) as string),'��') as w,count(*)
from energy_consumption_motor_status
group by concat(cast(year(first_buycar_time) as STRING),'���',cast(weekofyear(first_buycar_time) as string),'��')
order BY w ASC;



SELECT id, vin, vehicle_series, start_time, end_time, signal_name, signal_start_value, signal_end_value, starti, endi, startu, endu, `year`, `month`, `day`
FROM dpp.energy_consumption_motor_status;

select id,substr(vin,1,6) from energy_consumption_motor_status limit 10;

select id,substr(vin,1,9) from energy_consumption_motor_status limit 10;

select concat('hello','world') as concat;
select concat_ws('-','hello','world') as concat_ws;
select length('world') as len;



SELECT id, vin, mtoc, battery_type, battery_count, vehicle_type, vehicle_status, user_type, create_date, inshop_date, sell_date, sell_address, property, store_address, property_name, user_type_name, vin_type, vehicle_status_name, dealer_code, dealer_name, vehicle_config, vehicle_config_code, access_time, update_time, create_time, `year`, `month`, `day`
FROM dpp.dw_dim_vehicle limit 6;










