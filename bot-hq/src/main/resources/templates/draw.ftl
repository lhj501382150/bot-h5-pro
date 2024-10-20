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
    		width:480px;
    	}
    	table, th, td{
    		border: 1px solid #e2e2e2;
    		text-align:center;
    		font-size: 18px;
    		font-weight:800;
    		font-family: SimHei;
    	}
    	.head{
    		font-size: 42px;
    		font-weight:900;
    		font-family: simhei;
    	}
    	table, th{
    		border: 1px solid #e2e2e2;
    		text-align:center;
    		font-weight:900;
    		font-size: 20px;
    		font-family: sans-serif;
    	}
    	table{
    		width:100%;
    	}
    	tr{
    		height:36px;	
    	}
    </style>
</head>
<body>
<div class="bgDiv">
	<table border="1" cellspacing="0" cellpadding="0">
		<tr>
			<th colspan="4" class="head">
				开始投弹
			</th>
		</tr>
		<tr style="background-color:#ef43f3;color:#000000;">
			<th width="25%">庄家昵称</th>
			<th width="25%">庄家积分</th>
			<th width="25%">当前局数</th>
			<th width="25%">庄家上局</th>
		</tr>
		<tr>
			<td>${data.userName!''}</td>
			<td>${data.integRal!''}</td>
			<td>${data.iCount!''}</td>
			<td>${data.dBef!''}</td>
		</tr>
		<tr style="background-color:#ef43f3;color:#000000;">
			<th colspan="4">
				限注范围
			</th>
		</tr>
		<tr>
			<td colspan="4">
				${data.min!''}-${data.max!''}
			</td>
		</tr>
	</table>
     
</div>
</body>
</html>