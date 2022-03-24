package sia.sensorsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sia.sensorsservice.dao.BatteryRepository;
import sia.sensorsservice.models.Battery;
import sia.sensorsservice.models.Distance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BatteryController {

    @Autowired
    private BatteryRepository batteryRepository;

    @GetMapping("/batteries/{date1}/{date2}")
    List<Battery> findTemperaturesBetweenDates(@PathVariable(name = "date1")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date1,
                                               @PathVariable(name = "date2")
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date2){
        return batteryRepository.findByDeliveredDateBetween(date1,date2);
    }

    @GetMapping("/batteries/{date1}")
    List<Battery>findBatteriesOneDate(@PathVariable(name = "date1")
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1){
        return batteryRepository.findByDateToday(date1);
    }

    @GetMapping("/batteries/ibin/{id}")
    List<Battery>getBatteriesByIbinId(@PathVariable(name = "id")Long id){
        return batteryRepository.findByIbinId(id);
    }

    @GetMapping("/batteries/last/{id}")
    Battery getLastBatteryValue(@PathVariable(name = "id") Long id){
        return batteryRepository.findTopByIbinIdOrderByDeliveredDateDesc(id);
    }

    @GetMapping("/batteries/ibin/{id}/{date}")
    private List<Battery>getDistancesByIbinIdAndDate(@PathVariable(name = "id")Long id,
                                                      @PathVariable(name = "date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return batteryRepository.findByIbinIdAndDateTodayOrderByDeliveredDateAsc(id,date);
    }
}
