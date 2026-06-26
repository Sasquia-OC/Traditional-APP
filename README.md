# ☕ FocusFlow
Description : Une application web en Java pour la gestion des taches quotidiennes.

## 🛠️ Stack Technique

- **Backend :** Java (NetBeans Project)
- **Frontend :** HTML5, CSS3, JavaScript (Dossier Web indépendant)
- **Données :** Local (localhost:8080)
- **Gestionnaire de build :** Apache Ant

## 📦 Prérequis

- **Java Development Kit (JDK) :** Version 11 ou supérieure.
- **Apache Ant :** Installé pour exécuter les builds en ligne de commande (ou utilisez directement l'IDE NetBeans).

## 🚀 Installation et Exécution

### 1. Cloner le projet
```bash
git clone https://github.com
cd Test
```

### 2. Compiler avec Apache Ant
Ouvrez votre terminal à la racine du projet (où se trouve `build.xml`) :

* **Nettoyer le projet :**
  ```bash
  ant clean
  ```
* **Compiler et générer l'exécutable :**
  ```bash
  ant jar
  ```

Le fichier exécutable généré se trouvera dans le dossier `dist/Test.jar`.

### 3. Lancer l'application
```bash
java -jar dist/Test.jar
```

---

## 📂 Structure du Projet

Voici l'organisation des fichiers :

```text
├── Web/               # Fichiers de l'interface utilisateur
│   ├── app.js         # Logique JavaScript globale
│   ├── index.html     # Page d'accueil principale
│   └── style.css      # Feuilles de style CSS
├── src/test/          # Code source Java (.java)
├── nbproject/         # Fichiers de configuration spécifiques à NetBeans
├── build.xml          # Script de build Apache Ant
├── manifest.mf        # Manifeste pour l'archive JAR
├── build/             # Fichiers de compilation temporaires (générés)
└── dist/              # Livrables finaux contenant le fichier Test.jar (générés)
```

## 📄 Licence

Ce projet est sous licence MIT.
