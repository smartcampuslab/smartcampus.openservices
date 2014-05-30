package eu.trentorise.smartcampus.openservices.social;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

public class FbOperationsHelper {
	
	public FacebookProfile getUser(Facebook fb){
		return fb.userOperations().getUserProfile();
	}

}
