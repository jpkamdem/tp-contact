package com.tp.auth.model;

import java.sql.Timestamp;

public class Contact {
	int id;
	int user_id;
	String nom;
	String prenom;
	String email;
	String telephone;
	Timestamp created_at;
	
	public Contact(int id, int user_id, String nom, String prenom, String email, String telephone) {
		this.id = id;
		this.user_id = user_id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
	}
	
	public int getId() {
		return id;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public Timestamp getCreated_at() {
		return created_at;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
