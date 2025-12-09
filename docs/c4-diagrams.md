# C4 Diagrams - Slot Engine

This document contains the C4 architecture diagrams for the Slot Engine using Mermaid.

## Level 1: Context Diagram

Shows the system and its external users.

```mermaid
C4Context
    title Context Diagram - Slot Engine

    Person(gameOperator, "Game Operator", "Develops and configures slot games")
    Person(player, "Player", "Plays through a system that uses the engine")
    
    System(slotEngine, "Slot Engine", "Configurable and extensible slot game processing engine via plugins")
    
    System_Ext(gameSystem, "Game System", "External system that integrates the engine to offer games to players")
    
    Rel(gameOperator, slotEngine, "Configures games via JSON", "EngineConfig")
    Rel(gameOperator, slotEngine, "Develops custom plugins", "Plugin API")
    Rel(gameSystem, slotEngine, "Executes game commands", "Pipeline API")
    Rel(player, gameSystem, "Plays slots")
    
    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="1")
```

## Level 2: Container Diagram

Shows the main containers and modules of the system.

```mermaid
C4Container
    title Container Diagram - Slot Engine

    Container(engineCore, "Engine Core", "Go/Java", "Engine core with configurable pipeline and plugin system")
    Container(slotConfig, "Slot Configuration", "Go/Java + JSON", "Slot game configuration system (symbols, reels, displays, payTables)")
    Container(pluginSystem, "Plugin System", "Go/Java", "Extensible system to add custom functionality")
    Container(games, "Games", "Go/Java", "Concrete game implementations (e.g., Cluster Demo)")
    
    ContainerDb(configFiles, "Configuration Files", "JSON", "Game configuration files")
    
    Rel(games, engineCore, "Uses", "Game interface")
    Rel(games, slotConfig, "Loads configurations", "EngineConfigLoader")
    Rel(games, pluginSystem, "Registers plugins", "Plugin interface")
    Rel(slotConfig, configFiles, "Reads", "JSON")
    Rel(pluginSystem, engineCore, "Extends", "PipelineBuilder")
    Rel(engineCore, pluginSystem, "Executes", "Handler<I,O>")
    
    UpdateLayoutConfig($c4ShapeInRow="2", $c4BoundaryInRow="1")
```

## Level 3: Component Diagram - Engine Core

Shows the internal components of the engine core.

```mermaid
C4Component
    title Component Diagram - Engine Core

    Component(pipeline, "Pipeline", "Go/Java", "Orchestrates command execution through ordered steps")
    Component(pipelineBuilder, "PipelineBuilder", "Go/Java", "Builds and configures pipeline with commands, steps, and handlers")
    Component(stepOrderResolver, "StepOrderResolver", "Go/Java", "Resolves step execution order using topological sorting")
    Component(stepGraph, "StepGraph", "Go/Java", "Dependency graph between steps with priorities")
    
    Component(command, "Command", "Struct/Record", "Represents a game command (e.g., SPIN)")
    Component(step, "Step", "Struct/Record", "Represents a processing stage")
    Component(handler, "Handler<I,O>", "Interface", "Processes a step by transforming context")
    Component(context, "Context<I,O>", "Struct/Record", "Immutable context containing Input and Output")
    Component(input, "Input<T>", "Struct/Record", "Input: Command + Wager + Payload")
    Component(output, "Output<T>", "Struct/Record", "Output: Payload")
    
    Component(game, "Game", "Interface", "Interface for configuring games")
    Component(plugin, "Plugin", "Interface", "Interface for modular extensions")
    
    Rel(pipelineBuilder, game, "Registers", "configure()")
    Rel(pipelineBuilder, plugin, "Registers", "registerSteps/Handlers()")
    Rel(pipelineBuilder, command, "Registers")
    Rel(pipelineBuilder, step, "Registers with dependencies")
    Rel(pipelineBuilder, handler, "Associates to steps")
    Rel(pipelineBuilder, stepOrderResolver, "Uses to order steps")
    Rel(stepOrderResolver, stepGraph, "Builds and sorts")
    Rel(pipelineBuilder, pipeline, "Creates")
    
    Rel(pipeline, command, "Receives via Context")
    Rel(pipeline, step, "Executes sequentially")
    Rel(pipeline, handler, "Delegates processing")
    Rel(pipeline, context, "Passes and receives")
    
    Rel(context, input, "Contains")
    Rel(context, output, "Contains")
    Rel(input, command, "Contains")
    
    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="2")
```

## Level 3: Component Diagram - Slot Configuration

Shows the components of the slot configuration system.

```mermaid
C4Component
    title Component Diagram - Slot Configuration System

    Component(engineConfig, "EngineConfig", "Go/Java", "Complete engine configuration")
    Component(engineConfigLoader, "EngineConfigLoader", "Go/Java", "Loads configuration from JSON files")
    
    Component(symbolConfig, "SymbolConfig", "Go/Java", "Symbol configuration (name, flags)")
    Component(reelConfig, "ReelConfig", "Go/Java", "Reel configuration (symbols, weights, distribution)")
    Component(displayConfig, "DisplayConfig", "Go/Java", "Display configuration (columns, rows, reels)")
    Component(payTableConfig, "PayTableConfig", "Go/Java", "Pay table configuration")
    Component(stageConfig, "StageConfig", "Go/Java", "Game stage configuration")
    Component(betConfig, "BetConfig", "Go/Java", "Bet configuration")
    
    Component(jsonDeserializer, "JsonDeserializer", "Go/Java Util", "Custom JSON deserializer")
    
    ComponentDb(engineJson, "engine.json", "JSON File", "Game configuration file")
    
    Rel(engineConfigLoader, jsonDeserializer, "Uses")
    Rel(engineConfigLoader, engineJson, "Reads")
    Rel(engineConfigLoader, engineConfig, "Creates")
    
    Rel(engineConfig, symbolConfig, "Contains list")
    Rel(engineConfig, reelConfig, "Contains list")
    Rel(engineConfig, displayConfig, "Contains list")
    Rel(engineConfig, payTableConfig, "Contains list")
    Rel(engineConfig, stageConfig, "Contains list")
    Rel(engineConfig, betConfig, "Contains")
    
    Rel(displayConfig, reelConfig, "References by name")
    Rel(reelConfig, symbolConfig, "References by name")
    Rel(stageConfig, displayConfig, "References by name")
    Rel(stageConfig, payTableConfig, "References by name")
    Rel(payTableConfig, symbolConfig, "References by name")
    
    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="2")
```

## Sequence Diagram - Pipeline Execution

Shows the execution flow of a command through the pipeline.

```mermaid
sequenceDiagram
    participant Client
    participant Pipeline
    participant Handler1
    participant Handler2
    participant HandlerN
    
    Client->>Pipeline: execute(Context<I,O>)
    activate Pipeline
    
    Note over Pipeline: Gets Command from Context.Input
    Note over Pipeline: Fetches ordered Steps for Command
    
    loop For each Step
        Pipeline->>Pipeline: executeStep(context, command, step)
        Note over Pipeline: Fetches Handler for Step
        
        alt Step 1
            Pipeline->>Handler1: handle(context)
            activate Handler1
            Handler1->>Handler1: Processes Step logic
            Handler1-->>Pipeline: New Context
            deactivate Handler1
        else Step 2
            Pipeline->>Handler2: handle(context)
            activate Handler2
            Handler2->>Handler2: Processes Step logic
            Handler2-->>Pipeline: New Context
            deactivate Handler2
        else Step N
            Pipeline->>HandlerN: handle(context)
            activate HandlerN
            HandlerN->>HandlerN: Processes Step logic
            HandlerN-->>Pipeline: New Context
            deactivate HandlerN
        end
    end
    
    Pipeline-->>Client: Final Context with Output
    deactivate Pipeline
```

## Flow Diagram - Pipeline Construction

Shows how the pipeline is built and configured.

```mermaid
flowchart TD
    Start([Start]) --> CreateBuilder[Create PipelineBuilder]
    CreateBuilder --> RegisterGame[Register Game]
    
    RegisterGame --> GameConfig{Game.configure}
    GameConfig --> RegisterCommand[Register Commands]
    RegisterCommand --> RegisterPlugins[Register Plugins]
    
    RegisterPlugins --> PluginRegister{Plugin Register}
    PluginRegister --> RegisterSteps[Plugin registers Steps]
    RegisterSteps --> RegisterHandlers[Plugin registers Handlers]
    
    RegisterHandlers --> BuildPhase[Call build]
    
    BuildPhase --> OrderSteps[StepOrderResolver orders Steps]
    OrderSteps --> CreateGraph[Create StepGraph]
    CreateGraph --> TopSort[Topological Sorting]
    TopSort --> ValidateCycles{Cycles detected?}
    
    ValidateCycles -->|Yes| ThrowError[Throw StepOrderException]
    ValidateCycles -->|No| ValidateHandlers{Handlers for all Steps?}
    
    ValidateHandlers -->|No| ThrowError2[Throw HandlerNotRegistered]
    ValidateHandlers -->|Yes| CreatePipeline[Create Pipeline]
    
    CreatePipeline --> End([Pipeline Ready])
    
    ThrowError --> Error([Error])
    ThrowError2 --> Error
    
    style CreatePipeline fill:#90EE90
    style Error fill:#FFB6C1
    style End fill:#87CEEB
```

## Class Diagram - Core Domain Model

Shows the main classes and their relationships.

```mermaid
classDiagram
    class Pipeline {
        <<Go/Java>>
        -Map~Command, List~Step~~ steps
        -Map~Command, Map~Step, Handler~~ handlers
        +execute(Context) Context
        -executeStep(Context, Command, Step) Context
    }
    
    class PipelineBuilder {
        <<Go/Java>>
        -Set~Command~ commands
        -Map~Command, List~StepRule~~ stepRules
        -Map~Command, Map~Step, Handler~~ handlers
        +registerGame(Game) PipelineBuilder
        +registerPlugin(Command, Plugin) PipelineBuilder
        +registerCommand(Command) PipelineBuilder
        +registerStep(Command, Step, dependsOn, priority) PipelineBuilder
        +registerHandler(Command, Step, Handler) PipelineBuilder
        +build() Pipeline
    }
    
    class Game {
        <<interface>>
        +configure(PipelineBuilder)
    }
    
    class Plugin {
        <<interface>>
        +registerSteps(Command, PipelineBuilder)
        +registerHandlers(Command, PipelineBuilder)
    }
    
    class Handler~I,O~ {
        <<interface>>
        +handle(Context~I,O~) Context~I,O~
    }
    
    class Context~I,O~ {
        <<struct/record>>
        +Input~I~ input
        +Output~O~ output
        +withOutput(Output~O~) Context~I,O~
    }
    
    class Input~T~ {
        <<struct/record>>
        +Command command
        +Wager wager
        +T payload
    }
    
    class Output~T~ {
        <<struct/record>>
        +T payload
    }
    
    class Command {
        <<struct/record>>
        +String name
    }
    
    class Step {
        <<struct/record>>
        +String name
    }
    
    class StepRule {
        <<struct/record>>
        +Step step
        +Step dependsOn
        +int priority
    }
    
    class StepOrderResolver {
        <<utility>>
        +resolve(List~StepRule~)$ List~Step~
        -topologicalSort(StepGraph, List~StepRule~)$ List~Step~
    }
    
    class StepGraph {
        <<Go/Java>>
        -Map~Step, Set~Step~~ adjacencyList
        -Map~Step, Integer~ indegree
        -Map~Step, Integer~ priority
        +addRule(StepRule)
        +steps() Set~Step~
        +indegreeOf(Step) int
        +priorityOf(Step) int
        +dependentsOf(Step) Set~Step~
        +decrementIndegree(Step) int
    }
    
    class Wager {
        <<struct/record>>
        +long amount
    }
    
    PipelineBuilder --> Pipeline : creates
    PipelineBuilder --> Game : uses
    PipelineBuilder --> Plugin : uses
    PipelineBuilder --> StepOrderResolver : uses
    PipelineBuilder --> StepRule : manages
    
    StepOrderResolver --> StepGraph : uses
    StepRule --> Step : references
    
    Pipeline --> Command : uses
    Pipeline --> Step : executes
    Pipeline --> Handler : invokes
    Pipeline --> Context : processes
    
    Context --> Input : contains
    Context --> Output : contains
    Input --> Command : contains
    Input --> Wager : contains
    
    Handler --> Context : transforms
    
    Game ..> PipelineBuilder : configures
    Plugin ..> PipelineBuilder : extends
```

## Architectural Patterns Identified

### 1. **Pipeline Pattern**

- Sequential processing through stages (Steps)
- Each Handler transforms the Context
- Context immutability (records/structs)

### 2. **Command Pattern**

- Commands represent game actions (SPIN, etc.)
- Decoupling between request and execution

### 3. **Strategy Pattern**

- Handlers are interchangeable strategies
- Handler<I,O> interface allows different implementations

### 4. **Builder Pattern**

- PipelineBuilder for fluent construction
- Validation at build() stage

### 5. **Plugin Architecture**

- Extensible system via Plugin interface
- Dynamic registration of Steps and Handlers

### 6. **Dependency Injection**

- Handlers are injected into Pipeline via Builder
- Plugins configure dependencies

### 7. **Topological Sorting**

- Step ordering based on dependencies
- Cycle detection with StepGraph

### 8. **Immutable Domain Model**

- Records/Structs for Command, Step, Context, Input, Output
- Thread-safe security

## Technical Characteristics

### Extensibility

- ✅ Plugin system allows adding functionality
- ✅ Customizable Handlers per Step
- ✅ External configuration via JSON

### Configurability

- ✅ Steps ordered by dependencies and priorities
- ✅ Multiple Commands supported
- ✅ Complete slot configuration system

### Safety

- ✅ Immutability via records/structs
- ✅ Defensive copying in Pipeline
- ✅ Build-time validation

### Maintainability

- ✅ Clear separation of concerns
- ✅ Specific exceptions for each error
- ✅ Strongly typed code

### Performance

- ✅ Shareable immutable structures
- ✅ Efficient topological sorting (O(V+E))
- ✅ Maps for fast Handler access
