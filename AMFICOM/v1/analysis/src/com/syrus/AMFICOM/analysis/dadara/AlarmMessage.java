package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.General.UI.MessageBox;

public class AlarmMessage
{
	public static final int softCut = 1;
	public static final int softShift = 2;
	public static final int softWholeShift = 3;
	public static final int hardCut = 10;
	public static final int hardShift = 11;
	public static final int hardWholeShift = 12;
	private int alarmType = 0;

	public static final String weld = "weld";
	public static final String linear = "linear";
	public static final String connector = "connector";

	private String referenceEventType;
	private String message = "��������! �� ������ �����������!";

	private ReflectogramAlarm ra;
	private double deltaX;
	private double dE;

//----------------------------------
	public AlarmMessage(double deltaX, ReflectogramAlarm ra)
	{
		this.ra = ra;
		this.deltaX = deltaX;

		dE = (int)(ra.refAmplChangeValue*1000)/1000.;
		if(ra.getEventType() == ReflectogramEvent.WELD)
			referenceEventType = "���������������";
		else if(ra.getEventType() == ReflectogramEvent.CONNECTOR)
			referenceEventType = "�������������";
		else if(ra.getEventType() == ReflectogramEvent.LINEAR)
			referenceEventType = "�������� �������";
		else
			referenceEventType = "���������� ���";

		// shift of the whole reflectogramm.
		if(ra.alarmPointCoord == ra.leftReflectoEventCoord &&
			 ra.leftReflectoEventCoord == 0 &&
			 ra.alarmEndPointCoord == ra.rightReflectoEventCoord)
		{
			if(ra.level == 0)
				alarmType = softWholeShift;
			else
				alarmType = hardWholeShift;
		}
		else
		{
			if(dE < 0 && ra.level == 1)
				alarmType = hardCut;
			else if(dE < 0 && ra.level == 0)
				alarmType = softCut;
			else if(dE > 0 && ra.level == 1)
				alarmType = hardShift;
			else if(dE > 0 && ra.level == 0)
				alarmType = softShift;
		}

		if(alarmType == softCut)
			message = getSoftCutMessage();
		else if(alarmType == hardCut)
			message = getHardCutMessage();
		else if(alarmType == softShift)
			message = getSoftShiftMessage();
		else if(alarmType == hardShift)
			message = getHardShiftMessage();
		else if(alarmType == softWholeShift)
			message = getSoftWholeShiftAlarmMessage();
		else if(alarmType == hardWholeShift)
			message = getHardWholeShiftAlarmMessage();
		else
			message = getEmptyAlarmMessage();
	}
/*
//----------------------------------
	public AlarmMessage(int coord, double deltaX, int alarmType, double dE, String referenceEventType)
	{
		this(((double)coord)*deltaX, alarmType, dE, referenceEventType);
	}

/*
//----------------------------------
	public AlarmMessage(double coord, int alarmType, double dE, String referenceEventType)
	{
		this.coord = (int)(coord*1000)/1000.;
		this.dE = (int)(dE*1000)/1000.;
		this.alarmType = alarmType;

		if(referenceEventType.equals(weld))
			referenceEventType = "���������������";
		else if(referenceEventType.equals(connector))
			referenceEventType = "�������������";
		else if(referenceEventType.equals(linear))
			referenceEventType = "�������� �������";
		else
			referenceEventType = "���������� ���";

		if(alarmType == softCut)
			message = getSoftCutMessage();
		else if(alarmType == hardCut)
			message = getHardCutMessage();
		else if(alarmType == softShift)
			message = getSoftShiftMessage();
		else if(alarmType == hardShift)
			message = getHardShiftMessage();
		else
			message = getEmptyAlarmMessage();
	}

*/
//----------------------------------
	private String getSoftCutMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������! \n";
		s = s+"���������� ��������������: "+ (int)(ra.alarmPointCoord*deltaX)/1000. + " ��. \n";
		s = s+"���������� �� �������: "+ dE +" ��. \n";
		s = s+"��� �������: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" ��; " + ra.rightReflectoEventCoord * deltaX / 1000. + " �� \n";
			s = s+"���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * deltaX / 1000. + " �� \n";
		}
		s = s+"�������� ��������������: ��������� ������. \n";
		s = s+"������: ���������� �� ��������� ��������� ����������� ��������. \n";
		s = s+"������������: 1. ��������� �� ����������� �������������������� �������.";
		s = s+"              2. ������ ����� ������ ������� �� �������.";
		return s;
	}

//----------------------------------
	private String getHardCutMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n";
		s = s+"���������� �����������: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " ��. \n";
		s = s+"���������� �� �������: " + dE + " ��. \n";
		s = s+"��� �������: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" ��; " + ra.rightReflectoEventCoord * deltaX / 1000. + " �� \n";
			s = s+"���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * deltaX / 1000. + " �� \n";
		}
		s = s+"�������� ����������: ����� ��� ������������ ��������� ������. \n";
		s = s+"������: ���������� ��������� ��������� ����������� ��������.\n";
		s = s+"������������: 1. ��������� ����������� ������. \n";
		s = s+"              2. ������ ����� ��������� ��������������.";

		return s;
	}

//----------------------------------
	private String getSoftShiftMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������! \n";
		s = s+"���������� ��������������: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " ��. \n";
		s = s+"���������� �� �������: "+ dE + " ��.\n";
		s = s+"��� �������: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" ��; " + ra.rightReflectoEventCoord * deltaX / 1000. + " �� \n";
			s = s+"���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * deltaX / 1000. + " �� \n";
		}
		s = s+"�������� ��������������: ��������� ��������. \n";
		s = s+"������: ���������� �� ��������� ��������� ����������� ��������.\n";
		s = s+"������������: ������ ����� ������ ������� �� �������.";
		return s;
	}

//----------------------------------
	private String getHardShiftMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n";
		s = s+"���������� �����������: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " ��. \n";
		s = s+"���������� �� �������: " + dE + " ��.\n";
		s = s+"��� �������: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" ��; " + ra.rightReflectoEventCoord * deltaX / 1000. + " �� \n";
			s = s+"���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * deltaX / 1000. + " �� \n";
		}
		s = s+"�������� �����������: ������������ ��������� �������� \n";
		s = s+"������: ���������� ��������� ��������� ���������� ��������.\n";
		s = s+"������������: ������ ����� ��������� ��������������";
		return s;
	}

//----------------------------------
	private String getEmptyAlarmMessage()
	{
		String s = "��������! �� ������� ������������ �������� �����������. \n";
		s = s+"���������� ��������������: " +(int)(ra.alarmPointCoord*deltaX)/1000. + " ��. \n";
		s = s+"���������� �� �������: " + dE + " ��. \n";
		s = s+"��� �������: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" ��; " + ra.rightReflectoEventCoord * deltaX / 1000. + " �� \n";
			s = s+"���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * deltaX / 1000. + " �� \n";
		}
		s = s+"�������� ���������� �����������: ����������. \n";
		s = s+"������: ��� �������� � ����������� ���������� �������������� ����������. \n";
		s = s+"������������: �������� ��������� ��� ������ �������� ����������.";

		return s;
	}


//----------------------------------
	private String getHardWholeShiftAlarmMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n";
		s = s+"���������� �� �������: " + dE + " ��.\n";
		s = s+"�������� ���������� �����������: ������� ���� ���������. \n";
		s = s+"������: ���������� ��������� ��������� ���������� ��������. \n";
		s = s+"������������: 1. ��������� ���� ���������. \n";
		s = s+"              2. ������ ����� ��������� ��������������.";
		return s;
	}


//----------------------------------
	private String getSoftWholeShiftAlarmMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������!\n";
		s = s+"���������� �� �������: " + dE + " ��.\n";
		s = s+"�������� ���������� �����������: ������� ������� ��������� ���������. \n";
		s = s+"������: ���������� �� ��������� ��������� ����������� �������� \n";
		s = s+"������������: 1. ������ ����� ������ �������. \n";
		s = s+"              2. ������ ����� ��������� ��������������.";
		return s;
	}

//----------------------------------
	public String getAlarmMessage()
	{
		return message;
	}

	public int getAlarmType()
	{
		return alarmType;
	}

//----------------------------------
	public void showAlarmMessage(int x, int y)
	{
		MessageBox mb = new MessageBox(this.message, x, y);
		mb.setVisible(true);
	}

//----------------------------------
	public void showAlarmMessage()
	{
		MessageBox mb = new MessageBox(this.message);
		mb.setVisible(true);
	}
}