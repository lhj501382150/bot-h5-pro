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
    		width:700px;
    	}
    	table, td{
    		border: 1px solid gray;
    		text-align:left;
    		font-size: 18px;
    		font-weight:600;
    		font-family: simsun;
    	}
    	table{
    		width:100%;
    	}
    	tr{
    		height:36px;	
    	} 
    	.head th{
    		background-color: #000000;
		    color: #ffffff;
		    font-size:24px;
		    font-weight:900;
		    font-family: simsun;
		    text-align:center;
    	} 
    </style>
</head>
<body>
<div class="bgDiv">
	<table border="1" cellspacing="0" cellpadding="0">
		<tr class="head">
			<th width="19%">玩家昵称</th>
			<th width="14%">积分</th>
			<th width="19%">玩家昵称</th>
			<th width="14%">积分</th>
			<th width="19%">玩家昵称</th>
			<th width="15%">积分</th>
		</tr>
		 <#list dataList as data>
			<#if (data?index + 1) % 3 == 1>
				<tr> 
			</#if>
				<td>${data?index + 1}、${data.userName !''}</td>
				<td>${data.dEnable ! ''}</td>
			<#if data?index != 0 && (data?index + 1) % 3 == 0>
				</tr> 
			</#if>
	    </#list>
	    <#if dataList?size % 3 == 1>
			<td></td>
			<td></td>
			</tr> 
		<#elseif dataList?size % 3 == 2>
			<td></td>
			</tr> 
		</#if>
	    <tr class="head">
	    	<th colspan="2" width="40%">
	    		总积分:${data.sumInteg !''}
	    	</th>
	    	<th colspan="4" width="60%" style="text-align:left;padding-left:10px;">
	    		时 间:${data.sCurTime !''}
	    	</th>
	    </tr>
	</table>
     
</div>
</body>
</html>