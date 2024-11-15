# GestPro - Gestion des Projets 📊

**GestPro** est une application de gestion des projets permettant de suivre les tâches, les équipes et les délais associés à un projet. Le projet est composé d'un backend en **Java** avec **Spring Boot**, d'un frontend en **Angular**, et d'une base de données **PostgreSQL**.

## Table des matières 📑

- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Structure du projet](#structure-du-projet)
- [Utilisation](#utilisation)
- [API](#api)
- [Contributions](#contributions)
- [Licence](#licence)

## Technologies utilisées 💻

- **Backend :**
  - **Java** avec **Spring Boot** pour la gestion des services backend 🚀.
  - **Spring Data JPA** avec **Hibernate** pour l'interaction avec la base de données 🗄️.
  - **Spring Security** pour la gestion de l'authentification et de la sécurité 🔐.

- **Frontend :**
  - **Angular** pour le développement de l'interface utilisateur 🎨.
  - **Bootstrap** pour le design réactif et les composants UI 📱.

- **Base de données :**
  - **PostgreSQL** pour le stockage des données des projets, utilisateurs et tâches 🗃️.

- **Outils de développement :**
  - **Maven** pour la gestion des dépendances et la construction du projet backend ⚙️.
  - **Node.js** et **npm** pour la gestion des dépendances frontend 🌐.

---

## Prérequis 🛠️

Avant de démarrer, assurez-vous d’avoir installé les outils suivants :

- **Java 21** ou version ultérieure ☕.
- **Node.js** et **npm** pour Angular 🖥️.
- **PostgreSQL** pour la gestion de la base de données 🗄️.
- **IDE** (par exemple, IntelliJ IDEA, Eclipse pour Java ; VSCode pour Angular) 💡.

---

## Installation 🏗️

### Backend (Java + Spring Boot)

1. Clonez le repository :
   ```bash
   git clone https://github.com/thomas-calonnec/GestPro.git
   ```
2. Accédez au répertoire :
   ```bash
   cd GestPro/backend
   ```
3. Créez une base de données dans PostgreSQL, nommée gestpro (ou un autre nom de votre choix), puis modifiez les paramètres de connexion dans le fichier src/main/resources/application.properties :
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/gestpro
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
4. Lancez le projet en appuyant sur run

### Frontend (Angular)
1. Allez dans le répertoire frontend :
   ```bash
   cd Gestpro/frontend
   ```
2. Installez les dépendances frontend avec npm :
   ```bash
   npm install
   ```
3. Démarrez le serveur de développement Angular :
   ```bash
   ng serve
   ```
## Structure du projet 📂

Voici la structure de base du projet :
```bash
    GestPro/
    │
    ├── backend/              # Code source du backend (Java + Spring Boot)
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/
    │   │   │   │   └── com/
    │   │   │   │       └── gestpro/
    │   │   │   │           └── controller/
    │   │   │   │           └── service/
    │   │   │   │           └── repository/
    │   │   │   └── resources/
    │   │   │       └── application.properties  # Configuration de la base de données
    │   ├── pom.xml           # Configuration Maven
    │
    ├── frontend/             # Code source du frontend (Angular)
    │   ├── src/
    │   │   ├── app/
    │   │   │   └── components/
    │   │   └── services/
    │   │   └── dao/
    │   │   ├── environments/
    │   │   └── index.html
    │   ├── package.json      # Dépendances et scripts npm
    │   └── angular.json      # Configuration du projet Angular
    │
    └── README.md             # Ce fichier
```
## Utilisation 🚀
1. Frontend :
  L'application frontend permet de gérer les projets, les utilisateurs et les tâches. Il est possible de se connecter, de créer, modifier et supprimer des projets et des tâches, et de gérer l'affectation des tâches aux utilisateurs 👥.

2. Backend :
  Le backend fournit une API REST pour gérer les projets, les utilisateurs et les tâches. Les données sont stockées dans une base PostgreSQL et accessibles via des endpoints sécurisés 🔒.

## API 🔌
Voici quelques points d’API disponibles dans le backend :

1. Authentification 🔑
 - POST /api/auth/login : Permet de se connecter avec un nom d’utilisateur et un mot de passe 🔐.
 - POST /api/auth/register : Permet de créer un nouvel utilisateur 📝.
   
2. Gestion des Projets 📅
 - GET /api/user/workspaces : Récupère la liste de tous les projets 📃.
 - POST /api/user/workspaces  : Crée un nouveau projet 🆕.
 - PUT /api/user/workspaces/{id} : Met à jour un projet existant 🔄.
 - DELETE /api/user/workspaces/{id} : Supprime un projet ❌.
   
3. Gestion des Tâches 📝
 - GET /api/tasks : Récupère la liste de toutes les tâches d’un projet 📋.
 - POST /api/tasks : Crée une nouvelle tâche 🆕.
 - PUT /api/tasks/{id} : Met à jour une tâche existante 🔄.
 - DELETE /api/tasks/{id} : Supprime une tâche ❌.

