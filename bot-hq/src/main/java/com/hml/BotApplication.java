package com.hml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.hml.bot.BaseBot;
import com.hml.config.BotConfig;
import com.hml.thread.GetDataThread;
import com.hml.thread.Hash1Thread;
import com.hml.thread.Hash3Thread;
import com.hml.thread.Hash5Thread;
import com.hml.thread.NiuGetDataThread;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class BotApplication {

	public static void main(String[] args) {
		try {
			ApplicationContext applicationContext = SpringApplication.run(BotApplication.class, args);
			
			BaseBot bot = applicationContext.getBean(BaseBot.class);
			System.out.println("Token:" + bot.getBotToken());
			System.out.println("Username:" + bot.getBotUsername());
			
			if(BotConfig.ENABLE){
				TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
				botsApi.registerBot(bot);
			}
		    
		    new GetDataThread().start();
		    new NiuGetDataThread().start();
		    new Hash1Thread().start();
		    new Hash3Thread().start();
		    new Hash5Thread().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
	}

}

