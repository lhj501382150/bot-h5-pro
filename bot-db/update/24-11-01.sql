INSERT INTO `tb_sys_para`(`SYSID`, `SYSTYPE`, `GROUPNAME`, `SYSNAME`, `SVAL`, `MEMO`, `ISVALID`, `LASTUPTIME`) VALUES (30, '1', '客户端配置', '行情网站地址', 'http://18.212.105.77:8000/eth', NULL, '', '2023-12-25 21:19:22');



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