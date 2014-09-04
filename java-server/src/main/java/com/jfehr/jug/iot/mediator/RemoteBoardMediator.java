package com.jfehr.jug.iot.mediator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoteBoardMediator implements IMediator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteBoardMediator.class);
	
	@Autowired
	private SocketFactory socketFactory;
	
	@Override
	public List<Byte> executeCommand(RemoteBoardCommandTO commandTO) {
		Socket remoteBoardSocket = null;
		
		//ensure the provided number of data bytes equals what is expected for the provided command
		if(commandTO.getDataBytes().size() < commandTO.getCommand().getNumberOfDataBytes()){
			throw new MissingInputDataException(commandTO.getCommand(), commandTO.getDataBytes());
		}
		
		try{
			LOGGER.debug("attempting to open connection with remote board at address [{}:{}]", commandTO.getInputDataTO().getRemoteBoardIP(), commandTO.getInputDataTO().getRemoteBoardPort());
			remoteBoardSocket = socketFactory.buildSocket(commandTO.getInputDataTO().getRemoteBoardIP(), commandTO.getInputDataTO().getRemoteBoardPort());
			
			this.writeCommandAndDataBytes(remoteBoardSocket.getOutputStream(), commandTO);
			return this.readResponse(remoteBoardSocket.getInputStream(), commandTO);
		}catch(UnknownHostException e){
			LOGGER.error("exception communicating with remote board", e);
			throw new RemoteBoardMediatorException(e);
		}catch(IOException e){
			LOGGER.error("exception communicating with remote board", e);
			throw new RemoteBoardMediatorException(e);
		}catch(SocketInitializationException e){
			LOGGER.error("exception communicating with remote board", e);
			throw new RemoteBoardMediatorException(e);
		}finally{
			if(remoteBoardSocket != null){
				try {
					LOGGER.debug("close connection with remote board at address [{}:{}]", commandTO.getInputDataTO().getRemoteBoardIP(), commandTO.getInputDataTO().getRemoteBoardPort());
					remoteBoardSocket.close();
				} catch (IOException e) {
					LOGGER.error("error closing socket", e);
				}
			}
		}
	}
	
	private void writeCommandAndDataBytes(OutputStream outStream, RemoteBoardCommandTO command) throws IOException {
		LOGGER.debug("writing command byte [{}]", command.getCommand().getCommandNumber());
		outStream.write(command.getCommand().getCommandNumber().byteValue());
			
		if(command.getCommand().getNumberOfDataBytes() > 0){
			for(Byte b : command.getDataBytes()){
				LOGGER.debug("writing data byte [{}]", b);
				outStream.write(b.byteValue());
			}
		}else{
			LOGGER.debug("not writing any data bytes as data byte count is {}", command.getCommand().getNumberOfDataBytes());
		}
	}
	
	private List<Byte> readResponse(InputStream inStream, RemoteBoardCommandTO commandTO) throws IOException {
		final Integer expectedResponseBytesCount = commandTO.getCommand().getNumberOfResponseBytes();
		List<Byte> receivedBytes = new LinkedList<Byte>();
		int in;
		
		if(expectedResponseBytesCount > 0){
			LOGGER.debug("listening for reply from remote board, waiting on [{}] number of bytes", expectedResponseBytesCount);
			
			//read one byte at a time from the remote board until the expected number of response bytes have 
			//been read or the end of the input stream is reached
			while(receivedBytes.size() < expectedResponseBytesCount && (in = inStream.read()) != -1){
				LOGGER.debug("received byte [{}]", in);
				receivedBytes.add(Byte.valueOf((byte)in));
			}
			
			LOGGER.debug("received [{}] bytes: {}", receivedBytes.size(), receivedBytes);
			
			if(!Integer.valueOf(receivedBytes.size()).equals(expectedResponseBytesCount)){
				//the number of bytes that was received does not match the number that was expected
				throw new MissingResponseDataException(commandTO.getCommand(), receivedBytes);
			}
		}else{
			LOGGER.debug("not listening for reply from remote board as [{}] response data bytes are expected", expectedResponseBytesCount);
		}
		
		return receivedBytes;
	}
	
}
