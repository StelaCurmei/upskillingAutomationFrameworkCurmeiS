package stepdefinitions.API;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AddNewPetSteps {
    private static final Logger LOG = LogManager.getLogger(AddNewPetSteps.class);

    private Object requestBody;
    private Response response;

    // build the payload
    @Given("I prepare a pet payload with:")
    public void preparePetPayload(DataTable table) {
        // convert the single row into a Map<columnName, cellValue>
        Map<String, String> row = table.asMaps(String.class, String.class).get(0);

        // extract and clean each column as the API expects:
        long petId = Long.parseLong(row.get("id"));
        String petName = row.get("name").replace("\"", "");
        int catId = Integer.parseInt(row.get("category.id"));
        String catName = row.get("category.name").replace("\"", "");
        String photoRaw = row.get("photoUrls")      // something like ["http://..."]
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");
        List<String> photoUrls = Arrays.asList(photoRaw.split(","));
        int tagId = Integer.parseInt(row.get("tag.id"));
        String tagName = row.get("tag.name").replace("\"", "");
        String status = row.get("status").replace("\"", "");

        // build the nested Map/List as the JSON schema requires:
        this.requestBody = Map.of(
                "id", petId,
                "name", petName,
                "category", Map.of(
                        "id", catId,
                        "name", catName
                ),
                "photoUrls", photoUrls,
                "tags", List.of(
                        Map.of(
                                "id", tagId,
                                "name", tagName
                        )
                ),
                "status", status
        );

        // log the payload to see what Rest-Assured is about to send
        LOG.info("→ Built requestBody JSON as: {}", requestBody);
    }

    // send the POST
    @When("I send a POST request to {string}")
    public void sendPost(String path) {
        LOG.info("→ About to POST to: {}{}", RestAssured.baseURI, path);

        response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(path)
                .then()
                .extract()
                .response();

        // Log the raw response for debugging
        LOG.info("→ Response status: {}", response.getStatusCode());
        LOG.info("→ Response body:   {}", response.asString());
    }

    // verify status code
    @Then("the response status code should be {int}")
    public void verifyStatus(int code) {
        assertThat(response.getStatusCode(), equalTo(code));
    }

    // verify specific JSON paths
    @Then("the response JSON should contain:")
    public void verifyResponseJson(DataTable table) {
        // Each row is a List<String> of [path, rawValue]
        List<List<String>> rows = table.asLists(String.class);

        for (List<String> row : rows) {
            String rawPath = row.get(0).trim();             // e.g. "$.id"
            String jsonPath = rawPath.replaceFirst("^\\$\\.", ""); // strip "$."

            String rawValue = row.get(1).trim();             // e.g. "12345" or "\"Fido\""
            Object expected = parseExpectedValue(rawValue);  // may be Long, Double, or String

            // Grab the actual object from the response. RestAssured often returns Integer for small ints.
            Object actualRaw = response.jsonPath().get(jsonPath);

            // If both expected and actual are numbers, compare them numerically as longs:
            if (expected instanceof Number && actualRaw instanceof Number) {
                long expectedLong = ((Number) expected).longValue();
                long actualLong = ((Number) actualRaw).longValue();
                LOG.info("→ Asserting JSON path '{}' → expected(long)=<{}>, actual(long)=<{}>",
                        rawPath, expectedLong, actualLong);
                assertThat("JSON path " + rawPath, actualLong, equalTo(expectedLong));
            } else {
                // Otherwise fall back to a normal equals check (e.g. Strings):
                LOG.info("→ Asserting JSON path '{}' → expected=<{}>, actual=<{}>",
                        rawPath, expected, actualRaw);
                assertThat("JSON path " + rawPath, actualRaw, equalTo(expected));
            }
        }
    }

    private Object parseExpectedValue(String raw) {
        if (raw.startsWith("\"") && raw.endsWith("\"")) {
            // strip the surrounding quotes
            return raw.substring(1, raw.length() - 1);
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(raw);
            } catch (NumberFormatException e2) {
                return raw; // fallback to plain string
            }
        }
    }
}
