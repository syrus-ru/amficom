//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ����� �������� ����������� �� �� ���������� �����          * //
// *           ������ ������ - ��� ����������� ������� ������-������      * //
// *           ������� ������������ � ������� �������, ��� ��� ���        * //
// *           ����������� ������� ���������� ����� ����������� �����     * //
// *           �� ������� ����������� ��������, � � ������ �� ����������  * //
// *           ��� ������������ � �������                                 * //
// *                                                                      * //
// * ���: Java 1.4.0                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 24 mar 2003                                                      * //
// * ������������: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import java.util.*;
import java.util.zip.*;
import java.io.*;

import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.System.*;

public class ObjectDataSourceImage extends DataSourceImage
{
	protected ObjectDataSourceImage()
	{
	}
	
	public ObjectDataSourceImage(DataSourceInterface di)
	{
		super(di);
	}

	public void GetObjects()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(OperatorCategory.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(OperatorGroup.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(OperatorProfile.typ);

//		Pool.removeHash(OperatorCategory.typ);
//		Pool.removeHash(OperatorGroup.typ);
//		Pool.removeHash(OperatorProfile.typ);

		load(OperatorCategory.typ);
		load(OperatorGroup.typ);
		load(OperatorProfile.typ);
		Vector ids = filter(OperatorCategory.typ, desc, true);
		Vector ids2 = filter(OperatorGroup.typ, desc2, true);
		Vector ids3 = filter(OperatorProfile.typ, desc3, true);
		if(	ids.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 )
		{
			String []s1 = new String [ids.size()];
			ids.copyInto(s1);
			String []s2 = new String [ids2.size()];
			ids2.copyInto(s2);
			String []s3 = new String [ids3.size()];
			ids3.copyInto(s3);
			di.GetObjects(s1, s2, s3);
			save(OperatorCategory.typ);
			save(OperatorGroup.typ);
			save(OperatorProfile.typ);
		}
	}

	public void GetAdminObjects()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(Server.typ);
		ResourceDescriptor_Transferable[] desc2 = GetDescriptors(Client.typ);
		ResourceDescriptor_Transferable[] desc3 = GetDescriptors(Agent.typ);

//		Pool.removeHash(Server.typ);
//		Pool.removeHash(Client.typ);
//		Pool.removeHash(Agent.typ);

		load(Server.typ);
		load(Client.typ);
		load(Agent.typ);
		Vector ids = filter(Server.typ, desc, true);
		Vector ids2 = filter(Client.typ, desc2, true);
		Vector ids3 = filter(Agent.typ, desc3, true);
		if(	ids.size() > 0 ||
			ids2.size() > 0 ||
			ids3.size() > 0 )
		{
			String []s1 = new String [ids.size()];
			ids.copyInto(s1);
			String []s2 = new String [ids2.size()];
			ids2.copyInto(s2);
			String []s3 = new String [ids3.size()];
			ids3.copyInto(s3);
			di.GetAdminObjects(s1, s2, s3);
			save(Server.typ);
			save(Client.typ);
			save(Agent.typ);
		}
	}
}

