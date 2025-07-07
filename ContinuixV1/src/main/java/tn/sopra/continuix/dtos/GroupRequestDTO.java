package tn.sopra.continuix.dtos;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupRequestDTO {
    private String name;
    private String description;
    private List<Long> memberIds;
}
