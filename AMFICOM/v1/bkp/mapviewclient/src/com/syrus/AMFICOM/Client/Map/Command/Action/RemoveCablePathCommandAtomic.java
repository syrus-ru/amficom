/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * �������� ���������� ����� �� ����� - ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand
{
	MapCablePathElement cp;
	
	public RemoveCablePathCommandAtomic(MapCablePathElement cp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cp = cp;
	}
	
	public MapCablePathElement getPath()
	{
		return cp;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().removeCablePath(cp);
		Pool.remove(MapCablePathElement.typ, cp.getId());
	}

	public void redo()
	{
		logicalNetLayer.getMapView().removeCablePath(cp);
		Pool.remove(MapCablePathElement.typ, cp.getId());
	}

	public void undo()
	{
		logicalNetLayer.getMapView().addCablePath(cp);
		Pool.put(MapCablePathElement.typ, cp.getId(), cp);
	}
}

