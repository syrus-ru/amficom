package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmDescriptorEvent;
import com.syrus.AMFICOM.general.Identifier;

public class OpticalAlarmDescriptorEvent extends AlarmDescriptorEvent
{
	public static final int softCut = 1;
	public static final int softShift = 2;
	public static final int softWholeShift = 3;
	public static final int hardCut = 10;
	public static final int hardShift = 11;
	public static final int hardWholeShift = 12;

	public static final String weld = "weld";
	public static final String linear = "linear";
	public static final String connector = "connector";

	private int alarmType = 0;

	private String referenceEventType;
	private String message = "Внимание! На трассе повреждение!";

	private ReflectogramAlarm ra;
	private double deltaX;
	private double dE;

	public OpticalAlarmDescriptorEvent(Identifier me_id, double deltaX, ReflectogramAlarm ra)
	{
		super(me_id, "", "");
		this.ra = ra;
		this.deltaX = deltaX;

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
			title = "Предупреждение";
			severity = AlarmDescriptorEvent.WARNING;
		}
		else
		{
			title = "Тревога";
			severity = AlarmDescriptorEvent.ALARM;
		}
		text = message;
	}

	public PathElement getLink(double d)
	{
		double meters = d * 1000.;
		return getPathDecompositor().getPathElementByOpticalDistance(meters);
	}

	public PathElement getNearestEvent(double d)
	{
//		double meters = d * 1000.;
//		PathElement pe = getPathDecompositor().getPathElementByOpticalDistance(meters);
//		if (pe.getType() == PathElement.SCHEME_ELEMENT)
//			return pe;
//		if (isLeftNearest())
//			return getPathDecompositor().getPreviousNode(pe, true);
//		else
//			return getPathDecompositor().getNextNode(pe, true);
//
		double meters = d * 1000.;
		PathElement pe = getPathDecompositor().getPathElementByOpticalDistance(meters);
		if (pe.type().equals(Type.SCHEME_ELEMENT))
			return pe;
		PathElement next = getPathDecompositor().getNextNode(pe);
		PathElement prev = getPathDecompositor().getPreviousNode(pe);
		if (next == null)
		{
			if (prev == null)
				return null;
			else
				return prev;
		}
		else
		{
			if (prev == null)
				return next;
			else
			{
				if(Math.abs(getPathDecompositor().getOpticalDistanceFromStart(next)[0] - d) <
						Math.abs(getPathDecompositor().getOpticalDistanceFromStart(prev)[0] - d))
					return next;
				else
					return prev;
			}
		}
	}

	public PathElement getOtherEvent(double d)
	{
//		double meters = d * 1000.;
//		PathElement pe = getPathDecompositor().getPathElementByOpticalDistance(meters);
//		if(pe.getType() == PathElement.SCHEME_ELEMENT)
//			return pe;
//		if(isLeftNearest())
//			return getPathDecompositor().getNextNode(pe, true);
//		else
//			return getPathDecompositor().getPreviousNode(pe, true);

		PathElement pe = getPathDecompositor().getPathElementByOpticalDistance(d * 1000);
		if (pe.type().equals(Type.SCHEME_ELEMENT))
			return pe;
		PathElement next = getPathDecompositor().getNextNode(pe);
		PathElement prev = getPathDecompositor().getPreviousNode(pe);
		if (next == null)
		{
			if (prev == null)
				return null;
			else
				return prev;
		}
		else
		{
			if (prev == null)
				return next;
			else
			{
				if(Math.abs(getPathDecompositor().getOpticalDistanceFromStart(next)[0] - d) >
						Math.abs(getPathDecompositor().getOpticalDistanceFromStart(prev)[0] - d))
					return next;
				else
					return prev;
			}
		}
	}

	public double getEventCoord()
	{
		return ra.alarmPointCoord * deltaX;
	}

	public double getNearestEventDistance(double d)
	{
		PathElement pe = getNearestEvent(d);
		if (pe != null)
			return Math.abs(getPathDecompositor().getOpticalDistanceFromStart(pe)[0] - d);
		return -1;
	}

	public double getOtherEventDistance(double d)
	{
		PathElement pe = getNearestEvent(d);
		if(pe != null)
			return Math.abs(getPathDecompositor().getOpticalDistanceFromStart(pe)[0] - d);
		return -1;
}

	public double getNearestEventDistance()
	{
		return getNearestEventDistance(ra.alarmPointCoord * deltaX);
//		double d;
//		if(isLeftNearest())
//			d = ra.leftReflectoEventCoord - ra.alarmPointCoord;
//		else
//			d = ra.rightReflectoEventCoord - ra.alarmPointCoord;
//		return (Math.abs(d) * deltaX) / 1000d;
	}

	public double getOtherEventDistance()
	{
		return getOtherEventDistance(ra.alarmPointCoord * deltaX);
//		double d;
//		if(isLeftNearest())
//			d = ra.rightReflectoEventCoord - ra.alarmPointCoord;
//		else
//			d = ra.leftReflectoEventCoord - ra.alarmPointCoord;
//		return (Math.abs(d) * deltaX) / 1000d;
	}

	public double getNearestEventCoord()
	{
		return getPathDecompositor().getOpticalDistanceFromStart(getNearestEvent(ra.alarmPointCoord * deltaX))[0];
//		if(isLeftNearest())
//			return (ra.leftReflectoEventCoord * deltaX) / 1000d;
//		else
//			return (ra.rightReflectoEventCoord * deltaX) / 1000d;
	}

	public double getOtherEventCoord()
	{
		return getPathDecompositor().getOpticalDistanceFromStart(getOtherEvent(ra.alarmPointCoord * deltaX))[0];
//		if(isLeftNearest())
//			return (ra.rightReflectoEventCoord * deltaX) / 1000d;
//		else
//			return (ra.leftReflectoEventCoord * deltaX) / 1000d;
	}
//
//	public boolean isLeftNearest()
//	{
//		return Math.abs(ra.leftReflectoEventCoord - ra.alarmPointCoord) <
//				Math.abs(ra.rightReflectoEventCoord - ra.alarmPointCoord);
//	}

	public String getDistanceDescription()
	{
		double d0 = getEventCoord();
		double d5 = getNearestEventDistance(d0);
		double d6 = getOtherEventDistance(d0);

		StringBuffer s = new StringBuffer();
		s.append("Событие произошло на участке '");
		s.append(getLink(d0).name());
		s.append("' между устройствами:\n'");
		s.append(getNearestEvent(d0).name());
		s.append("' (дистанция до устройства - ");
		s.append(MathRef.round_3(d5));
		s.append(" км),\n'");
		s.append(getOtherEvent(d0).name());
		s.append("' (дистанция до устройства - ");
		s.append(MathRef.round_3(d6));
		s.append(" км).\n");
		return s.toString();
	}

	public String getEventsDescription()
	{
		double d0 = getEventCoord();
		double d3 = getNearestEventDistance();
		double d4 = getOtherEventDistance();

		StringBuffer s = new StringBuffer();
		s.append("Расстояние до ближайшего отражательного события '");
		s.append(getNearestEvent(d0).name());
		s.append("' = ");
		s.append(MathRef.round_3(d3));
		s.append(" км \nРасстояние до ближайшего отражательного события с другой стороны '");
		s.append(getOtherEvent(d0).name());
		s.append("' = ");
		s.append(MathRef.round_3(d4));
		s.append(" км \n");
		return s.toString();
	}

	public String getDistanceDescription2()
	{
		double d1 = getNearestEventDistance();
		double d3 = getNearestEventCoord();
		double d4 = getOtherEventCoord();

		StringBuffer s = new StringBuffer();
		s.append("Координаты ближайших отражательных событий: ");
		s.append(MathRef.round_3(d3 / 1000d));
		s.append(" км; ");
		s.append(MathRef.round_3(d4 / 1000d));
		s.append(" км.\nРасстояние до ближайшего отражательного события ");
		s.append(MathRef.round_3(d1 / 1000d));
		s.append(" км.\n");
		return s.toString();
	}

	private String getSoftCutMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено несоответствие!\nКоордината несоответствия: ");
		s.append(MathRef.round_3(ra.alarmPointCoord * deltaX / 1000.));
		s.append(" км.\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\nТип события: ");
		s.append(referenceEventType);
		s.append("\n");
		s.append(getDistanceDescription());
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
			s.append(getEventsDescription());

		s.append("Характер несоответствия: локальные потери.\n");
		s.append("Резюме: Отклонение не превышает предельно допустимого значения.\n");
		s.append("Рекомендации: 1. Проверить на возможность несанкционированного доступа.\n");
		s.append("              2. Задать новые уровни порогов на событие.");
		return s.toString();
	}

	private String getHardCutMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено повреждение!\nКоордината повреждения: ");
		s.append(MathRef.round_3(ra.alarmPointCoord * deltaX / 1000.));
		s.append(" км.\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\nТип события: ");
		s.append(referenceEventType);
		s.append("\n");
		s.append(getDistanceDescription());
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
			s.append(getEventsDescription());

		s.append("Характер повреждния: обрыв или значительные локальные потери.\n");
		s.append("Резюме: Отклонение превышает предельно допустиме значение.\n");
		s.append("Рекомендации: 1. Проверить целостность кабеля.\n");
		s.append("              2. Ввести новую эталонную рефлектограмму.");

		return s.toString();
	}

	private String getSoftShiftMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено несоответствие!\nКоордината несоответствия: ");
		s.append(MathRef.round_3(ra.alarmPointCoord * deltaX / 1000d));
		s.append(" км.\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\nТип события: ");
		s.append(referenceEventType);
		s.append("\n");
		s.append(getDistanceDescription());
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
			s.append(getEventsDescription());

		s.append("Характер несоответствия: локальное усиление.\n");
		s.append("Резюме: Отклонение не превышает предельно допустимого значения.\n");
		s.append("Рекомендации: Задать новые уровни порогов на событие.");
		return s.toString();
	}

	private String getHardShiftMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено повреждение!\nКоордината повреждения: ");
		s.append(MathRef.round_3(ra.alarmPointCoord * deltaX / 1000d));
				s.append(" км.\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\nТип события: ");
		s.append(referenceEventType);
		s.append("\n");
		s.append(getDistanceDescription());
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
			s.append(getEventsDescription());

		s.append("Характер повреждения: значительное локальное усиление \n");
		s.append("Резюме: Отклонение превышает предельно допустимое значение.\n");
		s.append("Рекомендации: Ввести новую эталонную рефлектограмму");
		return s.toString();
	}

	private String getEmptyAlarmMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования возможно повреждение.\nКоордината несоответствия: ");
		s.append(MathRef.round_3(ra.alarmPointCoord * deltaX / 1000d));
		s.append(" км.\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\nТип события: ");
		s.append(referenceEventType);
		s.append("\n");
		s.append(getDistanceDescription());
		if(ra.getEventType() != ReflectogramEvent.CONNECTOR)
			s.append(getEventsDescription());

		s.append("Характер возможного повреждения: неизвестен.\n");
		s.append("Резюме: Для суждения о повреждении необходима дополнительная информация.\n");
		s.append("Рекомендации: Провести измерения при других тестовых параметрах.");

		return s.toString();
	}

	private String getHardWholeShiftAlarmMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено повреждение!\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\n");
		s.append("Характер возможного повреждения: нарушен ввод излучения.\n");
		s.append("Резюме: Отклонение превышает предельно допустимое значение.\n");
		s.append("Рекомендации: 1. Проверить ввод излучения.\n");
		s.append("              2. Ввести новую эталонную рефлектограмму.");
		return s.toString();
	}

	private String getSoftWholeShiftAlarmMessage()
	{
		StringBuffer s = new StringBuffer();
		s.append("Внимание! На участке тестирования обнаружено несоответствие!\nОтклонение от эталона: ");
		s.append(dE);
		s.append(" дБ.\n");
		s.append("Характер возможного повреждения: изменен уровень вводимого излучения.\n");
		s.append("Резюме: Отклонение не превышает предельно допустимого значения\n");
		s.append("Рекомендации: 1. Задать новые уровни порогов.\n");
		s.append("              2. Ввести новую эталонную рефлектограмму.");
		return s.toString();
	}
}
