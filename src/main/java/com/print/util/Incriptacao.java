package com.print.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Incriptacao
{
	public static String transforma(String senha)
	{
		byte[] result;
		MessageDigest alo;
		StringBuilder hexString = new StringBuilder();
		String senhahex = null;

		try 
		{
			alo = MessageDigest.getInstance("SHA-1");
			
			result = alo.digest(senha.getBytes("UTF-8"));

			for (byte b : result)
				hexString.append(String.format("%02X", 0xFF & b));

			senhahex = hexString.toString();
		} 
		catch (UnsupportedEncodingException | NoSuchAlgorithmException e) 
		{
		}
		
		return senhahex;
	}			
	
}
