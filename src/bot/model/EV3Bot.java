package bot.model;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class EV3Bot
{
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor backTouch;
	private float [] ultrasonicSamples;
	private float [] touchSamples;


	public EV3Bot()
	{
		this.botMessage = "Billy coes billyBot";
		this.xPosition = 50;
		this.yPosition = 50;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		backTouch = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
		
		setupPilot();
		
		displayMessage();
		
	}

	private void setupPilot()
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 43.3).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 43.3).offset(72);
		WheeledChassis chassis = new WheeledChassis(new Wheel[] {leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}
	
	public void driveRoom()
	{
		ultrasonicSamples = new float[distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		if(ultrasonicSamples[0] < 1400) //The 2.5 is not a real number. Figure out a better number
		{
			//Front door
			displayMessage("Short Drive");
			botPilot.travel(1200);
			botPilot.rotate(60);
			botPilot.travel(3600);
			botPilot.rotate(-60);
			botPilot.travel(6600);
			botPilot.rotate(60);
			botPilot.travel(4800);
		}
		else
		{
			//Back door
			displayMessage("Long Drive");
			botPilot.travel(4800);
			botPilot.rotate(-60); //left
			botPilot.travel(6600);
			botPilot.rotate(60); //right
			botPilot.travel(3600);
			botPilot.rotate(-60);
			botPilot.travel(1200);
		}
		
		//call private helper method here
		displayMessage("driveRoom");
	}
	
	public void driveFree()
	{
		ultrasonicSamples = new float[distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		botPilot.travel(100000);
		if(ultrasonicSamples[0] < 500)
		{
			botPilot.stop();
			botPilot.travel(-1000);
			botPilot.rotate(60);
			botPilot.travel(100000);
		}
	}
	
	private void displayMessage()
	{
		LCD.drawString(botMessage,  xPosition,  yPosition);
		Delay.msDelay(waitTime);
	}
	
	private void displayMessage(String message)
	{
		LCD.drawString(message,  xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	

}

