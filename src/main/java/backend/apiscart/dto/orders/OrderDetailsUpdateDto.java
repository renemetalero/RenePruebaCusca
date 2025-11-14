package backend.apiscart.dto.orders;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsUpdateDto {
	
	@Min(value = 1, message = "message.request.id.size")
	private Long id;
	
	@Min(value = 1, message = "message.request.id.size")
	private Long idProduct;
	
	@Min(value = 1, message = "message.request.id.size")
	private int quantity;
	
	@DecimalMin(value = "0.00")
    @NotNull
	private BigDecimal price;
}
