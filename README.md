# Polynomial Calculator

Polynomial Calculator is a Java-based application that allows users to perform arithmetic operations on polynomials—including addition, subtraction, multiplication, division, differentiation, and integration—via an intuitive Swing-based graphical user interface (GUI).

## Features

- **Arithmetic Operations:**  
  Perform addition, subtraction, multiplication, and division on polynomials.

- **Differentiation & Integration:**  
  Compute the derivative or the indefinite integral (with simplified fractional representation) of any polynomial.

- **User-Friendly GUI:**  
  Interact with the application through a simple Swing interface that supports error handling and clear feedback.

- **Robust Data Models:**  
  Custom `Monomial` and `Polynomial` classes handle all polynomial operations and parsing from string inputs.

## Project Structure

- **businessLogic:**  
  Contains `Operations.java`, which implements core polynomial arithmetic operations such as add, subtract, multiply, divide, derivative, and integral.

- **dataModels:**  
  - `Monomial.java`: Represents a single term in a polynomial (e.g., `3x^2`).  
  - `Polynomial.java`: Represents a polynomial as a collection of monomials, including parsing from string expressions.

- **graphicalUserInterface:**  
  - `Calculator.java`: The main calculator window that allows for polynomial arithmetic operations.  
  - `DerivativeIntegralDialog.java`: A dialog window to calculate the derivative or integral of a polynomial.

- **org.example:**  
  Contains `Main.java`, the entry point for the application.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):**  
  Ensure you have JDK 8 (or higher) installed on your system.

- **Development Environment (Optional):**  
  Use an IDE like IntelliJ IDEA, Eclipse, or NetBeans for easier project management and debugging.

### Installation

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/YourUsername/PolynomialCalculator.git
    ```

2. **Import the Project:**

    - Open your IDE and import the project as a Java project.

3. **Compile the Project:**

    - Use your IDE's build tools or compile via the command line. For example:
    
    ```bash
    javac -d bin src/**/*.java
    ```

4. **Run the Application:**

    - Execute the main class located at `org.example.Main`:
    
    ```bash
    java -cp bin org.example.Main
    ```

## Usage

1. **Launch the Calculator:**
   - Run the application to open the main GUI window.

2. **Enter Polynomials:**
   - Input your polynomials in the provided text fields. Valid formats include:
     - `3x^2 - 2x + 1`
     - `-x^2 + x - 5`
     - `x` or `-x`
     - `4` or `-7`

3. **Perform Operations:**
   - Click on the buttons (Add, Subtract, Multiply, Divide) to perform the corresponding operation.
   - For division, the app checks if the result is an integer polynomial and displays an appropriate message if not.

4. **Derivative & Integral:**
   - Click the **D&I** button to open the derivative and integral dialog.
   - Enter a polynomial and click **Derivative** to compute its derivative or **Integral** to compute its integral.
   - The computed result will appear in the dialog’s result field.

5. **View the Result:**
   - The result of the operation will be shown in the result text field in the main window or dialog.

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature/YourFeature
    ```
3. Make your changes and commit them:
    ```bash
    git commit -m "Add feature/YourFeature"
    ```
4. Push your branch:
    ```bash
    git push origin feature/YourFeature
    ```
5. Open a Pull Request on GitHub.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

- **Author:** Your Name
- **Email:** ionut0203@gmail.com
- **GitHub:** [ionutoprisiu](https://github.com/ionutoprisiu)
