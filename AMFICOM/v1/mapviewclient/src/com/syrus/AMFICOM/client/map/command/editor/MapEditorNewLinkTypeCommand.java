/**
 * $Id: MapEditorNewLinkTypeCommand.java,v 1.5 2005/08/12 12:17:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkTypeEditor;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IntDimension;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;

public class MapEditorNewLinkTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorNewLinkTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		try {
			MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			PhysicalLinkType physicalLinkType = PhysicalLinkType.createInstance(
					LoginManager.getUserId(), 
					PhysicalLinkTypeSort.TUNNEL, 
					"codename",
					"Новый",
					"",
					new IntDimension(1, 1),
					true,
					MapLibraryController.getDefaultMapLibrary().getId());
			physicalLinkType.setCodename(physicalLinkType.getId().toString());
			PhysicalLinkTypeEditor physicalLinkTypeEditor = new PhysicalLinkTypeEditor();
			physicalLinkTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			if(EditorDialog.showEditorDialog(
					LangModelMap.getString("physicallinktype"),
					physicalLinkType,
					physicalLinkTypeEditor) ) {
				StorableObjectPool.flush(physicalLinkType, LoginManager.getUserId(), true);
				setResult(Command.RESULT_OK);
			} else {
				StorableObjectPool.delete(physicalLinkType.getId());
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
