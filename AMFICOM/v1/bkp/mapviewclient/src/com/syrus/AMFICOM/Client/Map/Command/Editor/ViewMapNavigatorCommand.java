/**
 * $Id: ViewMapNavigatorCommand.java,v 1.6 2005/02/01 13:29:56 krupenn Exp $
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
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/02/01 13:29:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		schemeTreeFrame = MapDesktopCommand.findMapSchemeTreeFrame(desktop);

		if(schemeTreeFrame == null)
		{
			schemeTreeFrame = new MapSchemeTreeFrame();
			MapSchemeTreePanel panel = new MapSchemeTreePanel("", aContext);

			schemeTreeFrame.setTitle(title);
			schemeTreeFrame.setClosable(true);
			schemeTreeFrame.setResizable(true);
			schemeTreeFrame.setMaximizable(false);
			schemeTreeFrame.setIconifiable(false);
			schemeTreeFrame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
			schemeTreeFrame.getContentPane().setLayout(new BorderLayout());
			schemeTreeFrame.getContentPane().add(panel, BorderLayout.CENTER);

			desktop.add(schemeTreeFrame);

			Dimension dim = desktop.getSize();
			schemeTreeFrame.setLocation(dim.width * 4 / 5, dim.height / 2);
			schemeTreeFrame.setSize(dim.width / 5, dim.height / 2);
		}

		schemeTreeFrame.setVisible(true);
		schemeTreeFrame.toFront();
		schemeTreeFrame.grabFocus();
		setResult(Command.RESULT_OK);
	}
}
