package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.io.*;
import java.util.*;

import java.awt.Color;

/**
 * @deprecated ОБИя
 * @version $Revision: 1.8 $, $Date: 2005/03/29 17:49:51 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public class ColorManager
{
	private ColorManager()
			{}

	static class Col
	{
		String name;
		Color color;

		public Col(String name, Color color)
		{
			this.name = name;
			this.color = color;
		}
	}
	private static String propertiesFileName = "colors.properties";
	private static Properties properties;

	static Map traceColorTable = new HashMap(25);  //список всех доступных цветов
																												 //в виде (IDцвета, ЦВЕТ)
	static Map freeTraceColors	= new HashMap(7);  //список цветов не занятых трассами
																												 //в виде (IDцвета, ЦВЕТ)
	static Map busyTraceColors	= new HashMap();   //список цветов занятых трассами
																												 //в виде (IDтрассы, IDцвета)
	static Map randomTraceColors	= new HashMap(); //список сгенерированных цветов
																												 //в виде (IDтрассы, ЦВЕТ)

	static Col[] defaultcolors = new Col[] {
//		new Col ("desktopColor", Color.lightGray),
//		new Col ("backColor", new Color(241,241,250)),
//		new Col ("textColor", Color.black),
//		new Col ("tableGridColor", Color.black),
//		new Col ("scaleColor", Color.lightGray),
//		new Col ("scaleDigitColor", Color.black),
		new Col ("alarmColor", new Color (255,51,51)),
//		new Col ("selectColor", Color.gray),
		new Col ("analysisMarkerColor", new Color(0, 0, 0)),//133,184,235)),
		new Col ("referencetrace", Color.gray),
//		new Col (RefUpdateEvent.PRIMARY_TRACE, Color.blue),
		new Col ("modeledtrace", Color.blue),
		new Col ("wavelet", Color.blue),
		new Col ("etalon", Color.darkGray),
	};
	static Col[] traceanalysiscolors = new Col[] {
		new Col ("connectColor", new Color (64, 180, 255)), // Color.cyan
		new Col ("reflectColor", new Color (64, 180, 255)), // Color.cyan
		new Col ("deadzoneColor", new Color(192,32,255)),
		new Col ("weldColor", new Color(0, 220, 16)),//new Color(32, 255, 255)), //Color.magenta
		new Col ("upweldColor", new Color(0, 220, 16)),//new Color(32, 255, 255)),//Color.magenta
		new Col ("nonidColor", new Color (200,9,0)),
		new Col ("linezoneColor", new Color(0, 0, 255)), //Color.blue
		new Col ("endColor", new Color(160,32,255)),
		new Col ("noiseColor", new Color (160, 160, 160)),
		new Col ("markeventColor", Color.gray),
		new Col ("markflashColor", new Color(133,184,235)),
		new Col ("analyseBarColor", Color.white),
		new Col ("analyseBarTextColor", Color.black),
		new Col ("dataWriteColor", Color.black),
		new Col ("minTraceLevelColor", new Color(255, 64, 64)),
	};

	static
	{
		// забиваем дефолтные значения.
		for (int i=0; i<defaultcolors.length; i++)
			traceColorTable.put(defaultcolors[i].name, defaultcolors[i].color);
		for (int i=0; i<traceanalysiscolors.length; i++)
			traceColorTable.put(traceanalysiscolors[i].name, traceanalysiscolors[i].color);

		properties = new Properties();
		try
		{
			properties.load(new FileInputStream(propertiesFileName));
			String key;
			String value;
			// если в инишнике есть другие значения - пишем их вместо дефолтных
			Iterator keys = properties.keySet().iterator();
			while (keys.hasNext())
			{
				key = (String)keys.next();
				value = properties.getProperty(key);
				if (value.length() != 0)
					traceColorTable.put(key, new Color(Integer.parseInt(value)));
			}
		}
		catch (IOException ex)
		{
		}
		// забиваем в freeTraceColors доступные цвета для рефлектограмм
	}

	static public Color getColor(String id)
	{
		if (id.equals("random"))
			return new Color ((float)Math.random(), (float)Math.random(), (float)Math.random());

		String colId;
		Color col = (Color)traceColorTable.get(id);

		// если traceColorTable содержит id - возвращаем соответствующий цвет
		if (col != null)
			return col;

		// в противном случае нужно выдать цвет для произвольно добавленной трассы
		// сначала ищем в списке выданных цветов
		if (!busyTraceColors.isEmpty())
		{
			colId = (String)busyTraceColors.get(id);
			if (colId != null)
				return (Color)traceColorTable.get(colId);
		}

		if (!randomTraceColors.isEmpty())
		{
			col = (Color)randomTraceColors.get(id);
			if (col != null)
				return col;
		}

		//пытаемся найти свободный цвет в списке
		if (!freeTraceColors.isEmpty())
		{
			colId = (String)freeTraceColors.keySet().iterator().next();
			busyTraceColors.put(id, colId);
			freeTraceColors.remove(colId);
			return (Color)traceColorTable.get(colId);
		}

		//если ничего не помогло генерим произвольный цвет
		col = new Color ((float)Math.random(), (float)Math.random(), (float)Math.random());
		randomTraceColors.put(id, col);
		return col;
	}

	public static void releaseAllColors()
	{
		freeTraceColors.clear();
		busyTraceColors.clear();
		randomTraceColors.clear();

	}

	public static void releaseColor(String id)
	{
		String colId;
		//если цвет в списке занятых, переносим его в свободные
		if (!busyTraceColors.isEmpty())
		{
			colId = (String)busyTraceColors.get(id);
			if (colId != null)
				{
					freeTraceColors.put(colId, traceColorTable.get(colId));
					busyTraceColors.remove(id);
					return;
				}
		}

		//иначе просто откидываем
		randomTraceColors.remove(id);
	}

	public static void setColor(String id, Color newColor)
	{
		if (traceColorTable.containsKey(id))
			traceColorTable.put(id, newColor);
		if (freeTraceColors.containsKey(id))
			freeTraceColors.put(id, newColor);
	}

	public static void resetToDefaults()
	{
		for (int i=0; i<defaultcolors.length; i++)
			traceColorTable.put(defaultcolors[i].name, defaultcolors[i].color);
		for (int i=0; i<traceanalysiscolors.length; i++)
			traceColorTable.put(traceanalysiscolors[i].name, traceanalysiscolors[i].color);
	}

	public static void saveIni()
	{
		Iterator keys = traceColorTable.keySet().iterator();
		String key;
		try
		{
			while (keys.hasNext())
			{
				key = (String)keys.next();
				properties.setProperty(key, String.valueOf(((Color)traceColorTable.get(key)).getRGB()));
			}
			properties.store(new FileOutputStream(propertiesFileName), null);
		}
		catch (IOException ex)
		{
		}
	}
}