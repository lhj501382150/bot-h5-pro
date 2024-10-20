package com.hml.task;

public enum Flow {

	START_ROB(1),CONFIRM_ROB(2),DOWN_ORDER(3),STOP_ORDER(4),OVER(5),TIPS(6);
	
	Integer step;
	
	Flow(Integer step){
		this.step = step;
	}
	public Integer getStep(){
		return step;
	}
}
