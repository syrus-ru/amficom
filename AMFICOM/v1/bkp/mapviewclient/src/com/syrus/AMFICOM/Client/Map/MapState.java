/**
 * $Id: MapState.java,v 1.7 2005/01/30 15:38:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

/**
 * Состояние карты. В фиксированный момент времени состоит из:
 * 	- состояние мыши (mouseMode)
 *  - включенный режим (operationMode)
 *  - действие над элементами в нулевом режиме (actionMode)
 *   -режим отображения линий (showMode)
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/01/30 15:38:17 $
 * @module mapviewclient_v1
 */
public final class MapState 
{
	//Флаги дополнительного состояния действий над картой

	/** Пустой режим. */
	public static final int NULL_ACTION_MODE = 100;
	/** Клаваша alt нажата (режим добавления топологических узлов). */
	public static final int ALT_LINK_ACTION_MODE = 101;
	/** Режим перемещения. Клавища ctrl нажата. */
	public static final int MOVE_ACTION_MODE = 102;
	/** Режим рисования линиий. */
	public static final int DRAW_ACTION_MODE = 103;
	/** Режим выбора. Клавиша Shift нажата. */
	public static final int SELECT_ACTION_MODE = 104;
	/** Режим выбора области. */
	public static final int SELECT_MARKER_ACTION_MODE = 105;
	/** Режим рисования линий (NodeLink и т.д.). */
	public static final int DRAW_LINES_ACTION_MODE = 106;
	
	/** Флаг дополнительного состояния действий. */
	protected int actionMode = NULL_ACTION_MODE;

	//Флаги состояния мыши
	/** с мышью ничего не происходит. */
	public static final int MOUSE_NONE = 200;
	/** мышь нажата. */
	public static final int MOUSE_PRESSED = 301;
	/** мышь отпущена. */
	public static final int MOUSE_RELEASED = 302;
	/** мышь перемещается. */
	public static final int MOUSE_MOVED = 303;
	/** мышь тащится. */
	public static final int MOUSE_DRAGGED = 304;

	/** Флаг состояния мыши. */
	protected int mouseMode = MOUSE_NONE;

	//Флаги состояния режмов для работы с картой
	/** Пустой режим. */
	public static final int NO_OPERATION = 300;
	/** Режим приближения точки. */
	public static final int ZOOM_TO_POINT = 301;
	/** Режим приближения области. */
	public static final int ZOOM_TO_RECT = 302;
	/** Режим центрирования точки. */
	public static final int MOVE_TO_CENTER = 303;
	/** Режим перемещения карты лапкой. */
	public static final int MOVE_HAND = 304;
	/** Режим редактирования длины фрагмента линии. */
	public static final int NODELINK_SIZE_EDIT = 305;
	/** Режим измерения дистанции. */
	public static final int MEASURE_DISTANCE = 306;
	/** Режим перемещения узла с фиксированной длиной фрагмента. */
	public static final int MOVE_FIXDIST = 307;

	/** Флаг состояния режимов для работы с картой. */
	protected int operationMode = NO_OPERATION;

	// режимы отображения линий на карте
	/** Показывать фрагменты линий. */
	public static final int SHOW_NODE_LINK = 1;
	/** Показывать физические линии. */
	public static final int SHOW_PHYSICAL_LINK = 2;
	/** Показывать прокладку кабелей. */
	public static final int SHOW_CABLE_PATH = 3;
	/** Показывать пути. */
	public static final int SHOW_MEASUREMENT_PATH = 4;

	/** Флаг режима отображения линий на карте. */
	protected int showMode = SHOW_PHYSICAL_LINK;

	/**
	 * Получить переменную режим мыши.
	 * @return режим мыши
	 */
	public int getMouseMode()
	{
		return mouseMode;
	}

	/**
	 * Установить режим мыши.
	 * @param mode режим мыши
	 */
	public void setMouseMode(int mode)
	{
		mouseMode = mode;
	}

	/**
	 * Получить дополнительное состояние действий.
	 * @return дополнительное состояние действий
	 */
	public int getActionMode()
	{
		return actionMode;
	}

	/**
	 * Установить дополнительное состояние действий.
	 * @param mode дополнительное состояние действий
	 */
	public void setActionMode(int mode)
	{
		actionMode = mode;
	}

	/**
	 * Получить режим карты.
	 * @return режим карты
	 */
	public int getOperationMode()
	{
		return operationMode;
	}

	/**
	 * Установить режим карты.
	 * @param mode режим карты
	 */
	public void setOperationMode(int mode)
	{
		operationMode = mode;
	}

	/**
	 * Получить режим отображения.
	 * @return режим отображения
	 */
	public int getShowMode()
	{
		return showMode;
	}

	/**
	 * Установить режим отображения.
	 * @param mode режим отображения
	 */
	public void setShowMode(int mode)
	{
		showMode = mode;
	}


}
