# Configuration de la base de données

## 1. Créer la base PostgreSQL

Ouvrir **pgAdmin 4**, puis créer une nouvelle base de données nommée :

```text
pathfinder
```

---

## 2. Charger le fichier `schema.sql`

Dans pgAdmin 4 :

1. Sélectionner la base `pathfinder`.
2. Ouvrir **Query Tool**.
3. Cliquer sur l'icône **Open File**.
4. Ouvrir le fichier :

```text
src/main/resources/database/schema.sql
```

5. Cliquer sur **Execute** ou appuyer sur `F5`.

Après l'exécution, les tables suivantes doivent apparaître dans :

```text
pathfinder
→ Schemas
→ public
→ Tables
```

Tables attendues :

```text
grid
grid_cell
algo_run
path_cell
```

Si les tables n'apparaissent pas immédiatement, faire :

```text
clic droit sur Tables
→ Refresh
```

---

## 3. Configurer le fichier `.env`

À la racine du projet, créer ou modifier le fichier :

```text
.env
```

Exemple :

```env
DB_URL=jdbc:postgresql://localhost:5432/pathfinder
DB_USER=postgres
DB_PASSWORD=VOTRE_MOT_DE_PASSE_POSTGRESQL
```

Remplacer :

```text
VOTRE_MOT_DE_PASSE_POSTGRESQL
```

par le mot de passe PostgreSQL utilisé sur votre ordinateur.

Chaque membre du groupe doit utiliser son propre mot de passe PostgreSQL.

---

## 4. Tester la connexion

Lancer le projet depuis NetBeans.

Si la connexion fonctionne, la console doit afficher :

```text
Connected to PostgreSQL!
```

avec :

```text
BUILD SUCCESS
```
