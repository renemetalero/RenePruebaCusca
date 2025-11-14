package backend.apiscart.dto.orders;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRegistrationDto {
	@Min(value = 1, message = "message.request.id.size")
	private Long id;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=250, message = "message.request.orden.size")
	private String customer;
	
	@DecimalMin(value = "0.00")
    @NotNull
	private BigDecimal total;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=50, message = "message.request.paymentorder.size")
	private String orderStatus;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=50, message = "message.request.paymentorder.size")
	private String paymentMethod;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=50, message = "message.request.paymentorder.size")
	private String paymentStatus;
	
	private List<OrderDetailsUpdateDto> details;
}
