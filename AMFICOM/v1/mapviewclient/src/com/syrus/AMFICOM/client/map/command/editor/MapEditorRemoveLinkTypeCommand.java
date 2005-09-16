/**
 * $Id: MapEditorRemoveLinkTypeCommand.java,v 1.7 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;

public class MapEditorRemoveLinkTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorRemoveLinkTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this, 
							StatusMessageEvent.STATUS_MESSAGE, 
							LangModelMap.getString("StatusMessage.OpenMapFrameFirst"))); //$NON-NLS-1$
			return;
		}
		Collection<PhysicalLinkType> topologicalLinkTypes = LinkTypeController.getTopologicalLinkTypes(mapFrame.getMap());
		topologicalLinkTypes.removeAll(MapLibraryController.getDefaultMapLibrary().getPhysicalLinkTypes());
		PhysicalLinkType physicalLinkType = (PhysicalLinkType )WrapperedComboChooserDialog.showChooserDialog(
				topologicalLinkTypes);

		if(physicalLinkType == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

//		physicalLinkType.getMapLibrary().removeChild(physicalLinkType);
		StorableObjectPool.delete(physicalLinkType.getId());
		try {
			StorableObjectPool.flush(physicalLinkType, LoginManager.getUserId(), true);
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
		setResult(Command.RESULT_OK);
	}
}
