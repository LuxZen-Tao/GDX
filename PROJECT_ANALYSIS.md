# GDX Project Analysis

## Project Classification

**Type:** Business Simulation Game - Pub/Bar Management Tycoon

**Framework:** libGDX 1.14.0 (Cross-platform Java game framework)

**Language:** Java 17

**Build System:** Gradle multi-module project

## Architecture

### Module Structure

- **`sim`** (~16,000 LOC): Core simulation engine with all game logic
- **`core`** (~478 LOC): libGDX UI layer and game screens
- **`lwjgl3`**: Desktop launcher using LWJGL3 backend

### Key Systems

#### Economic Engine
- Multi-layered debt management (credit lines, loan sharks, supplier credit)
- Credit scoring system with dynamic utilization tracking
- Debt spiral mechanics with cascading penalties
- Bankruptcy and bailiff systems
- Weekly rent and operating cost calculations
- Inn/room rental side business

#### Staff Management
- FOH (Front of House) and BOH (Back of House) staff
- Morale, misconduct, and performance tracking
- Wage payment system with traffic/service penalties
- Dynamic staff event generation

#### Operations
- Day/night cycle with 20-round service periods (36 min/round)
- Customer traffic simulation
- Food and wine inventory management
- Music system with multiple profiles
- 20+ pub activities (Quiz Night, Live Band, DJ Night, Sports Night, etc.)

#### Reputation & Identity
- Reputation score (-100 to +100)
- Pub identity system: Rowdy, Respectable, Artsy, Underground, Family-Friendly
- Rumor and reputation spreading mechanics
- Security/bouncer quality management
- Chaos and violence tracking

#### Progression
- Milestone and achievement system
- Pub level advancement
- Upgrade system for facility improvements
- VIP regular customers with preferences
- Rival pub mechanics
- Prestige system

#### Calendar & Events
- Season-based market pressures
- Event card system
- Time phase management (opening, service, closing)

## Code Structure Observations

### Strengths
- Clean separation between simulation logic and presentation
- Extensive domain modeling with appropriate use of enums
- Sophisticated interconnected game systems
- Realistic business simulation mechanics

### Technical Debt
- `Simulation.java` is 253KB (estimated 5,000+ lines) - clear God Object anti-pattern
- Opportunities for decomposition into focused subsystems
- Could benefit from further modularization

## Game Genre

Tycoon/Management Simulation with:
- Resource management depth
- Staff management complexity
- Economic strategy elements
- Progressive unlock mechanics
- Time-based operational challenges

**Comparable titles:** Game Dev Tycoon, Two Point Hospital (but pub-focused)

## Target Audience

Players who enjoy:
- Deep economic simulation
- Multi-system management challenges
- Strategic decision-making under constraints
- Progressive complexity and unlocks

## Current Development Status

- Core simulation engine: Feature-complete
- UI: Functional with basic screens (boot, menu, game)
- HUD displays: Cash, debt, reputation, chaos, week/day, traffic
- Controls: Service management and time advancement
- Project appears in active development
