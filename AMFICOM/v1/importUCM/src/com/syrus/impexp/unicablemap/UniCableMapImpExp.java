package com.syrus.impexp.unicablemap;

import com.syrus.impexp.ImportExportException;
import java.sql.SQLException;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1.1.1 $, $Date: 2005/03/31 10:23:28 $
 * @module mapviewclient_v1
 */
public class UniCableMapImpExp 
{
	String username = "sysdba";
	String password = "masterkey";
	String host = "localhost";
	String database = "d:/My Documents/ISM/doc/Resident/tel.gdb";

	String fileName = "d:/My Documents/ISM/prog/java/AMFICOm/run/Data/ucm/testucm.esf";

	public UniCableMapImpExp()
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
			UniCableMapExportCommand command = new UniCableMapExportCommand(ucmDatabase, fileName);
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
		new UniCableMapImpExp();
	}
}