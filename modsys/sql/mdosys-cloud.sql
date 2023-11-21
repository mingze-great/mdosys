# 建立项目实体表
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name`        varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'project name',
    `code`        bigint(20) NOT NULL COMMENT 'encoding',
    `description` text COLLATE utf8mb4_general_ci         DEFAULT NULL,
    `user_id`     bigint(20)                              DEFAULT NULL COMMENT 'creator id',
    `del_flag`    tinyint(4)                              DEFAULT '1' COMMENT '0 not available, 1 available',
    `create_time` datetime   NOT NULL                     DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime   NOT NULL                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`, `code`),
    KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

# 项目和用户之间对应的关系
DROP TABLE if EXISTS `sys_relation_project_user`;
CREATE TABLE `sys_relation_project_user`
(
    `id`          int      NOT NULL AUTO_INCREMENT COMMENT 'key',
    `user_id`     int      NOT NULL COMMENT 'user id',
    `project_id`  int               DEFAULT NULL COMMENT 'project id',
    `permission`  int               DEFAULT '7' COMMENT 'limits of authority',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


# 建立流程定义实体表
DROP TABLE if EXISTS `sys_process_definition`;
CREATE TABLE `sys_process_definition`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `code`             bigint(20) NOT NULL COMMENT 'primary encoding',
    `name`             varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'process definition name',
    `version`          int                                     DEFAULT '0' COMMENT 'process definition version',
    `description`      text COLLATE utf8mb4_general_ci COMMENT 'description',
    `project_code`     bigint     NOT NULL COMMENT 'project code',
    `release_state`    tinyint                                 DEFAULT NULL COMMENT 'process definition release state：0:offline,1:online',
    `user_id`          int                                     DEFAULT NULL COMMENT 'process definition creator id',
    `global_params`    text COMMENT 'global parameters',
    `del_flag`         tinyint                                 DEFAULT NULL COMMENT '0 not available, 1 available',
    `locations`        text COMMENT 'Node location information',
    `warning_group_id` int                                     DEFAULT NULL COMMENT 'alert group id',
    `timeout`          int                                     DEFAULT '0' COMMENT 'time out, unit: minute',
    `tenant_id`        int        NOT NULL                     DEFAULT '-1' COMMENT 'tenant id',
    `execution_type`   tinyint                                 DEFAULT '0' COMMENT 'execution_type 0:parallel,1:serial wait,2:serial discard,3:serial priority',
    `create_time`      datetime   NOT NULL                     DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time`      datetime   NOT NULL                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`, `code`),
    UNIQUE KEY `process_unique` (`name`, `project_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;



