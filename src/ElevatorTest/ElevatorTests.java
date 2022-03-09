//package ElevatorTest;
//
//import Elevator.FloorSubsystem.Request;
//import Elevator.Enums.Direction;
//import Elevator.Enums.ElevatorStatus;
//import Elevator.Enums.SchedulerStatus;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.time.LocalDateTime;
//
//
//// Junit4 Dependency
//
//public class ElevatorTests {
//
//    Thread elevatorThread;
//    Elevator elevator;
//    Scheduler scheduler;
//
//    @Before
//    public  void setUp() throws InterruptedException {
//        scheduler = new Scheduler();
//        elevator = new Elevator(scheduler, 1);
//        elevator.setTestEnabled(true);
//        elevatorThread = new Thread(elevator, "Elevator 1");
//
//    }
//
//    @Test
//    public void completeRequest() throws InterruptedException, ClassNotFoundException {
//        //Create and start threads
//        elevatorThread.start();
//        Assert.assertEquals(elevator.getCurrentStatus(), ElevatorStatus.IDLE);
//        Assert.assertEquals(scheduler.getCurrentState(), SchedulerStatus.INITIALIZE.toString());
//        scheduler.putRequest(new Request(LocalDateTime.now(), 1, Direction.UP, 3));
//        Assert.assertEquals(scheduler.getCurrentState(), SchedulerStatus.ADDINGREQUEST.toString());
//        elevatorThread.join();
//
//        Assert.assertEquals(elevator.getCurrentStatus(), ElevatorStatus.CLOSE_DOOR);
//        Assert.assertEquals(scheduler.getCurrentState(), SchedulerStatus.COMPLETEREQUEST.toString());
//        Assert.assertEquals(scheduler.getRequestsCompleted(), 1);
//        Assert.assertEquals(scheduler.getCurrentState(), SchedulerStatus.PRINTREQUEST.toString());
//        Assert.assertTrue(elevator.getCurrentFloor() == 3);
//    }
//}