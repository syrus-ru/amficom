/**
 * $Id: MapState.java,v 1.6 2005/01/21 16:19:57 krupenn Exp $
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
 * Состояние карты в фиксированный момент времени состоит из:
 * 	состояние мыши (mouseMode)
 *  включенный режим (operationMode)
 *  действие над элементами в нулевом режиме (actionMode)
 *  режим отображения линий (showMode)
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapState 
{
	//Флаги состояния действий над картой

	/** Пустой режим */
	public static final int NULL_ACTION_MODE = 100;
	/** Клаваша alt нажата (режим добавления PhysicalNodeElement) */
	public static final int ALT_LINK_ACTION_MODE = 101;
	/** Режим перемещения. Клавища ctrl нажата */
	public static final int MOVE_ACTION_MODE = 102;
	/**  */
	public static final int DRAW_ACTION_MODE = 103;
	/** Режив выбора. Клавиша Shift нажата */
	public static final int SELECT_ACTION_MODE = 104;
	/** Режим выбора области */
	public static final int SELECT_MARKER_ACTION_MODE = 105;
	/** Режим рисования линий (NodeLink и т.д.) */
	public static final int DRAW_LINES_ACTION_MODE = 106;
	
	/** Флаг состояния режимов */
	protected int actionMode = NULL_ACTION_MODE;

	//Флаги состояния мыши
	public static final int MOUSE_NONE = 200;
	public static final int MOUSE_PRESSED = 301;
	public static final int MOUSE_RELEASED = 302;
	public static final int MOUSE_MOVED = 303;
	public static final int MOUSE_DRAGGED = 304;

	/** Флаг состояния мыши */
	protected int mouseMode = MOUSE_NONE;

	//Флаги состояния режмов для работы с картой
	public static final int NO_OPERATION = 300;
	public static final int ZOOM_TO_POINT = 301;
	public static final int ZOOM_TO_RECT = 302;
	public static final int MOVE_TO_CENTER = 303;
	public static final int MOVE_HAND = 304;
	public static final int NODELINK_SIZE_EDIT = 305;
	public static final int MEASURE_DISTANCE = 306;
	public static final int MOVE_FIXDIST = 307;

	/** Флаг состояния режимов для работы с картой */
	protected int operationMode = NO_OPERATION;

	// режимы отображения линий на карте
	/** Показывать фрагменты линий */
	public static final int SHOW_NODE_LINK = 1;
	/** Показывать физические линии */
	public static final int SHOW_PHYSICAL_LINK = 2;
	/** Показывать прокладку кабелей */
	public static final int SHOW_CABLE_PATH = 3;
	/** Показывать пути */
	public static final int SHOW_MEASUREMENT_PATH = 4;

	/** Флаг режима отображения линий на карте */
	public int showMode = SHOW_PHYSICAL_LINK;

	/**
	 * Получить переменную mode
	 */
	public int getMouseMode()
	{
		return mouseMode;
	}

	/**
	 * Установить переменную mode
	 */
	public void setMouseMode(int mode)
	{
		mouseMode = mode;
	}

	/**
	 * Получить переменную actionMode
	 */
	public int getActionMode()
	{
		return actionMode;
	}

	/**
	 * Установить переменную actionMode
	 */
	public void setActionMode(int mode)
	{
		actionMode = mode;
	}

	/**
	 * Получить переменную actionMode
	 */
	public int getOperationMode()
	{
		return operationMode;
	}

	/**
	 * Установить переменную actionMode
	 */
	public void setOperationMode(int mode)
	{
		operationMode = mode;
	}

	/**
	 * Получить переменную actionMode
	 */
	public int getShowMode()
	{
		return showMode;
	}

	/**
	 * Установить переменную actionMode
	 */
	public void setShowMode(int mode)
	{
		showMode = mode;
	}


}
