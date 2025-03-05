package businessLogic;

import dataModels.Monomial;
import dataModels.Polynomial;

import java.util.*;

public class Operations {

    /**
     * Adds two polynomials and returns the result.
     * @param p1 First polynomial
     * @param p2 Second polynomial
     * @return Sum of the two polynomials
     */
    public static Polynomial add(Polynomial p1, Polynomial p2) {
        Polynomial result = new Polynomial();
        // Copy all monomials from p1 initially
        Map<Integer, Monomial> resultMonomials = new TreeMap<>(p1.getMonomials());
        // Merge each monomial from p2
        for (Map.Entry<Integer, Monomial> entry : p2.getMonomials().entrySet()) {
            int exponent = entry.getKey();
            Monomial monomial = entry.getValue();
            // If exponents match, use Monomial::add; otherwise insert the new monomial
            resultMonomials.merge(exponent, monomial, Monomial::add);
        }
        result.setMonomials(resultMonomials);
        return result;
    }

    /**
     * Subtracts polynomial p2 from polynomial p1 and returns the result (p1 - p2).
     * @param p1 First polynomial (minuend)
     * @param p2 Second polynomial (subtrahend)
     * @return The difference (p1 - p2)
     */
    public static Polynomial subtract(Polynomial p1, Polynomial p2) {
        Polynomial result = new Polynomial();
        // Start with all monomials from p1
        Map<Integer, Monomial> resultMonomials = new TreeMap<>(p1.getMonomials());
        // Negate the monomials in p2 and then merge
        for (Map.Entry<Integer, Monomial> entry : p2.getMonomials().entrySet()) {
            int exponent = entry.getKey();
            Monomial monomial = entry.getValue().negate();
            resultMonomials.merge(exponent, monomial, Monomial::add);
        }
        result.setMonomials(resultMonomials);
        return result;
    }

    /**
     * Multiplies two polynomials and returns the product.
     * @param p1 First polynomial
     * @param p2 Second polynomial
     * @return Product of the two polynomials
     */
    public static Polynomial multiply(Polynomial p1, Polynomial p2) {
        Polynomial result = new Polynomial();

        for (Map.Entry<Integer, Monomial> e1 : p1.getMonomials().entrySet()) {
            for (Map.Entry<Integer, Monomial> e2 : p2.getMonomials().entrySet()) {
                int newExponent = e1.getKey() + e2.getKey();
                Monomial product = e1.getValue().multiply(e2.getValue());
                result.getMonomials().merge(newExponent, product, Monomial::add);
            }
        }

        return result;
    }

    /**
     * Divides one polynomial by another using integer long division for polynomials.
     * Returns an array of size 2: [quotient, remainder].
     *
     * @param dividend The polynomial to be divided
     * @param divisor The polynomial to divide by
     * @return An array containing the quotient at index 0 and the remainder at index 1
     * @throws IllegalArgumentException if the divisor is the zero polynomial
     */
    public static Polynomial[] divide(Polynomial dividend, Polynomial divisor) {
        if (divisor.isZero()) {
            throw new IllegalArgumentException("Division by the zero polynomial is not allowed.");
        }

        Polynomial quotient = new Polynomial();
        Polynomial remainder = new Polynomial(dividend); // Make a copy of dividend

        int divisorLeadingDegree = divisor.getDegree();
        Monomial divisorLeadingMonomial = divisor.getMonomials().get(divisorLeadingDegree);

        // While the remainder has an equal or higher degree than the divisor
        while (!remainder.isZero() && remainder.getDegree() >= divisorLeadingDegree) {
            int remainderLeadingDegree = remainder.getDegree();
            Monomial remainderLeadingMonomial = remainder.getMonomials().get(remainderLeadingDegree);

            // Compute the degree difference and coefficient for the next term in the quotient
            int degreeDiff = remainderLeadingDegree - divisorLeadingDegree;
            int quotientCoeff = remainderLeadingMonomial.getCoefficient()
                    / divisorLeadingMonomial.getCoefficient();

            // Construct a single-term polynomial (the partial quotient)
            Polynomial quotientTerm = new Polynomial();
            quotientTerm.setMonomials(Collections.singletonMap(
                    degreeDiff, new Monomial(quotientCoeff, degreeDiff)));

            // Add this partial quotient to the overall quotient
            quotient = add(quotient, quotientTerm);

            // Multiply the divisor by this partial quotient term
            Polynomial product = multiply(divisor, quotientTerm);

            // Subtract from the remainder
            remainder = subtract(remainder, product);

            // Remove leading zero if created
            if (remainder.getMonomials().containsKey(remainder.getDegree())
                    && remainder.getMonomials().get(remainder.getDegree()).getCoefficient() == 0) {
                remainder.getMonomials().remove(remainder.getDegree());
            }
        }

        return new Polynomial[]{quotient, remainder};
    }

    /**
     * Checks if p1 / p2 will yield an integer polynomial (no fractional coefficients).
     * This is determined by verifying:
     * (1) The leading coefficient of p1 is an integer multiple of the leading coefficient of p2.
     * (2) The degree of p1 is at least as large as that of p2.
     *
     * @param p1 Dividend
     * @param p2 Divisor
     * @return True if the quotient would be an integer polynomial, false otherwise.
     */
    public static boolean isDivisionResultInteger(Polynomial p1, Polynomial p2) {
        Monomial leadingMonomial1 = p1.getMonomials().get(p1.getDegree());
        Monomial leadingMonomial2 = p2.getMonomials().get(p2.getDegree());

        // If either leading monomial is null, we can't do integer division in the usual sense
        if (leadingMonomial1 == null || leadingMonomial2 == null || leadingMonomial2.getCoefficient() == 0) {
            return false;
        }

        // Check divisibility of coefficients and exponent condition
        boolean divisionIsInteger = (leadingMonomial1.getCoefficient()
                % leadingMonomial2.getCoefficient() == 0);
        boolean exponentCondition = leadingMonomial1.getExponent() >= leadingMonomial2.getExponent();

        return divisionIsInteger && exponentCondition;
    }

    /**
     * Computes the derivative of the given polynomial.
     * @param poly The polynomial to differentiate
     * @return The derivative polynomial
     */
    public static Polynomial derivative(Polynomial poly) {
        Polynomial derivative = new Polynomial();

        for (Map.Entry<Integer, Monomial> entry : poly.getMonomials().entrySet()) {
            int exp = entry.getKey();
            int coeff = entry.getValue().getCoefficient();

            // The derivative of a constant term (exp = 0) is 0, so skip it
            if (exp == 0) {
                continue;
            }

            // d/dx (coeff * x^exp) = coeff * exp * x^(exp - 1)
            int newCoeff = coeff * exp;
            int newExp = exp - 1;

            derivative.getMonomials().put(newExp, new Monomial(newCoeff, newExp));
        }
        return derivative;
    }

    /**
     * Computes the indefinite integral of the given polynomial (without a constant).
     * For each term a * x^n, the integral is (a / (n+1)) * x^(n+1).
     * Since we keep things in integer form, the code also handles a GCD-based reduction.
     *
     * @param poly The polynomial to integrate
     * @return The integrated polynomial (coefficients might not remain pure integers if simplified,
     *         but this function is set up to manage integer coefficients with a factor in integralToString).
     */
    public static Polynomial integral(Polynomial poly) {
        Polynomial integral = new Polynomial();
        Map<Integer, Monomial> integralMonomials = new TreeMap<>();

        for (Map.Entry<Integer, Monomial> entry : poly.getMonomials().entrySet()) {
            int exponent = entry.getKey();
            int coefficient = entry.getValue().getCoefficient();

            // For a*x^n, the integrated term is a*x^(n+1)/(n+1)
            // We store the new exponent as n+1, the raw coefficient remains 'coefficient'.
            int newExponent = exponent + 1;
            Monomial newMonomial = new Monomial(coefficient, newExponent);

            integralMonomials.put(newExponent, newMonomial);
        }

        integral.setMonomials(integralMonomials);
        return integral;
    }

    /**
     * Converts an integrated polynomial into a string with each term displayed
     * as a fraction if necessary (coefficient / exponent).
     *
     * Example: if the integral is 3x^2 in raw form, we interpret that as 3/2 x^2, etc.
     *
     * @param integral Polynomial that has already been integrated
     * @return A string representation that includes fractional coefficients, plus "+ C" at the end.
     */
    public static String integralToString(Polynomial integral) {
        StringBuilder result = new StringBuilder();

        // We process terms in descending order of exponent
        List<Map.Entry<Integer, Monomial>> entries = new ArrayList<>(integral.getMonomials().entrySet());
        Collections.reverse(entries);

        for (Map.Entry<Integer, Monomial> entry : entries) {
            int exponent = entry.getKey();
            int coefficient = entry.getValue().getCoefficient();

            // Skip the trivial case 0 x^0
            if (exponent != 0) {
                int denominator = exponent;
                // gcd to reduce fraction (coefficient / exponent)
                int gcd = gcd(coefficient, denominator);
                coefficient /= gcd;
                denominator /= gcd;

                // Build the term string
                String term;
                if (denominator == 1) {
                    // If denominator is 1, it means no fraction needed
                    term = String.format("%dx^%d", coefficient, exponent);
                } else {
                    // fraction format
                    term = String.format("(%d/%d)x^%d", coefficient, denominator, exponent);
                }

                if (result.length() > 0) {
                    result.append(" + ");
                }
                result.append(term);
            }
        }

        // Always append "+ C" (the constant of integration) if there's at least one term
        if (result.length() > 0) {
            result.append(" + C");
        } else {
            // If the integral is 0, just show 'C'
            result.append("C");
        }

        return result.toString();
    }

    /**
     * Greatest Common Divisor (Euclidean algorithm).
     */
    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
