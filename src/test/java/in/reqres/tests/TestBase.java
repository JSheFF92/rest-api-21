package in.reqres.tests;

import in.reqres.config.ApiConfig;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
//    @BeforeAll
//    static void beforeAll() {
////        RestAssured.baseURI = "https://reqres.in";
////        RestAssured.basePath = "/api";
        protected static final ApiConfig config = ConfigFactory.create(ApiConfig.class, System.getProperties());
    }
//}
