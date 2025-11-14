package backend.apiscart.dto.products;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestGetOneDto {
	@NotNull(message = "message.request.idproducto.null")
	@Min(value = 1, message = "message.request.idproducto.min")
	private Long idProducto;
}
