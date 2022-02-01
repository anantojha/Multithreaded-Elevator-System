package Elevator;


public class Elevator implements Runnable{

    private Scheduler scheduler;
    private int Id;
    private int currentFloor;

    public Elevator(Scheduler scheduler, int id){
        this.scheduler = scheduler;
        this.Id = id;
        this.currentFloor = 1;
    }


    @Override
    public void run() {
        while(true) {
            // synchronize table object
            synchronized (scheduler) {
                // get request from table
                Request serviceRequest = scheduler.getRequest();

                // validate if source floor of request is the thread's floor
                if (serviceRequest.getSourceFloor() == currentFloor) {
                    System.out.println(Thread.currentThread().getName() +" in servicing "+ serviceRequest);
                    try {
                        Thread.sleep((serviceRequest.getDestinationFloor() - serviceRequest.getSourceFloor()) * 1000);
                        scheduler.serviceRequest(serviceRequest);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }
    }
}
