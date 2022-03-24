package sia.sensorsservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ibin {

    private Long id;
    private String address;
    private double latitude;
    private double longitude;
    private String details;
    private Long communeId;
    private String status;
}
