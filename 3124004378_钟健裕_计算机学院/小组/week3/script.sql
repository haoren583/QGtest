create table tb_appointment
(
    record_id   bigint                                     not null comment '预约ID'
        primary key,
    user_id     bigint           default 0                 not null comment '用户ID',
    doctor_id   bigint           default 0                 not null comment '医生ID',
    slot_id     bigint           default 0                 not null comment '预约时段',
    status      tinyint unsigned default '0'               not null comment '预约状态：0-待确认 1-已完成 2-已取消',
    create_time datetime         default CURRENT_TIMESTAMP not null,
    is_del      tinyint          default 0                 not null comment '是否删除',
    constraint idx_user_doctor
        unique (user_id, doctor_id)
)
    comment '预约记录表' collate = utf8mb4_0900_ai_ci
                         row_format = DYNAMIC;

create table tb_appointment_slot
(
    slot_id          bigint                                     not null comment '时段ID'
        primary key,
    doctor_id        bigint           default 0 not null comment '关联医生',
    date date null comment '预约日期',
    time_range varchar(16) default ''                           not null comment '可用时间段',
    max_capacity     int unsigned     default '30'              not null comment '最大预约人数',
    current_capacity int unsigned     default '30'              not null comment '当前剩余容量',
    status           tinyint unsigned default '1'               not null comment '时段状态：0-已满 1-可预约',
    create_time      datetime         default CURRENT_TIMESTAMP not null,
    is_del           tinyint          default 0                 not null comment '是否删除',
    constraint idx_unique_slot
        unique (doctor_id, date)
)
    comment '医生可预约时段表' collate = utf8mb4_0900_ai_ci
                               row_format = DYNAMIC;

create table tb_department
(
    dept_id     bigint                                 not null comment '科室ID'
        primary key,
    dept_name   varchar(50)  default ''                not null comment '科室名称',
    description varchar(512) default ''                not null comment '科室描述',
    create_time datetime     default CURRENT_TIMESTAMP not null,
    is_del      tinyint      default 0                 not null comment '是否删除'
)
    comment '科室信息表' collate = utf8mb4_0900_ai_ci
                         row_format = DYNAMIC;

create index idx_dept_name
    on tb_department (dept_name);

create table tb_doctor
(
    doctor_id   bigint auto_increment comment '医生ID'
        primary key,
    doctor_name varchar(20)  default ''                not null comment '医生姓名',
    dept_id     int unsigned default '0'               not null comment '所属科室',
    title       varchar(64)  default ''                not null comment '职称',
    job_intro   varchar(64)  default ''                not null comment '医生简介',
    schedule    varchar(512) default ''                not null comment '每周排班',
    status      int          default 0                 not null,
    create_time datetime     default CURRENT_TIMESTAMP not null,
    is_del      tinyint      default 0                 not null comment '是否删除'
)
    comment '医生信息表' collate = utf8mb4_0900_ai_ci
                         row_format = DYNAMIC;

create index idx_doctor_name
    on tb_doctor (doctor_name);

create table tb_evaluation
(
    eval_id      bigint                                     not null comment '评价ID'
        primary key,
    user_id      bigint           default 0                 not null comment '用户ID',
    doctor_id    bigint           default 0                 not null comment '医生ID',
    dept_id      bigint           default 0                 not null comment '科室ID',
    rating       tinyint unsigned default '5'               not null comment '评分（1-5星）',
    comment      varchar(512)                               null comment '评价内容',
    audit_status tinyint unsigned default '0'               null comment '审核状态',
    create_time  datetime         default CURRENT_TIMESTAMP null,
    is_del       tinyint          default 0                 not null comment '是否删除'
)
    comment '评价表' collate = utf8mb4_0900_ai_ci
                     row_format = DYNAMIC;

create index tb_evaluation_doctor_id_index
    on tb_evaluation (doctor_id);

create table tb_user
(
    user_id     bigint                                not null comment '用户ID'
        primary key,
    card_id     varchar(32) default ''                not null comment '学号或工号',
    password    varchar(32) default ''                not null comment '加密密码',
    phone       varchar(20)                           not null comment '联系电话',
    user_name   varchar(20) default ''                not null comment '真实姓名',
    status      int         default 0                 not null comment '认证状态：1-待认证 2-已认证  10-医生待认证  20-医生已认证',
    role        varchar(32) default 'Common'          not null comment '用户角色',
    create_time datetime    default CURRENT_TIMESTAMP null,
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_del      tinyint     default 0                 not null comment '是否删除',
    gender      varchar(16) default 'UNKNOWN'         not null comment '性别'
)
    comment '用户信息表' collate = utf8mb4_0900_ai_ci
                         row_format = DYNAMIC;

create index tb_user_phone_index
    on tb_user (phone);

create index tb_user_status_index
    on tb_user (status);


