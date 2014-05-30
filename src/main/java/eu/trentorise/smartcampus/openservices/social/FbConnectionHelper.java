package eu.trentorise.smartcampus.openservices.social;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;


public class FbConnectionHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(FbConnectionHelper.class);
	
	@Autowired
	private ConnectionFactoryRegistry registry;
	
	private boolean checkConnectionDataExists(String providerUserId, ConnectionRepository repository){
		logger.info("getConnectionData {}",providerUserId);
		try{
			Connection<Facebook> existingFbConnection = repository.getConnection(Facebook.class,providerUserId);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean checkForUserInRepository(String providerUserId, ConnectionRepository repository){
		return checkConnectionDataExists(providerUserId, repository);
	}
	
	public void updateExistingConnectionInRepository(String providerUserId, String fbAccessToken, 
			ConnectionRepository repository){
		ConnectionFactory<Facebook> fbConnFactory = registry.getConnectionFactory(Facebook.class);
		ConnectionData existingConnData = repository.getConnection(Facebook.class, providerUserId).createData();
		ConnectionData updateConnectionData = new ConnectionData(existingConnData.getProviderId(),
				existingConnData.getProviderUserId(), 
				existingConnData.getDisplayName(), 
				existingConnData.getProfileUrl(), 
				existingConnData.getImageUrl(), 
				fbAccessToken, 
				existingConnData.getSecret(), 
				existingConnData.getRefreshToken(), 
				getNewExpireTime());
		Connection<Facebook>  newFbConnection = fbConnFactory.createConnection(updateConnectionData);
		repository.updateConnection(newFbConnection);
	}
	
	public void addNewConnectionToRepository(String providerUserId, String fbAccessToken, 
			ConnectionRepository repository){
		ConnectionFactory<Facebook> fbConnectionFactory = registry.getConnectionFactory(Facebook.class);
		ConnectionData updateConnectionData = new ConnectionData("facebook", providerUserId, 
				null, null, null, fbAccessToken, null, null, getNewExpireTime());
		Connection<Facebook> newFbConnection = fbConnectionFactory.createConnection(updateConnectionData);
		repository.addConnection(newFbConnection);
	}

	private Long getNewExpireTime() {
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR,30);
		Date expireTime = calendar.getTime();
		return expireTime.getTime();
	}
	
	
}