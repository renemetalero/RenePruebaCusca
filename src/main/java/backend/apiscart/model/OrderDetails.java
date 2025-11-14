package backend.apiscart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "order_details",schema = "sch_purchase_order")
public class OrderDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_order_detail")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "id_order")
    private OrderRegistration order;
	
	@Column(name = "id_product")
	private int idProduct;
	
	private int quantity;
	
	@Column(name = "price")
	private BigDecimal price;
	
	private BigDecimal subtotal;
}
