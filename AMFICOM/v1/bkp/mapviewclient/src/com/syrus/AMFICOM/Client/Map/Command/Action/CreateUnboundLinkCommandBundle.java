/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.5 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * �������� ������������� �����, ��������� �� ������ ���������, 
 * �������� �� � ��� � �� �����
 *  
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle
{
	UnboundLink unbound;
	
	AbstractNode startNode;
	AbstractNode endNode;

	public CreateUnboundLinkCommandBundle(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public UnboundLink getUnbound()
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

