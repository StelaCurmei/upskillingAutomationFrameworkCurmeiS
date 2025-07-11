package stepdefinitions.API;

import api.models.Pet;
import api.models.PetCategory;
import api.models.PetTag;
import context.ContextKey;
import context.ScenarioContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PetStoreClient;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PetApiSteps {
    private static final Logger LOG = LogManager.getLogger(PetApiSteps.class);

    //—— Background ——
    @Given("I set the API base URL to the PetStore service")
    public void setApiBaseUrl() {
        LOG.info("Background: base URL already initialized by BaseApi");
    }

    //—— Create ——
    @When("I create a new Pet with name {string} and status {string}")
    public void createNewPet(String name, String status) {
        LOG.info("Create scenario: building Pet(name={}, status={})", name, status);
        Pet p = buildPet(name, status);

        Response r = PetStoreClient.createPet(p);
        LOG.debug("POST /pet response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @Then("the response status code should be {int}")
    public void assertStatusCode(int expected) {
        Response r = lastResponse();
        LOG.info("Asserting status code == {}", expected);
        assertEquals("Status code mismatch", expected, r.getStatusCode());
    }

    @And("the response body should contain a pet object with name {string} and status {string}")
    public void assertBodyNameAndStatus(String name, String status) {
        Pet p = lastPet();
        LOG.info("Verifying name={} and status={}", name, status);
        assertEquals("Name mismatch", name, p.getName());
        assertEquals("Status mismatch", status, p.getStatus());
    }

    //—— Read ——
    @And("a pet has been created with name {string} and status {string}")
    public void seedPet(String name, String status) {
        Pet p = buildPet(name, status);
        Response r = PetStoreClient.createPet(p);
        LOG.debug("Seeded pet creation response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @When("I GET that pet by its id")
    public void getById() {
        long id = lastPet().getId();
        LOG.info("Fetching pet with id={}", id);
        Response r = PetStoreClient.getPet(id);
        LOG.debug("GET /pet response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
    }

    @Then("the response body should contain a pet object with id matching the created id")
    public void assertIdMatches() {
        Pet created = lastPet();
        Pet fetched = lastResponse().as(Pet.class);
        LOG.info("Verifying ID match: {} == {}", created.getId(), fetched.getId());
        assertEquals("ID mismatch", created.getId(), fetched.getId());
    }

    //—— Update ——
    @And("there is an existing pet with id {int}, name {string}, and status {string}")
    public void ensurePetExistsByIdNameStatus(int id, String name, String status) {
        LOG.info("Creating/updating pet with id={}, name={}, status={}", id, name, status);
        Pet p = buildPet(name, status);
        p.setId(id);
        Response r = PetStoreClient.updatePet(p);
        LOG.debug("PUT /pet response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @When("I update that pet’s status to {string}")
    public void updatePetStatus(String status) {
        Pet p = lastPet();
        p.setStatus(status);
        LOG.info("Updating status of pet id={} to {}", p.getId(), status);
        Response r = PetStoreClient.updatePet(p);
        LOG.debug("PUT update response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @Then("the response body should show status {string}")
    public void assertUpdatedStatus(String status) {
        Pet updated = lastResponse().as(Pet.class);
        LOG.info("Verifying updated status == {}", status);
        assertEquals("Status mismatch", status, updated.getStatus());
    }

    //—— Delete ——
    @When("I DELETE that pet by its id")
    public void deleteById() {
        long id = lastPet().getId();
        LOG.info("Deleting pet with id={}", id);
        Response r = PetStoreClient.deletePet(id);
        LOG.debug("DELETE /pet response: {} - {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
    }

    @Then("a subsequent GET by that id returns {int}")
    public void assertDeleted(int expectedCode) {
        long id = lastPet().getId();
        LOG.info("Verifying deletion with GET by id={}, expecting status {}", id, expectedCode);
        Response r = PetStoreClient.getPet(id);
        LOG.debug("GET after delete returned status {}", r.getStatusCode());
        assertEquals("Delete-check status mismatch", expectedCode, r.getStatusCode());
    }

    //—— HELPER METHODS ——
    private Pet buildPet(String name, String status) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setStatus(status);

        PetCategory cat = new PetCategory();
        cat.setName("default-category");
        pet.setCategory(cat);

        PetTag tag = new PetTag();
        tag.setName("sample-tag");
        pet.setTags(Arrays.asList(tag));

        pet.setPhotoUrls(Arrays.asList("https://example.com/photo1.jpg"));
        return pet;
    }

    private Response lastResponse() {
        return ScenarioContext.getScenarioContext(ContextKey.LAST_RESPONSE);
    }

    private Pet lastPet() {
        return ScenarioContext.getScenarioContext(ContextKey.CREATED_PET);
    }
}
