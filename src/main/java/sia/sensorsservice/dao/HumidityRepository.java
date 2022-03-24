package sia.sensorsservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sia.sensorsservice.models.Distance;
import sia.sensorsservice.models.Humidity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface HumidityRepository extends MongoRepository<Humidity,Long> {

    List<Humidity> findByIbinId(Long id);
    List<Humidity>findByDeliveredDateBetween(LocalDateTime date1, LocalDateTime date2);
    Humidity findTopByIbinIdOrderByDeliveredDateDesc(Long id);
    List<Humidity>findByDateToday(LocalDate date);
    List<Humidity>findByIbinIdAndDateTodayOrderByDeliveredDateAsc(Long id, LocalDate date);
}
