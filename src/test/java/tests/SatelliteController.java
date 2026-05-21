package tests;

import static satellites.specs.ResponseSpecification.expectedStatusCode;
import static satellites.specs.ResponseSpecification.expectedStatusCodeWithoutResponse;

import base.BaseTest;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SatelliteController extends BaseTest {

  @Test
  @DisplayName("[200] GET /api/overview - Получение списка группировок и спутников в них")
  void getOverviewTest() {
    Response response = apiClient.get("/api/overview");
    response.then().spec(expectedStatusCode(200));
  }

  @Test
  @DisplayName("[200] POST /api/add-satellites - Создание спутника и добавление его в группировку")
  void postSatelliteTest() {
    Map<String, Object> requestBody = new HashMap<>();

    Map<String, Object> satellite = new HashMap<>();

    satellite.put("type", "IMAGE");
    satellite.put("name", "AT-Img-satellite-1");
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", "AT-CONS");
    requestBody.put("satelliteParams", satellitesList);

    Response response = apiClient.post("/api/add-satellites", requestBody);
    response.then().spec(expectedStatusCodeWithoutResponse(200));
  }

}
