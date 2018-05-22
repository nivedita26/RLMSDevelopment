package com.rlms.constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class Test {

	
	
	 
	public static void main(String[] args) {
	 
	String str="Hello world";
	String revstring="";
	 
	for(int i=str.length()-1;i>=0;--i){
	revstring +=str.charAt(i);
	}
	 
	System.out.println(revstring);
	}
	
	
	   
	
}
