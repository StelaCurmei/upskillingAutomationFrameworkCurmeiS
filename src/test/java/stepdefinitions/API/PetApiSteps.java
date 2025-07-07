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
    @When("I POST a new Pet with name {string} and status {string}")
    public void createNewPet(String name, String status) {
        LOG.info("Create scenario: building Pet(name={}, status={})", name, status);
        Pet p = buildPet(name, status);

        LOG.debug("About to POST /pet with payload: {}", p);
        Response r = PetStoreClient.createPet(p);
        LOG.debug("Received status {} and body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @Then("the response status code should be {int}")
    public void assertStatusCode(int expected) {
        LOG.info("Verifying status code is {}", expected);
        Response r = lastResponse();
        assertEquals("Status code mismatch", expected, r.getStatusCode());
    }

    @Then("the response body should contain a pet object with name {string} and status {string}")
    public void assertBodyNameAndStatus(String name, String status) {
        LOG.info("Verifying returned pet name/status == {}/{}", name, status);
        Pet p = lastPet();
        assertEquals("Name mismatch", name, p.getName());
        assertEquals("Status mismatch", status, p.getStatus());
    }

    //—— Read ——
    @Given("a pet has been created with name {string} and status {string}")
    public void seedPet(String name, String status) {
        LOG.info("Get scenario: seeding pet(name={}, status={})", name, status);
        Pet p = buildPet(name, status);
        Response r = PetStoreClient.createPet(p);
        LOG.debug("Seed POST returned {}, body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @When("I GET that pet by its id")
    public void getById() {
        long id = lastPet().getId();
        LOG.info("GET scenario: fetching pet by id={}", id);
        Response r = PetStoreClient.getPet(id);
        LOG.debug("GET returned status {} and body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
    }

    @Then("the response body should contain a pet object with id matching the created id")
    public void assertIdMatches() {
        LOG.info("Verifying fetched pet ID matches created");
        Pet created = lastPet();
        Pet fetched = lastResponse().as(Pet.class);
        assertEquals("ID mismatch", created.getId(), fetched.getId());
    }

    //—— Update ——
    @Given("there is an existing pet with id {int}")
    public void ensurePetExistsById(int id) {
        LOG.info("Update scenario: upserting pet id={}", id);
        Pet p = buildPet("auto", "available");
        p.setId(id);
        Response r = PetStoreClient.updatePet(p);
        LOG.debug("Upsert PUT returned {}, body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
        ScenarioContext.setScenarioContext(ContextKey.CREATED_PET, r.as(Pet.class));
    }

    @When("I PUT to update that pet’s status to {string}")
    public void updatePetStatus(String status) {
        LOG.info("Updating pet status to '{}'", status);
        Pet p = lastPet();
        p.setStatus(status);
        Response r = PetStoreClient.updatePet(p);
        LOG.debug("Update PUT returned {}, body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
    }

    @Then("the response body should show status {string}")
    public void assertUpdatedStatus(String status) {
        LOG.info("Verifying updated status == {}", status);
        Pet updated = lastResponse().as(Pet.class);
        assertEquals("Status mismatch", status, updated.getStatus());
    }

    //—— Delete ——
    @When("I DELETE that pet by its id")
    public void deleteById() {
        long id = lastPet().getId();
        LOG.info("Deleting pet id={}", id);
        Response r = PetStoreClient.deletePet(id);
        LOG.debug("DELETE returned status {}, body: {}", r.getStatusCode(), r.getBody().asString());

        ScenarioContext.setScenarioContext(ContextKey.LAST_RESPONSE, r);
    }

    @Then("a subsequent GET by that id returns {int}")
    public void assertDeleted(int expectedCode) {
        long id = lastPet().getId();
        LOG.info("Verifying deletion: GET id={} returns {}", id, expectedCode);
        Response r = PetStoreClient.getPet(id);
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
