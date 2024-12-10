
drop table if exists TB_KJ_DRAW;
/*==============================================================*/
/* Table: TB_KJ_DRAW                                             */
/*==============================================================*/
create table TB_KJ_DRAW
(
   DATA_ID              bigint not null comment '流水号',
   MODE                 varchar(8)   not null comment '类型',
   PRE_DRAW_DATE        varchar(16) default '' comment '日期',
   PRE_DRAW_TIME        varchar(32) default '' comment '上期开奖时间',
   DRAW_TIME        varchar(32) default '' comment '开奖时间',
   PRE_DRAW_ISSUE        varchar(32) default '' comment '上期期数',
   DRAW_ISSUE        varchar(32) default '' comment '当前期数',
   PRE_DRAW_CODE     varchar(64) default ''  comment '上期开奖结果',
   PRE_DRAW_HASH         varchar(128) default '' comment '上期开奖结果',
   VIDEO_URL         varchar(256) default '' comment '视频路径',
   TITLE         varchar(256) default '' comment '标题',
   MEMO      text default '' comment '标题',
   CREATE_TIME          timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (DATA_ID,MODE)
);

drop table if exists TB_KJ_DRAW_TYPE;
/*==============================================================*/
/* Table: TB_KJ_DRAW_TYPE                                             */
/*==============================================================*/
create table TB_KJ_DRAW_TYPE
(
   MODE                 varchar(8)   not null comment '类型',
   MODE_NAME        varchar(16) default '' comment '名称',
   TIME        int default 1 comment '间隔时间',
   MODE_URL        varchar(256) default '' comment '访问地址',
   TYPE         varchar(1) default '' comment '类型 A 外部 B 内部',
   SEQ         int default 0 ,
   primary key (MODE)
);

INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('A3','加拿大28',3,'https://pc28660.com','A',5);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('A1','澳洲幸运10',5,'https://api.api168168.com/pks/getPksDoubleCount.do?date=&lotCode=10012','A',20);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('A2','极速赛车',1,'https://api.api168168.com/pks/getPksDoubleCount.do?date=&lotCode=10037','A',21);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('B1','哈希1分赛车',1,'http://bot.hx0000.com:8000/eth','B',10);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('B2','哈希3分赛车',3,'http://bot.hx0000.com:8000/eth','B',11);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('B3','哈希5分赛车',5,'http://bot.hx0000.com:8000/eth','B',12);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('C1','澳门六合彩',5,'https://cc.a6tk46.com/#/recommend','C',30);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('C2','新澳门六合彩',5,'https://cc.a6tk46.com/#/recommend','C',31);
INSERT INTO TB_KJ_DRAW_TYPE(MODE,MODE_NAME,TIME,MODE_URL,TYPE,SEQ)VALUES('C3','香港六合彩',5,'https://cc.a6tk46.com/#/recommend','C',32);
