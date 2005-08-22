/*
 * $Id: ViewMapChooserCommand.java,v 1.1 2005/08/22 11:30:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapSaveAsCommand;
import com.syrus.AMFICOM.client.map.operations.MapChooserPanel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: ViewMapChooserCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.1 $, $Date: 2005/08/22 11:30:05 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class ViewMapChooserCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;
	
	MapChooserPanel mapChooserPanel = null;
	
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
			Log.debugMessage("map frame is null! Cannot change map view.", Level.SEVERE);
			setResult(Command.RESULT_NO);
			return;
		}

		if(this.mapChooserPanel == null)
			this.mapChooserPanel = new MapChooserPanel(this.mapFrame);
		else
			this.mapChooserPanel.setMapFrame(this.mapFrame);
		
		final String okButton = LangModelGeneral.getString("Button.OK");
		final String cancelButton = LangModelGeneral.getString("Button.Cancel");
		int result = JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				this.mapChooserPanel,
				LangModelGeneral.getString("select map view"),
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				new Object[] { okButton, cancelButton }, 
				okButton);
		
		if (result == JOptionPane.OK_OPTION) {
			this.mapChooserPanel.mapSelected();
		}		
		setResult(Command.RESULT_OK);
	}


}
