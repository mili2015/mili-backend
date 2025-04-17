/**
 * 
 */
package br.com.mili.milibackend.shared.service;

import br.com.mili.milibackend.shared.MyResponse;
import br.com.mili.milibackend.shared.exception.MyException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * @author felipe.alves
 *
 */
public class DefaultHttpService
{
	private final String url;

	public DefaultHttpService(String url)
	{
		super();
		this.url = url;
	}
	
	public String post(String urn, String json) throws IOException
	{
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		
		if (urn == null)
			urn = "";
		
		HttpPost httpPost = new HttpPost(url + urn);
		StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        
        try
		{
			CloseableHttpResponse httpResponse = client.execute(httpPost);
			
			String response = ""; 
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
			    response = EntityUtils.toString(responseEntity);
			}

			return response;
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	public MyResponse post(String urn, String json, Map<String, String> headers) throws IOException
	{
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();

		if (urn == null)
			urn = "";

		HttpPost httpPost = new HttpPost(url + urn);
		StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// adiciona os headers na requisição
		for (Map.Entry<String,String> header : headers.entrySet())
		{
			httpPost.setHeader(header.getKey(), header.getValue());
		}

		try
		{
			CloseableHttpResponse httpResponse = client.execute(httpPost);

			String response = "";
			HttpEntity responseEntity = httpResponse.getEntity();

			if(responseEntity != null) {
				response = EntityUtils.toString(responseEntity);
			}

			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	public MyResponse getAuth(String urn, String user, String pass) throws  IOException
	{
		
		CloseableHttpClient client = HttpClientBuilder.create()
			.setDefaultCredentialsProvider(getProvider(user, pass))
			.build();
		
		if (urn == null)
			urn = "";
		
        try
		{
        	HttpGet request = new HttpGet(url + urn);
			CloseableHttpResponse httpResponse = client.execute(request);
			
			String response = ""; 
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
			    response = EntityUtils.toString(responseEntity);
			}
			System.out.println(response);
			
			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	public MyResponse get(String urn, Map<String, String> headers) throws  IOException
	{
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();

		if (urn == null)
			urn = "";

		try
		{
			HttpGet request = new HttpGet(url + urn);

			for (Map.Entry<String,String> header : headers.entrySet())
			{
				request.setHeader(header.getKey(), header.getValue());
			}

			CloseableHttpResponse httpResponse = client.execute(request);

			// adiciona os headers na requisição

			String response = "";
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
				response = EntityUtils.toString(responseEntity);
			}
			System.out.println(response);

			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	public MyResponse get(String urn) throws  IOException
	{

		CloseableHttpClient client = HttpClientBuilder.create().build();

		if (urn == null)
			urn = "";

		try
		{
			HttpGet request = new HttpGet(url + urn);
			CloseableHttpResponse httpResponse = client.execute(request);

			String response = "";
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
				response = EntityUtils.toString(responseEntity);
			}
			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	public MyResponse getBytes(String urn) throws IOException
	{
		CloseableHttpClient client = HttpClientBuilder.create().build();

		if (urn == null)
			urn = "";

		HttpGet request = new HttpGet(url + urn);
		CloseableHttpResponse httpResponse = client.execute(request);

		byte[] content = EntityUtils.toByteArray(httpResponse.getEntity());
		return new MyResponse(httpResponse.getStatusLine().getStatusCode(), "", content);

	}

	public MyResponse getBytesApiKey(String urn, String key, String value) throws IOException
	{
		CloseableHttpClient client = HttpClientBuilder.create().build();

		if (urn == null)
			urn = "";

		HttpGet request = new HttpGet(url + urn);
		request.setHeader(key, value);
		CloseableHttpResponse httpResponse = client.execute(request);

		byte[] content = EntityUtils.toByteArray(httpResponse.getEntity());
		return new MyResponse(httpResponse.getStatusLine().getStatusCode(), "", content);

	}
	
	public MyResponse postAuth(String urn, String json, String user, String pass) throws  IOException
	{
		
		CloseableHttpClient client = HttpClientBuilder.create()
			.setDefaultCredentialsProvider(getProvider(user, pass))
			.build();
		
		if (urn == null)
			urn = "";
		
		HttpPost httpPost = new HttpPost(url + urn);
		StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        
        try
		{
			CloseableHttpResponse httpResponse = client.execute(httpPost);
			
			String response = ""; 
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
			    response = EntityUtils.toString(responseEntity);
			}
			System.out.println(response);
			
			
			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
			
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}

	public MyResponse postApiKey(String urn, String json, String key, String value) throws IOException
	{

		CloseableHttpClient client = HttpClientBuilder.create().build();

		if (urn == null)
			urn = "";

		HttpPost httpPost = new HttpPost(url + urn);
		StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader(key, value);

		try
		{
			CloseableHttpResponse httpResponse = client.execute(httpPost);

			String response = "";
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
				response = EntityUtils.toString(responseEntity);
			}

			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException ignored)
			{
			}
		}
	}
	
	public MyResponse putAuth(String urn, String json, String user, String pass) throws  IOException
	{
		
		CloseableHttpClient client = HttpClientBuilder.create()
			.setDefaultCredentialsProvider(getProvider(user, pass))
			.build();
		
		if (urn == null)
			urn = "";
		
		HttpPut httpPut = new HttpPut(url + urn);
		StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPut.setEntity(entity);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        
        try
		{
			CloseableHttpResponse httpResponse = client.execute(httpPut);
			
			String response = ""; 
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
			    response = EntityUtils.toString(responseEntity);
			}
			System.out.println(response);
			
			
			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	public MyResponse deleteAuth(String urn, String user, String pass) throws  IOException
	{
		
		CloseableHttpClient client = HttpClientBuilder.create()
			.setDefaultCredentialsProvider(getProvider(user, pass))
			.build();
		
		if (urn == null)
			urn = "";
		
		HttpDelete httpDelete = new HttpDelete(url + urn);
        
        try
		{
			CloseableHttpResponse httpResponse = client.execute(httpDelete);
			
			String response = ""; 
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity != null) {
			    response = EntityUtils.toString(responseEntity);
			}
			System.out.println(response);
			
			
			return new MyResponse(httpResponse.getStatusLine().getStatusCode(), response);
			
		}
		finally
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	
	public String put(String servico, String json) throws MyException
	{
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		
		if (servico == null)
			servico = "";
		
		HttpPut request = new HttpPut(url + servico);
		BufferedReader rd = null;
		try
		{
			if (json != null)
			{
				request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				
				HttpEntity entity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
				request.setEntity(entity);	
			}
			
			HttpResponse response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
			
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null)
			{
				result.append(line);
			}
			
			return result.toString();
			
		}
		catch (ClientProtocolException e)
		{
			throw new MyException("Erro de conexão com o servidor", e);
		}
		catch (IOException e)
		{
			throw new MyException("Erro de conexao com o servidor",e);
		}
		finally
		{
			try
			{
				client.close();
				
				if (rd != null)
					rd.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	private CredentialsProvider getProvider(String user, String pass ) {
		
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pass);
		provider.setCredentials(AuthScope.ANY, credentials);
		
		return provider;
	}
	
}
