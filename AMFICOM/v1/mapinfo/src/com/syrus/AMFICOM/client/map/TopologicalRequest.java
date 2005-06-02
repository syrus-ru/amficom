package com.syrus.AMFICOM.Client.Map;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * Структура запроса изображения с сервера
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.2 $, $Date: 2005/06/02 12:14:04 $
 * @module mapinfo_v1
 */
public class TopologicalRequest implements Comparable
{
	/**
	 * Для участков изображения, добавляемых в кэш "на всякий случай",
	 * до запроса пользователя и перерисовываемых в фоновом режиме
	 */
	private static final int PRIORITY_BACKGROUND = 20;
	
	public static final int PRIORITY_BACKGROUND_HIGH = TopologicalRequest.PRIORITY_BACKGROUND + 1;
	public static final int PRIORITY_BACKGROUND_MIDDLE = TopologicalRequest.PRIORITY_BACKGROUND + 2;	
	public static final int PRIORITY_BACKGROUND_LOW = TopologicalRequest.PRIORITY_BACKGROUND + 3;

	/**
	 * Для участков изображения, которые требуется отобразить по
	 * текущему запросу пользователя (самый высокий приоритет)
	 */
	public static final int PRIORITY_EXPRESS = 10;	
	
	/**
	 * Для участков изображения, уже подгруженных, которые требуется
	 * только перерисовать
	 */
	public static final int PRIORITY_ALREADY_LOADED = 0;
	
	/**
	 * Приоритет запроса
	 */
	private int priority = TopologicalRequest.PRIORITY_BACKGROUND;
	
	/**
	 * Отображаемое изображение
	 */
	private ImageIcon image = null;
	/**
	 * Масштаб избражения (для текущей ширины изображения!)
	 */
	private double topoScale = 1.f;
	/**
	 * Сферические координаты границы избражения
	 */
	private DoublePoint topoCenter = null;
	/**
	 * Время последнего использования
	 */
	private long lastUsed = 0;
	
	public String toString()
	{
		String resultString = 
			"priority (" + this.priority + "), " +
			"topo scale (" + this.topoScale + "), " +
			"topo center (" + this.topoCenter.getX() + ":" + this.topoCenter.getY() + ")";
		
		return resultString;
	}

	/**
	 * Для сортировки отчётов по убыванию по времени 
	 */
	public int compareTo(Object o)
	{
		TopologicalRequest req = (TopologicalRequest)o;
		if (this.lastUsed > req.lastUsed)
			return -1;
		else if (this.lastUsed < req.lastUsed)
			return 1;
		
		return 0;
	}
	/**
	 * @return Returns the image.
	 */
	public synchronized ImageIcon getImage()
	{
		return this.image;
	}
	/**
	 * @param image The image to set.
	 */
	public synchronized void setImage(ImageIcon image)
	{
		this.image = image;
	}
	/**
	 * @return Returns the lastUsed.
	 */
	public synchronized long getLastUsed()
	{
		return this.lastUsed;
	}
	/**
	 * @param lastUsed The lastUsed to set.
	 */
	public synchronized void setLastUsed(long lastUsed)
	{
		this.lastUsed = lastUsed;
	}
	/**
	 * @return Returns the priority.
	 */
	public synchronized int getPriority()
	{
		return this.priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public synchronized void setPriority(int priority)
	{
		this.priority = priority;
	}
	/**
	 * @return Returns the topoCenter.
	 */
	public synchronized DoublePoint getTopoCenter()
	{
		return this.topoCenter;
	}
	/**
	 * @param topoCenter The topoCenter to set.
	 */
	public synchronized void setTopoCenter(DoublePoint topoCenter)
	{
		this.topoCenter = topoCenter;
	}
	/**
	 * @return Returns the topoScale.
	 */
	public synchronized double getTopoScale()
	{
		return this.topoScale;
	}
	/**
	 * @param topoScale The topoScale to set.
	 */
	public synchronized void setTopoScale(double topoScale)
	{
		this.topoScale = topoScale;
	}
}