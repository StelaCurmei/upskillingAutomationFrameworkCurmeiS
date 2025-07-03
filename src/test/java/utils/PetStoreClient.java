package utils;

import api.models.Pet;
import io.restassured.response.Response;

public class PetStoreClient {
    public static Response createPet(Pet pet) {
        return BaseApi.post("/pet", pet);
    }

    public static Response getPet(long id) {
        return BaseApi.get("/pet/" + id);
    }

    public static Response updatePet(Pet pet) {
        return BaseApi.put("/pet", pet);
    }

    public static Response deletePet(long id) {
        return BaseApi.delete("/pet/" + id);
    }

    public static Pet fromResponse(Response r) {
        return r.as(Pet.class);
    }
}
