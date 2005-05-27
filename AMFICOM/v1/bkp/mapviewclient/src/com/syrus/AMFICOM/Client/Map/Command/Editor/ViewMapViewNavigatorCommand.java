/**
 * $Id: ViewMapViewNavigatorCommand.java,v 1.4 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreeFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapViewTreePanel;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * отобразить окно привязки схем к карте 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class ViewMapViewNavigatorCommand extends AbstractCommand {
	public ApplicationContext aContext;

	public JDesktopPane desktop;

	public String title = "Навигатор объектов";

	public MapViewTreeFrame treeFrame;

	public ViewMapViewNavigatorCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		this.treeFrame = MapDesktopCommand.findMapViewTreeFrame(this.desktop);

		if(this.treeFrame == null) {
			this.treeFrame = new MapViewTreeFrame();
			MapViewTreePanel panel = new MapViewTreePanel(this.aContext);

			this.treeFrame.setTitle(this.title);
			this.treeFrame.setClosable(true);
			this.treeFrame.setResizable(true);
			this.treeFrame.setMaximizable(false);
			this.treeFrame.setIconifiable(false);
			this.treeFrame.setFrameIcon(new ImageIcon(
					Toolkit.getDefaultToolkit().getImage("images/general.gif")));
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
