package sia.sensorsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sia.sensorsservice.dao.DistanceRepository;
import sia.sensorsservice.models.Distance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DistanceController {

    @Autowired
    private DistanceRepository distanceRepository;
    @GetMapping("/distances")
    List<Distance>getDistances(){return distanceRepository.findAll();}

    @GetMapping("/distances/{date1}/{date2}")
    private List<Distance> getDistancesBetweenDates(@PathVariable(name = "date1")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date1,
                                            @PathVariable(name = "date2")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date2){
        return distanceRepository.findByDeliveredDateBetween(date1,date2);

    }

    @GetMapping("/distances/{date1}")
    private List<Distance>getDistancesOneDate(@PathVariable(name = "date1")
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1){
        return distanceRepository.findByDateToday(date1);

    }

    @GetMapping("/distances/ibin/{id}")
    private List<Distance>getDistancesByIbinId(@PathVariable(name = "id")Long id){
        return distanceRepository.findByIbinId(id);
    }

    @GetMapping("/distances/ibin/{id}/{date}")
    private List<Distance>getDistancesByIbinIdAndDate(@PathVariable(name = "id")Long id,
                                                      @PathVariable(name = "date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return distanceRepository.findByIbinIdAndDateTodayOrderByDeliveredDateAsc(id,date);
    }

    @GetMapping("/distances/last/{id}")
    private Distance getLastDistanceValue(@PathVariable(name = "id") Long id){
        return distanceRepository.findTopByIbinIdOrderByDeliveredDateDesc(id);
    }

}
