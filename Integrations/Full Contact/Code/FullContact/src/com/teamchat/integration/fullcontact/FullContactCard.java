package com.teamchat.integration.fullcontact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


//import com.fullcontact.api.libs.fullcontact4j.FullContact;
//import com.fullcontact.api.libs.fullcontact4j.http.cardreader.CardReaderFullResponse;
//import com.fullcontact.api.libs.fullcontact4j.http.cardreader.CardReaderUploadConfirmResponse;
//import com.fullcontact.api.libs.fullcontact4j.http.cardreader.CardReaderUploadRequest;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;
import com.teamchat.integration.fullcontact.utils.Utility;

/**
 * Servlet implementation class TinyPNG
 */
@WebServlet("/FullContactCard")
public class FullContactCard extends HttpServlet {
	Properties configProps;
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FullContactCard() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject(getBody(request));
		String roomId = request.getParameter("room");
		
		int whiteSmoke = 0;
		
		String show = Utility.show;
		String start = Utility.start;
		String div = Utility.div;
		String startTable = Utility.startTable;
		String rowWhite = Utility.rowWhite;
		String rowSmoke = Utility.rowSmoke;
		String end = Utility.end;

		show += start;
		System.out.println(roomId);
		System.out.println(json.toString());

		try {
			String vCardUrl = "";
			if (json.has("vCardUrl") && !json.isNull("vCardUrl")) {
				vCardUrl = json.getString("vCardUrl");
				String divAdd = div.replace("__aa", "<a href='" + vCardUrl + "' target='_blank'>vCardUrl</a>");
				show += divAdd;
			}

			show += startTable;

			JSONObject contact = new JSONObject();
			if (json.has("contact")) {
				if (!json.isNull("contact")) {
					contact = json.getJSONObject("contact");

					if (contact.has("name")) {
						JSONObject name = new JSONObject();

						if (!contact.isNull("name")) {
							name = contact.getJSONObject("name");

							String givenName = "";
							String familyName = "";

							if (!name.isNull("givenName")) {
								givenName = name.getString("givenName");
							}
							if (!name.isNull("familyName")) {
								familyName = name.getString("familyName");
							}

							System.out.println(givenName);
							System.out.println(familyName);

							if (whiteSmoke == 0 && !givenName.equals("")
									&& !familyName.equals("")) {
								String rowWhiteAdd = rowWhite.replace("__aa",
										"Name");
								rowWhiteAdd = rowWhiteAdd.replace("__bb",
										givenName + " " + familyName);
								show += rowWhiteAdd;
								whiteSmoke = 1;
							} else if (whiteSmoke == 1 && !givenName.equals("")
									&& !familyName.equals("")) {
								String rowSmokeAdd = rowSmoke.replace("__aa",
										"Name");
								rowSmokeAdd = rowSmokeAdd.replace("__bb",
										givenName + " " + familyName);
								show += rowSmokeAdd;
								whiteSmoke = 0;
							}
						}
					}

					if (contact.has("photos")) {
						String vCardImage = "";

						if (!contact.isNull("photos")) {
							if (!contact.getJSONArray("photos")
									.getJSONObject(0).isNull("value")) {
								vCardImage = contact.getJSONArray("photos")
										.getJSONObject(0).getString("value");
							}

							System.out.println(vCardImage);

							if (whiteSmoke == 0 && !vCardImage.equals("")) {
								String rowWhiteAdd = rowWhite.replace("__aa",
										"VCard Image");
								rowWhiteAdd = rowWhiteAdd.replace("__bb", "<a href='" +
										vCardImage + "' target='_blank'>vCardImage</a>");
								show += rowWhiteAdd;
								whiteSmoke = 1;
							} else if (whiteSmoke == 1
									&& !vCardImage.equals("")) {
								String rowSmokeAdd = rowSmoke.replace("__aa",
										"VCard Image");
								rowSmokeAdd = rowSmokeAdd.replace("__bb", "<a href='" +
										vCardImage + "' target='_blank'>vCardImage</a>");
								show += rowSmokeAdd;
								whiteSmoke = 0;
							}
						}
					}

					if (contact.has("urls")) {
						String url = "";
						String type = "";

						if (!contact.isNull("urls")) {
							if (!contact.getJSONArray("urls").getJSONObject(0)
									.isNull("value")) {
								url = contact.getJSONArray("urls")
										.getJSONObject(0).getString("value");
							}
							if (!contact.getJSONArray("urls").getJSONObject(0)
									.isNull("type")) {
								type = contact.getJSONArray("urls")
										.getJSONObject(0).getString("type");

								if (type.equals("Company")) {
									if (whiteSmoke == 0) {
										String rowWhiteAdd = rowWhite.replace(
												"__aa", "Company Url");
										rowWhiteAdd = rowWhiteAdd.replace("__bb", "<a href='" +
												url + "' target='_blank'>" + url + "</a>");
										show += rowWhiteAdd;
										whiteSmoke = 1;
									} else if (whiteSmoke == 1) {
										String rowSmokeAdd = rowSmoke.replace(
												"__aa", "Company Url");
										rowSmokeAdd = rowSmokeAdd.replace("__bb", "<a href='" +
												url + "' target='_blank'>" + url + "</a>");
										show += rowSmokeAdd;
										whiteSmoke = 0;
									}
								}
							}
						}

						System.out.println(url);
					}

					if (contact.has("organizations")) {
						String title = "";
						String name = "";

						if (!contact.isNull("organizations")) {
							if (!contact.getJSONArray("organizations")
									.getJSONObject(0).isNull("title"))
								title = contact.getJSONArray("organizations")
										.getJSONObject(0).getString("title");
							if (!contact.getJSONArray("organizations")
									.getJSONObject(0).isNull("name"))
								name = contact.getJSONArray("organizations")
										.getJSONObject(0).getString("name");

							System.out.println(name);
							System.out.println(title);

							if (whiteSmoke == 0 && !title.equals("")
									&& !name.equals("")) {
								String rowWhiteAdd = rowWhite.replace("__aa",
										"Organization Name");
								rowWhiteAdd = rowWhiteAdd.replace("__bb", name);
								show += rowWhiteAdd;
								whiteSmoke = 1;
							} else if (whiteSmoke == 1 && !title.equals("")
									&& !name.equals("")) {
								String rowSmokeAdd = rowSmoke.replace("__aa",
										"Organization Name");
								rowSmokeAdd = rowSmokeAdd.replace("__bb", name);
								show += rowSmokeAdd;
								whiteSmoke = 0;
							}

							if (whiteSmoke == 0 && !title.equals("")
									&& !name.equals("")) {
								String rowWhiteAdd = rowWhite.replace("__aa",
										"Title");
								rowWhiteAdd = rowWhiteAdd.replace("__bb", title);
								show += rowWhiteAdd;
								whiteSmoke = 1;
							} else if (whiteSmoke == 1 && !title.equals("")
									&& !name.equals("")) {
								String rowSmokeAdd = rowSmoke.replace("__aa",
										"Title");
								rowSmokeAdd = rowSmokeAdd.replace("__bb", title);
								show += rowSmokeAdd;
								whiteSmoke = 0;
							}
						}
					}

					if (contact.has("emails")) {
						String email = "";
						String type = "";

						if (!contact.isNull("emails")) {
							if (!contact.getJSONArray("value").getJSONObject(0)
									.isNull("value"))
								email = contact.getJSONArray("value")
										.getJSONObject(0).getString("value");
							if (!contact.getJSONArray("value").getJSONObject(0)
									.isNull("type")) {
								type = contact.getJSONArray("emails")
										.getJSONObject(0).getString("type");
								if (type.equals("Work")) {
									if (whiteSmoke == 0) {
										String rowWhiteAdd = rowWhite.replace(
												"__aa", "Work Email");
										rowWhiteAdd = rowWhiteAdd.replace("__bb",
												email);
										show += rowWhiteAdd;
										whiteSmoke = 1;
									} else if (whiteSmoke == 1) {
										String rowSmokeAdd = rowSmoke.replace(
												"__aa", "Work Email");
										rowSmokeAdd = rowSmokeAdd.replace("__bb",
												email);
										show += rowSmokeAdd;
										whiteSmoke = 0;
									}
								}
							}
							System.out.println(type);
							System.out.println(email);
						}
					}

					if (contact.has("phoneNumbers")) {
						String number = "";
						String type = "";

						if (!contact.isNull("phoneNumbers")) {
							if (!contact.getJSONArray("phoneNumbers")
									.getJSONObject(0).isNull("value"))
								number = contact.getJSONArray("phoneNumbers")
										.getJSONObject(0).getString("value");
							if (!contact.getJSONArray("phoneNumbers")
									.getJSONObject(0).isNull("type")) {
								type = contact.getJSONArray("phoneNumbers")
										.getJSONObject(0).getString("type");

								if (type.equals("Mobile")) {
									if (whiteSmoke == 0) {
										String rowWhiteAdd = rowWhite.replace(
												"__aa", "Mobile Number");
										rowWhiteAdd = rowWhiteAdd.replace("__bb",
												number);
										show += rowWhiteAdd;
										whiteSmoke = 1;
									} else if (whiteSmoke == 1) {
										String rowSmokeAdd = rowSmoke.replace(
												"__aa", "Mobile Number");
										rowSmokeAdd = rowSmokeAdd.replace("__bb",
												number);
										show += rowSmokeAdd;
										whiteSmoke = 0;
									}
								}
							}
						}

						System.out.println(number);
						System.out.println(type);
					}

					if (contact.has("addresses")) {
						if (!contact.isNull("addresses")) {
							JSONObject address = contact.getJSONArray("addresses")
									.getJSONObject(0);
							String type = "";
							String addresss = "";

							if (address.has("type")) {
								if (!address.isNull("type"))
									type = address.getString("type");
							}
							if (address.has("streetAddress")) {
								if (!address.isNull("streetAddress")) {
									String streetAddress = address
											.getString("streetAddress");
									addresss += streetAddress + ", ";
									System.out.println(streetAddress);
								}
							}
							if (address.has("locality")) {
								if (!address.isNull("locality")) {
									String locality = address
											.getString("locality");
									addresss += locality + ", ";
								}
							}
							if (address.has("region")) {
								if (!address.isNull("region")) {
									String region = address.getString("region");
									addresss += region + ", ";
									System.out.println(region);
								}
							}
							if (address.has("country")) {
								if (!address.isNull("country")) {
									String country = address
											.getString("country");
									addresss += country;

									System.out.println(country);
								}
							}

							if (type != null && type.equals("Work")) {
								if (whiteSmoke == 0) {
									String rowWhiteAdd = rowWhite.replace(
											"__aa", "Work Address");
									rowWhiteAdd = rowWhiteAdd.replace("__bb",
											addresss);
									show += rowWhiteAdd;
									whiteSmoke = 1;
								} else if (whiteSmoke == 1) {
									String rowSmokeAdd = rowSmoke.replace(
											"__aa", "Work Address");
									rowSmokeAdd = rowSmokeAdd.replace("__bb",
											addresss);
									show += rowSmokeAdd;
									whiteSmoke = 0;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Message  " + e.getMessage());
		}

		show += end;
		TeamchatAPI api = WebAppTeamChatAPI
				.getTeamchatAPIInstance(getServletConfig());
		api.perform(api.context().byId(roomId)
				.post(new PrimaryChatlet().setQuestionHtml(show)));
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}
}