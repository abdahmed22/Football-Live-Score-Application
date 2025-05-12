# Football Live Score Application

## Overview

This project implements a **real-time sports live score system**. It allows an **Admin** to create and manage match scores, **Subscribers** to follow match updates live via WebSockets, and a **Statistics** user to query aggregated data. The system consists of a **Spring Boot Server** and a **Java Client** using both **HTTP** and **WebSocket** protocols.

---

## Features

### Server (Spring Boot + WebSocket)
- Handles HTTP requests and WebSocket connections from clients.
- Stores match information and real-time score updates.

#### REST Endpoints
- `POST /create`: Creates a match with given team names and returns a match ID.
- `GET /activeCount`: Returns the number of currently live matches.
- `GET /total/{team}`: Returns total goals scored by the specified team.
- `GET /score/{matchId}`: Returns score for the match or empty string if match doesn't exist.

#### WebSocket Functionality
- Clients can subscribe to match updates.
- Server broadcasts:
  - Score updates.
  - Match end notifications.
- Handles:
  - Score increment requests.
  - Match ending status changes.

---

### Client (Java HTTP & WebSocket)

A console-based application that supports three roles:

#### Admin
- Creates a match (via HTTP POST).
- Updates the score (via WebSocket) by selecting which team scored (A or B).
- Ends the match (via WebSocket), which terminates the session.

#### Subscriber
- Selects a match by ID to follow.
- Gets current score (via HTTP GET).
- Subscribes to live updates (via WebSocket).
- Automatically exits on match end or by user request.

#### Statistics
- Queries:
  - Number of active matches.
  - Total goals scored by a team.

