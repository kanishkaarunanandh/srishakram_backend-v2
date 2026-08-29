package ecommerce.com.srishakram.admin.Service;

import ecommerce.com.srishakram.admin.Repository.CustomerOrderRepository;
import ecommerce.com.srishakram.models.CartItem;
import ecommerce.com.srishakram.models.CustomerOrder;
import ecommerce.com.srishakram.models.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepo;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepo) {
        this.customerOrderRepo = customerOrderRepo;
    }

    @Transactional
    public CustomerOrder createOrder(
            CustomerOrder customerOrder,
            Users loggedInUser
    ) {

        customerOrder.setUser(loggedInUser);

        List<CartItem> mappedItems = customerOrder.getItems()
                .stream()
                .map(dto -> {

                    CartItem item = new CartItem();

                    item.setProductId(dto.getProductId());
                    item.setProductName(dto.getProductName());
                    item.setQuantity(dto.getQuantity());
                    item.setImage(dto.getImage());
                    item.setPrice(dto.getPrice());
                    item.setOffer_price(dto.getOffer_price());

                    return item;
                })
                .toList();

        customerOrder.setItems(mappedItems);


        if ("COD".equalsIgnoreCase(customerOrder.getPaymentMethod())) {

            customerOrder.setPaymentMethod("COD");
            customerOrder.setOrderStatus("COD");
            customerOrder.setPaymentId(null);

        } else if ("razorpay".equalsIgnoreCase(customerOrder.getPaymentMethod())) {

            // Demo payment only
            customerOrder.setPaymentMethod("razorpay");
            customerOrder.setOrderStatus("PAID");
            customerOrder.setPaymentId(null);

        } else {

            throw new IllegalArgumentException(
                    "Invalid payment method. Use COD or razorpay"
            );
        }

        return customerOrderRepo.save(customerOrder);
    }


    @Transactional
    public CustomerOrder cancelPaidOrder(CustomerOrder order) {

        order.setOrderStatus("CANCELLED");

        return customerOrderRepo.save(order);
    }
}