CREATE DATABASE IF NOT EXISTS lab_equipment_maintenance
    DEFAULT CHARACTER SET utf8mb4;

USE lab_equipment_maintenance;
create table if not exists work_order(
    id int auto_increment primary key,
    device_id int not null,
    campus_id int not null,
    reporter_id int not null,
    worker_id int,
    location varchar(255) not null,
    fault_description varchar(1000) not null,
    status varchar(32) not null
)engine=InnoDB;

create table if not exists outbox_event(
    id bigint auto_increment primary key,
    work_order_id int not null,
    event_type varchar(32) not null,
    send_status varchar(16) not null default 'PENDING'
)engine=InnoDB;

create table if not exists work_order_completion_log(
    event_id bigint primary key,
    work_order_id int not null
)engine=InnoDB;