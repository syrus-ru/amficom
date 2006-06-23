/*-
 * $Id: ValidateMapCommand.java,v 1.1 2006/06/23 14:02:00 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

public class ValidateMapCommand extends AbstractCommand {
	JDesktopPane desktop;
	ApplicationContext aContext;
	
	public ValidateMapCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}
	
	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;
		MapView mapView = mapFrame.getMapView();
		if(mapView == null)
			return;
		
		List<String> messages = new ArrayList<String>();

		Set<SchemeCableLink> unboundCables = new HashSet<SchemeCableLink>();
		for (UnboundLink link : mapView.getUnboundLinks()) {
			unboundCables.add(link.getCablePath().getSchemeCableLink());
		}
		for (SchemeCableLink link : unboundCables) {
			messages.add(I18N.getString("Message.Error.unbound_region") + " " + link.getName());
		}
		
		if (messages.isEmpty()) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					I18N.getString("Message.Information.map_valid"), 
					I18N.getString("Message.Information"),
					JOptionPane.INFORMATION_MESSAGE);	
		} else {
			JList messagesList = new JList(messages.toArray(new String[messages.size()]));
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(), 
					new JScrollPane(messagesList), 
					I18N.getString("Message.Information"),
					JOptionPane.INFORMATION_MESSAGE);
		}

//		Set<String> noPipeBlocks = new HashSet<String>();
//		for (PhysicalLink link :mapView.getAllPhysicalLinks()) {
//			final Set<PipeBlock> pipeBlocks = link.getBinding().getPipeBlocks();
//			if(pipeBlocks.size() > 0) {
//				noPipeBlocks.add(link.getName());
//			}
//		}
//		if (!noPipeBlocks.isEmpty()) {
//			System.err.println("Links without PipeBlocks : ");
//			System.err.println(noPipeBlocks);
//		}
	}
}
