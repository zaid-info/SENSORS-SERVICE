package sia.sensorsservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "distanceArchive")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceArchive {

    @Id
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate archiveDate;
    private List<Distance> distances=new ArrayList<>();
    private Long ibinId;
    @Transient
    private Ibin ibin;
    @Transient
    public static final String SEQUENCE_NAME = "archiveDistSequence";
}
