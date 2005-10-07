/*-
 * $Id: ReflectogramMismatch.java,v 1.3 2005/10/07 08:15:12 bass Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Описание несоответствия рефлектограммы эталону.
 * (Это еще не аларм!)
 * Несет информацию о типе, уровне и дистанции события и привязке дистанции.
 * <p>
 * Дистанция и привязка требуют особого пояснения.
 * Свойство {@link #getCoord} содержит локальную оптическую дистанцию (в точках)
 * объекта или события, с которым ассоциировано отклонение.
 * Свойство {@link #getDistance()} представляет ту же локальную оптическую
 * дистанцию в метрах
 * Для перевода локальной оптической дистанции
 * в схемную оптическую дистанцию есть два способа:
 * <ol>
 * <li> Непосредственная подстановка локальной оптической дистанции
 * {@link #getDistance()} в качестве схемной оптической дистанции.
 * При этом возникает погрешность, определяемая
 * отличием схемных оптических дистанций от истинных оптических дистанций
 * (из-за ограничений возможности точной схемной привязки), а
 * также отличием локальных оптических дистанций от истинных.
 * Хотя для определения точки повреждения кабеля эти отличия могут быть
 * непринципиальны, это сильно усложняет задачу определения, относится ли
 * событие к данному точечному объекту (муфте и пр.) или к смежному с ним
 * кабелю.
 * <li> В случае, если {@link #hasAnchors()} возвращает true, возможно
 * использование привязки
 *   {@link #getAnchor1Id}/{@link #getAnchor1Coord},
 *   {@link #getAnchor2Id}/{@link #getAnchor2Coord}.
 * Эта информация указывает локальную оптическую дистанцию (тоже в точках)
 * для 2 объектов, находящихся по разную сторону от аларма.
 * Это позволяет точнее определять место аларма. В случае, если в момент
 * задания эталона была предоставлена привязка для каждого объекта,
 * точность указания объекта, соответствующего аларму, становится абсолютной,
 * а соотношение дистанций аларма и объектов передает всю возможную
 * информацию о пропорциях положения.
 * <p> Для восстановления схемной оптической или физической дистанции,
 * надо использовать пропорцию
 * 
 * <pre> (coord - anchor1Coord) : (anchor2Coord - coord) = L1 : L2 </pre>
 * 
 * Где L1 и L2 - схемные (оптические или физические) расстояния до аларма
 * от соответвующих объектов схемы.
 * <p> Если объекты привязки определены, то они лежат по разные стороны
 * от аларма (по оси дистанции).
 * <p> Если аларм приходится на точечное событие (сварка, коннектор),
 * уже имеющее привязку к схеме, то anchor1Id и anchor2Id совпадают,
 * а соотв. дистанции нулевые.
 * <p> Если {@link #hasAnchors()} возвращает false, то привязки
 * нет и нужно использовать первый способ трансляции в схемные дистанции.
 * </ol>
 * 
 * @author Old Wise Saa
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
 * @module reflectometry
 */
public interface ReflectogramMismatch {
	/**
	 * Alarm levels. Must be comparable with >; >=
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
	 * @module reflectometry
	 */
	enum Severity {
		SEVERITY_NONE, // just a convenience level, not a real alarm
		SEVERITY_SOFT, // soft alarm ('warning')
		SEVERITY_HARD;  // hard alarm ('alarm')

		private static Severity[] values = values();

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final int i) {
			return values[i];
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.3 $, $Date: 2005/10/07 08:15:12 $
	 * @module reflectometry
	 */
	enum AlarmType {
		TYPE_UNDEFINED,
		TYPE_LINEBREAK, // обрыв линии
		TYPE_OUTOFMASK, // выход за маски
		TYPE_EVENTLISTCHANGED; // новое/потерянное событие в пределах масок

		private static AlarmType[] values = values();

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static AlarmType valueOf(final int i) {
			return values[i];
		}
	}

	/**
	 * @return true, если степень превышения предупр. порога определена
	 */
	boolean hasMismatch();

	/**
	 * @return нижняя оценка степени превышения предупредительного порога,
	 *   если степень превышения определена
	 *   ({@link #hasMismatch() возвращает true})
	 * @throws IllegalArgumentException, если степень превышения не определена
	 */
	double getMinMismatch();

	/**
	 * @return верхняя оценка степени превышения предупредительного порога,
	 *   если степень превышения определена
	 *   ({@link #hasMismatch() возвращает true})
	 * @throws IllegalArgumentException, если степень превышения не определена
	 */
	double getMaxMismatch();

	/**
	 * @return Существенность проблемы, определяется константами SEVERITY_*
	 */
	Severity getSeverity();

	/**
	 * XXX: По-хорошему, этот метод должен быть определен как final в абстрактном классе
	 * @return _должен_ возвращать
	 *   {@link #getDeltaX()} * {@link #getCoord()}
	 */
	double getDistance();

	/**
	 * @return true, если возможно использование привязки
	 * {@link #getAnchor1Id} {@link #getAnchor1Coord}
	 * {@link #getAnchor2Id} {@link #getAnchor2Coord}
	 */
	boolean hasAnchors();

	SOAnchor getAnchor1Id();

	SOAnchor getAnchor2Id();

	int getAnchor1Coord();

	int getAnchor2Coord();

	/**
	 * @return координата аларма (точки)
	 */
	int getCoord();

	/**
	 * @return условная конечная координата аларма (точки)
	 */
	int getEndCoord();

	/**
	 * @return Тип несоответствия, см. поля TYPE_*
	 */
	AlarmType getAlarmType();

	/**
	 * @return разрешение, точки/метр
	 */
	double getDeltaX();

}