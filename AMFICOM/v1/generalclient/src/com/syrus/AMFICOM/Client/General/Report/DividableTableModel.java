package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.UI.GeneralTableModel;

/**
 * <p>Title: </p>
 * <p>Description: ��������� ������ � ������������
 * ������� ����� ������������ ���������</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author ���������� ϸ��
 * @version 1.0
 */

public abstract class DividableTableModel extends GeneralTableModel {
  private int divisionsNumber = 1;

  public DividableTableModel(int divisionsNumber,int columnsNumber)
  {
	 super(divisionsNumber * columnsNumber);
  }

  public void setDivisionsNumber(int divNumber) {
	 divisionsNumber = divNumber;
  }

  public int getDivisionsNumber() {
	 return divisionsNumber;
  }
}
