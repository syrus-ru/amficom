/**
 * $Id: RemoveCollectorCommandAtomic.java,v 1.2 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * �������� ���� �� ����� - ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveCollectorCommandAtomic extends MapActionCommand
{
	MapPipePathElement collector;
	
	public RemoveCollectorCommandAtomic(MapPipePathElement collector)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.collector = collector;
	}
	
	public MapPipePathElement getCollector()
	{
		return collector;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);
	}
}

