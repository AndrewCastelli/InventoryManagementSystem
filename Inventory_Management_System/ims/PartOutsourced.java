package ims;

/**
 * Outsourced Parts - includes manufacturer's Company Name.
 */
public class PartOutsourced extends Part {
    private String companyName;

    /**
     * @param companyName - Manufacturer's Name
     */
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    /**
     * @return Manufacturer's Name
     */
    public String getCompanyName() { return companyName; }

    /**
     * Validation test for specified requirements
     * If part fails a test, prompt user with relevant info
     * @throws IVException - alert user to errors in form input
     */
    public void validatePart() throws IVException {
        // Min should be less than Max
        if (getMin() > getMax()) {
            throw new IVException("Minimum stock must be less than maximum.");
        }
        // Inv should be between Min and Max values
        if (getStock() < getMin() || getStock() > getMax()) {
            throw new IVException("Inv input must be between min and max.");
        }
        // Don't leave name blank
        if (getName().isBlank()) {
            throw new IVException("Part name should not be left blank.");
        }
        // Don't let min max or inventory stock be 0
        if (getStock() == 0 || getMin() <= 0 || getMax() == 0 || getPrice() <= 0.00) {
            throw new IVException("Min, Max, Price and Inv should not be 0");
        }
    }
}
