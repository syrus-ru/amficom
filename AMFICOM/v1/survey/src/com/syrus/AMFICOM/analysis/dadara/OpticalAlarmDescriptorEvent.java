package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPathDecompositor;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Port;

import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmDescriptorEvent;

public class OpticalAlarmDescriptorEvent extends AlarmDescriptorEvent
{
	public static final int softCut = 1;
	public static final int softShift = 2;
	public static final int softWholeShift = 3;
	public static final int hardCut = 10;
	public static final int hardShift = 11;
	public static final int hardWholeShift = 12;
	int alarmType = 0;

	public static final String weld = "weld";
	public static final String linear = "linear";
	public static final String connector = "connector";

	private String referenceEventType;
	private String message = "��������! �� ������ �����������!";

	public ReflectogramAlarm ra;
	public double delta_x;
	public double dE;

//----------------------------------
	public OpticalAlarmDescriptorEvent(String me_id, double delta_x, ReflectogramAlarm ra)
	{
		super(me_id, "", "");
		this.ra = ra;
		this.delta_x = delta_x;

		dE = (int )(ra.refAmplChangeValue * 1000) / 1000.;
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

		if (alarmType < 10)
		{
			super.title = "��������������";
			super.severity = super.WARNING;
		}
		else
		{
			super.title = "�������";
			super.severity = super.ALARM;
		}
		super.text = message;
	}
/*
//----------------------------------
	public AlarmMessage(int coord, double delta_x, int alarmType, double dE, String referenceEventType)
	{
		this(((double)coord)*delta_x, alarmType, dE, referenceEventType);
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

	public String getLink(double d)
	{
		double meters = d * 1000.;
		ObjectResource or = (ObjectResource )getPathDecompositor().getLinkByOpticalDistance(meters);
		return (or == null) ? "" : or.getName();
	}

	public String getLinkID(double d)
	{
		double meters = d * 1000.;
		ObjectResource or = (ObjectResource )getPathDecompositor().getLinkByOpticalDistance(meters);
		return (or == null) ? "" : or.getId();
	}

	public String getEquipment(double d)
	{
/*
		double meters = d * 1000.;
		ObjectResource or = (ObjectResource )getPathDecompositor().getSourcePortByOpticalDistance(meters);
		if(or == null)
			return "";
		Equipment eq;
		if(or instanceof CablePort)
		{
			CablePort port = (CablePort )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		else
		{
			Port port = (Port )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		return eq.getName();
*/
		double meters = d * 1000.;
		ObjectResource or = (ObjectResource )getPathDecompositor().getEquipmentByOpticalDistance(meters);
		return (or == null) ? "" : or.getName();
	}

	public String getNearestEvent(double d)
	{
		double meters = d * 1000.;
		ObjectResource or;
		if(isLeftNearest())
			or = (ObjectResource )getPathDecompositor().getSourcePortByOpticalDistance(meters);
		else
			or = (ObjectResource )getPathDecompositor().getTargetPortByOpticalDistance(meters);
		if(or == null)
			return "";
		Equipment eq;
		if(or instanceof CablePort)
		{
			CablePort port = (CablePort )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		else
		{
			Port port = (Port )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		return eq.getName();
	}

	public String getOtherEvent(double d)
	{
		double meters = d * 1000.;
		ObjectResource or;
		if(isLeftNearest())
			or = (ObjectResource )getPathDecompositor().getTargetPortByOpticalDistance(meters);
		else
			or = (ObjectResource )getPathDecompositor().getSourcePortByOpticalDistance(meters);
		if(or == null)
			return "";
		Equipment eq;
		if(or instanceof CablePort)
		{
			CablePort port = (CablePort )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		else
		{
			Port port = (Port )or;
			eq = (Equipment )Pool.get("kisequipment", port.equipment_id);
		}
		return eq.getName();
	}

	public double getEventCoord()
	{
		return (ra.alarmPointCoord * delta_x) / 1000.;
	}

	public double getNearestEventDistance(double d)
	{
		double meters = d * 1000.;
		double dist = 0.0;
		if(isLeftNearest())
			dist = getPathDecompositor().getSourcePortDistanceByOpticalDistance(meters);
		else
			dist = getPathDecompositor().getTargetPortDistanceByOpticalDistance(meters);
		return dist / 1000.;
	}

	public double getOtherEventDistance(double d)
	{
		double meters = d * 1000.;
		double dist = 0.0;
		if(isLeftNearest())
			dist = getPathDecompositor().getTargetPortDistanceByOpticalDistance(meters);
		else
			dist = getPathDecompositor().getSourcePortDistanceByOpticalDistance(meters);
		return dist / 1000.;
	}

	public double getNearestEventDistance()
	{
		double d = 0.0;
		if(isLeftNearest())
			d = ra.leftReflectoEventCoord - ra.alarmPointCoord;
		else
			d = ra.rightReflectoEventCoord - ra.alarmPointCoord;
		if(d < 0)
			d = -d;
		return (d * delta_x) / 1000.;
//		return (int )(ra.nearestReflectoEventDistance * delta_x) / 1000.;
	}

	public double getOtherEventDistance()
	{
		double d = 0.0;
		if(isLeftNearest())
			d = ra.rightReflectoEventCoord - ra.alarmPointCoord;
		else
			d = ra.leftReflectoEventCoord - ra.alarmPointCoord;
		if(d < 0)
			d = -d;
		return (d * delta_x) / 1000.;
	}

	public double getNearestEventCoord()
	{
		if(isLeftNearest())
			return (ra.leftReflectoEventCoord * delta_x) / 1000.;
		else
			return (ra.rightReflectoEventCoord * delta_x) / 1000.;
	}

	public double getOtherEventCoord()
	{
		if(isLeftNearest())
			return (ra.rightReflectoEventCoord * delta_x) / 1000.;
		else
			return (ra.leftReflectoEventCoord * delta_x) / 1000.;
	}

	public boolean isLeftNearest()
	{
		double d1 = ra.rightReflectoEventCoord - ra.alarmPointCoord;
		if(d1 < 0)
			d1 = -d1;
		double d2 = ra.leftReflectoEventCoord - ra.alarmPointCoord;
		if(d2 < 0)
			d2 = -d2;
		return (d2 < d1);
	}

	public String getDistanceDescription()
	{
		double d0 = getEventCoord();
		double d5 = getNearestEventDistance(d0);
		double d6 = getOtherEventDistance(d0);
		TransmissionPathDecompositor tpd = getPathDecompositor();
		String s =
				"������� ��������� �� ������� '" + getLink(d0) + "' ����� ������������:\n" +
				"'" + getNearestEvent(d0) + "' (��������� �� ���������� - " + ((int )(d5 * 1000.)) / 1000. + " ��),\n" +
				"'" + getOtherEvent(d0) + "' (��������� �� ���������� - " + ((int )(d6 * 1000)) / 1000. + " ��).\n";
		return s;
	}

	public String getEventsDescription()
	{
		double d1 = getNearestEventCoord();
		double d2 = getOtherEventCoord();
		double d3 = getNearestEventDistance();
		double d4 = getOtherEventDistance();
		TransmissionPathDecompositor tpd = getPathDecompositor();
		String s = "���������� �� ���������� �������������� ������� '" + getEquipment(d1) + "' - " + ((int )(d3 * 1000)) / 1000. + " �� \n" +
				"���������� �� ���������� �������������� ������� � ������ ������� '" + getEquipment(d2) + "' - " + ((int )(d4 * 1000)) / 1000. + " �� \n";
		return s;
	}

	public String getDistanceDescription2()
	{
		String s = "";
		s = s + "���������� ��������� ������������� �������: " + ra.leftReflectoEventCoord * delta_x / 1000. +
				" ��; " + ra.rightReflectoEventCoord * delta_x / 1000. + " �� \n";
		s = s + "���������� �� ���������� �������������� ������� " + ra.nearestReflectoEventDistance * delta_x / 1000. + " �� \n";
		return s;
	}

	private String getSoftCutMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������! \n" +
				"���������� ��������������: " + (int)(ra.alarmPointCoord * delta_x) / 1000. + " ��. \n" +
				"���������� �� �������: " + dE + " ��. \n" +
				"��� �������: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "�������� ��������������: ��������� ������. \n" +
				"������: ���������� �� ��������� ��������� ����������� ��������. \n" +
				"������������: 1. ��������� �� ����������� �������������������� �������." +
				"              2. ������ ����� ������ ������� �� �������.";
		return s;
	}

//----------------------------------
	private String getHardCutMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n" +
				"���������� �����������: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " ��. \n" +
				"���������� �� �������: " + dE + " ��. \n" +
				"��� �������: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "�������� ����������: ����� ��� ������������ ��������� ������. \n" +
				"������: ���������� ��������� ��������� ��������� ��������.\n" +
				"������������: 1. ��������� ����������� ������. \n" +
				"              2. ������ ����� ��������� ��������������.";

		return s;
	}

//----------------------------------
	private String getSoftShiftMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������! \n" +
				"���������� ��������������: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " ��. \n" +
				"���������� �� �������: " + dE + " ��.\n" +
				"��� �������: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "�������� ��������������: ��������� ��������. \n" +
				"������: ���������� �� ��������� ��������� ����������� ��������.\n"+
				"������������: ������ ����� ������ ������� �� �������.";
		return s;
	}

//----------------------------------
	private String getHardShiftMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n" +
				"���������� �����������: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " ��. \n" +
				"���������� �� �������: " + dE + " ��.\n" +
				"��� �������: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "�������� �����������: ������������ ��������� �������� \n" +
				"������: ���������� ��������� ��������� ���������� ��������.\n" +
				"������������: ������ ����� ��������� ��������������";
		return s;
	}

//----------------------------------
	private String getEmptyAlarmMessage()
	{
		String s = "��������! �� ������� ������������ �������� �����������. \n" +
				"���������� ��������������: " +(int )(ra.alarmPointCoord * delta_x) / 1000. + " ��. \n" +
				"���������� �� �������: " + dE + " ��. \n" +
				"��� �������: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "�������� ���������� �����������: ����������. \n" +
				"������: ��� �������� � ����������� ���������� �������������� ����������. \n" +
				"������������: �������� ��������� ��� ������ �������� ����������.";

		return s;
	}


//----------------------------------
	private String getHardWholeShiftAlarmMessage()
	{
		String s = "��������! �� ������� ������������ ���������� �����������! \n" +
				"���������� �� �������: " + dE + " ��.\n" +
				"�������� ���������� �����������: ������� ���� ���������. \n" +
				"������: ���������� ��������� ��������� ���������� ��������. \n" +
				"������������: 1. ��������� ���� ���������. \n" +
				"              2. ������ ����� ��������� ��������������.";
		return s;
	}


//----------------------------------
	private String getSoftWholeShiftAlarmMessage()
	{
		String s = "��������! �� ������� ������������ ���������� ��������������!\n" +
				"���������� �� �������: " + dE + " ��.\n" +
				"�������� ���������� �����������: ������� ������� ��������� ���������. \n" +
				"������: ���������� �� ��������� ��������� ����������� �������� \n" +
				"������������: 1. ������ ����� ������ �������. \n" +
				"              2. ������ ����� ��������� ��������������.";
		return s;
	}
}