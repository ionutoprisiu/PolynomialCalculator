package dataModels;

/**
 * Represents a single term in a polynomial, such as 3x^2.
 * Stores both the coefficient and the exponent as integers.
 */
public class Monomial {
    private int coefficient;
    private int exponent;

    public Monomial(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public int getExponent() {
        return exponent;
    }

    /**
     * Adds another monomial to this one, assuming they have the same exponent.
     * @param other the other Monomial
     * @return a new Monomial whose coefficient is the sum of the two coefficients
     * @throws IllegalArgumentException if exponents differ
     */
    public Monomial add(Monomial other) {
        if (this.exponent != other.exponent) {
            throw new IllegalArgumentException("Exponents must match to add monomials.");
        }
        return new Monomial(this.coefficient + other.coefficient, this.exponent);
    }

    /**
     * Returns a new Monomial with the coefficient negated.
     */
    public Monomial negate() {
        return new Monomial(-coefficient, exponent);
    }

    /**
     * Multiplies this monomial by another. Exponents are added and coefficients are multiplied.
     */
    public Monomial multiply(Monomial other) {
        return new Monomial(this.coefficient * other.coefficient, this.exponent + other.exponent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Monomial monomial = (Monomial) obj;
        return coefficient == monomial.coefficient && exponent == monomial.exponent;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + coefficient;
        result = 31 * result + exponent;
        return result;
    }

    /**
     * Returns a string representation. Examples:
     *  - Coefficient = 5, Exponent = 2 => "5x^2"
     *  - Coefficient = -1, Exponent = 1 => "-1x"
     *  - Coefficient = 3, Exponent = 0 => "3"
     */
    @Override
    public String toString() {
        if (exponent == 0) {
            // Just the constant term
            return String.valueOf(coefficient);
        } else if (exponent == 1) {
            // e.g. "5x"
            return coefficient + "x";
        } else {
            // e.g. "5x^3"
            return coefficient + "x^" + exponent;
        }
    }
}
