**Mastermind** is a small Android game built using Jetpack Compose for UI, Koin for dependency injection, Kotlin Coroutines and Flow.
At the start the app generates a random 4 letter secret from A to Z and prints it to the console for convenience (you can filter in logcat "Generated secret").
You have 1 minute to guess the secret using 4 input boxes and a Check button.
After each check, every box is colored based on the evaluation result. <br> <br>
<img width="200" alt="Screenshot_20260226_232421" src="https://github.com/user-attachments/assets/db69132c-4675-4036-92f3-d1525f273395" />
<img width="200" alt="Screenshot_20260226_232439" src="https://github.com/user-attachments/assets/97fa684a-d3c8-43b3-9e26-7892e6d49e4c" />
<img width="200" alt="Screenshot_20260226_232335" src="https://github.com/user-attachments/assets/c0d3d57f-a065-44c4-b4e4-da4d79d70150" />

<br>
The project also uses Navigation Compose for screen transitions and JUnit with Mockito for unit testing.

## Architecture
The project follows Clean Architecture. MVVM is used in the presentation layer, with small inspirations from MVI. To keep things simple, the layers are separated by packages rather than multiple app modules.

### Presentation Layer

Each screen exposes a single immutable state object that represents the entire UI.
All user interactions are modeled as actions that are handled centrally inside the ViewModel.
The ViewModel processes actions, updates the state, and emits one off events such as navigation.

Compose screens are fully state hoisted.
They receive state and callbacks and do not depend directly on a ViewModel instance.
This keeps the UI layer pure and significantly improves testability.
Because composables are independent from ViewModel wiring, it becomes very easy to mock state and add Compose UI tests in the future.

### Domain Layer

The domain layer contains the core game logic such as secret generation, guess evaluation, and the countdown timer.
The timer is implemented using Flow so it can be deterministically tested with coroutine test utilities.
All business rules are isolated from Android framework code.

### Data Layer

A repository abstraction is used between presentation and domain logic.

In this specific example the repository is very thin, but I kept it to demonstrate clean separation and to show how the architecture would scale if the game logic were backed by a database or remote source.

For this small game the repository layer is technically unnecessary, but I included it to demonstrate clean layering and future extensibility.

Overall the structure keeps the project small and simple while clearly showing how it could scale into a larger production ready architecture without major refactoring.


## Testing

The project contains unit tests for:

- Game logic and evaluation rules  
- Secret generation  
- Countdown timer flow behavior  
- ViewModel state transitions and event emission  

Coroutines test utilities are used to control dispatchers and verify Flow emissions.
The architecture makes the logic fully testable without depending on Android framework classes.
