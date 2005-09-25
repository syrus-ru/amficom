/*
 * $Id: ViewMapChooserCommand.java,v 1.8 2005/09/25 16:08:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapSaveAsCommand;
import com.syrus.AMFICOM.client.map.operations.MapChooserPanel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: ViewMapChooserCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.8 $, $Date: 2005/09/25 16:08:02 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class ViewMapChooserCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;
	
	static MapChooserPanel mapChooserPanel = null;
	
	MapFrame mapFrame;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public ViewMapChooserCommand(
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

		int result = chooseMap(this.mapFrame.getMapConnection());
		
		if (result == JOptionPane.OK_OPTION) {
//			mapChooserPanel.mapSelected();
			this.mapFrame.getMapViewer().getLogicalNetLayer()
					.getContext().getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.TOPOLOGY_CHANGED));
		}		
		setResult(Command.RESULT_OK);
	}


	public static int chooseMap(MapConnection connection) {
		if(mapChooserPanel == null)
			mapChooserPanel = new MapChooserPanel(connection);
		else
			mapChooserPanel.setMapConnection(connection);

		final String okButton = LangModelGeneral.getString(MapEditorResourceKeys.BUTTON_OK);
		final String cancelButton = LangModelGeneral.getString(MapEditorResourceKeys.BUTTON_CANCEL);
		int result = JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				mapChooserPanel,
				LangModelMap.getString(MapEditorResourceKeys.TITLE_CHOOSE_MAP),
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				new Object[] { okButton, cancelButton }, 
				okButton);
		if (result == JOptionPane.OK_OPTION) {
			mapChooserPanel.mapSelected();
		}
		return result;
	}
}
