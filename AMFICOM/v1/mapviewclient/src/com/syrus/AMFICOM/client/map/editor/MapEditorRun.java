/**
 * $Id: MapEditorRun.java,v 1.9 2005/06/17 13:34:08 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */
package com.syrus.AMFICOM.client.map.editor;

import com.syrus.AMFICOM.client.model.DefaultMapEditorApplicationModelFactory;

/**
 * ������ ���������� "�������� �������������� ����" 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2005/06/17 13:34:08 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor();
	}
}
