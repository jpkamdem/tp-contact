package com.tp.auth.service;

import java.util.List;

import com.tp.auth.model.Contact;
import com.tp.auth.model.ContactDao;

public class ContactService {

    private final ContactDao dao = new ContactDao();

    public String insert(Contact contact) throws Exception {
        String erreur = valider(contact);

        if (erreur != null) {
            return erreur;
        }

        dao.insert(contact);

        return null;
    }

    public String update(Contact contact) throws Exception {
        String erreur = valider(contact);

        if (erreur != null) {
            return erreur;
        }

        dao.update(contact);

        return null;
    }

    public void delete(int id, int userId) throws Exception {
        dao.delete(id, userId);
    }

    public List<Contact> findByUser(int userId) throws Exception {
        return dao.findByUser(userId);
    }

    public List<Contact> search(int userId, String text) throws Exception {
        return dao.search(userId, text);
    }

    private String valider(Contact contact) {

        if (contact == null) {
            return "Contact invalide";
        }

        if (contact.getNom() == null || contact.getNom().trim().isEmpty()) {
            return "Le nom est obligatoire";
        }

        if (contact.getPrenom() == null || contact.getPrenom().trim().isEmpty()) {
            return "Le prénom est obligatoire";
        }

        String email = contact.getEmail();

        if (email != null && !email.trim().isEmpty()) {
            email = email.trim();

            if (!email.contains("@")
                    || !email.substring(email.indexOf('@') + 1).contains(".")) {
                return "Email invalide";
            }
        }

        String telephone = contact.getTelephone();

        if (telephone != null && !telephone.trim().isEmpty()) {
            telephone = telephone.trim();

            if (!telephone.matches("\\+?[0-9 ]+")) {
                return "Téléphone invalide";
            }
        }

        return null;
    }
}