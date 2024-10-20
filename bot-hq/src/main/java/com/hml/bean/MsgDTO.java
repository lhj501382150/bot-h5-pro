package com.hml.bean;

import lombok.Data;

@Data
public class MsgDTO {

	private String command;
	
	private Integer money;
	
	private String what;
	
	private String num;
	
	private boolean isCommand;
	
	public MsgDTO(String what,boolean isCommand) {
		 this.what = what;
		 this.isCommand = isCommand;
	}

	public MsgDTO(String command, Integer money) {
		this.command = command;
		this.money = money;
		this.setCommand(parseCommand(command));
		this.setNum(CommandTextParser.textToNum(command));
	}
	
	private boolean parseCommand(String text) {
		String[] paras = {"抢庄","下庄"};
		boolean flag = false;
		for(String str : paras) {
			if(text.indexOf(str) > -1) {
				flag = true;
				break;
			}else {
				flag = CommandTextParser.checkCommand(str);
				if(flag = true) {
					break;
				}
			}
		}
		return flag;
	}


	public boolean isCommand() {
		return isCommand;
	}


	public void setCommand(boolean isCommand) {
		this.isCommand = isCommand;
	}

	
}
