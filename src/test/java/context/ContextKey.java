package context;

public enum ContextKey {
    //UI
    EMAIL,
    PASSWORD,
    FIRSTNAME,
    LASTNAME,
    NEGATIVE_ACTUAL_ERRORS,
    NEGATIVE_EXPECTED_ERRORS,

    //API
    LAST_RESPONSE,    // store RestAssured’s Response object
    CREATED_PET,      // store the deserialized Pet that was just created
    EXISTING_PET_ID   // store an integer ID when “ensure a pet with ID X exists”
}