/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.2 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;

/**
 * �������� ������������� �����, ��������� �� ������ ���������, 
 * �������� �� � ��� � �� �����
 *  
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle
{
	MapUnboundLinkElement unbound;
	
	MapNodeElement startNode;
	MapNodeElement endNode;

	public CreateUnboundLinkCommandBundle(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapUnboundLinkElement getUnbound()
	{
		return unbound;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		unbound = super.createUnboundLinkWithNodeLink(startNode, endNode);
	}
	
}

