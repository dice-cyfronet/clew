package test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

object ConfigurationSequence {
	val sequence =
		//check for development workflow
		exec(http("Getting development appliance sets")
				.get("/appliance_sets?appliance_set_type=development")
				.check(jsonPath("$.appliance_sets").count.saveAs("devWorkflows")))
		.doIf(session => session("devWorkflows").as[Int] < 1) {
			//create development workflow
			exec(http("Creating development workflow")
				.post("/appliance_sets")
				.body(StringBody("""{}""")).asJSON
			)
		}
}

object AtmoDevelopmentSequence {
	val sequence =
		//check for development workflow
		exec(http("Getting development appliance sets")
				.get("/appliance_sets?appliance_set_type=development")
				.check(jsonPath("$.appliance_sets").count.saveAs("devWorkflows")))
		//check until development workflow exists - another scenario should take care of creating it
		.asLongAs(session => session("devWorkflows").as[Int] < 1) {
			exec(http("Getting development appliance sets")
				.get("/appliance_sets?appliance_set_type=development")
				.check(jsonPath("$.appliance_sets").count.saveAs("devWorkflows")))
				.pause(1)
		}
			
			
			
			
		//if not create it
		
		//spin up an instance
			
		//check the status every 5 seconds 100 times
			
		//shutdown the instance
		//close the workflow
			
			
//		//printing the session to see the 'processId' value for debugging
//		.exec {session =>
//			println(session)
//			
//			session
//		}
//		//checking the job state with list (all user jobs) GET request
//		.exec(http("Job status with list GET")
//			.get("/api/jobs")
//			.check(jsonPath("$[*]").count.greaterThan(0))
//		)
//		//waiting until the job reaches finished or aborted state
//		.asLongAs(session => !session("status").as[String].equals("FINISHED")) {
//			exec(http("Job status check for finished")
//				.get("/api/jobs/${jobId}")
//				.check(jsonPath("$.status").saveAs("status"))
//			)
//			.exec {session =>
//				println(session)
//			
//				session
//			}
//			.pause(1)
//		}
//		//submitting another job to be aborted
//		.exec(http("Job submission to test aborting job")
//			.post("/api/jobs")
//			.body(StringBody("""{"host": "zeus.cyfronet.pl", "script": "#!/bin/bash\necho hello\nexit 0"}""")).asJSON
//			.check(jsonPath("$.status").saveAs("status"), jsonPath("$.job_id").saveAs("jobId"))
//		)
//		//waiting a little bit
//		.pause(1)
//		//aborting job
//		.exec(http("Job abort")
//			.delete("/api/jobs/${jobId}")
//			.check(status.is(204))
//		)
//		//waiting until job status is aborted
//		.asLongAs(session => !session("status").as[String].equals("ABORTED")) {
//			exec(http("Job status check for aborted")
//				.get("/api/jobs/${jobId}")
//				.check(jsonPath("$.status").saveAs("status"))
//			)
//			.exec {session =>
//				println(session)
//			
//				session
//			}
//			.pause(1)
//		}
}

object AtmoProductionSequence {
	val sequence =
		//submitting new job
		exec(http("User retrieval")
			.get("/users"))
			
		//check if production workflow exists
		//if not create it
		
		//spin up an instance
			
		//check the status every 5 seconds 100 times
			
		//shutdown the instance
		//close the workflow
}

object AtmoWorkflowSequence {
	val sequence =
		//submitting new job
		exec(http("User retrieval")
			.get("/users"))
			
		//check if production workflow exists
		//if not create it
		
		//spin up an instance
			
		//check the status every 5 seconds 100 times
			
		//shutdown the instance
		//close the workflow
}

class CfApiTestSimulation extends Simulation {
	val baseUrl = "https://vph.cyfronet.pl/api/v1"
	
	val httpProtocol = http
		.baseURL(baseUrl)
		.header("MI-TICKET", "dWlkPWRoYXJlemxhazt2YWxpZHVudGlsPTE0MTU5MDI2NzQ7Y2lwPTAuMC4wLjA7dG9rZW5zPVZQSC1TaGFyZSBkZXZlbG9wZXJzLGRoYXJlemxhayxldWhlYXJ0IEFNREIgY29tcHV0YXRpb25hbCBtZXNoZXNfZGF0YXNldF9yZWFkLHZwaCxWUEgsZGV2ZWxvcGVyO3VkYXRhPWRoYXJlemxhayxEYW5pZWwgSGFyZXpsYWssZC5oYXJlemxha0BjeWYta3IuZWR1LnBsLCxQT0xBTkQsMzA5NTA7c2lnPU1DMENGUUR1SEZsdW9Ob2lFSGhiR2hhMmkzNnJCOXhIZ3dJVWFkdlcxYjQxZWhNYTRId0RTOHRpSHREREpQaz0=")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.userAgentHeader("curl/7.37.1")

	val atmoDevelopmentScenario = scenario("Atmosphere development scenario").exec(AtmoDevelopmentSequence.sequence)
	val atmoProductionScenario = scenario("Atmosphere production scenario").exec(AtmoProductionSequence.sequence)
	val atmoWorkflowScenario = scenario("Atmosphere workflow scenario").exec(AtmoWorkflowSequence.sequence)

	setUp(
		atmoDevelopmentScenario.inject(atOnceUsers(1))
//		atmoProductionScenario.inject(atOnceUsers(50)),
//		atmoWorkflowScenario.inject(atOnceUsers(50))
	).protocols(httpProtocol)
}