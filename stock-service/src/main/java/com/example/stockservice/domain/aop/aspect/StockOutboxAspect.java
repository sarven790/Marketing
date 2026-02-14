package com.example.stockservice.domain.aop.aspect;

import com.example.stockservice.dao.entity.Product;
import com.example.stockservice.dao.repository.ProductJdbcRepository;
import com.example.stockservice.domain.model.ProductPayload;
import com.example.stockservice.domain.model.enums.EventType;
import com.example.stockservice.domain.model.input.OutboxInput;
import com.example.stockservice.domain.service.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class StockOutboxAspect {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;
    private final ProductJdbcRepository productJdbcRepository;

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..)) && args(entity)")
    public Object around(ProceedingJoinPoint pjp, Object entity) throws Throwable {

        if (!(entity instanceof Product p)) {
            return pjp.proceed();
        }

        Integer newUnit = p.getUnit();
        Integer oldUnit = productJdbcRepository.fetchOldUnitFromDb(p.getId());

        boolean shouldWrite =
                oldUnit != null && oldUnit == 0 &&
                        newUnit != null && newUnit != 0;

        if (shouldWrite && TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {

                    try {
                        var payload = ProductPayload.builder()
                                        .eventId(UUID.randomUUID().toString())
                                        .eventType(EventType.STOCK_REPLENISHED)
                                        .brand(p.getBrand())
                                        .model(p.getModel())
                                        .category(p.getCategory().getValue())
                                        .beforeTotalStock(oldUnit)
                                        .afterTotalStock(p.getUnit())
                                        .occurredAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                                        .build();

                        outboxService.addOutbox(OutboxInput.builder()
                                .payload(objectMapper.writeValueAsString(payload))
                                .build());
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }

                }
            });
        }
        return pjp.proceed();
    }

}
