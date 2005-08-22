/*
 * $Id: ViewMapLayersCommand.java,v 1.1 2005/08/22 11:30:06 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapSaveAsCommand;
import com.syrus.AMFICOM.client.map.operations.LayersPanel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Log;

/**
 * ����� $RCSfile: ViewMapLayersCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.1 $, $Date: 2005/08/22 11:30:06 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
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
			Log.debugMessage("map frame is null! Cannot change map view.", Level.SEVERE);
			setResult(Command.RESULT_NO);
			return;
		}

		if(this.layersPanel == null)
			this.layersPanel = new LayersPanel(this.mapFrame);
		else
			this.layersPanel.setMapFrame(this.mapFrame);
		
		final String okButton = LangModelGeneral.getString("Button.OK");
		final String cancelButton = LangModelGeneral.getString("Button.Cancel");
		JOptionPane.showOptionDialog(
				Environment.getActiveWindow(), 
				this.layersPanel,
				LangModelGeneral.getString("select map view"),
				JOptionPane.OK_OPTION, 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				new Object[] { okButton, cancelButton }, 
				okButton);
		setResult(Command.RESULT_OK);
	}


}
