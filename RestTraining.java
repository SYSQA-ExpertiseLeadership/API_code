package com.RESTtraining.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/RestTraining")
public class RestTraining {
	
	Connection connect = null;
	Statement statement = null;
	ResultSet resultSet = null;
	String url = "jdbc:postgresql://localhost:5432/postgres";
	String user = "postgres";
	String passwd = "admin";
	//String passwd = "HqwO(KqRc2T@Uoe[8s5H";

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create_customer/{gebruikersnaam}/{voornaam}/{achternaam}")
	public String create_customer(@PathParam ("gebruikersnaam") String gebruikersnaam, @PathParam("voornaam") String voornaam, @PathParam("achternaam") String achternaam) throws Exception{
		int klantnummer = create_customernumber();
		String Objid = create_objid();
		String query = "insert into persoon values ('"+gebruikersnaam+"','"+voornaam+"','"+achternaam+"',"+Integer.toString(klantnummer)+","+Objid+")";
		writeDataBase(query);
		String resultaat = "{\"resultaat\":\"Er is een nieuwe klant aangemaakt met klantnummer "+Integer.toString(klantnummer)+"\""+"}"; 
		return resultaat;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_customer/{klant_nummer}")
	public String get_customer(@PathParam("klant_nummer") int klant_nummer) throws Exception{
		List<String> userlist = null;
		String query = "Select * from persoon where klantnummer = "+Integer.toString(klant_nummer);
		resultSet = readDataBase(query);
		while(resultSet.next()){
			String gebruiker = resultSet.getString("gebruikersnaam").trim();
			String voornaam = resultSet.getString("voornaam").trim();
			String achternaam = resultSet.getString("achternaam").trim();
			int klantnummer = resultSet.getInt("klantnummer");
		
			userlist = new ArrayList<String>();
			userlist.add(gebruiker);
			userlist.add(voornaam);
			userlist.add(achternaam);
			userlist.add(Integer.toString(klantnummer));
		}
		close();
		return "{\"resultaat\":\""+userlist+"\"}";
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update_customer/{klant_nummer}/{veld}/{waarde}")
	public String update_customer(@PathParam("klant_nummer") int klantnummer, @PathParam("veld") String veld, @PathParam("waarde") String waarde) throws Exception{
		String query = "update persoon set "+veld+" = '"+waarde+"' where klantnummer = "+Integer.toString(klantnummer);
		writeDataBase(query);
		
		String resultaat = "{\"resultaat\":\"De "+veld+" van de klant is aangepast naar "+waarde+"\""+"}"; 
		return resultaat;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete_customer/{klant_nummer}")
	// Moet ik hier dan iets aanpassen om een globale delete ook mogelijk te maken?
	public String delete_customer(@PathParam("klant_nummer") int klantnummer) throws Exception{
		String query = "Delete from persoon where klantnummer = "+Integer.toString(klantnummer);
		writeDataBase(query);
		String resultaat = "{\"resultaat\":\"De klant is verwijderd\"}"; 
		return resultaat;
	}
	
	
	
	public String create_objid() {
		String objid = String.valueOf(System.currentTimeMillis());
		return objid;
	}
	
	public int create_customernumber() throws Exception {
		String query = "Select max(klantnummer) from persoon";
		resultSet = readDataBase(query);
		int klantnummer = 0;
		/*
		 * Handig stukje om de juiste kolomnaam te achtehalen.
		 * Blijkbaar is de kolomnaam max
		 */
		//ResultSetMetaData meta = resultSet.getMetaData();
		//String kolom = meta.getColumnName(1);
		//System.out.println("Welke kolom "+kolom);
		while(resultSet.next()){
			klantnummer = resultSet.getInt("max");
		}
		close();
		klantnummer = klantnummer + 1;
		return klantnummer;
	}
	
	public ResultSet readDataBase(String query) throws Exception {
		try {
	      // This will load the MySQL driver, each DB has its own driver
	      Class.forName("org.postgresql.Driver");
	      
	      // Setup the connection with the DB
	      connect = DriverManager.getConnection(url,user,passwd);

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      // Result set get the result of the SQL query
	      resultSet = statement
	          .executeQuery(query);
	      return resultSet;
	      
	    } catch (Exception e) {
	      throw e;
	    } 
	  }
		
	public void writeDataBase(String query) throws Exception {
		try {
	      // This will load the MySQL driver, each DB has its own driver
	      Class.forName("org.postgresql.Driver");
	      
	      // Setup the connection with the DB
	      connect = DriverManager.getConnection(url,user,passwd);

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      // executeUpdate verwacht geen resultaten, geschikt voor insert, update, delete. Dus ook geen resultset. 
	      statement.executeUpdate(query);
	    } catch (Exception e) {
	      throw e;
	    } 
	  }
	
	  // You need to close the resultSet
	  private void close() {
	    try {
	      if (resultSet != null) {
	        resultSet.close();
	      }

	      if (statement != null) {
	        statement.close();
	      }

	      if (connect != null) {
	        connect.close();
	      }
	    } catch (Exception e) {

	    }
	  }
}