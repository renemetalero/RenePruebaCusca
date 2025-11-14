package backend.apiscart.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "payment_order",schema = "sch_purchase_order")
@Data
public class PaymentOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_payment_order")
	private Long id;
	
	@Column(name = "id_order_registration")
    private Long idOrder;
	
	@Column(name = "names")
	private String names;
	
	@Column(name = "surnames")
	private String surnames;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "number_card")
	private String number_card;
}
