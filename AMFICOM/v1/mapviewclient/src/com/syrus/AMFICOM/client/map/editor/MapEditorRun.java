/**
 * $Id: MapEditorRun.java,v 1.8 2005/06/06 12:57:02 krupenn Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/06/06 12:57:02 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorRun {
	private MapEditorRun() {
		//empty
	}

	public static void main(String[] args) {
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}
