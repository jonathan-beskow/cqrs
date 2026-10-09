package br.com.beautique.ms_sync.dto.beautyProcedures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "beautyProcedures")
public class BeautyProcedureDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
