/*-
 * $$Id: ViewAdditionalPropertiesCommand.java,v 1.11 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * Команда отображает окно свойств элемента карты
 *  
 * @version $Revision: 1.11 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewAdditionalPropertiesCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapAdditionalPropertiesFrame frame;

	public ViewAdditionalPropertiesCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.frame = MapDesktopCommand
				.findMapAdditionalPropertiesFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new MapAdditionalPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_ADDITIONAL_PROPERTIES), this.aContext); //$NON-NLS-1$
			this.frame.setClosable(true);
			this.frame.setResizable(true);
			this.frame.setMaximizable(false);
			this.frame.setIconifiable(false);

			this.desktop.add(this.frame);

			Dimension dim = this.desktop.getSize();
			this.frame.setLocation(dim.width * 4 / 5, dim.height * 2 / 5);
			this.frame.setSize(dim.width / 5, dim.height * 2 / 5);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}

}
