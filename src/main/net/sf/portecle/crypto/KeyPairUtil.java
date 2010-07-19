/*
 * KeyPairUtil.java
 * This file is part of Portecle, a multipurpose keystore and certificate tool.
 *
 * Copyright © 2004 Wayne Grant, waynedgrant@hotmail.com
 *             2005-2008 Ville Skyttä, ville.skytta@iki.fi
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sf.portecle.crypto;

import static net.sf.portecle.FPortecle.RB;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.DSAKey;
import java.security.interfaces.RSAKey;
import java.text.MessageFormat;

import javax.crypto.interfaces.DHKey;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyParameters;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

/**
 * Provides utility methods for the generation of keys.
 */
public final class KeyPairUtil
{
	/**
	 * Private to prevent construction.
	 */
	private KeyPairUtil()
	{
		// Nothing to do
	}

	/**
	 * Generate a key pair.
	 * 
	 * @param keyPairType Key pair type to generate
	 * @param iKeySize Key size of key pair
	 * @return A key pair
	 * @throws CryptoException If there was a problem generating the key pair
	 */
	public static KeyPair generateKeyPair(KeyPairType keyPairType, int iKeySize)
	    throws CryptoException
	{
		try
		{
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(keyPairType.name());

			// Create a SecureRandom
			SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");

			// Initialize key pair generator with key strength and a randomness
			keyPairGen.initialize(iKeySize, rand);

			// Generate and return the key pair
			KeyPair keyPair = keyPairGen.generateKeyPair();
			return keyPair;
		}
		catch (NoSuchAlgorithmException ex)
		{
			throw new CryptoException(MessageFormat.format(
			    RB.getString("NoGenerateKeypair.exception.message"), keyPairType), ex);
		}
		catch (InvalidParameterException ex)
		{
			throw new CryptoException(MessageFormat.format(
			    RB.getString("NoGenerateKeypairParm.exception.message"), keyPairType), ex);
		}
	}

	/**
	 * Get the key size of a public key.
	 * 
	 * @param pubKey The public key
	 * @return The key size
	 * @throws CryptoException If there is a problem getting the key size
	 */
	public static int getKeyLength(PublicKey pubKey)
	    throws CryptoException
	{
		if (pubKey instanceof RSAKey)
		{
			return ((RSAKey) pubKey).getModulus().bitLength();
		}
		else if (pubKey instanceof DSAKey)
		{
			return ((DSAKey) pubKey).getParams().getP().bitLength();
		}
		else if (pubKey instanceof DHKey)
		{
			return ((DHKey) pubKey).getParams().getP().bitLength();
		}
		else
		{
			throw new CryptoException(RB.getString("NoPublicKeysize.exception.message"));
		}
	}

	/**
	 * Get the key size of a key represented by key parameters.
	 * 
	 * @param keyParams The key parameters
	 * @return The key size
	 * @throws CryptoException If there is a problem getting the key size
	 */
	public static int getKeyLength(AsymmetricKeyParameter keyParams)
	    throws CryptoException
	{
		if (keyParams instanceof RSAKeyParameters)
		{
			return ((RSAKeyParameters) keyParams).getModulus().bitLength();
		}
		else if (keyParams instanceof DSAKeyParameters)
		{
			return ((DSAKeyParameters) keyParams).getParameters().getP().bitLength();
		}
		else if (keyParams instanceof DHKeyParameters)
		{
			return ((DHKeyParameters) keyParams).getParameters().getP().bitLength();
		}
		else
		{
			throw new CryptoException(RB.getString("NoPublicKeysize.exception.message"));
		}
	}
}
