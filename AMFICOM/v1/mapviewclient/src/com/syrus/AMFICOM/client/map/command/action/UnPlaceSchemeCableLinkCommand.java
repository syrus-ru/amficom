/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.20 2005/07/19 13:11:11 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * ������ ��������� ���� � ��������� �� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/07/19 13:11:11 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
			// OLD
//			final SortedSet<CableChannelingItem> cableChannelingItems = scl.getCableChannelingItems();
//			if (!cableChannelingItems.isEmpty()) {
//				cableChannelingItems.first().setParentSchemeCableLink(null);
//			}
			// NEW
			for (final CableChannelingItem cableChannelingItem : scl.getCableChannelingItems()) {
				cableChannelingItem.setParentSchemeCableLink(null);
//				scl.removeCableChannelingItem(cableChannelingItem);
			}

			List ccis = new LinkedList();
			for(Iterator it = this.cablePath.getLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				if(link instanceof UnboundLink)
					continue;
				ccis.add(this.cablePath.getBinding().getCCI(link));
//			scl.channelingItems.add(cablePath.getBinding().getCCI(link));
			}
			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
