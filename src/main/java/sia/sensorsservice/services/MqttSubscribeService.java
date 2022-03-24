package sia.sensorsservice.services;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sia.sensorsservice.dao.BatteryRepository;
import sia.sensorsservice.dao.DistanceRepository;
import sia.sensorsservice.dao.HumidityRepository;
import sia.sensorsservice.dao.TemperatureRepository;
import sia.sensorsservice.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MqttSubscribeService implements MqttCallback{
    private final int qos = 0;

    @Autowired
    private DistanceRepository distanceRepository;
    @Autowired
    private HumidityRepository humidityRepository;
    @Autowired
    private TemperatureRepository temperatureRepository;
    @Autowired
    private BatteryRepository batteryRepository;
    @Autowired
    private SequenceGeneratorService generatorService;
    @Autowired
    private IbinService ibinService;

    private final double MAX_FULL_VOLUM=350;
    private final double SEIUL=0.33;
    private final double MAX_LOW=SEIUL*100;
    private final double MAX_MEDIUM=2*SEIUL*100;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");




    public void subscribe(String idClient, String hostname,int port,String topic) {
        idClient=idClient+"-"+generateString();
        MemoryPersistence persistence = new MemoryPersistence();
        try
        {
            MqttClient sampleClient = new MqttClient("tcp://" + hostname + ":" + port,
                    idClient);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setAutomaticReconnect(true);
            connOpts.setConnectionTimeout(9009);
            System.out.println("checking Broker ...");
            System.out.println("Mqtt Connecting to broker: " + "tcp://" + hostname + ":" + port );
            System.out.println(idClient + " is Subscribed to " + topic +" topic");
            sampleClient.connect(connOpts);
            System.out.println("Mqtt Connected :)");
            sampleClient.setCallback(this);
            sampleClient.subscribe(topic,qos);
            System.out.println("Subscribed :)");
            System.out.println("Listening  :)");
        } catch (MqttException me) {
            System.out.println("exception :("+me);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {

        String message= new String(mqttMessage.getPayload());
        String id=message.substring(0,message.indexOf(","));
        Long ibinId=Long.parseLong(id,10);

        message=message.substring(message.indexOf(",")+1,message.length());
        String distance=message.substring(0,message.indexOf(","));
        double distanceValue=Double.parseDouble(distance);

        message=message.substring(message.indexOf(",")+1,message.length());
        String humidity=message.substring(0,message.indexOf(","));
        double humidityValue=Double.parseDouble(humidity);

        message=message.substring(message.indexOf(",")+1,message.length());
        String temperature=message.substring(0,message.indexOf(","));
        double temperatureValue=Double.parseDouble(temperature);

        message=message.substring(message.indexOf(",")+1,message.length());
        String battery=message;
        double batteryValue=Double.parseDouble(battery);
        LocalDateTime now=LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        saveDeliveredValues(ibinId,distanceValue, temperatureValue, humidityValue, batteryValue);
        System.out.println("Hello user Your Data has been Saved At::"+formatDateTime);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void saveDeliveredValues(Long ibinId, double distanceValue,double temperatureValue,
                                    double humidityValue,double batteryValue){
        LocalDate today=LocalDate.now();
        LocalDateTime deliveredTime=LocalDateTime.now();
        LocalDateTime now=LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        List<Distance>distances=distanceRepository.findByDateToday(today);
        List<Temperature>temperatures=temperatureRepository.findByDateToday(today);
        List<Humidity>humidities=humidityRepository.findByDateToday(today);
        List<Battery>batteries=batteryRepository.findByDateToday(today);

        List<Double>avgDistValues=new ArrayList<>();
        List<Double>avgTempValues=new ArrayList<>();
        List<Double>avgHumValues=new ArrayList<>();
        List<Double>avgBatteryValues=new ArrayList<>();

        double avgDistance=1.1;
        double avgTemperature=1.1;
        double avgHumidity=1.1;
        double avgBattery=1.1;

        if((!distances.isEmpty()) && (!temperatures.isEmpty()) && (!humidities.isEmpty()) && (!batteries.isEmpty())){
            for (int i=0;i<distances.size();i++){
                avgDistValues.add(distances.get(i).getDistanceValue());
                avgTempValues.add(temperatures.get(i).getTemperatureValue());
                avgHumValues.add(humidities.get(i).getHumidityValue());
                avgBatteryValues.add(batteries.get(i).getBatteryValue());
            }
            avgDistance=this.average(avgDistValues,distanceValue);
            avgTemperature=this.average(avgTempValues,temperatureValue);
            avgHumidity=this.average(avgHumValues,humidityValue);
            avgBattery=this.average(avgBatteryValues,batteryValue);
        }

        distanceRepository.save(
                new Distance(generatorService.generateSequence(Distance.SEQUENCE_NAME),
                        distanceValue,deliveredTime,today,
                        avgDistance,ibinId));
        temperatureRepository.save(
                new Temperature(generatorService.generateSequence(Temperature.SEQUENCE_NAME),
                        temperatureValue,deliveredTime,today,
                        avgTemperature,ibinId));
        humidityRepository.save(
                new Humidity(generatorService.generateSequence(Humidity.SEQUENCE_NAME),
                        humidityValue,deliveredTime,today,
                        avgHumidity,ibinId));
        batteryRepository.save(new Battery(generatorService.generateSequence(Battery.SEQUENCE_NAME),
                batteryValue,deliveredTime,today,avgBattery,ibinId));
        System.out.println("data saved BIN --> "+ibinId+" || deliveredTime is:=> "+formatDateTime);
    }

    public double average(List<Double>array, double newValue) {
        if(array.isEmpty()) {
            return 1.1;
        }
        else {
            double sum = 0;
            for(int i=0; i < array.size(); i++){
                sum += Double.valueOf( array.get(i) );
            }
            sum+=newValue;
            double average = sum/array.size();
            return average*100/MAX_FULL_VOLUM;
        }
    }
    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
