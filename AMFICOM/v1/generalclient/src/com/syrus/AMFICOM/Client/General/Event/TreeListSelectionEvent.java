/*
��������� ��������!!!
�� ������� ������� ������ ���������� ���� ������������ event �� ���
������ �����. � ������ TreeListSelectionEvent.

�������: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.SELECT_EVENT, boolean search, boolean
searchall);

������: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.REFRESH_EVENT);

��������: new TreeListSelectionEvent(ObjectResource obj,
TreeListSelectionEvent.DESELECT_EVENT);

���������, ��� � ������ ������� ��� ��������� ��������� �� ���������
false. �� ��������� �����
����������� ������� ������������ � ������� ���������� ����� ������.
�������� select ���������, ��������� ��� ��� �������������� ����� ��
������.

select   selectall |
------------------ |
true     false     |  ����� �� �����.����� � � �����. �����
true     true      |  ����� �� ����� ������

�����, ������ �������� - Object.
�.�. ����� �������� ��� String, ��� � OR.

�������� ����� �������� ��������� event.
����� ������ �������� ����� ���������, ��������, ���:
TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.DESELECT_EVENT

���, ��� ����� ������, �� ����� �����������
TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT

������������� ����� ������ �� TreeListSelectionEvent.
*/
package com.syrus.AMFICOM.Client.General.Event;


public class TreeListSelectionEvent extends OperationEvent
{
	public boolean SELECT = false;
	public boolean REFRESH = false;
	public boolean DESELECT = false;

	public boolean search = false;
	public boolean searchAll = false;

	public static final long SELECT_EVENT = 0x00000001;
	public static final long REFRESH_EVENT = 0x00000010;
	public static final long DESELECT_EVENT = 0x00000100;
	public static final String typ = "treelistselectionevent";

	public TreeListSelectionEvent(Object source, long type, boolean search, boolean searchall )
	{
		super(source, 0, typ);
		typ_processing(type);
		this.search = search;
		this.searchAll = searchall;
	}

	public TreeListSelectionEvent(Object source, long type, boolean search)
	{
		super(source, 0, typ);
		typ_processing(type);
		this.search = search;
	}

	public TreeListSelectionEvent(Object source, long type)
	{
		super(source, 0, typ);
		typ_processing(type);
	}

	void typ_processing(long typ)
	{
		if((typ & SELECT_EVENT) != 0)
			SELECT = true;
		if((typ & REFRESH_EVENT) != 0)
			REFRESH = true;
		if((typ & DESELECT_EVENT) != 0)
			DESELECT = true;
	}
}
