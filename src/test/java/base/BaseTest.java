package base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import satellites.client.ApiClient;
import satellites.config.TestConfig;

public class BaseTest {

  public static ApiClient apiClient;
  protected static Response lastResponse;

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = TestConfig.BASE_URL;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    apiClient = new ApiClient();
  }

}
