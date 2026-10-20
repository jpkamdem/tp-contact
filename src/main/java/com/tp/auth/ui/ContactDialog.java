package com.tp.auth.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tp.auth.model.Contact;
import com.tp.auth.model.User;
import com.tp.auth.service.ContactService;

public class ContactDialog extends JDialog {
    private static final long serialVersionUID = 1L;
	
    private final User user;
    private final Contact contact;
    private final ContactService service = new ContactService();

    private final JTextField nomField = new JTextField(20);
    private final JTextField prenomField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField telephoneField = new JTextField(20);

    private boolean saved = false;

    public ContactDialog(
            java.awt.Frame owner,
            User user,
            Contact contact) {

        super(
                owner,
                contact == null ? "Ajouter un contact" : "Modifier un contact",
                true
        );

        this.user = user;
        this.contact = contact;

        if (contact != null) {
            nomField.setText(contact.getNom());
            prenomField.setText(contact.getPrenom());
            emailField.setText(contact.getEmail());
            telephoneField.setText(contact.getTelephone());
        }

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nomField);

        formPanel.add(new JLabel("Prénom :"));
        formPanel.add(prenomField);

        formPanel.add(new JLabel("Email :"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Téléphone :"));
        formPanel.add(telephoneField);

        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(saveButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void saveContact() {
        int id = contact == null ? 0 : contact.getId();

        Contact newContact = new Contact(
                id,
                user.getId(),
                nomField.getText().trim(),
                prenomField.getText().trim(),
                emailField.getText().trim(),
                telephoneField.getText().trim()
        );

        try {
            String erreur;

            if (contact == null) {
                erreur = service.insert(newContact);
            } else {
                erreur = service.update(newContact);
            }

            if (erreur != null) {
                JOptionPane.showMessageDialog(
                        this,
                        erreur,
                        "Erreur de validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            saved = true;
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de l'enregistrement : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSaved() {
        return saved;
    }
}