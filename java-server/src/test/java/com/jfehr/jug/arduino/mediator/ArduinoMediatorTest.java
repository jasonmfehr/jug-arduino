package com.jfehr.jug.arduino.mediator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.jug.arduino.led.LEDStatus;

@RunWith(MockitoJUnitRunner.class)
public class ArduinoMediatorTest {

	private static final String TEST_IP = "42.42.42.42";
	private static final Integer TEST_ARDUINO_PORT = Integer.valueOf(123456789);
	
	@Mock private Socket mockSocket;
	@Mock private SocketFactory mockSocketFactory;
	@Mock private OutputStream mockOutputStream;
	@Captor ArgumentCaptor<Integer> writtenBytesCaptor;
	@InjectMocks private ArduinoMediator fixture;
	
	@Test
	public void testSetLed() throws Exception {
		LEDStatus status = new LEDStatus();
		
		status.setLedNumber(Integer.valueOf(1));
		status.setLedOn(Boolean.TRUE);
		when(mockSocketFactory.buildSocket(TEST_IP, TEST_ARDUINO_PORT)).thenReturn(mockSocket);
		when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		
		fixture.setLed(status, TEST_IP, TEST_ARDUINO_PORT);
		
		verify(mockOutputStream, times(5)).write(writtenBytesCaptor.capture());
		
		assertEquals((int)ArduinoCommandEnum.SET_LED.getCommandNumber().byteValue(), writtenBytesCaptor.getAllValues().get(0).byteValue());
		assertEquals(status.getLedNumber().byteValue(), writtenBytesCaptor.getAllValues().get(1).byteValue());
		assertEquals(status.getLedOn() ? (byte)1 : (byte)0, writtenBytesCaptor.getAllValues().get(2).byteValue());
		assertEquals((byte)-1, writtenBytesCaptor.getAllValues().get(3).byteValue());
		assertEquals((byte)-1, writtenBytesCaptor.getAllValues().get(4).byteValue());
		
		verify(mockSocket).close();
	}

}
