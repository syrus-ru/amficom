/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.1 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.util.HashMap;

/**
 * �������� ���������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCollectorCommandAtomic extends MapActionCommand
{
	MapPipePathElement collector;

	String name;
	
	public CreateCollectorCommandAtomic(String name)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.name = name;
	}
	
	public MapPipePathElement getCollector()
	{
		return collector;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		
		collector = new MapPipePathElement(
				dataSource.GetUId( MapPipePathElement.typ ),
				name,
				logicalNetLayer.getMapView().getMap());
		
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);

		logicalNetLayer.getMapView().getMap().addCollector(collector);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
}

