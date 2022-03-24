package sia.sensorsservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sia.sensorsservice.models.Distance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface DistanceRepository extends MongoRepository<Distance,Long> {

    List<Distance> findByIbinId(Long id);
    List<Distance>findByDeliveredDateBetween(LocalDateTime date1, LocalDateTime date2);
    List<Distance>findByDateToday(LocalDate date);
    Distance findTopByIbinIdOrderByDeliveredDateDesc(Long id);
    List<Distance>findByIbinIdAndDateTodayOrderByDeliveredDateAsc(Long id, LocalDate date);
}
