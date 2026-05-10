# Escampe

## 📋 Vue d'ensemble

Implémentation du jeu **Escampe** en Java.

## 📁 Structure du projet

```
escampe/
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
├── Testing
│   └── TestEscampeBoard.java     (Suite de 35 tests)
│
├── Documentation
│   ├── README.md                 (Ce fichier)
│   ├── FILE_FORMAT.md            (Format de fichier expliqué)
│   ├── GUIDE_TEST_COMPLET.md     (Guide complet des tests)
│   └── VERIFICATION_CONSIGNES.md (Vérification des consignes)
│
└── Test Files (créés à l'exécution)
    ├── escampe_board_figure5.txt
    ├── escampe_board_figure6.txt
    └── autres fichiers de test
```

## 🚀 Démarrage rapide

### Compilation
```bash
javac -d . src/*.java
```

### Exécution des tests
```bash
java src.TestEscampeBoard
```

### Démonstration interactive
```bash
java src.EscampeBoard
```

### Script automatisé
```bash
./run_tests.sh
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

### ✓ Placement initial
- [x] Noir: lignes 1-2
- [x] Blanc: lignes 5-6
- [x] 6 pièces par joueur (1 licorne + 5 paladins)
- [x] Validation stricte
- [x] Pas de chevauchement

### ✓ Format de fichier
- [x] Commentaires avec `%`
- [x] 6 lignes numérotées (01-06)
- [x] Numérotation de bas en haut
- [x] 6 colonnes A-F
- [x] Support Figures 5 et 6

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

## 📊 Tests couverts

| Catégorie | Tests | Status |
|-----------|-------|--------|
| Placement initial | 6 | ✓ PASS |
| Validation coups | 5 | ✓ PASS |
| Contraintes bordure | 2 | ✓ PASS |
| Règles de jeu | 7 | ✓ PASS |
| Fichier I/O | 8 | ✓ PASS |
| Cas limites | 7 | ✓ PASS |
| **TOTAL** | **35** | **✓ 100%** |

## 📖 Documentation

Consultez les fichiers suivants pour plus de détails:

1. **FILE_FORMAT.md** - Explication détaillée du format de fichier
2. **GUIDE_TEST_COMPLET.md** - Guide complet des tests et troubleshooting
3. **VERIFICATION_CONSIGNES.md** - Vérification point par point des consignes

## 🎯 Utilisation

### Exemple de partie
```java
EscampeBoard game = new EscampeBoard();

// Phase 1: Placement initial
game.play("C1/A1/B2/D2/E1/F2", "noir");
game.play("D6/B6/C5/E5/F6/A5", "blanc");

// Phase 2: Jeu régulier
game.play("B6-B5", "blanc");
game.play("C1-C2", "noir");

// Vérifier l'état
boolean valid = game.isValidMove("C2-C3", "blanc");
String[] moves = game.possiblesMoves("blanc");

// Vérifier fin de partie
if (game.gameOver()) {
    System.out.println("Partie terminée!");
}

// Sauvegarder
game.saveToFile("save.txt");

// Charger
EscampeBoard loaded = new EscampeBoard();
loaded.setFromFile("save.txt");
```

## 📝 Notes d'implémentation

- Sensibilité à la casse: "B6-B5" valide, "b6-b5" invalide
- Positions: A-F (colonnes), 1-6 (lignes)
- Coups possibles toujours inclus "E" pour passer
- Save/load cycle préserve l'état exact
- Format fichier strictement validé
