/**
 * $Id: ViewMapNavigatorCommand.java,v 1.7 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ViewNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * отобразить окно привязки схем к карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class ViewMapNavigatorCommand extends ViewNavigatorCommand
{
	public MapSchemeTreeFrame schemeTreeFrame;
	
	public ViewMapNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super(aContext.getDispatcher(), desktop, "Навигатор объектов");

		super.desktop = desktop;
		super.aContext = aContext;
	}

	public void execute()
	{
		this.schemeTreeFrame = MapDesktopCommand.findMapSchemeTreeFrame(this.desktop);

		if(this.schemeTreeFrame == null)
		{
			this.schemeTreeFrame = new MapSchemeTreeFrame();
			MapSchemeTreePanel panel = new MapSchemeTreePanel("", this.aContext);

			this.schemeTreeFrame.setTitle(this.title);
			this.schemeTreeFrame.setClosable(true);
			this.schemeTreeFrame.setResizable(true);
			this.schemeTreeFrame.setMaximizable(false);
			this.schemeTreeFrame.setIconifiable(false);
			this.schemeTreeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
			this.schemeTreeFrame.getContentPane().setLayout(new BorderLayout());
			this.schemeTreeFrame.getContentPane().add(panel, BorderLayout.CENTER);

			this.desktop.add(this.schemeTreeFrame);

			Dimension dim = this.desktop.getSize();
			this.schemeTreeFrame.setLocation(dim.width * 4 / 5, dim.height / 2);
			this.schemeTreeFrame.setSize(dim.width / 5, dim.height / 2);
		}

		this.schemeTreeFrame.setVisible(true);
		this.schemeTreeFrame.toFront();
		this.schemeTreeFrame.grabFocus();
		setResult(Command.RESULT_OK);
	}
}
