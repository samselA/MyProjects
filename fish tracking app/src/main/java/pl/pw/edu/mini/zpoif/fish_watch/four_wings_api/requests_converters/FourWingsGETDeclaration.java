package pl.pw.edu.mini.zpoif.fish_watch.four_wings_api.requests_converters;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import pl.pw.edu.mini.zpoif.fish_watch.constant_info_holders.ClientInfo;

public class FourWingsGETDeclaration {
	private String changedURL;
	
	public FourWingsGETDeclaration(String URLToChange, int z, int x, int y) {
		
		changedURL= URLToChange.replace("{z}", Integer.toString(z))
				.replace("{x}", Integer.toString(x))
				.replace("{y}", Integer.toString(y));
	}

	/**
	 * @return returns changed URL
	 */
	
	public String getChangedURL() {
		return changedURL;
	}
	
	/**
	 * @param httpRquest, request to get HTTP response
	 * @return returns HTTP response, which might be later used to save object/objects
	 */
	
	public HttpResponse<byte[]> sendGETRequest(HttpRequest httpRquest){
		
		HttpResponse<byte[]> response = null;
		
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			response = httpClient.send(httpRquest, HttpResponse.BodyHandlers.ofByteArray());
		
		}catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * @return returns HTTP, GET request
	 */
	
	public HttpRequest createGETRequest(){

		HttpRequest getRequest = null;

		try {
			URI uri= new URI(changedURL);

			getRequest = HttpRequest.newBuilder()
		               .uri(uri)
		                .header("Authorization", "Bearer " + ClientInfo.getApiToken())
		                .GET()
		                .build();

		} catch (URISyntaxException e) {
			e.printStackTrace();

		}

		return getRequest;
	}
	
	/**
	 * @param responseBytes, bytes from server to be saved as PNG
	 * @param pngName, name of the saving PNG
	 */

	public void savePNG(byte[] responseBytes, String pngName) {
		// Use a relative path from your project root
		String directoryPath = "src\\main\\resources";
		String filePath = directoryPath + "\\" + pngName;

		try {
			// Ensure the directory exists
			Path directory = Paths.get(directoryPath);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}
			System.out.println(filePath);
			// Write the file
			Path file = Paths.get(filePath);
			Files.write(file, responseBytes, StandardOpenOption.CREATE);

			System.out.println("PNG file saved successfully.");
//			openPNG(pngName);

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error saving the PNG file.");
		}
	}

	public void openPNG(String pngName) {
		String directoryPath = "src\\main\\resources\\img";
		String filePath = directoryPath + "\\" + pngName;

		try {
			File file = new File(filePath).getAbsoluteFile();
			System.out.println(file.getAbsoluteFile());
			if (file.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
				} else {
					System.err.println("Desktop is not supported on this platform.");
				}
			} else {
				System.err.println("File does not exist: " + filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error opening the PNG file.");
		}
	}
	
}
