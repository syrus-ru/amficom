/**
 * $Id: CreateUnboundNodeCommandAtomic.java,v 1.19 2005/07/01 16:22:36 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.map.controllers.UnboundNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * ���������� ������� ������� �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/07/01 16:22:36 $
 * @module mapviewclietn_v1
 */
public class CreateUnboundNodeCommandAtomic extends MapActionCommand
{
	/**
	 * ����������� ����
	 */
	UnboundNode unbound;

	SchemeElement schemeElement;	

	Map map;
	
	/**
	 * �������������� �����, � ������� ��������� ����� �������������� ����.
	 * ����� ������������������ �� point
	 */
	DoublePoint coordinatePoint = null;

	public CreateUnboundNodeCommandAtomic(
			SchemeElement se,
			DoublePoint dpoint)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.schemeElement = se;
		this.coordinatePoint = dpoint;
	}

	public UnboundNode getUnbound()
	{
		return this.unbound;
	}

	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		this.map = this.logicalNetLayer.getMapView().getMap();

		try
		{
			// ������� ����� ����
			this.unbound = UnboundNode.createInstance(
				LoginManager.getUserId(),
				this.schemeElement,
				this.coordinatePoint,
				this.logicalNetLayer.getUnboundNodeType());
			
			UnboundNodeController unc = (UnboundNodeController)getLogicalNetLayer().getMapViewController().getController(this.unbound);

			unc.updateScaleCoefficient(this.unbound);
		
			this.map.addNode(this.unbound);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(ApplicationException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void undo()
	{
		this.map.removeNode(this.unbound);
	}
	
	public void redo()
	{
		this.map.addNode(this.unbound);
	}
}
