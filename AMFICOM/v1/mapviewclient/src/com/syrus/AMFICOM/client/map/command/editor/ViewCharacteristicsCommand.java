/*-
 * $$Id: ViewCharacteristicsCommand.java,v 1.13 2005/10/19 11:56:52 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapPropertiesEventHandler;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * 
 * @version $Revision: 1.13 $, $Date: 2005/10/19 11:56:52 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewCharacteristicsCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public CharacteristicPropertiesFrame frame;

	@SuppressWarnings("unused")
	private MapPropertiesEventHandler handler;

	public ViewCharacteristicsCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.frame = MapDesktopCommand
				.findCharacteristicsFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new CharacteristicPropertiesFrame(I18N.getString(MapEditorResourceKeys.TITLE_CHARACTERISTICS));
			this.handler = new MapPropertiesEventHandler(this.frame, this.aContext);
			this.frame.setClosable(true);
			this.frame.setResizable(true);
			this.frame.setMaximizable(false);
			this.frame.setIconifiable(false);

			this.desktop.add(this.frame);

			Dimension dim = this.desktop.getSize();
			this.frame.setLocation(dim.width * 4 / 5, dim.height * 4 / 5);
			this.frame.setSize(dim.width / 5, dim.height / 2);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}
}
