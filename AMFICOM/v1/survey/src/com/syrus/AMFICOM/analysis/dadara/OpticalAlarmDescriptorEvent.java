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
	private String message = "Внимание! На трассе повреждение!";

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
			referenceEventType = "неотражательное";
		else if(ra.getEventType() == ReflectogramEvent.CONNECTOR)
			referenceEventType = "отражательное";
		else if(ra.getEventType() == ReflectogramEvent.LINEAR)
			referenceEventType = "линейный участок";
		else
			referenceEventType = "информации нет";

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
			super.title = "Предупреждение";
			super.severity = super.WARNING;
		}
		else
		{
			super.title = "Тревога";
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
			referenceEventType = "неотражательное";
		else if(referenceEventType.equals(connector))
			referenceEventType = "отражательное";
		else if(referenceEventType.equals(linear))
			referenceEventType = "линейный участок";
		else
			referenceEventType = "информации нет";

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
				"Событие произошло на участке '" + getLink(d0) + "' между устройствами:\n" +
				"'" + getNearestEvent(d0) + "' (дистанция до устройства - " + ((int )(d5 * 1000.)) / 1000. + " км),\n" +
				"'" + getOtherEvent(d0) + "' (дистанция до устройства - " + ((int )(d6 * 1000)) / 1000. + " км).\n";
		return s;
	}

	public String getEventsDescription()
	{
		double d1 = getNearestEventCoord();
		double d2 = getOtherEventCoord();
		double d3 = getNearestEventDistance();
		double d4 = getOtherEventDistance();
		TransmissionPathDecompositor tpd = getPathDecompositor();
		String s = "Расстояние до ближайшего отражательного события '" + getEquipment(d1) + "' - " + ((int )(d3 * 1000)) / 1000. + " км \n" +
				"Расстояние до ближайшего отражательного события с другой стороны '" + getEquipment(d2) + "' - " + ((int )(d4 * 1000)) / 1000. + " км \n";
		return s;
	}

	public String getDistanceDescription2()
	{
		String s = "";
		s = s + "Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * delta_x / 1000. +
				" км; " + ra.rightReflectoEventCoord * delta_x / 1000. + " км \n";
		s = s + "Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * delta_x / 1000. + " км \n";
		return s;
	}

	private String getSoftCutMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие! \n" +
				"Координата несоответствия: " + (int)(ra.alarmPointCoord * delta_x) / 1000. + " км. \n" +
				"Отклонение от эталона: " + dE + " дБ. \n" +
				"Тип события: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "Характер несоответствия: локальные потери. \n" +
				"Резюме: Отклонение не превышает предельно допустимого значения. \n" +
				"Рекомендации: 1. Проверить на возможность несанкционированного доступа." +
				"              2. Задать новые уровни порогов на событие.";
		return s;
	}

//----------------------------------
	private String getHardCutMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n" +
				"Координата повреждения: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " км. \n" +
				"Отклонение от эталона: " + dE + " дБ. \n" +
				"Тип события: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "Характер повреждния: обрыв или значительные локальные потери. \n" +
				"Резюме: Отклонение превышает предельно допустиме значение.\n" +
				"Рекомендации: 1. Проверить целостность кабеля. \n" +
				"              2. Ввести новую эталонную рефлектограмму.";

		return s;
	}

//----------------------------------
	private String getSoftShiftMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие! \n" +
				"Координата несоответствия: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " км. \n" +
				"Отклонение от эталона: " + dE + " дБ.\n" +
				"Тип события: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "Характер несоответствия: локальное усиление. \n" +
				"Резюме: Отклонение не превышает предельно допустимого значения.\n"+
				"Рекомендации: Задать новые уровни порогов на событие.";
		return s;
	}

//----------------------------------
	private String getHardShiftMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n" +
				"Координата повреждения: "+(int )(ra.alarmPointCoord * delta_x) / 1000. + " км. \n" +
				"Отклонение от эталона: " + dE + " дБ.\n" +
				"Тип события: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "Характер повреждения: значительное локальное усиление \n" +
				"Резюме: Отклонение превышает предельно допустимое значение.\n" +
				"Рекомендации: Ввести новую эталонную рефлектограмму";
		return s;
	}

//----------------------------------
	private String getEmptyAlarmMessage()
	{
		String s = "Внимание! На участке тестирования возможно повреждение. \n" +
				"Координата несоответствия: " +(int )(ra.alarmPointCoord * delta_x) / 1000. + " км. \n" +
				"Отклонение от эталона: " + dE + " дБ. \n" +
				"Тип события: " + referenceEventType + "\n";
		s = s + getDistanceDescription();
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s + getEventsDescription();
		}
		s = s + "Характер возможного повреждения: неизвестен. \n" +
				"Резюме: Для суждения о повреждении необходима дополнительная информация. \n" +
				"Рекомендации: Провести измерения при других тестовых параметрах.";

		return s;
	}


//----------------------------------
	private String getHardWholeShiftAlarmMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n" +
				"Отклонение от эталона: " + dE + " дБ.\n" +
				"Характер возможного повреждения: нарушен ввод излучения. \n" +
				"Резюме: Отклонение превышает предельно допустимое значение. \n" +
				"Рекомендации: 1. Проверить ввод излучения. \n" +
				"              2. Ввести новую эталонную рефлектограмму.";
		return s;
	}


//----------------------------------
	private String getSoftWholeShiftAlarmMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие!\n" +
				"Отклонение от эталона: " + dE + " дБ.\n" +
				"Характер возможного повреждения: изменен уровень вводимого излучения. \n" +
				"Резюме: Отклонение не превышает предельно допустимого значения \n" +
				"Рекомендации: 1. Задать новые уровни порогов. \n" +
				"              2. Ввести новую эталонную рефлектограмму.";
		return s;
	}
}