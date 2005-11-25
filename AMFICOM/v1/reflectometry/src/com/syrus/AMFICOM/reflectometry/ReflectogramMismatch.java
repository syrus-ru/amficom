/*-
 * $Id: ReflectogramMismatch.java,v 1.16 2005/11/25 08:23:48 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.reflectometry.corba.IdlAlarmType;
import com.syrus.AMFICOM.reflectometry.corba.IdlSeverity;
import com.syrus.util.TransferableObject;

/**
 * Описание несоответствия рефлектограммы эталону.
 * (Это еще не аларм!)
 * Несет информацию о типе, уровне и дистанции события и привязке дистанции.
 * <p>
 * Дистанция и привязка требуют особого пояснения.
 * Свойство {@link #getCoord} содержит локальную оптическую дистанцию (в точках)
 * объекта или события, с которым ассоциировано отклонение.
 * Свойство {@link #getDistance()} представляет ту же локальную оптическую
 * дистанцию в метрах.
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
 * от аларма (по оси дистанции) или совпадают с ним.
 * <p> Если аларм приходится на точечное событие (сварка, коннектор),
 * уже имеющее привязку к схеме, то anchor1Id и anchor2Id совпадают,
 * а соотв. дистанции нулевые.
 * <p> Если {@link #hasAnchors()} возвращает false, то привязки
 * нет и нужно использовать первый способ трансляции в схемные дистанции.
 * </ol>
 * XXX: возможно, имеет смысл разрешить точкам привязки совпадать и при этом
 * находиться по одну сторону от аларма. Этот случай использовался бы,
 * если был бы доступен только один якорь.
 * 
 * @author Old Wise Saa
 * @author $Author: saa $
 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
 * @module reflectometry
 */
public interface ReflectogramMismatch {
	/**
	 * Уровень события (или, иначе, существенность проблемы).
	 * Must be comparable.
	 * В настоящей версии практически полностью определяет реакцию системы на
	 * событие: будет ли создан аларм, или же будет предупреждение.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: saa $
	 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
	 * @module reflectometry
	 */
	enum Severity implements TransferableObject<IdlSeverity> {
		/**
		 * just a convenience level, not a real alarm
		 */
		SEVERITY_NONE("ReflectogramMismatch.Severity.None"), 
		/**
		 * soft alarm ('warning')
		 */
		SEVERITY_SOFT("ReflectogramMismatch.Severity.Soft"), 
		/**
		 * hard alarm ('alarm')
		 */
		SEVERITY_HARD("ReflectogramMismatch.Severity.Hard"); 

		private static Severity[] values = values();

		private final String key;

		private Severity(final String key) {
			this.key = key;
		}

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlSeverity getTransferable(final ORB orb) {
			return IdlSeverity.from_int(this.ordinal());
		}

		public String getLocalizedName() {
			return I18N.getString(this.key);
		}

		public String getLocalizedDescription() {
			return I18N.getString("ReflectogramMismatch.Severity") + ":\t" + this.getLocalizedName();
		}

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final int i) {
			return values[i];
		}

		/**
		 * @param severity
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static Severity valueOf(final IdlSeverity severity) {
			return valueOf(severity.value());
		}
	}

	/**
	 * Тип отклонения.
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: saa $
	 * @version $Revision: 1.16 $, $Date: 2005/11/25 08:23:48 $
	 * @module reflectometry
	 */
	enum AlarmType implements TransferableObject<IdlAlarmType> {
		/**
		 * тип не определен
		 * XXX: используется только вместе с SEVERITY_NONE
		 */
		TYPE_UNDEFINED("ReflectogramMismatch.AlarmType.Undefined"),
		/**
		 * обрыв линии
		 */
		TYPE_LINEBREAK("ReflectogramMismatch.AlarmType.LineBreak"),
		/**
		 * отклонение (выход за маски)
		 */
		TYPE_OUTOFMASK("ReflectogramMismatch.AlarmType.OutOfMask"),
		/**
		 * новое/потерянное событие в пределах масок
		 */
		TYPE_EVENTLISTCHANGED("ReflectogramMismatch.AlarmType.EventListChanged");

		private static AlarmType[] values = values();

		private final String key;

		private AlarmType(final String key) {
			this.key = key;
		}

		/**
		 * @param orb
		 * @see TransferableObject#getTransferable(ORB)
		 */
		public IdlAlarmType getTransferable(final ORB orb) {
			return IdlAlarmType.from_int(this.ordinal());
		}

		public String getLocalizedName() {
			return I18N.getString(this.key);
		}

		public String getLocalizedDescription() {
			return I18N.getString("ReflectogramMismatch.AlarmType") + ":\t" + this.getLocalizedName();
		}

		/**
		 * @param i
		 * @throws ArrayIndexOutOfBoundsException
		 */
		public static AlarmType valueOf(final int i) {
			return values[i];
		}

		public static AlarmType valueOf(final IdlAlarmType alarmType) {
			return valueOf(alarmType.value());
		}
	}

	/**
	 * возвращает существенность проблемы, see {@link Severity}.
	 * @return Существенность проблемы, see {@link Severity}.
	 */
	Severity getSeverity();

	/**
	 * возвращает тип несоответствия, see {@link AlarmType}.
	 * @return Тип несоответствия, see {@link AlarmType}.
	 */
	AlarmType getAlarmType();

	/**
	 * возвращает координату аларма (точки).
	 * @return координата аларма (точки).
	 */
	int getCoord();

	/**
	 * возвращает условную координату окончания участка аларма (точки).
	 * Конечная координата должна быть "не левее" начальной.
	 * @return условная координата окончания участка аларма (точки).
	 */
	int getEndCoord();

	/**
	 * возвращает разрешение, точки/метр.
	 * @return разрешение, точки/метр.
	 */
	double getDeltaX();

	/**
	 * Возвращает дистанцию, метры.
	 * XXX: По-хорошему, этот метод должен быть определен как final в абстрактном классе.
	 * @return _должен_ возвращать
	 *   {@link #getDeltaX()} * {@link #getCoord()}
	 */
	double getDistance();

	/**
	 * Возвращает, определена ли степень превышения порога.
	 * @return true, если степень превышения порога определена.
	 */
	boolean hasMismatch();

	/**
	 * Если степень превышения порога определена,
	 * то возвращает нижнюю оценку степени превышения порога. 
	 * @return нижняя оценка степени превышения порога,
	 *   если только степень превышения порога определена.
	 * Гарантировано, что {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException степень превышения не определена,
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMinMismatch();

	/**
	 * Если степень превышения порога определена,
	 * то возвращает верхнюю оценку степени превышения порога. 
	 * @return верхняя оценка степени превышения порога,
	 *   если только степень превышения порога определена.
	 * Гарантировано, что {@link #getMinMismatch()} &lt;= {@link #getMaxMismatch()}
	 * @throws IllegalStateException степень превышения не определена
	 *  ({@link #hasMismatch()} == false)
	 */
	double getMaxMismatch();

	/**
	 * Возвращает, возможно ли использование привязки по двум якорям.
	 * @return true, если возможно использование привязки по двум якорям
	 * {@link #getAnchor1Id} {@link #getAnchor1Coord}
	 * {@link #getAnchor2Id} {@link #getAnchor2Coord}
	 */
	boolean hasAnchors();

	/**
	 * Возвращает ID якоря 1.
	 * @return ID якоря 1. not null.
	 * @throws IllegalStateException если {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor1Id();

	/**
	 * Возвращает ID якоря 2.
	 * @return ID якоря 2. not null.
	 * @throws IllegalStateException если {@link #hasAnchors()} is false
	 */
	SOAnchor getAnchor2Id();

	/**
	 * Возвращает дистанцию(точки) якоря 1.
	 * @return дистанция(точки) якоря 1.
	 * @throws IllegalStateException если {@link #hasAnchors()} is false
	 */
	int getAnchor1Coord();

	/**
	 * Возвращает дистанцию(точки) якоря 2.
	 * @return дистанция(точки) якоря 2.
	 * @throws IllegalStateException если {@link #hasAnchors()} is false
	 */
	int getAnchor2Coord();
}