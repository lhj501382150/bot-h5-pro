<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8"></meta>
    <title></title>
    <style type="text/css">
    	*{
	    	padding:0;
	    	margin:0;
	    }
    	.bgDiv{
    		width:1080px;
    	}
    	table, th, td{
    		border: 1px solid #e2e2e2;
    		text-align:center;
    		font-size: 22px;
    		font-weight:600;
    		font-family: simsun;
    	}
    	table{
    		width:100%;
    	}
    	tr{
    		height:36px;	
    	}
    	.bgcolor{
    		background-color:#95d3ef
    	}
    	.head_tr th{
    		color:#ffffff;
    		font-weight:900;
    		font-size:28px;
    	}
    </style>
</head>
<body>
<div class="bgDiv">
	<table border="1" cellspacing="0" cellpadding="0">
		<tr style="background-color:#02a7f0;" class="head_tr">
			<th width="14%">投注人数</th>
			<th width="14%">投注总额</th>
			<th width="14%">开奖号码</th>
			<th width="16%">庄家昵称</th>
			<th width="14%">庄家盈亏</th>
			<th width="14%">庄家剩余</th>
			<th width="14%">庄家吃赔</th>
		</tr>
		<tr>
			<td>${data.count!''}</td>
			<td>${data.total!''}</td>
			<td>${data.drawNo!''}</td>
			<td>${data.userName!''}</td>
			<td>${data.winInteg!''}</td>
			<td>${data.integRal!''}</td>
			<td>吃:${data.iWinNum!''} 赔:${data.iLossNum!''}</td>
		</tr>
	</table>
     <table border="1" cellspacing="0" cellpadding="0">
		<tr style="background-color:#42a507;" class="head_tr">
			<th width="8%">ID</th>
			<th width="15%">昵称</th>
			<th width="12%">注单</th>
			<th width="13%">金额</th>
			<th width="13%">倍数</th>
			<th width="13%">上局积分</th>
			<th width="13%">本局输赢</th>
			<th width="13%">结束金额</th>
		</tr>
		<#list dataList as data>
			<#if data?index % 2 == 0>
				<tr>
			<#else>
				<tr class="bgcolor">
			</#if>
				<td>${data.seq !''}</td>
				<td>${data.userName!''}</td>
				<td>${data.RulId!''}</td>
				<td>${data.integRal!''}</td>
				<td>${data.fRate!''}</td>
				<td>${data.dBef!''}</td>
				<td>
					<#if data.winLose == 1>
					    <span>${data.winInteg!''}</span>
					<#else>
					    <span style="color:red;">${data.winInteg!''}</span>
					</#if>
				</td>
				<td>${data.dEnd!''}</td>
			</tr>
	    </#list>
	</table>
</div>
</body>
</html>