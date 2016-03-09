package bot.controller;

import bot.model.EV3Bot;

public class BotController
{
	private EV3Bot billyBot;
	
	private BotController()
	{
		billyBot = new EV3Bot();
	}
	
	public void start()
	{
		billyBot.driveRoom();
	}
}
