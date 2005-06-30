package com.syrus.AMFICOM.analysis.dadara;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.AMFICOM.analysis.SOAnchor;

/**
 * Структура для описания рефлектограммного аларма.
 * Несет информацию о типе, уровне и дистанции события.
 * <p>
 * Дистанция требует особого пояснения.
 * Поле {@link #pointCoord} содержит локальную оптическую дистанцию (в точках)
 * объекта или события, с которым ассоциировано отклонение. Для перевода
 * локальной оптической дистанции в схемную оптическую дистанцию есть два способа:
 * <ol>
 * <li> Непосредственная подстановка локальной оптической дистанции в качестве
 * схемной оптической дистанции. При этом возникает погрешность, определяемая
 * отличием схемных оптических дистанций от истинных оптических дистанций
 * (из-за ограничений возможности точной схемной привязки), а
 * также отличием локальных оптических дистанций от истинных.
 * Хотя для определения точки повреждения кабеля эти отличия могут быть
 * непринципиальны, это сильно усложняет задачу определения, относится ли
 * событие к данному точечному объекту (муфте и пр.) или к смежному с ним
 * кабелю.
 * <li> Использование привязки
 *   {@link #ref1Id}/{@link #ref1Coord},
 *   {@link #ref2Id}/{@link #ref2Coord}.
 * Эта информация указывает локальную оптическую дистанцию (тоже в точках)
 * для 0, 1 или 2 объектов, находящихся по разную сторону от аларма.
 * Это позволяет точнее определять место аларма. В случае, если
 * при задании эталона была предоставлена привязка для каждого объекта,
 * точность указания объекта, соответствующего аларму, становится абсолютной,
 * а соотношение дистанций аларма и объектов, передает всю возможную
 * информацию о пропорциях положения.
 * <p> Для восстановления схемной оптической или физической дистанции,
 * надо использовать пропорции
 * 
 * <pre> pointCoord-ref1Coord : ref2Coord-pointCoord = L1 : L2 </pre>
 * 
 * Где L1 и L2 - схемные (оптические или физические) расстояния до аларма
 * от соответвующих объектов схемы.
 * <p> Если объекты привязки определены, то они лежат по разные стороны
 * от аларма (по оси дистанции).
 * <p> Если аларм приходится на точечное событие (сварка, коннектор),
 * уже имеющее привязку к схеме, то ref1Id и ref2Id совпадают,
 * а соотв. дистанции нулевые.
 * <p> Если хотя бы одно их ref1Id, ref2Id - null, то считается, что привязки
 * нет и нужно использовать первый способ трансляции в схемные дистанции.
 * </ol>
 * 
 * @author $Author: saa $
 * @version $Revision: 1.17 $, $Date: 2005/06/30 14:19:57 $
 * @module
 */
public class ReflectogramAlarm {
	// Alarm levels. Must be comparable with >; >=
	public static final int LEVEL_NONE = 0; // just a convenience level, not a real alarm
	public static final int LEVEL_SOFT = 1; // soft alarm ('warning')
	public static final int LEVEL_HARD = 2; // hard alarm ('alarm')

	public static final int TYPE_UNDEFINED = 0;
	public static final int TYPE_LINEBREAK = 1; // обрыв линии
	public static final int TYPE_OUTOFMASK = 2; // выход за маски
	public static final int TYPE_EVENTLISTCHANGED = 3; // новое/потерянное событие в пределах масок

	public int level = LEVEL_NONE;
	// оптическая дистанция (в точках) события эталона или точки на
	// событии эталона, в котором/которой произошел аларм.
	// Может отличаться от фактической точки выхода за пределы порогов.
	public int pointCoord = 0;
	public int endPointCoord = 0; // ?
	public int alarmType = TYPE_UNDEFINED;
    public double deltaX = 0.0;

    // информация о (максимум двух) ближайших привязанных объектах
    public SOAnchor ref1Id = null; // null, если привязка #1 не определена
    public int ref1Coord = 0; // оптическая дистанция обьъекта #1, не определено, если ref1Id == null
    public SOAnchor ref2Id = null; // null, если привязка #2 не определена
    public int ref2Coord = 0; // оптическая дистанция обьъекта #2, не определено, если ref2Id == null

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
    	return minMismatch <= maxMismatch;
    }
    /**
     * @return нижняя оценка степени превышения предупредительного порога,
     *   если степень превышения определена
     *   ({@link #hasMismatch() возвращает true})
     * @throws IllegalArgumentException, если степень превышения не определена
     */
    public double getMinMismatch() {
    	if (hasMismatch())
    		return minMismatch;
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
    		return maxMismatch;
    	else
    		throw new IllegalArgumentException();
    }

    /*
	public int getLevel()
	{
		return level;
	}
	public int getPoint()
	{
		return pointCoord;
	}
*/
    public int getSeverity() {
        return level;
    }
	public int getSpecificType() {
		return alarmType;
	}
    public double getDistance() {
        return deltaX * pointCoord;
    }
	/**
	 * Creates an empty 'no alarm' alarm
	 * with level = LEVEL_NONE and type = TYPE_UNDEFINED.
	 */
	public ReflectogramAlarm()
	{ // all initialization is already done
	}

	public static ReflectogramAlarm createFromDIS(DataInputStream dis)
	throws IOException, SignatureMismatchException
	{
		ReflectogramAlarm ret = new ReflectogramAlarm();
		ret.level = dis.readInt();
		ret.pointCoord = dis.readInt();
		ret.endPointCoord = dis.readInt();
		ret.alarmType = dis.readInt();
        ret.deltaX = dis.readDouble();
        if (dis.readBoolean()) {
        	ret.minMismatch = dis.readDouble(); // XXX: неплохо бы покомпактнее
        	ret.maxMismatch = dis.readDouble();
        } else {
        	ret.minMismatch = 1.0;
        	ret.maxMismatch = 0.0;
        }
        if(dis.readBoolean()) {
        	ret.ref1Id = (SOAnchor) SOAnchor.getDSReader().readFromDIS(dis);
        	ret.ref2Id = (SOAnchor) SOAnchor.getDSReader().readFromDIS(dis);
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
		// ориентировочно, занимает суммарно от 26 до 66 байт
		dos.writeInt(this.level);
		dos.writeInt(this.pointCoord);
		dos.writeInt(this.endPointCoord);
		dos.writeInt(this.alarmType);
        dos.writeDouble(this.deltaX);
        if (hasMismatch()) {
        	dos.writeBoolean(true);
        	dos.writeDouble(this.minMismatch);
        	dos.writeDouble(this.maxMismatch);
        } else {
        	dos.writeBoolean(false);
        }
        if (ref1Id != null && ref2Id != null) {
        	dos.writeBoolean(true);
        	ref1Id.writeToDOS(dos);
        	ref2Id.writeToDOS(dos);
        	dos.writeInt(ref1Coord);
        	dos.writeInt(ref2Coord);
        } else {
        	dos.writeBoolean(false);
        }
	}

	public static ReflectogramAlarm createFromByteArray(byte[] bar) throws DataFormatException
	{
            try {
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bar));
                ReflectogramAlarm ret;
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

	public static byte[] alarmsToByteArray(ReflectogramAlarm[] ralarms) {
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
	public static ReflectogramAlarm[] alarmsFromByteArray(byte[] bar)
    throws DataFormatException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bar);
		DataInputStream dis = new DataInputStream(bais);
		try
		{
			int count = dis.readInt();
			ReflectogramAlarm[] ret = new ReflectogramAlarm[count]; // exception possible when input data is malformed
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
    public void toHardest(ReflectogramAlarm that)
    {
        if (that.level > this.level
                || that.level == this.level && that.pointCoord < this.pointCoord)
        {
            this.level = that.level;
            this.pointCoord = that.pointCoord;
            this.endPointCoord = that.endPointCoord;
            this.alarmType = that.alarmType;
            this.deltaX = that.deltaX;
        }
    }
    
    public String toString()
    {
        return "ReflectogramAlarm(level=" + level
        + ",type=" + alarmType
        + ",begin=" + pointCoord
        + ",end=" + endPointCoord
        + ",distance=" + getDistance()
        + (hasMismatch() ? ",mismatch=" + getMinMismatch() + "-" + getMaxMismatch() : "")
        + ")";
    }
}
