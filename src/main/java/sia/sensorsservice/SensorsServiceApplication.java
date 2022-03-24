package sia.sensorsservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import sia.sensorsservice.models.*;
import sia.sensorsservice.services.MqttSubscribeService;

@SpringBootApplication
@EnableFeignClients
public class SensorsServiceApplication {

    @Value("${mqtt.clientId}")
    String clientId;
    @Value("${mqtt.hostname}")
    String hostname;
    @Value("${mqtt.port}")
    int port;
    @Value("${mqtt.topic}")
    String topic;

    public static void main(String[] args) {
        SpringApplication.run(SensorsServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(RepositoryRestConfiguration configuration, MqttSubscribeService mqttSubscriber){

        return args -> {
            configuration.exposeIdsFor(Ibin.class);
            configuration.exposeIdsFor(Distance.class);
            configuration.exposeIdsFor(Humidity.class);
            configuration.exposeIdsFor(Temperature.class);
            configuration.exposeIdsFor(DistanceArchive.class);
            configuration.exposeIdsFor(HumidityArchive.class);
            configuration.exposeIdsFor(TemperatureArchive.class);
            mqttSubscriber.subscribe(this.clientId,this.hostname,this.port,this.topic);
        };
    }
}
