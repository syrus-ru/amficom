/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.6 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * �������� ������������ ����� �� �����, ������� ��������, ��
 * ������� ��� �������
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class RemoveUnboundLinkCommandBundle extends MapActionCommandBundle
{
	UnboundLink unbound;
	
	public RemoveUnboundLinkCommandBundle(UnboundLink unbound)
	{
		this.unbound = unbound;
	}
	
	public UnboundLink getUnbound()
	{
		return this.unbound;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		super.removeUnboundLink(this.unbound);
	}

}

