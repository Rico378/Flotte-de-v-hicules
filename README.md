# Gestionnaire Flotte de Véhicules

## Description

Cette application Java permet de piloter une flotte automobile (véhicules légers, lourds, urgences), les chauffeurs et leurs missions. Elle intègre un suivi de maintenance kilométrique et des statistiques en temps réel. Les données sont sécurisées par une persistance automatique (sérialisation) et des exports CSV.

##  Technologie d'interface
Nous avons choisi Java Swing (Option A) pour sa robustesse en environnement desktop. L'usage de JTable avec des TableModels personnalisés et du pattern Observer permet une gestion dynamique des données.

##  Instructions de compil et d'execution
# Prérequis
Il faut au minimum Java 11 ou version supérieure installé sur votre machine.

# Compilation
Il faut lancer le fichier compile.bat (Windows) compile.sh(Linux/Mac) à la racine du dossier
L'application se lance automatiquement

# Liste des fonctionnalités implémentées
Architecture MVC complète : Séparation totale entre le modèle, la vue et le contrôleur.

Gestion CRUD : Création, lecture, mise à jour et suppression pour les véhicules, les missions et les chauffeurs.

Hiérarchie de classes complexe : Utilisation de classes abstraites (Véhicule, Mission) et de 4 interfaces métier (Assignable, Maintenable, Trackable, Urgence).

Généricité bornée : Utilisation de Registre<T extends Véhicule> avec gestion de wildcards pour la manipulation des collections.

Traitements avancés (Streams) : Filtrage multicritères, tris dynamiques sur plusieurs colonnes et calculs statistiques (taux de disponibilité, coûts, etc.).

Persistance des données : Sauvegarde et chargement automatique via sérialisation Java et export manuel en CSV.

Gestion des erreurs : Utilisation d'exceptions métier personnalisées (ex: ChauffeurIndisponibleException).

# Arborescence
```
TP Flotte véhicule/
├── compile.bat          # Script Windows
├── compile.sh           # Script Linux/Mac
├── README.md            # Ce fichier
├── src/                 # Code source Java
├── bin/                 # Fichiers compilés (créé à la compilation)
└── resources/           # Données persistées (créé à l'exécution)
```

```
src/
├── model/           # Modèle métier (classes, interfaces, exceptions)
├── view/            # Interface graphique Swing
├── controller/      # Logique applicative (GestionnaireFlotte)
├── util/            # Utilitaires (PersistenceManager)
```

# Répartition des tâches

Aymeric :
- Différents fichier Java
- Synthèse
- Powerpoint

Benjamin :
- Texte Powerpoint
- Push fichier Java
- README.md
