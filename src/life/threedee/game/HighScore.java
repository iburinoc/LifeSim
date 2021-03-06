package life.threedee.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for interfacing with the High Score board over the Internet.
 *
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class HighScore {

    /**
     * Gets the high scores from the server.
     * @return The list of the highest scorers and their scores.
     */
	public static List<String> getHighScores() {
		try {
			URL url = new URL(GameUtilities.CONNECT_URL);
			HttpURLConnection c = (HttpURLConnection)  url.openConnection();

			c.setConnectTimeout(4000);
			c.setRequestMethod("GET");
			int i = c.getResponseCode();
			if(i == 200) {
				String s = (new BufferedReader(new InputStreamReader(
						c.getInputStream()))).readLine();
				System.out.println(s);
				if(s != null) {
					List<String> high = new ArrayList<String>();
					for(String d : s.split(";")) {
						high.add(d.substring(0, d.indexOf(',')) + ": " + d.substring(d.indexOf(',') + 1));
					}
					return high;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Posts a high score to the high score board.
	 * @param name The name of the user who got the high score.
	 * @param score Their score.
	 */
	public static void postHighScores(String name, int score) {
		try{
			URL url = new URL(GameUtilities.CONNECT_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setConnectTimeout(4000);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			writer.write("{\"name\":\"" + name + "\", \"score\":" + score + " }");
			writer.flush();

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			writer.close();
			reader.close();
		}
		catch(MalformedURLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
