/**
 * $Id: ViewCharacteristicsCommand.java,v 1.6 2005/08/11 12:43:30 arseniy Exp $
 * Syrus Systems ������-����������� ����� ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class ViewCharacteristicsCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapCharacteristicPropertiesFrame frame;

	public ViewCharacteristicsCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.frame = MapDesktopCommand
				.findMapCharacteristicsFrame(this.desktop);

		if(this.frame == null) {
			this.frame = new MapCharacteristicPropertiesFrame(LangModelMap.getString("Characteristics"), this.aContext);
			this.frame.setClosable(true);
			this.frame.setResizable(true);
			this.frame.setMaximizable(false);
			this.frame.setIconifiable(false);

			this.desktop.add(this.frame);

			Dimension dim = this.desktop.getSize();
			this.frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			this.frame.setSize(dim.width / 5, dim.height / 2);
		}

		this.frame.setVisible(true);
		setResult(Command.RESULT_OK);
	}
}
