# gestPro

**gestPro** est une application web collaborative de gestion de projets et de tâches inspirée par Trello. Elle permet aux utilisateurs de créer des tableaux, des listes, ajouter des cartes, des étiquettes, et de collaborer facilement autour de leurs projets.

## Fonctionnalités principales

- Authentification (inscription, connexion, OAuth Google)
- Gestion de tableaux, listes et cartes
- Ajout et suppression d’étiquettes et de checklists
- Attribution de tâches aux membres
- Interface utilisateur interactive et responsive

---

## Architecture et technologies utilisées

Le projet est structuré en 2 parties :

### Backend

- **Langage principal** : Java 23
- **Framework** : Spring Boot (Spring Data JPA, REST Controller)
- **Sécurité** : Gestion JWT, OAuth2, cookies
- **Lombok** : Pour réduire le code boilerplate (getters, setters, etc.)
- **Jakarta EE** : (Notamment pour les imports et la gestion des requêtes)
- **Base de données** : JPA (avec potentielle compatibilité H2, MySQL…)
- **Gestion des dépendances** : Maven

### Frontend

- **Langage** : TypeScript
- **Framework** : Angular 19
- **Outils** :
    - @angular/material (UI moderne)
    - RxJS (programmation réactive)
    - @kolkov/angular-editor (éditeur riche pour cartes ou commentaires)
    - ngx-cookie-service (gestion des cookies côté client)
- **Authentification** : angular-oauth2-oidc, JWT

### Tests

- **Backend** : Tests intégrés via JUnit et Spring Test
- **Frontend** : Karma, Jasmine pour les tests unitaires

---

## Prérequis & Installation rapide

- **Backend**
    - Java 23 installé
    - Maven
    - Lancer avec :
      ```sh
      mvn spring-boot:run
      ```
- **Frontend**
    - Node.js (npm)
    - Installer les dépendances :
      ```sh
      npm install
      ```
    - Démarrer l’application Angular :
      ```sh
      npm start
      ```

---

## Contribuer

Les contributions sont les bienvenues !
1. Fork du dépôt
2. Création de branche
3. Pull Request après relecture

---

## Remarques

- L’application supporte l’authentification standard et par Google.
- Le backend expose une API REST sécurisée.
- L’architecture respecte le découpage moderne “frontend / backend” pour faciliter la maintenance et l’évolution.

---

## Auteurs

Projet développé par Thomas Calonnec.

---

N’hésitez pas à adapter ce README selon la spécificité de certaines de vos briques techniques ou de vos usages particuliers.