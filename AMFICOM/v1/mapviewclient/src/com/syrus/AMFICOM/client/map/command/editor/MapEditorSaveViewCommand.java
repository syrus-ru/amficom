/*-
 * $$Id: MapEditorSaveViewCommand.java,v 1.15 2005/10/30 15:20:33 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewSaveCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.util.Log;

/**
 * ����� MapEditorSaveViewCommand ������������ ��� ���������� ��������������
 * ����� � ������ "�������� �������������� ����". ���������� �������
 * MapSaveCommand
 * 
 * @version $Revision: 1.15 $, $Date: 2005/10/30 15:20:33 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapViewSaveCommand
 */
public class MapEditorSaveViewCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveViewCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null) {
			assert Log.debugMessage("map frame is null! Cannot create new map.", Level.SEVERE); //$NON-NLS-1$
			setResult(Command.RESULT_NO);
			return;
		}
		new MapViewSaveCommand(mapFrame.getMapView(), this.aContext).execute();

		setResult(Command.RESULT_OK);
	}

}
