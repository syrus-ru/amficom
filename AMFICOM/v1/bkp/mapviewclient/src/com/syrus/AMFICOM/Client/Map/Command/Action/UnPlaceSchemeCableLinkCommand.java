/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.11 2005/03/28 08:25:11 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * ������ ��������� ���� � ��������� �� �����
 * 
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/03/28 08:25:11 $
 * @module mapviewclient_v1
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	CablePath cablePath = null;
	
	public UnPlaceSchemeCableLinkCommand(CablePath cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		try {
			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
			scl.setCableChannelingItemsAsArray(null);//.clear();
			List ccis = new LinkedList();
			for(Iterator it = this.cablePath.getLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				if(link instanceof UnboundLink)
					continue;
				ccis.add(this.cablePath.getBinding().getCCI(link));
//			scl.channelingItems.add(cablePath.getBinding().getCCI(link));
			}
			scl.setCableChannelingItemsAsArray((CableChannelingItem [])ccis.toArray(new CableChannelingItem [0]));
			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
