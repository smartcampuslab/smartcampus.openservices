package com.openserviceproject.support;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Just a sample of a generation of a private and a public key
 * with java security
 * @author Giulia Canobbio
 *
 */
public class GenerateKey {
	
	private PrivateKey priv;
	private PublicKey publ;
	
	public GenerateKey(){
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);
			
			//Generate pair of keys
			KeyPair pair = keyGen.generateKeyPair();
			this.priv = pair.getPrivate();
			this.publ = pair.getPublic();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public PrivateKey getPriv() {
		return priv;
	}

	public void setPriv(PrivateKey priv) {
		this.priv = priv;
	}

	public PublicKey getPubl() {
		return publ;
	}

	public void setPubl(PublicKey publ) {
		this.publ = publ;
	}

}
