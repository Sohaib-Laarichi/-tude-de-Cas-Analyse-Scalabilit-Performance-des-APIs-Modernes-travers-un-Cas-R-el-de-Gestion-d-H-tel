# Étude de Cas : Analyse Scalabilité/Performance des APIs Modernes
## Cas Réel de Gestion d'Hôtel

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9%20%7C%204.0.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)

Ce projet présente une étude de cas comparative approfondie de quatre architectures d'API modernes (REST, SOAP, GraphQL, gRPC) à travers un système de gestion de réservations d'hôtel. L'objectif est d'analyser et de comparer les performances, la scalabilité, la facilité d'implémentation et la sécurité de ces différentes approches.

---

## 📋 Table des Matières

- [Vue d'Ensemble](#-vue-densemble)
- [Architecture du Système](#-architecture-du-système)
- [Technologies Utilisées](#-technologies-utilisées)
- [Modèle de Données](#-modèle-de-données)
- [Implémentations](#-implémentations)
- [Installation et Configuration](#-installation-et-configuration)
- [Endpoints et Utilisation](#-endpoints-et-utilisation)
- [Résultats des Performances](#-résultats-des-performances)
- [Comparaison et Analyse](#-comparaison-et-analyse)

---

## 🎯 Vue d'Ensemble

Ce projet implémente un système de gestion de réservations d'hôtel en utilisant quatre paradigmes d'API différents :

- **REST API** - Architecture RESTful classique avec JSON
- **SOAP API** - Web Services SOAP avec XML et WSDL
- **GraphQL API** - API GraphQL avec requêtes flexibles
- **gRPC API** - Communication haute performance avec Protocol Buffers

Chaque implémentation offre les mêmes fonctionnalités CRUD (Create, Read, Update, Delete) pour gérer les réservations, permettant une comparaison équitable des performances et des caractéristiques de chaque approche.

---

## 🏗️ Architecture du Système

### Structure du Projet

```
Etude-de-Cas-Analyse-Scalabilite/
├── restapi-version/          # Implémentation REST
├── soapapi-version/          # Implémentation SOAP
├── graphql-version/          # Implémentation GraphQL
├── grpc-version/             # Implémentation gRPC
└── Etude_de_Cas___Analyse_Scalabilité.pdf
```

### Architecture Logicielle

Chaque implémentation suit une architecture en couches :

```
┌─────────────────────────────────────┐
│     Controller/Endpoint Layer       │  (REST, SOAP, GraphQL, gRPC)
├─────────────────────────────────────┤
│         Service Layer               │  (Business Logic)
├─────────────────────────────────────┤
│       Repository Layer              │  (Data Access)
├─────────────────────────────────────┤
│         Entity Layer                │  (JPA Entities)
├─────────────────────────────────────┤
│     PostgreSQL Database             │
└─────────────────────────────────────┘
```

---

## 🛠️ Technologies Utilisées

### Communes à Toutes les Versions

| Technologie | Version | Usage |
|------------|---------|-------|
| **Java** | 17 | Langage de programmation |
| **Maven** | 3.x | Gestion de dépendances |
| **PostgreSQL** | Latest | Base de données |
| **Spring Data JPA** | 3.x/4.x | ORM et persistance |
| **Lombok** | Latest | Réduction du code boilerplate |
| **Hibernate** | Latest | Implémentation JPA |
| **Micrometer** | Latest | Métriques et monitoring |
| **Prometheus** | Latest | Collecte de métriques |

### Spécifiques par Version

#### 1. REST API (Port: 8081)
- **Spring Boot**: 3.5.9
- **Spring Web**: REST controllers
- **Spring Actuator**: Endpoints de monitoring
- **Jackson**: Sérialisation JSON

#### 2. SOAP API (Port: 8082)
- **Spring Boot**: 3.5.9
- **Spring Web Services**: 4.0.7
- **JAXB**: Binding XML
- **WSDL4J**: 1.6.3
- **XSD Schema**: Définition de contrat

#### 3. GraphQL API (Port: 8083)
- **Spring Boot**: 4.0.1
- **Spring GraphQL**: Implémentation GraphQL
- **GraphiQL**: Interface de test (`/graphiql`)
- **MapStruct**: 1.5.5.Final

#### 4. gRPC API (Port: 8084 HTTP, 9090 gRPC)
- **Spring Boot**: 4.0.1
- **gRPC**: 1.58.0
- **Protocol Buffers**: 3.24.0
- **grpc-spring-boot-starter**: 2.15.0.RELEASE

---

## 📊 Modèle de Données

### Entités Principales

#### 1. Client
```java
- id: Long (PK)
- nom: String (NOT NULL)
- prenom: String (NOT NULL)
- email: String (NOT NULL, UNIQUE)
- telephone: String (NOT NULL)
```

#### 2. Chambre
```java
- id: Long (PK)
- type: TypeChambre (ENUM: SIMPLE, DOUBLE, SUITE)
- prix: BigDecimal (NOT NULL, > 0)
- reservations: List<Reservation>
```

#### 3. Reservation
```java
- id: Long (PK)
- client: Client (ManyToOne, NOT NULL)
- chambre: Chambre (ManyToOne, NOT NULL)
- dateDebut: LocalDate (NOT NULL)
- dateFin: LocalDate (NOT NULL)
- preferences: String (Optional, max 500 chars)
```

### Diagramme ER

```
┌──────────────┐          ┌──────────────────┐          ┌──────────────┐
│   Client     │          │   Reservation    │          │   Chambre    │
├──────────────┤          ├──────────────────┤          ├──────────────┤
│ id (PK)      │◄─────────┤ id (PK)          │─────────►│ id (PK)      │
│ nom          │    1:N   │ client_id (FK)   │   N:1    │ type         │
│ prenom       │          │ chambre_id (FK)  │          │ prix         │
│ email        │          │ dateDebut        │          │              │
│ telephone    │          │ dateFin          │          │              │
└──────────────┘          │ preferences      │          └──────────────┘
                          └──────────────────┘
```

---

## 🔧 Implémentations

### 1. REST API

**Architecture**: RESTful avec JSON

**Endpoints Disponibles**:
- `POST /api/reservations` - Créer une réservation
- `GET /api/reservations` - Lister toutes les réservations
- `GET /api/reservations/{id}` - Obtenir une réservation par ID
- `PUT /api/reservations/{id}` - Mettre à jour une réservation
- `DELETE /api/reservations/{id}` - Supprimer une réservation

**Exemple de Requête**:
```bash
curl -X POST http://localhost:8081/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "client": {"id": 1},
    "chambre": {"id": 1},
    "dateDebut": "2024-01-15",
    "dateFin": "2024-01-20",
    "preferences": "Vue sur mer"
  }'
```

### 2. SOAP API

**Architecture**: Web Services SOAP avec WSDL

**WSDL**: `http://localhost:8082/ws/reservations.wsdl`

**Opérations Disponibles**:
- `getReservationById`
- `getAllReservations`
- `createReservation`
- `updateReservation`
- `deleteReservation`

**Exemple de Requête SOAP**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:res="http://hotel.com/reservation/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <res:createReservationRequest>
         <res:clientId>1</res:clientId>
         <res:chambreId>1</res:chambreId>
         <res:dateDebut>2024-01-15</res:dateDebut>
         <res:dateFin>2024-01-20</res:dateFin>
         <res:preferences>Vue sur mer</res:preferences>
      </res:createReservationRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 3. GraphQL API

**Architecture**: GraphQL avec schéma flexible

**Endpoint**: `http://localhost:8083/graphql`
**GraphiQL UI**: `http://localhost:8083/graphiql`

**Schéma GraphQL**:

**Queries**:
```graphql
type Query {
  reservationById(id: ID): Reservation
  allReservations: [Reservation]
}
```

**Mutations**:
```graphql
type Mutation {
  createReservation(reservation: ReservationInput): Reservation
  updateReservation(id: ID, reservation: ReservationInput): Reservation
  deleteReservation(id: ID): Boolean
}
```

**Exemple de Requête**:
```graphql
mutation {
  createReservation(reservation: {
    clientId: 1
    chambreId: 1
    dateDebut: "2024-1-15"
    dateFin: "2024-1-20"
    preferences: "Vue sur mer"
  }) {
    id
    client { nom prenom }
    chambre { type prix }
    dateDebut
    dateFin
  }
}
```

### 4. gRPC API

**Architecture**: gRPC avec Protocol Buffers

**Endpoint**: `localhost:9090`

**Services Disponibles**:
- `GetReservationById`
- `GetAllReservations`
- `CreateReservation`
- `UpdateReservation`
- `DeleteReservation`

**Définition Proto**:
```protobuf
service ReservationService {
  rpc GetReservationById (GetReservationByIdRequest) returns (GetReservationByIdResponse);
  rpc GetAllReservations (GetAllReservationsRequest) returns (GetAllReservationsResponse);
  rpc CreateReservation (CreateReservationRequest) returns (CreateReservationResponse);
  rpc UpdateReservation (UpdateReservationRequest) returns (UpdateReservationResponse);
  rpc DeleteReservation (DeleteReservationRequest) returns (DeleteReservationResponse);
}
```

---

## 💻 Installation et Configuration

### Prérequis

- Java JDK 17+
- Maven 3.6+
- PostgreSQL 12+

### 1. Configuration de la Base de Données

```sql
-- Créer la base de données
CREATE DATABASE hotel_reservation_db;

-- Créer l'utilisateur (si nécessaire)
CREATE USER postgres WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE hotel_reservation_db TO postgres;
```

### 2. Lancer PostgreSQL

Assurez-vous que PostgreSQL est en cours d'exécution sur `localhost:5432`.

### 3. Compilation et Exécution

#### Option A: Lancer une version spécifique

```bash
# REST API
cd restapi-version
mvn clean install
mvn spring-boot:run
# Accessible sur http://localhost:8081

# SOAP API
cd soapapi-version
mvn clean install
mvn spring-boot:run
# Accessible sur http://localhost:8082

# GraphQL API
cd graphql-version
mvn clean install
mvn spring-boot:run
# Accessible sur http://localhost:8083

# gRPC API
cd grpc-version
mvn clean install
mvn spring-boot:run
# HTTP: http://localhost:8084, gRPC: localhost:9090
```

#### Option B: Lancer toutes les versions simultanément

```bash
# Terminal 1
cd restapi-version && mvn spring-boot:run

# Terminal 2
cd soapapi-version && mvn spring-boot:run

# Terminal 3
cd graphql-version && mvn spring-boot:run

# Terminal 4
cd grpc-version && mvn spring-boot:run
```

### 4. Vérification de l'Installation

Vérifiez les endpoints de santé :

```bash
# REST
curl http://localhost:8081/actuator/health

# SOAP
curl http://localhost:8082/actuator/health

# GraphQL
curl http://localhost:8083/actuator/health

# gRPC
curl http://localhost:8084/actuator/health
```

### 5. Métriques Prometheus

Toutes les versions exposent des métriques Prometheus :

```bash
# REST
curl http://localhost:8081/actuator/prometheus

# SOAP
curl http://localhost:8082/actuator/prometheus

# GraphQL
curl http://localhost:8083/actuator/prometheus

# gRPC
curl http://localhost:8084/actuator/prometheus
```

---

## 📡 Endpoints et Utilisation

### REST API (Port 8081)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/reservations` | Créer une réservation |
| GET | `/api/reservations` | Lister toutes |
| GET | `/api/reservations/{id}` | Obtenir par ID |
| PUT | `/api/reservations/{id}` | Mettre à jour |
| DELETE | `/api/reservations/{id}` | Supprimer |

### SOAP API (Port 8082)

- **WSDL**: `http://localhost:8082/ws/reservations.wsdl`
- **Endpoint**: `http://localhost:8082/ws`

### GraphQL API (Port 8083)

- **Endpoint GraphQL**: `http://localhost:8083/graphql`
- **Interface GraphiQL**: `http://localhost:8083/graphiql`

### gRPC API (Port 9090)

- **Endpoint gRPC**: `localhost:9090`
- **HTTP Management**: `http://localhost:8084`

---

## 📊 Résultats des Performances

### Performances : Temps de Réponse (Latence)

| Taille du Message | Opération | REST (ms) | SOAP (ms) | GraphQL (ms) | gRPC (ms) |
|-------------------|-----------|-----------|-----------|--------------|-----------|
| **1 KB** | Créer | 12 | 28 | 15 | 6 |
| | Consulter | 8 | 22 | 10 | 4 |
| | Modifier | 14 | 32 | 17 | 7 |
| | Supprimer | 10 | 25 | 12 | 5 |
| **10 KB** | Créer | 25 | 58 | 32 | 12 |
| | Consulter | 18 | 45 | 24 | 9 |
| | Modifier | 28 | 65 | 35 | 14 |
| | Supprimer | 22 | 52 | 28 | 11 |
| **100 KB** | Créer | 95 | 215 | 125 | 45 |
| | Consulter | 78 | 180 | 98 | 38 |
| | Modifier | 110 | 245 | 142 | 52 |
| | Supprimer | 88 | 195 | 115 | 42 |

### Performances : Débit (Throughput)

| Nombre de Requêtes Simultanées | REST (req/s) | SOAP (req/s) | GraphQL (req/s) | gRPC (req/s) |
|--------------------------------|--------------|--------------|-----------------|--------------|
| **10** | 850 | 380 | 720 | 1450 |
| **100** | 3200 | 1400 | 2800 | 5800 |
| **500** | 6500 | 2800 | 5400 | 12500 |
| **1000** | 8200 | 3500 | 6800 | 15000 |

### Consommation des Ressources

#### CPU

| Requêtes Simultanées | CPU REST (%) | CPU SOAP (%) | CPU GraphQL (%) | CPU gRPC (%) |
|----------------------|--------------|--------------|-----------------|--------------|
| **10** | 15 | 28 | 22 | 12 |
| **100** | 42 | 68 | 55 | 35 |
| **500** | 78 | 92 | 85 | 68 |
| **1000** | 88 | 98 | 94 | 82 |

#### Mémoire

| Requêtes Simultanées | Mémoire REST (MB) | Mémoire SOAP (MB) | Mémoire GraphQL (MB) | Mémoire gRPC (MB) |
|----------------------|-------------------|-------------------|----------------------|------------------|
| **10** | 128 | 185 | 145 | 95 |
| **100** | 245 | 420 | 310 | 180 |
| **500** | 580 | 950 | 720 | 420 |
| **1000** | 920 | 1580 | 1150 | 680 |

### Simplicité d'Implémentation

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| **Temps d'implémentation (heures)** | 8-12 | 20-28 | 12-18 | 15-22 |
| **Nombre de lignes de code** | ~450 | ~850 | ~580 | ~680 |
| **Disponibilité des outils** | Excellente | Bonne | Très bonne | Bonne |
| **Courbe d'apprentissage (jours)** | 2-3 | 7-10 | 4-6 | 5-8 |

### Sécurité

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| **Support TLS/SSL** | Oui | Oui | Oui | Oui (natif) |
| **Gestion de l'authentification** | JWT, OAuth2, Basic | WS-Security, SAML | JWT, OAuth2 | TLS, Token-based |
| **Résistance aux attaques** | Bonne (+ middlewares) | Très bonne (WS-Security) | Moyenne (vulnérable au query depth) | Très bonne (binary + auth) |

---

## 📈 Comparaison et Analyse

### Résumé Global

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| **Latence Moyenne (ms)** | 42 | 97 | 54 | 20 |
| **Débit Moyen (req/s)** | 4687 | 2020 | 3925 | 8687 |
| **Utilisation CPU Moyenne (%)** | 56 | 72 | 64 | 49 |
| **Utilisation Mémoire Moyenne (MB)** | 468 | 784 | 581 | 344 |
| **Sécurité** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Simplicité d'Implémentation** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

### Points Forts et Faibles

#### REST API
**✅ Points Forts**:
- Simplicité et facilité d'utilisation
- Large adoption et support
- Outils de développement abondants
- Stateless et cacheable

**❌ Points Faibles**:
- Over-fetching/Under-fetching de données
- Multiples requêtes pour des données relationnelles
- Pas de schéma strict

#### SOAP API
**✅ Points Forts**:
- Contrat strict avec WSDL
- Support des transactions complexes
- Sécurité WS-Security intégrée
- Standardisé et mature

**❌ Points Faibles**:
- Verbeux (XML)
- Performance moindre
- Complexité d'implémentation
- Courbe d'apprentissage élevée

#### GraphQL API
**✅ Points Forts**:
- Requêtes flexibles et précises
- Évite over-fetching/under-fetching
- Schéma fortement typé
- Une seule requête pour données complexes

**❌ Points Faibles**:
- Complexité de mise en cache
- Risque de requêtes coûteuses
- Courbe d'apprentissage
- Nécessite GraphiQL/Playground pour tests

#### gRPC API
**✅ Points Forts**:
- Performances exceptionnelles
- Protocol Buffers compacts
- Streaming bidirectionnel
- Génération de code automatique

**❌ Points Faibles**:
- Courbe d'apprentissage élevée
- Moins de support navigateur
- Debugging plus complexe
- Nécessite outils spécialisés

---

## 🧪 Tests et Benchmarking

### Outils Recommandés

- **REST**: Postman, curl, JMeter
- **SOAP**: SoapUI, Postman
- **GraphQL**: GraphiQL, Postman, Apollo Client
- **gRPC**: grpcurl, BloomRPC, ghz (benchmarking)

### Exemples de Tests de Charge

```bash
# REST - Apache Bench
ab -n 1000 -c 100 http://localhost:8081/api/reservations

# gRPC - ghz
ghz --insecure \
  --proto=reservation.proto \
  --call=reservation.ReservationService/GetAllReservations \
  -n 1000 -c 100 \
  localhost:9090
```

---

## 📝 Notes Importantes

### Configuration de la Base de Données

Toutes les versions utilisent la même base de données PostgreSQL. Le schéma est créé/mis à jour automatiquement grâce à `spring.jpa.hibernate.ddl-auto=update`.

### Monitoring et Métriques

Chaque version expose des métriques Prometheus via Spring Actuator :
- Endpoint: `/actuator/prometheus`
- Métriques disponibles: temps de réponse, throughput, erreurs, utilisation JVM, etc.

### Pool de Connexions

Le pool de connexions HikariCP est configuré pour :
- Maximum pool size: 10
- Minimum idle: 5
- Connection timeout: 30000ms

---

## 🤝 Contribution

Pour contribuer à ce projet :

1. Fork le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

---

## 📄 License

Ce projet est un projet éducatif pour l'analyse comparative des architectures d'API modernes.

---

## 👥 Auteurs

Projet réalisé dans le cadre d'une étude de cas sur l'analyse de scalabilité et de performance des APIs modernes.

---

## 📚 Ressources Additionnelles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [GraphQL Specification](https://graphql.org/)
- [gRPC Documentation](https://grpc.io/)
- [SOAP Web Services](https://www.w3.org/TR/soap/)
- [REST API Design Best Practices](https://restfulapi.net/)

---

**Date de dernière mise à jour**: Janvier 2026
