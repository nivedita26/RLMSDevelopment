CREATE TABLE `rlms_event` (
  `event_id` int(11) NOT NULL AUTO_INCREMENT,
  `event_type` varchar(45) DEFAULT NULL,
  `event_description` varchar(45) DEFAULT NULL,
  `lift_customer_map_id` int(11) DEFAULT NULL,
  `generated_date` timestamp NULL DEFAULT NULL,
  `generated_by` varchar(45) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `active_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `FK_CUSTO_LIFTMAP_EVENT` (`lift_customer_map_id`),
  CONSTRAINT `FK_CUSTO_LIFTMAP_EVENT` FOREIGN KEY (`lift_customer_map_id`) REFERENCES `rlms_lift_customer_map` (`lift_customer_map_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


INSERT INTO `rlms_db`.`rlms_event`
(`event_type`,
`event_description`,
`lift_customer_map_id`,
`generated_date`,
`generated_by`,
`updated_date`,
`updated_by`,
`active_flag`)
VALUES
("In","In Event ocurred",1,now(),1,now(),1,1);

INSERT INTO `rlms_db`.`rlms_event`
(`event_type`,
`event_description`,
`lift_customer_map_id`,
`generated_date`,
`generated_by`,
`updated_date`,
`updated_by`,
`active_flag`)
VALUES
("Out","Out Event ocurred",1,now(),1,now(),1,1);

ALTER TABLE rlms_db.rlms_complaint_master
ADD call_type int(11);

update rlms_db.rlms_complaint_master set call_type=0;