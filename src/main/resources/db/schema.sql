-- 创建数据库
CREATE DATABASE IF NOT EXISTS `template_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `template_db`;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `resource_type` VARCHAR(20) DEFAULT 'menu' COMMENT '资源类型：menu-菜单，button-按钮，api-接口',
    `resource_path` VARCHAR(255) DEFAULT NULL COMMENT '资源路径',
    `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 文件表
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `file_name` VARCHAR(100) NOT NULL COMMENT '文件名称（存储名称）',
    `original_file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名称',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件访问URL',
    `file_type` VARCHAR(20) DEFAULT NULL COMMENT '文件类型（扩展名）',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
    `file_md5` VARCHAR(32) DEFAULT NULL COMMENT '文件MD5值',
    `uploader_id` BIGINT DEFAULT NULL COMMENT '上传者ID',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '所属模块',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_file_type` (`file_type`),
    KEY `idx_uploader_id` (`uploader_id`),
    KEY `idx_module` (`module`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 初始化数据
-- 插入默认管理员用户（密码为：123456，使用BCrypt加密）
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `email`, `phone`, `status`) VALUES
('admin', '$10$k6o0X36xkZfJvQwJXPvZsuZHHHRhDguscEN61ykKbRk5egEyt3pFy', '系统管理员', 'admin@example.com', '13800138000', 1);

-- 插入默认角色
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`, `status`, `sort`) VALUES
('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1, 1),
('普通用户', 'USER', '普通用户，拥有基本权限', 1, 2);

-- 插入默认权限
INSERT INTO `sys_permission` (`permission_name`, `permission_code`, `resource_type`, `resource_path`, `method`, `parent_id`, `status`, `sort`) VALUES
('用户管理', 'user:manage', 'menu', '/user', NULL, 0, 1, 1),
('用户列表', 'user:list', 'api', '/api/users/page', 'GET', 1, 1, 1),
('用户详情', 'user:detail', 'api', '/api/users/*', 'GET', 1, 1, 2),
('新增用户', 'user:add', 'api', '/api/users', 'POST', 1, 1, 3),
('编辑用户', 'user:edit', 'api', '/api/users', 'PUT', 1, 1, 4),
('删除用户', 'user:delete', 'api', '/api/users/*', 'DELETE', 1, 1, 5),
('角色管理', 'role:manage', 'menu', '/role', NULL, 0, 1, 2),
('角色列表', 'role:list', 'api', '/api/roles/page', 'GET', 7, 1, 1);

-- 关联管理员用户和超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 关联超级管理员角色和所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8);

-- 关联普通用户角色和部分权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 2), (2, 3);
