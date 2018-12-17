import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class CgDelete extends JFrame {

	private JPanel contentPane;
	private JTextField textField_url;
	private JTextField textField_at;
	private JTextField textField_author;
	private JLabel lblUpdateYourWebsite;
	private JLabel lblWebsiteUrl_1;
	private JTextField textField_searchurl;
	private JButton btnSearch;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CgDelete frame = new CgDelete();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CgDelete() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 335);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 51, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWebsiteUrl = new JLabel("Website URL:");
		lblWebsiteUrl.setForeground(new Color(255, 255, 255));
		lblWebsiteUrl.setBounds(20, 116, 107, 14);
		contentPane.add(lblWebsiteUrl);
		
		JLabel lblWebsiteArticleTitle = new JLabel("Website Article Title:");
		lblWebsiteArticleTitle.setForeground(new Color(255, 255, 255));
		lblWebsiteArticleTitle.setBounds(20, 143, 129, 14);
		contentPane.add(lblWebsiteArticleTitle);
		
		JLabel lblWebsiteAuthor = new JLabel("Website Author:");
		lblWebsiteAuthor.setForeground(new Color(255, 255, 255));
		lblWebsiteAuthor.setBounds(20, 170, 107, 14);
		contentPane.add(lblWebsiteAuthor);
		
		textField_url = new JTextField();
		textField_url.setBounds(161, 113, 242, 20);
		contentPane.add(textField_url);
		textField_url.setColumns(10);
		
		textField_at = new JTextField();
		textField_at.setBounds(161, 140, 242, 20);
		contentPane.add(textField_at);
		textField_at.setColumns(10);
		
		textField_author = new JTextField();
		textField_author.setBounds(161, 167, 242, 20);
		contentPane.add(textField_author);
		textField_author.setColumns(10);
		
		JButton btnSave = new JButton("DELETE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "deleteWebsite"));
						params.add(new BasicNameValuePair("websiteURL", textField_url.getText()));
						params.add(new BasicNameValuePair("websiteARTICLETITLE", textField_at.getText()));
						params.add(new BasicNameValuePair("websiteAUTHOR", textField_author.getText()));
				
						String strUrl = "http://localhost/apacg/apacgWebService.php";
						JSONObject jsonObj = makeHttpRequest(strUrl, "POST", params);
				
						try {
							JOptionPane.showMessageDialog(contentPane, jsonObj.getString("respond"));
						} catch (HeadlessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					public JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jObj = null;
						
						try {
							if(method == "POST") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(strUrl);
								httpPost.setEntity(new UrlEncodedFormEntity(params));
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							else if(method == "GET") {
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								strUrl+="?"+paramString;
								HttpGet httpGet = new HttpGet(strUrl);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jObj = new JSONObject(json);
							
						}	catch(JSONException e) {
							try {
								JSONArray jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jObj;
					}
				});
				thread.start();
			
			}
		});
		btnSave.setBounds(161, 200, 242, 23);
		contentPane.add(btnSave);
		
		lblUpdateYourWebsite = new JLabel("Website details:");
		lblUpdateYourWebsite.setForeground(new Color(255, 255, 255));
		lblUpdateYourWebsite.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUpdateYourWebsite.setBounds(20, 89, 198, 14);
		contentPane.add(lblUpdateYourWebsite);
		
		lblWebsiteUrl_1 = new JLabel("Website URL:");
		lblWebsiteUrl_1.setForeground(new Color(255, 255, 255));
		lblWebsiteUrl_1.setBounds(20, 13, 140, 14);
		contentPane.add(lblWebsiteUrl_1);
		
		textField_searchurl = new JTextField();
		textField_searchurl.setBounds(20, 40, 282, 20);
		contentPane.add(textField_searchurl);
		textField_searchurl.setColumns(10);
		
		btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "searchURL"));
						params.add(new BasicNameValuePair("websiteURL", textField_searchurl.getText()));
				
						String strUrl = "http://localhost/apacg/apacgWebService.php";
						JSONObject jsonObj = makeHttpRequest(strUrl, "POST", params);
				
						try {
							String weburl = jsonObj.getString("websiteURL");
							String webat = jsonObj.getString("websiteARTICLETITLE");
							String webauthor = jsonObj.getString("websiteAUTHOR");
							
							textField_url.setText(weburl);
							textField_at.setText(webat);
							textField_author.setText(webauthor);
					
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONObject jobj = null;
						//making HTTP request
						
						try {
							//check for request method
							if(method == "POST") {
								//request method is POST
								//defaultHttpClient
								DefaultHttpClient httpClient = new DefaultHttpClient();
								HttpPost httpPost = new HttpPost(url);
								httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));
								
								HttpResponse httpResponse = httpClient.execute(httpPost);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							
							} else if(method == "GET") {
								//request method is GET
								DefaultHttpClient httpClient = new DefaultHttpClient();
								String paramString = URLEncodedUtils.format(params, "utf-8");
								url += "?" + paramString;
								HttpGet httpGet = new HttpGet(url);
								
								HttpResponse httpResponse = httpClient.execute(httpGet);
								HttpEntity httpEntity = httpResponse.getEntity();
								is = httpEntity.getContent();
							}
							
							BufferedReader reader = new BufferedReader (new InputStreamReader(is, "iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							
									while((line = reader.readLine()) != null) {
										sb.append(line + "\n");
									}
									is.close();
									json = sb.toString();
									
									jobj = new JSONObject(json);
						}
						catch (JSONException e)
						{
							try {
								JSONArray jsnArr = new JSONArray(json);
								jobj = jsnArr.getJSONObject(0);
							}
							catch(JSONException e1)
							{
								e1.printStackTrace();
							}	
						} catch (Exception ee)
						{
							ee.printStackTrace();
						}
						return jobj;
					}
				});
				thread.start();
			
			
			
			}
		});
		btnSearch.setBounds(314, 39, 89, 23);
		contentPane.add(btnSearch);
		
		btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton.setBounds(306, 253, 97, 25);
		contentPane.add(btnNewButton);
	}

}
