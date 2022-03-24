package sia.sensorsservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sia.sensorsservice.models.Ibin;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "CONTROL-SERVICE", url="${CONTROL-SERVICE-URI:http://10.1.200.20:8082}")
public interface IbinService {
    @GetMapping("/ibins/{id}")
    Optional<Ibin> findIbinById(@PathVariable(name = "id") Long id);
    @GetMapping("/ibins/{status}")
    List<Ibin> findByStatus(@PathVariable(name = "status") String status);
    @PostMapping("/ibins/update/{id}/{status}")
    Ibin updateStatus(@PathVariable(name = "id") Long id,@PathVariable(name = "status") String status);
}
