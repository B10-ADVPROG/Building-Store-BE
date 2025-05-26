package id.ac.ui.cs.advprog.buildingstore.manajemensupplier.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupplierMonitoringServiceTest {

    private SupplierMonitoringService monitoringService;
    private MeterRegistry meterRegistry;

    @Mock
    MeterRegistry mockRegistry;

    @BeforeEach
    void setUp() {
        // Use a real SimpleMeterRegistry instead of mocking
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new SupplierMonitoringService(meterRegistry);
    }

    @Test
    void testRecordSupplierCreation() {
        monitoringService.recordSupplierCreation();
        assertEquals(1.0, meterRegistry.get("supplier_creation_total").counter().count());
    }
    
    @Test
    void testRecordSupplierUpdate() {
        monitoringService.recordSupplierUpdate();
        assertEquals(1.0, meterRegistry.get("supplier_update_total").counter().count());
    }
    
    @Test
    void testRecordSupplierDeletion() {
        monitoringService.recordSupplierDeletion();
        assertEquals(1.0, meterRegistry.get("supplier_deletion_total").counter().count());
    }
    
    @Test
    void testStartTimer() {
        Timer.Sample sample = monitoringService.startTimer();
        assertNotNull(sample);
    }
    
    @Test
    void testStartTimerHandlesException() {
        // Can't easily test exception with real registry, so we'll skip this test
        // A real registry won't throw exceptions in normal operation
    }
    
    @Test
    void testStartTimerHandlesExceptionBranch() {
        // Use a real registry for the service, but mock the static Timer.start to throw
        try (MockedStatic<Timer> timerMock = Mockito.mockStatic(Timer.class)) {
            timerMock.when(() -> Timer.start(any(MeterRegistry.class)))
                    .thenThrow(new RuntimeException("Forced"));

            // Use the already constructed monitoringService with a real registry
            assertNull(monitoringService.startTimer());
        }
    }
    
    @Test
    void testRecordOperationTime() {
        Timer.Sample sample = Timer.start(meterRegistry);
        monitoringService.recordOperationTime(sample, "test");
        // If no exception is thrown, the test passes
    }
    
    @Test
    void testRecordOperationTimeHandlesExceptionBranch() {
        Timer.Sample mockSample = mock(Timer.Sample.class);
        // Force sample.stop to throw
        doThrow(new RuntimeException("Forced")).when(mockSample).stop(any(Timer.class));

        SupplierMonitoringService service = new SupplierMonitoringService(new SimpleMeterRegistry());
        // Should not throw, just silently handle
        service.recordOperationTime(mockSample, "test-exception");
    }
    
    @Test
    void testRecordOperationTimeWithNullSample() {
        monitoringService.recordOperationTime(null, "test");
        // If no exception is thrown, the test passes
    }
    
    @Test 
    void testRecordSupplierCount() {
        monitoringService.recordSupplierCount(10L);
        assertEquals(10.0, meterRegistry.get("supplier_total_count").gauge().value());
    }
    
    @Test
    void testRecordActiveSupplierCount() {
        monitoringService.recordActiveSupplierCount(5L);
        assertEquals(5.0, meterRegistry.get("supplier_active_count").gauge().value());
    }
    
    @Test
    void testRecordSupplierError() {
        monitoringService.recordSupplierError("update");
        assertEquals(1.0, meterRegistry.get("supplier_errors_total").tag("operation", "update").counter().count());
    }
    
    @Test
    void testCompleteLifecycle() {
        // Execute all methods
        monitoringService.recordSupplierCreation();
        monitoringService.recordSupplierUpdate();
        monitoringService.recordSupplierDeletion();
        
        Timer.Sample sample = monitoringService.startTimer();
        monitoringService.recordOperationTime(sample, "test");
        
        monitoringService.recordSupplierCount(10);
        monitoringService.recordActiveSupplierCount(8);
        monitoringService.recordSupplierError("test");
        
        // Verify all metrics exist
        assertNotNull(meterRegistry.find("supplier_creation_total").counter());
        assertNotNull(meterRegistry.find("supplier_update_total").counter());
        assertNotNull(meterRegistry.find("supplier_deletion_total").counter());
        assertNotNull(meterRegistry.find("supplier_errors_total").tags("operation", "test").counter());
    }
}