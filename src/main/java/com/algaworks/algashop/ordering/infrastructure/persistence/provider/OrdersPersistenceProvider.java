package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceRepository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;

    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = persistenceRepository.
                findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return persistenceRepository.existsById(orderId.value().toLong());
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Transactional(readOnly = false)
    @Override
    public void add(Order aggregrateRoot) {
        long orderId = aggregrateRoot.id().value().toLong();

        persistenceRepository.findById(orderId).
                ifPresentOrElse(
                        (orderPersistenceEntity) -> {
                          update(aggregrateRoot,orderPersistenceEntity);
                        },
                        () ->{
                            insert(aggregrateRoot);
                        }

                );
    }

    public void insert(Order aggregrateRoot) {

        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(aggregrateRoot);
        persistenceRepository.saveAndFlush(orderPersistenceEntity);
        updateVersion(aggregrateRoot,orderPersistenceEntity);
    }


    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {

        List<OrderPersistenceEntity> entities = persistenceRepository.placedByCustomerInYear(
                customerId.value(),
                year.getValue()
        );

        return entities.stream().map(disassembler::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
        return this.persistenceRepository.salesQuantityByCustomerInYear(customerId.value(),year.getValue());
    }

    @Override
    public Money totalSoldForCustomer(CustomerId customerId) {
        return new Money(this.persistenceRepository.totalSoldForCustomer(customerId.value()));
    }

    private void update(Order aggregrateRoot, OrderPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity,aggregrateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceRepository.saveAndFlush(persistenceEntity);
         updateVersion(aggregrateRoot,persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Order aggregrateRoot, OrderPersistenceEntity persistenceEntity) {
        Field version= aggregrateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregrateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);


    }


}
