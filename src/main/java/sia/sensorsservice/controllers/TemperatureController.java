package sia.sensorsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sia.sensorsservice.dao.TemperatureRepository;
import sia.sensorsservice.models.Battery;
import sia.sensorsservice.models.Temperature;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class TemperatureController {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @GetMapping("/temperatures/{date1}/{date2}")
    List<Temperature> findTemperaturesBetweenDates(@PathVariable(name = "date1")
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date1,
                                                   @PathVariable(name = "date2")
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date2){
        return temperatureRepository.findByDeliveredDateBetween(date1,date2);
    }

    @GetMapping("/temperatures/{date1}")
    List<Temperature>findTemperaturesOneDate(@PathVariable(name = "date1")
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1){
        return temperatureRepository.findByDateToday(date1);
    }

    @GetMapping("/temperatures/ibin/{id}")
    List<Temperature>getTemperaturesByIbinId(@PathVariable(name = "id")Long id){
        return temperatureRepository.findByIbinId(id);
    }

    @GetMapping("/temperatures/last/{id}")
    Temperature getLastTempValue(@PathVariable(name = "id") Long id){
        return temperatureRepository.findTopByIbinIdOrderByDeliveredDateDesc(id);
    }
    @GetMapping("/temperatures/ibin/{id}/{date}")
    private List<Temperature>getDistancesByIbinIdAndDate(@PathVariable(name = "id")Long id,
                                                     @PathVariable(name = "date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return temperatureRepository.findByIbinIdAndDateTodayOrderByDeliveredDateAsc(id,date);
    }
}
