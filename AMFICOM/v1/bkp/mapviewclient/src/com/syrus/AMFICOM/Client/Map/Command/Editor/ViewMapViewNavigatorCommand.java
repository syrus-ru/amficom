/**
 * $Id: ViewMapViewNavigatorCommand.java,v 1.1 2005/03/10 17:51:06 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreePanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * отобразить окно привязки схем к карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 17:51:06 $
 * @module mapviewclient_v1
 */
public class ViewMapViewNavigatorCommand extends ViewNavigatorCommand
{
	public MapViewTreeFrame treeFrame;
	
	public ViewMapViewNavigatorCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		super(aContext.getDispatcher(), desktop, "Навигатор объектов");

		super.desktop = desktop;
		super.aContext = aContext;
	}

	public void execute()
	{
		this.treeFrame = MapDesktopCommand.findMapViewTreeFrame(this.desktop);

		if(this.treeFrame == null)
		{
			this.treeFrame = new MapViewTreeFrame();
			MapViewTreePanel panel = new MapViewTreePanel(this.aContext);

			this.treeFrame.setTitle(this.title);
			this.treeFrame.setClosable(true);
			this.treeFrame.setResizable(true);
			this.treeFrame.setMaximizable(false);
			this.treeFrame.setIconifiable(false);
			this.treeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
			this.treeFrame.getContentPane().setLayout(new BorderLayout());
			this.treeFrame.getContentPane().add(panel, BorderLayout.CENTER);

			this.desktop.add(this.treeFrame);

			Dimension dim = this.desktop.getSize();
			this.treeFrame.setLocation(dim.width * 4 / 5, dim.height / 2);
			this.treeFrame.setSize(dim.width / 5, dim.height / 2);
		}

		this.treeFrame.setVisible(true);
		this.treeFrame.toFront();
		this.treeFrame.grabFocus();
		setResult(Command.RESULT_OK);
	}
}
