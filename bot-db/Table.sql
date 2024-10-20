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
   BOTID               bigint not null AUTO_INCREMENT comment 'Ⱥ��',
   TOKEN                varchar(64) not null comment '������TOKEN',
   BOTNAME                varchar(64) not null comment '����������',
   KFURL               varchar(64) comment '�ͷ�����',
   PLAYURL               varchar(64) comment '�淨˵��',
   STATUS               varchar(1) comment '��ǰ״̬ Y ���� N ֹͣ',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '������ʱ��',
   primary key (BOTID)
);

drop table if exists TB_JY_CHAT;
/*==============================================================*/
/* Table: TB_JY_CHAT                                            */
/*==============================================================*/
create table TB_JY_CHAT
(
   CHATID               varchar(64) not null comment 'Ⱥ��',
   CHATNAME             varchar(32) not null comment 'Ⱥ����',
   USERNO		        varchar(32) comment 'ׯ��ID',
   INTEGE								int(11) comment '���',
   STATUS               varchar(1) comment '��ǰ״̬ Y ���� N ֹͣ',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '������ʱ��',
   primary key (CHATID)
);

drop table if exists TB_JY_CHAT_USER;
/*==============================================================*/
/* Table: TB_JY_CHAT_USER                                            */
/*==============================================================*/
create table TB_JY_CHAT_USER
(
   CHATID               varchar(16) not null comment 'Ⱥ��',
   USERNO             varchar(32) not null comment 'Ⱥ����',
   STATUS               varchar(1) comment '��ǰ״̬ Y ���� N ֹͣ',
   LAST_UPDATE          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '������ʱ��',
   primary key (CHATID,USERNO)
);

INSERT INTO TB_SYS_MENU(MENU_ID,MENU_NAME,PARENT_ID,MENU_URL,PERMS,TYPE,ICON,ORDER_NO) VALUES('201007','�Ϸ�','201000','','firm:member:addMoney',2,'',7);
INSERT INTO TB_SYS_MENU(MENU_ID,MENU_NAME,PARENT_ID,MENU_URL,PERMS,TYPE,ICON,ORDER_NO) VALUES('201008','�·�','201000','','firm:member:subMoney',2,'',8);


DROP TABLE IF EXISTS `tb_jy_his_order`;
CREATE TABLE `tb_jy_his_order`  (
  `ORDERNO` bigint(0) NOT NULL,
  `FDATE` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `WARENO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `MODE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT 'ģʽ 0 ��ͨ 1����4ä��',
  `ARTID` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '�ʲ����',
  `OPENFLAT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'A',
  `BUYORSAL` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'S',
  `PRICE` decimal(15, 2) NULL DEFAULT 0.00,
  `FLATPRICE` decimal(15, 2) NULL DEFAULT 0.00,
  `NUM` int(0) NULL DEFAULT 0,
  `CONTNUM` int(0) NULL DEFAULT 0,
  `COMM` decimal(15, 2) NULL DEFAULT NULL,
  `BAILMONEY` decimal(15, 2) NULL DEFAULT 0.00,
  `CPRIGHT` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '��Ȩ��',
  `TRANSCOMM` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '������',
  `LOSS` decimal(15, 2) NULL DEFAULT 0.00,
  `STATUS` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '0δ�ɽ�1�ѳɽ�2���ֳɽ�3�ѳ���',
  `ORDTIME` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT 'yyyy-MM-dd HH:mm:ss',
  `CONTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '',
  `RECVNO` bigint(0) NULL DEFAULT 0,
  `EXT` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '��չ��Ϣ���ʲ�����',
  `LASTUPTIME` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`ORDERNO`) USING BTREE,
  INDEX `date_user_idx`(`FDATE`, `USERNO`) USING BTREE
);

DROP TABLE IF EXISTS `tb_zj_his_usermoney`;
CREATE TABLE `tb_zj_his_usermoney` (
  `FDATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '��������',
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '�ͻ����',
  `ACCTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '�˻����',
  `START` decimal(15,2) DEFAULT '0.00' COMMENT '�ڳ�',
  `INMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '���',
  `OUTMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '����',
  `IN` decimal(15,2) DEFAULT NULL,
  `OUT` decimal(15,2) DEFAULT NULL,
  `INCOME` decimal(15,2) DEFAULT '0.00' COMMENT '����',
  `PAYMENT` decimal(15,2) DEFAULT '0.00' COMMENT '֧��',
  `BALANCE` decimal(15,2) DEFAULT '0.00' COMMENT '��ĩ',
  `FREEZE` decimal(15,2) DEFAULT '0.00' COMMENT '����',
  `LOSE` decimal(15,2) DEFAULT '0.00' COMMENT 'ӯ��',
  `ENABLE` decimal(15,2) DEFAULT '0.00' COMMENT '����',
  `ALLQUITY` decimal(15,2) DEFAULT '0.00' COMMENT 'Ȩ��',
  `LIMTOUT` decimal(15,2) DEFAULT '0.00' COMMENT '�޳�',
  `CANOUT` decimal(15,2) DEFAULT '0.00' COMMENT '�ɳ�',
  PRIMARY KEY (`FDATE`,`USERNO`,`ACCTNO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `tb_zj_his_usermoney_change`;
CREATE TABLE `tb_zj_his_usermoney_change` (
  `WARTNO` bigint NOT NULL COMMENT '��ˮ��',
  `FDATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '��������',
  `USERNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '�û����',
  `ACCTNO` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '�˱����',
  `SUBNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '��Ŀ���',
  `CHMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '�䶯�ʽ�',
  `BEFMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '�䶯ǰ���',
  `AFTMONEY` decimal(15,2) DEFAULT '0.00' COMMENT '�䶯����',
  `CONTNO` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '�������',
  `PAYTYPE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'A' COMMENT 'A����֧��B����֧��',
  `PAYSTATUS` varchar(3) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '100' COMMENT '100��ʼ101��ʼ֧��102�ѷ�������103�ȴ�����200֧���ɹ�300֧��ʧ��',
  `CTIME` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `MEMO` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '��ע',
  `EXTEN` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '��չ��Ϣ',
  PRIMARY KEY (`WARTNO`) USING BTREE,
  KEY `index_fdate_userno` (`FDATE`,`USERNO`) USING BTREE
) ;


DROP TABLE IF EXISTS tb_hy_user_limit;
CREATE TABLE tb_hy_user_limit (
  USERNO varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  COMMENT '�û����',
  MIN_DOU int DEFAULT 0 COMMENT '',
  MAX_DOU int DEFAULT 0 COMMENT '',
  MIN_NIU_P int DEFAULT 0 COMMENT '',
  MAX_NIU_P int DEFAULT 0 COMMENT '',
  MIN_NIU_B int DEFAULT 0 COMMENT '',
  MAX_NIU_B int DEFAULT 0 COMMENT '',
  PRIMARY KEY (USERNO) USING BTREE
 ) ;