/*-
 * $$Id: ViewMapAllCommand.java,v 1.20 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.beans.PropertyChangeEvent;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.editor.MapEditorWindowArranger;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;

/**
 * отобразить стандартный набор окон модуля "Редактор топологических схем"
 * 
 * @version $Revision: 1.20 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewMapAllCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	MapApplicationModelFactory factory;

	public ViewMapAllCommand(
			JDesktopPane desktop,
			ApplicationContext aContext,
			MapApplicationModelFactory factory) {
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	@Override
	public void execute() {
		new ViewMapViewNavigatorCommand(this.desktop, this.aContext).execute();
//		new ViewMapControlsCommand(this.desktop, this.aContext).execute();
		new ViewGeneralPropertiesCommand(this.desktop, this.aContext).execute();
		new ViewAdditionalPropertiesCommand(this.desktop, this.aContext).execute();
		new ViewCharacteristicsCommand(this.desktop, this.aContext).execute();
		new ViewMapWindowCommand(this.desktop, this.aContext, this.factory).execute();

		this.aContext.getDispatcher().firePropertyChange(
				new PropertyChangeEvent(
						this.desktop,
						MapEditorWindowArranger.EVENT_ARRANGE,
						null,
						null));

		setResult(Command.RESULT_OK);
	}
}
