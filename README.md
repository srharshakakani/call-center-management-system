# 📞 Call Center Management System

A full-stack application that simulates a real-world call center system with call queuing, agent assignment, and workload-based distribution.

Built using **Spring Boot (Backend)** and **Java Swing (Frontend UI)**.

---

# 🚀 Features

## 🔹 Call Management

* Create new calls with priority levels (LOW, MEDIUM, HIGH, CRITICAL)
* View all calls and queued calls
* Update call status (QUEUED → IN_PROGRESS → RESOLVED)

## 🔹 Smart Agent Assignment

* Automatically assigns calls to the **least-loaded available agent**
* Ensures fair distribution of workload
* Prevents overloading agents

## 🔹 Agent Management

* Create and view agents from UI
* Agent lifecycle:

    * AVAILABLE → BUSY → AVAILABLE

## 🔹 Queue Handling

Calls are prioritized based on:

1. Priority (CRITICAL → LOW)
2. Created time (FIFO within same priority)

## 🔹 Dashboard

Displays real-time summary:

* Total calls
* Queued / In Progress / Resolved / Escalated
* Available vs Busy agents

## 🔹 UI (Java Swing)

* Queue View
* New Call Form
* Agent Management Panel
* Dashboard View
* Auto-refresh after actions

---

# 🏗️ Tech Stack

**Backend**

* Java 17
* Spring Boot
* Spring Data JPA (Hibernate)
* H2 In-Memory Database

**Frontend**

* Java Swing

**Build Tool**

* Maven

---

# 🧠 Architecture

```
Controller → Service → Repository → Database
```

* **Controller** → REST APIs
* **Service** → Business logic (assignment, validation)
* **Repository** → Database interaction
* **UI** → Communicates via HTTP APIs (no direct DB access)

---

# ⚙️ Setup Instructions

## 1. Clone Repository

```
git clone <your-repo-url>
cd call-center-management-system
```

---

## 2. Run Backend

```
./mvnw spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## 3. Run UI

Run:

```
MainUI.java
```

from your IDE (IntelliJ recommended)

---

# 🧪 How to Use

## Step 1: Create Agents

* Go to **Agents Tab**
* Click **Create Agent**

## Step 2: Create Calls

* Go to **New Call Tab**
* Submit call details

## Step 3: Assign Calls

* Go to **Queue Tab**
* Select a call
* Click **Mark In Progress**

👉 System automatically assigns the **least-loaded available agent**

---

# 🔁 Call Lifecycle

```
QUEUED → IN_PROGRESS → RESOLVED
```

* IN_PROGRESS requires an agent
* RESOLVED frees the agent

---

# 📡 API Endpoints

## Calls

* `POST /calls` → Create call
* `GET /calls` → Get all calls
* `GET /calls/queue` → Get queued calls
* `POST /calls/{id}/update?status=IN_PROGRESS` → Update call

## Agents

* `POST /agents` → Create agent
* `GET /agents` → Get agents

## Dashboard

* `GET /dashboard/summary` → Summary stats

---

# ⚠️ Notes

* Uses **H2 in-memory database** (data resets on restart)
* No authentication (out of scope)
* UI uses simple JSON parsing for demonstration purposes

---

# ⭐ Future Improvements

* Skill-based routing
* Agent selection dropdown in UI
* Persistent DB (PostgreSQL/MySQL)
* Replace manual JSON parsing with Jackson
* Add authentication
* Real-time updates (WebSockets)

---

# 💡 Design Decisions

* Calls and agents are decoupled
* Assignment happens dynamically (not at creation)
* Backend-driven business logic
* UI acts as HTTP client

---

# 👨‍💻 Author

**Sree Harsha Kakani**

---

# 🎯 Summary

This project demonstrates:

* REST API design
* Backend business logic
* UI integration
* State management
* Real-world system modeling

---
