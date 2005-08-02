/**
 * $Id: MapEditorNewSiteTypeCommand.java,v 1.1 2005/08/02 07:22:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNodeTypeSort;

public class MapEditorNewSiteTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorNewSiteTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		try {
			SiteNodeType siteNodeType = SiteNodeType.createInstance(
					LoginManager.getUserId(), 
					SiteNodeTypeSort.BUILDING, 
					"codename",
					"Новый",
					"",
					null,//todo default ImageResource
					true);
			if(EditorDialog.showEditorDialog(
					LangModelMap.getString("type"),
					siteNodeType,
					new SiteNodeTypeEditor()) ) {
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
