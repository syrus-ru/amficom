/**
 * $Id: MapEditorRemoveSiteTypeCommand.java,v 1.5 2005/09/04 17:17:20 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;

public class MapEditorRemoveSiteTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorRemoveSiteTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		Collection<SiteNodeType> topologicalNodeTypes = NodeTypeController.getTopologicalNodeTypes(mapFrame.getMapView().getMap());
		topologicalNodeTypes.removeAll(MapLibraryController.getDefaultMapLibrary().getSiteNodeTypes());
		SiteNodeType siteNodeType = (SiteNodeType )WrapperedComboChooserDialog.showChooserDialog(
				topologicalNodeTypes);

		if(siteNodeType == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

//		siteNodeType.getMapLibrary().removeChild(siteNodeType);
		StorableObjectPool.delete(siteNodeType.getId());
		try {
			StorableObjectPool.flush(siteNodeType, LoginManager.getUserId(), true);
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
		setResult(Command.RESULT_OK);
	}
}
