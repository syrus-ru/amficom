package com.syrus.AMFICOM.server.measurement;

import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import oracle.jdbc.driver.OracleResultSet;
import com.syrus.AMFICOM.CORBA.Survey.ClientParameter_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class SetParameter {
	private String id;
  private String type_id;
  private String set_id;
  private byte[] value;
  private String set_sort;
	private String sort;

	public SetParameter(String id, String set_sort, String sort) throws SQLException {
		this.set_sort = set_sort;
		this.sort = sort;
		String table_name = this.set_sort + this.sort + "s";

		this.id = id;

		Statement st = DatabaseConnection.getConnection().createStatement();
		String query = "SELECT set_id, type_id, value"
			+ " FROM amficom." + table_name
			+ " WHERE id = '" + this.id + "'";
		Log.debugMessage("SetParameter.init | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    if (!rs.next()) {
      String mesg = "No record in " + table_name + " with id '" + this.id + "'";
      throw new SQLException(mesg);
    }
		this.type_id = rs.getString("type_id");
		this.set_id = rs.getString("set_id");
    this.value = ByteArrayDatabase.toByteArray(rs.getBLOB("value"));
		rs.close();
		st.close();
	}

	public SetParameter(ClientParameter_Transferable parameter_t,
											String set_id,
											String set_sort,
											String sort) throws Exception {
		this(parameter_t.id,//ResourcedbInterface.getUId(set_sort + sort),
				 parameter_t.type_id,
				 set_id,
				 parameter_t.value,
				 set_sort,
				 sort);
		this.save();
	}

	private SetParameter(String id,
											 String type_id,
											 String set_id,
											 byte[] value,
											 String set_sort,
											 String sort) {
		this.id = id;
		this.type_id = type_id;
		this.set_id = set_id;
		this.value = value;
		this.set_sort = set_sort;
		this.sort = sort;
	}

	public void save() throws Exception {
		String table_name = this.set_sort + this.sort + "s";

    Connection conn = DatabaseConnection.getConnection();
    Statement st = conn.createStatement();
		String str = "INSERT INTO amficom." + table_name
			+ " (id, set_id, type_id, value)"
	    + " VALUES ('" + this.id + "', '" + this.set_id + "', '" + this.type_id + "', EMPTY_BLOB())";
		Log.debugMessage("SetParameter.init | Trying: " + str, Log.DEBUGLEVEL05);
		st.executeUpdate(str);
		st.close();
    ByteArrayDatabase bdb  = new ByteArrayDatabase(this.value);
    bdb.saveAsBlob(conn, table_name, "value", "id = '" + this.id + "'");
	}

	public String getId() {
    return this.id;
  }

  public String getTypeId() {
    return this.type_id;
  }

  public String getSetId() {
    return this.set_id;
  }

  public byte[] getValue() {
    return this.value;
  }

	public ClientParameter_Transferable getClientTransferable() throws SQLException {
		ParameterType parameterType = new ParameterType(this.type_id, this.set_sort, this.sort);
		return new ClientParameter_Transferable(this.id,
                                            this.type_id,
                                            this.value,
                                            parameterType.getCodename(),
                                            parameterType.getParameterTypeId());
	}

	public Parameter_Transferable getTransferable() throws SQLException {
    ParameterType parameterType = new ParameterType(this.type_id, this.set_sort, this.sort);
    return new Parameter_Transferable(parameterType.getCodename(),
                                      this.type_id,
                                      this.value);
  }

	protected void update(ClientParameter_Transferable parameter_t) throws Exception {
		if (parameter_t.id.compareTo(this.id) != 0)
			return;

		this.value = parameter_t.value;

		Connection conn = DatabaseConnection.getConnection();
    String table_name = this.set_sort + this.sort + "s";
    ByteArrayDatabase badb = new ByteArrayDatabase(this.value);
		badb.saveAsBlob(conn, table_name, "value", "id = '" + this.id + "'");
	}

	public static SetParameter[] retrieveParameters(String set_id, String set_sort, String sort) throws SQLException {
		String table_name = set_sort + sort + "s";

		Statement st = DatabaseConnection.getConnection().createStatement();
		String query = "SELECT id, type_id, value"
      + " FROM amficom." + table_name
      + " WHERE set_id = '" + set_id + "'";
		Log.debugMessage("SetParameter.retrieveParameters | Trying: " + query, Log.DEBUGLEVEL05);
    OracleResultSet rs = (OracleResultSet)st.executeQuery(query);
    LinkedList llsp = new LinkedList();
    while (rs.next())
			llsp.add(new SetParameter(rs.getString("id"),
																rs.getString("type_id"),
																set_id,
																ByteArrayDatabase.toByteArray(rs.getBLOB("value")),
																set_sort,
																sort));
		rs.close();
		st.close();
		Log.debugMessage("SetParameter.retrieveArguments | Retrieved " + llsp.size() + " " + table_name + " for set_id = '" + set_id + "'", Log.DEBUGLEVEL05);
		return (SetParameter[])llsp.toArray(new SetParameter[llsp.size()]);
	}
}
