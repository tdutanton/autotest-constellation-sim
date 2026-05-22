package tests;

import static satellites.specs.ResponseSpecification.expectedStatusCode;
import static satellites.specs.ResponseSpecification.expectedStatusCodeWithoutResponse;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
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

    String satName = "AT-Img-satellite-1";
    String constName = "AT-CONS";

    satellite.put("type", "IMAGE");
    satellite.put("name", satName);
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", constName);
    requestBody.put("satelliteParams", satellitesList);

    Response response = apiClient.post("/api/add-satellites", requestBody);
    response.then().spec(expectedStatusCodeWithoutResponse(200));

    Response getResponse = apiClient.get("/api/overview");
    JsonPath jsonPath = getResponse.jsonPath();
    String satelliteString = jsonPath.getString("[0].satellitesStatus[0]");

    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("name='([^']+)'");
    java.util.regex.Matcher matcher = pattern.matcher(satelliteString);

    Assertions.assertTrue(matcher.find(), "Имя спутника не найдено в строке: " + satelliteString);
    Assertions.assertEquals(satName, matcher.group(1));

    Map<String, Object> pathParams = Map.of(
        "constellationName", constName,
        "satelliteName", satName
    );

    apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        pathParams
    );
  }

  @Test
  @DisplayName("[200] POST /api/activate-satellites/{constellationName} - Активация спутников в группировке")
  void activateSatByConstellationTest() {

    String satName = "AT-Img-satellite-1";
    String constName = "AT-CONS";
    Map<String, Object> activateParams = Map.of("constellationName", constName);
    Map<String, Object> deleteParams = Map.of(
        "constellationName", constName,
        "satelliteName", satName
    );

    Map<String, Object> requestBody = new HashMap<>();

    Map<String, Object> satellite = new HashMap<>();
    satellite.put("type", "IMAGE");
    satellite.put("name", satName);
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", constName);
    requestBody.put("satelliteParams", satellitesList);

    apiClient.post("/api/add-satellites", requestBody);

    Response response = apiClient.postWithPathParams("/api/activate-satellites/{constellationName}",
        activateParams);
    response.then().spec(expectedStatusCodeWithoutResponse(200));
    apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        deleteParams
    );
  }

  @Test
  @DisplayName("[200] POST /api/activate-all-satellites - Активация всех спутников во всех группировках")
  void activateAllSatellitesTest() {

    String satName = "AT-Img-satellite-1";
    String satNameSecond = "AT-Img-satellite-2";
    String constName = "AT-CONS";
    String constNameSecond = "AT-CONS-2";

    Map<String, Object> deleteParams = Map.of(
        "constellationName", constName,
        "satelliteName", satName
    );
    Map<String, Object> deleteParamsSecond = Map.of(
        "constellationName", constNameSecond,
        "satelliteName", satNameSecond
    );

    Map<String, Object> requestBody = new HashMap<>();

    Map<String, Object> satellite = new HashMap<>();
    satellite.put("type", "IMAGE");
    satellite.put("name", satName);
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", constName);
    requestBody.put("satelliteParams", satellitesList);

    apiClient.post("/api/add-satellites", requestBody);

    /// Вторая группировка
    Map<String, Object> requestBodySecond = new HashMap<>();

    Map<String, Object> satelliteSecond = new HashMap<>();
    satelliteSecond.put("type", "IMAGE");
    satelliteSecond.put("name", satNameSecond);
    satelliteSecond.put("batteryLevel", 90);
    satelliteSecond.put("resolution", 500);

    List<Map<String, Object>> satellitesListSecond = List.of(satelliteSecond);

    requestBody.put("constellationName", constNameSecond);
    requestBody.put("satelliteParams", satellitesListSecond);

    apiClient.post("/api/add-satellites", requestBodySecond);

    Response response = apiClient.postWithoutBody("/api/activate-all-satellites");
    response.then().spec(expectedStatusCodeWithoutResponse(200));

    apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        deleteParams
    );
    apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        deleteParamsSecond
    );
  }

  @Test
  @DisplayName("[200] POST /api/missions - Выполнение спутником миссии")
  void postMissionTest() {
    Map<String, Object> requestBody = new HashMap<>();

    Map<String, Object> satellite = new HashMap<>();

    String satName = "AT-Img-satellite-1";
    String constName = "AT-CONS";

    satellite.put("type", "IMAGE");
    satellite.put("name", satName);
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", constName);
    requestBody.put("satelliteParams", satellitesList);

    apiClient.post("/api/add-satellites", requestBody);
    apiClient.postWithoutBody("/api/activate-all-satellites");

    Map<String, Object> requestBodyMission = new HashMap<>();
    requestBodyMission.put("targetType", "CONSTELLATION");
    requestBodyMission.put("constellationName", constName);
    requestBodyMission.put("satelliteName", satName);

    Response response = apiClient.post("api/missions", requestBodyMission);
    response.then().spec(expectedStatusCodeWithoutResponse(200));

    Map<String, Object> pathParams = Map.of(
        "constellationName", constName,
        "satelliteName", satName
    );

    apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        pathParams
    );
  }

  @Test
  @DisplayName("[204] DELETE /api/delete-satellite/{constellationName}/satellites/{satelliteName} - Удаление спутника из группировки")
  void deleteSatelliteTest() {
    Map<String, Object> requestBody = new HashMap<>();

    Map<String, Object> satellite = new HashMap<>();

    String satName = "AT-Img-satellite-1";
    String constName = "AT-CONS";

    satellite.put("type", "IMAGE");
    satellite.put("name", satName);
    satellite.put("batteryLevel", 90);
    satellite.put("resolution", 500);

    List<Map<String, Object>> satellitesList = List.of(satellite);

    requestBody.put("constellationName", constName);
    requestBody.put("satelliteParams", satellitesList);

    apiClient.post("/api/add-satellites", requestBody);

    Map<String, Object> pathParams = Map.of(
        "constellationName", constName,
        "satelliteName", satName
    );

    Response response = apiClient.delete(
        "/api/delete-satellite/{constellationName}/satellites/{satelliteName}",
        pathParams
    );

    response.then().spec(expectedStatusCodeWithoutResponse(204));
  }

}
