package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.SOAnchor;
import com.syrus.io.DataFormatException;
import com.syrus.io.SignatureMismatchException;

/**
 * Структура для описания несоответствия рефлектограммы эталону.
 * (Это еще не аларм!)
 * Несет информацию о типе, уровне и дистанции события и привязке дистанции.
 * <p>
 * Дистанция и привязка требуют особого пояснения.
 * Свойство {@link #getCoord} содержит локальную оптическую дистанцию (в точках)
 * объекта или события, с которым ассоциировано отклонение.
 * Свойство {@link #getDistance()} представляет ту же локальную оптическую
 * дистанцию в метрах.
 * Для перевода
 * локальной оптической дистанции в схемную оптическую дистанцию есть два способа:
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
 * @author $Author: saa $
 * @version $Revision: 1.6 $, $Date: 2005/10/06 13:34:02 $
 * @module dadara
 */
public class ReflectogramMismatch {
	private static final long SIGNATURE = 5490879050929171200L;

	// Alarm levels. Must be comparable with >; >=
	public static final int SEVERITY_NONE = 0; // just a convenience level, not a real alarm
	public static final int SEVERITY_SOFT = 1; // soft alarm ('warning')
	public static final int SEVERITY_HARD = 2; // hard alarm ('alarm')

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_LINEBREAK = 1; // обрыв линии
	public static final int TYPE_OUTOFMASK = 2; // выход за маски
	public static final int TYPE_EVENTLISTCHANGED = 3; // новое/потерянное событие в пределах масок

	private int severity = SEVERITY_NONE;
	// оптическая дистанция (в точках) события эталона или точки на
	// событии эталона, в котором/которой произошел аларм.
	// Может отличаться от фактической точки выхода за пределы порогов.
	private int coord = 0;
	private int endCoord = 0; // для отображения
	private int alarmType = TYPE_UNDEFINED;
	private double deltaX = 0.0;

	// информация о (максимум двух) ближайших привязанных объектах
	private SOAnchor ref1Id = null; // null, если привязка #1 не определена
	private int ref1Coord = 0; // оптическая дистанция обьъекта #1, не определено, если ref1Id == null
	private SOAnchor ref2Id = null; // null, если привязка #2 не определена
	private int ref2Coord = 0; // оптическая дистанция обьъекта #2, не определено, если ref2Id == null

	// оценка степени превышения предупр. порога по сравнению с тревожным
	// состояние "не определено" - если min > max
	// начальное состояние - "не определено"
	private double minMismatch = 1.0; // оценка снизу (состояние не определено)
	private double maxMismatch = 0.0; // оценка сверху (состояние не определено)

	/**
	 * Устанавливает степень превышения предупр. порога в состояние
	 * "определено" с диапазоном от min до max.
	 * min должен быть не более max
	 */
	public void setMismatch(double min, double max) {
		if (min > max)
			throw new IllegalArgumentException("minMismatch > maxMismatch");
		this.minMismatch = min;
		this.maxMismatch = max;
	}

	/**
	 * Устанавливает степень превышения предупр. порога в состояние "не определено" 
	 */
	public void unsetMismatch() {
		this.minMismatch = 1.0;
		this.maxMismatch = 0.0;
	}

	/**
	 * @return true, если степень превышения предупр. порога определена
	 */
	public boolean hasMismatch() {
		return this.minMismatch <= this.maxMismatch;
	}
	/**
	 * @return нижняя оценка степени превышения предупредительного порога,
	 *   если степень превышения определена
	 *   ({@link #hasMismatch() возвращает true})
	 * @throws IllegalArgumentException, если степень превышения не определена
	 */
	public double getMinMismatch() {
		if (hasMismatch())
			return this.minMismatch;
		else
			throw new IllegalArgumentException();
	}
	/**
	 * @return верхняя оценка степени превышения предупредительного порога,
	 *   если степень превышения определена
	 *   ({@link #hasMismatch() возвращает true})
	 * @throws IllegalArgumentException, если степень превышения не определена
	 */
	public double getMaxMismatch() {
		if (hasMismatch())
			return this.maxMismatch;
		else
			throw new IllegalArgumentException();
	}

	public int getSeverity() {
		return this.severity;
	}
	public void setSeverity(int severity) {
		this.severity = severity;
	}
	public int getSpecificType() {
		return getAlarmType();
	}
	public double getDistance() {
		return getDeltaX() * getCoord();
	}
	public void setAnchors(SOAnchor anchor1, int coord1,
			SOAnchor anchor2, int coord2) {
		if (anchor1 == null || anchor2 == null) {
			throw new IllegalArgumentException("null anchor in setAnchor");
		}
		this.ref1Id = anchor1;
		this.ref1Coord = coord1;
		this.ref2Id = anchor2;
		this.ref2Coord = coord2;
	}
	public void unSetAnchors() {
		this.ref1Id = null;
		this.ref1Coord = 0;
		this.ref2Id = null;
		this.ref2Coord = 0;
	}
	public boolean hasAnchors() {
		return this.ref1Id != null && this.ref2Id != null;
	}
	public SOAnchor getAnchor1Id() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref1Id;
	}
	public SOAnchor getAnchor2Id() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref2Id;
	}
	public int getAnchor1Coord() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref1Coord;
	}
	public int getAnchor2Coord() {
		if (! hasAnchors()) {
			throw new IllegalStateException();
		}
		return this.ref2Coord;
	}

	/**
	 * Creates an empty 'no alarm' alarm
	 * with level = LEVEL_NONE and type = TYPE_UNDEFINED.
	 */
	public ReflectogramMismatch()
	{ // all initialization is already done
	}

	public static ReflectogramMismatch createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		if (dis.readLong() != SIGNATURE) {
			throw new SignatureMismatchException();
		}
		ReflectogramMismatch ret = new ReflectogramMismatch();
		ret.severity = dis.readInt();
		ret.setCoord(dis.readInt());
		ret.setEndCoord(dis.readInt());
		ret.setAlarmType(dis.readInt());
		ret.setDeltaX(dis.readDouble());
		if (dis.readBoolean()) {
			ret.minMismatch = dis.readDouble(); // XXX: неплохо бы покомпактнее
			ret.maxMismatch = dis.readDouble();
		} else {
			ret.minMismatch = 1.0;
			ret.maxMismatch = 0.0;
		}
		if(dis.readBoolean()) {
			ret.ref1Id = SOAnchor.createFromDIS(dis);
			ret.ref2Id = SOAnchor.createFromDIS(dis);
			ret.ref1Coord = dis.readInt();
			ret.ref2Coord = dis.readInt();
		} else {
			ret.ref1Id = null;
			ret.ref1Id = null;
			ret.ref1Coord = 0;
			ret.ref2Coord = 0;
		}
		return ret;
	}
 
	public void writeToDOS(DataOutputStream dos)
	throws IOException
	{
		// ориентировочно, занимает суммарно от 34 до 74 байт
		dos.writeLong(SIGNATURE);
		dos.writeInt(this.severity);
		dos.writeInt(this.getCoord());
		dos.writeInt(this.getEndCoord());
		dos.writeInt(this.getAlarmType());
		dos.writeDouble(this.getDeltaX());
		if (hasMismatch()) {
			dos.writeBoolean(true);
			dos.writeDouble(this.minMismatch);
			dos.writeDouble(this.maxMismatch);
		} else {
			dos.writeBoolean(false);
		}
		if (this.ref1Id != null && this.ref2Id != null) {
			dos.writeBoolean(true);
			this.ref1Id.writeToDOS(dos);
			this.ref2Id.writeToDOS(dos);
			dos.writeInt(this.ref1Coord);
			dos.writeInt(this.ref2Coord);
		} else {
			dos.writeBoolean(false);
		}
	}

	public static ReflectogramMismatch createFromByteArray(byte[] bar) throws DataFormatException
	{
			try {
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bar));
				ReflectogramMismatch ret;
				ret = createFromDIS(dis);
				dis.close();
				return ret;
			} catch (IOException e) {
				throw new DataFormatException(e.toString());
			}
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			writeToDOS(dos);
			dos.close();
			return baos.toByteArray();
		}
		catch (IOException ioe) {
			throw new InternalError("Unexpected exception" + ioe.toString());
		}
	}

	public static byte[] alarmsToByteArray(ReflectogramMismatch[] ralarms) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeInt(ralarms.length);
			for (int i = 0; i < ralarms.length; i++)
			{
				ralarms[i].writeToDOS(dos);
			}
		}
		catch (IOException ioe) {
			throw new InternalError("Unexpected exception" + ioe.toString());
		}
		return baos.toByteArray();
	}

	// seem to be unused
	public static ReflectogramMismatch[] alarmsFromByteArray(byte[] bar)
	throws DataFormatException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			int count = dis.readInt();
			ReflectogramMismatch[] ret = new ReflectogramMismatch[count]; // exception possible when input data is malformed
			for (int i = 0; i < count; i++)
				ret[i] = createFromDIS(dis);
			dis.close();
			return ret;
		}
		catch(IOException ioe)
		{
			throw new DataFormatException(ioe.toString());
		}
	}

	/**
	 * Если аларм that более приоритетен, чем this,
	 * загружает параметры that в this.
	 * <p>Приоритет определяется в таком порядке:
	 * <ol>
	 * <li> бОльший level
	 * <li> меньший pointCoord
	 * <li> сравнение остальных параметров пока не определено 
	 * </ol>
	 * 
	 * @param that
	 */
	public void toHardest(ReflectogramMismatch that)
	{
		if (that.severity > this.severity
				|| that.severity == this.severity && that.getCoord() < this.getCoord())
		{
			this.severity = that.severity;
			this.setCoord(that.getCoord());
			this.setEndCoord(that.getEndCoord());
			this.setAlarmType(that.getAlarmType());
			this.setDeltaX(that.getDeltaX());
		}
	}
	
	@Override
	public String toString()
	{
		return "ReflectogramMismatch(level=" + this.severity
		+ ",type=" + getAlarmType()
		+ ",begin=" + getCoord()
		+ ",end=" + getEndCoord()
		+ ",distance=" + getDistance()
		+ (hasMismatch() ? ",mismatch=" + getMinMismatch() + "-" + getMaxMismatch() : "")
		+ (this.ref1Id != null ?  ",anc1=" + this.ref1Id + "@" + this.ref1Coord : "")
		+ (this.ref2Id != null ?  ",anc2=" + this.ref2Id + "@" + this.ref2Coord : "")
		+ ")";
	}

	public void setCoord(int coord) {
		this.coord = coord;
	}

	public int getCoord() {
		return this.coord;
	}

	public void setEndCoord(int endCoord) {
		this.endCoord = endCoord;
	}

	public int getEndCoord() {
		return this.endCoord;
	}

	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}

	public int getAlarmType() {
		return this.alarmType;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	public double getDeltaX() {
		return this.deltaX;
	}
}
