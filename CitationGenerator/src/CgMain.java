import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextArea;

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

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class CgMain {

	private JFrame frame;
	private JTextField textField_search;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CgMain window = new CgMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CgMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(51, 0, 51));
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setBounds(100, 100, 466, 314);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnCreate = new JButton("CREATE");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CgCreate cgcreate = new CgCreate();
				cgcreate.setVisible(true);
			}
		});
		btnCreate.setBounds(10, 73, 122, 23);
		frame.getContentPane().add(btnCreate);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CgUpdate cgupdate = new CgUpdate();
				cgupdate.setVisible(true);
			}
		});
		btnUpdate.setBounds(10, 109, 122, 23);
		frame.getContentPane().add(btnUpdate);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CgDelete cgdelete = new CgDelete();
				cgdelete.setVisible(true);
			}
		});
		btnDelete.setBounds(10, 145, 122, 23);
		frame.getContentPane().add(btnDelete);
		
		JTextArea textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBounds(144, 73, 280, 167);
		scroll.setBounds(144, 73, 280, 167);
		frame.getContentPane().add(scroll);
		
		JButton btnView = new JButton("VIEW");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new Thread(){
					public void run(){
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("selectFn", "viewDatabase"));
					
					String strUrl = "http://localhost/apacg/apacgWebService.php";
					JSONArray jsonArray = makeHttpRequest(strUrl,"POST",params);
					
					/*String strFromPHP = jsonArray.toString();
					textArea.setText(strFromPHP);*/
					
					JSONObject jsonObj = null;
					String strSetText="";
					
					for(int i = 0;i<jsonArray.length();i++) {
						try {
							jsonObj = jsonArray.getJSONObject(i);
							String webURL = jsonObj.getString("websiteURL");
							String webAT = jsonObj.getString("websiteARTICLETITLE");
							String webAuthor = jsonObj.getString("websiteAUTHOR");
							
							strSetText = "URL: " + webURL + "\nArticle Title: " + webAT + "\nAuthor: " + webAuthor+"\n\n";
							
							textArea.append(strSetText);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

				}
				
					public JSONArray makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
						InputStream is = null;
						String json = "";
						JSONArray jArr = null;
						
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
							jArr = new JSONArray(json);
							
						}	catch(JSONException e) {
							try {
								jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jArr;
					}
			};	
			thread.start();
			
			}
		});
		btnView.setBounds(10, 181, 122, 23);
		frame.getContentPane().add(btnView);
		
		textField_search = new JTextField();
		textField_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				Thread thread = new Thread (new Runnable()
				{	
					public void run()
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("selectFn", "searchURL"));
						params.add(new BasicNameValuePair("websiteURL", textField_search.getText()));
				
						String strUrl = "http://localhost/apacg/apacgWebService.php";
						JSONObject jsonObj = makeHttpRequest(strUrl, "POST", params);
				
						try {
							String wURL = jsonObj.getString("websiteURL");
							String wArticleTitle = jsonObj.getString("websiteARTICLETITLE");
							String wAuthor = jsonObj.getString("websiteAUTHOR");
					
							String strSetText = "Website URL : "+wURL+" \nWebsite Article Title : "+wArticleTitle+" \nWebsite Author : "+wAuthor;
							textArea.setText(strSetText);
							
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
		textField_search.setBounds(144, 32, 280, 20);
		frame.getContentPane().add(textField_search);
		textField_search.setColumns(10);
		
		JLabel lblSearchByUrl = new JLabel("Search by URL:");
		lblSearchByUrl.setForeground(Color.WHITE);
		lblSearchByUrl.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSearchByUrl.setBounds(144, 13, 252, 14);
		frame.getContentPane().add(lblSearchByUrl);
		
		JButton btnGenerate = new JButton("GENERATE");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CgGenerate cggenerate = new CgGenerate();
				cggenerate.setVisible(true);
			}
		});
		btnGenerate.setBounds(10, 217, 122, 23);
		frame.getContentPane().add(btnGenerate);
	}
}
