package myApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhoneDAO {

	private Connection con;
	
	public PhoneDAO() {
		// start db driver
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql) {
		try {
			return (con.createStatement()).executeQuery(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void update(String sql) {
		try {
			Statement s = con.createStatement();
			s.executeQuery(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Phone> getAllPhones(){
		List<Phone> phones = new ArrayList<Phone>();
		try {
			ResultSet rs = query("select * from phone");
			while(rs.next()) {
				Phone phone = new Phone();
				phone.setId(rs.getInt("id"));
				phone.setMake(rs.getString("make"));
				phone.setModel(rs.getString("model"));
				phone.setBattery(rs.getString("battery"));
				
				phones.add(phone);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return phones;
		
	}
	
	public void addPhone(Phone phone) {
		String s = ("INSERT INTO \"PUBLIC\".\"PHONE\" ( \"MAKE\", \"MODEL\", \"BATTERY\" ) VALUES (");
		s += ("'" + phone.getMake() + "', ");
		s += ("'" + phone.getModel() + "', ");
		s += ("'" + phone.getBattery() + "')");
		
		update(s);
	}
	
	public void updatePhone(Phone phone) {
		String s = ("UPDATE \"PUBLIC\".\"PHONE\" SET ");
		s += ("\"MAKE\"= '" + phone.getMake() + "', ");
		s += ("\"MODEL\"= '" + phone.getModel() + "', ");
		s += ("\"BATTERY\"= '" + phone.getBattery() + "' ");
		s += (" WHERE \"ID\" = " + phone.getId());
		
		update(s);
	}
	
	public void deletePhone(int id) {
		String s = ("DELETE FROM \"PUBLIC\".\"PHONE\" WHERE \"ID\" = " + id);
		update(s);
	}
	
}
