/**
 * $Id: MoveFixedDistanceCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import java.awt.Point;

/**
 * ������� ��������� ���������� �������������� ���� ������ �������
 * ��������������� ����, ���������� � ��� ���������� �����, ��� ����������
 * ����� ���������
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveFixedDistanceCommand extends MoveSelectionCommandBundle
{

	public MoveFixedDistanceCommand(Point point)
	{
		super(point);
	}

	public MoveFixedDistanceCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}
}
