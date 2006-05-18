/*-
 * $$Id: ViewMapViewNavigatorCommand.java,v 1.15 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapViewTreeFrame;
import com.syrus.AMFICOM.client.map.ui.MapViewTreePanel;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * отобразить окно привязки схем к карте
 *  
 * @version $Revision: 1.15 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ViewMapViewNavigatorCommand extends AbstractCommand {
	public ApplicationContext aContext;

	public JDesktopPane desktop;

	public String title = I18N.getString(MapEditorResourceKeys.TITLE_NAVIGATOR);

	public MapViewTreeFrame treeFrame;

	public ViewMapViewNavigatorCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
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
			this.treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			this.treeFrame.setFrameIcon(new ImageIcon(
					Toolkit.getDefaultToolkit().getImage("images/general.gif"))); //$NON-NLS-1$
			this.treeFrame.getContentPane().setLayout(new BorderLayout());
			this.treeFrame.getContentPane().add(panel, BorderLayout.CENTER);

			this.desktop.add(this.treeFrame);

			Dimension dim = this.desktop.getSize();
			this.treeFrame.setLocation(0, 0);
			this.treeFrame.setSize(dim.width / 5, dim.height);
		}

		this.treeFrame.setVisible(true);
		this.treeFrame.toFront();
		this.treeFrame.grabFocus();
		setResult(Command.RESULT_OK);
	}
}
