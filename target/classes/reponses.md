# Questions README.md

### Q1. Pourquoi la colonne user_id est-elle indispensable pour respecter la règle « un utilisateur ne voit que ses contacts » ?

- Cela sert à assurer la qualité des données insérées dans la bdd, pour vérifier la présence d'un user possédant l'id adéquat.

### Q2. Que se passerait-il, sans `ON DELETE CASCADE` , si on supprimait un utilisateur qui a des contacts ?

- A la suppression d'un user, le contact associé pourrait subsister en ayant un id non assigné, donc une donnée inutile.

### Q3. Pourquoi update et delete doivent-ils aussi vérifier user_id dans leur WHERE , alors que l' id du contact suffit à l'identifier ? Pensez à un utilisateur malveillant qui connaîtrait l' id d'un contact d'un autre.

- Il faut ajouter user_id dans le WHERE de update et delete pour garantir qu'un utilisateur ne puisse modifier ou supprimer que ses propres contacts.