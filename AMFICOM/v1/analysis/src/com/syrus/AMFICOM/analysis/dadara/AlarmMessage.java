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
	private String message = "Внимание! На трассе повреждение!";

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
	private String getSoftCutMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие! \n";
		s = s+"Координата несоответствия: "+ (int)(ra.alarmPointCoord*deltaX)/1000. + " км. \n";
		s = s+"Отклонение от эталона: "+ dE +" дБ. \n";
		s = s+"Тип события: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" км; " + ra.rightReflectoEventCoord * deltaX / 1000. + " км \n";
			s = s+"Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * deltaX / 1000. + " км \n";
		}
		s = s+"Характер несоответствия: локальные потери. \n";
		s = s+"Резюме: Отклонение не превышает предельно допустимого значения. \n";
		s = s+"Рекомендации: 1. Проверить на возможность несанкционированного доступа.";
		s = s+"              2. Задать новые уровни порогов на событие.";
		return s;
	}

//----------------------------------
	private String getHardCutMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n";
		s = s+"Координата повреждения: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " км. \n";
		s = s+"Отклонение от эталона: " + dE + " дБ. \n";
		s = s+"Тип события: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" км; " + ra.rightReflectoEventCoord * deltaX / 1000. + " км \n";
			s = s+"Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * deltaX / 1000. + " км \n";
		}
		s = s+"Характер повреждния: обрыв или значительные локальные потери. \n";
		s = s+"Резюме: Отклонение превышает предельно допустимого значения.\n";
		s = s+"Рекомендации: 1. Проверить целостность кабеля. \n";
		s = s+"              2. Ввести новую эталонную рефлектограмму.";

		return s;
	}

//----------------------------------
	private String getSoftShiftMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие! \n";
		s = s+"Координата несоответствия: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " км. \n";
		s = s+"Отклонение от эталона: "+ dE + " дБ.\n";
		s = s+"Тип события: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" км; " + ra.rightReflectoEventCoord * deltaX / 1000. + " км \n";
			s = s+"Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * deltaX / 1000. + " км \n";
		}
		s = s+"Характер несоответствия: локальное усиление. \n";
		s = s+"Резюме: Отклонение не превышает предельно допустимого значения.\n";
		s = s+"Рекомендации: Задать новые уровни порогов на событие.";
		return s;
	}

//----------------------------------
	private String getHardShiftMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n";
		s = s+"Координата повреждения: "+(int)(ra.alarmPointCoord*deltaX)/1000. + " км. \n";
		s = s+"Отклонение от эталона: " + dE + " дБ.\n";
		s = s+"Тип события: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" км; " + ra.rightReflectoEventCoord * deltaX / 1000. + " км \n";
			s = s+"Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * deltaX / 1000. + " км \n";
		}
		s = s+"Характер повреждения: значительное локальное усиление \n";
		s = s+"Резюме: Отклонение превышает предельно допустимое значение.\n";
		s = s+"Рекомендации: Ввести новую эталонную рефлектограмму";
		return s;
	}

//----------------------------------
	private String getEmptyAlarmMessage()
	{
		String s = "Внимание! На участке тестирования возможно повреждение. \n";
		s = s+"Координата несоответствия: " +(int)(ra.alarmPointCoord*deltaX)/1000. + " км. \n";
		s = s+"Отклонение от эталона: " + dE + " дБ. \n";
		s = s+"Тип события: " + referenceEventType + "\n";
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
		{
			s = s+"Координаты ближайших отражательных событий: " + ra.leftReflectoEventCoord * deltaX / 1000. +
					" км; " + ra.rightReflectoEventCoord * deltaX / 1000. + " км \n";
			s = s+"Расстояние до ближайщего отражательного события " + ra.nearestReflectoEventDistance * deltaX / 1000. + " км \n";
		}
		s = s+"Характер возможного повреждения: неизвестен. \n";
		s = s+"Резюме: Для суждения о повреждении необходима дополнительная информация. \n";
		s = s+"Рекомендации: Провести измерения при других тестовых параметрах.";

		return s;
	}


//----------------------------------
	private String getHardWholeShiftAlarmMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено повреждение! \n";
		s = s+"Отклонение от эталона: " + dE + " дБ.\n";
		s = s+"Характер возможного повреждения: нарушен ввод излучения. \n";
		s = s+"Резюме: Отклонение превышает предельно допустимое значение. \n";
		s = s+"Рекомендации: 1. Проверить ввод излучения. \n";
		s = s+"              2. Ввести новую эталонную рефлектограмму.";
		return s;
	}


//----------------------------------
	private String getSoftWholeShiftAlarmMessage()
	{
		String s = "Внимание! На участке тестирования обнаружено несоответствие!\n";
		s = s+"Отклонение от эталона: " + dE + " дБ.\n";
		s = s+"Характер возможного повреждения: изменен уровень вводимого излучения. \n";
		s = s+"Резюме: Отклонение не превышает предельно допустимого значения \n";
		s = s+"Рекомендации: 1. Задать новые уровни порогов. \n";
		s = s+"              2. Ввести новую эталонную рефлектограмму.";
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