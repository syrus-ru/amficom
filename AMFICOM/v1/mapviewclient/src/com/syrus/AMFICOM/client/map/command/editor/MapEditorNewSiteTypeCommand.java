/**
 * $Id: MapEditorNewSiteTypeCommand.java,v 1.8 2005/09/16 15:45:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;

public class MapEditorNewSiteTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorNewSiteTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		try {
			MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			if(mapFrame == null) {
				this.aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this, 
								StatusMessageEvent.STATUS_MESSAGE, 
								LangModelMap.getString("StatusMessage.OpenMapFrameFirst"))); //$NON-NLS-1$
				return;
			}
			SiteNodeType siteNodeType = SiteNodeType.createInstance(
					LoginManager.getUserId(), 
					SiteNodeTypeSort.BUILDING, 
					"codename", //$NON-NLS-1$
					LangModelMap.getString("New"), //$NON-NLS-1$
					"", //$NON-NLS-1$
					NodeTypeController.getDefaultImageId(),
					true,
					MapLibraryController.getDefaultMapLibrary().getId());
			siteNodeType.setCodename(siteNodeType.getId().toString());
			SiteNodeTypeEditor siteNodeTypeEditor = new SiteNodeTypeEditor();
			siteNodeTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			if(EditorDialog.showEditorDialog(
					LangModelMap.getString("sitenodetype"), //$NON-NLS-1$
					siteNodeType,
					siteNodeTypeEditor) ) {
				StorableObjectPool.flush(siteNodeType, LoginManager.getUserId(), true);
				setResult(Command.RESULT_OK);
			} else {
				StorableObjectPool.delete(siteNodeType.getId());
				setResult(Command.RESULT_CANCEL);
			}
		} catch(CreateObjectException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		} catch(ApplicationException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}
}
