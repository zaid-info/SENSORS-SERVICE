package sia.sensorsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sia.sensorsservice.dao.HumidityRepository;
import sia.sensorsservice.models.Battery;
import sia.sensorsservice.models.Humidity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class HumidityController {

    @Autowired
    private HumidityRepository humidityRepository;

    @GetMapping("/humidities/{date1}/{date2}")
    List<Humidity> findHumiditiesBetweenDates(@PathVariable(name = "date1")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date1,
                                              @PathVariable(name = "date2")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date2){
        return humidityRepository.findByDeliveredDateBetween(date1,date2);
    }

    @GetMapping("/humidities/{date1}")
    List<Humidity>findHumiditiesOneDate(@PathVariable(name = "date1")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1){
        return humidityRepository.findByDateToday(date1);
    }

    @GetMapping("/humidities/ibin/{id}")
    List<Humidity>getHumiditiesByIbinId(@PathVariable(name = "id")Long id){
        return humidityRepository.findByIbinId(id);
    }

    @GetMapping("/humidities/last/{id}")
    Humidity getLastHumValue(@PathVariable(name = "id") Long id){
        return humidityRepository.findTopByIbinIdOrderByDeliveredDateDesc(id);
    }
    @GetMapping("/humidities/ibin/{id}/{date}")
    private List<Humidity>getDistancesByIbinIdAndDate(@PathVariable(name = "id")Long id,
                                                     @PathVariable(name = "date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return humidityRepository.findByIbinIdAndDateTodayOrderByDeliveredDateAsc(id,date);
    }
}
