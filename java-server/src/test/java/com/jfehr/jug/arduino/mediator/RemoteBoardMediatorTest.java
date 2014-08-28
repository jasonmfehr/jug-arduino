package com.jfehr.jug.arduino.mediator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoteBoardMediatorTest {

	private static final String TEST_IP = "42.42.42.42";
	private static final Integer TEST_PORT = Integer.valueOf(123456789);
	private static final Byte TEST_DATA_BYTE_1 = Byte.valueOf((byte)2);
	private static final Byte TEST_DATA_BYTE_2 = Byte.valueOf((byte)3);
	
	@Mock private Socket mockSocket;
	@Mock private SocketFactory mockSocketFactory;
	@Mock private OutputStream mockOutputStream;
	@Mock private InputStream mockInputStream;
	@Captor ArgumentCaptor<Integer> writtenBytesCaptor;
	@InjectMocks private RemoteBoardMediator fixture;

	@Test
	public void testExecuteEchoCommand() throws Exception {
		RemoteBoardCommandTO testTO = new RemoteBoardCommandTO();
		List<Byte> actualReadBytes;
		
		when(mockSocketFactory.buildSocket(TEST_IP, TEST_PORT)).thenReturn(mockSocket);
		when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		when(mockSocket.getInputStream()).thenReturn(mockInputStream);
		when(mockInputStream.read()).thenReturn(TEST_DATA_BYTE_1.intValue(), TEST_DATA_BYTE_2.intValue(), -1);
		
		testTO.setRemoteIP(TEST_IP);
		testTO.setRemotePort(TEST_PORT);
		testTO.setCommand(RemoteBoardCommandEnum.ECHO);
		testTO.getDataBytes().add(TEST_DATA_BYTE_1);
		testTO.getDataBytes().add(TEST_DATA_BYTE_2);
		
		actualReadBytes = fixture.executeCommand(testTO);
		
		verify(mockSocketFactory).buildSocket(TEST_IP, TEST_PORT);
		verify(mockOutputStream, times(3)).write(writtenBytesCaptor.capture());
		
		assertEquals(RemoteBoardCommandEnum.ECHO.getCommandNumber().byteValue(), writtenBytesCaptor.getAllValues().get(0).byteValue());
		assertEquals(TEST_DATA_BYTE_1.byteValue(), writtenBytesCaptor.getAllValues().get(1).byteValue());
		assertEquals(TEST_DATA_BYTE_2.byteValue(), writtenBytesCaptor.getAllValues().get(2).byteValue());
		
		assertEquals(2, actualReadBytes.size());
		assertEquals(TEST_DATA_BYTE_1, actualReadBytes.get(0));
		assertEquals(TEST_DATA_BYTE_2, actualReadBytes.get(1));
		
		verify(mockSocket).close();
	}
	
	@Test
	public void testLEDStatusCommand() throws Exception {
		RemoteBoardCommandTO testTO = new RemoteBoardCommandTO();
		List<Byte> actualReadBytes;
		
		when(mockSocketFactory.buildSocket(TEST_IP, TEST_PORT)).thenReturn(mockSocket);
		when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		//when(mockSocket.getInputStream()).thenReturn(mockInputStream);
		//when(mockInputStream.read()).thenReturn(TEST_DATA_BYTE_1.intValue(), TEST_DATA_BYTE_2.intValue(), -1);
		
		testTO.setRemoteIP(TEST_IP);
		testTO.setRemotePort(TEST_PORT);
		testTO.setCommand(RemoteBoardCommandEnum.SET_LED);
		testTO.getDataBytes().add(TEST_DATA_BYTE_1);
		testTO.getDataBytes().add(TEST_DATA_BYTE_2);
		
		actualReadBytes = fixture.executeCommand(testTO);
		
		verify(mockSocketFactory).buildSocket(TEST_IP, TEST_PORT);
		verify(mockOutputStream, times(3)).write(writtenBytesCaptor.capture());
		
		assertEquals(RemoteBoardCommandEnum.SET_LED.getCommandNumber().byteValue(), writtenBytesCaptor.getAllValues().get(0).byteValue());
		assertEquals(TEST_DATA_BYTE_1.byteValue(), writtenBytesCaptor.getAllValues().get(1).byteValue());
		assertEquals(TEST_DATA_BYTE_2.byteValue(), writtenBytesCaptor.getAllValues().get(2).byteValue());
		
		//assertEquals(2, actualReadBytes.size());
		//assertEquals(TEST_DATA_BYTE_1, actualReadBytes.get(0));
		//assertEquals(TEST_DATA_BYTE_2, actualReadBytes.get(1));
		assertEquals(0, actualReadBytes.size());
		
		verify(mockSocket).close();
	}

}
