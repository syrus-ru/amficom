/**
 * $Id: MapState.java,v 1.13 2005/09/08 18:22:41 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * ��������� �����. � ������������� ������ ������� ������� ��: - ��������� ����
 * (mouseMode) - ���������� ����� (operationMode) - �������� ��� ���������� �
 * ������� ������ (actionMode) -����� ����������� ����� (showMode)
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/09/08 18:22:41 $
 * @module mapviewclient
 */
public final class MapState {
	// ����� ��������������� ��������� �������� ��� ������

	/** ������ �����. */
	public static final int NULL_ACTION_MODE = 100;
	/** ������� alt ������ (����� ���������� �������������� �����). */
	public static final int ALT_LINK_ACTION_MODE = 101;
	/** ����� �����������. ������� ctrl ������. */
	public static final int MOVE_ACTION_MODE = 102;
	/** ����� ��������� ������. */
	public static final int DRAW_ACTION_MODE = 103;
	/** ����� ������. ������� Shift ������. */
	public static final int SELECT_ACTION_MODE = 104;
	/** ����� ������ �������. */
	public static final int SELECT_MARKER_ACTION_MODE = 105;
	/** ����� ��������� ����� (NodeLink � �.�.). */
	public static final int DRAW_LINES_ACTION_MODE = 106;

	/** ���� ��������������� ��������� ��������. */
	protected int actionMode = NULL_ACTION_MODE;

	// ����� ��������� ����
	/** � ����� ������ �� ����������. */
	public static final int MOUSE_NONE = 200;
	/** ���� ������. */
	public static final int MOUSE_PRESSED = 301;
	/** ���� ��������. */
	public static final int MOUSE_RELEASED = 302;
	/** ���� ������������. */
	public static final int MOUSE_MOVED = 303;
	/** ���� �������. */
	public static final int MOUSE_DRAGGED = 304;

	/** ���� ��������� ����. */
	protected int mouseMode = MOUSE_NONE;

	// ����� ��������� ������ ��� ������ � ������
	/** ������ �����. */
	public static final int NO_OPERATION = 300;
	/** ����� ����������� �����. */
	public static final int ZOOM_TO_POINT = 301;
	/** ����� ����������� �������. */
	public static final int ZOOM_TO_RECT = 302;
	/** ����� ������������� �����. */
	public static final int MOVE_TO_CENTER = 303;
	/** ����� ����������� ����� ������. */
	public static final int MOVE_HAND = 304;
	/** ����� �������������� ����� ��������� �����. */
	public static final int NODELINK_SIZE_EDIT = 305;
	/** ����� ��������� ���������. */
	public static final int MEASURE_DISTANCE = 306;
	/** ����� ����������� ���� � ������������� ������ ���������. */
	public static final int MOVE_FIXDIST = 307;
	/** ���� ���������. */
	public static final int NAVIGATE = 308;

	/** ���� ��������� ������� ��� ������ � ������. */
	protected int operationMode = NO_OPERATION;

	// ������ ����������� ����� �� �����
	/** ���������� ��������� �����. */
	public static final int SHOW_NODE_LINK = 1;
	/** ���������� ���������� �����. */
	public static final int SHOW_PHYSICAL_LINK = 2;
	/** ���������� ��������� �������. */
	public static final int SHOW_CABLE_PATH = 3;
	/** ���������� ����. */
	public static final int SHOW_MEASUREMENT_PATH = 4;

	/** ���� ������ ����������� ����� �� �����. */
	protected int showMode = SHOW_PHYSICAL_LINK;

	static Map<Integer, String> actionModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> operationModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> mouseModeNames = new HashMap<Integer, String>();
	static Map<Integer, String> showModeNames = new HashMap<Integer, String>();

	static {
		actionModeNames.put(Integer.valueOf(NULL_ACTION_MODE), "NULL_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(ALT_LINK_ACTION_MODE), "ALT_LINK_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(MOVE_ACTION_MODE), "MOVE_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(DRAW_ACTION_MODE), "DRAW_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(SELECT_ACTION_MODE), "SELECT_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(SELECT_MARKER_ACTION_MODE), "SELECT_MARKER_ACTION_MODE");
		actionModeNames.put(Integer.valueOf(DRAW_LINES_ACTION_MODE), "DRAW_LINES_ACTION_MODE");

		operationModeNames.put(Integer.valueOf(NO_OPERATION), "NO_OPERATION");
		operationModeNames.put(Integer.valueOf(ZOOM_TO_POINT), "ZOOM_TO_POINT");
		operationModeNames.put(Integer.valueOf(ZOOM_TO_RECT), "ZOOM_TO_RECT");
		operationModeNames.put(Integer.valueOf(MOVE_TO_CENTER), "MOVE_TO_CENTER");
		operationModeNames.put(Integer.valueOf(MOVE_HAND), "MOVE_HAND");
		operationModeNames.put(Integer.valueOf(NODELINK_SIZE_EDIT), "NODELINK_SIZE_EDIT");
		operationModeNames.put(Integer.valueOf(MEASURE_DISTANCE), "MEASURE_DISTANCE");
		operationModeNames.put(Integer.valueOf(MOVE_FIXDIST), "MOVE_FIXDIST");
		operationModeNames.put(Integer.valueOf(NAVIGATE), "NAVIGATE");

		mouseModeNames.put(Integer.valueOf(MOUSE_NONE), "MOUSE_NONE");
		mouseModeNames.put(Integer.valueOf(MOUSE_PRESSED), "MOUSE_PRESSED");
		mouseModeNames.put(Integer.valueOf(MOUSE_RELEASED), "MOUSE_RELEASED");
		mouseModeNames.put(Integer.valueOf(MOUSE_MOVED), "MOUSE_MOVED");
		mouseModeNames.put(Integer.valueOf(MOUSE_DRAGGED), "MOUSE_DRAGGED");

		showModeNames.put(Integer.valueOf(SHOW_NODE_LINK), "SHOW_NODE_LINK");
		showModeNames.put(Integer.valueOf(SHOW_PHYSICAL_LINK), "SHOW_PHYSICAL_LINK");
		showModeNames.put(Integer.valueOf(SHOW_CABLE_PATH), "SHOW_CABLE_PATH");
		showModeNames.put(Integer.valueOf(SHOW_MEASUREMENT_PATH), "SHOW_MEASUREMENT_PATH");
	}

	/**
	 * �������� ���������� ����� ����.
	 * 
	 * @return ����� ����
	 */
	public int getMouseMode() {
		return this.mouseMode;
	}

	/**
	 * ���������� ����� ����.
	 * 
	 * @param mode ����� ����
	 */
	public void setMouseMode(int mode) {
		this.mouseMode = mode;
		Log.debugMessage("Set mouse mode " + mouseModeToString(), Level.FINEST);
	}

	/**
	 * �������� �������������� ��������� ��������.
	 * 
	 * @return �������������� ��������� ��������
	 */
	public int getActionMode() {
		return this.actionMode;
	}

	/**
	 * ���������� �������������� ��������� ��������.
	 * 
	 * @param mode �������������� ��������� ��������
	 */
	public void setActionMode(int mode) {
		this.actionMode = mode;
		Log.debugMessage("Set action mode " + actionModeToString(), Level.FINEST);
	}

	/**
	 * �������� ����� �����.
	 * 
	 * @return ����� �����
	 */
	public int getOperationMode() {
		return this.operationMode;
	}

	/**
	 * ���������� ����� �����.
	 * 
	 * @param mode ����� �����
	 */
	public void setOperationMode(int mode) {
		this.operationMode = mode;
		Log.debugMessage("Set operation mode " + operationModeToString(), Level.FINEST);
	}

	/**
	 * �������� ����� �����������.
	 * 
	 * @return ����� �����������
	 */
	public int getShowMode() {
		return this.showMode;
	}

	/**
	 * ���������� ����� �����������.
	 * 
	 * @param mode ����� �����������
	 */
	public void setShowMode(int mode) {
		this.showMode = mode;
		Log.debugMessage("Set show mode " + showModeToString(), Level.FINEST);
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
		return "MapState [action = " + actionModeToString() 
				+ ", operation = " + operationModeToString() 
				+ ", mouse = " + mouseModeToString()
				+ ", show = " + showModeToString() + "]";
	}

}
