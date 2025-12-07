package com.cosmocats.market.repository;

import com.cosmocats.market.domain.ProductEntity;
import com.cosmocats.market.repository.projection.TopProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    @Query("""
           select p.name as name, sum(oi.quantity) as totalQuantity
           from OrderItemEntity oi
           join oi.product p
           group by p.id, p.name
           order by totalQuantity desc
           """)
    List<TopProductProjection> findTopProducts();
}
