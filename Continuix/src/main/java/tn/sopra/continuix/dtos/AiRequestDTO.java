package tn.sopra.continuix.dtos;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestDTO {
    private int incident_type_encoded;
    private int impact_encoded;
}
