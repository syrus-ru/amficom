
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
				"Первый домен",
				"Кодовое имя домен 1",
				"user1",
				" описание - просто домен",
				0,
				"user1",
				0,
				"",
				"",
				new Vector(0));
		Pool.put(domain.typ, domain.id, domain);

		domain = new Domain(
				"domain2",
				"Второй домен",
				"Кодовое имя домен 2",
				"user2",
				" описание - просто домен",
				0,
				"user2",
				0,
				"",
				"",
				new Vector(0));
		Pool.put(domain.typ, domain.id, domain);

		domain = new Domain(
				"domain3",
				"Третий домен",
				"Кодовое имя домен 3",
				"user3",
				" описание - просто домен",
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
				"Системный администратор",
				"sys",
				"описание - системный администратор",
				vec);
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category2",
				"Администратор",
				"admin",
				"описание - администратор",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category3",
				"Конфигуратор",
				"config",
				"описание - конфигуратор",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		category = new OperatorCategory(
				"category4",
				"Супероператор",
				"sup",
				"описание - супероператор",
				new Vector(0));
		Pool.put(category.typ, category.id, category);

		vec = new Vector();
		vec.add("user1");
		vec.add("user2");
		category = new OperatorCategory(
				"category5",
				"Оператор",
				"oper",
				"описание - оператор",
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
				"Группа1",
				"кодовое имя - группа 1",
				"user1",
				"описание - группа",
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
				"Группа2",
				"кодовое имя - группа 2",
				"user1",
				"описание - группа",
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
				"Роль1",
				"кн роль1",
				"описание - просто роль",
				new Vector(0));

		Pool.put(role.typ, role.id, role);
	}

	public void addPrivileges()
	{
		OperatorPrivilege privilege;

		privilege = new OperatorPrivilege(
				"privilege1",
				"прив1",
				"кн прив1",
				"привилегия",
				"domain1",
				"delete");
		Pool.put(privilege.typ, privilege.id, privilege);

		privilege = new OperatorPrivilege(
				"privilege2",
				"прив2",
				"кн прив2",
				"привилегия",
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
				"кодовое имя - krupenn",
				"user1",
				"описание - пользователь",
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

		profile.first_name = "Андрей";
		profile.second_name = "В.";
		profile.last_name = "Крупенников";
		profile.phone_work = "2627744";
		profile.phone_home = "4260078";
		profile.phone_mobile = "не скажу!";
		profile.phone_emergency = "аналогично";

		profile.pager_phone = "";
		profile.pager_number = "";
		profile.sms_number = "";

		profile.address = "Москваб 3й Новый пер, 5";
		profile.language = "russian";
		profile.organization = "Syrus Systems";
		profile.e_mail = "kroupennikov@syrus.ru";


		Pool.put(profile.typ, profile.id, profile);

		profile = new OperatorProfile(
				"user2",
				"stas",
				"кодовое имя - stas",
				"user1",
				"описание - пользователь",
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

		profile.first_name = "Станислав";
		profile.second_name = "И.";
		profile.last_name = "Хольшин";
		profile.phone_work = "2627744";
		profile.phone_home = "";
		profile.phone_mobile = "проебал :)";
		profile.phone_emergency = "";

		profile.pager_phone = "";
		profile.pager_number = "";
		profile.sms_number = "";

		profile.address = "Долгопрудный, семерка, 405";
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