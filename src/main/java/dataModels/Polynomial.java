package dataModels;

import java.util.*;

/**
 * Represents a polynomial made up of multiple Monomials.
 * Internally stores them in a Map<Integer, Monomial>,
 * with the key being the exponent.
 */
public class Polynomial {
    private Map<Integer, Monomial> monomials;

    /**
     * Default constructor creates an empty polynomial (0).
     */
    public Polynomial() {
        monomials = new TreeMap<>();
    }

    /**
     * Copy constructor. Makes a deep copy of the given polynomial.
     */
    public Polynomial(Polynomial polynomial) {
        this.monomials = new HashMap<>();
        for (Map.Entry<Integer, Monomial> entry : polynomial.getMonomials().entrySet()) {
            this.monomials.put(
                    entry.getKey(),
                    new Monomial(entry.getValue().getCoefficient(), entry.getValue().getExponent())
            );
        }
    }

    /**
     * Constructs a polynomial from a string, e.g. "3x^2 - 2x + 1".
     * Negative signs are handled by a split trick: replace "-" with "+-" before splitting on "+".
     */
    public Polynomial(String polynomial) {
        monomials = new HashMap<>();
        parsePolynomial(polynomial);
    }

    /**
     * Parses a polynomial string into monomials.
     * Examples of valid input:
     *  - "3x^2 - 2x + 1"
     *  - "-x^2 + x - 5"
     *  - "x" or "-x"
     *  - "4" or "-7"
     */
    private void parsePolynomial(String polynomial) {
        // Replace '-' with '+-' to simplify splitting
        polynomial = polynomial.replace("-", "+-");

        // Split on '+'
        String[] terms = polynomial.split("\\+");
        for (String term : terms) {
            term = term.trim();
            if (term.isEmpty()) {
                continue;
            }

            int coeff;
            int exp = 0;

            // Cases:
            // 1) "-x" or "x"
            // 2) "2x", "2x^3"
            // 3) plain numbers like "5" or "-3"

            if (term.matches("-?x")) {
                // Something like 'x' or '-x'
                coeff = term.equals("x") ? 1 : -1;
                exp = 1;
            } else if (term.contains("x")) {
                // Something like '2x', '-2x', '2x^3', etc.
                String[] parts = term.split("x\\^?");
                // parts[0] might be '2', '-2', '' (if 'x' alone), or '-'
                // parts[1] might be exponent if present, or missing if just '2x'

                // Coefficient
                if (parts[0].isEmpty() || parts[0].equals("-")) {
                    // This handles when the part before 'x' is empty or just '-'
                    coeff = parts[0].equals("-") ? -1 : 1;
                } else {
                    coeff = Integer.parseInt(parts[0]);
                }

                // Exponent
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    exp = Integer.parseInt(parts[1]);
                } else {
                    // If there's no ^number, exponent is 1 (e.g., "2x")
                    exp = 1;
                }
            } else {
                // Pure number, no 'x'
                coeff = Integer.parseInt(term);
                exp = 0;
            }

            // If there's already a monomial with that exponent, add up
            monomials.put(exp, monomials.getOrDefault(exp, new Monomial(0, exp))
                    .add(new Monomial(coeff, exp)));
        }
    }

    public Map<Integer, Monomial> getMonomials() {
        return monomials;
    }

    public void setMonomials(Map<Integer, Monomial> monomials) {
        this.monomials = monomials;
    }

    /**
     * @return The highest exponent with a non-zero coefficient, or -1 if polynomial is empty.
     */
    public int getDegree() {
        if (monomials.isEmpty()) {
            return -1;
        }
        return monomials.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * @return True if this polynomial is effectively 0 (no terms or all zero coefficients).
     */
    public boolean isZero() {
        return monomials.isEmpty() || monomials.values().stream().allMatch(m -> m.getCoefficient() == 0);
    }

    /**
     * @param exponent The exponent to check
     * @return The integer coefficient of the term with that exponent, or 0 if not present.
     */
    public int getCoefficient(int exponent) {
        Monomial monomial = monomials.get(exponent);
        return monomial != null ? monomial.getCoefficient() : 0;
    }

    /**
     * Sets or replaces the coefficient for a given exponent.
     */
    public void setCoefficient(int exponent, int coefficient) {
        monomials.put(exponent, new Monomial(coefficient, exponent));
    }

    /**
     * Converts the polynomial to a human-readable string.
     * Examples:
     *   - "3x^2 - 2x + 1" for a typical polynomial
     *   - "0" if all coefficients are zero
     */
    @Override
    public String toString() {
        if (monomials.isEmpty()) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        boolean firstTerm = true;

        // Sort exponents in descending order
        List<Integer> exponents = new ArrayList<>(monomials.keySet());
        exponents.sort(Collections.reverseOrder());

        for (int exp : exponents) {
            Monomial m = monomials.get(exp);
            int coeff = m.getCoefficient();
            if (coeff == 0) continue; // Skip zero coeff

            // Determine sign
            String sign = coeff >= 0 ? (firstTerm ? "" : " + ") : " - ";
            sb.append(sign);

            // Absolute value for the coefficient if it's not the first sign
            int absCoeff = Math.abs(coeff);

            // Omit "1" except if exponent=0
            if (exp == 0) {
                sb.append(absCoeff); // just the number
            } else if (absCoeff != 1) {
                // e.g., "2x", "3x^2"
                sb.append(absCoeff).append("x");
                if (exp != 1) {
                    sb.append("^").append(exp);
                }
            } else {
                // e.g. "x", "x^2"
                sb.append("x");
                if (exp != 1) {
                    sb.append("^").append(exp);
                }
            }

            firstTerm = false;
        }

        // If everything was zero, return "0"
        return sb.length() == 0 ? "0" : sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polynomial that = (Polynomial) obj;
        return this.monomials.equals(that.monomials);
    }

    @Override
    public int hashCode() {
        return monomials.hashCode();
    }
}
