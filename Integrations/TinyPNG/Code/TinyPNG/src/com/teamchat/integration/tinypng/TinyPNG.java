package com.teamchat.integration.tinypng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;

/**
 * Servlet implementation class TinyPNG
 */
@WebServlet("/TinyPNG")
public class TinyPNG extends HttpServlet{
	Properties configProps;
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TinyPNG() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		configProps = loadPropertyFromClasspath("tinypng-config.properties", TinyPNGBot.class);
		String roomId = request.getParameter("roomid");

		// checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
		    PrintWriter writer = response.getWriter();
		    writer.println("Request does not contain upload data");
		    writer.flush();
		    return;
		}
		
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		int thresholdSize = 1024 * 1024 * 3;  // 3MB
		factory.setSizeThreshold(thresholdSize);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		int maxFileSize = 1024 * 1024 * 40; // 40MB
		upload.setFileSizeMax(maxFileSize);
		int maxRequestSize   = 1024 * 1024 * 50; // 50MB
		upload.setSizeMax(maxRequestSize);
		
		// constructs the directory path to store upload file
		String uploadPath = configProps.getProperty("directory");
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
		    uploadDir.mkdir();
		}
		
		try {
            // parses the request's content to extract file data
            @SuppressWarnings("rawtypes")
			List formItems = upload.parseRequest(request);
            @SuppressWarnings("rawtypes")
			Iterator iter = formItems.iterator();
            String fileName = "";
            
            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    String filePath = uploadPath + fileName;
                    File storeFile = new File(filePath);
                    // saves the file on disk
                    item.write(storeFile);
                }
            }
            
            final String key = configProps.getProperty("apikey");
            final String input = uploadPath + File.separator + fileName;
            final String nFileName = fileName.substring(0, fileName.indexOf(".")) + "_compressed" + fileName.substring(fileName.indexOf("."));
            final String output = uploadPath + File.separator + nFileName;

            HttpURLConnection connection = (HttpURLConnection) new URL(configProps.getProperty("apiurl")).openConnection();
            String auth = DatatypeConverter.printBase64Binary(("api:" + key).getBytes("UTF-8"));
            connection.setRequestProperty("Authorization", "Basic " + auth);
            connection.setDoOutput(true);

            try (OutputStream requesta = connection.getOutputStream()) {
              Files.copy(Paths.get(input), requesta);
            }

            if (connection.getResponseCode() == 201) {
              // Compression was successful, retrieve output from location header.
              final String url = connection.getHeaderField("location");
              connection = (HttpURLConnection) new URL(url).openConnection();
              try (InputStream responsea = connection.getInputStream()) {
                Files.copy(responsea, Paths.get(output), StandardCopyOption.REPLACE_EXISTING);
              }
            } else {
              // Something went wrong! You can parse the JSON body for details.
              System.out.println("Compression failed.");
            }
            //Upload has been done successfully!
            request.setAttribute("message", "Compressed Successfully, Go to teamchat");
            
            TeamchatAPI api = WebAppTeamChatAPI
					.getTeamchatAPIInstance(getServletConfig());
			api.perform(api
					.context()
					.byId(roomId)
					.post(new PrimaryChatlet()
							.setQuestionHtml("<div align='center'><a href='" + configProps.getProperty("filepath") + nFileName + "' target='_blank'><img src ='" + configProps.getProperty("filepath") + nFileName + "' width=200px height=100px/></a></div>")));
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }
        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
	}
	
	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}
