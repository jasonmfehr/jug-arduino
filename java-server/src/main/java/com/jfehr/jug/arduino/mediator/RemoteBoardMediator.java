package com.jfehr.jug.arduino.mediator;

import java.io.IOException;
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
		List<Byte> receivedBytes = new LinkedList<Byte>();
		int in;
		
		if(commandTO.getDataBytes().size() < commandTO.getCommand().getNumberOfDataBytes()){
			throw new MissingInputDataException(commandTO.getCommand(), commandTO.getDataBytes());
		}
		
		try{
			LOGGER.debug("attempting to open connection with remote board at address [{}:{}]", commandTO.getRemoteIP(), commandTO.getRemotePort());
			remoteBoardSocket = socketFactory.buildSocket(commandTO.getRemoteIP(), commandTO.getRemotePort());
			
			LOGGER.debug("writing command byte [{}]", commandTO.getCommand().getCommandNumber());
			remoteBoardSocket.getOutputStream().write(commandTO.getCommand().getCommandNumber().byteValue());
			
			for(Byte b : commandTO.getDataBytes()){
				LOGGER.debug("writing data byte [{}]", b);
				remoteBoardSocket.getOutputStream().write(b.byteValue());
			}
			
			//wait for reply from arudino
			if(commandTO.getCommand().getNumberOfResponseBytes() > 0){
				LOGGER.debug("listening for reply from remote board, waiting on [{}] number of bytes", commandTO.getCommand().getNumberOfResponseBytes());
				remoteBoardSocket.setSoTimeout(10000);
				while(receivedBytes.size() < commandTO.getCommand().getNumberOfResponseBytes() && (in = remoteBoardSocket.getInputStream().read()) != -1){
					LOGGER.debug("received byte [{}]", in);
					receivedBytes.add(Byte.valueOf((byte)in));
				}
				
				LOGGER.debug("received [{}] bytes: {}", receivedBytes.size(), receivedBytes);
				
				if(!Integer.valueOf(receivedBytes.size()).equals(commandTO.getCommand().getNumberOfResponseBytes())){
					throw new MissingResponseDataException(commandTO.getCommand(), receivedBytes);
				}
			}else{
				LOGGER.debug("not listening for reply from remote board as 0 response data bytes are expected");
			}
			
			return receivedBytes;
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
					LOGGER.debug("close connection with remote board at address [{}:{}]", commandTO.getRemoteIP(), commandTO.getRemotePort());
					remoteBoardSocket.close();
				} catch (IOException e) {
					LOGGER.error("error closing socket", e);
				}
			}
		}
		
	}
	
}
