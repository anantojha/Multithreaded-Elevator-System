package ElevatorTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Elevator.Enums.ElevatorStatus;
import Elevator.Enums.FloorStatus;

public class FloorStatusTest {
	FloorStatus state;
	
	@Test
	/*
	 * test to verify the correct state flow of floor state machine
	 * 
	 * */
	public void test() {
		state = FloorStatus.INITIALIZE;
		
		state = state.nextState();
		assertEquals(FloorStatus.PROCESSING, state);
		
		state = state.nextState();
		assertEquals(FloorStatus.WAITING, state);
		
		state = state.nextState();
		assertEquals(FloorStatus.SENDING, state);
		
		state = state.nextState();
		assertEquals(FloorStatus.RECEIVING, state);
		
		state = state.nextState();
		assertEquals(FloorStatus.WAITING, state);
	}

}
