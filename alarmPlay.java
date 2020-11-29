package example;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;


public class alarmPlay extends JFrame {

	//�˶� ������ ArrayList
	static ArrayList <clock> AlarmList = new ArrayList <clock> ();
	//���� ����
	int year, month, day, hour, min;
	alarmAlert ar;
	Container c;
	static boolean alarmExist = false;
	
	//�ӽÿ� �˶� ���� ����â	
	alarmPlay() {

		RingAlarm ra = new RingAlarm();
		
		c = getContentPane();
		setTitle("Alarm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
		ra.start();
	}
	
	class RingAlarm extends Thread {
		public void run() {
			while(true) {
				//����� �˶��� ������ ���
				if(!alarmExist) {
					continue;
				}
				//����� �˶��� ������ ����
				else if(alarmExist) {
					try {
						year = AlarmList.get(0).year;
						month = AlarmList.get(0).month;
						day = AlarmList.get(0).day;
						hour = AlarmList.get(0).hour;
						min = AlarmList.get(0).min;
										
						//����� �ð����� ���
						sleep(timeUntil(year, month, day, hour, min));				
						//�۾��� ����
						ring();					
						
					} catch (InterruptedException e) {}	
					
					AlarmList.remove(0);
					if(AlarmList.isEmpty())
						alarmExist = false;
				}
			}
		}
	}
	
	public void ring(){
		ar = new alarmAlert(this, "Alert!");
		ar.setVisible(true);
	}
	
	//�˶� ���� ���
	public static void AlarmSound(String fn) {
		try {
			FileInputStream file = new FileInputStream(new File(fn));
			Player player = new Player(file);
			player.play();
		} catch (Exception e) {
		}	
	}
	
	// ����� �ð����� ���� �ð� ���
	public long timeUntil(int year, int month, int day, int hour, int min){
		Date now = new Date();
		Calendar calUntil = Calendar.getInstance();
		calUntil.set(Calendar.YEAR, year);
		calUntil.set(Calendar.MONTH, month - 1);
		calUntil.set(Calendar.DAY_OF_WEEK, day);
		calUntil.set(Calendar.HOUR_OF_DAY, hour);
		calUntil.set(Calendar.MINUTE, min);
		calUntil.set(Calendar.SECOND, 0);
		Date until = calUntil.getTime();
		long sleep = until.getTime() - now.getTime();
		return sleep;
	}
	
	//�ð��� �� ������ ������� �˾�â
	public class alarmAlert extends JDialog {
		public alarmAlert(JFrame frame, String title) {
			super(frame, title);
			setSize(200, 100);
			setVisible(true);
			AlarmSound("alarmSound.wav");
			//â �����鼭 ���� ������ ��ư �߰��ؾߵ�
		}		
	}
	
	//�˶� �߰�
	public static void AddAlarm(int year, int month, int day, int hour, int min) {		
		boolean isAdd = false;
		if(AlarmList.size() == 0) {
			AlarmList.add(new clock(year, month, day, hour, min));
			isAdd = true;
			alarmExist = true;
		}
		else if(AlarmList.size() == 10) {
			//��� �˾�â �߰�
			System.out.println("�˶��� ������ 10���� ���� �� �����ϴ�.");
		}
		else if(AlarmList.size() > 0 && AlarmList.size() < 11) {
			for(int i = 0; i < AlarmList.size(); i++) {
				if(month < AlarmList.get(i).month) {
					AlarmList.add(i, new clock(year, month, day, hour, min));
					isAdd = true;
					alarmExist = true;
					break;
				}
				else if(month == AlarmList.get(i).month) {
					if(day < AlarmList.get(i).day) {
						AlarmList.add(i, new clock(year, month, day, hour, min));
						isAdd = true;
						alarmExist = true;
						break;
					}
					else if(day == AlarmList.get(i).day) {
						if(hour < AlarmList.get(i).hour) {
							AlarmList.add(i, new clock(year, month, day, hour, min));
							isAdd = true;
							alarmExist = true;
							break;
						}
						else if(hour == AlarmList.get(i).hour) {
							if(min < AlarmList.get(i).min) {
								AlarmList.add(i, new clock(year, month, day, hour, min));
								isAdd = true;
								alarmExist = true;
								break;
							}
							else if(min == AlarmList.get(i).min) {
								//��� �˾�â �߰�
								System.out.println("�̹� ��ϵ� �˶��Դϴ�.");
								isAdd = true;
								break;
							}
						}
					}
				}
				
				if(i == AlarmList.size() - 1 && isAdd == false) {
					AlarmList.add(i + 1, new clock(year, month, day, hour, min));
					isAdd = true;
					alarmExist = true;
					break;
				}					
			}
		}		
	}
		
	//�˶� ����(�ʿ� ���� : �ش� �˶��� ���� ArrayList�� index)
	public static void ModAlarm(int index, int year, int month, int day, int hour, int min) {
		AlarmList.remove(index - 1);
		AddAlarm(year, month, day, hour, min);
	}
	
	//�˶� ����(�ʿ� ���� : �ش� �˶��� ���� ArrayList�� index)
	public static void DelAlarm(int index) {
		AlarmList.remove(index - 1);
	}
}


/*
������ ��ũ
https://ismydream.tistory.com/151
https://www.youtube.com/watch?v=oFs7FPpf5-w
http://www.javazoom.net/javalayer/javalayer.html

���� ��ó
https://www.mewpot.com/
*/