Introduction au Refactoring de la Dette Technique de l'Application Spotify
---

Dans le cadre de l'amélioration continue et de la maintenance de l'application Spotify, un refactoring majeur a été entrepris pour résoudre plusieurs problèmes liés à la dette technique accumulée au fil du développement. Ce processus vise à améliorer la qualité, la sécurité et la performance de l'application tout en facilitant les évolutions futures.

## Objectifs du Refactoring

- Amélioration de la sécurité : Transition du flux de jetons "Implicit grant" à un flux "Authorization code" pour renforcer la sécurité des connexions utilisateur et la gestion des tokens d'accès.

- Simplification du code : Réduction de la complexité du code en extrayant la logique métier dans des fonctions réutilisables et en structurant mieux les composants du frontend.

- Optimisation de la performance : Mise en place d'un système de pagination pour les résultats de recherche afin d'améliorer l'expérience utilisateur.

- Amélioration de la maintenabilité : Adoption de bonnes pratiques de développement, telles que la séparation des responsabilités, pour rendre le code plus testable, réutilisable et évolutif.

## Contexte
Au fil des itérations de l'application, plusieurs solutions temporaires ont été mises en place pour répondre aux besoins immédiats, mais elles ont conduit à une accumulation de dettes techniques. Ces problèmes ont affecté à la fois la sécurité des données, la performance de l'application, ainsi que la possibilité d'ajouter de nouvelles fonctionnalités de manière flexible.

Ce refactoring constitue donc une étape essentielle pour aligner l'architecture du projet avec les standards modernes de développement, en garantissant une meilleure sécurité, un code plus propre et une expérience utilisateur optimale.
## 🚀 Frontend : Problèmes rencontrés et solutions


## ⚠️ **Problème 1 : Couplage direct des requêtes API dans les composants**

### Impact :
- 💡 **Code difficilement testable** : les appels API étaient fortement couplés à la logique des composants.
- 🔁 **Manque de réutilisabilité** : duplication de la logique si d'autres composants avaient besoin de cette fonctionnalité.

### ✅ **Solution : Extraction de la logique dans une fonction réutilisable**
La logique d'interaction avec l'API a été déplacée dans une fonction utilitaire `searchAlbum`.

### 🎯 **Avantages** :
- Meilleure séparation des responsabilités.
- Réutilisabilité accrue.

### 🛠 **Avant** :
```typescript
const searchAlbum = async () => {
  const result: AxiosResponse = await axios({
    method: "GET",
    url: "https://api.spotify.com/v1/search",
    headers: {
      Authorization: `Bearer ${authCode.value}`,
    },
    params: {
      q: searchInput.value,
      type: "album",
    },
  });
  albums.value = result.data.albums;
};
```
### ✨ Après : 
```typescript
const handleSearch = async () => {
  const albumList = await searchAlbum(
    searchInput.value,
    offset.value,
    authCode.value,
    favorites.value
  );
  albums.value.items = albumList.items;
  albums.value.total = albumList.total;
};
```

## ❤️ Problème 2 : Gestion des favoris
### Impact :
🚫 Absence de gestion des favoris : les utilisateurs ne pouvaient pas marquer ou consulter leurs albums favoris.
📉 Fonctionnalité manquante qui réduit l'intérêt de l'application.
✅ Solution : Ajout d'une gestion des favoris
💾 Intégration d'une API backend pour récupérer les favoris.
🔄 Synchronisation en temps réel après chaque modification.

### ✨ Ajout des favoris :
```typescript
const getFavorites = async () => {
  try {
    const response = await axios.get<Favorite[]>(
      `http://localhost:8080/api/favorites/${authCode.value}`
    );
    favorites.value = response.data.map((fav) => fav.albumId); // 🎯 Récupération des IDs
  } catch (error) {
    console.error("Erreur lors du chargement des favoris:", error);
  }
};
```

### 📌 Mise à jour après une action :
```typescript
const updateFavorites = (newFavoriteList: string[]) => {
  favorites.value = [...newFavoriteList];
};  
```

## 🔄 Problème 3 : Pagination des résultats
### Impact :
📃 Seule la première page des résultats était affichée, ce qui limitait la navigation.
📉 Expérience utilisateur incomplète.

### ✅ Solution : Introduction de l'offset pour la pagination
➡️ Ajout d'un paramètre offset pour gérer la pagination.


### ✨ Mise en œuvre :
```typescript
const offset = ref(1);

const handleSearch = async () => {
  const albumList = await searchAlbum(
    searchInput.value,
    offset.value,
    authCode.value,
    favorites.value
  );
  albums.value.items = albumList.items;
  albums.value.total = albumList.total;
};
```

### 🎉 Avantages des améliorations
✅ Code propre et maintenable : séparation des responsabilités et meilleure organisation.
📈 Expérience utilisateur améliorée : gestion des favoris, pagination et synchronisation des recherches.
🛡️ Résilience accrue : gestion robuste des états et des erreurs.

## 🌀 Problème 4 : Composants non réutilisables et difficilement maintenables

Les composants AlbumItem et Card sont rigides :
 - AlbumItem :
        Attente d'une structure de données spécifique non normalisée.
        Pas de vérifications pour éviter des erreurs en cas de données manquantes.
 - Card :
    Trop d'efforts sont concentrés sur le rendu visuel, sans considération pour l'organisation des données (par exemple, pas de pagination ni de filtrage).

### ✅ Solution apportée : Refactorisation avec PrimeVue DataTable
Remplacement des boucles manuelles `v-for` par un composant de tableau interactif `DataTable`.

Ajout de fonctionnalités comme :
Pagination dynamique.
Recherche globale et par colonne.
Gestion centralisée des actions utilisateur (favoris, navigation).
### 🎯 Avantages :

Réutilisabilité accrue : Le composant peut être utilisé avec d'autres ensembles de données similaires.
Performance optimisée : La pagination charge uniquement les données nécessaires.
Expérience utilisateur enrichie : Ajout de fonctionnalités interactives (favoris, recherche).

## Problème 5 : Token Implicit grant peu sécurisable
Le projet passe d'un Implicit grant token flow  à un authorization code flow, offrant une meilleure sécurité et la capacité d'obtenir des refresh tokens pour des connexions prolongées.

 Dans le flow précédent, les jetons d'accès étaient directement récupérés via l'URL hash. Avec le nouveau flow, un code d'autorisation est échangé contre un jeton d'accès via le backend.

### 🚀 Changements principaux
1. **Ancienne implémentation : Token-Based Flow**
Dans l'ancien système, le jeton d'accès était extrait directement de l'URL hash après la redirection depuis Spotify.

Exemple de code (ancien flow) :
```typescript
<template>
  <div />
</template>

<script lang="ts">
import { onMounted } from "vue";

export default {
  setup() {
    onMounted(async () => {
      console.log("mounted");
      const token = window.location.hash.substr(1).split("&")[0].split("=")[1];
      if (token) {
        localStorage.setItem("authorization_code", token);
        await window.opener.spotifyCallback(token);
      }
      window.location.replace("/");
    });
  },
};
</script>
```
🚫 Problème : Ce flow n'offrait pas de refresh tokens, ce qui signifiait que les utilisateurs devaient se reconnecter régulièrement.

Limitation : Moins sécurisé car les jetons sont exposés dans l'URL.

2. Nouvelle implémentation : Authorization Code Flow
Avec ce nouveau flow, le backend de l'application gère l'échange du code d'autorisation contre un jeton d'accès et un refresh token, renforçant ainsi la sécurité et la persistance de l'authentification.

🧾 Bénéfices du nouveau flow
Amélioration de la sécurité :

Les tokens ne sont plus exposés dans l'URL.
Utilisation des refresh tokens pour prolonger la session sans réauthentification utilisateur.
Meilleure expérience utilisateur :

Réduction des interruptions dues à l'expiration des tokens.
Conformité OAuth2 :

Implémentation correcte et conforme au standard OAuth2 recommandé par Spotify.



# 🔧 Backend : Problèmes rencontrés et solutions

---
## 📁 Problème 1 : Absence de structure organisée (Repository, Service, Model)
### Impact :
❌ Le code était directement intégré dans les contrôleurs, ce qui rendait la logique métier et les accès aux données difficilement maintenables.
⚙️ Difficile d'ajouter ou de modifier des fonctionnalités sans introduire des bugs.

### ✅ Solution : Adoption de la structure MVC

📂 Création d'un package repository pour les interactions avec la base de données (H2).
📂 Création d'un package service pour contenir la logique métier.
📂 Déplacement des entités dans un package model.

### Dossiers ajoutés :
1. `model` :
    - Contient les entités représentant les données manipulées par l'application.
    - Exemple : ListenedAlbum.java.

2. `repository` :

    - Regroupe les interfaces qui gèrent les interactions avec la base de données en utilisant Spring Data JPA.
    - Exemple : ListenedAlbumRepository.java.
3. `service` :

    - Contient la logique métier, séparée des contrôleurs.
    - Exemple : ListenedAlbumService.java.
4. `controller` :

    - Gère uniquement les requêtes HTTP et délègue les tâches à la couche service.

Exemple d'organisation actuelle :

```bash
src/
├── main/java/com/groupeonepoint/codingchallenge
│   ├── controller/
│   │   └── FavoriteController.java
│   │   └── ListenedAlbumsController.java
│   │   └── SpotifyController.java
│   ├── service/
│   │   └── Favorite.java
│   │   └── ListenedAlbumService.java
│   │   └── SpotifyService.java
│   ├── repository/
│   │   └── FavoriteRepository.java
│   │   └── ListenedAlbumRepository.java
│   ├── model/
│   │   └── Favorite.java
│   │   └── ListenedAlbum.java
│   │   └── SpotifyConfig.java
```
### 🎯 Avantages :

Meilleure séparation des responsabilités.
Facilite les tests unitaires et les évolutions du projet.


## 🕒 Problème 2 : Lecture inefficace des fichiers CSV
### Impact :
🐢 Le fichier CSV était lu ligne par ligne avec BufferedReader, ce qui compliquait le traitement et la maintenabilité.
💥 En cas de fichier volumineux, cette méthode aurait pu causer des problèmes de performance.
✅ Solution : Chargement des données via Repository
📥 Déplacement des données nécessaires dans une table H2 via un script d’initialisation dans `/src/main/ressources/db.changelog/master-changelog.xml`.
🗃️ Utilisation de Spring Data JPA pour interagir avec les données.

```java
public List<ListenedAlbum> getLastListenedAlbums() {
    return listenedAlbumRepository.findTop10ByOrderByLastListenedDesc();
}
```

### 🎯 Avantages :

Performance améliorée : pas de lecture manuelle de fichiers.
Lisibilité accrue et gestion plus robuste des données.

## 🔗 Problème 3 : Gestion des relations entre entités

### Impact :
❓ Pas de gestion des relations (par exemple, albums favoris liés aux utilisateurs).
🚫 Les requêtes manuelles nécessitaient beaucoup de logique custom, augmentant la complexité.

### ✅ Solution : Ajout des relations via JPA
Ajout d’annotations comme `@OneToMany`, `@ManyToOne`, et `@JoinColumn` pour gérer les relations directement dans les entités.

```java
@Entity
public class Favorite {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private ListenedAlbum album;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // Getters et Setters ...
}
```
### 🎯 Avantages :

Simplifie les requêtes grâce à la gestion automatique des relations.
Réduit le risque d’erreurs dans les jointures.

🎉 Résultats des améliorations
🏗️ Structure claire et maintenable grâce à la séparation des responsabilités (MVC).
⚡ Performance optimisée avec la gestion des données via JPA.
🛡️ Application robuste et résiliente, avec une gestion efficace des exceptions.




## 📘 Ajout du Service Spotify pour le Flow Authorization Code
### 📝 Contexte
Le Spotify Authorization Code Flow permet une authentification sécurisée en deux étapes :

Obtenir un code d'autorisation via une redirection de l'utilisateur vers Spotify.
Échanger ce code contre un jeton d'accès et un jeton d'actualisation via une requête HTTP sécurisée.
Cette implémentation permet également de stocker les tokens sensibles dans des cookies sécurisés et uniquement accessibles par le serveur (HTTP-only), garantissant la confidentialité et la sécurité.

### 🚀 Principales Méthodes Implémentées
1. **Configuration des Headers**
Cette méthode configure les headers HTTP requis pour interagir avec l'API Spotify, en incluant les identifiants client pour l'authentification.

```java

private HttpHeaders configHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(spotifyConfig.getClientId(), spotifyConfig.getClientSecret()); // Authentification avec ID et secret
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Type du contenu requis par Spotify
    return headers;
}
```
2. **Construction du Corps de Requête**
La méthode suivante crée le corps de la requête HTTP pour les échanges de tokens avec Spotify. Selon le type de grant, elle ajoute les paramètres nécessaires :

authorization_code : inclut le code reçu après authentification.
refresh_token : inclut le token d'actualisation pour générer un nouveau jeton d'accès.

```java

private MultiValueMap<String, String> configRequestBody(String grant_type, String value) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", grant_type);

    if (grant_type.equals("authorization_code")) body.add("code", value);
    else if (grant_type.equals("refresh_token")) body.add("refresh_token", value);

    String redirectUri = "http://localhost:3000/callback"; // URI utilisée dans le processus OAuth
    body.add("redirect_uri", redirectUri);

    return body;
}
```
3. **Stockage Sécurisé du Refresh Token**
Le refresh token est stocké dans un cookie HTTP-only, qui est sécurisé et non accessible par JavaScript, protégeant ainsi contre les attaques XSS.

```java
private void setCookieRefreshToken(String refreshToken, HttpServletResponse response) {
    Cookie cookie = new Cookie("refresh_token", refreshToken);
    cookie.setPath("/");
    cookie.setHttpOnly(true); // Rendre inaccessible via JavaScript
    cookie.setSecure(true); // Transmettre uniquement via HTTPS
    response.addCookie(cookie);
}
```
4. **Gestion du Token**
Cette méthode gère l'échange avec l'API Spotify et vérifie si un refresh token est inclus dans la réponse. Si oui, il est stocké dans un cookie.

```java
private Map<String, String> configureToken(String tokenType,
                                           HttpEntity<MultiValueMap<String, String>> request,
                                           HttpServletResponse httpServletResponse) {
    try {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://accounts.spotify.com/api/token";

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });

        Map<String, String> bodyResponse = response.getBody();

        if (bodyResponse != null) {
            if (bodyResponse.containsKey("refresh_token")) {
                setCookieRefreshToken(tokenType, httpServletResponse);
            }
            return bodyResponse;
        } else {
            throw new RuntimeException(tokenType + " not found in response.");
        }

    } catch (Exception e) {
        throw new RuntimeException("Error occurred while retrieving token: " + e.getMessage(), e);
    }
}
```

5. **Échange du Code d'Autorisation**
Cette méthode est responsable de l'échange d'un code d'autorisation contre un jeton d'accès et un jeton d'actualisation. Les étapes incluent :

Configuration des headers.
Construction du corps de requête.
Appel de l'API Spotify pour récupérer les tokens.

```java
public Map<String, String> exchangeCodeForToken(Map<String, String> payload,
                                                HttpServletResponse httpServletResponse) {
    String code = payload.get("code");

    HttpHeaders headers = configHeader();
    MultiValueMap<String, String> body = configRequestBody("authorization_code", code);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    return configureToken("refresh_token", request, httpServletResponse);
}
```
6. Rafraîchissement des Tokens
Cette méthode utilise un refresh token pour demander un nouveau jeton d'accès à l'API Spotify.

```java

public Map<String, String> configureRefreshToken(String refreshToken, HttpServletResponse httpServletResponse) {
    if (refreshToken == null || refreshToken.isEmpty()) {
        return null;
    }

    HttpHeaders headers = configHeader();
    MultiValueMap<String, String> body = configRequestBody("refresh_token", refreshToken);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    return configureToken("access_token", request, httpServletResponse);
}
```
### 🌐 Résumé du Workflow
Utilisateur redirigé vers Spotify :

L'utilisateur est redirigé avec l'URL de callback contenant le code d'autorisation.
Backend : échange de code :

Le code est envoyé à l'API Spotify pour obtenir des tokens.
Si un refresh token est inclus dans la réponse, il est stocké dans un cookie.
Rafraîchissement du jeton :

Lorsque le jeton d'accès expire, le refresh token est utilisé pour en obtenir un nouveau.
### ✅ Avantages
Sécurité accrue :

Stockage du refresh token dans un cookie HTTP-only sécurisé.
Les tokens sensibles ne sont pas exposés au frontend.
Authentification persistante :

Les utilisateurs n'ont pas besoin de se reconnecter tant que le refresh token est valide.
Conformité OAuth2 :

