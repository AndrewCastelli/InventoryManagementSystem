package ims;


/**
 * Custom Input Validation Exception, thrown when input fails specified requirements
 */
public class IVException extends Exception {

    //Input Validity Requirements:
    //  Min should be less than Max; and Inv should be between those two values.
    //  The user should not delete a product that has a part associated with it.
    //  The application confirms the “Delete” and “Remove” actions.
    //  The application will not crash when inappropriate user data is entered in the forms.
    //  Instead, error messages should be generated.
    public IVException(String err) {
        super(err);
    }

}
