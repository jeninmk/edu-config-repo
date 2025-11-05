import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class EducationPlatformSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080") // Gateway URL
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // Authentication scenario
  val authScenario = scenario("Authentication")
    .exec(http("Register User")
      .post("/auth/register")
      .body(StringBody("""{"username": "testuser", "email": "test@example.com", "password": "password123"}"""))
      .check(status.is(200)))

    .pause(1)

    .exec(http("Login")
      .post("/auth/login")
      .body(StringBody("""{"username": "testuser", "password": "password123"}"""))
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("authToken")))

  // API calls with bearer token
  val apiScenario = scenario("API Calls")
    .exec(http("Login for API")
      .post("/auth/login")
      .body(StringBody("""{"username": "testuser", "password": "password123"}"""))
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("authToken")))

    .exec(http("Get Students")
      .get("/students")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200)))

    .exec(http("Get Courses")
      .get("/courses")
      .header("Authorization", "Bearer ${authToken}")
      .check(status.is(200)))

    .exec(http("Create Student")
      .post("/students")
      .header("Authorization", "Bearer ${authToken}")
      .body(StringBody("""{"firstName": "John", "lastName": "Doe", "email": "john.doe@example.com", "studentId": "12345"}"""))
      .check(status.is(200)))

  // Stress test configuration
  setUp(
    authScenario.inject(
      rampUsers(50).during(10.seconds), // Ramp up 50 users over 10 seconds
      constantUsersPerSec(20).during(30.seconds), // Maintain 20 users per second for 30 seconds
      rampUsersPerSec(20).to(100).during(20.seconds) // Ramp up to 100 users per second over 20 seconds
    ),
    apiScenario.inject(
      rampUsers(100).during(20.seconds),
      constantUsersPerSec(50).during(60.seconds),
      rampUsersPerSec(50).to(200).during(30.seconds)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.max.lt(1000), // Max response time < 1 second
      global.successfulRequests.percent.gt(95) // > 95% success rate
    )
}
