<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8"></meta>
    <title></title>
    <style type="text/css">
    	*{
    		margin:0;
    		padding:0;
    	}
    	.bgDiv{
    		width:630px;
    		padding:0;
    		margin:0;
    	}
    	.head_title{
    		font-weight:1000;
    		font-size:28px;
    	}
    	.head{
    		width: 630px;
		    height: 36px;
		    line-height: 36px;
		    background-color: #000000;
		    color: #ffffff;
		    margin:0px;
    	}
    	.name{
    		text-align: center;
    		width:130px;
    		margin:0;
    		display: inline-block;
    		font-weight:500;
    		font-size:18px;
    		border-bottom:1px solid  gray;
    	}
    	.score{
    		text-align: center;
    		width:70px;
    		margin:0;
    		display: inline-block;
    		font-weight:500;
    		font-size:18px;
    		border-bottom:1px solid  gray;
    	}
    	.data{
    		width: 100%;
		    line-height: 28px; 
		    min-height:228px;
		    
    	}
    	.foot{
    		width: 100%;
		    height: 36px;
		    line-height: 36px;
		    background-color: #000000;
		    color: #ffffff;
    	}
    	.bl{
    		border-left:1px solid  gray; 
    	}
    	.br{
    		border-right:1px solid  gray; 
    	}
    </style>
</head>
<body>
<div class="bgDiv">
		<div class="head">
			<div class="name head_title">玩家昵称</div>
			<div class="score bl head_title">积分</div>
			<div class="name bl head_title">玩家昵称</div>
			<div class="score bl head_title">积分</div>
			<div class="name bl ">玩家昵称</div>
			<div class="score bl head_title">积分</div>
		</div>
		<div class="data">
			<#list dataList as item>
		         <div class="name bl">${item_index + 1 !''}、${item.userName !''}</div>
		         <div class="score bl">${item.dEnable !''}</div>
	    	</#list> 
		</div>
     <div class="foot">
			<div style="width:250px;display: inline-block;">总积分:${data.sumInteg !''}</div>
			<div style="width:310px;display: inline-block;">时 间:${data.sCurTime !''}</div>
		</div>
</div>
</body>
</html>