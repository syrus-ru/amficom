package com.syrus.impexp.unicablemap;

import com.syrus.impexp.ImportExportException;
import java.sql.SQLException;
import java.util.*;
import java.util.Iterator;

/**
 * 
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/08/29 13:14:35 $
 * @module mapviewclient_v1
 */
public class SchemeUniCableMapImpExp 
{
	String username = "sysdba";
	String password = "masterkey";
	String host = "localhost";
	String database = "d:/cbdb/tel.gdb";
	String fileName = "d:/testucm.esf";

	public SchemeUniCableMapImpExp()
	{
//		UniCableMapDialog dialog = new UniCableMapDialog();
//
//		dialog.setVisible(true);

		try
		{
			UniCableMapDatabase ucmDatabase = new UniCableMapDatabase(
				username,
				password,
				host,
				database);
			
			UCMSchemeExportCommand command = new UCMSchemeExportCommand(ucmDatabase);
			command.execute();
			ucmDatabase.close();
		}
		catch (ImportExportException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		new SchemeUniCableMapImpExp();
	}
}