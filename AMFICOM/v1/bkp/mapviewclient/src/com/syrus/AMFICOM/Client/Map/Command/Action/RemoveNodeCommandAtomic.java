/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;

/**
 * �������� ���� �� ����� - ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class RemoveNodeCommandAtomic extends MapActionCommand
{
	MapNodeElement node;
	
	public RemoveNodeCommandAtomic(MapNodeElement node)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
	}
	
	public MapNodeElement getNode()
	{
		return node;
	}
	
	public void execute()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
}

