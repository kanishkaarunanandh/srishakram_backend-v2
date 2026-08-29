package ecommerce.com.srishakram.admin.Controllers;

import ecommerce.com.srishakram.admin.Repository.CustomerOrderRepository;
import ecommerce.com.srishakram.admin.Repository.OrderStatusHistoryRepository;
import ecommerce.com.srishakram.admin.Service.CustomerOrderService;
import ecommerce.com.srishakram.admin.Service.UsersService;
import ecommerce.com.srishakram.models.CustomerOrder;
import ecommerce.com.srishakram.models.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;
    @Autowired
    private UsersService userService;
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;


    // CREATE ORDER
    @PostMapping("/create-order")
    public ResponseEntity<CustomerOrder> createOrder(
            @RequestBody CustomerOrder customerOrder
    ) {
        try {

            // Find logged-in user
            Users loggedInUser =
                    userService.findByEmail(customerOrder.getEmail());

            if (loggedInUser == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }

            /*
             * Payment method is only stored.
             *
             * ONLINE -> demo online payment
             * COD    -> cash on delivery
             */

            if ("COD".equalsIgnoreCase(customerOrder.getPaymentMethod())) {

                customerOrder.setPaymentMethod("COD");
                customerOrder.setOrderStatus("COD");

            } else {

                customerOrder.setPaymentMethod("ONLINE");
                customerOrder.setOrderStatus("ONLINE");
            }


            CustomerOrder createdOrder =
                    customerOrderService.createOrder(
                            customerOrder,
                            loggedInUser
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdOrder);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    // DELETE ORDER
    @DeleteMapping("/orders/{id}")
    @Transactional
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {

        try {

            if (!customerOrderRepository.existsById(id)) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Order not found with ID: " + id);
            }


            CustomerOrder order =
                    customerOrderRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Order not found")
                            );


            // Delete order status history
            orderStatusHistoryRepository.deleteByOrderId(id);


            // Clear order items
            if (order.getItems() != null) {
                order.getItems().clear();
            }


            // Save relationship changes
            customerOrderRepository.save(order);


            // Delete order
            customerOrderRepository.delete(order);


            return ResponseEntity.ok("Order deleted successfully");


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete order: " + e.getMessage());
        }
    }
}