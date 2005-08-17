/**
 * $Id: MapEditorRemoveLinkTypeCommand.java,v 1.2 2005/08/17 14:14:18 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
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
		PhysicalLinkType physicalLinkType = (PhysicalLinkType )WrapperedComboChooserDialog.showChooserDialog(
				LinkTypeController.getTopologicalLinkTypes());

		if(physicalLinkType == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		physicalLinkType.getMapLibrary().removeChild(physicalLinkType);
		StorableObjectPool.delete(physicalLinkType.getId());
		setResult(Command.RESULT_OK);
	}
}
