/*
 * $Id: MapCloseCommand.java,v 1.14 2005/08/24 10:20:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� ����� ���
 * ���������� �� ������ ������ ���� �����. ��� ���� � ��������� ����
 * ������������ ���������� � ���, ��� �������� ����� ���, � ����� ������������
 * �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/08/24 10:20:59 $
 * @module mapviewclient
 */
public class MapCloseCommand extends AbstractCommand {
	/**
	 * ���� �����
	 */
	Map map;

	public MapCloseCommand(Map map) {
		this.map = map;
	}

	@Override
	public void execute() {
		// mapFrame.saveConfig();

		if(this.map != null) {
			// TODO should clean only changes in this.map
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MAP_CODE);
		}

		setResult(Command.RESULT_OK);
	}

}
