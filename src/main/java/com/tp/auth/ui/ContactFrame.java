package com.tp.auth.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.tp.auth.model.Contact;
import com.tp.auth.model.User;
import com.tp.auth.service.ContactService;

public class ContactFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Rechercher");

    private final User user;
    private final ContactService service = new ContactService();

    private final List<Contact> contacts = new ArrayList<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Nom", "Prénom", "Email", "Téléphone"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    public ContactFrame(User user) {
        super("Gestion des contacts");

        this.user = user;

        JLabel userLabel = new JLabel(
                "Connecté en tant que " + user.getUsername()
        );

        JButton logoutButton = new JButton("Se déconnecter");
        logoutButton.addActionListener(e -> logout());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        table.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher :"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        searchButton.addActionListener(e -> searchContacts());
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        refreshTable();
    }
    
    private void searchContacts() {
        String text = searchField.getText().trim();

        try {
            contacts.clear();

            if (text.isEmpty()) {
                contacts.addAll(service.findByUser(user.getId()));
            } else {
                contacts.addAll(service.search(user.getId(), text));
            }

            tableModel.setRowCount(0);

            for (Contact contact : contacts) {
                tableModel.addRow(new Object[]{
                        contact.getNom(),
                        contact.getPrenom(),
                        contact.getEmail(),
                        contact.getTelephone()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la recherche : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshTable() {
        try {
            contacts.clear();
            contacts.addAll(service.findByUser(user.getId()));

            tableModel.setRowCount(0);

            for (Contact contact : contacts) {
                tableModel.addRow(new Object[]{
                        contact.getNom(),
                        contact.getPrenom(),
                        contact.getEmail(),
                        contact.getTelephone()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors du chargement des contacts : "
                            + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addContact() {
        ContactDialog dialog = new ContactDialog(this, user, null);

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshTable();
        }
    }

    private void updateContact() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez un contact",
                    "Aucun contact sélectionné",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        Contact selectedContact = contacts.get(modelRow);

        ContactDialog dialog = new ContactDialog(
                this,
                user,
                selectedContact
        );

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            refreshTable();
        }
    }

    private void deleteContact() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez un contact",
                    "Aucun contact sélectionné",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        Contact selectedContact = contacts.get(modelRow);

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Supprimer " + selectedContact.getNom() + " "
                        + selectedContact.getPrenom() + " ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.delete(
                    selectedContact.getId(),
                    user.getId()
            );

            refreshTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Contact supprimé avec succès."
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la suppression : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Contact showContactDialog(Contact existingContact) {
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField telephoneField = new JTextField(20);

        if (existingContact != null) {
            nomField.setText(existingContact.getNom());
            prenomField.setText(existingContact.getPrenom());
            emailField.setText(existingContact.getEmail());
            telephoneField.setText(existingContact.getTelephone());
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));

        panel.add(new JLabel("Nom :"));
        panel.add(nomField);

        panel.add(new JLabel("Prénom :"));
        panel.add(prenomField);

        panel.add(new JLabel("Email :"));
        panel.add(emailField);

        panel.add(new JLabel("Téléphone :"));
        panel.add(telephoneField);

        String title = existingContact == null
                ? "Ajouter un contact"
                : "Modifier un contact";

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        int id = existingContact == null
                ? 0
                : existingContact.getId();

        return new Contact(
                id,
                user.getId(),
                nomField.getText().trim(),
                prenomField.getText().trim(),
                emailField.getText().trim(),
                telephoneField.getText().trim()
        );
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                message + " : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
        );
    }
}