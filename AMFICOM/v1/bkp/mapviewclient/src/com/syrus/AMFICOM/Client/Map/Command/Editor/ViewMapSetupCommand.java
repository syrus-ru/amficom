/**
 * $Id: ViewMapSetupCommand.java,v 1.6 2005/03/02 12:33:29 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Operations.ControlsFrame;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

/**
 * ������� ���������� ���� ���������� ������ � ������ 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/03/02 12:33:29 $
 * @module mapviewclient_v1
 */
public class ViewMapSetupCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	public ControlsFrame frame;

	public ViewMapSetupCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		this.frame = MapDesktopCommand.findControlsFrame(this.desktop);

		if(this.frame == null)
		{
			this.frame = new ControlsFrame(null, this.aContext);

			this.desktop.add(this.frame);

			Dimension dim = new Dimension(this.desktop.getWidth(), this.desktop.getHeight());
			this.frame.setLocation(dim.width * 4 / 5, dim.height / 2);
			this.frame.setSize(dim.width / 5, dim.height / 2);
		}

		this.frame.setVisible(true);
		this.frame.setMapFrame(MapDesktopCommand.findMapFrame(this.desktop));
		setResult(Command.RESULT_OK);
	}

}
