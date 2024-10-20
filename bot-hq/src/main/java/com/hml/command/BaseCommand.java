package com.hml.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BaseCommand {

	SendPhoto sendPhoto(Long who, String what,Message mesage);
	SendMessage sendMessage(Long who, String what,Message mesage);
}
