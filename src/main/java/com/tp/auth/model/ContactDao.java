package com.tp.auth.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tp.auth.db.Database;

public class ContactDao {
	List<Contact> findByUser(int user_id) throws Exception {
		String sql = "select * from contacts where user_id = ? order by nom desc, prenom desc;";
	
        List<Contact> contacts = new ArrayList<>();
		
		try (Connection conn = Database.getConnection();
			     PreparedStatement ps = conn.prepareStatement(sql)) {

			    ps.setInt(1, user_id);
			    
			    try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Contact contact = new Contact(
	                            rs.getInt("id"),
	                            rs.getInt("user_id"),
	                            rs.getString("nom"),
	                            rs.getString("prenom"),
	                            rs.getString("email"),
	                            rs.getString("telephone")
	                    );

	                    contacts.add(contact);
	                }
	            }
			}
		
		return contacts;
	}
	
	public void insert(Contact c) throws Exception {
	    String sql = "INSERT INTO contacts "
	               + "(user_id, nom, prenom, email, telephone) "
	               + "VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = Database.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, c.getUser_id());
	        ps.setString(2, c.getNom());
	        ps.setString(3, c.getPrenom());
	        ps.setString(4, c.getEmail());
	        ps.setString(5, c.getTelephone());

	        ps.executeUpdate();
	    }
	}
	
	public void update(Contact c) throws Exception {
	    String sql = "UPDATE contacts "
	               + "SET nom = ?, prenom = ?, email = ?, telephone = ? "
	               + "WHERE id = ? AND user_id = ?";

	    try (Connection conn = Database.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, c.getNom());
	        ps.setString(2, c.getPrenom());
	        ps.setString(3, c.getEmail());
	        ps.setString(4, c.getTelephone());
	        ps.setInt(5, c.getId());
	        ps.setInt(6, c.getUser_id());

	        ps.executeUpdate();
	    }
	    
	    
	}
	
	public void delete(int id, int userId) throws Exception {
	    String sql = "DELETE FROM contacts "
	               + "WHERE id = ? AND user_id = ?";

	    try (Connection conn = Database.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);
	        ps.setInt(2, userId);

	        ps.executeUpdate();
	    }
	}
	
	public List<Contact> search(int userId, String text) throws Exception {
	    String sql = "SELECT * FROM contacts "
	               + "WHERE user_id = ? "
	               + "AND (nom LIKE ? OR prenom LIKE ? OR email LIKE ?) "
	               + "ORDER BY nom DESC, prenom DESC";

	    List<Contact> contacts = new ArrayList<>();

	    try (Connection conn = Database.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        String pattern = "%" + text + "%";

	        ps.setInt(1, userId);
	        ps.setString(2, pattern);
	        ps.setString(3, pattern);
	        ps.setString(4, pattern);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Contact contact = new Contact(
	                        rs.getInt("id"),
	                        rs.getInt("user_id"),
	                        rs.getString("nom"),
	                        rs.getString("prenom"),
	                        rs.getString("email"),
	                        rs.getString("telephone")
	                );

	                contacts.add(contact);
	            }
	        }
	    }

	    return contacts;
	}
}
