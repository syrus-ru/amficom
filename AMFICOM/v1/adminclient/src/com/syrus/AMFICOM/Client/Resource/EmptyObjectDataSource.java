
package com.syrus.AMFICOM.Client.Resource;

//import java.util.*;

import com.syrus.AMFICOM.Client.General.SessionInterface;
//import com.syrus.AMFICOM.Client.Resource.Object.*;

public class EmptyObjectDataSource
		extends EmptyDataSource
		implements DataSourceInterface
{
	protected EmptyObjectDataSource()
	{
		super();
	}

	public EmptyObjectDataSource(SessionInterface si)
	{
		super(si);
	}
/*
	public void GetObjects()
	{
		addDomains();
		addCategories();
		addGroups();
//		addRoles();
//		addPrivileges();
		addProfiles();
		addImages();
		addExecs();
		addUser();

		for(Enumeration enum1 = Pool.getKeys(); enum1.hasMoreElements();)
		{
			for(Enumeration enum2 = Pool.getHash((String )enum1.nextElement()).elements();
					enum2.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )enum2.nextElement();
				or.updateLocalFromTransferable();
			}
		}
	}

	public void SaveObjects()
	{
	}

	public void addDomains()
	{
		Domain domain;

		domain = new Domain(
				"domain1",
				"������ �����",
				"������� ��� ����� 1",
				"user1",
				" �������� - ������ �����",
				0,
				"user1",
				0,
				"",
				"",
				new Vector(0));
		Pool.put(domain.typ, domain.id, domain);

		domain = new Domain(
				"domain2",
				"������ �����",
				"������� ��� ����� 2",
				"user2",
				" �������� - ������ �����",
				0,
				"user2",
				0,
				"",
				"",
				new Vector(0));
		Pool.put(domain.typ, domain.id, domain);

		domain = new Domain(
				"domain3",
				"������ �����",
				"������� ��� ����� 3",
				"user3",
				" �������� - ������ �����",
				0,
				"user3",
				0,
				"",
				"",
				new Vector(0));
		Pool.put(domain.typ, domain.id, domain);
	}

	public void addCategories()
	{
		OperatorCategory category;
		Vector vec;

		vec = new Vector();
		vec.add("user1");
		vec.add("user2");
		category = new OperatorCategory(
				"category1",
				"��������� �������������",
				"sys",
				"�������� - ��������� �������������",
				vec);
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category2",
				"�������������",
				"admin",
				"�������� - �������������",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category3",
				"������������",
				"config",
				"�������� - ������������",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category4",
				"�������������",
				"sup",
				"�������� - �������������",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		vec = new Vector();
		vec.add("user1");
		vec.add("user2");
		category = new OperatorCategory(
				"category5",
				"��������",
				"oper",
				"�������� - ��������",
				vec);
		Pool.put(category.typ, category.id, category);
	}

	public void addGroups()
	{
		OperatorGroup group;
		Vector vec;

		vec = new Vector();
		vec.add("user1");
		vec.add("user2");
		group = new OperatorGroup(
				"group1",
				"������1",
				"������� ��� - ������ 1",
				"user1",
				"�������� - ������",
				0,
				"user1",
				0,
				"user1",
				vec,
				new Vector(0),
				new Vector(0),
        new Vector());
		Pool.put(group.typ, group.id, group);

		group = new OperatorGroup(
				"group2",
				"������2",
				"������� ��� - ������ 2",
				"user1",
				"�������� - ������",
				0,
				"user1",
				0,
				"user1",
				new Vector(0),
				new Vector(0),
				new Vector(0),
        new Vector());
		Pool.put(group.typ, group.id, group);
	}
*/
/*	public void addRoles()
	{
		OperatorRole role;

		role = new OperatorRole(
				"role1",
				"����1",
				"�� ����1",
				"�������� - ������ ����",
				new Vector(0));

		Pool.put(role.typ, role.id, role);
	}

	public void addPrivileges()
	{
		OperatorPrivilege privilege;

		privilege = new OperatorPrivilege(
				"privilege1",
				"����1",
				"�� ����1",
				"����������",
				"domain1",
				"delete");
		Pool.put(privilege.typ, privilege.id, privilege);

		privilege = new OperatorPrivilege(
				"privilege2",
				"����2",
				"�� ����2",
				"����������",
				"domain1",
				"modify");
		Pool.put(privilege.typ, privilege.id, privilege);
	}*/
/*
	public void addProfiles()
	{
		OperatorProfile profile;

		profile = new OperatorProfile(
				"user1",
				"krupenn",
				"������� ��� - krupenn",
				"user1",
				"�������� - ������������",
				0,
				"user1",
				0,
				"user1");
		profile.login = "krupenn";

		profile.last_login = 0;
		profile.status = "ok";
		profile.state = "active";

		profile.category_ids = new Vector();
		profile.category_ids.add("category1");
		profile.category_ids.add("category5");

		profile.disabled = 0;
		profile.disabled_comments = "";

		profile.priority = "normal";
		profile.logfile = "";

		profile.group_ids = new Vector();
		profile.group_ids.add("group1");

		profile.first_name = "������";
		profile.second_name = "�.";
		profile.last_name = "�����������";
		profile.phone_work = "2627744";
		profile.phone_home = "4260078";
		profile.phone_mobile = "�� �����!";
		profile.phone_emergency = "����������";

		profile.pager_phone = "";
		profile.pager_number = "";
		profile.sms_number = "";

		profile.address = "������� 3� ����� ���, 5";
		profile.language = "russian";
		profile.organization = "Syrus Systems";
		profile.e_mail = "kroupennikov@syrus.ru";


		Pool.put(profile.typ, profile.id, profile);

		profile = new OperatorProfile(
				"user2",
				"stas",
				"������� ��� - stas",
				"user1",
				"�������� - ������������",
				0,
				"user1",
				0,
				"user1");
		profile.login = "stas";

		profile.last_login = 0;
		profile.status = "ok";
		profile.state = "active";

		profile.category_ids = new Vector();
		profile.category_ids.add("category1");
		profile.category_ids.add("category5");

		profile.disabled = 0;
		profile.disabled_comments = "";

		profile.priority = "normal";
		profile.logfile = "";

		profile.group_ids = new Vector();
		profile.group_ids.add("group1");

		profile.first_name = "���������";
		profile.second_name = "�.";
		profile.last_name = "�������";
		profile.phone_work = "2627744";
		profile.phone_home = "";
		profile.phone_mobile = "������� :)";
		profile.phone_emergency = "";

		profile.pager_phone = "";
		profile.pager_number = "";
		profile.sms_number = "";

		profile.address = "������������, �������, 405";
		profile.language = "russian";
		profile.organization = "Syrus Systems";
		profile.e_mail = "kholshin@syrus.ru";


		Pool.put(profile.typ, profile.id, profile);
	}
	public void addExecs()
	{
	  CommandPermissionAttributes cpa = new CommandPermissionAttributes();
	        Pool.put(cpa.typ, cpa.id, cpa);
//		Executables execs = new Executables(this);



	}

	public void addImages()
	{
		ImageCatalogue.add(
				"default",
				new ImageResource("default", "default", "images/pc.gif"));
		ImageCatalogue.add(
				"image1",
				new ImageResource("image1", "image1", "images/pc.gif"));
		ImageCatalogue.add(
				"image2",
				new ImageResource("image2", "image2", "images/node2.gif"));
		ImageCatalogue.add(
				"image3",
				new ImageResource("image3", "image3", "images/node2.gif"));
		ImageCatalogue.add(
				"image4",
				new ImageResource("image4", "image4", "images/pc.gif"));
		ImageCatalogue.add(
				"image5",
				new ImageResource("image5", "image5", "images/pc.gif"));
		ImageCatalogue.add(
				"peretz",
				new ImageResource("peretz", "peretz", "images/peretz.gif"));
		ImageCatalogue.add(
				"andr",
				new ImageResource("andr", "andr", "images/andr.gif"));
	}
	public void addUser()
	{
	  User user = new User();
//	  User.
	}
*/
}