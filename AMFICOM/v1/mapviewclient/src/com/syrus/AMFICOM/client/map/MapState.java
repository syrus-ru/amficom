/**
 * $Id: MapState.java,v 1.2 2004/09/15 08:21:49 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:21:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapState 
{
	//����� ��������� �������� ��� ������

	/** ������ ����� */
	public final static int NULL_ACTION_MODE = 100;
	/** ������� alt ������ (����� ���������� PhysicalNodeElement) */
	public final static int ALT_LINK_ACTION_MODE = 101;
	/** ����� �����������. ������� ctrl ������ */
	public final static int MOVE_ACTION_MODE = 102;
	/**  */
	public final static int DRAW_ACTION_MODE = 103;
	/** ����� ������. ������� Shift ������ */
	public final static int SELECT_ACTION_MODE = 104;
	/** ����� ������ ������� */
	public final static int SELECT_MARKER_ACTION_MODE = 105;
	/** ����� ��������� ����� (NodeLink � �.�.) */
	public final static int DRAW_LINES_ACTION_MODE = 106;
	/** ����� ��������� ����� � ������������� ���������� �� ��������� ���� */
	public final static int FIXDIST_ACTION_MODE = 107;
	
	/** ���� ��������� ������� */
	protected int actionMode = NULL_ACTION_MODE;

	//����� ��������� ����
	public final static int MOUSE_NONE = 200;
	public final static int MOUSE_PRESSED = 301;
	public final static int MOUSE_RELEASED = 302;
	public final static int MOUSE_MOVED = 303;
	public final static int MOUSE_DRAGGED = 304;

	/** ���� ��������� ���� */
	protected int mouseMode = MOUSE_NONE;

	//����� ��������� ������ ��� ������ � ������
	public final static int NO_OPERATION = 300;
	public final static int ZOOM_TO_POINT = 301;
	public final static int ZOOM_TO_RECT = 302;
	public final static int MOVE_TO_CENTER = 303;
	public final static int MOVE_HAND = 304;
	public final static int NODELINK_SIZE_EDIT = 305;

	/** ���� ��������� ������� ��� ������ � ������ */
	protected int operationMode = NO_OPERATION;

	// ������ ����������� ����� �� �����
	/** ���������� ��������� ����� */
	static final public int SHOW_NODE_LINK = 1;
	/** ���������� ���������� ����� */
	static final public int SHOW_PHYSICAL_LINK = 2;
	/** ���������� ��������� ������� */
	static final public int SHOW_CABLE_PATH = 3;
	/** ���������� ���� */
	static final public int SHOW_TRANSMISSION_PATH = 4;

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
