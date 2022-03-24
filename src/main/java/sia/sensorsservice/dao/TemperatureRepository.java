package sia.sensorsservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sia.sensorsservice.models.Distance;
import sia.sensorsservice.models.Temperature;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TemperatureRepository extends MongoRepository<Temperature,Long> {

    List<Temperature> findByIbinId(Long id);
    List<Temperature>findByDeliveredDateBetween(LocalDateTime date1, LocalDateTime date2);
    Temperature findTopByIbinIdOrderByDeliveredDateDesc(Long id);
    List<Temperature>findByDateToday(LocalDate date);
    List<Temperature>findByIbinIdAndDateTodayOrderByDeliveredDateAsc(Long id, LocalDate date);
}
