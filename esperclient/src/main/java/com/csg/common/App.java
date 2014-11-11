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
//		testarWebServiceCitsmart2();
		enviarEvmCronDTO();

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
	
	public static void enviarEvmCronDTO() {
		try {
			System.out.println("Atualizando temporizador...");
			
			String userPassword = "ADMIN" + ":" + "1";
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
			
			
			URL url = new URL("http://10.2.1.115:8089/citsmartevm/ws/atualizarTemporizador");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/json");
			
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("  {  ");
			stringBuilder.append("      \"idCron\":3,");
			stringBuilder.append("      \"nome\":\"A cada 15 minutos\",");
			stringBuilder.append("      \"expressao\":\"0 */15 * * * ? *\",");
			stringBuilder.append("      \"idUsuarioCriador\":22,");
			stringBuilder.append("      \"idUsuarioModificador\":22,");
			stringBuilder.append("      \"dataHoraInicio\":1415356956000,");
			stringBuilder.append("      \"dataHoraFim\":null,");
			stringBuilder.append("      \"dataHoraUltimaAlteracao\":1415108352000,");
			stringBuilder.append("      \"nomeUsuarioModificador\":null,");
			stringBuilder.append("      \"nomeUsuarioCriador\":null,");
			stringBuilder.append("      \"listGerente\":[  ");
			stringBuilder.append("         {  ");
			stringBuilder.append("            \"idGerente\":1,");
			stringBuilder.append("            \"nome\":\"Gerente do IC CITGOPC959 via Nagios 21\",");
			stringBuilder.append("            \"idItemConfiguracaoPai\":98728,");
			stringBuilder.append("            \"idCaracteristicaIdentificacao\":null,");
			stringBuilder.append("            \"host\":null,");
			stringBuilder.append("            \"idCron\":3,");
			stringBuilder.append("            \"idFluxoDown\":1,");
			stringBuilder.append("            \"idUsuarioCriador\":22,");
			stringBuilder.append("            \"idUsuarioModificador\":null,");
			stringBuilder.append("            \"dataHoraInicio\":1415290706000,");
			stringBuilder.append("            \"dataHoraFim\":null,");
			stringBuilder.append("            \"dataHoraUltimaAlteracao\":null,");
			stringBuilder.append("            \"idConexao\":11,");
			stringBuilder.append("            \"listItensGerenciados\":[  ");
			stringBuilder.append("               {  ");
			stringBuilder.append("                  \"idGerenciado\":1,");
			stringBuilder.append("                  \"idGerente\":1,");
			stringBuilder.append("                  \"servico\":null,");
			stringBuilder.append("                  \"idItemConfiguracaoFilho\":98977,");
			stringBuilder.append("                  \"idCaracteristicaIdentificacao\":10,");
			stringBuilder.append("                  \"idCheck\":8,");
			stringBuilder.append("                  \"idEvento\":1,");
			stringBuilder.append("                  \"idFluxoAcaoWarning\":null,");
			stringBuilder.append("                  \"idFluxoAcaoException\":null,");
			stringBuilder.append("                  \"idUsuarioCriador\":22,");
			stringBuilder.append("                  \"idUsuarioModificador\":null,");
			stringBuilder.append("                  \"dataHoraInicio\":1415123100000,");
			stringBuilder.append("                  \"dataHoraFim\":null,");
			stringBuilder.append("                  \"dataHoraUltimaAlteracao\":null,");
			stringBuilder.append("                  \"itemConfiguracaoFilhoMonitorado\":null,");
			stringBuilder.append("                  \"evmCheckDto\":{  ");
			stringBuilder.append("                     \"idCheck\":8,");
			stringBuilder.append("                     \"idEvento\":1,");
			stringBuilder.append("                     \"nome\":\"Check Disco\",");
			stringBuilder.append("                     \"idTipoItemConfiguracao\":10,");
			stringBuilder.append("                     \"idCaracteristicaPrincipal\":54,");
			stringBuilder.append("                     \"tipoCheck\":1,");
			stringBuilder.append("                     \"idCaracteristicaComplementar\":null,");
			stringBuilder.append("                     \"simboloDaExpressaoWarning\":\"2\",");
			stringBuilder.append("                     \"valorDaExpressaoWarning\":\"20\",");
			stringBuilder.append("                     \"simboloDaExpressaoException\":\"2\",");
			stringBuilder.append("                     \"valorDaExpressaoException\":\"10\",");
			stringBuilder.append("                     \"idUsuarioCriador\":22,");
			stringBuilder.append("                     \"idUsuarioModificador\":null,");
			stringBuilder.append("                     \"dataHoraInicio\":1415126579000,");
			stringBuilder.append("                     \"dataHoraFim\":null,");
			stringBuilder.append("                     \"dataHoraUltimaAlteracao\":null,");
			stringBuilder.append("                     \"nomeUsuarioModificador\":null,");
			stringBuilder.append("                     \"nomeUsuarioCriador\":null");
			stringBuilder.append("                  }");
			stringBuilder.append("               }");
			stringBuilder.append("            ],");
			stringBuilder.append("            \"itemConfiguracaoMonitorado\":null,");
			stringBuilder.append("            \"evmConexaoDto\":{  ");
			stringBuilder.append("               \"idConexao\":11,");
			stringBuilder.append("               \"nome\":\"A - Nagios 10.2.1.21\",");
			stringBuilder.append("               \"ferramenta\":\"NAGIOS\",");
			stringBuilder.append("               \"protocolo\":\"TCP\",");
			stringBuilder.append("               \"endereco\":\"10.2.1.21\",");
			stringBuilder.append("               \"porta\":6557,");
			stringBuilder.append("               \"usuario\":\"nagiosadmin\",");
			stringBuilder.append("               \"senha\":\"123mudar\",");
			stringBuilder.append("               \"idUsuarioCriador\":22,");
			stringBuilder.append("               \"idUsuarioModificador\":1899,");
			stringBuilder.append("               \"dataHoraInicio\":1415290565000,");
			stringBuilder.append("               \"dataHoraFim\":null,");
			stringBuilder.append("               \"dataHoraUltimaAlteracao\":1415294119000,");
			stringBuilder.append("               \"nomeUsuarioModificador\":null,");
			stringBuilder.append("               \"nomeUsuarioCriador\":null");
			stringBuilder.append("            }");
			stringBuilder.append("         }");
			stringBuilder.append("      ],");
			stringBuilder.append("      \"ano\":null,");
			stringBuilder.append("      \"diaDaSemana\":null,");
			stringBuilder.append("      \"mes\":null,");
			stringBuilder.append("      \"diaDoMes\":null,");
			stringBuilder.append("      \"hora\":null,");
			stringBuilder.append("      \"minuto\":null,");
			stringBuilder.append("      \"segundo\":null");
			stringBuilder.append("   }");			
			
//			stringBuilder.append("   {  ");
//			stringBuilder.append("      \"idCron\":5,");
//			stringBuilder.append("      \"nome\":\"A cada - 2-  minuto\",");
//			stringBuilder.append("      \"expressao\":\"0 */2 * * * ? *\",");
//			stringBuilder.append("      \"idUsuarioCriador\":1899,");
//			stringBuilder.append("      \"idUsuarioModificador\":null,");
//			stringBuilder.append("      \"dataHoraInicio\":1415372558000,");
//			stringBuilder.append("      \"dataHoraFim\":null,");
//			stringBuilder.append("      \"dataHoraUltimaAlteracao\":null,");
//			stringBuilder.append("      \"nomeUsuarioModificador\":null,");
//			stringBuilder.append("      \"nomeUsuarioCriador\":null,");
//			stringBuilder.append("      \"listGerente\":[  ");
//			stringBuilder.append("         {  ");
//			stringBuilder.append("            \"idGerente\":2,");
//			stringBuilder.append("            \"nome\":\"Gerente do IC CITGOPC959 via Nagios\",");
//			stringBuilder.append("            \"idItemConfiguracaoPai\":98728,");
//			stringBuilder.append("            \"idCaracteristicaIdentificacao\":null,");
//			stringBuilder.append("            \"host\":\"CITGOPC959-VALDOILO\",");
//			stringBuilder.append("            \"idCron\":5,");
//			stringBuilder.append("            \"idFluxoDown\":1,");
//			stringBuilder.append("            \"idUsuarioCriador\":22,");
//			stringBuilder.append("            \"idUsuarioModificador\":null,");
//			stringBuilder.append("            \"dataHoraInicio\":1415378163000,");
//			stringBuilder.append("            \"dataHoraFim\":null,");
//			stringBuilder.append("            \"dataHoraUltimaAlteracao\":null,");
//			stringBuilder.append("            \"idConexao\":15,");
//			stringBuilder.append("            \"listItensGerenciados\":null,");
//			stringBuilder.append("            \"itemConfiguracaoMonitorado\":null,");
//			stringBuilder.append("            \"evmConexaoDto\":{  ");
//			stringBuilder.append("               \"idConexao\":15,");
//			stringBuilder.append("               \"nome\":\"A - Citsmart Nagios 172.20.0.68\",");
//			stringBuilder.append("               \"ferramenta\":\"NAGIOS\",");
//			stringBuilder.append("               \"protocolo\":\"TCP\",");
//			stringBuilder.append("               \"endereco\":\"172.20.0.68\",");
//			stringBuilder.append("               \"porta\":6557,");
//			stringBuilder.append("               \"usuario\":null,");
//			stringBuilder.append("               \"senha\":null,");
//			stringBuilder.append("               \"idUsuarioCriador\":1899,");
//			stringBuilder.append("               \"idUsuarioModificador\":null,");
//			stringBuilder.append("               \"dataHoraInicio\":1415378373000,");
//			stringBuilder.append("               \"dataHoraFim\":null,");
//			stringBuilder.append("               \"dataHoraUltimaAlteracao\":null,");
//			stringBuilder.append("               \"nomeUsuarioModificador\":null,");
//			stringBuilder.append("               \"nomeUsuarioCriador\":null");
//			stringBuilder.append("            }");
//			stringBuilder.append("         }");
//			stringBuilder.append("  ]");
//			stringBuilder.append("  }");
			
			
			OutputStream os = connection.getOutputStream();
			os.write(stringBuilder.toString().getBytes());
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
