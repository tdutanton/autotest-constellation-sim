package tests;

import static satellites.specs.ResponseSpecification.expectedStatusCode;

import base.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SatelliteController extends BaseTest {

  @Test
  @DisplayName("[200] GET /api/overview - Получение списка группировок и спутников в них")
  void getOverview() {
    Response response = apiClient.get("/api/overview");
    response.then().spec(expectedStatusCode(200));
  }

}
