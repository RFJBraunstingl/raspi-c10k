package dev.rfj.vertx_example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SensorVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(SensorVerticle.class.getCanonicalName());
  private static final int HTTP_PORT = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));

  private final String uuid = UUID.randomUUID().toString();
  private final Random random = ThreadLocalRandom.current();

  private double temperature = 21;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.setPeriodic(2_000, this::updateTemperature);

    Router router = Router.router(vertx);
    router.get("/data").handler(this::getTemperatureData);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(HTTP_PORT)
      .onSuccess(httpServer -> {
        LOG.info("HTTP Server running at http://127.0.0.1:{}", HTTP_PORT);
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  private void getTemperatureData(RoutingContext routingContext) {

  }

  private void updateTemperature(Long timerIdentifier) {
    temperature = temperature + (random.nextGaussian() / 2);
    LOG.info("Temperature: {}", temperature);
  }
}
