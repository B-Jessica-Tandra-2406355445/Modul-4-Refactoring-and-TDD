Nama: Jessica Tandra  
NPM: 2406355445  
Kelas: Adpro B  

# Reflection 4: Refactoring and TDD (1)
> Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this TDD flow is useful enough for you or not. If not, explain things that you need to do next time you make more tests.

Following the TDD workflow in this tutorial was quite helpful for me. Based on the self-reflective questions proposed by Percival (2017), I feel that the tests I wrote were able to support the main objectives of testing.

From the **Correctness** perspective, the tests help check whether the main functionalities, such as creating and updating orders, behave as expected. I also tried to include some edge cases, for example when the status input is invalid or when the product list is empty. However, I realize that most of my tests are still focused on unit-level behavior. Next time, I think I should also add more functional tests so I can better ensure that the application works properly from the user's point of view.

In terms of **Maintainability**, having tests made it easier for me to refactor the code with more confidence. For instance, when I refactored the `Order` model to use the `OrderStatus` enum instead of primitive strings, the existing tests helped confirm that the behavior of the system was still correct. Writing tests first also encouraged me to think about how the classes and methods should be structured before implementing them.

Regarding **productive workflow**, the feedback cycle felt relatively fast because the unit tests run very quickly. I was able to run specific tests, such as `OrderTest` or `OrderServiceImplTest`, whenever I wanted to check a particular component. Since the dependencies were stubbed using Mockito, the tests stayed lightweight and did not require external systems to run.

For future improvements, I think I should start incorporating integration tests to verify whether different components work correctly when combined. In addition, I would like to explore ways to write functional tests that are still efficient so that the feedback cycle remains fast.

> You have created unit tests in Tutorial. Now reflect whether your tests have successfully followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you create more tests.

Based on the F.I.R.S.T. principles, most of the unit tests I created generally follow these guidelines.

1. **Fast:** Since they do not depend on slow external systems. By using stubs with Mockito in `OrderServiceImplTest`, the tests can run quickly and repeatedly without waiting for external resources.
2. **Isolated/Independent:** Each test initializes its own data using the `@BeforeEach` setup method, which helps avoid interference between tests. The repository layer is also replaced with test doubles so that the service layer can be tested independently.
3. **Repeatable:** Because they do not rely on changing external conditions. Since dependencies like `OrderRepository` are stubbed, the tests should produce consistent results across different runs and environments.
4. **Self-Validating:** I used assertions such as `assertEquals`, `assertThrows`, and `assertNull` so that the test results are automatically evaluated without requiring manual inspection. I avoided manual `print` functions and tried to keep the scope of the assertions as small and accurate as possible.
5. **Thorough/Timely:** The tests were timely and thorough, because they were written before implementing the production code as part of the TDD workflow. I also tried to include both happy path and unhappy path scenarios, such as successful ID lookups and cases where invalid inputs are provided.

However, I realize that being thorough does not only mean achieving high coverage. In the future, I need to be more careful in designing test cases so that they truly validate important behaviors of the system, instead of only executing lines of code to increase coverage.


<details>
    <summary><b>Reflection 3: Maintainability and OO Principles</b></summary>

# Reflection 3: Maintainability and OO Principles
> Explain what principles you apply to your project!

Here is how I applied the SOLID principle to improve this project:
### Single Responsibility Principle (SRP)
I applied the SRP by ensuring that each class has a single, well-defined responsibility to avoid tight coupling. In this project, I separated `CarController` from `ProductController` into its own file and removed the inheritance relationship (extends), as the previous structure forced CarController to inherit unrelated product routing logic. I also enforced a strict layered architecture where Controllers solely handle HTTP requests, Services manage business logic, and Repositories focus exclusively on data access. This clear separation ensures that changes in one module, such as updating the storage mechanism in the Repository, do not impact the request handling logic in the Controller.

### Open/Closed Principle (OCP)
I applied the OCP to ensure that software entities are open for extension but closed for modification. I achieved this by introducing `CarRepository` and `ProductRepository` interfaces, ensuring that services like `CarServiceImpl` depend only on these abstractions rather than concrete implementations. This design allows me to extend or switch the underlying data storage mechanism (e.g., from an in-memory list to a database) by simply creating a new repository implementation without modifying the existing service logic. Additionally, the `ModelAbstract` class allows the system to be easily extended with new vehicle types by inheriting common attributes, ensuring that new features can be added without altering the core logic or existing models.

### Liskov Substitution Principle (LSP)
I applied the LSP by ensuring that inheritance hierarchies strictly adhere to behavioral subtyping, meaning a subclass must be substitutable for its superclass without breaking the application. Previously, `CarController` extended `ProductController`, which violated LSP because `CarController` inherited unrelated product routing logic and could not safely substitute `ProductController` without causing unexpected side effects. I resolved this by removing the improper inheritance and introducing `ModelAbstract` as a proper base class for both `Car` and `Product`. This ensures that both entities can now be used interchangeably where `ModelAbstract` is expected, such as in common UUID generation logic—without altering the correctness or consistency of the program behavior.

### Interface Segregation Principle (ISP)
I applied the ISP to prevent "fat" interfaces that force classes to depend on methods they do not use. Instead of creating a generic service interface, I defined specific interfaces: `CarService` and `ProductService`. This design ensures that implementing classes (like `CarServiceImpl`) are not forced to implement irrelevant methods. Simultaneously, it protects clients like `CarController` by exposing only the car-related operations it actually needs, keeping the dependencies focused and decoupled.

### Dependency Inversion Principle (DIP)
I applied DIP by refactoring my controllers (high-level modules) to depend on interfaces rather than concrete classes (low-level modules). Previously, `CarController` depended on a concrete class `CarServiceImpl`. I decoupled this by changing the dependency to the `CarService` interface. I also used Constructor Injection with Spring's container to handle these dependencies properly, ensuring the implementation details are decoupled from the controller logic.

> Explain the advantages of applying SOLID principles to your project with examples.

Applying SOLID principles has significantly improved the project's maintainability, extensibility, and testability:
1. Improved Maintainability (SRP): Separating CarController from ProductController ensures each class has a focused responsibility. This isolates code changes, preventing modifications in car logic from breaking product features. 
2. Enhanced Extensibility (OCP): Utilizing the CarService interface allows us to switch implementations (e.g., from a temporary list to a database) without touching the existing CarController code. 
3. Better Testability (DIP): Depending on interfaces rather than concrete classes enables easy dependency injection. We can now inject "Mock" services to test the controller in isolation without relying on the actual implementation. 
4. Increased Reliability (LSP & ISP): Removing the inheritance between CarController and ProductController (LSP) and splitting large interfaces (ISP) prevents "code pollution," ensuring classes only have access to the methods they actually need.

> Explain the disadvantages of not applying SOLID principles to your project with examples.

Not applying SOLID principles to our project can lead to a codebase that is rigid, fragile, and difficult to maintain. Without SRP, classes like CarController would become bloated with multiple responsibilities, making the code complex and hard to debug. Ignoring OCP and DIP leads to tight coupling, where a simple change (like switching storage types) forces us to rewrite the core controller logic, increasing the risk of introducing new bugs. Furthermore, disregarding LSP and ISP causes "code pollution," where classes inherit irrelevant methods (e.g., CarController inheriting Product routes) or depend on unused interfaces, leading to unexpected behavior and a system that is confusing to extend.

</details>

<details>
    <summary><b>Reflection 2: CI/CD and DevOps</b></summary>

# Reflection 2: CI/CD and DevOps
> List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

1. **Avoid Field Dependency Injection**: I refactored `ProductController` and `ProductServiceImpl` to use constructor-based injection instead of using `@Autowired` directly on fields. This ensures objects are instantiated in a valid state and makes unit testing much easier.
2. **Extract Duplicated String Literals**: I replaced multiple instances of the "redirect:/product/list" string with a single constant variable. This reduces the risk of typos and makes the code easier to maintain.
3. **Remove Redundant HTML Attributes**: I removed the `role="button"` attribute from standard anchor (<a>) tags. Since standard HTML elements already have inherent semantics, removing this prevents screen reader confusion and improves accessibility.
4. **Delete Empty Setup Methods**: I completely removed an empty `setUp()` method in my test class. This eliminates dead code and keeps the test class clean and focused.
5. **Group Gradle Dependencies**: I reorganized `build.gradle.kts` to group dependencies logically by their destination. This makes the build configuration file much easier to read and manage.
6. **Remove Unnecessary Exceptions**: I removed `throws Exception` declarations from test methods that didn't actually throw any checked exceptions. This cleans up the method signatures and improves code readability.

> Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

Yes, I believe my current implementation has successfully met the definition of both Continuous Integration (CI) and Continuous Deployment (CD). For the CI part, whenever I push new changes to the repository, the GitHub Action workflows (ci.yml and scorecard.yml) automatically execute. These workflows reliably build the project, run the test, and perform code quality and security analysis using SonarCloud and OSSF Scorecard. This automated process ensures that any new code is continuously verified and free from major bugs or code smells. As for the CD part, the deployment process is fully automated using a pull-based approach. Once the CI workflows passes, Koyeb automatically detects the update and deploys the latest master branch of the application without requiring any manual intervention. All of these reasons show that my project has successfully implemented a complete CI/CD pipeline.  

</details>

<details>
    <summary><b>Reflection 1: Coding Standards and Secure Coding</b></summary>

# Reflection 1: Coding Standards and Secure Coding
In this tutorial, I applied several clean code principles and secure coding practices, including:
1. **Meaningful names**  
I used descriptive and self-explanatory names for variables, methods, and classes that clearly indicate their purpose without needing extra comments.
2. **Avoiding duplication (DRY)**  
I have avoided code duplication by reusing components that I have previously made.
3. **Single Responsibility Principle**  
I separated the application logic into three distinct layers: Controller, Service, and Repository. Each class has only one responsibility, making the code modular and easier to test.
4. **Functions do one thing**  
I ensured that methods are small and focused. For instance, the `create` method only adds a product to the list. 
5. **UUID for identifiers**  
I implemented UUID for identifiers like product IDs. This prevents ID enumeration attacks, making it harder for attackers to guess other product IDs.

I also identified a few areas that need improvement to meet better standards:
1. **Lack of input validation**  
Currently, user can enter an empty name or a negative number for quantity. I should implement validation annotations in the Product model.
2. **Improper error handling**  
In `ProductController`, if `service.findById(productId)` returns `null`, I currently just redirect back to the list. It would be better to show an error message to inform the user.

</details>

<details>
    <summary><b>Reflection 2: Unit Test and Functional Test</b></summary>

# Reflection 2: Unit Test and Functional Test
1. After writing unit tests, I feel more confident about my code because they help me understand how it behaves in different situations, including edge cases and possible errors. There is no fixed number of tests required for a class, it depends on how complex the class is and how many functions it has. Ideally, each method should have at least one unit test to cover different input cases and edge cases. To check whether the tests are sufficient, we can use code coverage tools to see which parts of the code are executed during testing and find areas that are not tested yet. However, even if we reach 100% code coverage, it does not mean the code is free from bugs, because coverage only shows that the code runs, not that it works correctly as we wanted or not.

2. When creating a new functional test suite, simply copying variables and setup steps from previous tests is not a clean approach. It is not aligned with the do not repeat yourself principle. It causes duplicated code, which makes the program harder to maintain and update later. For example, repeating the same base URL setup in many places can lead to inconsistency if changes are needed. A better solution is to move shared setup code into a base functional test class or helper method so it can be reused. This keeps the test code simpler, more organized, and easier to manage.

</details>