package dev.rfj.vertx_example;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("SensorVerticle", new DeploymentOptions().setInstances(1));

    vertx.eventBus()
      .<JsonObject>consumer("temperature.updates", message -> {
        LOG.info("Update received in main: {}", message.body().encodePrettily());
      });
  }
}
