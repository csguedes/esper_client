package com.csg.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;


public class App {
	@SuppressWarnings({ "resource", "deprecation" })
	public static void main(String[] args) {
//		enviarInventarios();
//		testarSeguranca();
//		enviarConsultasNagios();
		testarWebServiceCitsmart2();

	}
	

	private static String taskEnviarXMLsCTM() {
		
        String resultado="";
		try {
			
			// Início
			
			
	     	String target_dir = "D:\\cristian\\backup\\inventários";
	        File dir = new File(target_dir);
	        File[] files = dir.listFiles();

	        for (File f : files) {
	            if(f.isFile()) {
	                BufferedReader inputStream = null;

	                try {
	                    inputStream = new BufferedReader(
	                                    new FileReader(f));
	                    String line;

	                    while ((line = inputStream.readLine()) != null) {
	                    	resultado+=line;
	                    }
	                }
	                finally {
	                    if (inputStream != null) {
	                        inputStream.close();
	                    }
	                }
	            }
	            
	    		resultado = resultado.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><EventoCTM>");
	    		resultado = resultado+"</EventoCTM>";
	    		
	    		// Remover caracteres "inválidos"
	    		resultado = resultado.replaceAll("\0", "_");
	    		
	    		enviarXMLCTM(resultado);
	    		
	    		resultado = "";
	            
	        }		
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return resultado;
	}
	
	public static void testarWebServiceCitsmart2() {
		try {
			System.out.println("Testando WebService Citsmart...");
			
			
			
			URL url = new URL("http://localhost:8080/citsmart/evm/agendamentos");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/json");
			
			StringBuilder dados = new StringBuilder();

//			dados.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//			dados.append("<LoginPojo>");
//			dados.append("<UserName>cristian.guedes</UserName>");
//			dados.append("<Password>teste</Password>");
//			dados.append("</LoginPojo>");
			
			dados.append("{\"userName\":\"cristian.guedes\",\"password\":\"1\"}");	
			
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
	
			OutputStream os = connection.getOutputStream();
			os.write(dados.toString().getBytes());
			os.flush();
			
			String saidaDoServidor = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String output;

			while ((output = br.readLine()) != null) {
				saidaDoServidor+=output;
			}
				
			
			System.out.println(saidaDoServidor);
			
			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());
			connection.disconnect();  
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	
	public static void testarWebServiceCitsmart() {
		try {
			System.out.println("Testando WebService Citsmart...");
			
			
			
			URL url = new URL("http://localhost:8080/citsmart/evm/agendamentos");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
//			connection.setRequestProperty("Content-Type", "application/xml");
			
//			StringBuilder XML = new StringBuilder();
//			XML.append("<CtNotificationNew>");
//			XML.append("<Description>Agendamentos</Description>");
//			XML.append("<MessageID>Agendamentos</MessageID>");
//			XML.append("<SessionID>Agendamentos</SessionID>");
//			XML.append("</CtNotificationNew>");
//			 			
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
	
//			OutputStream os = connection.getOutputStream();
//			os.write(XML.toString().getBytes());
//			os.flush();
			
			String saidaDoServidor = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String output;

			while ((output = br.readLine()) != null) {
				saidaDoServidor+=output;
			}
			
			System.out.println(saidaDoServidor);
			
			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());
			connection.disconnect();  
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void testarSeguranca() {
		try {
			System.out.println("Testando seguranca...");
			
			String userPassword = "ADMIN" + ":" + "1";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			
			
			URL url = new URL("http://10.2.1.115:8089/ExemploEsper/user-service/users/1");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setRequestMethod("PUT");
			
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);

			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());
			connection.disconnect();  
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void enviarXMLCTM(String XML) {
		try {
			System.out.println("enviando XML CTM...");
			
			String userPassword = "ADMIN" + ":" + "1";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			
			
//			URL url = new URL("http://10.2.1.115:8089/ExemploEsper/send/xmlCTM?oneway=true&username=citsmart&password=centralit");
			URL url = new URL("http://10.2.1.115:8089/ExemploEsper/send/xmlCTM");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/xml");
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);

			OutputStream os = connection.getOutputStream();
			os.write(XML.getBytes());
			os.flush();

			System.out.println(connection.getResponseMessage());
			System.out.println(connection.getResponseCode());
			connection.disconnect();  
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void enviarInventarios() {
		ScheduledExecutorService executor =
			    Executors.newSingleThreadScheduledExecutor();

			Runnable periodicTask = new Runnable() {
			    public void run() {
			        
		    		taskEnviarXMLsCTM();

			    }
			};

			executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);
		
	}
	
	
	public static void enviarConsultasNagios() {
		ScheduledExecutorService executor =
			    Executors.newSingleThreadScheduledExecutor();

			Runnable periodicTask = new Runnable() {
			    public void run() {
			        
			    	taskEnviarConsultaNagios();
			    	

			    }
			};

			executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);
		
	}
	
	private static String consultaNagios() {
		String jsonResponse = "";
		try {
			String obj = "Host";
			ClientRequest request = new ClientRequest(String.format("http://10.2.1.138:8080/Citeventger/zabbix/nagios_host/get/tcp,10.2.1.104,6557,hosts,,name = srv104", obj));
			request.accept("application/json");
			ClientResponse<String> response = request.get(String.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));

			String output = "";
			jsonResponse = "";
			while ((output = br.readLine()) != null) {
				jsonResponse += output;
			}
			


		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
		
		return jsonResponse;

	}		
		
  public static void taskEnviarConsultaNagios() {
		System.out.println("enviando EventoNagios ...");
	  
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL("http://10.2.1.115:8089/ExemploEsper/send/jsonnagios/");

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);

			OutputStream os = connection.getOutputStream();
			os.write(consultaNagios().toLowerCase().getBytes());
			os.flush();

			System.out.println(connection.getResponseMessage());

			connection.getResponseCode();
			connection.disconnect();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
  }

	
	
	

}
