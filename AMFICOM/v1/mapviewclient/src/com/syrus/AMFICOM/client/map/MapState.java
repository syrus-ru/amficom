/**
 * $Id: MapState.java,v 1.2 2004/09/15 08:21:49 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:21:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapState 
{
	//Флаги состояния действий над картой

	/** Пустой режим */
	public final static int NULL_ACTION_MODE = 100;
	/** Клаваша alt нажата (режим добавления PhysicalNodeElement) */
	public final static int ALT_LINK_ACTION_MODE = 101;
	/** Режим перемещения. Клавища ctrl нажата */
	public final static int MOVE_ACTION_MODE = 102;
	/**  */
	public final static int DRAW_ACTION_MODE = 103;
	/** Режив выбора. Клавиша Shift нажата */
	public final static int SELECT_ACTION_MODE = 104;
	/** Режим выбора области */
	public final static int SELECT_MARKER_ACTION_MODE = 105;
	/** Режим рисования линий (NodeLink и т.д.) */
	public final static int DRAW_LINES_ACTION_MODE = 106;
	/** Режим рисования линий с фиксированной дистанцией до соседнего узла */
	public final static int FIXDIST_ACTION_MODE = 107;
	
	/** Флаг состояния режимов */
	protected int actionMode = NULL_ACTION_MODE;

	//Флаги состояния мыши
	public final static int MOUSE_NONE = 200;
	public final static int MOUSE_PRESSED = 301;
	public final static int MOUSE_RELEASED = 302;
	public final static int MOUSE_MOVED = 303;
	public final static int MOUSE_DRAGGED = 304;

	/** Флаг состояния мыши */
	protected int mouseMode = MOUSE_NONE;

	//Флаги состояния режмов для работы с картой
	public final static int NO_OPERATION = 300;
	public final static int ZOOM_TO_POINT = 301;
	public final static int ZOOM_TO_RECT = 302;
	public final static int MOVE_TO_CENTER = 303;
	public final static int MOVE_HAND = 304;
	public final static int NODELINK_SIZE_EDIT = 305;

	/** Флаг состояния режимов для работы с картой */
	protected int operationMode = NO_OPERATION;

	// режимы отображения линий на карте
	/** Показывать фрагменты линий */
	static final public int SHOW_NODE_LINK = 1;
	/** Показывать физические линии */
	static final public int SHOW_PHYSICAL_LINK = 2;
	/** Показывать прокладку кабелей */
	static final public int SHOW_CABLE_PATH = 3;
	/** Показывать пути */
	static final public int SHOW_TRANSMISSION_PATH = 4;

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
