package backend.apiscart.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderUpdateDto {
	@Min(value = 1, message = "message.request.id.size")
    private Long id;
	
	@Min(value = 1, message = "message.request.id.size")
    private Long idOrder;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=50, message = "message.request.paymentorder.size")
	private String names;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=50, message = "message.request.paymentorder.size")
	private String surnames;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=100, message = "message.request.email.size")
	private String email;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=20, message = "message.request.phone.size")
	private String phone;
	
	@NotBlank(message = "message.request.blank")
	@NotNull(message = "message.request.null")
	@Size(max=30, message = "message.request.numbercard.size")
	private String number_card;
}
