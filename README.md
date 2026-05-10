# Escampe

## Binôme
HNEINI Ola, SLIMANI Inès

## 📁 Structure du projet

```
src/
├── Core Classes
│   ├── EscampeBoard.java         (Classe principale - implémente Partie1)
│   ├── Partie1.java              (Interface requise)
│   ├── Board.java                (Gestion de l'état du plateau)
│   ├── Position.java             (Coordonnées)
│   ├── Piece.java                (Représentation des pièces)
│   ├── Border.java               (Types de bordure)
│   ├── MoveValidator.java        (Validation des coups)
│   └── FileHandler.java          (I/O fichiers)
│
└── Board State Files
    ├── figure5_board.txt
    ├── figure6_board.txt
    └── current_board.txt
```

## 🚀 Démarrage rapide

### Compilation
```bash
javac -d . src/*.java
```

### Démonstration interactive
```bash
java src.EscampeBoard
```

### Exécution des tests
```bash
java src.TestEscampeBoard
```

## 📚 Classes principales

### 1. **EscampeBoard** (Classe principale)
Implémente l'interface `Partie1` complètement:
- Gestion de l'état du jeu
- Placement initial des pièces
- Validation et exécution des coups
- Sauvegarde/chargement de fichiers
- Détection de fin de partie

### 2. **Board**
Représente l'état du plateau:
- Grille 6x6 avec types de bordure
- Gestion des pièces
- Suivi de la dernière position jouée

### 3. **MoveValidator**
Valide les coups selon les règles:
- Contraintes de bordure
- Distance de mouvement
- Absence de collision
- Capture de licorne

### 4. **FileHandler**
Gère l'I/O avec format spécifique:
- Chargement depuis fichier texte
- Sauvegarde avec format commentaires
- Support Figures 5 et 6

### 5. **TestEscampeBoard**
Suite complète de tests (35 tests):
- Placement initial (6 tests)
- Validation des coups (5 tests)
- Contraintes de bordure (2 tests)
- Règles de jeu (7 tests)
- Fichier I/O (8 tests)
- Cas limites (7 tests)

## 🎮 Formats de coup

### Coup régulier
```
"B1-D1"  (déplace pièce de B1 vers D1)
```

### Placement initial
```
"C6/A6/B5/D5/E6/F5"  (licorne en C6, paladins en autres positions)
```

### Skip tour
```
"E"  (passer le tour)
```

## 📄 Format de fichier

```
% ABCDEF
01 bb---- 01
02 -Bb-bb 02
03 ------ 03
04 ------ 04
05 -n-n-n 05
06 n-N-n- 06
% ABCDEF
```

**Légende:**
- `N` = Licorne noire
- `n` = Paladin noir
- `B` = Licorne blanche
- `b` = Paladin blanc
- `-` = Case vide

## 🔍 Consignes respectées

### ✓ Interface Partie1
- [x] `setFromFile()` - Charge depuis fichier
- [x] `saveToFile()` - Sauvegarde dans fichier
- [x] `isValidMove()` - Valide un coup
- [x] `possiblesMoves()` - Retourne coups possibles
- [x] `play()` - Exécute un coup
- [x] `gameOver()` - Détecte fin de partie

### ✓ Format de fichier
- [x] Commentaires avec `%`
- [x] 6 lignes numérotées (01-06)
- [x] 6 colonnes A-F

### ✓ Règles de jeu
- [x] Début: jeu non terminé
- [x] Fin: licorne capturée par paladin
- [x] Alternance des joueurs
- [x] Option skip tour

### ✓ Gestion des erreurs
- [x] Rejet format invalide
- [x] Rejet position invalide
- [x] Rejet joueur invalide
- [x] Sensibilité à la casse
