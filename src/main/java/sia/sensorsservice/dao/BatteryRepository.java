package sia.sensorsservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sia.sensorsservice.models.Battery;
import sia.sensorsservice.models.Distance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface BatteryRepository extends MongoRepository<Battery,Long> {

    List<Battery> findByIbinId(Long id);
    List<Battery>findByDeliveredDateBetween(LocalDateTime date1, LocalDateTime date2);
    Battery findTopByIbinIdOrderByDeliveredDateDesc(Long id);
    List<Battery>findByDateToday(LocalDate date);
    List<Battery>findByIbinIdAndDateTodayOrderByDeliveredDateAsc(Long id, LocalDate date);
}
