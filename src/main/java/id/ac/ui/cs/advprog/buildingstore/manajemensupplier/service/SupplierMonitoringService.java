package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierMonitoringService {

    private final MeterRegistry meterRegistry;
    private final Counter supplierCreationCounter;
    private final Counter supplierUpdateCounter;
    private final Counter supplierDeletionCounter;
    private final Timer supplierOperationTimer;

    @Autowired
    public SupplierMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.supplierCreationCounter = Counter.builder("supplier_creation_total")
                .description("Total number of suppliers created")
                .register(meterRegistry);
        this.supplierUpdateCounter = Counter.builder("supplier_update_total")
                .description("Total number of suppliers updated")
                .register(meterRegistry);
        this.supplierDeletionCounter = Counter.builder("supplier_deletion_total")
                .description("Total number of suppliers deleted")
                .register(meterRegistry);
        this.supplierOperationTimer = Timer.builder("supplier_operation_duration_seconds")
                .description("Time taken for supplier operations")
                .register(meterRegistry);
    }

    public void recordSupplierCreation() {
        supplierCreationCounter.increment();
    }

    public void recordSupplierUpdate() {
        supplierUpdateCounter.increment();
    }

    public void recordSupplierDeletion() {
        supplierDeletionCounter.increment();
    }

    public Timer.Sample startTimer() {
        try {
            return Timer.start(meterRegistry);
        } catch (Exception e) {
            // Fallback for test environments
            return null;
        }
    }

    public void recordOperationTime(Timer.Sample sample, String operation) {
        if (sample != null) {
            try {
                sample.stop(Timer.builder("supplier_operation_duration_seconds")
                        .tag("operation", operation)
                        .register(meterRegistry));
            } catch (Exception e) {
                // Silently handle timer errors in test environment
            }
        }
    }

    public void recordSupplierCount(long count) {
        meterRegistry.gauge("supplier_total_count", count);
    }

    public void recordActiveSupplierCount(long count) {
        meterRegistry.gauge("supplier_active_count", count);
    }

    public void recordSupplierError(String operation) {
        Counter.builder("supplier_errors_total")
                .tag("operation", operation)
                .description("Total number of supplier operation errors")
                .register(meterRegistry)
                .increment();
    }
}