/**
 * $Id: MapState.java,v 1.6 2005/01/21 16:19:57 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

/**
 * ��������� ����� � ������������� ������ ������� ������� ��:
 * 	��������� ���� (mouseMode)
 *  ���������� ����� (operationMode)
 *  �������� ��� ���������� � ������� ������ (actionMode)
 *  ����� ����������� ����� (showMode)
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
	//����� ��������� �������� ��� ������

	/** ������ ����� */
	public static final int NULL_ACTION_MODE = 100;
	/** ������� alt ������ (����� ���������� PhysicalNodeElement) */
	public static final int ALT_LINK_ACTION_MODE = 101;
	/** ����� �����������. ������� ctrl ������ */
	public static final int MOVE_ACTION_MODE = 102;
	/**  */
	public static final int DRAW_ACTION_MODE = 103;
	/** ����� ������. ������� Shift ������ */
	public static final int SELECT_ACTION_MODE = 104;
	/** ����� ������ ������� */
	public static final int SELECT_MARKER_ACTION_MODE = 105;
	/** ����� ��������� ����� (NodeLink � �.�.) */
	public static final int DRAW_LINES_ACTION_MODE = 106;
	
	/** ���� ��������� ������� */
	protected int actionMode = NULL_ACTION_MODE;

	//����� ��������� ����
	public static final int MOUSE_NONE = 200;
	public static final int MOUSE_PRESSED = 301;
	public static final int MOUSE_RELEASED = 302;
	public static final int MOUSE_MOVED = 303;
	public static final int MOUSE_DRAGGED = 304;

	/** ���� ��������� ���� */
	protected int mouseMode = MOUSE_NONE;

	//����� ��������� ������ ��� ������ � ������
	public static final int NO_OPERATION = 300;
	public static final int ZOOM_TO_POINT = 301;
	public static final int ZOOM_TO_RECT = 302;
	public static final int MOVE_TO_CENTER = 303;
	public static final int MOVE_HAND = 304;
	public static final int NODELINK_SIZE_EDIT = 305;
	public static final int MEASURE_DISTANCE = 306;
	public static final int MOVE_FIXDIST = 307;

	/** ���� ��������� ������� ��� ������ � ������ */
	protected int operationMode = NO_OPERATION;

	// ������ ����������� ����� �� �����
	/** ���������� ��������� ����� */
	public static final int SHOW_NODE_LINK = 1;
	/** ���������� ���������� ����� */
	public static final int SHOW_PHYSICAL_LINK = 2;
	/** ���������� ��������� ������� */
	public static final int SHOW_CABLE_PATH = 3;
	/** ���������� ���� */
	public static final int SHOW_MEASUREMENT_PATH = 4;

	/** ���� ������ ����������� ����� �� ����� */
	public int showMode = SHOW_PHYSICAL_LINK;

	/**
	 * �������� ���������� mode
	 */
	public int getMouseMode()
	{
		return mouseMode;
	}

	/**
	 * ���������� ���������� mode
	 */
	public void setMouseMode(int mode)
	{
		mouseMode = mode;
	}

	/**
	 * �������� ���������� actionMode
	 */
	public int getActionMode()
	{
		return actionMode;
	}

	/**
	 * ���������� ���������� actionMode
	 */
	public void setActionMode(int mode)
	{
		actionMode = mode;
	}

	/**
	 * �������� ���������� actionMode
	 */
	public int getOperationMode()
	{
		return operationMode;
	}

	/**
	 * ���������� ���������� actionMode
	 */
	public void setOperationMode(int mode)
	{
		operationMode = mode;
	}

	/**
	 * �������� ���������� actionMode
	 */
	public int getShowMode()
	{
		return showMode;
	}

	/**
	 * ���������� ���������� actionMode
	 */
	public void setShowMode(int mode)
	{
		showMode = mode;
	}


}
