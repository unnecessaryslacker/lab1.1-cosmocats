package com.cosmocats.market.service;

import com.cosmocats.market.domain.OrderEntity;
import com.cosmocats.market.domain.OrderItemEntity;
import com.cosmocats.market.domain.ProductEntity;
import com.cosmocats.market.repository.OrderRepository;
import com.cosmocats.market.repository.ProductEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductEntityRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductEntityRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderEntity createOrder(String orderNumber,
                                   String customerEmail,
                                   List<Long> productIds) {

        OrderEntity order = new OrderEntity();
        order.setOrderNumber(orderNumber);
        order.setCustomerEmail(customerEmail);

        List<ProductEntity> products = productRepository.findAllById(productIds);

        List<OrderItemEntity> items = products.stream()
                .map(p -> {
                    OrderItemEntity item = new OrderItemEntity();
                    item.setOrder(order);
                    item.setProduct(p);
                    item.setQuantity(1);
                    item.setPrice(p.getPrice());
                    return item;
                })
                .toList();

        order.setItems(items);

        return orderRepository.save(order);
    }
}
