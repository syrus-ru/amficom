/*-
 * $$Id: MapCloseCommand.java,v 1.15 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * @version $Revision: 1.15 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
