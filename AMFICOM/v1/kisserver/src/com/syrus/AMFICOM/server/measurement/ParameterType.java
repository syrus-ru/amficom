package com.syrus.AMFICOM.server.measurement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.LinkedList;
import com.syrus.AMFICOM.CORBA.Survey.ActionParameterType_Transferable;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class ParameterType {
  private String id;
  private String name;
  private String codename;
  private String parameter_type_id;
  private String holder_type_id;
  private String holder_sort;
  private String sort;

  public ParameterType(String id, String holder_sort, String sort) throws SQLException {
		this.id = id;
    this.holder_sort = holder_sort;
    this.sort = sort;

    Statement st = DatabaseConnection.getConnection().createStatement();
    String table_name = this.holder_sort + "type" + this.sort + "s";
    String holder_column_name = holder_sort + "_type_id";
    String query = "SELECT name, codename, parameter_type_id, " + holder_column_name + " holder_type_id"
			+ " FROM amficom." + table_name
			+ " WHERE id = '" + this.id + "'";
		Log.debugMessage("ParameterType.init | Trying: " + query, Log.DEBUGLEVEL05);
    ResultSet rs = st.executeQuery(query);
    if (!rs.next()) {
      String mesg = "No record in " + table_name + " with id '" + this.id + "'";
      throw new SQLException(mesg);
    }
    this.name = rs.getString("name");
    this.codename = rs.getString("codename");
    this.parameter_type_id = rs.getString("parameter_type_id");
    this.holder_type_id = rs.getString("holder_type_id");
    rs.close();
    st.close();
		this.name = (this.name == null)?"":this.name;
		this.parameter_type_id = (this.parameter_type_id == null)?"":this.parameter_type_id;
  }

    public ParameterType(ActionParameterType_Transferable parameter_type_t,
                         String holder_sort,
                         String sort) throws SQLException {
      this.holder_sort = holder_sort;
      this.sort = sort;
      this.id = parameter_type_t.id;//ResourcedbInterface.getUId(this.holder_sort + "type" + this.sort);
      this.name = parameter_type_t.name;
      this.codename = parameter_type_t.codename;
      this.parameter_type_id = parameter_type_t.parameter_type_id;
      this.holder_type_id = parameter_type_t.holder_type_id;

      Statement st = DatabaseConnection.getConnection().createStatement();
      String table_name = this.holder_sort + "type" + this.sort + "s";
      String holder_column_name = holder_sort + "_type_id";
      String update = "INSERT INTO " + table_name
        + " (id, name, codename, parameter_type_id, " + holder_column_name + ")"
        + " VALUES ('" + this.id + "', '" + this.name + "', '" + this.codename + "', '" + this.parameter_type_id + "', '" + this.holder_type_id + "')";
			Log.debugMessage("ParameterType.init | Trying: " + update, Log.DEBUGLEVEL05);
      st.executeUpdate(update);
			st.close();
    }

    private ParameterType(String id,
                          String name,
                          String codename,
                          String parameter_type_id,
                          String holder_type_id,
                          String holder_sort,
                          String sort) {
        this.id = id;
        this.name = name;
        this.codename = codename;
        this.parameter_type_id = parameter_type_id;
        this.holder_type_id = holder_type_id;
        this.holder_sort = holder_sort;
        this.sort = sort;
    }

    public String getId() {
      return this.id;
    }

    public String getCodename() {
      return this.codename;
    }

    public String getParameterTypeId() {
      return this.parameter_type_id;
    }

    public ActionParameterType_Transferable getTransferable() {
      return new ActionParameterType_Transferable(this.id,
                                                  this.name,
                                                  this.codename,
                                                  this.parameter_type_id,
                                                  this.holder_type_id);
    }

    protected static ParameterType[] retrieveParameterTypes(String holder_type_id, String holder_sort, String sort) throws SQLException {
      Statement st = DatabaseConnection.getConnection().createStatement();
      String table_name = holder_sort + "type" + sort + "s";
      String holder_column_name = holder_sort + "_type_id";
      String query = "SELECT id, name, codename, parameter_type_id"
        + " FROM amficom." + table_name
        + " WHERE " + holder_column_name + " = '" + holder_type_id + "'";
			Log.debugMessage("ParameterType.retrieveParameterTypes | Trying: " + query, Log.DEBUGLEVEL05);
      ResultSet rs = st.executeQuery(query);
      LinkedList llpt = new LinkedList();
			String name, parameter_type_id;
      while (rs.next()) {
				name = rs.getString("name");
				parameter_type_id = rs.getString("parameter_type_id");
				llpt.add(new ParameterType(rs.getString("id"),
                                   (name == null)?"":name,
																	 rs.getString("codename"),
																	 (parameter_type_id == null)?"":parameter_type_id,
																	 holder_type_id,
																	 holder_sort,
																	 sort));
			}
      rs.close();
      st.close();
			Log.debugMessage("ParameterType.retrieveParameterTypes | Retrieved " + llpt.size() + " " + sort + " for " + holder_column_name + " = '" + holder_type_id + "'", Log.DEBUGLEVEL05);
      return (ParameterType[])llpt.toArray(new ParameterType[llpt.size()]);
    }
}