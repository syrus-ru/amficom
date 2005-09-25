/**
 * $Id: SpatialObject.java,v 1.11 2005/09/25 16:08:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map;

/**
 * �������������� ������. ���������� ����������� ������������ ������� ���.
 * ������� ������������� ������ - � ������ ��������� �� ���������� �������
 * ����������� ��������.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/09/25 16:08:01 $
 * @module mapviewclient
 */
public abstract class SpatialObject implements Comparable<SpatialObject> {
	protected String label;

	public SpatialObject(String label) {
		this.label = label;
	}

	/**
	 * ���������� ��� ��������������� �������.
	 * 
	 * @return ��� �������
	 */
	public String getLabel() {
		return this.label;
	}

	public int compareTo(SpatialObject spatialObject) {
		return (this.label.compareTo(spatialObject.label));
	}
}
