/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.11 2005/03/01 15:37:25 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;

/**
 * �������� ���� �� ����� - ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/03/01 15:37:25 $
 * @module mapviewclient_v1
 */
public class RemoveNodeCommandAtomic extends MapActionCommand
{
	AbstractNode node;
	
	public RemoveNodeCommandAtomic(AbstractNode node)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
	}
	
	public AbstractNode getNode()
	{
		return this.node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		if(this.node instanceof Marker)
		{
			this.logicalNetLayer.getMapView().removeMarker((Marker)this.node);
		}
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
}

