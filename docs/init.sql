-- 平台初始化数据

-- 初始化门店 store
INSERT INTO `freelycar_saas`.`store`(`id`, `address`, `closingTime`, `createTime`, `delStatus`, `headUrl`, `latitude`, `linkman`, `longitude`, `name`, `openingTime`, `phone`, `remark`, `sort`) VALUES ('1', '南京市苏宁青创园', '18:00:00', CURRENT_TIMESTAMP, b'0', 'https://jiantuku-image-tangtang233.oss-cn-hangzhou.aliyuncs.com/18-10-30/35952693.jpg', 32.087114, '测试员', 118.891374, '测试门店', '09:00:00', '1736741688', '备注测试', 10);

-- 初始化超级管理员&管理员 sysUser
INSERT INTO `freelycar_saas`.`sysuser`(`id`, `createTime`, `delStatus`, `password`, `permissions`, `phone`, `remark`, `staffName`, `storeId`, `username`) VALUES (1, CURRENT_TIMESTAMP, b'0', 'sysadmin', NULL, NULL, '初始化生成', '超级管理员', NULL, 'sysadmin');
INSERT INTO `freelycar_saas`.`sysuser`(`id`, `createTime`, `delStatus`, `password`, `permissions`, `phone`, `remark`, `staffName`, `storeId`, `username`) VALUES (2, CURRENT_TIMESTAMP, b'0', 'admin', NULL, NULL, '初始化生成', '测试管理员', '1', 'admin');


-- 初始化基本角色（暂时没有用到） sysRole

INSERT INTO `freelycar_saas`.`sysrole`(`id`, `name`) VALUES (1, 'SUPERADMIN');

INSERT INTO `freelycar_saas`.`sysrole`(`id`, `name`) VALUES (2, 'ADMIN');

INSERT INTO `freelycar_saas`.`sysrole`(`id`, `name`) VALUES (3, 'USER');


-- 注：自行执行车牌信息的初始化接口方法

