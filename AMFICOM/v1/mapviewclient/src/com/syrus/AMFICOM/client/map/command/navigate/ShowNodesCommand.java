/**
 * $Id: ShowNodesCommand.java,v 1.14 2005/09/16 14:53:34 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.command.navigate;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;

/**
 * ������� ���������/���������� ������ ����������� �������������� ����� �� ����� 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/09/16 14:53:34 $
 * @module mapviewclient
 */
public class ShowNodesCommand extends MapNavigateCommand {
	AbstractButton button;

	public ShowNodesCommand(ApplicationModel aModel, NetMapViewer netMapViewer) {
		super(aModel, netMapViewer);
	}

	@Override
	public void setParameter(String field, Object value) {
		super.setParameter(field, value);
		if(field.equals("button")) //$NON-NLS-1$
			this.button = (AbstractButton )value;
	}

	Icon visibleIcon = new ImageIcon("images/nodes_visible.gif"); //$NON-NLS-1$

	Icon invisibleIcon = new ImageIcon("images/nodes_invisible.gif"); //$NON-NLS-1$

	@Override
	public void execute() {
		if(MapPropertiesManager.isShowPhysicalNodes()) {
			this.button.setIcon(this.invisibleIcon);
			MapPropertiesManager.setShowPhysicalNodes(false);
		}
		else
			if(!this.aModel.isSelected(MapApplicationModel.MODE_NODES)) {
				this.button.setIcon(this.visibleIcon);
				MapPropertiesManager.setShowPhysicalNodes(true);
			}

		this.aModel.setSelected(
				MapApplicationModel.MODE_NODES,
				MapPropertiesManager.isShowPhysicalNodes());
		this.aModel.fireModelChanged();
		try {
			this.netMapViewer.repaint(false);
		} catch(MapConnectionException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(MapDataException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
