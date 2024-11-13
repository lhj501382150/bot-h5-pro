/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2021/4/16 9:42:16                            */
/*==============================================================*/


drop table if exists TB_JY_BOT;
/*==============================================================*/
/* Table: TB_JY_BOT                                             */
/*==============================================================*/
create table TB_JY_BOT
(
   BOTID               bigint not null AUTO_INCREMENT comment '群号',
   TOKEN                varchar(64) not null comment '机器人TOKEN',
   BOTNAME                varchar(64) not null comment '机器人名称',
   KFURL               varchar(64) comment '客服链接',
   PLAYURL               varchar(64) comment '玩法说明',
   STATUS               varchar(1) comment '当前状态 Y 开启 N 停止',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (BOTID)
);

drop table if exists TB_JY_CHAT;
/*==============================================================*/
/* Table: TB_JY_CHAT                                            */
/*==============================================================*/
create table TB_JY_CHAT
(
   CHATID               varchar(64) not null comment '群号',
   CHATNAME             varchar(32) not null comment '群名称',
   USERNO		        varchar(32) comment '庄家ID',
   INTEGE								int(11) comment '金额',
   STATUS               varchar(1) comment '当前状态 Y 开启 N 停止',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (CHATID)
);

drop table if exists TB_JY_CHAT_USER;
/*==============================================================*/
/* Table: TB_JY_CHAT_USER                                            */
/*==============================================================*/
create table TB_JY_CHAT_USER
(
   CHATID               varchar(16) not null comment '群号',
   USERNO             varchar(32) not null comment '群名称',
   STATUS               varchar(1) comment '当前状态 Y 开启 N 停止',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (CHATID,USERNO)
);

INSERT INTO TB_SYS_MENU(MENU_ID,MENU_NAME,PARENT_ID,MENU_URL,PERMS,TYPE,ICON,ORDER_NO) VALUES('201007','上分','201000','','firm:member:addMoney',2,'',7);
INSERT INTO TB_SYS_MENU(MENU_ID,MENU_NAME,PARENT_ID,MENU_URL,PERMS,TYPE,ICON,ORDER_NO) VALUES('201008','下分','201000','','firm:member:subMoney',2,'',8);


DROP TABLE IF EXISTS `tb_jy_his_order`;
CREATE TABLE `tb_jy_his_order`  (
  `ORDERNO` bigint(0) NOT NULL,
  `FDATE` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `WARENO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `MODE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '模式 0 普通 1竞价4盲盒',
  `ARTID` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '资产编号',
  `OPENFLAT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'A',
  `BUYORSAL` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'S',
  `PRICE` decimal(15, 2) NULL DEFAULT 0.00,
  `FLATPRICE` decimal(15, 2) NULL DEFAULT 0.00,
  `NUM` int(0) NULL DEFAULT 0,
  `CONTNUM` int(0) NULL DEFAULT 0,
  `COMM` decimal(15, 2) NULL DEFAULT NULL,
  `BAILMONEY` decimal(15, 2) NULL DEFAULT 0.00,
  `CPRIGHT` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '版权费',
  `TRANSCOMM` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '物流费',
  `LOSS` decimal(15, 2) NULL DEFAULT 0.00,
  `STATUS` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '0未成交1已成交2部分成交3已撤单',
  `ORDTIME` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT 'yyyy-MM-dd HH:mm:ss',
  `CONTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '',
  `RECVNO` bigint(0) NULL DEFAULT 0,
  `EXT` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '扩展信息如资产属性',
  `LASTUPTIME` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`ORDERNO`) USING BTREE,
  INDEX `date_user_idx`(`FDATE`, `USERNO`) USING BTREE
);

DROP TABLE IF EXISTS `tb_zj_his_usermoney`;
CREATE TABLE `tb_zj_his_usermoney` (
  `FDATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '结算日期',
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '客户编号',
  `ACCTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账户编号',
  `START` decimal(15,2) DEFAULT '0.00' COMMENT '期初',
  `INMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '入金',
  `OUTMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '出金',
  `IN` decimal(15,2) DEFAULT NULL,
  `OUT` decimal(15,2) DEFAULT NULL,
  `INCOME` decimal(15,2) DEFAULT '0.00' COMMENT '收入',
  `PAYMENT` decimal(15,2) DEFAULT '0.00' COMMENT '支出',
  `BALANCE` decimal(15,2) DEFAULT '0.00' COMMENT '期末',
  `FREEZE` decimal(15,2) DEFAULT '0.00' COMMENT '冻结',
  `LOSE` decimal(15,2) DEFAULT '0.00' COMMENT '盈亏',
  `ENABLE` decimal(15,2) DEFAULT '0.00' COMMENT '可用',
  `ALLQUITY` decimal(15,2) DEFAULT '0.00' COMMENT '权益',
  `LIMTOUT` decimal(15,2) DEFAULT '0.00' COMMENT '限出',
  `CANOUT` decimal(15,2) DEFAULT '0.00' COMMENT '可出',
  PRIMARY KEY (`FDATE`,`USERNO`,`ACCTNO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `tb_zj_his_usermoney_change`;
CREATE TABLE `tb_zj_his_usermoney_change` (
  `WARTNO` bigint NOT NULL COMMENT '流水号',
  `FDATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '结算日期',
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户编号',
  `ACCTNO` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '账本编号',
  `SUBNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '科目编号',
  `CHMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '变动资金',
  `BEFMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '变动前金额',
  `AFTMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '变动后金额',
  `CONTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '订单编号',
  `PAYTYPE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'A' COMMENT 'A本地支付B三方支付',
  `PAYSTATUS` varchar(3) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '100' COMMENT '100初始101开始支付102已发送三方103等待反馈200支付成功300支付失败',
  `CTIME` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `MEMO` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `EXTEN` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`WARTNO`) USING BTREE,
  KEY `index_fdate_userno` (`FDATE`,`USERNO`) USING BTREE
) ;


DROP TABLE IF EXISTS tb_hy_user_limit;
CREATE TABLE tb_hy_user_limit (
  USERNO varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  COMMENT '用户编号',
  MIN_DOU int DEFAULT 0 COMMENT '',
  MAX_DOU int DEFAULT 0 COMMENT '',
  MIN_NIU_P int DEFAULT 0 COMMENT '',
  MAX_NIU_P int DEFAULT 0 COMMENT '',
  MIN_NIU_B int DEFAULT 0 COMMENT '',
  MAX_NIU_B int DEFAULT 0 COMMENT '',
  PRIMARY KEY (USERNO) USING BTREE
 ) ;
 
DROP TABLE IF EXISTS tb_data_pre_code;
CREATE TABLE tb_data_pre_code (
  ID  bigint not null AUTO_INCREMENT comment '流水号',
  RKEY varchar(16) DEFAULT 0 COMMENT 'REDIS KEY',
  MODE int DEFAULT 0 COMMENT '模式',
  DATA_ID int DEFAULT 0 COMMENT '',
  DRAW_ISSUE varchar(32)  DEFAULT '' COMMENT '期数',
  CODE  varchar(32) DEFAULT '' COMMENT '结果',
  MEMO varchar(128) DEFAULT '' COMMENT '',
  CREATE_USER varchar(32) DEFAULT '' COMMENT '',
  UPDATE_USER varchar(32) DEFAULT '' COMMENT '',
  CREATE_TIME timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
  UPDATE_TIME timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
  PRIMARY KEY (ID) USING BTREE
 ) ;