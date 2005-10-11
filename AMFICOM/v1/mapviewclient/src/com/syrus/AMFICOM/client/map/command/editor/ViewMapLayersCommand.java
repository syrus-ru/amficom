/*-
 * $$Id: ViewMapLayersCommand.java,v 1.7 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.operations.LayersPanel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Log;

/**
 * ����� $RCSfile: ViewMapLayersCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.7 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewMapLayersCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;
	
	LayersPanel layersPanel = null;
	
	MapFrame mapFrame;

	/**
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public ViewMapLayersCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		
		if(this.mapFrame == null) {
			Log.debugMessage("map frame is null! Cannot change map view.", Level.SEVERE); //$NON-NLS-1$
			setResult(Command.RESULT_NO);
			return;
		}

		if(this.layersPanel == null) {
			this.layersPanel = new LayersPanel(this.mapFrame);
		}
		else {
			this.layersPanel.setMapFrame(this.mapFrame);
		}
		
		final String okButton = I18N.getString(MapEditorResourceKeys.BUTTON_OK);
		final String cancelButton = I18N.getString(MapEditorResourceKeys.BUTTON_CANCEL);
		JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				this.layersPanel,
				I18N.getString(MapEditorResourceKeys.TITLE_CONFIGURE_TOPOLOGICAL_LAYERS),
				JOptionPane.OK_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				new Object[] { okButton, cancelButton }, 
				okButton);
		setResult(Command.RESULT_OK);
	}


}
