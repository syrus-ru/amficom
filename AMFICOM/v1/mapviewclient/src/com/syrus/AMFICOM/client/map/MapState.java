/*-
 * $$Id: MapState.java,v 1.17 2005/10/31 12:30:07 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * Состояние карты. В фиксированный момент времени состоит из: - состояние мыши
 * (mouseMode) - включенный режим (operationMode) - действие над элементами в
 * нулевом режиме (actionMode) -режим отображения линий (showMode)
 * 
 * @version $Revision: 1.17 $, $Date: 2005/10/31 12:30:07 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapState {
	// Флаги дополнительного состояния действий над картой

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

	// Флаги состояния мыши
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

	// Флаги состояния режмов для работы с картой
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
	/** Режи навигации. */
	public static final int NAVIGATE = 308;

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

	static Map<Integer, String> actionModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> operationModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> mouseModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> showModeNames = new HashMap<Integer, String>();

	static {
		actionModeNames.put(Integer.valueOf(NULL_ACTION_MODE), "NULL_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(ALT_LINK_ACTION_MODE), "ALT_LINK_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(MOVE_ACTION_MODE), "MOVE_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(DRAW_ACTION_MODE), "DRAW_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(SELECT_ACTION_MODE), "SELECT_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(SELECT_MARKER_ACTION_MODE), "SELECT_MARKER_ACTION_MODE"); //$NON-NLS-1$
		actionModeNames.put(Integer.valueOf(DRAW_LINES_ACTION_MODE), "DRAW_LINES_ACTION_MODE"); //$NON-NLS-1$

		operationModeNames.put(Integer.valueOf(NO_OPERATION), "NO_OPERATION"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(ZOOM_TO_POINT), "ZOOM_TO_POINT"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(ZOOM_TO_RECT), "ZOOM_TO_RECT"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(MOVE_TO_CENTER), "MOVE_TO_CENTER"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(MOVE_HAND), "MOVE_HAND"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(NODELINK_SIZE_EDIT), "NODELINK_SIZE_EDIT"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(MEASURE_DISTANCE), "MEASURE_DISTANCE"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(MOVE_FIXDIST), "MOVE_FIXDIST"); //$NON-NLS-1$
		operationModeNames.put(Integer.valueOf(NAVIGATE), "NAVIGATE"); //$NON-NLS-1$

		mouseModeNames.put(Integer.valueOf(MOUSE_NONE), "MOUSE_NONE"); //$NON-NLS-1$
		mouseModeNames.put(Integer.valueOf(MOUSE_PRESSED), "MOUSE_PRESSED"); //$NON-NLS-1$
		mouseModeNames.put(Integer.valueOf(MOUSE_RELEASED), "MOUSE_RELEASED"); //$NON-NLS-1$
		mouseModeNames.put(Integer.valueOf(MOUSE_MOVED), "MOUSE_MOVED"); //$NON-NLS-1$
		mouseModeNames.put(Integer.valueOf(MOUSE_DRAGGED), "MOUSE_DRAGGED"); //$NON-NLS-1$

		showModeNames.put(Integer.valueOf(SHOW_NODE_LINK), "SHOW_NODE_LINK"); //$NON-NLS-1$
		showModeNames.put(Integer.valueOf(SHOW_PHYSICAL_LINK), "SHOW_PHYSICAL_LINK"); //$NON-NLS-1$
		showModeNames.put(Integer.valueOf(SHOW_CABLE_PATH), "SHOW_CABLE_PATH"); //$NON-NLS-1$
		showModeNames.put(Integer.valueOf(SHOW_MEASUREMENT_PATH), "SHOW_MEASUREMENT_PATH"); //$NON-NLS-1$
	}

	/**
	 * Получить переменную режим мыши.
	 * 
	 * @return режим мыши
	 */
	public int getMouseMode() {
		return this.mouseMode;
	}

	/**
	 * Установить режим мыши.
	 * 
	 * @param mode режим мыши
	 */
	public void setMouseMode(int mode) {
		this.mouseMode = mode;
		Log.debugMessage("Set mouse mode " + mouseModeToString(), Level.FINEST); //$NON-NLS-1$
	}

	/**
	 * Получить дополнительное состояние действий.
	 * 
	 * @return дополнительное состояние действий
	 */
	public int getActionMode() {
		return this.actionMode;
	}

	/**
	 * Установить дополнительное состояние действий.
	 * 
	 * @param mode дополнительное состояние действий
	 */
	public void setActionMode(int mode) {
		this.actionMode = mode;
		Log.debugMessage("Set action mode " + actionModeToString(), Level.FINEST); //$NON-NLS-1$
	}

	/**
	 * Получить режим карты.
	 * 
	 * @return режим карты
	 */
	public int getOperationMode() {
		return this.operationMode;
	}

	/**
	 * Установить режим карты.
	 * 
	 * @param mode режим карты
	 */
	public void setOperationMode(int mode) {
		this.operationMode = mode;
		Log.debugMessage("Set operation mode " + operationModeToString(), Level.FINEST); //$NON-NLS-1$
	}

	/**
	 * Получить режим отображения.
	 * 
	 * @return режим отображения
	 */
	public int getShowMode() {
		return this.showMode;
	}

	/**
	 * Установить режим отображения.
	 * 
	 * @param mode режим отображения
	 */
	public void setShowMode(int mode) {
		this.showMode = mode;
		Log.debugMessage("Set show mode " + showModeToString(), Level.FINEST); //$NON-NLS-1$
	}

	public String actionModeToString() {
		return actionModeNames.get(Integer.valueOf(this.actionMode));
	}

	public String operationModeToString() {
		return operationModeNames.get(Integer.valueOf(this.operationMode));
	}

	public String mouseModeToString() {
		return mouseModeNames.get(Integer.valueOf(this.mouseMode));
	}

	public String showModeToString() {
		return showModeNames.get(Integer.valueOf(this.showMode));
	}

	@Override
	public String toString() {
		return "MapState [action = " + actionModeToString()  //$NON-NLS-1$
				+ ", operation = " + operationModeToString()  //$NON-NLS-1$
				+ ", mouse = " + mouseModeToString() //$NON-NLS-1$
				+ ", show = " + showModeToString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
