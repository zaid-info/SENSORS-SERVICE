package sia.sensorsservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Document(collection = "Humidities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Humidity {
    @Id
    private Long id;
    private double humidityValue;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deliveredDate;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private LocalDate dateToday;
    private double avgHumidity;
    private Long ibinId;
    /*@Transient
    private Optional<Ibin> ibin;*/
    @Transient
    public static final String SEQUENCE_NAME = "humiditySequence";

}
